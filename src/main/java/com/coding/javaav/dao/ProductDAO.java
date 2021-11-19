package com.coding.javaav.dao;

import com.coding.javaav.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        try{
            return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Product.class), id);
        }
        catch (Exception e){
            return null;
        }
    }

    //FILTRE
    public List<List<Product>> findAllByFilter(String type, String rating, String createdat){
        Date date;
        String[] whereStringTab = new String[3];
        String typeFinal = null;
        String ratingFinal = null;
        String createDateFinal = null;
        String[] typeTab = new String[0];
        String[] ratingTab = new String[0];
        String[] createdatTab;

        String[] typeTabFinal = new String[0];
        String[] ratingTabFinal = new String[0];
        String[] createdatTabFinal = new String[0];

        try{
            typeTab = type.split(",");
        }
        catch (Exception e){
            type = null;
        }
        try{
            ratingTab = rating.split(",");
        }
        catch (Exception e){
            rating = null;
        }
        try{
            createdatTab = createdat.split(",");
        }
        catch (Exception e){
            createdatTab = null;
        }


        int index = 0;
        int typeCorrect = 0;
        for (String s : typeTab) {
            if (s != null && s.length() != 0) {
                typeCorrect++;
                index += 1;
            }
        }

        typeTabFinal = new String[typeCorrect];
        typeCorrect = 0;
        for (String s : typeTab) {
            if (s != null && s.length() != 0) {
                typeTabFinal[typeCorrect] = s;
                typeCorrect++;
            }
        }

        int createdAtCorrect = 0;
        if (createdatTab != null){
            for (String s : createdatTab) {
                if (s != null && s.length() != 0) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.FRENCH);
                    try {
                        date = formatter.parse(createdat);
                        String year = Integer.toString(date.getYear() + 1900);
                        String month = Integer.toString(date.getMonth() + 1);
                        String day = Integer.toString(date.getDate());
                        createdAtCorrect++;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            createdatTabFinal = new String[createdAtCorrect];
            createdAtCorrect = 0;
            for (String s : createdatTab) {
                if (s != null && s.length() != 0) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.FRENCH);
                    try {
                        date = formatter.parse(s);
                        String year = Integer.toString(date.getYear() + 1900);
                        String month = Integer.toString(date.getMonth() + 1);
                        String day = Integer.toString(date.getDate());
                        createDateFinal = year + "-0" + month + "-" + day;
                        createdatTabFinal[createdAtCorrect] = createDateFinal;
                        createdAtCorrect++;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        int ratingCorrect = 0;
        if(ratingTab.length>0){
            for (String item : ratingTab) {
                if (item != null && item.length() != 0){
                    try{
                        if(Integer.parseInt(item) >= 0 && Integer.parseInt(item) <= 10){
                            ratingCorrect++;
                        }
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            }
            ratingTabFinal = new String[ratingCorrect];
            ratingCorrect = 0;
            for (String item : ratingTab) {
                if (item != null && item.length() != 0){
                    try{
                        if(Integer.parseInt(item) >= 0 && Integer.parseInt(item) <= 10){
                            ratingTabFinal[ratingCorrect] = item;
                            ratingCorrect++;
                        }
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        ArrayList<List<Product>> result = new ArrayList<>();
        if(typeTabFinal.length > 0){
            for (String s : typeTab) {
                // Que createdat
                if(createdat != null && ratingTabFinal.length == 0){
                    for (String value : createdatTabFinal) {
                        result.add(getAllByTypeAndDate(s, value));
                    }
                }
                //Que ratingTabFinal
                else if(createdat == null && ratingTabFinal.length > 0){
                    for (String value : ratingTabFinal) {
                        result.add(getAllByTypeAndRating(s, value));
                    }
                }
                // Les Deux
                else if (createdat != null && ratingTabFinal.length > 0){
                    for (String value : createdatTabFinal) {
                        for (String rat : ratingTabFinal) {
                            result.add(getAllByTypeAndDateAndRating(s, value, rat));
                        }
                    }
                }
                // Aucun des deux
                else{
                    result.add(getAllByType(s));
                }
            }
        }
        else if (createdatTabFinal.length > 0){
            for (String s : createdatTabFinal) {
                if(ratingTabFinal.length > 0){
                    for (String value : ratingTabFinal) {
                        result.add(getAllByDateAndRating(s, value));
                    }
                }
                else{
                    result.add(getAllByDate(s));
                }
            }
        }
        else{
            for (String s : ratingTabFinal) {
                result.add(getAllByRating(s));
            }
        }
        return result;
    }
    private List<Product> getAllByRating(String s) {
        return jdbcTemplate.query("SELECT * FROM product WHERE rating = ?", BeanPropertyRowMapper.newInstance(Product.class), s);
    }
    private List<Product> getAllByDate(String s) {
        return jdbcTemplate.query("SELECT * FROM product WHERE createdAt = ?", BeanPropertyRowMapper.newInstance(Product.class), s);
    }
    private List<Product> getAllByDateAndRating(String s,String Rating) {
        return jdbcTemplate.query("SELECT * FROM product WHERE createdAt = ? AND rating = ?", BeanPropertyRowMapper.newInstance(Product.class), s, Rating);
    }

    private List<Product> getAllByTypeAndRating(String s,String Rating) {
        return jdbcTemplate.query("SELECT * FROM product WHERE type = ? AND rating = ?", BeanPropertyRowMapper.newInstance(Product.class), s, Rating);
    }

    private List<Product> getAllByTypeAndDateAndRating(String s, String Date, String Rating) {
        return jdbcTemplate.query("SELECT * FROM product WHERE type = ?", BeanPropertyRowMapper.newInstance(Product.class), s);
    }

    private List<Product> getAllByType(String s) {
        return jdbcTemplate.query("SELECT * FROM product WHERE type = ?", BeanPropertyRowMapper.newInstance(Product.class), s);
    }

    private List<Product> getAllByTypeAndDate(String type, String date){
        if (date != null){
            return jdbcTemplate.query("SELECT * FROM product WHERE type = ? AND createdAt = ?", BeanPropertyRowMapper.newInstance(Product.class), type, date);

        }
        else{
            return jdbcTemplate.query("SELECT * FROM product WHERE type = ?", BeanPropertyRowMapper.newInstance(Product.class), type);

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

    // PRODUCT ORDER
    public List<Product> getAllProduct(Integer start, Integer end){
        String sql = "SELECT * FROM product LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Product.class), end, start);
    }

}