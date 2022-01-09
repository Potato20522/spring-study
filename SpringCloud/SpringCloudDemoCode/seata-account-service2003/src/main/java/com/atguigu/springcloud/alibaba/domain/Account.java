package com.atguigu.springcloud.alibaba.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private Long id;
    private Long userId;//用户id
    private BigDecimal total;//总额度
    private BigDecimal used;//已用额度
    private BigDecimal residue;//剩余额度
}
