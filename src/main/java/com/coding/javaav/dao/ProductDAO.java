package com.coding.javaav.dao;

import com.coding.javaav.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Repository
public class ProductDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CategoryDAO categoryDAO;

    // SHOW ALL PRODUCT
    public List<Product> listAll() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Product.class));
    }

    // ADD PRODUCT
    public String addProduct(Product newProduct) {
        String sql = "INSERT INTO product (type, rating, name, createdAt, categoryId) values (?, ?, ?, ?, ?);";
        String type = newProduct.getType();
        String name = newProduct.getName();
        int rating = newProduct.getRating();
        int categoryId = newProduct.getCategoryId();
        Date createdAt = newProduct.getCreatedAt();

        if (categoryDAO.findOne(categoryId) != null) {
            if (createdAt != null) {
                if ((type.length() > 0) && (rating >= 0) && (rating <= 10) && (name.length() > 0)) {
                    jdbcTemplate.update(sql, type, rating, name, createdAt, categoryId);
                    return "Product Added";
                }
            }
        }
        return "Error in arguments";
    }

    //FIND ONE BY ID
    public Product findOne(int id) {
        String sql = "SELECT * FROM product WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Product.class), id);
    }

    //FILTRE
    public List<Product> findAllByFilter(String type, String rating, String createdat) {
        //A faire : intervalles
        Date date;
        String whereString = "";
        String[] whereStringTab = new String[3];
        String typeFinal = null;
        String ratingFinal = null;
        String createDateFinal = null;

        int index = 0;


        if (type != null && type.length() != 0) {
            whereStringTab[index] = "type = ?";
            typeFinal = type;
            index += 1;
        }

        if (rating != null && rating.length() != 0) {
            try {
                if (Integer.parseInt(rating) >= 0 && Integer.parseInt(rating) <= 10) {
                    whereStringTab[index] = "rating = ?";
                    ratingFinal = rating;
                    index += 1;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        if (createdat != null && createdat.length() != 0) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.FRENCH);
            try {
                date = formatter.parse(createdat);
                String year = Integer.toString(date.getYear() + 1900);
                String month = Integer.toString(date.getMonth() + 1);
                String day = Integer.toString(date.getDate());
                createDateFinal = year + "-" + month + "-" + day + "-";
                whereStringTab[index] = "createdAt = ?";
                index += 1;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        int trouve = 0;
        for (int i = 0; i < whereStringTab.length; i++) {
            if (whereStringTab[i] != null) {
                trouve += 1;
                if (trouve == 2 || trouve == 3) {
                    whereString += " AND ";
                }
                whereString += whereStringTab[i];
            }
        }

        String sql = "SELECT * FROM product WHERE " + whereString;

        String[] finalTab = new String[index];
        int h = 0;
        if (typeFinal != null) {
            finalTab[h] = typeFinal;
            h++;
        }
        if (ratingFinal != null) {
            finalTab[h] = ratingFinal;
            h++;
        }
        if (createDateFinal != null) {
            finalTab[h] = createDateFinal;
        }

        System.out.println(finalTab[0]);
        if (finalTab.length == 1) {
            return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Product.class), finalTab[0]);
        } else if (finalTab.length == 2) {
            return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Product.class), finalTab[0], finalTab[1]);
        } else if (finalTab.length == 3) {
            return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Product.class), finalTab[0], finalTab[1], finalTab[2]);
        } else {
            return null;
        }
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