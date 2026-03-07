package tome.seeders.csv

interface CsvReader {
    fun read(filePath: String): Sequence<Map<String, String>>
}
