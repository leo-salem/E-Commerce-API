package API.com.example.E_COMMERCY.controller;


import API.com.example.E_COMMERCY.dto.category.CategoryResponseDto;
import API.com.example.E_COMMERCY.dto.category.request.ChangeCategoryNameRequestDto;
import API.com.example.E_COMMERCY.dto.category.request.ChangeCategoryProductsSetRequestDto;
import API.com.example.E_COMMERCY.dto.category.request.NewCategoryRequestDto;
import API.com.example.E_COMMERCY.exception.customExceptions.CategoryNameAlreadyExist;
import API.com.example.E_COMMERCY.exception.customExceptions.CategoryNotFoundException;
import API.com.example.E_COMMERCY.exception.customExceptions.ProductNotFoundException;
import API.com.example.E_COMMERCY.service.category.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/CategorySection")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/category/TheId/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) throws CategoryNotFoundException {
        CategoryResponseDto categoryResponseDto=categoryService.displayCategoryById(id);
        return ResponseEntity.ok().body(categoryResponseDto);
    }

    @GetMapping("/category/TheName/{name}")
    public ResponseEntity<?> getCategoryByName(@PathVariable String name) throws CategoryNotFoundException {
        CategoryResponseDto categoryResponseDto=categoryService.displayCategoryByName(name);
        return ResponseEntity.ok().body(categoryResponseDto);
    }

    @GetMapping("/category/All")
    public ResponseEntity<?> GetAllCategories() throws CategoryNotFoundException {
        List<CategoryResponseDto> categoryResponseDtos =categoryService.displayAllCategories();
        return ResponseEntity.ok().body(categoryResponseDtos);
    }

    @PostMapping("/category/Add")
    public  ResponseEntity<?> AddTheCategory(@RequestBody @Valid NewCategoryRequestDto newCategoryRequestDto) throws CategoryNameAlreadyExist {
        categoryService.AddCategory(newCategoryRequestDto);
        return ResponseEntity.ok("Category added successfully");
    }

    @PutMapping("/category/update/products")
    public ResponseEntity<?> UpdateTheCategoryProducts(@RequestBody @Valid ChangeCategoryProductsSetRequestDto productsSetRequestDto) throws CategoryNotFoundException, ProductNotFoundException {
        categoryService.UpdateCategoryProducts(productsSetRequestDto);
        return ResponseEntity.ok("Category products updated successfully");
    }

    @PatchMapping("/category/update/TheName")
    public ResponseEntity<?> UpdateTheCategoryProducts(@RequestBody @Valid ChangeCategoryNameRequestDto categoryNameRequestDto) throws CategoryNotFoundException{
        categoryService.UpdateCategoryName(categoryNameRequestDto);
        return ResponseEntity.ok("Category name updated successfully");
    }

    @DeleteMapping("/category/delete/{name}")
    public ResponseEntity<?> DeleteTheCategory(@PathVariable @Valid String name) throws CategoryNotFoundException {
        categoryService.DeleteCategory(name);
        return ResponseEntity.ok("Category deleted successfully");
    }





}
