package tome.seeders

import org.springframework.boot.CommandLineRunner
import org.springframework.core.Ordered

interface DatabaseSeeder :
    CommandLineRunner,
    Ordered
