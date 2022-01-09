package cn.tx.service.imp;

import cn.tx.serice.PayService;

public class WxPay implements PayService {
    @Override
    public void pay() {
        System.out.println("微信支付");
    }
}
