package com.potato.webflux.service;

import com.potato.webflux.entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//用户操作接口
public interface UserService { 
    //根据id查询用户
    //根据主键查询，返回一个值，所以用Mono
    Mono<User> getUserById(int id);
    //查询所有用户
    //返回多个值，用Flux
    Flux<User> getAllUser();
    //添加用户 
    Mono<Void> saveUserInfo(Mono<User> user);
}