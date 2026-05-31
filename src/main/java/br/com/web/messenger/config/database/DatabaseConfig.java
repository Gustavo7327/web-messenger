package br.com.web.messenger.config.database;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "br.com.web.messenger.repository.jpa")
@EnableMongoRepositories(basePackages = "br.com.web.messenger.repository.mongo")
public class DatabaseConfig {
}