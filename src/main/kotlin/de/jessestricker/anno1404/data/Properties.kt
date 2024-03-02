package de.jessestricker.anno1404.data

import java.nio.file.Path
import javax.xml.stream.XMLStreamConstants.START_ELEMENT
import javax.xml.stream.XMLStreamReader

data class Properties(
    val defaultFactory: Factory,
    val defaultWareProduction: WareProduction,
) {
    companion object {
        private const val TAG = "Properties"
        private const val DEFAULT_VALUES_TAG = "DefaultValues"

        fun parse(path: Path): Properties {
            return parseAsXml(path) { reader ->
                reader.nextTag()
                reader.require(START_ELEMENT, null, TAG)
                parse(reader)
            }
        }

        private fun parse(reader: XMLStreamReader): Properties {
            check(reader.isStartElement && reader.localName == TAG)

            var defaultFactory: Factory? = null
            var defaultWareProduction: WareProduction? = null

            if (!reader.skipToStartElement(null, DEFAULT_VALUES_TAG)) {
                missingElement(DEFAULT_VALUES_TAG)
            }
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
        private const val RAW_MATERIAL_1_TAG = "RawMaterial1"
        private const val RAW_CAPACITY_1_TAG = "RawCapacity1"
        private const val RAW_NEEDED_1_TAG = "RawNeeded1"
        private const val RAW_MATERIAL_2_TAG = "RawMaterial2"

        fun parse(reader: XMLStreamReader, default: Factory? = null): Factory {
            check(reader.isStartElement && reader.localName == TAG)

            var rawMaterial1: Product? = null
            var rawCapacity1: Amount? = null
            var rawNeeded1: Amount? = null
            var rawMaterial2: Product? = null

            while (reader.nextTag() == START_ELEMENT) {
                when (reader.localName) {
                    RAW_MATERIAL_1_TAG -> rawMaterial1 = reader.elementText.toProduct()
                    RAW_CAPACITY_1_TAG -> rawCapacity1 = reader.elementText.toInt().toAmount()
                    RAW_NEEDED_1_TAG -> rawNeeded1 = reader.elementText.toInt().toAmount()
                    RAW_MATERIAL_2_TAG -> rawMaterial2 = reader.elementText.toProduct()
                    else -> reader.skipSubtree()
                }
            }

            return Factory(
                rawMaterial1 = rawMaterial1 ?: default?.rawMaterial1 ?: missingElement(RAW_MATERIAL_1_TAG),
                rawCapacity1 = rawCapacity1 ?: default?.rawCapacity1 ?: missingElement(RAW_CAPACITY_1_TAG),
                rawNeeded1 = rawNeeded1 ?: default?.rawNeeded1 ?: missingElement(RAW_NEEDED_1_TAG),
                rawMaterial2 = rawMaterial2 ?: default?.rawMaterial2 ?: missingElement(RAW_MATERIAL_2_TAG),
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
        private const val PRODUCTION_TIME_TAG = "ProductionTime"
        private const val PRODUCT_TAG = "Product"
        private const val PRODUCTION_CAPACITY_TAG = "ProductionCapacity"
        private const val PRODUCTION_COUNT_TAG = "ProductionCount"

        fun parse(reader: XMLStreamReader, default: WareProduction? = null): WareProduction {
            check(reader.isStartElement && reader.localName == TAG)

            var productionTime: Time? = null
            var product: Product? = null
            var productionCapacity: Amount? = null
            var productionCount: Amount? = null

            while (reader.nextTag() == START_ELEMENT) {
                when (reader.localName) {
                    PRODUCTION_TIME_TAG -> productionTime = reader.elementText.toInt().toTime()
                    PRODUCT_TAG -> product = reader.elementText.toProduct()
                    PRODUCTION_CAPACITY_TAG -> productionCapacity = reader.elementText.toInt().toAmount()
                    PRODUCTION_COUNT_TAG -> productionCount = reader.elementText.toInt().toAmount()
                    else -> reader.skipSubtree()
                }
            }

            return WareProduction(
                productionTime = productionTime ?: default?.productionTime ?: missingElement(PRODUCTION_TIME_TAG),
                product = product ?: default?.product ?: missingElement(PRODUCT_TAG),
                productionCapacity = productionCapacity ?: default?.productionCapacity ?: missingElement(
                    PRODUCTION_CAPACITY_TAG
                ),
                productionCount = productionCount ?: default?.productionCount ?: missingElement(PRODUCTION_COUNT_TAG),
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
