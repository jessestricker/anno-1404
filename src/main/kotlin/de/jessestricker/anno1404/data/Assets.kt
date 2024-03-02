package de.jessestricker.anno1404.data

import java.nio.file.Path
import javax.xml.stream.XMLStreamConstants.START_ELEMENT
import javax.xml.stream.XMLStreamReader

private const val VALUES_TAG = "Values"

data class Assets(
    val farmBuildings: List<FarmBuilding>,
    val factoryBuildings: List<FactoryBuilding>,
) {
    companion object {
        private const val TAG = "AssetList"
        private const val TEMPLATE_TAG = "Template"

        fun parse(path: Path, properties: Properties): Assets {
            return parseAsXml(path) { reader ->
                reader.nextTag()
                reader.require(START_ELEMENT, null, TAG)
                parse(reader, properties)
            }
        }

        private fun parse(reader: XMLStreamReader, properties: Properties): Assets {
            val farmBuildings = mutableListOf<FarmBuilding>()
            val factoryBuildings = mutableListOf<FactoryBuilding>()

            while (reader.skipToStartElement(null, TEMPLATE_TAG)) {
                val template = reader.elementText
                reader.nextTag()
                reader.require(START_ELEMENT, null, VALUES_TAG)
                when (template) {
                    FarmBuilding.TAG -> farmBuildings += FarmBuilding.parse(reader, properties)
                    FactoryBuilding.TAG -> factoryBuildings += FactoryBuilding.parse(reader, properties)
                    else -> reader.skipSubtree()
                }
            }

            return Assets(farmBuildings, factoryBuildings)
        }
    }
}

data class AssetId(
    val name: String,
    val guid: String,
) {
    internal companion object {
        const val TAG = "Standard"
        private const val NAME_TAG = "Name"
        private const val GUID_TAG = "GUID"

        fun parse(reader: XMLStreamReader): AssetId {
            check(reader.isStartElement && reader.localName == TAG)

            var name: String? = null
            var guid: String? = null

            while (reader.nextTag() == START_ELEMENT) {
                when (reader.localName) {
                    NAME_TAG -> name = reader.elementText
                    GUID_TAG -> guid = reader.elementText
                    else -> reader.skipSubtree()
                }
            }

            return AssetId(
                name = name ?: missingElement(NAME_TAG),
                guid = guid ?: missingElement(GUID_TAG),
            )
        }
    }
}

data class FarmBuilding(
    val id: AssetId,
    val wareProduction: WareProduction,
) {
    internal companion object {
        const val TAG = "FarmBuilding"

        fun parse(reader: XMLStreamReader, properties: Properties): FarmBuilding {
            check(reader.isStartElement && reader.localName == VALUES_TAG)

            var id: AssetId? = null
            var wareProduction: WareProduction? = null

            while (reader.nextTag() == START_ELEMENT) {
                when (reader.localName) {
                    AssetId.TAG -> id = AssetId.parse(reader)
                    WareProduction.TAG -> wareProduction =
                        WareProduction.parse(reader, properties.defaultWareProduction)

                    else -> reader.skipSubtree()
                }
            }

            return FarmBuilding(
                id = id ?: missingElement(AssetId.TAG),
                wareProduction = wareProduction ?: missingElement(WareProduction.TAG),
            )
        }
    }
}

data class FactoryBuilding(
    val id: AssetId,
    val wareProduction: WareProduction,
    val factory: Factory,
) {
    internal companion object {
        const val TAG = "FactoryBuilding"

        fun parse(reader: XMLStreamReader, properties: Properties): FactoryBuilding {
            check(reader.isStartElement && reader.localName == VALUES_TAG)

            var id: AssetId? = null
            var wareProduction: WareProduction? = null
            var factory: Factory? = null

            while (reader.nextTag() == START_ELEMENT) {
                when (reader.localName) {
                    AssetId.TAG -> id = AssetId.parse(reader)
                    WareProduction.TAG -> wareProduction =
                        WareProduction.parse(reader, properties.defaultWareProduction)

                    Factory.TAG -> factory = Factory.parse(reader, properties.defaultFactory)
                    else -> reader.skipSubtree()
                }
            }

            return FactoryBuilding(
                id = id ?: missingElement(AssetId.TAG),
                wareProduction = wareProduction ?: missingElement(WareProduction.TAG),
                factory = factory ?: missingElement(Factory.TAG),
            )
        }
    }
}
