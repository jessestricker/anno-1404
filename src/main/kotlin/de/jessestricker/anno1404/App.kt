package de.jessestricker.anno1404

import de.jessestricker.anno1404.data.Localizations
import de.jessestricker.anno1404.data.Properties
import kotlin.io.path.Path
import kotlin.io.path.div

private val MAINDATA_DIR = Path("extracted/maindata")
private val DATA1_FILE = MAINDATA_DIR / "data1.rda"
private val PROPERTIES_XML_FILE = DATA1_FILE / "data/config/game/properties.xml"
private val GER0_FILE = MAINDATA_DIR / "ger0.rda"
private val GUIDS_TXT_FILE = GER0_FILE / "data/loca/ger/txt/guids.txt"

fun main() {
    val properties = Properties.parse(PROPERTIES_XML_FILE)
    println(properties)

    val localizations = Localizations.parse(GUIDS_TXT_FILE)
    println("localizations: ${localizations.size}")
}
