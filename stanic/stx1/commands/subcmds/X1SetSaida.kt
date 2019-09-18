package stanic.stx1.commands.subcmds

import hazae41.minecraft.kotlin.bukkit.msg
import org.bukkit.entity.Player
import stanic.stx1.Main
import stanic.stx1.utils.LocUtils
import stanic.stx1.utils.Messages

class X1SetSaida {

    fun run(p: Player) {
        if (p.hasPermission(Main.settings!!.getString("Config.permissao"))) {
            LocUtils().salvar(p, "Saida")
            p.msg(Messages().get("localSetado").replace("{local}", "Saida"))
        } else {
            Messages().semPerm(p)
        }
    }

}