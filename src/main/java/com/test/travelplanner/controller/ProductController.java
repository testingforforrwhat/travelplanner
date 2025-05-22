package com.test.travelplanner.controller;


import com.test.travelplanner.model.entity.Product;
import com.test.travelplanner.service.impl.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }
    
    @PostMapping("/{id}/decrease-stock")
    public ResponseEntity<String> decreaseStock(
            @PathVariable Long id,
            @RequestParam int quantity) {
        try {
            productService.decreaseStockWithRetry(id, quantity);
            return ResponseEntity.ok("库存减少成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("操作失败: " + e.getMessage());
        }
    }
}