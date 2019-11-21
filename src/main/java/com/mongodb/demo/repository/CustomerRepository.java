package com.mongodb.demo.repository;

import com.mongodb.demo.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * * MongoRepository<Customer, Integer>, PagingAndSortingRepository<Customer, String>
 * * 第一个参数：T 操作的vo
 * * 第二个参数：ID T的主键类型
 * * 作用：该接口实现了CRUD方法
 * *
 * * 注意：
 * * 1、由于boot使用了spring-data-mongodb，所以我们不需要写该接口的实现，
 * *   当我们运行程序的时候，spring-data-mongodb会动态创建
 * * 2、findBySecondname命名是有讲究的，Secondname（是Customer的属性）若改为lastname就会报找不到属性lastname的错误
 */
public interface CustomerRepository extends MongoRepository<Customer, String>,
        PagingAndSortingRepository<Customer, String>, QueryDslPredicateExecutor<Customer> {
    Customer findByFirstName(String firstname);

    List<Customer> findBySecondName(String secondname);

    /**
     * 这个方法名不能乱写，findByXXX，那么对于的类中必须有XXX字段。也就是说对应的数据库中一定要存在XXX字段对应的列
     *
     * @param firstName
     * @param pageable
     * @return
     */
    Page<Customer> findByFirstName(String firstName, Pageable pageable);
}
