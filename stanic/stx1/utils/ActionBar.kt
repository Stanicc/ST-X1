package stanic.stx1.utils

import net.minecraft.server.v1_8_R3.ChatComponentText
import net.minecraft.server.v1_8_R3.IChatBaseComponent
import net.minecraft.server.v1_8_R3.PacketPlayOutChat
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player

class ActionBar {

    fun send(p: Player, msg: String) {
        val packet = PacketPlayOutChat(ChatComponentText(msg) as IChatBaseComponent, 2)
        (p as CraftPlayer).handle.playerConnection.sendPacket(packet)
    }

}