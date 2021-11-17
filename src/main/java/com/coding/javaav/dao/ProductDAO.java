package com.coding.javaav.dao;

import com.coding.javaav.models.Category;
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

    @Autowired
    private CategoryDAO categoryDAO;

    public List<Product> listAll() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Product.class));
    }

    // ADD PRODUCT
    public String addProduct(Product newProduct) {
        String sql = "INSERT INTO product (type, rating, name, createdAt, categoryId) values (?, ?, ?, ?, ?);";
        String type;
        int rating;
        String name;
        int categoryId;
        Date createdAt;
        Object message;
        int res;

        try {
            name = newProduct.getName();
            if (name.length() == 0) {
                name = null;
            }
        } catch (Exception e) {
            name = null;
        }


        try {
            type = newProduct.getType();
            if (type.length() == 0) {
                type = null;
            }
        } catch (Exception e) {
            type = null;
        }

        try {
            rating = newProduct.getRating();
            if (rating < 0 || rating > 10) {
                rating = -1;
            }
        } catch (Exception e) {
            rating = -1;
        }


        try {
            categoryId = newProduct.getCategoryId();
            if (categoryDAO.findOne(categoryId) == null) {
                categoryId = -1;
            }
        } catch (Exception e) {
            categoryId = -1;
        }

        try {
            createdAt = newProduct.getCreatedAt();
        } catch (Exception e) {
            createdAt = null;
        }


        if (name != null && type != null && rating != -1 && categoryId != -1 && createdAt != null) {
            res = jdbcTemplate.update(sql, type, rating, name, createdAt, categoryId);
        } else {
            res = 0;
        }

        if (res == 1) {
            return "Product Added";
        } else {
            return "Error in arguments";
        }
    }

    //FIND ONE BY ID
    public Product findOne(int id) {
        String sql = "SELECT * FROM product WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Product.class), id);
    }


    // UPDATE PRODUCT
    public String updateProduct(Product updatedProduct, Integer idProduct) {
        try {
            if ((updatedProduct.getType() != "") && (updatedProduct.getRating() >= 0) && (updatedProduct.getRating() <= 10) && (updatedProduct.getName() != "") && (updatedProduct.getCategoryId() >= 0) && (idProduct > 0)) {
                String requestSQL = "UPDATE product SET type = ?, rating = ?, name = ?, categoryId = ? WHERE id=? ;";
                jdbcTemplate.update(requestSQL, updatedProduct.getType(), updatedProduct.getRating(), updatedProduct.getName(), updatedProduct.getCategoryId(), idProduct);
                return "OK";
            } else {
                return "C PT !";
            }
        } catch (Exception e) {
            return e.toString();
        }
    }

    // DELETE PRODUCT
    public int deleteProduct(Integer idProduct) {
        String requestSQL = "DELETE FROM product WHERE id=? ;";
        return jdbcTemplate.update(requestSQL, idProduct);
    }

}