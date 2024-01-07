package com.pagepal.capstone;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = {"com.pagepal.capstone.repositories.postgre"})
@EntityScan(basePackages = {"com.pagepal.capstone.entities.postgre"})
@ComponentScan(basePackages = {"com.pagepal.capstone"})
@DataJpaTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
public class TestConfiguration {
}
