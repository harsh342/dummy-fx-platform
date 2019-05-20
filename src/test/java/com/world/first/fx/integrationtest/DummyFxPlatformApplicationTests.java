package com.world.first.fx.integrationtest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment =WebEnvironment.RANDOM_PORT)
public class DummyFxPlatformApplicationTests {
	
	@Autowired
	private WebTestClient webTestClient;

	@Test
	public void contextLoads() {
		webTestClient.get().uri("/v2/api-docs").exchange().expectStatus().isOk();
	}

}
