package cn.tx.service.impl;

import cn.tx.mapper.EmpMapper;
import cn.tx.model.Emp;
import cn.tx.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;


    @Override
    public void insert(Emp emp) {
        empMapper.insert(emp);
    }

    @Override
    public Emp getById(int empId) {
        return empMapper.getById(empId);
    }

    @Override
    public List<Emp> list() {
        return empMapper.list();
    }

    @Override
    public void update(Emp emp) {
        empMapper.update(emp);
    }

    @Override
    public void delete(int empId) {
        empMapper.delete(empId);
    }
}
