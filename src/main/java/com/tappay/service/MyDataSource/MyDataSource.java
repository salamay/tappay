package com.tappay.service.MyDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Component
public class MyDataSource {
    private static String PROP_DB_DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    private static String PROP_DB_URL = "jdbc:mysql://localhost:3306/tappay?serverTimezone=UTC";
    private static String PROP_DB_USER = "salam";
    private static String PROP_DB_PASS = "rootroot";
    private static String PACKAGE = "com.tappay.service";
    private Logger logger= LoggerFactory.getLogger(this.getClass().getName());

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        logger.info("Setting up Local container entity manager");
        LocalContainerEntityManagerFactoryBean em=new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(PACKAGE);
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        em.setJpaProperties(properties);
        return em;
    }


    @Bean
    public DataSource dataSource(){
        logger.info("Setting up data source");
        DriverManagerDataSource dataSource=new DriverManagerDataSource();
        dataSource.setDriverClassName(PROP_DB_DRIVER_CLASS);
        dataSource.setUrl(PROP_DB_URL);
        dataSource.setUsername(PROP_DB_USER);
        dataSource.setPassword(PROP_DB_PASS);
        return dataSource;
    }

}
