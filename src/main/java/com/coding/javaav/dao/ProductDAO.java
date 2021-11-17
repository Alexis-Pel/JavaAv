package com.coding.javaav.dao;

import com.coding.javaav.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class ProductDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Product> listAll(){
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Product.class));
    }

    // ADD PRODUCT
    public Product addProduct(Product newProduct){
        String sql = "INSERT INTO product (type, rating, name, createdAt, categoryId) values (?) (?) (?) (?) (?);";
        String type;
        int rating;
        String name;
        int categoryId;
        Date createdAt;

        try {
            name = newProduct.getName();
            if(name.length() == 0){
                name = null;
            }
        }
        catch (Exception e){
            name = null;
        }


        try{
            type = newProduct.getType();
            if(type.length() == 0){
                type = null;
            }
        }
        catch (Exception e){
            type = null;
        }

        try{
            rating = newProduct.getRating();
            if( rating < 0 || rating > 10){
                rating = -1;
            }
        }
        catch (Exception e){
            rating = -1;
        }


        try{
            categoryId = newProduct.getCategoryId();
            CategoryDAO service = new CategoryDAO();

            if(service.findOne(categoryId) == null){
                categoryId = -1;
            }
        }
        catch (Exception e){
            categoryId = -1;
        }

        try{
            createdAt = newProduct.getCreatedAt();
        }
        catch (Exception e){
            createdAt = null;
        }


        if (name != null && type != null && rating != -1 && categoryId != -1 && createdAt != null ){
            return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Product.class), type, rating, name, createdAt, categoryId);
        }
        System.out.println(name + " " + type + " " + rating + " " + categoryId + " " + createdAt +" ");
        return newProduct;
    }

    //FIND ONE BY ID
    public Product findOne(int id){
        String sql = "SELECT * FROM product WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Product.class), id);
    }
}