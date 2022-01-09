package cn.tx.service;

import cn.tx.model.Emp;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface EmpService {

    public void insert(Emp emp);

    public Emp getById(int empId);

    public List<Emp> list();

    public void update(Emp emp);

    public void delete(int empId);
}
