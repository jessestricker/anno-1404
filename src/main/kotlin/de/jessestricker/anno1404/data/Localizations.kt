package de.jessestricker.anno1404.data

import java.nio.file.Path
import kotlin.io.path.useLines

@JvmInline
value class Localizations(private val guidToTexts: Map<String, String>) : Map<String, String> by guidToTexts {
    companion object {
        fun parse(path: Path): Localizations {
            val guidToTexts = mutableMapOf<String, String>()
            path.useLines(Charsets.UTF_16) { lines ->
                for (line in lines) {
                    val lineContent = line.substringBefore('#').trim()
                    if (lineContent.isEmpty()) {
                        continue
                    }
                    val (guid, text) = lineContent.split('=', limit = 2)
                    val previousValue = guidToTexts.putIfAbsent(guid, text)
                    check(previousValue == null) { "duplicate GUID $guid" }
                }
            }
            return Localizations(guidToTexts)
        }
    }
}
