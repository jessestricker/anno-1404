package de.jessestricker.anno1404.data

import java.nio.file.Path
import kotlin.io.path.div

data class Data(
    val properties: Properties,
    val assets: Assets,
    val localizations: Localizations,
) {
    companion object {
        fun load(path: Path, languageName: String): Data {
            val gameDir = path / "data1.rda/data/config/game"
            val propertiesXmlFile = gameDir / "properties.xml"
            val assetsXmlFile = gameDir / "assets.xml"
            val guidTxtFile = path / "${languageName}0.rda/data/loca/${languageName}/txt/guids.txt"

            val properties = Properties.parse(propertiesXmlFile)
            val assets = Assets.parse(assetsXmlFile, properties)
            val localizations = Localizations.parse(guidTxtFile)

            return Data(properties, assets, localizations)
        }
    }
}
