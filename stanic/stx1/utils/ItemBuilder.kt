package stanic.stx1.utils

import org.bukkit.Color
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

class ItemBuilder {

    private var itemStack: ItemStack? = null

    constructor(itemStack: ItemStack) {
        this.itemStack = itemStack
    }

    @JvmOverloads
    constructor(m: Material, quantia: Int = 1) {
        itemStack = ItemStack(m, quantia)
    }

    constructor(m: Material, quantia: Int, durabilidade: Byte) {
        itemStack = ItemStack(m, quantia, durabilidade.toShort())
    }


    constructor(m: Material, quantia: Int, durabilidade: Int) {
        itemStack = ItemStack(m, quantia, durabilidade.toShort())
    }

    fun clonar(): ItemBuilder {
        return ItemBuilder(itemStack!!)
    }

    fun setarDurabilidade(durabilidade: Short): ItemBuilder {
        itemStack!!.durability = durabilidade
        return this
    }

    fun setarQuantia(amount: Int): ItemBuilder {
        itemStack!!.amount = amount
        val im = itemStack!!.itemMeta
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)
        itemStack!!.itemMeta = im
        return this
    }

    fun setarDurabilidade(durabilidade: Int): ItemBuilder {
        itemStack!!.durability = java.lang.Short.valueOf("" + durabilidade)
        return this
    }

    fun setarNome(nome: String): ItemBuilder {
        val im = itemStack!!.itemMeta
        im.displayName = nome
        itemStack!!.itemMeta = im
        return this
    }

    fun addUnsafeEnchantment(ench: Enchantment, level: Int): ItemBuilder {
        itemStack!!.addUnsafeEnchantment(ench, level)
        return this
    }

    fun removeEnchantment(ench: Enchantment): ItemBuilder {
        itemStack!!.removeEnchantment(ench)
        return this
    }

    fun setSkullOwner(dono: String): ItemBuilder {
        try {
            val im = itemStack!!.itemMeta as SkullMeta
            im.owner = dono
            itemStack!!.itemMeta = im
        } catch (expected: ClassCastException) {
        }

        return this
    }

    fun addEnchant(ench: Enchantment, level: Int): ItemBuilder {
        val im = itemStack!!.itemMeta
        im.addEnchant(ench, level, true)
        itemStack!!.itemMeta = im
        return this
    }

    fun addEnchantments(enchantments: Map<Enchantment, Int>): ItemBuilder {
        itemStack!!.addEnchantments(enchantments)
        return this
    }

    fun setInfinityDurability(): ItemBuilder {
        itemStack!!.durability = java.lang.Short.MAX_VALUE
        return this
    }

    fun addItemFlag(flag: ItemFlag): ItemBuilder {
        val im = itemStack!!.itemMeta
        im.addItemFlags(flag)
        itemStack!!.itemMeta = im
        return this
    }

    fun setLore(vararg lore: String): ItemBuilder {
        val im = itemStack!!.itemMeta
        im.lore = listOf("Ã‚Â§f$lore")
        itemStack!!.itemMeta = im
        return this
    }

    fun setLore(lore: List<String>): ItemBuilder {
        val im = itemStack!!.itemMeta
        im.lore = lore
        itemStack!!.itemMeta = im
        return this
    }

    fun removeLoreLine(linha: String): ItemBuilder {
        val im = itemStack!!.itemMeta
        val lore = ArrayList<String>(im.lore)
        if (!lore.contains(linha)) return this
        lore.remove(linha)
        im.lore = lore
        itemStack!!.itemMeta = im
        return this
    }

    fun removeLoreLine(index: Int): ItemBuilder {
        val im = itemStack!!.itemMeta
        val lore = ArrayList<String>(im.lore)
        if (index < 0 || index > lore.size) return this
        lore.removeAt(index)
        im.lore = lore
        itemStack!!.itemMeta = im
        return this
    }

    fun addLoreLine(linha: String): ItemBuilder {
        val im = itemStack!!.itemMeta
        var lore: MutableList<String> = ArrayList()
        if (im.hasLore()) lore = ArrayList(im.lore)
        lore.add(linha)
        im.lore = lore
        itemStack!!.itemMeta = im
        return this
    }

    fun addLores(linha: List<String>): ItemBuilder {
        val im = itemStack!!.itemMeta
        var lore: MutableList<String> = ArrayList()
        if (im.hasLore()) lore = ArrayList(im.lore)
        for (s in linha) {
            lore.add(s)
        }
        im.lore = lore
        itemStack!!.itemMeta = im
        return this
    }

    fun addLoreLine(linha: String, pos: Int): ItemBuilder {
        val im = itemStack!!.itemMeta
        val lore = ArrayList<String>(im.lore)
        lore[pos] = linha
        im.lore = lore
        itemStack!!.itemMeta = im
        return this
    }

    fun setDyeColor(cor: DyeColor): ItemBuilder {
        this.itemStack!!.durability = cor.data.toShort()
        return this
    }

    @Deprecated("")
    fun setWoolColor(cor: DyeColor): ItemBuilder {
        if (itemStack!!.type == Material.WOOL)
            this.itemStack!!.durability = cor.data.toShort()
        return this
    }

    fun setLeatherArmorColor(cor: Color): ItemBuilder {
        try {
            val im = itemStack!!.itemMeta as LeatherArmorMeta
            im.color = cor
            itemStack!!.itemMeta = im
        } catch (expected: ClassCastException) {
        }

        return this
    }

    fun toItemStack(): ItemStack? {
        return itemStack
    }
}
