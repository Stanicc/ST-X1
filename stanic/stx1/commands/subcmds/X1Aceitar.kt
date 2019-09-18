package stanic.stx1.commands.subcmds

import hazae41.minecraft.kotlin.bukkit.msg
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import stanic.stx1.Main
import stanic.stx1.apis.Vault
import stanic.stx1.utils.LocUtils
import stanic.stx1.utils.Messages
import stanic.stx1.x1.X1

class X1Aceitar {

    fun run(p: Player, args: Array<out String>) {
        if (args.size < 2) {
            p.msg(Messages().get("ErrosMsg", "semArgsAceitar"))
        } else {
            val pp = Bukkit.getPlayer(args[1])
            if (pp != null) {
                if (Main.instance!!.convite.containsKey(pp.name)) {
                    Main.instance!!.preX1.remove(p)
                    Main.instance!!.preX1.remove(pp)
                    Main.instance!!.convite.remove(p.name, pp.name)
                    val x1 = X1(p, pp)
                    x1.start()
                    Main.instance!!.inX1[p.name] = x1
                    Main.instance!!.inX1[pp.name] = x1
                    for (players in Bukkit.getOnlinePlayers()) {
                        for (msg in Main.settings!!.getStringList("Mensagens.aceitouEveryone"))
                            players.msg(
                                msg.replace("&", "§").replace(
                                    "{player1}",
                                    p.name
                                ).replace("{player2}", pp.name)
                            )
                    }
                    LocUtils().teleportar(p, "Loc1")
                    LocUtils().teleportar(pp, "Loc2")
                    Vault.econ!!.withdrawPlayer(p, Main.settings!!.getInt("Config.custo").toDouble())
                    Vault.econ!!.withdrawPlayer(pp, Main.settings!!.getInt("Config.custo").toDouble())
                    object : BukkitRunnable() {
                        override fun run() {
                            if (Main.instance!!.convite.containsKey(p.name)) {
                                Main.instance!!.convite.remove(p.name, pp.name)
                                Main.instance!!.inX1.remove(p.name)
                                Main.instance!!.inX1.remove(pp.name)
                                for (players in Bukkit.getOnlinePlayers()) {
                                    p.showPlayer(players)
                                    pp.showPlayer(players)
                                    for (msg in Main.settings!!.getStringList("Mensagens.empateEveryone"))
                                        players.msg(
                                            msg.replace("&", "§").replace(
                                                "{player2}",
                                                p.name
                                            ).replace("{player1}", pp.name)
                                        )
                                }
                                LocUtils().teleportar(p, "Saida")
                                LocUtils().teleportar(pp, "Saida")
                            }
                        }
                    }.runTaskLater(Main.instance!!, Main.settings!!.getInt("Config.tempo") * 60L * 20L)
                } else {
                    p.msg(Messages().get("ErrosMsg", "player2NaoConvidou"))
                }
            } else {
                p.msg(Messages().get("ErrosMsg", "pessoaOffline"))
            }
        }
    }

}