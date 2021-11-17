package com.coding.javaav;

import com.coding.javaav.dao.ProductDAO;
import com.coding.javaav.models.Category;
import com.coding.javaav.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductDAO productService;

    @GetMapping("/products")
    @ResponseBody
    public List<Product> index(Model model){
        return productService.listAll();
    }

}
