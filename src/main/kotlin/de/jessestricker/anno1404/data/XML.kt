package de.jessestricker.anno1404.data

import java.nio.file.Path
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamConstants.END_ELEMENT
import javax.xml.stream.XMLStreamConstants.START_ELEMENT
import javax.xml.stream.XMLStreamReader
import kotlin.io.path.inputStream

private val xmlInputFactory = XMLInputFactory.newDefaultFactory().apply {
    setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false)
}

internal fun <T> parseAsXml(path: Path, block: (XMLStreamReader) -> T): T {
    path.inputStream().use { stream ->
        val reader = xmlInputFactory.createXMLStreamReader(stream)
        try {
            return block(reader)
        } finally {
            reader.close()
        }
    }
}

internal fun XMLStreamReader.skipSubtree() {
    check(eventType == START_ELEMENT)
    var depth = 0
    while (depth >= 0) {
        when (next()) {
            START_ELEMENT -> depth++
            END_ELEMENT -> depth--
        }
    }
    assert(eventType == END_ELEMENT)
}

internal fun XMLStreamReader.skipToStartElement(namespaceURI: String?, localName: String?) {
    while (true) {
        val eventType = next()
        if (eventType == START_ELEMENT) {
            if ((namespaceURI == null || namespaceURI == getNamespaceURI()) && (localName == null || localName == getLocalName())) {
                break
            }
        }
    }
}

internal fun missingElement(localName: String): Nothing {
    throw NoSuchElementException("missing element $localName")
}
