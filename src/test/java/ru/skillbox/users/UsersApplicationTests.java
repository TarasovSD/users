package ru.skillbox.users;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.liquibase.enabled=false"})
class UsersApplicationTests {

	@Test
	void contextLoads() {
	}

}
