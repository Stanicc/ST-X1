package stanic.stx1.x1

import stanic.stx1.Main
import java.util.stream.Collectors

class Partidas(var p: String?, var pp: String?, var partida: Int, var bot: Boolean) {

    companion object {
        val all: List<Partidas>
            get() = Main.instance!!.partidas.values.stream().collect(Collectors.toList())
    }

}