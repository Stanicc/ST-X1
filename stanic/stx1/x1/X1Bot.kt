package stanic.stx1.x1

import com.google.common.collect.Multimap
import hazae41.minecraft.kotlin.bukkit.msg
import net.minecraft.server.v1_8_R3.AttributeModifier
import net.minecraft.server.v1_8_R3.GenericAttributes
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import stanic.stx1.Main
import stanic.stx1.apis.NPCAPI
import stanic.stx1.utils.ActionBar
import stanic.stx1.utils.LocUtils
import stanic.stx1.utils.TitleMsg
import java.util.*
import kotlin.collections.HashMap

class X1Bot(val p: Player) {

    //Criando o bot para o X1
    private val bot = NPCAPI("BOT${p.name}", LocUtils().getLoc("Loc2"))

    //Método para iniciar
    fun start() {
        //Setando algumas coisas no player para ter nenhum problema
        p.fallDistance = 0.0f
        p.health = 20.0
        p.foodLevel = 20
        p.fireTicks = 0

        //Criando a partida
        val partida = Partidas(p.name, bot.npc.name, Partidas.all.size + 1, true)

        //Setando o player e o bot nas maps
        Main.instance!!.partidas[p.name] = partida
        Main.instance!!.inX1Bot[p.name] = bot.npc

        //Colocando o player em modo de espera | Não é necessário, só achei legal por
        Main.instance!!.esperando.add(p)
        Main.instance!!.esperando.add(bot.entity)

        //Pequeno pog para fazer o bot pegar o item mais forte do inventário e setar na mão
        val itens = HashMap<ItemStack, Double>()
        val itensList = ArrayList<ItemStack>()
        for (i in p.inventory.contents) if (i != null) {
            val nbt = CraftItemStack.asNMSCopy(i)
            val map: Multimap<String, AttributeModifier> = nbt.B()
            val atr = map[GenericAttributes.ATTACK_DAMAGE.name]
            if (atr.isNotEmpty()) {
                for (a in atr) itens[i] = a.d()
            }
            if (!itens.containsKey(i)) itensList.add(i)
        }
        val sorted = itens.toList().sortedBy { (_, value) -> value }.toMap()
        for (i in sorted.entries.reversed()) bot.entity.inventory.addItem(i.key)
        for (i in itensList) bot.entity.inventory.addItem(i)

        //Deixando o player e o bot invisíveis
        for (players in Bukkit.getOnlinePlayers()) {
            if (players != p && players != bot.entity) {
                players.hidePlayer(p)
                players.hidePlayer(bot.entity)
            }
        }
        p.showPlayer(bot.entity)
        bot.entity.showPlayer(p)

        //Enviando mensagem
        for (players in Bukkit.getOnlinePlayers()) {
            for (msg in Main.settings!!.getStringList("Mensagens.iniciouX1BotEveryone")) players.msg(
                msg.replace(
                    "§",
                    "&"
                ).replace("{player1}", p.name)
            )
        }

        //Iniciando a task do modo de espera
        startTask()
    }

    private fun startTask() {
        var time = Main.settings!!.getInt("Config.delayParaIniciar")
        object : BukkitRunnable() {
            override fun run() {
                --time
                if (time != 0 && Main.settings!!.getBoolean("Config.ativarActionBar")) ActionBar().send(
                    p,
                    ChatColor.translateAlternateColorCodes(
                        '&',
                        Main.settings!!.getString("ActionBarMsgs.delayParaIniciar")
                    ).replace("{tempo}", "$time")
                )
                else {
                    Main.instance!!.esperando.remove(p)
                    Main.instance!!.esperando.remove(bot.entity)
                    bot.startCombat(p)
                    if (Main.settings!!.getBoolean("Config.ativarTitle"))
                        TitleMsg().send(
                            Main.settings!!.getString("TitleMsg.titulo").replace("&", "§"),
                            Main.settings!!.getString("TitleMsg.subtitulo").replace("&", "§"),
                            p
                        )
                    for (i in p.inventory.armorContents) if (i != null) bot.entity.inventory.armorContents =
                        p.inventory.armorContents
                    cancel()
                }
            }
        }.runTaskTimer(Main.instance!!, 3L, 20L)
    }

}