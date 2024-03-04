package com.tobeto.config;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlyWayConfig {
	@Autowired
	private DataSource dataSource;

	@Bean
	FlywayMigrationStrategy flywayMigrationStrategy() {
		return (flywayOld) -> {
			Flyway flyway = Flyway.configure().dataSource(this.dataSource).table("flyway").load();
			flyway.baseline();
			try {
				flyway.migrate();
			} catch (Exception e) {
				flyway.repair();
				flyway.migrate();
			}

		};
	}
}
