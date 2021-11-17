package com.coding.javaav;

import com.coding.javaav.dao.CategoryDAO;
import com.coding.javaav.dao.ProductDAO;
import com.coding.javaav.models.Category;
import com.coding.javaav.models.Product;
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
        return categoryService.addCategory(newCategory);
    }



    // UPDATE CATEGORY
    @PutMapping("")
    @ResponseBody
    public String updateCategory(@RequestBody Category updatedCategory, @RequestParam String idCategory){
        categoryService.updateCategory(updatedCategory, idCategory);
        return "Category " + updatedCategory.getName() + " updated !" + idCategory;
    }

    // GET ONE CATEGORY
    @GetMapping("/{id}")
    @ResponseBody
    public Category showOne(@PathVariable(value="id") int id){
        return categoryService.findOne(id);
    }


}
