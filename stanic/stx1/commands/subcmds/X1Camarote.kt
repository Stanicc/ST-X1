package stanic.stx1.commands.subcmds

import hazae41.minecraft.kotlin.bukkit.msg
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import stanic.stx1.Main
import stanic.stx1.utils.LocUtils
import stanic.stx1.utils.Messages

class X1Camarote {

    fun run(p: Player, args: Array<out String>) {
        if (args.size < 2) {
            p.msg(Messages().get("ErrosMsg", "semArgsAceitar"))
        } else {
            when {
                Main.instance!!.inX1.containsKey(args[1]) -> {
                    val info = Main.instance!!.inX1[args[1]]!!
                    LocUtils().teleportar(p, "Camarote")
                    p.showPlayer(info.player)
                    p.showPlayer(info.oponente)
                    return
                }
                Main.instance!!.inX1Bot.containsKey(args[1]) -> {
                    val info = Main.instance!!.inX1Bot[args[1]]!!
                    LocUtils().teleportar(p, "Camarote")
                    p.showPlayer(Bukkit.getPlayer(args[1]))
                    p.showPlayer(info.entity as Player)
                }
                else -> {
                    p.msg(Messages().get("ErrosMsg", "x1NaoEncontrado"))
                }
            }
        }
    }

}