package de.jessestricker.anno1404

import de.jessestricker.anno1404.data.Data
import kotlin.io.path.Path

private val MAINDATA_DIR = Path("extracted/maindata")

fun main() {
    val data = Data.load(MAINDATA_DIR, "ger")

    println(data.properties)
    println(data.assets)
    println("localizations: ${data.localizations.size}")
}
