package com.mongodb.demo.common;


import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * 分页查询帮助类
 */
public class SortAndPageHelper {

    /**
     * 执行分页排序查询，并返回执行结果..
     *
     * @param pageable     前端传入的分页数据
     * @param query        JPA 查询表达式
     * @param defaultOrder 默认排序
     * @return 结果集
     */
    public static List executePageAndSort(Pageable pageable, JPAQuery query, OrderSpecifier... defaultOrder) {
        return setSortAndPage(pageable, query, defaultOrder).fetch();
    }

    /**
     * 仅仅执行分页排序查询，不返回结果集
     *
     * @param pageable     前端传入的分页数据
     * @param query        JPA 查询表达式
     * @param defaultOrder 默认排序
     * @return 结果集
     */
    private static JPAQuery setSortAndPage(Pageable pageable, JPAQuery query, OrderSpecifier... defaultOrder) {
        if (pageable != null) {
            query.offset(pageable.getOffset());
            query.limit(pageable.getPageSize());
            if (pageable.getSort() == null) {
                query.orderBy(defaultOrder);
            } else {
                for (Sort.Order o : pageable.getSort()) {
                    PathBuilder orderByExpression = new PathBuilder(Object.class, "id");
                    query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC
                            : Order.DESC, orderByExpression.get(o.getProperty())));
                }
            }
        } else {
            //不分页
            query.orderBy(defaultOrder);
        }
        return query;
    }
}