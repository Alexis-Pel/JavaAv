package com.coding.javaav;

import com.coding.javaav.dao.ProductDAO;
import com.coding.javaav.models.Category;
import com.coding.javaav.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public List<Product> index(Model model) {
        return productService.listAll();
    }


    // POST PRODUCT
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String addProduct(@RequestBody Product newProduct) {
        return productService.addProduct(newProduct);
    }


    // GET ONE PRODUCT
    @GetMapping("/{id}")
    public Product showOne(@PathVariable(value="id") int id){
        Product product = productService.findOne(id);
        if (product==null){
            return null;
        }
        else{
            return productService.findOne(id);
        }
    }


    // UPDATE PRODUCT
    @PutMapping("/{id}")
    @ResponseBody
    public String updateProduct(@RequestBody Product updatedProduct, @PathVariable(value = "id") int id) {
        return productService.updateProduct(updatedProduct, id);
    }

    // DELETE PRODUCT
    @DeleteMapping("/{id}")
    @ResponseBody
    public HttpStatus deleteProduct(@PathVariable(value = "id") int id) {
        int result = productService.deleteProduct(id);
        if (result == 1){
            return HttpStatus.NO_CONTENT;
        } else {
          return HttpStatus.BAD_REQUEST;
        }
    }

}
