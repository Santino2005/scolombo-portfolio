package tome.seeders.csv.cleaner

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileReader
import java.io.FileWriter

@Component
class DefaultCsvCleaner : CsvCleaner {
    override fun deleteRow(
        filePath: String,
        rows: List<Int>,
    ): Boolean {
        val file = resolveFile(filePath) ?: return false

        if (!file.exists()) {
            println("CSVCleaner: File not found: ${file.absolutePath}")
            return false
        }

        println("Cleaning ${rows.size} rows from ${file.absolutePath}")

        try {
            // Read CSV
            val parser =
                CSVParser(
                    FileReader(file),
                    CSVFormat.DEFAULT
                        .builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .build(),
                )

            val headers = parser.headerNames
            val records = parser.records.toMutableList()
            parser.close()

            // Delete requested rows (sorted descending to keep indexes valid)
            rows
                .sortedDescending()
                .filter { it in 0 until records.size }
                .forEach { records.removeAt(it) }

            // Rewrite CSV
            FileWriter(file).use { writer ->
                val printer =
                    CSVPrinter(
                        writer,
                        CSVFormat.DEFAULT
                            .builder()
                            .setHeader(*headers.toTypedArray())
                            .build(),
                    )
                records.forEach { printer.printRecord(it) }
                printer.flush()
            }

            println("CSVCleaner: Deleted ${rows.size} rows from ${file.name}")
            return true
        } catch (ex: Exception) {
            ex.printStackTrace()
            println("CSVCleaner: Error while cleaning ${file.name}: ${ex.message}")
            return false
        }
    }

    /**
     * Resolves a file path that might start with 'classpath:' into a writable File.
     * If it's a classpath resource, copies it into the system temp directory so it can be modified.
     */
    private fun resolveFile(filePath: String): File? {
        return if (filePath.startsWith("classpath:")) {
            val resourcePath = filePath.removePrefix("classpath:")
            val resource = ClassPathResource(resourcePath)
            if (!resource.exists()) return null

            // Copy classpath resource to a temp file for writing
            val tempFile = File(System.getProperty("java.io.tmpdir"), resource.filename ?: "temp.csv")
            resource.inputStream.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            tempFile
        } else {
            File(filePath)
        }
    }
}
