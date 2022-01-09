package cn.tx.sboot.mapper;

import cn.tx.sboot.model.Person;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface PersonMapper {

    @Select("select * from person t where t.pid = #{pid}")
    public Person selectById(int pid);

}
