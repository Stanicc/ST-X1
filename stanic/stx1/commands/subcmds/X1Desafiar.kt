package stanic.stx1.commands.subcmds

import hazae41.minecraft.kotlin.bukkit.msg
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import stanic.stx1.Main
import stanic.stx1.apis.Vault
import stanic.stx1.utils.Messages

class X1Desafiar {

    fun run(p: Player, args: Array<out String>) {
        if (args.size < 2) {
            p.msg(Messages().get("ErrosMsg", "semArgsDesafiar"))
        } else {
            val pp = Bukkit.getPlayer(args[1])
            if (pp != null) {
                if (p != pp) {
                    if (!Main.instance!!.inX1.containsKey(pp.name) || !Main.instance!!.inX1Bot.containsKey(pp.name)) {
                        if (!Main.instance!!.preX1.contains(pp)) {
                            if (!Main.instance!!.preX1.contains(p)) {
                                if (Vault.econ!!.getBalance(p) < Main.settings!!.getInt("Config.custo")) {
                                    p.msg(Messages().get("ErrosMsg", "semMoneyPlayer"))
                                    return
                                }
                                if (Vault.econ!!.getBalance(pp) < Main.settings!!.getInt("Config.custo")) {
                                    p.msg(Messages().get("ErrosMsg", "semMoneyPlayer2"))
                                    return
                                }
                                Main.instance!!.convite[p.name] = pp.name
                                Main.instance!!.preX1.add(p)
                                Main.instance!!.preX1.add(pp)
                                for (players in Bukkit.getOnlinePlayers()) {
                                    for (msg in Main.settings!!.getStringList("Mensagens.conviteEnviadoEveryone"))
                                        players.msg(
                                            msg.replace("&", "§").replace(
                                                "{player1}",
                                                p.name
                                            ).replace("{player2}", pp.name)
                                        )
                                }
                                p.msg(Messages().get("conviteSucesso").replace("{player2}", pp.name))
                                pp.msg(Messages().get("convitePlayer2"))
                                object : BukkitRunnable() {
                                    override fun run() {
                                        if (Main.instance!!.convite.contains(pp.name)) {
                                            for (players in Bukkit.getOnlinePlayers()) {
                                                for (msg in Main.settings!!.getStringList("Mensagens.recusouPlayerEveryone"))
                                                    players.msg(
                                                        msg.replace("&", "§").replace(
                                                            "{player1}",
                                                            p.name
                                                        ).replace("{player2}", pp.name)
                                                    )
                                            }
                                            p.msg(
                                                Messages().get("recusouPlayer").replace(
                                                    "{player2}",
                                                    pp.name
                                                )
                                            )
                                            Main.instance!!.preX1.remove(p)
                                            Main.instance!!.preX1.remove(pp)
                                            Main.instance!!.convite.remove(p.name, pp.name)
                                        }
                                    }
                                }.runTaskLater(Main.instance!!, Main.settings!!.getInt("Config.tempoParaAceitar") * 20L)
                            } else {
                                p.msg(Messages().get("ErrosMsg", "player1JaTemPedido"))
                            }
                        } else {
                            p.msg(Messages().get("ErrosMsg", "player2JaTemPedido"))
                        }
                    } else {
                        p.msg(Messages().get("ErrosMsg", "player2JaLutando"))
                    }
                } else {
                    p.msg(Messages().get("ErrosMsg", "siMesmo"))
                }
            } else {
                p.msg(Messages().get("ErrosMsg", "pessoaOffline"))
            }
        }
    }

}