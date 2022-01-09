package com.atguigu.springcloud.alibaba.domain;

import lombok.Data;

@Data
public class Storage {
    private Long id;
    private Long productId;//产品id
    private Integer total;//总库存
    private Integer used;//已用库存
    private Integer residue;//剩余库存
}
