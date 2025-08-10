package API.com.example.E_COMMERCY.controller;


import API.com.example.E_COMMERCY.dto.category.CategoryResponseDto;
import API.com.example.E_COMMERCY.dto.product.ProductResponseDto;
import API.com.example.E_COMMERCY.dto.product.request.*;
import API.com.example.E_COMMERCY.exception.customExceptions.CategoryNotFoundException;
import API.com.example.E_COMMERCY.exception.customExceptions.ProductNameAlreadyExist;
import API.com.example.E_COMMERCY.exception.customExceptions.ProductNotFoundException;
import API.com.example.E_COMMERCY.service.product.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ProductSection")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/TheId/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) throws ProductNotFoundException {
        ProductResponseDto productResponseDto=productService.displayProductById(id);
        return ResponseEntity.ok().body(productResponseDto);
    }

    @GetMapping("/product/TheName/{name}")
    public ResponseEntity<?> getProductByName(@PathVariable String name) throws ProductNotFoundException {
        ProductResponseDto productResponseDto=productService.displayProductByName(name);
        return ResponseEntity.ok().body(productResponseDto);
    }

    @GetMapping("/product/All")
    public ResponseEntity<?> GetAllProducts() throws ProductNotFoundException {
        List<ProductResponseDto> productResponseDtos =productService.displayAllProducts();
        return ResponseEntity.ok().body(productResponseDtos);
    }

    @PostMapping("/product/Add")
    public ResponseEntity<?> AddTheProduct (@RequestBody @Valid NewProductRequestDto productRequestDto) throws ProductNameAlreadyExist {
        productService.AddProduct(productRequestDto);
        return ResponseEntity.ok("product added successfully");
    }

    @PatchMapping("product/ChangeName")
    public ResponseEntity<?> UpdateName(@RequestBody @Valid ChangeProductNameRequestDto productRequestDto) throws ProductNotFoundException {
        productService.UpdateProductName(productRequestDto);
        return ResponseEntity.ok("product name updated successfully");
    }

    @PatchMapping("product/ChangePrice")
    public ResponseEntity<?> UpdatePrice(@RequestBody @Valid ChangeProductPriceDto productRequestDto) throws ProductNotFoundException {
        productService.UpdateProductPrice(productRequestDto);
        return ResponseEntity.ok("product Price updated successfully");
    }

    @PatchMapping("product/ChangeDescription")
    public ResponseEntity<?> UpdateDescription(@RequestBody @Valid ChangeProductDescriptionDto productRequestDto) throws ProductNotFoundException {
        productService.UpdateProductDescription(productRequestDto);
        return ResponseEntity.ok("product Description updated successfully");
    }

    @PutMapping("product/ChangeCategory")
    public ResponseEntity<?> UpdateTheProductCategory(@RequestBody @Valid ChangeProductCategoryRequestDto productRequestDto) throws ProductNotFoundException, CategoryNotFoundException {
        productService.UpdateProductCategory(productRequestDto);
        return ResponseEntity.ok("product Category updated successfully");
    }

    @DeleteMapping("/product/delete/{name}")
    public ResponseEntity<?> DeleteTheProduct(@PathVariable @Valid String name) throws ProductNotFoundException {
        productService.DeleteProductByName(name);
        return ResponseEntity.ok("product deleted successfully");
    }









}
