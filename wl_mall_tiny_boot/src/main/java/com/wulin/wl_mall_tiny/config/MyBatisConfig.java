package com.wulin.wl_mall_tiny.config;


import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;


/*
*  mybatis 配置类*/
@Configuration
@MapperScan("com.wulin.wl_mall_tiny.mbg.mapper")
public class MyBatisConfig {
}


/*
*
* mapperscan注解，这个注解告诉mybatis扫描指定保重的所有的mapper接口，并将他们注册称为mybatis的mapper。
* 这使得你可以直接通过接口调用相应的数据库操作，而不需要手动创建sqlsession或者是mapper的实现类
*
* mybatis与spring的集成，当使用spring框架的时候，通常会使用@configuration注解来定义配置类，结合mapperscan，
* 这个配置嘞不仅仅是一个普通的spring配置类，他还明确的指示了要使用mybatis的功能，并配置了mapper的自动扫描和注册。
*
* */