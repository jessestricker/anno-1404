package de.jessestricker.anno1404.data.properties

import de.jessestricker.anno1404.data.missingElement
import de.jessestricker.anno1404.data.parseAsXml
import de.jessestricker.anno1404.data.skipSubtree
import de.jessestricker.anno1404.data.skipToStartElement
import java.nio.file.Path
import javax.xml.stream.XMLStreamConstants.START_ELEMENT
import javax.xml.stream.XMLStreamReader

data class Properties(
    val defaultFactory: Factory,
    val defaultWareProduction: WareProduction,
) {
    internal companion object {
        const val TAG = "Properties"
        private const val DEFAULT_VALUES_TAG = "DefaultValues"

        fun parse(reader: XMLStreamReader): Properties {
            check(reader.isStartElement && reader.localName == TAG)

            var defaultFactory: Factory? = null
            var defaultWareProduction: WareProduction? = null

            reader.skipToStartElement(null, DEFAULT_VALUES_TAG)
            while (reader.nextTag() == START_ELEMENT) {
                when (reader.localName) {
                    Factory.TAG -> defaultFactory = Factory.parse(reader)
                    WareProduction.TAG -> defaultWareProduction = WareProduction.parse(reader)
                    else -> reader.skipSubtree()
                }
            }

            return Properties(
                defaultFactory = defaultFactory ?: missingElement(Factory.TAG),
                defaultWareProduction = defaultWareProduction ?: missingElement(WareProduction.TAG),
            )
        }
    }
}

data class Factory(
    val rawMaterial1: Product,
    val rawCapacity1: Amount,
    val rawNeeded1: Amount,
    val rawMaterial2: Product,
) {
    internal companion object {
        const val TAG = "Factory"

        fun parse(reader: XMLStreamReader): Factory {
            check(reader.isStartElement && reader.localName == TAG)

            var rawMaterial1: Product? = null
            var rawCapacity1: Amount? = null
            var rawNeeded1: Amount? = null
            var rawMaterial2: Product? = null

            while (reader.nextTag() == START_ELEMENT) {
                when (reader.localName) {
                    "RawMaterial1" -> rawMaterial1 = reader.elementText.toProduct()
                    "RawCapacity1" -> rawCapacity1 = reader.elementText.toInt().toAmount()
                    "RawNeeded1" -> rawNeeded1 = reader.elementText.toInt().toAmount()
                    "RawMaterial2" -> rawMaterial2 = reader.elementText.toProduct()
                    else -> reader.skipSubtree()
                }
            }

            return Factory(
                rawMaterial1 = rawMaterial1 ?: missingElement("RawMaterial1"),
                rawCapacity1 = rawCapacity1 ?: missingElement("RawCapacity1"),
                rawNeeded1 = rawNeeded1 ?: missingElement("RawNeeded1"),
                rawMaterial2 = rawMaterial2 ?: missingElement("RawMaterial2"),
            )
        }
    }
}

data class WareProduction(
    val productionTime: Time,
    val product: Product,
    val productionCapacity: Amount,
    val productionCount: Amount,
) {
    internal companion object {
        const val TAG = "WareProduction"

        fun parse(reader: XMLStreamReader): WareProduction {
            check(reader.isStartElement && reader.localName == TAG)

            var productionTime: Time? = null
            var product: Product? = null
            var productionCapacity: Amount? = null
            var productionCount: Amount? = null

            while (reader.nextTag() == START_ELEMENT) {
                when (reader.localName) {
                    "ProductionTime" -> productionTime = reader.elementText.toInt().toTime()
                    "Product" -> product = reader.elementText.toProduct()
                    "ProductionCapacity" -> productionCapacity = reader.elementText.toInt().toAmount()
                    "ProductionCount" -> productionCount = reader.elementText.toInt().toAmount()
                    else -> reader.skipSubtree()
                }
            }

            return WareProduction(
                productionTime = productionTime ?: missingElement("ProductionTime"),
                product = product ?: missingElement("Product"),
                productionCapacity = productionCapacity ?: missingElement("ProductionCapacity"),
                productionCount = productionCount ?: missingElement("ProductionCount"),
            )
        }
    }
}

@JvmInline
value class Time(val milliseconds: Int)

fun Int.toTime() = Time(this)

@JvmInline
value class Product(val name: String)

fun String.toProduct() = Product(this)

@JvmInline
value class Amount(val kilograms: Int)

fun Int.toAmount() = Amount(this)

fun parseProperties(path: Path) = parseAsXml(path) { reader ->
    reader.nextTag()
    reader.require(START_ELEMENT, null, Properties.TAG)
    Properties.parse(reader)
}
