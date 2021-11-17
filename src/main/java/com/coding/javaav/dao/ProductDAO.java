package com.coding.javaav.dao;

import com.coding.javaav.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Product> listAll(){
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Product.class));
    }
}