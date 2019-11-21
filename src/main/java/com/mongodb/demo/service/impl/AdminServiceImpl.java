package com.mongodb.demo.service.impl;

import com.mongodb.demo.domain.Admin;
import com.mongodb.demo.domain.QAdmin;
import com.mongodb.demo.repository.AdminRepository;
import com.mongodb.demo.service.AdminService;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;


    @Override
    public Admin findAdminByName(String name) throws Exception {

        QAdmin qAdmin= QAdmin.admin;

        Predicate predicate = qAdmin.name.eq(name);

        return adminRepository.findOne(predicate);
    }
}
