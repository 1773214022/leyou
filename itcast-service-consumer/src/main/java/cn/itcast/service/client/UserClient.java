package cn.itcast.service.client;

import cn.itcast.service.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
/**
 * 如何去使用feign
 * 1.首先我们创建一个接口，在接口上使用@feignClient注解，并且在其启动类中开启@EnableFeignClients 注解，再在接口中使用被调用接口的方法。
 * 2.我们在消费者控制层中去注入该接口。
 * 3.使用feign接口中的方法来实现。
 */
@FeignClient(value = "service-provider")//声明一个服务的id，看看是哪个服务的。
public interface UserClient {
    @GetMapping("{id}")
    public User queryUserById(@PathVariable("id") Long id);
}
