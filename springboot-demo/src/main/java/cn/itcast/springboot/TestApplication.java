package cn.itcast.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 引导类：springboot应用的入口
 */
@EnableAutoConfiguration//启动springboot应用的自动配置
@ComponentScan//类似于<context:component-scan base-package=""> 扫描该类所在的包以及他的子子孙孙包。
@SpringBootConfiguration//使用组合注解，相当@EnableAutoApplication和@ComponentScan的合体。
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class,args);

    }
}
