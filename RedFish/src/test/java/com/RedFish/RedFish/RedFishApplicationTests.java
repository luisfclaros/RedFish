package com.RedFish.RedFish;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@Disabled("Requires Docker/Testcontainers; infrastructure validation will be enabled in a later HU.")
class RedFishApplicationTests {

	@Test
	void contextLoads() {
	}

}
