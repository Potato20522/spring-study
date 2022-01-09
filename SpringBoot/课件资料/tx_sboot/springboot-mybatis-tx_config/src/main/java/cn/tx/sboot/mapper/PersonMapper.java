package cn.tx.sboot.mapper;

import cn.tx.sboot.model.Person;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PersonMapper {

    public Person selectById(int pid);

    public List<Person> selectAll();

    public void insert(Person p);

    public void delete(int pid);
}
