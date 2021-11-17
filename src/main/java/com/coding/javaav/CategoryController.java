package com.coding.javaav;

import com.coding.javaav.dao.CategoryDAO;
import com.coding.javaav.dao.ProductDAO;
import com.coding.javaav.models.Category;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryDAO categoryService;


    // GET ALL CATEGORY
    @GetMapping("")
    @ResponseBody
    public List<Category> index(){
        return categoryService.listAll();
    }


    // POST CATEGORY
    @PostMapping("")
    @ResponseBody
    public String addCategory(@RequestBody Category newCategory){
        categoryService.addCategory(newCategory);
        return "New category " + newCategory.getName() + " created !";
    }


}
