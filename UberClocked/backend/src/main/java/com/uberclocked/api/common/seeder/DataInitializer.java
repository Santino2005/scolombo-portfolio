package com.uberclocked.api.common.seeder;

import com.uberclocked.api.component.model.entity.Component;
import com.uberclocked.api.component.model.entity.field.FieldType;
import com.uberclocked.api.component.repository.ComponentRepository;
import com.uberclocked.api.product.model.entity.Product;
import com.uberclocked.api.product.repository.ProductRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

  private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

  private final ComponentRepository componentRepository;
  private final ProductRepository productRepository;

  public DataInitializer(
      ComponentRepository componentRepository, ProductRepository productRepository) {
    this.componentRepository = componentRepository;
    this.productRepository = productRepository;
  }

  @Override
  @Transactional
  public void run(String... args) {
    seedComponentsAndProducts();
  }

  public void seedComponentsAndProducts() {
    log.info("Checking database seed for components and products...");

    Map<String, Component> compMap = new HashMap<>();

    // 1. CPU
    compMap.put("CPU", getOrCreateComponent("CPU", "Processors", Map.of(
        "brand", FieldType.STRING,
        "socket", FieldType.STRING,
        "cores", FieldType.INTEGER,
        "threads", FieldType.INTEGER,
        "clock_speed", FieldType.STRING,
        "tdp", FieldType.STRING
    )));

    // 2. GPU
    compMap.put("GPU", getOrCreateComponent("GPU", "Graphics Cards", Map.of(
        "brand", FieldType.STRING,
        "chipset", FieldType.STRING,
        "vram_gb", FieldType.INTEGER,
        "memory_type", FieldType.STRING,
        "tdp", FieldType.STRING
    )));

    // 3. MOTHERBOARD
    compMap.put("MOTHERBOARD", getOrCreateComponent("MOTHERBOARD", "Motherboards", Map.of(
        "brand", FieldType.STRING,
        "socket", FieldType.STRING,
        "chipset", FieldType.STRING,
        "form_factor", FieldType.STRING,
        "memory_type", FieldType.STRING,
        "memory_slots", FieldType.INTEGER
    )));

    // 4. RAM
    compMap.put("RAM", getOrCreateComponent("RAM", "RAM Memory", Map.of(
        "brand", FieldType.STRING,
        "capacity_gb", FieldType.INTEGER,
        "speed_mhz", FieldType.INTEGER,
        "type", FieldType.STRING,
        "modules", FieldType.STRING
    )));

    // 5. SD (Storage)
    compMap.put("SD", getOrCreateComponent("SD", "Storage (SSD / HDD)", Map.of(
        "brand", FieldType.STRING,
        "capacity_gb", FieldType.INTEGER,
        "interface", FieldType.STRING,
        "form_factor", FieldType.STRING,
        "read_speed_mb", FieldType.INTEGER
    )));

    // 6. CASE
    compMap.put("CASE", getOrCreateComponent("CASE", "Cases & Chassis", Map.of(
        "brand", FieldType.STRING,
        "form_factor", FieldType.STRING,
        "color", FieldType.STRING,
        "side_panel", FieldType.STRING
    )));

    // 7. PSU
    compMap.put("PSU", getOrCreateComponent("PSU", "Power Supplies", Map.of(
        "brand", FieldType.STRING,
        "wattage", FieldType.INTEGER,
        "efficiency", FieldType.STRING,
        "modular", FieldType.STRING
    )));

    // 8. COOLER
    compMap.put("COOLER", getOrCreateComponent("COOLER", "CPU Coolers", Map.of(
        "brand", FieldType.STRING,
        "type", FieldType.STRING,
        "fan_size_mm", FieldType.INTEGER,
        "rgb", FieldType.STRING
    )));

    // 9. MONITOR
    compMap.put("MONITOR", getOrCreateComponent("MONITOR", "Monitors & Displays", Map.of(
        "brand", FieldType.STRING,
        "screen_size_inches", FieldType.DECIMAL,
        "resolution", FieldType.STRING,
        "refresh_rate_hz", FieldType.INTEGER,
        "panel_type", FieldType.STRING
    )));

    // 10. PERIPHERAL
    compMap.put("PERIPHERAL", getOrCreateComponent("PERIPHERAL", "Gaming Peripherals", Map.of(
        "brand", FieldType.STRING,
        "category", FieldType.STRING,
        "connectivity", FieldType.STRING,
        "color", FieldType.STRING
    )));

    long productCount = productRepository.count();
    if (productCount >= 200) {
      log.info("Database already contains {} products. Skipping seed.", productCount);
      return;
    }

    log.info("Seeding products (at least 20 per component)...");

    List<ProductSeedData> seeds = new ArrayList<>();

    // ==========================================
    // CPUs (21 items)
    // ==========================================
    seeds.add(new ProductSeedData("CPU-INTEL-I9-14900KS", "Intel Core i9-14900KS Special Edition 6.2GHz", "CPU", 699.99, 25, Map.of("brand", "Intel", "socket", "LGA1700", "cores", "24", "threads", "32", "clock_speed", "6.2 GHz", "tdp", "150W")));
    seeds.add(new ProductSeedData("CPU-INTEL-I9-14900K", "Intel Core i9-14900K 6.0GHz", "CPU", 549.99, 40, Map.of("brand", "Intel", "socket", "LGA1700", "cores", "24", "threads", "32", "clock_speed", "6.0 GHz", "tdp", "125W")));
    seeds.add(new ProductSeedData("CPU-INTEL-I7-14700K", "Intel Core i7-14700K 5.6GHz", "CPU", 389.99, 50, Map.of("brand", "Intel", "socket", "LGA1700", "cores", "20", "threads", "28", "clock_speed", "5.6 GHz", "tdp", "125W")));
    seeds.add(new ProductSeedData("CPU-INTEL-I5-14600K", "Intel Core i5-14600K 5.3GHz", "CPU", 299.99, 65, Map.of("brand", "Intel", "socket", "LGA1700", "cores", "14", "threads", "20", "clock_speed", "5.3 GHz", "tdp", "125W")));
    seeds.add(new ProductSeedData("CPU-INTEL-I5-13400F", "Intel Core i5-13400F 4.6GHz", "CPU", 185.00, 80, Map.of("brand", "Intel", "socket", "LGA1700", "cores", "10", "threads", "16", "clock_speed", "4.6 GHz", "tdp", "65W")));
    seeds.add(new ProductSeedData("CPU-INTEL-I3-13100F", "Intel Core i3-13100F 4.5GHz", "CPU", 110.00, 45, Map.of("brand", "Intel", "socket", "LGA1700", "cores", "4", "threads", "8", "clock_speed", "4.5 GHz", "tdp", "58W")));
    seeds.add(new ProductSeedData("CPU-INTEL-U9-285K", "Intel Core Ultra 9 285K Arrow Lake", "CPU", 629.99, 30, Map.of("brand", "Intel", "socket", "LGA1851", "cores", "24", "threads", "24", "clock_speed", "5.7 GHz", "tdp", "125W")));
    seeds.add(new ProductSeedData("CPU-INTEL-U7-265K", "Intel Core Ultra 7 265K Arrow Lake", "CPU", 419.99, 35, Map.of("brand", "Intel", "socket", "LGA1851", "cores", "20", "threads", "20", "clock_speed", "5.5 GHz", "tdp", "125W")));
    seeds.add(new ProductSeedData("CPU-INTEL-U5-245K", "Intel Core Ultra 5 245K Arrow Lake", "CPU", 319.99, 40, Map.of("brand", "Intel", "socket", "LGA1851", "cores", "14", "threads", "14", "clock_speed", "5.2 GHz", "tdp", "125W")));
    seeds.add(new ProductSeedData("CPU-AMD-R9-7950X3D", "AMD Ryzen 9 7950X3D 3D V-Cache", "CPU", 599.99, 30, Map.of("brand", "AMD", "socket", "AM5", "cores", "16", "threads", "32", "clock_speed", "5.7 GHz", "tdp", "120W")));
    seeds.add(new ProductSeedData("CPU-AMD-R9-7950X", "AMD Ryzen 9 7950X 16-Core", "CPU", 519.99, 25, Map.of("brand", "AMD", "socket", "AM5", "cores", "16", "threads", "32", "clock_speed", "5.7 GHz", "tdp", "170W")));
    seeds.add(new ProductSeedData("CPU-AMD-R9-7900X3D", "AMD Ryzen 9 7900X3D 12-Core", "CPU", 449.99, 20, Map.of("brand", "AMD", "socket", "AM5", "cores", "12", "threads", "24", "clock_speed", "5.6 GHz", "tdp", "120W")));
    seeds.add(new ProductSeedData("CPU-AMD-R7-7800X3D", "AMD Ryzen 7 7800X3D Gaming Beast", "CPU", 449.00, 75, Map.of("brand", "AMD", "socket", "AM5", "cores", "8", "threads", "16", "clock_speed", "5.0 GHz", "tdp", "120W")));
    seeds.add(new ProductSeedData("CPU-AMD-R7-7700X", "AMD Ryzen 7 7700X 8-Core", "CPU", 299.99, 50, Map.of("brand", "AMD", "socket", "AM5", "cores", "8", "threads", "16", "clock_speed", "5.4 GHz", "tdp", "105W")));
    seeds.add(new ProductSeedData("CPU-AMD-R5-7600X", "AMD Ryzen 5 7600X 6-Core", "CPU", 209.99, 60, Map.of("brand", "AMD", "socket", "AM5", "cores", "6", "threads", "12", "clock_speed", "5.3 GHz", "tdp", "105W")));
    seeds.add(new ProductSeedData("CPU-AMD-R5-7600", "AMD Ryzen 5 7600 6-Core with Wraith Stealth", "CPU", 189.99, 70, Map.of("brand", "AMD", "socket", "AM5", "cores", "6", "threads", "12", "clock_speed", "5.1 GHz", "tdp", "65W")));
    seeds.add(new ProductSeedData("CPU-AMD-R9-9950X", "AMD Ryzen 9 9950X Zen 5", "CPU", 649.99, 25, Map.of("brand", "AMD", "socket", "AM5", "cores", "16", "threads", "32", "clock_speed", "5.7 GHz", "tdp", "170W")));
    seeds.add(new ProductSeedData("CPU-AMD-R9-9900X", "AMD Ryzen 9 9900X Zen 5", "CPU", 499.99, 25, Map.of("brand", "AMD", "socket", "AM5", "cores", "12", "threads", "24", "clock_speed", "5.6 GHz", "tdp", "120W")));
    seeds.add(new ProductSeedData("CPU-AMD-R7-9700X", "AMD Ryzen 7 9700X Zen 5", "CPU", 359.99, 45, Map.of("brand", "AMD", "socket", "AM5", "cores", "8", "threads", "16", "clock_speed", "5.5 GHz", "tdp", "65W")));
    seeds.add(new ProductSeedData("CPU-AMD-R5-9600X", "AMD Ryzen 5 9600X Zen 5", "CPU", 279.99, 50, Map.of("brand", "AMD", "socket", "AM5", "cores", "6", "threads", "12", "clock_speed", "5.4 GHz", "tdp", "65W")));
    seeds.add(new ProductSeedData("CPU-AMD-R7-5700X3D", "AMD Ryzen 7 5700X3D AM4 Upgrade", "CPU", 199.99, 40, Map.of("brand", "AMD", "socket", "AM4", "cores", "8", "threads", "16", "clock_speed", "4.1 GHz", "tdp", "105W")));

    // ==========================================
    // GPUs (21 items)
    // ==========================================
    seeds.add(new ProductSeedData("GPU-NV-RTX-4090-ROG", "ASUS ROG Strix GeForce RTX 4090 OC 24GB", "GPU", 1999.99, 15, Map.of("brand", "ASUS", "chipset", "NVIDIA GeForce RTX 4090", "vram_gb", "24", "memory_type", "GDDR6X", "tdp", "450W")));
    seeds.add(new ProductSeedData("GPU-NV-RTX-4090-FE", "NVIDIA GeForce RTX 4090 Founders Edition 24GB", "GPU", 1799.99, 10, Map.of("brand", "NVIDIA", "chipset", "NVIDIA GeForce RTX 4090", "vram_gb", "24", "memory_type", "GDDR6X", "tdp", "450W")));
    seeds.add(new ProductSeedData("GPU-NV-RTX-4080S-MSI", "MSI Gaming X Slim GeForce RTX 4080 Super 16GB", "GPU", 1099.99, 25, Map.of("brand", "MSI", "chipset", "NVIDIA GeForce RTX 4080 Super", "vram_gb", "16", "memory_type", "GDDR6X", "tdp", "320W")));
    seeds.add(new ProductSeedData("GPU-NV-RTX-4080S-GIGA", "Gigabyte GeForce RTX 4080 Super Gaming OC 16GB", "GPU", 1049.99, 20, Map.of("brand", "Gigabyte", "chipset", "NVIDIA GeForce RTX 4080 Super", "vram_gb", "16", "memory_type", "GDDR6X", "tdp", "320W")));
    seeds.add(new ProductSeedData("GPU-NV-RTX-4070TIS-TUF", "ASUS TUF Gaming GeForce RTX 4070 Ti Super 16GB", "GPU", 849.99, 30, Map.of("brand", "ASUS", "chipset", "NVIDIA GeForce RTX 4070 Ti Super", "vram_gb", "16", "memory_type", "GDDR6X", "tdp", "285W")));
    seeds.add(new ProductSeedData("GPU-NV-RTX-4070TIS-MSI", "MSI Ventus 3X GeForce RTX 4070 Ti Super 16GB", "GPU", 829.99, 35, Map.of("brand", "MSI", "chipset", "NVIDIA GeForce RTX 4070 Ti Super", "vram_gb", "16", "memory_type", "GDDR6X", "tdp", "285W")));
    seeds.add(new ProductSeedData("GPU-NV-RTX-4070S-FE", "NVIDIA GeForce RTX 4070 Super Founders Edition 12GB", "GPU", 599.99, 40, Map.of("brand", "NVIDIA", "chipset", "NVIDIA GeForce RTX 4070 Super", "vram_gb", "12", "memory_type", "GDDR6X", "tdp", "220W")));
    seeds.add(new ProductSeedData("GPU-NV-RTX-4070S-ZOTAC", "Zotac Gaming GeForce RTX 4070 Super Twin Edge 12GB", "GPU", 589.99, 45, Map.of("brand", "Zotac", "chipset", "NVIDIA GeForce RTX 4070 Super", "vram_gb", "12", "memory_type", "GDDR6X", "tdp", "220W")));
    seeds.add(new ProductSeedData("GPU-NV-RTX-4060TI-16", "Gigabyte GeForce RTX 4060 Ti Gaming OC 16GB", "GPU", 479.99, 35, Map.of("brand", "Gigabyte", "chipset", "NVIDIA GeForce RTX 4060 Ti", "vram_gb", "16", "memory_type", "GDDR6", "tdp", "165W")));
    seeds.add(new ProductSeedData("GPU-NV-RTX-4060TI-8", "MSI Ventus 2X GeForce RTX 4060 Ti 8GB", "GPU", 389.99, 50, Map.of("brand", "MSI", "chipset", "NVIDIA GeForce RTX 4060 Ti", "vram_gb", "8", "memory_type", "GDDR6", "tdp", "160W")));
    seeds.add(new ProductSeedData("GPU-NV-RTX-4060-ASUS", "ASUS Dual GeForce RTX 4060 EVO OC 8GB", "GPU", 299.99, 60, Map.of("brand", "ASUS", "chipset", "NVIDIA GeForce RTX 4060", "vram_gb", "8", "memory_type", "GDDR6", "tdp", "115W")));
    seeds.add(new ProductSeedData("GPU-NV-RTX-3060-12", "EVGA GeForce RTX 3060 XC Gaming 12GB", "GPU", 279.99, 25, Map.of("brand", "EVGA", "chipset", "NVIDIA GeForce RTX 3060", "vram_gb", "12", "memory_type", "GDDR6", "tdp", "170W")));
    seeds.add(new ProductSeedData("GPU-AMD-RX-7900XTX-NITRO", "Sapphire Nitro+ Radeon RX 7900 XTX 24GB Vapor-X", "GPU", 1049.99, 20, Map.of("brand", "Sapphire", "chipset", "AMD Radeon RX 7900 XTX", "vram_gb", "24", "memory_type", "GDDR6", "tdp", "355W")));
    seeds.add(new ProductSeedData("GPU-AMD-RX-7900XT-XFX", "XFX Speedster MERC310 Radeon RX 7900 XT 20GB", "GPU", 729.99, 25, Map.of("brand", "XFX", "chipset", "AMD Radeon RX 7900 XT", "vram_gb", "20", "memory_type", "GDDR6", "tdp", "315W")));
    seeds.add(new ProductSeedData("GPU-AMD-RX-7900GRE-POWER", "PowerColor Hellhound Radeon RX 7900 GRE 16GB", "GPU", 549.99, 30, Map.of("brand", "PowerColor", "chipset", "AMD Radeon RX 7900 GRE", "vram_gb", "16", "memory_type", "GDDR6", "tdp", "260W")));
    seeds.add(new ProductSeedData("GPU-AMD-RX-7800XT-PULSE", "Sapphire Pulse Radeon RX 7800 XT 16GB", "GPU", 499.99, 45, Map.of("brand", "Sapphire", "chipset", "AMD Radeon RX 7800 XT", "vram_gb", "16", "memory_type", "GDDR6", "tdp", "263W")));
    seeds.add(new ProductSeedData("GPU-AMD-RX-7700XT-ASUS", "ASUS TUF Gaming Radeon RX 7700 XT 12GB", "GPU", 419.99, 35, Map.of("brand", "ASUS", "chipset", "AMD Radeon RX 7700 XT", "vram_gb", "12", "memory_type", "GDDR6", "tdp", "245W")));
    seeds.add(new ProductSeedData("GPU-AMD-RX-7600XT-XFX", "XFX Speedster QICK309 Radeon RX 7600 XT 16GB", "GPU", 329.99, 40, Map.of("brand", "XFX", "chipset", "AMD Radeon RX 7600 XT", "vram_gb", "16", "memory_type", "GDDR6", "tdp", "190W")));
    seeds.add(new ProductSeedData("GPU-AMD-RX-6600-ASR", "ASRock Radeon RX 6600 Challenger D 8GB", "GPU", 199.99, 50, Map.of("brand", "ASRock", "chipset", "AMD Radeon RX 6600", "vram_gb", "8", "memory_type", "GDDR6", "tdp", "132W")));
    seeds.add(new ProductSeedData("GPU-INTEL-ARC-A770-16", "Intel Arc A770 Limited Edition 16GB", "GPU", 289.99, 25, Map.of("brand", "Intel", "chipset", "Intel Arc A770", "vram_gb", "16", "memory_type", "GDDR6", "tdp", "225W")));
    seeds.add(new ProductSeedData("GPU-INTEL-ARC-A580-8", "ASRock Intel Arc A580 Challenger OC 8GB", "GPU", 169.99, 30, Map.of("brand", "ASRock", "chipset", "Intel Arc A580", "vram_gb", "8", "memory_type", "GDDR6", "tdp", "185W")));

    // ==========================================
    // Motherboards (21 items)
    // ==========================================
    seeds.add(new ProductSeedData("MB-ASUS-Z790-HERO", "ASUS ROG Maximus Z790 Hero WiFi", "MOTHERBOARD", 629.99, 20, Map.of("brand", "ASUS", "socket", "LGA1700", "chipset", "Intel Z790", "form_factor", "ATX", "memory_type", "DDR5", "memory_slots", "4")));
    seeds.add(new ProductSeedData("MB-MSI-Z790-TOMAHAWK", "MSI MAG Z790 Tomahawk WiFi DDR5", "MOTHERBOARD", 269.99, 35, Map.of("brand", "MSI", "socket", "LGA1700", "chipset", "Intel Z790", "form_factor", "ATX", "memory_type", "DDR5", "memory_slots", "4")));
    seeds.add(new ProductSeedData("MB-GIGA-Z790-AORUS-ELITE", "Gigabyte Z790 AORUS Elite AX", "MOTHERBOARD", 239.99, 40, Map.of("brand", "Gigabyte", "socket", "LGA1700", "chipset", "Intel Z790", "form_factor", "ATX", "memory_type", "DDR5", "memory_slots", "4")));
    seeds.add(new ProductSeedData("MB-ASUS-B760-PLUS", "ASUS TUF Gaming B760-PLUS WiFi D4", "MOTHERBOARD", 179.99, 45, Map.of("brand", "ASUS", "socket", "LGA1700", "chipset", "Intel B760", "form_factor", "ATX", "memory_type", "DDR4", "memory_slots", "4")));
    seeds.add(new ProductSeedData("MB-MSI-B760M-A", "MSI PRO B760M-A WiFi DDR5 Micro-ATX", "MOTHERBOARD", 149.99, 50, Map.of("brand", "MSI", "socket", "LGA1700", "chipset", "Intel B760", "form_factor", "Micro-ATX", "memory_type", "DDR5", "memory_slots", "4")));
    seeds.add(new ProductSeedData("MB-ASR-B760M-PRO", "ASRock B760M Pro RS DDR5", "MOTHERBOARD", 129.99, 35, Map.of("brand", "ASRock", "socket", "LGA1700", "chipset", "Intel B760", "form_factor", "Micro-ATX", "memory_type", "DDR5", "memory_slots", "4")));
    seeds.add(new ProductSeedData("MB-ASUS-X670E-HERO", "ASUS ROG Crosshair X670E Hero", "MOTHERBOARD", 699.99, 15, Map.of("brand", "ASUS", "socket", "AM5", "chipset", "AMD X670E", "form_factor", "ATX", "memory_type", "DDR5", "memory_slots", "4")));
    seeds.add(new ProductSeedData("MB-MSI-X670E-TOMAHAWK", "MSI MAG X670E Tomahawk WiFi", "MOTHERBOARD", 299.99, 30, Map.of("brand", "MSI", "socket", "AM5", "chipset", "AMD X670E", "form_factor", "ATX", "memory_type", "DDR5", "memory_slots", "4")));
    seeds.add(new ProductSeedData("MB-GIGA-X670-AORUS", "Gigabyte X670 AORUS Elite AX", "MOTHERBOARD", 259.99, 35, Map.of("brand", "Gigabyte", "socket", "AM5", "chipset", "AMD X670", "form_factor", "ATX", "memory_type", "DDR5", "memory_slots", "4")));
    seeds.add(new ProductSeedData("MB-ASUS-B650-TUF", "ASUS TUF Gaming B650-PLUS WiFi", "MOTHERBOARD", 199.99, 60, Map.of("brand", "ASUS", "socket", "AM5", "chipset", "AMD B650", "form_factor", "ATX", "memory_type", "DDR5", "memory_slots", "4")));
    seeds.add(new ProductSeedData("MB-MSI-B650-TOMAHAWK", "MSI MAG B650 Tomahawk WiFi", "MOTHERBOARD", 199.99, 55, Map.of("brand", "MSI", "socket", "AM5", "chipset", "AMD B650", "form_factor", "ATX", "memory_type", "DDR5", "memory_slots", "4")));
    seeds.add(new ProductSeedData("MB-GIGA-B650-AORUS", "Gigabyte B650 AORUS Elite AX V2", "MOTHERBOARD", 189.99, 50, Map.of("brand", "Gigabyte", "socket", "AM5", "chipset", "AMD B650", "form_factor", "ATX", "memory_type", "DDR5", "memory_slots", "4")));
    seeds.add(new ProductSeedData("MB-ASR-B650M-HDV", "ASRock B650M-HDV/M.2 Micro-ATX", "MOTHERBOARD", 119.99, 45, Map.of("brand", "ASRock", "socket", "AM5", "chipset", "AMD B650", "form_factor", "Micro-ATX", "memory_type", "DDR5", "memory_slots", "2")));
    seeds.add(new ProductSeedData("MB-ASUS-B650E-ITX", "ASUS ROG Strix B650E-I Gaming WiFi Mini-ITX", "MOTHERBOARD", 319.99, 20, Map.of("brand", "ASUS", "socket", "AM5", "chipset", "AMD B650E", "form_factor", "Mini-ITX", "memory_type", "DDR5", "memory_slots", "2")));
    seeds.add(new ProductSeedData("MB-MSI-B650I-EDGE", "MSI MPG B650I Edge WiFi Mini-ITX", "MOTHERBOARD", 269.99, 20, Map.of("brand", "MSI", "socket", "AM5", "chipset", "AMD B650", "form_factor", "Mini-ITX", "memory_type", "DDR5", "memory_slots", "2")));
    seeds.add(new ProductSeedData("MB-ASUS-A620M-A", "ASUS Prime A620M-A Micro-ATX", "MOTHERBOARD", 99.99, 40, Map.of("brand", "ASUS", "socket", "AM5", "chipset", "AMD A620", "form_factor", "Micro-ATX", "memory_type", "DDR5", "memory_slots", "4")));
    seeds.add(new ProductSeedData("MB-ASUS-Z890-HERO", "ASUS ROG Maximus Z890 Hero LGA1851", "MOTHERBOARD", 679.99, 15, Map.of("brand", "ASUS", "socket", "LGA1851", "chipset", "Intel Z890", "form_factor", "ATX", "memory_type", "DDR5", "memory_slots", "4")));
    seeds.add(new ProductSeedData("MB-MSI-Z890-TOMAHAWK", "MSI MAG Z890 Tomahawk WiFi", "MOTHERBOARD", 289.99, 25, Map.of("brand", "MSI", "socket", "LGA1851", "chipset", "Intel Z890", "form_factor", "ATX", "memory_type", "DDR5", "memory_slots", "4")));
    seeds.add(new ProductSeedData("MB-GIGA-Z890-MASTER", "Gigabyte Z890 AORUS Master WiFi 7", "MOTHERBOARD", 589.99, 15, Map.of("brand", "Gigabyte", "socket", "LGA1851", "chipset", "Intel Z890", "form_factor", "ATX", "memory_type", "DDR5", "memory_slots", "4")));
    seeds.add(new ProductSeedData("MB-ASR-X870E-TAICHI", "ASRock X870E Taichi AM5 Flagship", "MOTHERBOARD", 449.99, 20, Map.of("brand", "ASRock", "socket", "AM5", "chipset", "AMD X870E", "form_factor", "ATX", "memory_type", "DDR5", "memory_slots", "4")));
    seeds.add(new ProductSeedData("MB-ASUS-B550-F", "ASUS ROG Strix B550-F Gaming WiFi II AM4", "MOTHERBOARD", 169.99, 35, Map.of("brand", "ASUS", "socket", "AM4", "chipset", "AMD B550", "form_factor", "ATX", "memory_type", "DDR4", "memory_slots", "4")));

    // ==========================================
    // RAM Memory (21 items)
    // ==========================================
    seeds.add(new ProductSeedData("RAM-CORSAIR-TIT-64-6000", "Corsair Dominator Titanium DDR5 64GB (2x32GB) 6000MHz CL30", "RAM", 319.99, 25, Map.of("brand", "Corsair", "capacity_gb", "64", "speed_mhz", "6000", "type", "DDR5", "modules", "2x32GB")));
    seeds.add(new ProductSeedData("RAM-CORSAIR-VEN-32-6000", "Corsair Vengeance RGB DDR5 32GB (2x16GB) 6000MHz CL30", "RAM", 124.99, 60, Map.of("brand", "Corsair", "capacity_gb", "32", "speed_mhz", "6000", "type", "DDR5", "modules", "2x16GB")));
    seeds.add(new ProductSeedData("RAM-CORSAIR-VEN-32-5600", "Corsair Vengeance DDR5 32GB (2x16GB) 5600MHz CL36", "RAM", 99.99, 70, Map.of("brand", "Corsair", "capacity_gb", "32", "speed_mhz", "5600", "type", "DDR5", "modules", "2x16GB")));
    seeds.add(new ProductSeedData("RAM-GSKILL-TZ5-32-6400", "G.Skill Trident Z5 RGB DDR5 32GB (2x16GB) 6400MHz CL32", "RAM", 139.99, 45, Map.of("brand", "G.Skill", "capacity_gb", "32", "speed_mhz", "6400", "type", "DDR5", "modules", "2x16GB")));
    seeds.add(new ProductSeedData("RAM-GSKILL-TZ5-64-6000", "G.Skill Trident Z5 Neo RGB DDR5 64GB (2x32GB) 6000MHz CL30", "RAM", 219.99, 30, Map.of("brand", "G.Skill", "capacity_gb", "64", "speed_mhz", "6000", "type", "DDR5", "modules", "2x32GB")));
    seeds.add(new ProductSeedData("RAM-GSKILL-FLARE-32-6000", "G.Skill Flare X5 Series DDR5 32GB (2x16GB) 6000MHz AMD EXPO", "RAM", 104.99, 65, Map.of("brand", "G.Skill", "capacity_gb", "32", "speed_mhz", "6000", "type", "DDR5", "modules", "2x16GB")));
    seeds.add(new ProductSeedData("RAM-GSKILL-RIP-32-5600", "G.Skill Ripjaws S5 DDR5 32GB (2x16GB) 5600MHz", "RAM", 92.99, 50, Map.of("brand", "G.Skill", "capacity_gb", "32", "speed_mhz", "5600", "type", "DDR5", "modules", "2x16GB")));
    seeds.add(new ProductSeedData("RAM-KINGSTON-FURY-32-6000", "Kingston Fury Beast DDR5 32GB (2x16GB) 6000MHz RGB", "RAM", 119.99, 55, Map.of("brand", "Kingston", "capacity_gb", "32", "speed_mhz", "6000", "type", "DDR5", "modules", "2x16GB")));
    seeds.add(new ProductSeedData("RAM-KINGSTON-REN-64-6400", "Kingston Fury Renegade DDR5 64GB (2x32GB) 6400MHz", "RAM", 249.99, 20, Map.of("brand", "Kingston", "capacity_gb", "64", "speed_mhz", "6400", "type", "DDR5", "modules", "2x32GB")));
    seeds.add(new ProductSeedData("RAM-TEAM-DELTA-32-6000", "TeamGroup T-Force Delta RGB DDR5 32GB (2x16GB) 6000MHz White", "RAM", 109.99, 50, Map.of("brand", "TeamGroup", "capacity_gb", "32", "speed_mhz", "6000", "type", "DDR5", "modules", "2x16GB")));
    seeds.add(new ProductSeedData("RAM-CRUCIAL-PRO-32-5600", "Crucial Pro DDR5 32GB (2x16GB) 5600MHz Plug & Play", "RAM", 89.99, 60, Map.of("brand", "Crucial", "capacity_gb", "32", "speed_mhz", "5600", "type", "DDR5", "modules", "2x16GB")));
    seeds.add(new ProductSeedData("RAM-CRUCIAL-PRO-64-5600", "Crucial Pro DDR5 64GB (2x32GB) 5600MHz", "RAM", 169.99, 30, Map.of("brand", "Crucial", "capacity_gb", "64", "speed_mhz", "5600", "type", "DDR5", "modules", "2x32GB")));
    seeds.add(new ProductSeedData("RAM-ADATA-LANCER-32-6000", "ADATA XPG Lancer Blade RGB DDR5 32GB (2x16GB) 6000MHz", "RAM", 112.99, 40, Map.of("brand", "ADATA", "capacity_gb", "32", "speed_mhz", "6000", "type", "DDR5", "modules", "2x16GB")));
    seeds.add(new ProductSeedData("RAM-PATRIOT-VIPER-32-7000", "Patriot Viper Venom DDR5 32GB (2x16GB) 7000MHz Ultra Speed", "RAM", 149.99, 25, Map.of("brand", "Patriot", "capacity_gb", "32", "speed_mhz", "7000", "type", "DDR5", "modules", "2x16GB")));
    seeds.add(new ProductSeedData("RAM-CORSAIR-PRO-32-3600", "Corsair Vengeance RGB Pro DDR4 32GB (2x16GB) 3600MHz", "RAM", 79.99, 65, Map.of("brand", "Corsair", "capacity_gb", "32", "speed_mhz", "3600", "type", "DDR4", "modules", "2x16GB")));
    seeds.add(new ProductSeedData("RAM-CORSAIR-LPX-16-3200", "Corsair Vengeance LPX DDR4 16GB (2x8GB) 3200MHz", "RAM", 39.99, 90, Map.of("brand", "Corsair", "capacity_gb", "16", "speed_mhz", "3200", "type", "DDR4", "modules", "2x8GB")));
    seeds.add(new ProductSeedData("RAM-GSKILL-TZ-32-3600", "G.Skill Trident Z RGB DDR4 32GB (2x16GB) 3600MHz CL16", "RAM", 84.99, 50, Map.of("brand", "G.Skill", "capacity_gb", "32", "speed_mhz", "3600", "type", "DDR4", "modules", "2x16GB")));
    seeds.add(new ProductSeedData("RAM-KINGSTON-BEAST-16-3200", "Kingston Fury Beast DDR4 16GB (2x8GB) 3200MHz", "RAM", 38.99, 85, Map.of("brand", "Kingston", "capacity_gb", "16", "speed_mhz", "3200", "type", "DDR4", "modules", "2x8GB")));
    seeds.add(new ProductSeedData("RAM-KINGSTON-BEAST-32-3200", "Kingston Fury Beast DDR4 32GB (2x16GB) 3200MHz", "RAM", 69.99, 70, Map.of("brand", "Kingston", "capacity_gb", "32", "speed_mhz", "3200", "type", "DDR4", "modules", "2x16GB")));
    seeds.add(new ProductSeedData("RAM-TEAM-VULCAN-16-3200", "TeamGroup T-Force Vulcan Z DDR4 16GB (2x8GB) 3200MHz", "RAM", 34.99, 80, Map.of("brand", "TeamGroup", "capacity_gb", "16", "speed_mhz", "3200", "type", "DDR4", "modules", "2x8GB")));
    seeds.add(new ProductSeedData("RAM-CRUCIAL-PRO-32-3200", "Crucial Pro DDR4 32GB (2x16GB) 3200MHz", "RAM", 64.99, 55, Map.of("brand", "Crucial", "capacity_gb", "32", "speed_mhz", "3200", "type", "DDR4", "modules", "2x16GB")));

    // ==========================================
    // Storage (SD) (21 items)
    // ==========================================
    seeds.add(new ProductSeedData("SD-SAMSUNG-990PRO-2TB", "Samsung 990 PRO 2TB PCIe 4.0 NVMe M.2 SSD", "SD", 179.99, 50, Map.of("brand", "Samsung", "capacity_gb", "2000", "interface", "PCIe 4.0 NVMe", "form_factor", "M.2 2280", "read_speed_mb", "7450")));
    seeds.add(new ProductSeedData("SD-SAMSUNG-990PRO-1TB", "Samsung 990 PRO 1TB PCIe 4.0 NVMe M.2 SSD", "SD", 109.99, 60, Map.of("brand", "Samsung", "capacity_gb", "1000", "interface", "PCIe 4.0 NVMe", "form_factor", "M.2 2280", "read_speed_mb", "7450")));
    seeds.add(new ProductSeedData("SD-SAMSUNG-980PRO-2TB", "Samsung 980 PRO 2TB NVMe M.2 SSD with Heatsink", "SD", 159.99, 40, Map.of("brand", "Samsung", "capacity_gb", "2000", "interface", "PCIe 4.0 NVMe", "form_factor", "M.2 2280", "read_speed_mb", "7000")));
    seeds.add(new ProductSeedData("SD-WD-SN850X-2TB", "WD_BLACK SN850X 2TB PCIe Gen4 NVMe M.2 SSD", "SD", 164.99, 45, Map.of("brand", "Western Digital", "capacity_gb", "2000", "interface", "PCIe 4.0 NVMe", "form_factor", "M.2 2280", "read_speed_mb", "7300")));
    seeds.add(new ProductSeedData("SD-WD-SN850X-1TB", "WD_BLACK SN850X 1TB PCIe Gen4 NVMe M.2 SSD", "SD", 94.99, 55, Map.of("brand", "Western Digital", "capacity_gb", "1000", "interface", "PCIe 4.0 NVMe", "form_factor", "M.2 2280", "read_speed_mb", "7300")));
    seeds.add(new ProductSeedData("SD-WD-SN770-1TB", "WD_BLACK SN770 1TB NVMe M.2 SSD", "SD", 74.99, 70, Map.of("brand", "Western Digital", "capacity_gb", "1000", "interface", "PCIe 4.0 NVMe", "form_factor", "M.2 2280", "read_speed_mb", "5150")));
    seeds.add(new ProductSeedData("SD-WD-SN580-1TB", "WD Blue SN580 1TB NVMe M.2 SSD", "SD", 64.99, 80, Map.of("brand", "Western Digital", "capacity_gb", "1000", "interface", "PCIe 4.0 NVMe", "form_factor", "M.2 2280", "read_speed_mb", "4150")));
    seeds.add(new ProductSeedData("SD-CRUCIAL-T700-2TB", "Crucial T700 2TB Gen5 PCIe NVMe SSD Heatsink", "SD", 289.99, 20, Map.of("brand", "Crucial", "capacity_gb", "2000", "interface", "PCIe 5.0 NVMe", "form_factor", "M.2 2280", "read_speed_mb", "12400")));
    seeds.add(new ProductSeedData("SD-CRUCIAL-T500-2TB", "Crucial T500 2TB PCIe Gen4 NVMe M.2 SSD", "SD", 154.99, 35, Map.of("brand", "Crucial", "capacity_gb", "2000", "interface", "PCIe 4.0 NVMe", "form_factor", "M.2 2280", "read_speed_mb", "7400")));
    seeds.add(new ProductSeedData("SD-CRUCIAL-P3PLUS-2TB", "Crucial P3 Plus 2TB PCIe 4.0 3D NAND NVMe M.2", "SD", 119.99, 65, Map.of("brand", "Crucial", "capacity_gb", "2000", "interface", "PCIe 4.0 NVMe", "form_factor", "M.2 2280", "read_speed_mb", "5000")));
    seeds.add(new ProductSeedData("SD-CRUCIAL-P3PLUS-1TB", "Crucial P3 Plus 1TB PCIe 4.0 NVMe M.2", "SD", 68.99, 80, Map.of("brand", "Crucial", "capacity_gb", "1000", "interface", "PCIe 4.0 NVMe", "form_factor", "M.2 2280", "read_speed_mb", "5000")));
    seeds.add(new ProductSeedData("SD-KINGSTON-KC3000-2TB", "Kingston KC3000 2TB PCIe 4.0 NVMe M.2 SSD", "SD", 169.99, 35, Map.of("brand", "Kingston", "capacity_gb", "2000", "interface", "PCIe 4.0 NVMe", "form_factor", "M.2 2280", "read_speed_mb", "7000")));
    seeds.add(new ProductSeedData("SD-KINGSTON-NV2-1TB", "Kingston NV2 1TB PCIe 4.0 NVMe M.2 SSD", "SD", 58.99, 100, Map.of("brand", "Kingston", "capacity_gb", "1000", "interface", "PCIe 4.0 NVMe", "form_factor", "M.2 2280", "read_speed_mb", "3500")));
    seeds.add(new ProductSeedData("SD-KINGSTON-NV2-2TB", "Kingston NV2 2TB PCIe 4.0 NVMe M.2 SSD", "SD", 109.99, 75, Map.of("brand", "Kingston", "capacity_gb", "2000", "interface", "PCIe 4.0 NVMe", "form_factor", "M.2 2280", "read_speed_mb", "3500")));
    seeds.add(new ProductSeedData("SD-SABRENT-ROCKET4P-2TB", "Sabrent Rocket 4 Plus 2TB NVMe M.2 SSD", "SD", 189.99, 25, Map.of("brand", "Sabrent", "capacity_gb", "2000", "interface", "PCIe 4.0 NVMe", "form_factor", "M.2 2280", "read_speed_mb", "7100")));
    seeds.add(new ProductSeedData("SD-CORSAIR-MP600-2TB", "Corsair MP600 PRO LPX 2TB M.2 NVMe SSD", "SD", 164.99, 30, Map.of("brand", "Corsair", "capacity_gb", "2000", "interface", "PCIe 4.0 NVMe", "form_factor", "M.2 2280", "read_speed_mb", "7100")));
    seeds.add(new ProductSeedData("SD-TEAM-MP44-2TB", "TeamGroup MP44 2TB PCIe 4.0 SLC Cache NVMe SSD", "SD", 122.99, 45, Map.of("brand", "TeamGroup", "capacity_gb", "2000", "interface", "PCIe 4.0 NVMe", "form_factor", "M.2 2280", "read_speed_mb", "7400")));
    seeds.add(new ProductSeedData("SD-SAMSUNG-870EVO-1TB", "Samsung 870 EVO 1TB 2.5\" SATA III SSD", "SD", 89.99, 40, Map.of("brand", "Samsung", "capacity_gb", "1000", "interface", "SATA III", "form_factor", "2.5 Inch", "read_speed_mb", "560")));
    seeds.add(new ProductSeedData("SD-CRUCIAL-MX500-1TB", "Crucial MX500 1TB 3D NAND 2.5\" SATA SSD", "SD", 79.99, 50, Map.of("brand", "Crucial", "capacity_gb", "1000", "interface", "SATA III", "form_factor", "2.5 Inch", "read_speed_mb", "540")));
    seeds.add(new ProductSeedData("SD-SEAGATE-BARRA-4TB", "Seagate BarraCuda 4TB 5400 RPM 3.5\" Internal HDD", "SD", 84.99, 30, Map.of("brand", "Seagate", "capacity_gb", "4000", "interface", "SATA III", "form_factor", "3.5 Inch", "read_speed_mb", "190")));
    seeds.add(new ProductSeedData("SD-WD-BLUE-2TB-HDD", "WD Blue 2TB 7200 RPM 3.5\" Internal HDD", "SD", 59.99, 40, Map.of("brand", "Western Digital", "capacity_gb", "2000", "interface", "SATA III", "form_factor", "3.5 Inch", "read_speed_mb", "215")));

    // ==========================================
    // Cases (CASE) (21 items)
    // ==========================================
    seeds.add(new ProductSeedData("CASE-NZXT-H9-FLOW-B", "NZXT H9 Flow Dual-Chamber ATX Mid-Tower Black", "CASE", 159.99, 30, Map.of("brand", "NZXT", "form_factor", "ATX Mid-Tower", "color", "Matte Black", "side_panel", "Tempered Glass Dual Panoramic")));
    seeds.add(new ProductSeedData("CASE-NZXT-H9-FLOW-W", "NZXT H9 Flow Dual-Chamber ATX Mid-Tower White", "CASE", 159.99, 25, Map.of("brand", "NZXT", "form_factor", "ATX Mid-Tower", "color", "Matte White", "side_panel", "Tempered Glass Dual Panoramic")));
    seeds.add(new ProductSeedData("CASE-NZXT-H7-FLOW-RGB", "NZXT H7 Flow RGB High-Airflow Mid-Tower", "CASE", 149.99, 35, Map.of("brand", "NZXT", "form_factor", "ATX Mid-Tower", "color", "Black", "side_panel", "Tempered Glass")));
    seeds.add(new ProductSeedData("CASE-NZXT-H5-FLOW", "NZXT H5 Flow Compact High-Airflow Mid-Tower", "CASE", 94.99, 50, Map.of("brand", "NZXT", "form_factor", "ATX Mid-Tower", "color", "Black", "side_panel", "Tempered Glass with Bottom Fan")));
    seeds.add(new ProductSeedData("CASE-LIANLI-O11-EVO", "Lian Li O11 Dynamic EVO RGB Panoramic Case", "CASE", 169.99, 40, Map.of("brand", "Lian Li", "form_factor", "ATX Full-Tower", "color", "Black", "side_panel", "Dual Tempered Glass")));
    seeds.add(new ProductSeedData("CASE-LIANLI-O11-MINI", "Lian Li O11 Dynamic Mini Modular Case White", "CASE", 119.99, 30, Map.of("brand", "Lian Li", "form_factor", "Micro-ATX Mini-Tower", "color", "Snow White", "side_panel", "Tempered Glass")));
    seeds.add(new ProductSeedData("CASE-LIANLI-LANCOOL-216", "Lian Li Lancool 216 RGB Focus Airflow Case", "CASE", 99.99, 45, Map.of("brand", "Lian Li", "form_factor", "ATX Mid-Tower", "color", "Black", "side_panel", "Tempered Glass")));
    seeds.add(new ProductSeedData("CASE-LIANLI-A3-MATX", "Lian Li x DAN Cases A3-mATX Micro Chassis Wood/Mesh", "CASE", 89.99, 30, Map.of("brand", "Lian Li", "form_factor", "Micro-ATX", "color", "Black Wood Accent", "side_panel", "Full Mesh Ventilation")));
    seeds.add(new ProductSeedData("CASE-CORSAIR-4000D-AIR", "Corsair 4000D Airflow High-Airflow ATX Case", "CASE", 104.99, 60, Map.of("brand", "Corsair", "form_factor", "ATX Mid-Tower", "color", "Black", "side_panel", "Tempered Glass")));
    seeds.add(new ProductSeedData("CASE-CORSAIR-5000D-AIR", "Corsair 5000D Airflow Tempered Glass Mid-Tower", "CASE", 174.99, 25, Map.of("brand", "Corsair", "form_factor", "ATX Mid-Tower", "color", "White", "side_panel", "Tempered Glass")));
    seeds.add(new ProductSeedData("CASE-CORSAIR-5000X-RGB", "Corsair iCUE 5000X RGB All Glass Mid-Tower", "CASE", 214.99, 20, Map.of("brand", "Corsair", "form_factor", "ATX Mid-Tower", "color", "Black", "side_panel", "4x Tempered Glass Panels")));
    seeds.add(new ProductSeedData("CASE-FRACTAL-NORTH-BLK", "Fractal Design North Charcoal Black Real Walnut", "CASE", 139.99, 35, Map.of("brand", "Fractal Design", "form_factor", "ATX Mid-Tower", "color", "Charcoal Black / Walnut", "side_panel", "Tinted Tempered Glass")));
    seeds.add(new ProductSeedData("CASE-FRACTAL-NORTH-WHT", "Fractal Design North Chalk White Real Oak", "CASE", 139.99, 30, Map.of("brand", "Fractal Design", "form_factor", "ATX Mid-Tower", "color", "Chalk White / Oak", "side_panel", "Clear Tempered Glass")));
    seeds.add(new ProductSeedData("CASE-FRACTAL-MESHIFY-2", "Fractal Design Meshify 2 Compact RGB", "CASE", 129.99, 35, Map.of("brand", "Fractal Design", "form_factor", "ATX Mid-Tower", "color", "Black", "side_panel", "Tempered Glass")));
    seeds.add(new ProductSeedData("CASE-FRACTAL-POP-AIR", "Fractal Design Pop Air RGB High Airflow Case", "CASE", 89.99, 45, Map.of("brand", "Fractal Design", "form_factor", "ATX Mid-Tower", "color", "Cyan Core / Black", "side_panel", "Tempered Glass")));
    seeds.add(new ProductSeedData("CASE-PHANTEKS-NV5", "Phanteks NV5 Seamless Glass Panoramic Case", "CASE", 109.99, 35, Map.of("brand", "Phanteks", "form_factor", "ATX Mid-Tower", "color", "Satin Black", "side_panel", "Seamless Glass View")));
    seeds.add(new ProductSeedData("CASE-PHANTEKS-G360A", "Phanteks Eclipse G360A High Airflow RGB", "CASE", 79.99, 50, Map.of("brand", "Phanteks", "form_factor", "ATX Mid-Tower", "color", "Black", "side_panel", "Tempered Glass")));
    seeds.add(new ProductSeedData("CASE-MONTECH-AIR903", "Montech AIR 903 MAX E-ATX High Airflow with 4 Fans", "CASE", 74.99, 60, Map.of("brand", "Montech", "form_factor", "E-ATX / ATX", "color", "Black", "side_panel", "Tempered Glass")));
    seeds.add(new ProductSeedData("CASE-MONTECH-KING95", "Montech KING 95 PRO Curved Glass Showcase Case", "CASE", 149.99, 25, Map.of("brand", "Montech", "form_factor", "ATX Mid-Tower", "color", "Prussian Blue", "side_panel", "Curved Tempered Glass")));
    seeds.add(new ProductSeedData("CASE-HYTE-Y70-TOUCH", "Hyte Y70 Touch Infinite 4K LCD Panoramic Case", "CASE", 379.99, 10, Map.of("brand", "Hyte", "form_factor", "ATX Mid-Tower", "color", "Snow White", "side_panel", "Integrated 14.5-inch 4K Touch Screen")));
    seeds.add(new ProductSeedData("CASE-HYTE-Y60-RED", "Hyte Y60 Panoramic 3-Piece Glass Case Red", "CASE", 199.99, 15, Map.of("brand", "Hyte", "form_factor", "ATX Mid-Tower", "color", "Red & Black", "side_panel", "3-Piece Panoramic Glass")));

    // ==========================================
    // Power Supplies (PSU) (21 items)
    // ==========================================
    seeds.add(new ProductSeedData("PSU-CORSAIR-RM1000X", "Corsair RM1000x 1000W 80+ Gold Fully Modular", "PSU", 189.99, 35, Map.of("brand", "Corsair", "wattage", "1000", "efficiency", "80 Plus Gold", "modular", "Fully Modular")));
    seeds.add(new ProductSeedData("PSU-CORSAIR-RM850X", "Corsair RM850x 850W 80+ Gold Fully Modular Low-Noise", "PSU", 139.99, 50, Map.of("brand", "Corsair", "wattage", "850", "efficiency", "80 Plus Gold", "modular", "Fully Modular")));
    seeds.add(new ProductSeedData("PSU-CORSAIR-RM750E", "Corsair RM750e 750W 80+ Gold ATX 3.0 & PCIe 5.0", "PSU", 99.99, 65, Map.of("brand", "Corsair", "wattage", "750", "efficiency", "80 Plus Gold", "modular", "Fully Modular")));
    seeds.add(new ProductSeedData("PSU-CORSAIR-RM650", "Corsair RM650 650W 80+ Gold Fully Modular", "PSU", 89.99, 55, Map.of("brand", "Corsair", "wattage", "650", "efficiency", "80 Plus Gold", "modular", "Fully Modular")));
    seeds.add(new ProductSeedData("PSU-SEASONIC-GX850", "Seasonic Focus GX-850 850W 80+ Gold", "PSU", 139.99, 40, Map.of("brand", "Seasonic", "wattage", "850", "efficiency", "80 Plus Gold", "modular", "Fully Modular")));
    seeds.add(new ProductSeedData("PSU-SEASONIC-GX1000", "Seasonic Focus GX-1000 1000W 80+ Gold ATX 3.0", "PSU", 179.99, 30, Map.of("brand", "Seasonic", "wattage", "1000", "efficiency", "80 Plus Gold", "modular", "Fully Modular")));
    seeds.add(new ProductSeedData("PSU-SEASONIC-VTX1200", "Seasonic Vertex GX-1200 1200W ATX 3.0 PCIe 5.0", "PSU", 239.99, 20, Map.of("brand", "Seasonic", "wattage", "1200", "efficiency", "80 Plus Gold", "modular", "Fully Modular")));
    seeds.add(new ProductSeedData("PSU-EVGA-850GT", "EVGA SuperNOVA 850 GT 850W 80+ Gold", "PSU", 129.99, 40, Map.of("brand", "EVGA", "wattage", "850", "efficiency", "80 Plus Gold", "modular", "Fully Modular")));
    seeds.add(new ProductSeedData("PSU-EVGA-750G6", "EVGA SuperNOVA 750 G6 750W 80+ Gold Compact", "PSU", 109.99, 45, Map.of("brand", "EVGA", "wattage", "750", "efficiency", "80 Plus Gold", "modular", "Fully Modular")));
    seeds.add(new ProductSeedData("PSU-BEQUIET-PP12-850", "be quiet! Pure Power 12 M 850W 80+ Gold ATX 3.0", "PSU", 134.99, 35, Map.of("brand", "be quiet!", "wattage", "850", "efficiency", "80 Plus Gold", "modular", "Fully Modular")));
    seeds.add(new ProductSeedData("PSU-BEQUIET-SP12-1000", "be quiet! Straight Power 12 1000W 80+ Platinum", "PSU", 209.99, 20, Map.of("brand", "be quiet!", "wattage", "1000", "efficiency", "80 Plus Platinum", "modular", "Fully Modular")));
    seeds.add(new ProductSeedData("PSU-MSI-A850GL", "MSI MAG A850GL PCIE5 850W 80+ Gold Fully Modular", "PSU", 119.99, 50, Map.of("brand", "MSI", "wattage", "850", "efficiency", "80 Plus Gold", "modular", "Fully Modular")));
    seeds.add(new ProductSeedData("PSU-MSI-A1000G", "MSI MPG A1000G PCIE5 1000W 80+ Gold", "PSU", 179.99, 30, Map.of("brand", "MSI", "wattage", "1000", "efficiency", "80 Plus Gold", "modular", "Fully Modular")));
    seeds.add(new ProductSeedData("PSU-ASUS-THOR-1000", "ASUS ROG Thor 1000W Platinum II OLED Display", "PSU", 329.99, 15, Map.of("brand", "ASUS", "wattage", "1000", "efficiency", "80 Plus Platinum", "modular", "Fully Modular")));
    seeds.add(new ProductSeedData("PSU-ASUS-TUF-750", "ASUS TUF Gaming 750W 80+ Gold Military Grade", "PSU", 99.99, 55, Map.of("brand", "ASUS", "wattage", "750", "efficiency", "80 Plus Gold", "modular", "Fully Modular")));
    seeds.add(new ProductSeedData("PSU-THERMAL-GF3-850", "Thermaltake Toughpower GF3 850W PCIe 5.0 ATX 3.0", "PSU", 129.99, 35, Map.of("brand", "Thermaltake", "wattage", "850", "efficiency", "80 Plus Gold", "modular", "Fully Modular")));
    seeds.add(new ProductSeedData("PSU-COOLER-MWE-850", "Cooler Master MWE Gold 850 V2 Fully Modular", "PSU", 109.99, 45, Map.of("brand", "Cooler Master", "wattage", "850", "efficiency", "80 Plus Gold", "modular", "Fully Modular")));
    seeds.add(new ProductSeedData("PSU-GIGA-UD850GM", "Gigabyte UD850GM 850W 80+ Gold Ultra Durable", "PSU", 99.99, 40, Map.of("brand", "Gigabyte", "wattage", "850", "efficiency", "80 Plus Gold", "modular", "Fully Modular")));
    seeds.add(new ProductSeedData("PSU-DEEPCOOL-PQ850M", "DeepCool PQ850M 850W 80+ Gold Seasonic OEM", "PSU", 114.99, 35, Map.of("brand", "DeepCool", "wattage", "850", "efficiency", "80 Plus Gold", "modular", "Fully Modular")));
    seeds.add(new ProductSeedData("PSU-NZXT-C850", "NZXT C850 850W 80+ Gold ATX 3.0 Modular", "PSU", 124.99, 40, Map.of("brand", "NZXT", "wattage", "850", "efficiency", "80 Plus Gold", "modular", "Fully Modular")));
    seeds.add(new ProductSeedData("PSU-CORSAIR-SF750", "Corsair SF750 750W 80+ Platinum SFX Form Factor", "PSU", 179.99, 25, Map.of("brand", "Corsair", "wattage", "750", "efficiency", "80 Plus Platinum", "modular", "Fully Modular")));

    // ==========================================
    // CPU Coolers (COOLER) (21 items)
    // ==========================================
    seeds.add(new ProductSeedData("COOL-NZXT-KRAKEN-360-LCD", "NZXT Kraken Elite 360 RGB LCD Display AIO Liquid Cooler", "COOLER", 279.99, 20, Map.of("brand", "NZXT", "type", "Liquid AIO 360mm", "fan_size_mm", "120", "rgb", "RGB + 2.36\" LCD Screen")));
    seeds.add(new ProductSeedData("COOL-NZXT-KRAKEN-240-RGB", "NZXT Kraken 240 RGB High Performance AIO", "COOLER", 159.99, 30, Map.of("brand", "NZXT", "type", "Liquid AIO 240mm", "fan_size_mm", "120", "rgb", "ARGB Fans + Infinite Mirror")));
    seeds.add(new ProductSeedData("COOL-CORSAIR-H150I-LCD", "Corsair iCUE H150i ELITE LCD XT 360mm Liquid CPU Cooler", "COOLER", 289.99, 15, Map.of("brand", "Corsair", "type", "Liquid AIO 360mm", "fan_size_mm", "120", "rgb", "IPS LCD Screen + RGB")));
    seeds.add(new ProductSeedData("COOL-CORSAIR-H100I-RGB", "Corsair iCUE H100i RGB ELITE 240mm Liquid Cooler", "COOLER", 139.99, 35, Map.of("brand", "Corsair", "type", "Liquid AIO 240mm", "fan_size_mm", "120", "rgb", "RGB Pump Cap")));
    seeds.add(new ProductSeedData("COOL-ARCTIC-LF3-360", "ARCTIC Liquid Freezer III 360 A-RGB High Airflow", "COOLER", 139.99, 40, Map.of("brand", "Arctic", "type", "Liquid AIO 360mm", "fan_size_mm", "120", "rgb", "Addressable RGB + VRM Fan")));
    seeds.add(new ProductSeedData("COOL-ARCTIC-LF3-240", "ARCTIC Liquid Freezer III 240 A-RGB Compact", "COOLER", 109.99, 45, Map.of("brand", "Arctic", "type", "Liquid AIO 240mm", "fan_size_mm", "120", "rgb", "Addressable RGB + VRM Fan")));
    seeds.add(new ProductSeedData("COOL-DEEPCOOL-LT720", "DeepCool LT720 360mm Multidimensional Mirror AIO", "COOLER", 139.99, 30, Map.of("brand", "DeepCool", "type", "Liquid AIO 360mm", "fan_size_mm", "120", "rgb", "Infinity Mirror Geometric Block")));
    seeds.add(new ProductSeedData("COOL-DEEPCOOL-LS520", "DeepCool LS520 240mm High-Performance AIO", "COOLER", 99.99, 35, Map.of("brand", "DeepCool", "type", "Liquid AIO 240mm", "fan_size_mm", "120", "rgb", "ARGB Infinity Mirror")));
    seeds.add(new ProductSeedData("COOL-LIANLI-GALAHAD-360", "Lian Li Galahad II Trinity 360 SL-INF Daisy-Chain AIO", "COOLER", 189.99, 25, Map.of("brand", "Lian Li", "type", "Liquid AIO 360mm", "fan_size_mm", "120", "rgb", "Uni Fan SL-Infinity RGB")));
    seeds.add(new ProductSeedData("COOL-NOCTUA-NHD15-BLK", "Noctua NH-D15 chromax.black Dual-Tower Air Cooler", "COOLER", 119.99, 40, Map.of("brand", "Noctua", "type", "Dual-Tower Air Cooler", "fan_size_mm", "140", "rgb", "None (Stealth Black)")));
    seeds.add(new ProductSeedData("COOL-NOCTUA-NHU12S-BLK", "Noctua NH-U12S chromax.black 120mm Single-Tower", "COOLER", 79.99, 45, Map.of("brand", "Noctua", "type", "Single-Tower Air Cooler", "fan_size_mm", "120", "rgb", "None (Stealth Black)")));
    seeds.add(new ProductSeedData("COOL-THERMAL-PEERLESS-120", "Thermalright Peerless Assassin 120 SE Dual-Tower", "COOLER", 34.99, 95, Map.of("brand", "Thermalright", "type", "Dual-Tower Air Cooler", "fan_size_mm", "120", "rgb", "ARGB Top Accent")));
    seeds.add(new ProductSeedData("COOL-THERMAL-PHANTOM-120", "Thermalright Phantom Spirit 120 EVO 7 Heatpipes", "COOLER", 49.99, 80, Map.of("brand", "Thermalright", "type", "Dual-Tower Air Cooler", "fan_size_mm", "120", "rgb", "ARGB Stealth Accents")));
    seeds.add(new ProductSeedData("COOL-BEQUIET-DARKROCK5", "be quiet! Dark Rock Pro 5 270W TDP Silent Air Cooler", "COOLER", 99.99, 30, Map.of("brand", "be quiet!", "type", "Dual-Tower Air Cooler", "fan_size_mm", "135", "rgb", "None (Silent Wings Fans)")));
    seeds.add(new ProductSeedData("COOL-BEQUIET-PUREROCK2", "be quiet! Pure Rock 2 Black 150W TDP Cooler", "COOLER", 44.99, 50, Map.of("brand", "be quiet!", "type", "Single-Tower Air Cooler", "fan_size_mm", "120", "rgb", "None (Pure Wings Fan)")));
    seeds.add(new ProductSeedData("COOL-COOLER-HYPER212", "Cooler Master Hyper 212 Halo Black ARGB", "COOLER", 42.99, 65, Map.of("brand", "Cooler Master", "type", "Single-Tower Air Cooler", "fan_size_mm", "120", "rgb", "Halo ARGB Fan")));
    seeds.add(new ProductSeedData("COOL-DEEPCOOL-AK620-DIG", "DeepCool AK620 Digital Dual-Tower Real-Time Temp Display", "COOLER", 79.99, 45, Map.of("brand", "DeepCool", "type", "Dual-Tower Air Cooler", "fan_size_mm", "120", "rgb", "Real-Time Digital Temperature Display")));
    seeds.add(new ProductSeedData("COOL-DEEPCOOL-AK400", "DeepCool AK400 High-Performance Single Tower", "COOLER", 34.99, 70, Map.of("brand", "DeepCool", "type", "Single-Tower Air Cooler", "fan_size_mm", "120", "rgb", "None")));
    seeds.add(new ProductSeedData("COOL-ASUS-RYUJIN-360", "ASUS ROG Ryujin III 360 ARGB 3.5\" LCD Screen Liquid Cooler", "COOLER", 349.99, 12, Map.of("brand", "ASUS", "type", "Liquid AIO 360mm", "fan_size_mm", "120", "rgb", "3.5\" Full Color LCD Display")));
    seeds.add(new ProductSeedData("COOL-EK-NUCLEUS-360", "EK Nucleus AIO CR360 Lux D-RGB Enthusiast Cooler", "COOLER", 204.99, 20, Map.of("brand", "EKWB", "type", "Liquid AIO 360mm", "fan_size_mm", "120", "rgb", "Digital Addressable RGB")));
    seeds.add(new ProductSeedData("COOL-THERMAL-BURST-120", "Thermalright Burst Assassin 120 Refined SE", "COOLER", 24.99, 85, Map.of("brand", "Thermalright", "type", "Single-Tower Air Cooler", "fan_size_mm", "120", "rgb", "None")));

    // ==========================================
    // Monitors (MONITOR) (21 items)
    // ==========================================
    seeds.add(new ProductSeedData("MON-ASUS-PG27AQDM", "ASUS ROG Swift OLED PG27AQDM 27\" QHD 240Hz 0.03ms", "MONITOR", 899.99, 15, Map.of("brand", "ASUS", "screen_size_inches", "27.0", "resolution", "2560x1440 (2K QHD)", "refresh_rate_hz", "240", "panel_type", "OLED")));
    seeds.add(new ProductSeedData("MON-ASUS-VG27AQ", "ASUS TUF Gaming VG27AQ 27\" G-SYNC IPS 165Hz", "MONITOR", 279.99, 45, Map.of("brand", "ASUS", "screen_size_inches", "27.0", "resolution", "2560x1440 (2K QHD)", "refresh_rate_hz", "165", "panel_type", "Fast IPS")));
    seeds.add(new ProductSeedData("MON-LG-27GP850", "LG UltraGear 27GP850-B 27\" Nano IPS 1ms 165Hz", "MONITOR", 329.99, 40, Map.of("brand", "LG", "screen_size_inches", "27.0", "resolution", "2560x1440 (2K QHD)", "refresh_rate_hz", "165", "panel_type", "Nano IPS")));
    seeds.add(new ProductSeedData("MON-LG-34GP83A", "LG UltraGear 34GP83A-B 34\" Curved 21:9 WQHD 144Hz", "MONITOR", 649.99, 20, Map.of("brand", "LG", "screen_size_inches", "34.0", "resolution", "3440x1440 (Ultrawide WQHD)", "refresh_rate_hz", "144", "panel_type", "Nano IPS Curved")));
    seeds.add(new ProductSeedData("MON-SAMSUNG-G9-OLED", "Samsung Odyssey OLED G9 49\" Curved Dual QHD 240Hz 0.03ms", "MONITOR", 1399.99, 10, Map.of("brand", "Samsung", "screen_size_inches", "49.0", "resolution", "5120x1440 (Dual QHD)", "refresh_rate_hz", "240", "panel_type", "OLED Curved 1800R")));
    seeds.add(new ProductSeedData("MON-SAMSUNG-G7-4K", "Samsung Odyssey G7 28\" 4K UHD 144Hz 1ms IPS", "MONITOR", 549.99, 25, Map.of("brand", "Samsung", "screen_size_inches", "28.0", "resolution", "3840x2160 (4K UHD)", "refresh_rate_hz", "144", "panel_type", "IPS HDR400")));
    seeds.add(new ProductSeedData("MON-DELL-AW3423DWF", "Alienware AW3423DWF 34\" Curved QD-OLED 165Hz", "MONITOR", 799.99, 15, Map.of("brand", "Alienware", "screen_size_inches", "34.0", "resolution", "3440x1440 (Ultrawide WQHD)", "refresh_rate_hz", "165", "panel_type", "QD-OLED")));
    seeds.add(new ProductSeedData("MON-DELL-AW2725DF", "Alienware AW2725DF 27\" 360Hz QD-OLED Esports Monitor", "MONITOR", 899.99, 12, Map.of("brand", "Alienware", "screen_size_inches", "27.0", "resolution", "2560x1440 (2K QHD)", "refresh_rate_hz", "360", "panel_type", "QD-OLED")));
    seeds.add(new ProductSeedData("MON-GIGA-M27Q", "Gigabyte M27Q 27\" 170Hz 1440p KVM Gaming Monitor", "MONITOR", 249.99, 50, Map.of("brand", "Gigabyte", "screen_size_inches", "27.0", "resolution", "2560x1440 (2K QHD)", "refresh_rate_hz", "170", "panel_type", "Super Speed IPS")));
    seeds.add(new ProductSeedData("MON-GIGA-M32U", "Gigabyte M32U 32\" 4K 144Hz HDMI 2.1 Gaming Monitor", "MONITOR", 579.99, 20, Map.of("brand", "Gigabyte", "screen_size_inches", "32.0", "resolution", "3840x2160 (4K UHD)", "refresh_rate_hz", "144", "panel_type", "SS IPS")));
    seeds.add(new ProductSeedData("MON-MSI-MAG274QRF", "MSI Optix MAG274QRF-QD 27\" Rapid IPS Quantum Dot 165Hz", "MONITOR", 319.99, 35, Map.of("brand", "MSI", "screen_size_inches", "27.0", "resolution", "2560x1440 (2K QHD)", "refresh_rate_hz", "165", "panel_type", "Rapid IPS Quantum Dot")));
    seeds.add(new ProductSeedData("MON-MSI-323UPF", "MSI MAG 323UPF 32\" 4K 160Hz Rapid IPS Display", "MONITOR", 599.99, 20, Map.of("brand", "MSI", "screen_size_inches", "32.0", "resolution", "3840x2160 (4K UHD)", "refresh_rate_hz", "160", "panel_type", "Rapid IPS")));
    seeds.add(new ProductSeedData("MON-AOC-CQ27G2", "AOC CQ27G2 27\" Curved QHD 144Hz 1ms Gaming Monitor", "MONITOR", 219.99, 45, Map.of("brand", "AOC", "screen_size_inches", "27.0", "resolution", "2560x1440 (2K QHD)", "refresh_rate_hz", "144", "panel_type", "VA Curved 1500R")));
    seeds.add(new ProductSeedData("MON-AOC-24G2SP", "AOC 24G2SP 24\" FHD 165Hz Frameless IPS Gaming Monitor", "MONITOR", 149.99, 80, Map.of("brand", "AOC", "screen_size_inches", "24.0", "resolution", "1920x1080 (Full HD)", "refresh_rate_hz", "165", "panel_type", "IPS")));
    seeds.add(new ProductSeedData("MON-BENQ-EX2710Q", "BenQ MOBIUZ EX2710Q 27\" 165Hz QHD treVolo Speakers", "MONITOR", 329.99, 30, Map.of("brand", "BenQ", "screen_size_inches", "27.0", "resolution", "2560x1440 (2K QHD)", "refresh_rate_hz", "165", "panel_type", "IPS HDR400")));
    seeds.add(new ProductSeedData("MON-BENQ-XL2546K", "BenQ ZOWIE XL2546K 24.5\" 240Hz DyAc+ Esports Pro Monitor", "MONITOR", 469.99, 25, Map.of("brand", "BenQ", "screen_size_inches", "24.5", "resolution", "1920x1080 (Full HD)", "refresh_rate_hz", "240", "panel_type", "Fast TN DyAc+")));
    seeds.add(new ProductSeedData("MON-ACER-XV272U", "Acer Nitro XV272U 27\" 170Hz WQHD IPS FreeSync Premium", "MONITOR", 229.99, 55, Map.of("brand", "Acer", "screen_size_inches", "27.0", "resolution", "2560x1440 (2K QHD)", "refresh_rate_hz", "170", "panel_type", "Agile-Splendor IPS")));
    seeds.add(new ProductSeedData("MON-VIEWSONIC-VX2728", "ViewSonic Omni VX2728-2K 27\" 180Hz 0.5ms Fast IPS", "MONITOR", 209.99, 50, Map.of("brand", "ViewSonic", "screen_size_inches", "27.0", "resolution", "2560x1440 (2K QHD)", "refresh_rate_hz", "180", "panel_type", "Fast IPS")));
    seeds.add(new ProductSeedData("MON-CORSAIR-XENEON27", "Corsair XENEON 27QHD240 27\" OLED 240Hz 0.03ms", "MONITOR", 849.99, 15, Map.of("brand", "Corsair", "screen_size_inches", "27.0", "resolution", "2560x1440 (2K QHD)", "refresh_rate_hz", "240", "panel_type", "LG OLED Anti-Glare")));
    seeds.add(new ProductSeedData("MON-ASUS-PROART-PA278", "ASUS ProArt Display PA278CV 27\" 100% sRGB Color Accurate", "MONITOR", 349.99, 30, Map.of("brand", "ASUS", "screen_size_inches", "27.0", "resolution", "2560x1440 (2K QHD)", "refresh_rate_hz", "75", "panel_type", "ProArt IPS Calman Verified")));
    seeds.add(new ProductSeedData("MON-KOORUI-24E3", "KOORUI 24E3 24\" FHD 165Hz IPS Ultra-Budget Gaming Monitor", "MONITOR", 119.99, 90, Map.of("brand", "KOORUI", "screen_size_inches", "24.0", "resolution", "1920x1080 (Full HD)", "refresh_rate_hz", "165", "panel_type", "IPS")));

    // ==========================================
    // Peripherals (PERIPHERAL) (21 items)
    // ==========================================
    seeds.add(new ProductSeedData("PERIPH-LOGI-GPX2-BLK", "Logitech G PRO X Superlight 2 Wireless Gaming Mouse Black", "PERIPHERAL", 159.99, 50, Map.of("brand", "Logitech G", "category", "Mouse", "connectivity", "LIGHTSPEED Wireless / USB-C", "color", "Matte Black")));
    seeds.add(new ProductSeedData("PERIPH-LOGI-GPX2-MAG", "Logitech G PRO X Superlight 2 Wireless Gaming Mouse Magenta", "PERIPHERAL", 159.99, 30, Map.of("brand", "Logitech G", "category", "Mouse", "connectivity", "LIGHTSPEED Wireless / USB-C", "color", "Hot Magenta")));
    seeds.add(new ProductSeedData("PERIPH-RAZER-VIPER-V2", "Razer Viper V2 Pro Ultra-Lightweight Wireless Mouse White", "PERIPHERAL", 149.99, 45, Map.of("brand", "Razer", "category", "Mouse", "connectivity", "HyperSpeed Wireless", "color", "Mercury White")));
    seeds.add(new ProductSeedData("PERIPH-RAZER-DEATHADDER-V3", "Razer DeathAdder V3 Pro Ergonomic Esports Mouse", "PERIPHERAL", 149.99, 50, Map.of("brand", "Razer", "category", "Mouse", "connectivity", "HyperSpeed Wireless 8KHz", "color", "Black")));
    seeds.add(new ProductSeedData("PERIPH-STEEL-AEROX3", "SteelSeries Aerox 3 Wireless Ultra-Light 68g RGB Mouse", "PERIPHERAL", 79.99, 40, Map.of("brand", "SteelSeries", "category", "Mouse", "connectivity", "Quantum 2.0 Dual Wireless", "color", "Onyx Black")));
    seeds.add(new ProductSeedData("PERIPH-LOGI-G502X-PLUS", "Logitech G502 X PLUS LIGHTSPEED Wireless RGB Mouse", "PERIPHERAL", 139.99, 55, Map.of("brand", "Logitech G", "category", "Mouse", "connectivity", "LIGHTSPEED Wireless + LIGHTSYNC RGB", "color", "Black")));
    seeds.add(new ProductSeedData("PERIPH-GLORIOUS-MODEL-O", "Glorious Model O 2 Wireless Honeycomb Mouse", "PERIPHERAL", 89.99, 35, Map.of("brand", "Glorious", "category", "Mouse", "connectivity", "2.4GHz Wireless / Bluetooth", "color", "Matte White")));
    seeds.add(new ProductSeedData("PERIPH-WOOTING-60HE", "Wooting 60HE+ Hall Effect Analog Rapid Trigger Keyboard", "PERIPHERAL", 199.99, 25, Map.of("brand", "Wooting", "category", "Keyboard", "connectivity", "USB-C Detachable Braided", "color", "Classic Black")));
    seeds.add(new ProductSeedData("PERIPH-LOGI-G915-TKL", "Logitech G915 LIGHTSPEED Wireless RGB Mechanical Keyboard GL Tactile", "PERIPHERAL", 189.99, 35, Map.of("brand", "Logitech G", "category", "Keyboard", "connectivity", "LIGHTSPEED Wireless / Bluetooth", "color", "Carbon Brushed Aluminum")));
    seeds.add(new ProductSeedData("PERIPH-RAZER-HUNTSMAN-V3", "Razer Huntsman V3 Pro Analog Optical Esports Keyboard", "PERIPHERAL", 249.99, 20, Map.of("brand", "Razer", "category", "Keyboard", "connectivity", "USB-C Braided", "color", "Black / Brushed Metal")));
    seeds.add(new ProductSeedData("PERIPH-CORSAIR-K70-MAX", "Corsair K70 MAX RGB Magnetic-Mechanical Keyboard MGX Switches", "PERIPHERAL", 229.99, 25, Map.of("brand", "Corsair", "category", "Keyboard", "connectivity", "USB-C 8000Hz Hyper-Polling", "color", "Gunmetal Black")));
    seeds.add(new ProductSeedData("PERIPH-STEEL-APEX-PRO", "SteelSeries Apex Pro TKL Wireless Gen 3 OmniPoint 3.0 Keyboard", "PERIPHERAL", 269.99, 20, Map.of("brand", "SteelSeries", "category", "Keyboard", "connectivity", "2.4GHz Quantum / Bluetooth", "color", "Black with OLED Screen")));
    seeds.add(new ProductSeedData("PERIPH-KEYCHRON-Q1-PRO", "Keychron Q1 Pro Wireless Custom Mechanical Keyboard QMK/VIA", "PERIPHERAL", 209.99, 25, Map.of("brand", "Keychron", "category", "Keyboard", "connectivity", "Bluetooth 5.1 / Type-C Wired", "color", "Carbon Black CNC Aluminum")));
    seeds.add(new ProductSeedData("PERIPH-HYPERX-CLOUD3-WL", "HyperX Cloud III Wireless Gaming Headset 120h Battery", "PERIPHERAL", 149.99, 45, Map.of("brand", "HyperX", "category", "Headset", "connectivity", "2.4GHz Fast Wireless / USB-C", "color", "Black & Red")));
    seeds.add(new ProductSeedData("PERIPH-STEEL-NOVA-PRO", "SteelSeries Arctis Nova Pro Wireless Multi-System Headset", "PERIPHERAL", 349.99, 20, Map.of("brand", "SteelSeries", "category", "Headset", "connectivity", "Dual Wireless 2.4GHz + Bluetooth Base Station", "color", "Black")));
    seeds.add(new ProductSeedData("PERIPH-RAZER-BLACKSHARK-V2", "Razer BlackShark V2 Pro Wireless Esports Headset 2024", "PERIPHERAL", 199.99, 35, Map.of("brand", "Razer", "category", "Headset", "connectivity", "HyperSpeed Wireless / Bluetooth", "color", "Black")));
    seeds.add(new ProductSeedData("PERIPH-LOGI-GPROX2-HEAD", "Logitech G PRO X 2 LIGHTSPEED Wireless Graphene Driver Headset", "PERIPHERAL", 249.99, 30, Map.of("brand", "Logitech G", "category", "Headset", "connectivity", "LIGHTSPEED Wireless / Bluetooth / 3.5mm", "color", "White")));
    seeds.add(new ProductSeedData("PERIPH-ELGATO-STREAMDECK", "Elgato Stream Deck MK.2 15 Customizable LCD Keys", "PERIPHERAL", 149.99, 40, Map.of("brand", "Elgato", "category", "Streaming Controller", "connectivity", "USB-C", "color", "Matte Black")));
    seeds.add(new ProductSeedData("PERIPH-ELGATO-WAVE3", "Elgato Wave:3 Premium USB Condenser Microphone Anti-Clipping", "PERIPHERAL", 149.99, 35, Map.of("brand", "Elgato", "category", "Microphone", "connectivity", "USB-C 24-bit 96kHz", "color", "Black")));
    seeds.add(new ProductSeedData("PERIPH-SHURE-MV7", "Shure MV7 USB/XLR Dynamic Podcast & Gaming Microphone", "PERIPHERAL", 249.99, 20, Map.of("brand", "Shure", "category", "Microphone", "connectivity", "Dual USB / XLR", "color", "Black")));
    seeds.add(new ProductSeedData("PERIPH-LOGI-G240-PAD", "Logitech G240 Cloth Gaming Mouse Pad Large", "PERIPHERAL", 19.99, 100, Map.of("brand", "Logitech G", "category", "Mousepad", "connectivity", "Non-Slip Rubber Base", "color", "Black")));

    for (ProductSeedData d : seeds) {
      if (!productRepository.existsById(d.sku)) {
        Component c = compMap.get(d.componentSkuPrefix);
        if (c != null) {
          Product p = new Product(d.sku, d.name, c, d.price, d.stock);
          p.setActive(true);
          for (Map.Entry<String, String> attr : d.attributes.entrySet()) {
            p.updateAttribute(attr.getKey(), attr.getValue());
          }
          productRepository.save(p);
        }
      }
    }

    log.info("Successfully finished seeding {} total products in database!", productRepository.count());
  }

  private Component getOrCreateComponent(
      String skuPrefix, String displayName, Map<String, FieldType> fields) {
    return componentRepository
        .findById(skuPrefix)
        .orElseGet(
            () -> {
              Component comp = new Component(skuPrefix, displayName);
              for (Map.Entry<String, FieldType> f : fields.entrySet()) {
                comp.addField(f.getKey(), f.getValue(), false, null);
              }
              return componentRepository.save(comp);
            });
  }

  private record ProductSeedData(
      String sku,
      String name,
      String componentSkuPrefix,
      double price,
      int stock,
      Map<String, String> attributes) {}
}
