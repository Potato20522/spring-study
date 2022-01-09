package cn.tx.sboot.controller;

import cn.tx.sboot.model.Role;
import cn.tx.sboot.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ThymeleafController {


    @GetMapping("hello")
    public String hello(Model model){
        model.addAttribute("hello", "hello txjava");

        return "hello";
    }


    @GetMapping("hello1")
    public String hello1(Model model, HttpSession session){
        User user = new User("亮哥", 20);
        user.setRole(new Role("教练"));

        model.addAttribute("user", user);

        List<User> users = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            users.add(new User("亮哥"+i, 20+i));
        }
        model.addAttribute("users", users);
        model.addAttribute("now", new Date());
        model.addAttribute("num", 0.2);
        model.addAttribute("name", "txjavatxjava");
        model.addAttribute("strarr", new String[]{"aa","bb","cc"});
        model.addAttribute("strs", "aa-bb-cc");

        session.setAttribute("user", user);
        return "hello";
    }


    @ResponseBody
    @GetMapping("order/details")
    public String details(String itemName, Model model){
        System.out.println(itemName);
        return "ok";
    }

    @GetMapping("literals")
    public String literals(Model model){
        model.addAttribute("isAdmin", true);
        return "literals";
    }


    @GetMapping("textopt")
    public String hello1(Model model){
        User user = new User("亮哥", 20);
        user.setRole(new Role("教练"));
        model.addAttribute("user", user);
        model.addAttribute("execMode", "dev");
        return "textopt";
    }



    @GetMapping("each")
    public String each(Model model, HttpSession session){

        List<User> users = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            users.add(new User("亮哥"+i, 20+i));
        }
        model.addAttribute("users", users);

        return "each";
    }

    @GetMapping("condition")
    public String condition(Model model, HttpSession session){

        List<User> users = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            User user = new User("亮哥" + i, 20 + i);
            if(i % 3 == 0){
                user.setRole(new Role("开发人员"));
            }
            users.add(user);
        }
        model.addAttribute("users", users);

        return "condition";
    }

    @GetMapping("switch")
    public String switch1(Model model,String roleName){

        Role role = new Role(roleName);
        model.addAttribute("role", role);

        return "switch";
    }


    @GetMapping("attr")
    public String attr(Model model,String roleName){



        return "attr";
    }

}
