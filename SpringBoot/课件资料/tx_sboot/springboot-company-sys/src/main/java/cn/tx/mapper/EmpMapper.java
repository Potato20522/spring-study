package cn.tx.mapper;

import cn.tx.model.Emp;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface EmpMapper {

    @Insert("insert into emp values(null, #{username}, #{password}, #{pAddr}, #{gender}, #{birth})")
    public void insert(Emp emp);

    @Select("select * from emp where pid = #{empId}")
    public Emp getById(int empId);

    @Select("select * from emp ")
    public List<Emp> list();

    @Update("update emp set username = #{username}, " +
            "password = #{password}, " +
            "p_addr = #{pAddr}, " +
            "gender = #{gender}, " +
            "birth = #{birth} where pid = #{pid}")
    public void update(Emp emp);

    @Delete("delete from emp where pid = #{empId}")
    public void delete(int empId);
}
