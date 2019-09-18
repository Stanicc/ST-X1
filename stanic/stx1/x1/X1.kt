package stanic.stx1.x1

import hazae41.minecraft.kotlin.bukkit.msg
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import stanic.stx1.Main
import stanic.stx1.apis.Vault
import stanic.stx1.dao.X1Player
import stanic.stx1.utils.ActionBar
import stanic.stx1.utils.LocUtils
import stanic.stx1.utils.Messages
import stanic.stx1.utils.TitleMsg

class X1(p: Player, pp: Player) {

    private val p = p.name
    private val pp = pp.name

    val player: Player
        get() = Bukkit.getPlayer(p)

    val oponente: Player
        get() = Bukkit.getPlayer(pp)

    fun start() {
        //Setando algumas coisas nos players para ter nenhum problema
        player.run {
            fallDistance = 0.0f
            health = 20.0
            foodLevel = 20
            fireTicks = 0
        }
        oponente.run {
            fallDistance = 0.0f
            health = 20.0
            foodLevel = 20
            fireTicks = 0
        }

        //Criando a partida
        val partida = Partidas(player.name, oponente.name, Partidas.all.size + 1, false)

        //Setando os players nas maps
        Main.instance!!.partidas[player.name] = partida

        //Colocando o player em modo de espera | Não é necessário, só achei legal por
        Main.instance!!.esperando.add(player)
        Main.instance!!.esperando.add(oponente)

        //Deixando os players invisíveis
        for (players in Bukkit.getOnlinePlayers()) {
            if (players != player || players != oponente) {
                players.hidePlayer(player)
                players.hidePlayer(oponente)
            }
        }
        player.showPlayer(oponente)
        oponente.showPlayer(player)

        //Iniciando a task do modo de espera
        startTask()
    }

    //Task para o modo de espera
    private fun startTask() {
        var time = Main.settings!!.getInt("Config.delayParaIniciar")
        object : BukkitRunnable() {
            override fun run() {
                --time
                if (time != 0 && Main.settings!!.getBoolean("Config.ativarActionBar")) ActionBar().send(
                    player,
                    ChatColor.translateAlternateColorCodes(
                        '&',
                        Main.settings!!.getString("ActionBarMsgs.delayParaIniciar")
                    ).replace("{tempo}", "$time")
                )
                else {
                    Main.instance!!.esperando.remove(player)
                    Main.instance!!.esperando.remove(oponente)
                    if (Main.settings!!.getBoolean("Config.ativarTitle"))
                        TitleMsg().send(
                            Main.settings!!.getString("TitleMsg.titulo").replace("&", "§"),
                            Main.settings!!.getString("TitleMsg.subtitulo").replace("&", "§"),
                            player
                        )
                    cancel()
                }
            }
        }.runTaskTimer(Main.instance!!, 3L, 20L)
    }

    //Método para parar o X1
    fun stop(motivo: String, nick: String) {
        Main.instance!!.inX1.remove(p)
        Main.instance!!.inX1.remove(pp)
        Main.instance!!.partidas[p] = Partidas(null, null, Partidas.all.size - 1, false)

        for (msg in Main.settings!!.getStringList("Mensagens.venceuEveryone"))
            for (players in Bukkit.getOnlinePlayers()) {
                players.msg(
                    msg.replace("&", "§").replace("{player1}", p).replace(
                        "{player2}",
                        pp
                    )
                )
            }
        when (motivo) {
            "quit" -> {
                if (nick == p) {
                    Vault.econ!!.depositPlayer(pp, Main.settings!!.getInt("Config.premio").toDouble())

                    Bukkit.getPlayer(pp)?.msg(
                        Messages().get("venceuPlayer").replace(
                            "{player2}",
                            p
                        ).replace("{premio}", Main.settings!!.getInt("Config.premio").toString())
                    )

                    Main.instance!!.cache[pp] = X1Player(
                        pp,
                        Main.instance!!.cache[pp]!!.jogadas + 1,
                        Main.instance!!.cache[pp]!!.venceu + 1,
                        Main.instance!!.cache[pp]!!.perdeu + 1
                    )
                    Main.instance!!.cache[p] = X1Player(
                        p,
                        Main.instance!!.cache[p]!!.jogadas + 1,
                        Main.instance!!.cache[p]!!.venceu,
                        Main.instance!!.cache[p]!!.perdeu + 1
                    )
                } else {
                    Vault.econ!!.depositPlayer(p, Main.settings!!.getInt("Config.premio").toDouble())

                    Bukkit.getPlayer(p)?.msg(
                        Messages().get("venceuPlayer").replace(
                            "{player2}",
                            pp
                        ).replace("{premio}", Main.settings!!.getInt("Config.premio").toString())
                    )

                    Main.instance!!.cache[p] = X1Player(
                        p,
                        Main.instance!!.cache[p]!!.jogadas + 1,
                        Main.instance!!.cache[p]!!.venceu + 1,
                        Main.instance!!.cache[p]!!.perdeu + 1
                    )
                    Main.instance!!.cache[pp] = X1Player(
                        pp,
                        Main.instance!!.cache[pp]!!.jogadas + 1,
                        Main.instance!!.cache[pp]!!.venceu,
                        Main.instance!!.cache[pp]!!.perdeu + 1
                    )
                }
            }
            "morreu" -> {
                if (nick == p) {
                    Vault.econ!!.depositPlayer(pp, Main.settings!!.getInt("Config.premio").toDouble())

                    Bukkit.getPlayer(pp)?.msg(
                        Messages().get("venceuPlayer").replace(
                            "{player2}",
                            p
                        ).replace("{premio}", Main.settings!!.getInt("Config.premio").toString())
                    )

                    Main.instance!!.cache[pp] = X1Player(
                        pp,
                        Main.instance!!.cache[pp]!!.jogadas + 1,
                        Main.instance!!.cache[pp]!!.venceu + 1,
                        Main.instance!!.cache[pp]!!.perdeu + 1
                    )
                    Main.instance!!.cache[p] = X1Player(
                        p,
                        Main.instance!!.cache[p]!!.jogadas + 1,
                        Main.instance!!.cache[p]!!.venceu,
                        Main.instance!!.cache[p]!!.perdeu + 1
                    )
                } else {
                    Vault.econ!!.depositPlayer(p, Main.settings!!.getInt("Config.premio").toDouble())

                    Bukkit.getPlayer(p)?.msg(
                        Messages().get("venceuPlayer").replace(
                            "{player2}",
                            pp
                        ).replace("{premio}", Main.settings!!.getInt("Config.premio").toString())
                    )

                    Main.instance!!.cache[p] = X1Player(
                        p,
                        Main.instance!!.cache[p]!!.jogadas + 1,
                        Main.instance!!.cache[p]!!.venceu + 1,
                        Main.instance!!.cache[p]!!.perdeu + 1
                    )
                    Main.instance!!.cache[pp] = X1Player(
                        pp,
                        Main.instance!!.cache[pp]!!.jogadas + 1,
                        Main.instance!!.cache[pp]!!.venceu,
                        Main.instance!!.cache[pp]!!.perdeu + 1
                    )
                }
            }
        }
        Main.instance!!.inX1.remove(p)
        Main.instance!!.inX1.remove(pp)
        for (players in Bukkit.getOnlinePlayers()) {
            for (msg in Main.settings!!.getStringList("Mensagens.venceuEveryone"))
                players.msg(
                    msg.replace("&", "§").replace(
                        "{player1}",
                        p
                    ).replace("{player2}", pp)
                )
        }
        Bukkit.getPlayer(p)?.run {
            for (players in Bukkit.getOnlinePlayers()) showPlayer(players)
            LocUtils().teleportar(this, "Saida")
        }
        Bukkit.getPlayer(pp)?.run {
            for (players in Bukkit.getOnlinePlayers()) showPlayer(players)
            LocUtils().teleportar(this, "Saida")
        }
    }

}