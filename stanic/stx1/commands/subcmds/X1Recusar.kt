package stanic.stx1.commands.subcmds

import hazae41.minecraft.kotlin.bukkit.msg
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import stanic.stx1.Main
import stanic.stx1.utils.Messages

class X1Recusar {

    fun run(p: Player, args: Array<out String>) {
        if (args.size < 2) {
            p.msg(Messages().get("ErrosMsg", "semArgsRecusar"))
        } else {
            val pp = Bukkit.getPlayer(args[1])
            if (pp != null) {
                if (Main.instance!!.convite.containsKey(pp.name)) {
                    Main.instance!!.convite.remove(p.name, pp.name)
                    Main.instance!!.preX1.remove(p)
                    Main.instance!!.preX1.remove(pp)
                    for (players in Bukkit.getOnlinePlayers()) {
                        for (msg in Main.settings!!.getStringList("Mensagens.recusouPlayerEveryone"))
                            players.msg(
                                msg.replace(
                                    "&", "§".replace(
                                        "{player1}",
                                        p.name
                                    ).replace("{player2}", pp.name)
                                )
                            )
                    }
                    p.msg(
                        Messages().get("recusouPlayer").replace(
                            "{player2}",
                            pp.name
                        )
                    )
                }
            }
        }
    }

}