package com.mongodb.demo.api;

import com.mongodb.DBObject;
import com.mongodb.demo.common.Tools;
import com.mongodb.demo.domain.Customer;
import com.mongodb.demo.repository.CustomerRepository;
import com.mongodb.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * Created by eric on 2017/5/11.
 */
@RestController
@RequestMapping("/api/customer")
@Api("customer相关的API,用于测试mongodb Jpa操作")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @ApiOperation("增加一个Customer")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public Customer addCustomer(@RequestParam("firstname") String firstname,
                                @RequestParam("secondname") String secondname) {
        Customer customer = new Customer();
        customer.setFirstName(firstname);
        customer.setSecondName(secondname);
        return customerRepository.save(customer);
    }

    @ApiOperation("获取所有的Customer")
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }

    @ApiOperation("获取所有的Customer")
    @RequestMapping(value = "/getAllByDsl", method = RequestMethod.GET)
    public List<Customer> getAllCustomerByDSL() throws Exception {
        return customerService.findAll();
    }

    @ApiOperation("根据firstname获取Customer")
    @RequestMapping(value = "/getByFirstname", method = RequestMethod.GET)
    public Customer getCustomerByFirstname(@RequestParam("firstname") String firstname) {
        return customerRepository.findByFirstName(firstname);
    }

    @ApiOperation("获取根据ID排序后的Customer")
    @RequestMapping(value = "/getCustomerAndOrder", method = RequestMethod.GET)
    public List<Customer> findCustomerAndOrder() throws Exception{
        return customerService.findCustomerAndOrder();
    }

    @ApiOperation("根据secondname获取多个Customer")
    @RequestMapping(value = "/getBySecondname", method = RequestMethod.GET)
    public List<Customer> getCustomerBySecondname(@RequestParam("secondname") String secondname) {
        return customerRepository.findBySecondName(secondname);
    }

    @ApiOperation("根据id删除Customer")
    @RequestMapping(value = "/deleteById", method = RequestMethod.GET)
    public boolean deleteCustomerById(@RequestParam("cid") String cid) {
        customerRepository.delete(cid);
        return true;
    }

    @ApiOperation("分页查询")
    @RequestMapping(value = "/selectByFirstName", method = RequestMethod.GET)
    public Page<Customer> getBygender(String firstName, @RequestParam("pageNumber") int pageNumber) {
        //构建分页信息
        PageRequest pageRequest = Tools.buildPageRequest(pageNumber, 5, "firstName");
        Page<Customer> customers = customerRepository.findByFirstName(firstName, pageRequest);
        return customers;
    }

    @ApiOperation("列表查询")
    @RequestMapping(value = "/queryByTemplate", method = RequestMethod.GET)
    public List<DBObject> queryByTemplate(String firstName, String secondName) throws Exception {
        return customerService.queryByTemplate(firstName, secondName);
    }


    @ApiOperation("QueryDSL分页查询")
    @RequestMapping(value = "/queryByDsl", method = RequestMethod.GET)
    public Page<Customer> queryByDsl(String firstName, String secondName, Pageable pageable) throws Exception {
        return customerService.queryByDsl(firstName, secondName, pageable);
    }

}