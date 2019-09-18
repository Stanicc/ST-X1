package stanic.stx1.events

import hazae41.minecraft.kotlin.bukkit.listen
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import stanic.stx1.Main

class InventoryEvents : Listener {

    //Evento para quando clicar no inventário do /x1 info
    fun onInfoClick(m: Main) {
        m.listen<InventoryClickEvent> {
            if (it.inventory.title == Main.settings!!.getString("Config.infoGUI.guiNome").replace("&", "§"))
                it.isCancelled = true
        }
    }

}