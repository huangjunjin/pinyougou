package com.pinyougou.pojo;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "tb_brand")
public class TbBrand implements Serializable {

    //Id注解表示该属性在数据库表中是一个主键字段
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//主键生成策略
    private Long id;

    private String name;

    //在数据库表字段与属性名称不一致的时候使用；默认可以不使用该注解
    //将数据库表中first_char字段的值映射到firstChar属性
    @Column(name = "first_char")
    private String firstChar;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }
}