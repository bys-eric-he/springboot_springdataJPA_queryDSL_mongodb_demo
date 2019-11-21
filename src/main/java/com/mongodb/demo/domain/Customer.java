package com.mongodb.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by eric on 2017/5/11.
 */
@Document
public class Customer {
    /**
     * cid：该字段用于mongodb的"_id"索引
     * 1、需要@Id注解
     * 2、取名无所谓，反正在mongodb中最后都会转化为"_id"
     * 3、定义为String类型，如果定义为Integer可能索引只会是0，会出现key重复导致数据库插不进去的情况；
     * 4、该类型也是MongoRepository泛型中主键的ID
     */
    @Id
    private String cid;
    private String firstName;
    private String secondName;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }
}
