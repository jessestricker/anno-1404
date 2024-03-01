package de.jessestricker.anno1404

import de.jessestricker.anno1404.data.Properties
import kotlin.io.path.Path
import kotlin.io.path.div

private val MAINDATA_DIR = Path("extracted/maindata")
private val DATA1_FILE = MAINDATA_DIR / "data1.rda"
private val PROPERTIES_XML_FILE = DATA1_FILE / "data/config/game/properties.xml"

fun main() {
    val properties = Properties.parse(PROPERTIES_XML_FILE)
    println(properties)
}
