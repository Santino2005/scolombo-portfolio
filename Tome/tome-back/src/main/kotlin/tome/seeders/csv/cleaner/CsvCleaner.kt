package tome.seeders.csv.cleaner

interface CsvCleaner {
    fun deleteRow(
        filePath: String,
        rows: List<Int>,
    ): Boolean
}
