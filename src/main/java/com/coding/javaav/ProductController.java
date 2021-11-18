package com.coding.javaav;

import com.coding.javaav.dao.ProductDAO;
import com.coding.javaav.models.Product;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductDAO productService;

    // GET ALL PRODUCT
    // FILTRE
    @GetMapping("")
    public Object index(@RequestParam(required = false) String type, @RequestParam(required = false) String rating, @RequestParam(required = false) String createdat){
        if (type == null && rating == null && createdat == null) {
            return productService.listAll();
        }
        else{
            return productService.findAllByFilter(type, rating, createdat).toArray();
        }
    }


    // POST PRODUCT
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String addProduct(@RequestBody Product newProduct) {
        return productService.addProduct(newProduct);
    }


    // GET ONE PRODUCT
    @GetMapping("/{id}")
    public ResponseEntity<Product> showOne(@PathVariable(value = "id") int id)
            throws ResourceNotFoundException {
        Product product = productService.findOne(id);
        System.out.println("TEST ============================");
        if (product == null) {
            throw new ResourceNotFoundException("User not found on :: " + id);
        }
        return ResponseEntity.ok().
                body(product);
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
        if (result == 1) {
            return HttpStatus.NO_CONTENT;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    // PRODUCT ORDER
    @GetMapping("/orders")
    public ResponseEntity<List<Product>> getAllProduct(@RequestParam String range){
        System.out.println(range);

        String split = "-";
        String[] arg = range.split(split);
        int start = Integer.parseInt(arg[0]);
        int end = Integer.parseInt(arg[1]);

        List<Product> result = productService.getAllProduct(start, end);

        int x = end + start;
        String reply = String.valueOf(end) + "/" + String.valueOf(x);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Range:", reply);
        headers.add("Accept-Range:", "product: " + end);

        return new ResponseEntity<>(result, headers, HttpStatus.OK);

    }
}