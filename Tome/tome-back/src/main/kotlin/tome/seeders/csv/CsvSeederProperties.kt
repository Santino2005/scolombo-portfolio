package tome.seeders.csv

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "seeders")
class CsvSeederProperties {
    lateinit var csv: String
}
