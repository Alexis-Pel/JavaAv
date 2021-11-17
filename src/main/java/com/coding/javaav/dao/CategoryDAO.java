package com.coding.javaav.dao;

import com.coding.javaav.models.Category;
import com.coding.javaav.models.Product;
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
    public String addCategory(Category newCategory){
        try {
            if (newCategory.getName().length() > 0) {
                String requestSQL = "INSERT INTO category (name) values (?);";
                jdbcTemplate.update(requestSQL, newCategory.getName());
                return "Category " + newCategory.getName() + " created !";
            } else {
                return "Le nom ne peut pas être vide";
            }
        }
        catch (Exception e){
            return e.getMessage();
        }
    }


    // UPDATE CATEGORY
    public void updateCategory(Category updatedCategory, Integer idCategory){
        String requestSQL = "UPDATE category SET name=? WHERE id=?;";
        jdbcTemplate.update(requestSQL, updatedCategory.getName(), idCategory);
    }

    //FIND ONE BY ID
    public Category findOne(int id){
        String sql = "SELECT * FROM category WHERE id = " + id;
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Category.class)).get(0);
    }
}