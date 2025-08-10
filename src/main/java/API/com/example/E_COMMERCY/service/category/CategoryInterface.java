package API.com.example.E_COMMERCY.service.category;

import API.com.example.E_COMMERCY.dto.category.CategoryResponseDto;
import API.com.example.E_COMMERCY.dto.category.request.ChangeCategoryNameRequestDto;
import API.com.example.E_COMMERCY.dto.category.request.ChangeCategoryProductsSetRequestDto;
import API.com.example.E_COMMERCY.dto.category.request.NewCategoryRequestDto;
import API.com.example.E_COMMERCY.exception.customExceptions.CategoryNameAlreadyExist;
import API.com.example.E_COMMERCY.exception.customExceptions.CategoryNotFoundException;
import API.com.example.E_COMMERCY.exception.customExceptions.ProductNotFoundException;

import java.util.List;

public interface CategoryInterface {
    public CategoryResponseDto displayCategoryById(Long id) throws CategoryNotFoundException;
    public CategoryResponseDto displayCategoryByName(String name) throws CategoryNotFoundException;
    public List<CategoryResponseDto> displayAllCategories() throws CategoryNotFoundException;
    public void AddCategory (NewCategoryRequestDto newCategory) throws CategoryNotFoundException, CategoryNameAlreadyExist;
    public void UpdateCategoryName(ChangeCategoryNameRequestDto categoryName) throws CategoryNotFoundException;
    public void UpdateCategoryProducts(ChangeCategoryProductsSetRequestDto changeCategoryProductsSetRequestDto) throws CategoryNotFoundException, ProductNotFoundException;
    public void DeleteCategory(String name) throws CategoryNotFoundException;
}
