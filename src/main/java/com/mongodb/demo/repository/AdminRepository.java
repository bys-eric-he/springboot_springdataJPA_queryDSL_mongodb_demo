package com.mongodb.demo.repository;

import com.mongodb.demo.domain.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * Created by eric on 2017/5/11.
 */
public interface AdminRepository extends MongoRepository<Admin, String> , QueryDslPredicateExecutor<Admin> {
}
