package stanic.stx1

import net.citizensnpcs.api.npc.NPC
import org.bukkit.Bukkit
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import stanic.stx1.apis.NPCAPI
import stanic.stx1.apis.Vault
import stanic.stx1.commands.X1CMD
import stanic.stx1.dao.X1Player
import stanic.stx1.database.Database
import stanic.stx1.database.MySQL
import stanic.stx1.database.SQLite
import stanic.stx1.events.InventoryEvents
import stanic.stx1.events.X1Events
import stanic.stx1.x1.Partidas
import stanic.stx1.x1.X1
import java.io.File
import java.io.IOException

class Main : JavaPlugin() {

    //Cache do x1 e partidas
    val cache = HashMap<String, X1Player>()
    val partidas = HashMap<String, Partidas>()

    //Convite e delay para iniciar.
    val convite = HashMap<String, String>()
    val preX1 = ArrayList<Player>()
    val esperando = ArrayList<Player>()

    //Maps do x1
    val inX1 = HashMap<String, X1>()
    val inX1Bot = HashMap<String, NPC>()

    //Verificação para o citizens
    var citizens = false

    //onEnable
    override fun onEnable() {
        //Definindo a instance
        instance = this

        //Carregando o arquivo de configuração
        loadConfig()

        //Carregando o Vault
        Vault.setupEconomy()

        //Carregando database
        database()

        //Carregando comandos
        loadCommands()

        //Carregando eventos
        loadEvents()

        //Carregando o Citizens para o X1 com BOT
        NPCAPI.enable()

        //Fim da inicialização
        Bukkit.getConsoleSender().sendMessage("§e[ST-X1] §fAtivado!")
    }

    //onDisable
    override fun onDisable() {

    }

    //Método para carregar a config
    private fun loadConfig() {
        sett = File(dataFolder, "settings.yml")
        if (!sett!!.exists()) {
            sett!!.parentFile.mkdirs()
            saveResource("settings.yml", false)
        }
        settings = YamlConfiguration()
        try {
            try {
                settings!!.load(sett)
            } catch (e: InvalidConfigurationException) {
                e.printStackTrace()
            }
            config.options().copyDefaults(true)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //Método para carregar os comandos
    private fun loadCommands() {
        X1CMD().run(this)
    }

    //Método para carregar os eventos
    private fun loadEvents() {
        X1Events().onJoin(this)
        X1Events().onQuit(this)
        X1Events().onCommand(this)
        X1Events().onKill(this)
        X1Events().onMove(this)
        InventoryEvents().onInfoClick(this)
    }

    //Método para iniciar a database
    var database: Database? = null
    var table = ""

    private fun database() {
        if (settings!!.getBoolean("Database.mysql")) {
            val hostname = settings!!.getString("Database.hostname")
            val databaseName = settings!!.getString("Database.database")
            val username = settings!!.getString("Database.username")
            val password = settings!!.getString("Database.password")
            val tableName = settings!!.getString("Database.table")
            val port = settings!!.getInt("Database.port")
            val mysql = MySQL()
            mysql.hostname = hostname
            mysql.database = databaseName
            mysql.username = username
            mysql.password = password
            mysql.port = port
            table = tableName
            database = mysql
        } else {
            table = "stx1"
            database = SQLite(this)
        }

        if (database!!.open()) {
            database!!.close()
        } else {
            table = "stx1"
            database = SQLite(this)
        }
        database!!.open()
        database!!.statement!!.execute("create table if not exists $table (player text, jogadas int, venceu int, perdeu int);")
        database!!.close()
    }

    companion object {
        //Vars do arquivo de configuração
        var settings: FileConfiguration? = null
            internal set
        internal var sett: File? = null

        //Var da instance da main
        var instance: Main? = null
            private set
    }

}