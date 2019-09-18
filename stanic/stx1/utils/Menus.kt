package stanic.stx1.utils

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import stanic.stx1.Main

class Menus {

    fun openInfo(p: Player, pp: Player) {
        val ppi = Main.instance!!.cache[pp.name]!!
        val inv = Bukkit.createInventory(
            null,
            Main.settings!!.getInt("Config.infoGUI.guiSlots") * 9,
            Main.settings!!.getString("Config.infoGUI.guiNome").replace("&", "Â§")
        )

        val lorePartidas = ArrayList<String>()
        for (partidasLore in Main.settings!!.getStringList("Config.infoGUI.partidas.lore")) lorePartidas.add(
            partidasLore.replace("&", "Â§").replace("{player}", pp.name).replace("{partidas}", ppi.jogadas.toString())
        )
        inv.setItem(
            Main.settings!!.getInt("Config.infoGUI.partidas.slot"),
            ItemBuilder(ItemStack(Main.settings!!.getInt("Config.infoGUI.partidas.id"))).setarNome(
                Main.settings!!.getString("Config.infoGUI.partidas.nome").replace("&", "Â§")
            ).addLores(lorePartidas).toItemStack()
        )

        val loreVenceu = ArrayList<String>()
        for (venceuLore in Main.settings!!.getStringList("Config.infoGUI.venceu.lore")) loreVenceu.add(
            venceuLore.replace(
                "&",
                "Â§"
            ).replace("{player}", pp.name).replace("{venceu}", ppi.venceu.toString())
        )
        inv.setItem(
            Main.settings!!.getInt("Config.infoGUI.venceu.slot"),
            ItemBuilder(ItemStack(Main.settings!!.getInt("Config.infoGUI.venceu.id"))).setarNome(
                Main.settings!!.getString("Config.infoGUI.venceu.nome").replace("&", "Â§")
            ).addLores(loreVenceu).toItemStack()
        )

        val lorePerdeu = ArrayList<String>()
        for (perdeuLore in Main.settings!!.getStringList("Config.infoGUI.perdeu.lore")) lorePerdeu.add(
            perdeuLore.replace(
                "&",
                "Â§"
            ).replace("{player}", pp.name).replace("{perdeu}", ppi.perdeu.toString())
        )
        inv.setItem(
            Main.settings!!.getInt("Config.infoGUI.perdeu.slot"),
            ItemBuilder(ItemStack(Main.settings!!.getInt("Config.infoGUI.perdeu.id"))).setarNome(
                Main.settings!!.getString("Config.infoGUI.perdeu.nome").replace("&", "Â§")
            ).addLores(lorePerdeu).toItemStack()
        )

        p.openInventory(inv)
    }

}