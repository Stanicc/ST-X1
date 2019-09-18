package stanic.stx1.events

import hazae41.minecraft.kotlin.bukkit.listen
import hazae41.minecraft.kotlin.bukkit.msg
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import stanic.stx1.Main
import stanic.stx1.dao.X1Player
import stanic.stx1.utils.Messages

class X1Events : Listener {

    //Evento para quando alguém entrar pela primeira vez
    fun onJoin(m: Main) {
        m.listen<PlayerJoinEvent> {
            if (Main.instance!!.cache.containsKey(it.player.name)) return@listen
            val x1 = X1Player(it.player.name, 0, 0, 0)
            Main.instance!!.cache[it.player.name] = x1
        }
    }

    //Evento para quando alguém sair
    fun onQuit(m: Main) {
        m.listen<PlayerQuitEvent> {
            val main = Main.instance!!
            val p = it.player
            if (main.inX1.containsKey(p.name)) {
                val x1 = main.inX1[p.name]!!
                x1.stop("quit", p.name)
                main.convite.remove(p.name)
                return@listen
            }
            if (main.inX1Bot.containsKey(p.name)) {
                main.inX1Bot[p.name]!!.despawn()
                for (players in Bukkit.getOnlinePlayers()) {
                    for (msg in Main.settings!!.getStringList("Mensagens.perdeuBotEveryone"))
                        players.msg(
                            msg.replace("&", "§").replace(
                                "{player1}",
                                p.name
                            )
                        )
                }
                main.inX1Bot.remove(p.name)
            }
        }
    }

    //Evento para não deixar a pessoa se mover enquanto está em modo de espera
    fun onMove(m: Main) {
        m.listen<PlayerMoveEvent> {
            if (Main.instance!!.esperando.contains(it.player)) it.player.teleport(it.player.location)
        }
    }

    //Evento para quando matar alguém no X1
    fun onKill(m: Main) {
        m.listen<EntityDeathEvent> {
            if (Main.instance!!.inX1Bot.containsKey(it.entity.killer.name)) {
                for (players in Bukkit.getOnlinePlayers()) {
                    for (msg in Main.settings!!.getStringList("Mensagens.venceuBotEveryone"))
                        players.msg(
                            msg.replace("&", "§").replace(
                                "{player1}",
                                it.entity.killer.name
                            )
                        )
                }
                Main.instance!!.inX1Bot.remove(it.entity.killer.name)
                return@listen
            }
            if (Main.instance!!.inX1Bot.containsKey(it.entity.name)) {
                for (players in Bukkit.getOnlinePlayers()) {
                    for (msg in Main.settings!!.getStringList("Mensagens.perdeuBotEveryone"))
                        players.msg(
                            msg.replace("&", "§").replace(
                                "{player1}",
                                it.entity.name
                            )
                        )
                }
                Main.instance!!.inX1Bot[it.entity.name]!!.despawn()
                Main.instance!!.inX1Bot.remove(it.entity.name)
                return@listen
            }
            if (Main.instance!!.inX1.containsKey(it.entity.name)) {
                val x1 = Main.instance!!.inX1[it.entity.name]!!
                x1.stop("quit", it.entity.name)
                Main.instance!!.convite.remove(it.entity.name)
                return@listen
            }
        }
    }

    fun onCommand(m: Main) {
        m.listen<PlayerCommandPreprocessEvent> {
            if (Main.instance!!.inX1.containsKey(it.player.name) || Main.instance!!.inX1Bot.containsKey(it.player.name)) {
                for (cmds in Main.settings!!.getStringList("Config.comandos")) {
                    if (!it.message.startsWith(cmds)) {
                        it.player.msg(Messages().get("ErrosMsg", "cmdBloqueado"))
                        it.isCancelled = true
                        break
                    }
                }
            }
        }
    }

}