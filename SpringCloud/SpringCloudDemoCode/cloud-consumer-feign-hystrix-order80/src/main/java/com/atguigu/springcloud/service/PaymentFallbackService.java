package com.atguigu.springcloud.service;

import org.springframework.stereotype.Component;

@Component
public class PaymentFallbackService implements PaymentHystrixService {
    @Override
    public String PaymentInfoOK(Integer id) {
        return "----PaymentHystrixService fallback PaymentInfoOK (⊙o⊙)？";
    }

    @Override
    public String PaymentInfoTimeout(Integer id) {
        return "----PaymentHystrixService fallback PaymentInfoTimeout (⊙o⊙)？";
    }
}
