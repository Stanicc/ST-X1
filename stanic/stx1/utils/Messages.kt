package stanic.stx1.utils

import hazae41.minecraft.kotlin.bukkit.msg
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import stanic.stx1.Main

class Messages {

    fun semPerm(sender: CommandSender) {
        sender.msg(ChatColor.translateAlternateColorCodes('&', Main.settings!!.getString("ErrosMsg.semPermissao")))
    }

    fun get(sender: CommandSender, section: String, msg: String) {
        sender.msg(ChatColor.translateAlternateColorCodes('&', Main.settings!!.getString("$section.$msg")))
    }

    fun get(sender: CommandSender, msg: String) {
        sender.msg(ChatColor.translateAlternateColorCodes('&', Main.settings!!.getString("Mensagens.$msg")))
    }

    fun get(section: String, msg: String): String {
        return ChatColor.translateAlternateColorCodes('&', Main.settings!!.getString("$section.$msg"))
    }

    fun get(msg: String): String {
        return ChatColor.translateAlternateColorCodes('&', Main.settings!!.getString("Mensagens.$msg"))
    }

    fun getList(sender: CommandSender, section: String, msg: String) {
        for (mensagem in Main.settings!!.getStringList("$section.$msg"))
            sender.msg(ChatColor.translateAlternateColorCodes('&', mensagem))
    }

    fun getList(sender: CommandSender, msg: String) {
        for (mensagem in Main.settings!!.getStringList("Mensagens.$msg"))
            sender.msg(ChatColor.translateAlternateColorCodes('&', mensagem))
    }

}