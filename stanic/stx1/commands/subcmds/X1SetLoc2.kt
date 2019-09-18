package stanic.stx1.commands.subcmds

import hazae41.minecraft.kotlin.bukkit.msg
import org.bukkit.entity.Player
import stanic.stx1.Main
import stanic.stx1.utils.LocUtils
import stanic.stx1.utils.Messages

class X1SetLoc2 {

    fun run(p: Player) {
        if (p.hasPermission(Main.settings!!.getString("Config.permissao"))) {
            LocUtils().salvar(p, "Loc2")
            p.msg(Messages().get("localSetado").replace("{local}", "Loc2"))
        } else {
            Messages().semPerm(p)
        }
    }

}