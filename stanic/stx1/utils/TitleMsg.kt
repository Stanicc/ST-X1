package stanic.stx1.utils

import net.minecraft.server.v1_8_R3.IChatBaseComponent
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player

class TitleMsg {

    fun send(title: String, subtitle: String, p: Player) {
        val connection = (p as CraftPlayer).handle.playerConnection
        val titleJSON = IChatBaseComponent.ChatSerializer.a("{'text': '$title'}")
        val subtitleJSON = IChatBaseComponent.ChatSerializer.a("{'text': '$subtitle'}")
        val titlePacket = PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleJSON, 1, 25, 25)
        val subtitlePacket = PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitleJSON)
        connection.sendPacket(titlePacket)
        connection.sendPacket(subtitlePacket)
    }

}