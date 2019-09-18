package stanic.stx1.utils

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import stanic.stx1.Main

class LocUtils {

    //Método para salvar a loc
    fun salvar(p: Player, loc: String) {
        val locp = p.location
        Main.settings!!.set("Locais.$loc.X", locp.x)
        Main.settings!!.set("Locais.$loc.Y", locp.y)
        Main.settings!!.set("Locais.$loc.Z", locp.z)
        Main.settings!!.set("Locais.$loc.YAW", locp.yaw)
        Main.settings!!.set("Locais.$loc.PITCH", locp.pitch)
        Main.settings!!.set("Locais.$loc.WORLD", locp.world.name)
        Main.settings!!.save(Main.sett!!)
    }

    //Método para teleportar
    fun teleportar(p: Player, loc: String) {
        val x = Main.settings!!.getDouble("Locais.$loc.X")
        val y = Main.settings!!.getDouble("Locais.$loc.Y")
        val z = Main.settings!!.getDouble("Locais.$loc.Z")
        val yaw = Main.settings!!.getDouble("Locais.$loc.YAW")
        val pitch = Main.settings!!.getDouble("Locais.$loc.PITCH")
        val world = Bukkit.getWorld(Main.settings!!.getString("Locais.$loc.WORLD"))
        val tloc = Location(world, x, y, z)
        tloc.yaw = yaw.toFloat()
        tloc.pitch = pitch.toFloat()
        p.teleport(tloc)
    }

    //Método para pegar a loc
    fun getLoc(loc: String): Location {
        val x = Main.settings!!.getDouble("Locais.$loc.X")
        val y = Main.settings!!.getDouble("Locais.$loc.Y")
        val z = Main.settings!!.getDouble("Locais.$loc.Z")
        val yaw = Main.settings!!.getDouble("Locais.$loc.YAW")
        val pitch = Main.settings!!.getDouble("Locais.$loc.PITCH")
        val world = Bukkit.getWorld(Main.settings!!.getString("Locais.$loc.WORLD"))
        val tloc = Location(world, x, y, z)
        tloc.yaw = yaw.toFloat()
        tloc.pitch = pitch.toFloat()
        return tloc
    }

}