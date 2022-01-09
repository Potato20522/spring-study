package cn.tx.sboot.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@ConfigurationProperties(prefix = "yaml.level")
public class YamlModel {


    private String str;

    private String specialStr;

    private int num;

    private double Dnum;

    private Date birth;

    private List<String> list = new ArrayList<>();


    private Set<Integer> set = new HashSet<>();

    private Map<String, String> map = new HashMap<>();

    private List<User> users = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public Set<Integer> getSet() {
        return set;
    }

    public void setSet(Set<Integer> set) {
        this.set = set;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public String getSpecialStr() {
        return specialStr;
    }

    public void setSpecialStr(String specialStr) {
        this.specialStr = specialStr;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getDnum() {
        return Dnum;
    }

    public void setDnum(double dnum) {
        Dnum = dnum;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return "YamlModel{" +
                "str='" + str + '\'' +
                ", specialStr='" + specialStr + '\'' +
                ", num=" + num +
                ", Dnum=" + Dnum +
                ", birth=" + birth +
                ", list=" + list +
                ", set=" + set +
                ", map=" + map +
                ", users=" + users +
                '}';
    }
}
