package com.example.signal.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean(name = "controllerJdbcTemplate")
    public JdbcTemplate controllerJdbcTemplate() {
        DataSource ds = DataSourceBuilder.create()
                .url("jdbc:mysql://localhost:3306/signalcontrollerdb")
                .username("root")
                .password("localhost@123")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
        return new JdbcTemplate(ds);
    }

    @Bean(name = "manipulatorJdbcTemplate")
    public JdbcTemplate manipulatorJdbcTemplate() {
        DataSource ds = DataSourceBuilder.create()
                .url("jdbc:mysql://localhost:3306/signalmanipulatordb")
                .username("root")
                .password("localhost@123")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
        return new JdbcTemplate(ds);
    }

    @Bean(name = "pedestrianJdbcTemplate")
    public JdbcTemplate pedestrianJdbcTemplate() {
        DataSource ds = DataSourceBuilder.create()
                .url("jdbc:mysql://localhost:3306/pedestriansignalmanipulatordb")
                .username("root")
                .password("localhost@123")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
        return new JdbcTemplate(ds);
    }
}

