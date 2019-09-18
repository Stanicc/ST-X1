package stanic.stx1.apis

import net.milkbowl.vault.economy.Economy
import stanic.stx1.Main

object Vault {
    var econ: Economy? = null

    init {
        econ = null
    }

    fun setupEconomy(): Boolean {
        if (Main.instance!!.server.pluginManager.getPlugin("Vault") == null) {
            return false
        }
        val economia =
            Main.instance!!.server.servicesManager.getRegistration(Economy::class.java as Class<*>)
        econ = economia.provider as Economy
        return econ != null
    }
}
