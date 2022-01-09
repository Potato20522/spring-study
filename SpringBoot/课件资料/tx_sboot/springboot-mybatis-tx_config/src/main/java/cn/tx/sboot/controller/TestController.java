package cn.tx.sboot.controller;

import cn.tx.sboot.mapper.PersonMapper;
import cn.tx.sboot.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private PersonMapper personMapper;

    @RequestMapping("selectById")
    public Person selectById(){
        return personMapper.selectById(1);
    }

    @RequestMapping("selectAll")
    public List<Person> selectAll(){
        return personMapper.selectAll();
    }

    @RequestMapping("insert")
    public String insert(Person p){
        personMapper.insert(p);
        return "success";
    }
}
