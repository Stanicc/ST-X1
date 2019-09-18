package stanic.stx1.commands

import hazae41.minecraft.kotlin.bukkit.command
import hazae41.minecraft.kotlin.bukkit.msg
import org.bukkit.entity.Player
import stanic.stx1.Main
import stanic.stx1.commands.subcmds.*
import stanic.stx1.utils.LocUtils
import stanic.stx1.utils.Messages
import stanic.stx1.x1.X1Bot

class X1CMD {

    fun run(m: Main) {
        m.command("x1", "stx1.x1cmd", "duelo") { sender, args ->
            if (sender !is Player) {
                Messages().get(sender, "ErrosMsg", "somenteInGame")
                return@command
            }
            if (args.isEmpty()) {
                if (sender.hasPermission(Main.settings!!.getString("Config.permissao"))) Messages().getList(
                    sender,
                    "argsHelpAdmin"
                )
                else Messages().getList(sender, "argsHelpPlayer")
                return@command
            }
            when (args[0]) {
                //Admin
                "setloc1" -> X1SetLoc1().run(sender)
                "setloc2" -> X1SetLoc2().run(sender)
                "setsaida" -> X1SetSaida().run(sender)
                "setcamarote" -> X1SetCamarote().run(sender)
                //Player
                "bot" -> {
                    if (Main.instance!!.citizens) {
                        LocUtils().teleportar(sender, "Loc1")
                        val x1 = X1Bot(sender)
                        x1.start()
                    } else {
                        sender.msg("§4§lST-X1 §cO X1 com bot não está ativado pois o Citizens não está no servidor.")
                    }
                }
                "desafiar" -> X1Desafiar().run(sender, args)
                "aceitar" -> X1Aceitar().run(sender, args)
                "recusar" -> X1Recusar().run(sender, args)
                "info" -> X1Info().run(sender, args)
                "camarote" -> X1Camarote().run(sender, args)
            }
        }
    }

}