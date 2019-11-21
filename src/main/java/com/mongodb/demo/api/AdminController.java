package com.mongodb.demo.api;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.demo.domain.Admin;
import com.mongodb.demo.repository.AdminRepository;

import com.mongodb.demo.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by eric on 2017/5/11.
 */
@RestController
@RequestMapping("/api/admin")
@Api("Admin相关的API,用于测试mongodb复杂查询")
public class AdminController {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AdminService adminService;


    @ApiOperation("增加一个Admin")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public Admin addAdmin(@RequestParam("name") String name,
                          @RequestParam("sex") Integer sex,
                          @RequestParam("address") String address) {
        Admin admin = new Admin();
        admin.setName(name);
        admin.setSex(sex);
        admin.setAddress(address);
        return adminRepository.save(admin);
    }

    @ApiOperation("获取所有的Admin")
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public List<Admin> getAllAdmin() {
        return adminRepository.findAll();
    }

    @ApiOperation("根据名称获取Admin")
    @RequestMapping(value = "/getAdminByName", method = RequestMethod.GET)
    public Admin findAdminByName(@RequestParam("name") String name) throws Exception{
        return adminService.findAdminByName(name);
    }

    @ApiOperation("复杂的admin查询")
    @RequestMapping(value = "/getByNameAndSexOrAddress", method = RequestMethod.GET)
    public Admin getAdminByNameAndSexOrAddress(@RequestParam("name") String name,
                                               @RequestParam(value = "sex", required = false) Integer sex,
                                               @RequestParam(value = "address", required = false) String address) {
        //OR
        BasicDBList orList = new BasicDBList(); //用于记录
        if (sex != null) {
            orList.add(new BasicDBObject("sex", sex));
        }
        if (address != null && address.length() > 0) {
            orList.add(new BasicDBObject("address", address));
        }
        BasicDBObject orDBObject = new BasicDBObject("$or", orList);

        //and
        BasicDBList andList = new BasicDBList();
        andList.add(new BasicDBObject("name", name));
        andList.add(orDBObject);
        BasicDBObject andDBObject = new BasicDBObject("$and", andList);

        return mongoTemplate.findOne(new BasicQuery(andDBObject), Admin.class);
    }
}
