package cn.itcast.service.controller;

import cn.itcast.service.client.UserClient;
import cn.itcast.service.pojo.User;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;


@Controller
@RequestMapping("consumer/user")
//@DefaultProperties(defaultFallback = "fallbackMethod")//定义全局的熔断方法
public class UserController {
    @Autowired
    private UserClient userClient;
//    @Autowired
//    private RestTemplate restTemplate;
//    @Autowired
//    private DiscoveryClient discoveryClient;
    @GetMapping
    @ResponseBody
//    @HystrixCommand//全局的时候也需要加注解 只是不需要指定方法了。
    public String queryUserById(@RequestParam("id") Long id){//返回值应该和熔断方法的信息一样。
//        List<ServiceInstance> instances = discoveryClient.getInstances("service-provider");
//        ServiceInstance in = instances.get(0);
//        return this.restTemplate.getForObject("http://127.0.0.1:8081/user/"+id, User.class);
//        return this.restTemplate.getForObject("http://"+in.getHost()+":"+in.getPort()+"/user/"+id, User.class);
//        return this.restTemplate.getForObject("http://service-provider/user/"+id, String.class);
        return  userClient.queryUserById(id).toString();
    }
//熔断方法
//    public String fallbackMethod(){
//
//            return "服务器正忙！请稍后再试！";
//    }
}
