package cn.tx.sboot.model;

import lombok.Data;

@Data
public class Role {

    private String roleName;

    public Role(String roleName) {
        this.roleName = roleName;
    }
}
