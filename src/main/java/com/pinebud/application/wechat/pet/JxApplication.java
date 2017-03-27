package com.pinebud.application.wechat.pet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by jiangyuesong on 2017/3/17 0017.
 *
 */
@SpringBootApplication
public class JxApplication {
    private static final Logger log= LoggerFactory.getLogger(JxApplication.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(new Object[]{JxApplication.class}, args);

    }

    @PostConstruct
    public void init(){
        log.info("com.JxApplication.init() ...");
    }
    @PreDestroy
    public void  destroy(){
        log.info("com.JxApplication cleanup...");
    }


    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource ds=new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:5432/pet?useUnicode=true&amp;characterEncoding=UTF-8");
        ds.setUsername("postgres");
        ds.setPassword("root");
        return ds;
    }

    //jpa的配置存在问题，所以这里直接使用了hibernate的配置，使用版本5.1.0可以解决
    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource){
        LocalSessionFactoryBean sfb=new LocalSessionFactoryBean();
        sfb.setDataSource(dataSource);
        sfb.setPackagesToScan("com.pinebud.application.wechatsangzi.service");
        Properties props=new Properties();
        props.setProperty("dialect","org.hibernate.dialect.PostgreSQL95Dialect");
        sfb.setHibernateProperties(props);
        try {
            sfb.afterPropertiesSet();
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        return sfb;
    }

    /* @Bean
    public JpaVendorAdapter jpaVendorAdapter(){
        HibernateJpaVendorAdapter adapter=new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.POSTGRESQL);
        adapter.setShowSql(true);
        adapter.setGenerateDdl(false);
        adapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQL95Dialect");
        return adapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(DataSource dataSource,JpaVendorAdapter jpaVendorAdapter){
        LocalContainerEntityManagerFactoryBean emfb=new LocalContainerEntityManagerFactoryBean();
        emfb.setDataSource(dataSource);
        emfb.setJpaVendorAdapter(jpaVendorAdapter);
        emfb.setPackagesToScan("org.uvlab.cloud.service.weixin");
        return emfb;
    }*/

    @Bean
    public PersistenceAnnotationBeanPostProcessor paPostProcessor(){
        return new PersistenceAnnotationBeanPostProcessor();
    }

    @Bean
    public BeanPostProcessor persistenceTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory em) {
        return new JpaTransactionManager(em);
    }

}
