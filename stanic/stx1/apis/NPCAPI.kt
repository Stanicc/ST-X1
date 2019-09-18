package stanic.stx1.apis

import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.NPC
import net.citizensnpcs.util.PlayerAnimation
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Damageable
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import stanic.stx1.Main
import kotlin.random.Random

class NPCAPI(name: String, loc: Location) {

    //Criando o NPC
    val npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name)

    init {
        //Setando skin no NPC
        npc.data().set("player-skin-name", "010987654321" as Any)
        npc.data().set("player-skin-use-latest", false as Any)

        //Verificando se ele já está spawnado | Meio improvável mas como nada é impossível...
        //Se ele já estiver spawnado o servidor vai dar despawn nele.
        if (npc.isSpawned) {
            npc.despawn()
        }

        //Spawnando o npc
        npc.spawn(loc)
    }

    //Método para pegar o npc como um player
    val entity: Player
        get() = npc.entity as Player

    companion object {
        //Método para ativar o x1 com bot
        fun enable() {
            object : BukkitRunnable() {
                override fun run() {
                    try {
                        val i = CitizensAPI.getNPCRegistry().iterator() as Iterator<NPC>
                        val remove = ArrayList<NPC>()
                        while (i.hasNext()) {
                            val npc = i.next()
                            remove.add(npc)
                        }
                        val iterator = remove.iterator()
                        while (iterator.hasNext()) {
                            val npc = iterator.next()
                            CitizensAPI.getNPCRegistry().deregister(npc)
                        }
                        Main.instance!!.citizens = true
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Bukkit.getLogger().warning("Citizens não encontrado! O X1 com BOT não irá funcionar.")
                    }
                }
            }.runTaskLater(Main.instance!!, 20L)
        }
    }

    //Método para dar despawn no bot
    fun despawn() {
        npc.despawn()
        CitizensAPI.getNPCRegistry().deregister(npc)
    }

    //Verificar se o bot está regenerando (Caso ele tenha comido uma maçã dourada)
    var regeneration = false

    //Método para iniciar o combate
    fun startCombat(p: Player) {
        //Setando algumas coisas no npc
        npc.isProtected = false
        npc.navigator.localParameters.attackRange(5.5)

        //Task de combate
        object : BukkitRunnable() {
            override fun run() {

                //Verificando se ele está spawnado, caso não esteja tudo será cancelado.
                if (!npc.isSpawned) {
                    despawn()
                    cancel()
                    return
                }

                //Verificando a distância do player e colocando-o como alvo do bot
                var distance = 2500.0
                val dis = entity.location.distanceSquared(p.location)
                if (dis < distance) distance = dis
                npc.navigator.setTarget(p, true)
                //Setando outros valores no bot
                if (npc.navigator.targetAsLocation != null) entity.isSprinting = true
                val x = 8.0 + Random.nextDouble() * 2.0
                if (distance < x * x && !npc.navigator.isPaused && !regeneration) PlayerAnimation.ARM_SWING.play(entity)

                //Método para regenerar a vida
                regeneratioLife()
            }
        }.runTaskTimer(Main.instance!!, 40L, 4L)
        //Setando a armadura no bot
        for (i in p.inventory.armorContents) if (i != null) entity.inventory.armorContents = p.inventory.armorContents
    }

    //Método para regenerar a vida
    fun regeneratioLife() {
        //Verificando se ele está spawnado, caso não esteja o método será cancelado
        if (!npc.isSpawned) {
            despawn()
            return
        }
        //Verificando se ele já está regenerando, caso esteja o método será cancelado
        if (regeneration) return

        //Verificando a vida dele
        val damageable = entity as Damageable
        //Verificando se a vida é menor que 10.0 (Metade dos corações)
        if (damageable.health <= 10.0) {
            //Fazendo uma pequena verificação para parecer mais real e fazendo o bot comer a maçã
            var x = ((10.0 - damageable.health) / 3.0).toInt()
            if (x < 1) x = 1
            if (Random.nextInt(x) > 0) useApple()
        }
    }

    //Método para comer a maçã
    private fun useApple(): Boolean {
        //Verificando se ele está spawnado, caso não esteja o método será cancelado
        if (!npc.isSpawned) {
            despawn()
            return false
        }
        //Verificando se no inventário do bot contém uma maçã dourada
        var apple: ItemStack? = null
        for (i in entity.inventory.contents) if (i != null && i.type == Material.GOLDEN_APPLE) {
            apple = i.clone()
            break
        }
        //Verificando algumas coisas e ativando a regeneração
        if (apple != null) {
            regeneration = true
            var inHand: ItemStack? = null
            for (i in 0..8) if (entity.inventory.getItem(i) != null && entity.inventory.getItem(i).type == apple) {
                inHand = entity.inventory.getItem(i)
                entity.inventory.heldItemSlot = i
                break
            }
            if (inHand == null) {
                entity.inventory.heldItemSlot = 1
                if (apple.amount > 0) apple.amount = apple.amount - 1
                else entity.inventory.remove(apple)
                for (i in 9..35) if (entity.inventory.getItem(i) == null || entity.inventory.getItem(i).type == Material.AIR) {
                    entity.inventory.setItem(i, entity.itemInHand)
                    break
                }
                entity.itemInHand = apple
            }
            //Task para iniciar o uso da maçã
            object : BukkitRunnable() {
                override fun run() {
                    //Verificando se ele está spawnado, caso não esteja o método será cancelado
                    if (!npc.isSpawned) {
                        despawn()
                        cancel()
                        return
                    }
                    //Verificando se o item que está em sua mão é uma maçã
                    entity.itemInHand = apple
                    //Iniciando a animação de comer
                    PlayerAnimation.EAT_FOOD.play(entity)
                    //Task para comer a maçã (Ficar mais realista)
                    object : BukkitRunnable() {
                        override fun run() {
                            //Verificando se ele está spawnado, caso não esteja o método será cancelado
                            if (!npc.isSpawned) {
                                despawn()
                                cancel()
                                return
                            }
                            //Setando algumas no bot e verificando o tipo de maçã para adicionar os efeitos
                            npc.navigator.isPaused = true
                            entity.itemInHand = ItemStack(Material.AIR)
                            if (apple.durability == 0.toShort()) {
                                entity.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 40, 1))
                                entity.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 2400, 1))
                            } else {
                                entity.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 2400, 1))
                                entity.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 600, 1))
                                entity.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 6000, 1))
                                entity.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 6000, 1))
                            }
                            //Removendo a maçã do inventário
                            if (apple.amount > 0) apple.amount = apple.amount - 1
                            else entity.inventory.remove(apple)
                            entity.updateInventory()
                            //Task para finalizar
                            object : BukkitRunnable() {
                                override fun run() {
                                    //Setando algumas coisas no bot e desativando a regeneração
                                    if (npc != null && npc.isSpawned && npc.navigator != null) {
                                        npc.navigator.isPaused = false
                                        entity.inventory.heldItemSlot = 0
                                        regeneration = false
                                    }
                                }
                            }.runTaskLater(Main.instance!!, (Random.nextLong(4) + 2))
                        }
                    }.runTaskLater(Main.instance!!, 35)
                }
            }.runTaskLater(Main.instance!!, (Random.nextLong(2) + 1))
            return true
        }
        return false
    }

}