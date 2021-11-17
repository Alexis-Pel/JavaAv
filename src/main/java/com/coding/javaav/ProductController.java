package com.coding.javaav;

import com.coding.javaav.dao.ProductDAO;
import com.coding.javaav.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductDAO productService;

    // GET ALL PRODUCT
    @GetMapping("")
    public List<Product> index(Model model){
        return productService.listAll();
    }


    // POST PRODUCT
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Product addProduct(@RequestBody Product newProduct){
        return productService.addProduct(newProduct);
    }


    // GET ONE PRODUCT
    @GetMapping("/{id}")
    public ResponseEntity.BodyBuilder showOne(@PathVariable(value="id") int id){
        Product product = productService.findOne(id);
        if (product==null){
            return (ResponseEntity.BodyBuilder) ResponseEntity.notFound();
        }
        else{
            return (ResponseEntity.BodyBuilder) ResponseEntity.ok().body(productService.findOne(id));
        }
    }

}
