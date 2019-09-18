package stanic.stx1.dao

import stanic.stx1.Main
import java.util.stream.Collectors

class X1Player(
    var nick: String,
    var jogadas: Int,
    var venceu: Int,
    var perdeu: Int
) {

    fun save() {
        val c = Main.instance!!.database!!
        val table = Main.instance!!.table
        c.open()
        val result = c.statement!!.executeQuery("select * from $table where player='$nick'")!!
        if (result.next()) c.statement!!.execute("update $table set jogadas='$jogadas', venceu='$venceu', perdeu='$perdeu' where player=$nick")
        else c.statement!!.execute("insert into $table value ('$nick', '$jogadas', '$venceu', '$perdeu')")
        c.close()
    }

    companion object {
        val all: List<X1Player>
            get() = Main.instance!!.cache.values.stream().collect(Collectors.toList())
    }

}