package tome.seeders.csv

import org.apache.commons.csv.CSVFormat
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.io.InputStreamReader

@Component
class DefaultCsvReader : CsvReader {
    override fun read(filePath: String): Sequence<Map<String, String>> {
        val resourcePath = filePath.removePrefix("classpath:")
        val resource = ClassPathResource(resourcePath)
        val reader = InputStreamReader(resource.inputStream, Charsets.UTF_8)
        val parser =
            CSVFormat.Builder
                .create()
                .setHeader()
                .setSkipHeaderRecord(true)
                .build()
                .parse(reader)

        val iterator =
            object : Iterator<Map<String, String>> {
                private val csvIter = parser.iterator()

                override fun hasNext(): Boolean {
                    val hasNext = csvIter.hasNext()
                    if (!hasNext) close()
                    return hasNext
                }

                override fun next(): Map<String, String> = csvIter.next().toMap()

                private fun close() {
                    parser.close()
                    reader.close()
                }
            }

        return Sequence { iterator }
    }
}
