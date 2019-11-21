package com.mongodb.demo.service;

import com.mongodb.demo.domain.Admin;

public interface AdminService {
    Admin findAdminByName(String name) throws Exception;
}
