package tome.seeders.filter

interface Filter {
    fun isValid(text: String): Boolean
}
