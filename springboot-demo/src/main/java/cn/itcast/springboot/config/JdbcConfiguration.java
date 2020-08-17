package cn.itcast.springboot.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration//申明一个类是一个java配置类，相当于一个xml配置文件
//@EnableConfigurationProperties(JdbcProperties.class)
public class JdbcConfiguration {
//    @Value("${jdbc.driverClassName}")
//    private  String driverClassName;
//    @Value("${jdbc.url}")
//    private  String url;
//    @Value("${jdbc.password}")
//    private  String password;
//    @Value("${jdbc.username}")
//    private  String username;
//     @Autowired
//    private  JdbcProperties jdbcProperties;
     @Bean//把方法返回的值注入到spring容器中
     @ConfigurationProperties(prefix = "jdbc")
    public DataSource dataSource(){
         //使用this.xx  是为了区分全局变量和局部变量。
         DruidDataSource dataSource = new DruidDataSource();
         return dataSource;
     }
}
