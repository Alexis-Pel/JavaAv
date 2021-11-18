package com.coding.javaav.dao;

import com.coding.javaav.ProductController;
import com.coding.javaav.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.awt.print.Pageable;
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
        return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Product.class), id);
    }

    //FILTRE
    public Product[] findAllByFilter(String type, String rating, String createdat){
        List<Product> allProducts = this.listAll();

        Date date;
        String whereString = "";
        String[] whereStringTab = new String[3];
        String typeFinal = null;
        String ratingFinal = null;
        String createDateFinal = null;
        String[] typeTab = new String[0];
        String[] ratingTab;
        String[] createdatTab;

        String[] typeTabFinal;
        String[] ratingTabFinal;
        String[] createdatTabFinal;
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
            createdat = null;
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

        if (rating != null && rating.length() != 0){
            try{
                if(Integer.parseInt(rating) >= 0 && Integer.parseInt(rating) <= 10){
                    ratingFinal = rating;
                    index+=1;
                }
            }
            catch (Exception e){
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
                createDateFinal = year+"-0"+month+"-"+day;
                index+=1;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int numberOfResults = 0;
        //SI RIEN N'EST NULL
        if(typeFinal != null && ratingFinal != null && createDateFinal != null){
            for (Product allProduct : allProducts) {
                if(allProduct.getType().equals(typeFinal) && Integer.toString(allProduct.getRating()).equals(ratingFinal) && allProduct.getCreatedAt().toString().substring(0, 10).equals(createDateFinal)){
                    numberOfResults++;
                }
            }
        }
        //SI TypeFinal est null
        else if (typeFinal == null && ratingFinal != null && createDateFinal != null) {
            for (Product allProduct : allProducts) {
                if(Integer.toString(allProduct.getRating()).equals(ratingFinal) && allProduct.getCreatedAt().toString().substring(0, 10).equals(createDateFinal)){
                    numberOfResults++;
                }
            }
        }
        //si ratingFinal est null
        else if (typeFinal != null && ratingFinal == null && createDateFinal != null) {
            for (Product allProduct : allProducts) {
                if(allProduct.getType().equals(typeFinal) && allProduct.getCreatedAt().toString().substring(0, 10).equals(createDateFinal)){
                    numberOfResults++;
                }
            }
        }
        //si creatingFinal est null
        else if (typeFinal != null && ratingFinal != null && createDateFinal == null) {
            for (Product allProduct : allProducts) {
                if(allProduct.getType().equals(typeFinal) && Integer.toString(allProduct.getRating()).equals(ratingFinal)){
                    numberOfResults++;
                }
            }
        }
        //Si ratingFinal et createDateFinak sont null
        else if (typeFinal != null && ratingFinal == null && createDateFinal == null) {
            for (Product allProduct : allProducts) {
                if(allProduct.getType().equals(typeFinal)){
                    numberOfResults++;
                }
            }
        }
        //Si typeFinak et createDateFinal sont null
        else if (typeFinal == null && ratingFinal != null && createDateFinal == null) {
            for (Product allProduct : allProducts) {
                if(Integer.toString(allProduct.getRating()).equals(ratingFinal)){
                    numberOfResults++;
                }
                //whereString += whereStringTab[i];
            }
        }
        //Si typeFinak et ratingFinal sont null
        else if (typeFinal == null && ratingFinal == null && createDateFinal != null) {
            for (Product allProduct : allProducts) {
                if(allProduct.getCreatedAt().toString().substring(0, 10).equals(createDateFinal)){
                    numberOfResults++;
                }
            }
        }

        if (numberOfResults != 0){
            Product[] result = new Product[numberOfResults];


            int indexFinal = 0;
            if(typeFinal != null && ratingFinal != null && createDateFinal != null){
                for (Product allProduct : allProducts) {
                    if(allProduct.getType().equals(typeFinal) && Integer.toString(allProduct.getRating()).equals(ratingFinal) && allProduct.getCreatedAt().toString().substring(0, 10).equals(createDateFinal)){
                        result[indexFinal] = allProduct;
                        indexFinal++;
                    }
                }
            }
            //SI TypeFinal est null
            else if (typeFinal == null && ratingFinal != null && createDateFinal != null) {
                for (Product allProduct : allProducts) {
                    if(Integer.toString(allProduct.getRating()).equals(ratingFinal) && allProduct.getCreatedAt().toString().substring(0, 10).equals(createDateFinal)){
                        result[indexFinal] = allProduct;
                        indexFinal++;
                    }
                }
            }
            //si ratingFinal est null
            else if (typeFinal != null && ratingFinal == null && createDateFinal != null) {
                for (Product allProduct : allProducts) {
                    if(allProduct.getType().equals(typeFinal) && allProduct.getCreatedAt().toString().substring(0, 10).equals(createDateFinal)){
                        result[indexFinal] = allProduct;
                        indexFinal++;
                    }
                }
            }
            //si creatingFinal est null
            else if (typeFinal != null && ratingFinal != null && createDateFinal == null) {
                for (Product allProduct : allProducts) {
                    if(allProduct.getType().equals(typeFinal) && Integer.toString(allProduct.getRating()).equals(ratingFinal)){
                        result[indexFinal] = allProduct;
                        indexFinal++;
                    }
                }
            }
            //Si ratingFinal et createDateFinak sont null
            else if (typeFinal != null && ratingFinal == null && createDateFinal == null) {
                for (Product allProduct : allProducts) {
                    if(allProduct.getType().equals(typeFinal)){
                        result[indexFinal] = allProduct;
                        indexFinal++;
                    }
                }
            }
            //Si typeFinak et createDateFinal sont null
            else if (typeFinal == null && ratingFinal != null && createDateFinal == null) {
                for (Product allProduct : allProducts) {
                    if(Integer.toString(allProduct.getRating()).equals(ratingFinal)){
                        result[indexFinal] = allProduct;
                        indexFinal++;
                    }
                }
            }
            //Si typeFinak et ratingFinal sont null
            else if (typeFinal == null && ratingFinal == null && createDateFinal != null) {
                for (Product allProduct : allProducts) {
                    if(allProduct.getCreatedAt().toString().substring(0, 10).equals(createDateFinal)){
                        result[indexFinal] = allProduct;
                        indexFinal++;
                    }
                }
            }
            return result;
        }
        else {
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

    // PRODUCT ORDER
    public List<Product> getAllProduct(Integer start, Integer end){
        String sql = "SELECT * FROM product LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Product.class), end, start);
    }

}