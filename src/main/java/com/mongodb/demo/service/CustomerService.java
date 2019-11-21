package com.mongodb.demo.service;

import com.mongodb.DBObject;
import com.mongodb.demo.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by eric on 2017/5/12.
 */
public interface CustomerService {
    Page<Customer> queryByDsl(String firstName, String secondName, Pageable pageable) throws Exception;
    List<DBObject> queryByTemplate(String firstName, String secondName) throws Exception;
    Customer findCustomerByFirstName(final String firstName) throws Exception;
    List<Customer> findAll() throws Exception;
    Customer findOneByFirstName(final String firstName) throws Exception;
    Customer findOneByFirstNameAndSecondName(final String firstName, final String secondName) throws Exception;
    List<Customer> findCustomersByJoin() throws Exception;
    List<Customer> findCustomerAndOrder() throws Exception;
    List<String> findCustomerByGroup() throws Exception;
    long deleteCustomer(String firstName) throws Exception;
    long updateCustomer(final Customer customer, final String firstName) throws Exception;
    Page<Customer> findAllAndPager(final int offset, final int pageSize) throws Exception;
}
