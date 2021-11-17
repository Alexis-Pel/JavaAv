package com.coding.javaav.dao;

import com.coding.javaav.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class CategoryDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // DISPLAY ALL CATEGORY
    public List<Category> listAll(){
        String sql = "SELECT * FROM category";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Category.class));
    }


    // ADD CATEGORY
    public void addCategory(Category newCategory){
        String requestSQL = "INSERT INTO category (name) values (?);";
        jdbcTemplate.update(requestSQL, newCategory.getName());
    }


    // UPDATE CATEGORY
    public void updateCategory(Category updatedCategory, String idCategory){
        String requestSQL = "UPDATE category SET name ='" + updatedCategory.getName() + "' WHERE id = " + idCategory + ";";
        jdbcTemplate.update(requestSQL);
    }
}