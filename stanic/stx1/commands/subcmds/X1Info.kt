package stanic.stx1.commands.subcmds

import hazae41.minecraft.kotlin.bukkit.msg
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import stanic.stx1.utils.Menus
import stanic.stx1.utils.Messages

class X1Info {

    fun run(p: Player, args: Array<out String>) {
        if (args.size < 2) {
            p.msg(Messages().get("ErrosMsg", "semArgsInfo"))
        } else {
            val pp = Bukkit.getPlayer(args[1])
            if (pp != null) {
                Menus().openInfo(p, pp)
            } else {
                p.msg(Messages().get("pessoaOffline"))
            }
        }
    }

}