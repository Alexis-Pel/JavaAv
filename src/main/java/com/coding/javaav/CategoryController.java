package com.coding.javaav;

import com.coding.javaav.dao.CategoryDAO;
import com.coding.javaav.dao.ProductDAO;
import com.coding.javaav.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryDAO categoryService;

    @GetMapping("/category")
    @ResponseBody
    public List<Category> index(){
        return categoryService.listAll();
    }

}
