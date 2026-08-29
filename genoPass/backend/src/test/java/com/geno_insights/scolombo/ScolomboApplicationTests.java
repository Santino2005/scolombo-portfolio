package com.geno_insights.scolombo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ScolomboApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainMethodTest() {
		ScolomboApplication app = new ScolomboApplication();
		assertNotNull(app);
	}
}

