package com.potato.event.repository;

import com.potato.event.entity.User;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private Map<String, User> store = new HashMap<>();
}