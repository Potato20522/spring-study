package cn.tx.service.impl;

import cn.tx.serice.PayService;

public class AliPay  implements PayService {
    @Override
    public void pay() {
        System.out.println("支付宝支付");
    }
}
