package com.mongodb.demo.service.impl;

import com.mongodb.DBObject;
import com.mongodb.demo.common.SortAndPageHelper;
import com.mongodb.demo.domain.Customer;
import com.mongodb.demo.domain.QCustomer;
import com.mongodb.demo.repository.CustomerRepository;
import com.mongodb.demo.service.CustomerService;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by eric on 2017/5/12.
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    private CustomerRepository customerRepository;


    @Override
    public List<DBObject> queryByTemplate(String firstName, String secondName) throws Exception {
        Query query = new Query(
                Criteria.where("firstName").is(firstName)
                        .andOperator(Criteria.where("secondName").is(secondName)));

        return mongoTemplate.find(query, DBObject.class, "customer");
    }


    @Override
    public Page<Customer> queryByDsl(String firstName, String secondName, Pageable pageable) throws Exception {
        QCustomer qCustomer = QCustomer.customer;

        JPAQuery jpaQuery = jpaQueryFactory.query();

        if (firstName != null) {
            jpaQuery.where(qCustomer.firstName.containsIgnoreCase(firstName));
        }

        if (secondName != null) {
            jpaQuery.where(qCustomer.secondName.containsIgnoreCase(secondName));
        }

        long totalCount = jpaQuery.fetchCount();

        OrderSpecifier orderSpecifierCid = qCustomer.cid.desc();
        List<Customer> customers = SortAndPageHelper.executePageAndSort(pageable, jpaQuery, orderSpecifierCid);


        return new PageImpl<>(customers, pageable, totalCount);
    }

    @Override
    public Customer findCustomerByFirstName(final String firstName) {

        QCustomer qCustomer = QCustomer.customer;
        Predicate predicate = qCustomer.firstName.eq(firstName);
        return customerRepository.findOne(predicate);
        //return jpaQueryFactory.selectFrom(qCustomer).where(predicate).fetchOne();
    }


    @Override
    public List<Customer> findAll() {
        QCustomer qCustomer = QCustomer.customer;
        return jpaQueryFactory.selectFrom(qCustomer)
                .fetch();
    }


    @Override
    public Customer findOneByFirstName(final String firstName) {
        QCustomer qCustomer = QCustomer.customer;
        return jpaQueryFactory.selectFrom(qCustomer)
                .where(qCustomer.firstName.eq(firstName))
                .fetchOne();
    }

    @Override
    public Customer findOneByFirstNameAndSecondName(final String firstName, final String secondName) {
        QCustomer qCustomer = QCustomer.customer;
        return jpaQueryFactory.select(qCustomer)
                .from(qCustomer) // 上面两句代码等价与selectFrom
                .where(qCustomer.firstName.contains(firstName).and(qCustomer.secondName.contains(secondName)))
                // 这句代码等同于where(qCustomer.firstName.contains(firstName), qCustomer.secondName.contains(secondName))
                .fetchOne();
    }

    @Override
    public List<Customer> findCustomersByJoin() {
        QCustomer qCustomer = QCustomer.customer;
        QCustomer qFirstName = new QCustomer("firstName");
        return jpaQueryFactory.selectFrom(qCustomer)
                .innerJoin(qFirstName)
                .on(qCustomer.cid.stringValue().eq(qFirstName.cid.stringValue()))
                .fetch();
    }

    @Override
    public List<Customer> findCustomerAndOrder() {
        QCustomer qCustomer = QCustomer.customer;
        return jpaQueryFactory.selectFrom(qCustomer)
                .orderBy(qCustomer.cid.desc())
                .fetch();
    }

    @Override
    public List<String> findCustomerByGroup() {
        QCustomer qCustomer = QCustomer.customer;
        return jpaQueryFactory.select(qCustomer.firstName)
                .from(qCustomer)
                .groupBy(qCustomer.firstName)
                .fetch();
    }

    @Override
    public long deleteCustomer(String firstName) {
        QCustomer qCustomer = QCustomer.customer;
        return jpaQueryFactory.delete(qCustomer).where(qCustomer.firstName.eq(firstName)).execute();
    }

    @Override
    public long updateCustomer(final Customer customer, final String firstName) {
        QCustomer qCustomer = QCustomer.customer;
        return jpaQueryFactory.update(qCustomer).where(qCustomer.firstName.eq(firstName))
                .set(qCustomer.firstName, customer.getFirstName())
                .set(qCustomer.secondName, customer.getSecondName())
                .execute();
    }


    @Override
    public Page<Customer> findAllAndPager(final int offset, final int pageSize) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "cid"));
        PageRequest pageRequest = new PageRequest(offset, pageSize, sort);
        return customerRepository.findAll(pageRequest);
    }

}
