package cn.itcast.user.contorller;
import cn.itcast.user.pojo.User;
import cn.itcast.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
    @ResponseBody
    @GetMapping("{id}")
   public User queryUserById(@PathVariable("id")Long id){
        System.out.println(this.userService.queryUserById(id));
        return this.userService.queryUserById(id);


   }








    @GetMapping("test")
    @ResponseBody
    public String test(){

        return "hello,user!";

    }
}
