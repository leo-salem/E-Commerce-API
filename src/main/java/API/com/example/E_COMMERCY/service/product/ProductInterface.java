package API.com.example.E_COMMERCY.service.product;

import API.com.example.E_COMMERCY.dto.product.ProductResponseDto;
import API.com.example.E_COMMERCY.dto.product.request.*;
import API.com.example.E_COMMERCY.exception.customExceptions.CategoryNotFoundException;
import API.com.example.E_COMMERCY.exception.customExceptions.ProductNameAlreadyExist;
import API.com.example.E_COMMERCY.exception.customExceptions.ProductNotFoundException;

import java.util.List;

public interface ProductInterface {

    public ProductResponseDto displayProductById(Long id) throws ProductNotFoundException;
    public ProductResponseDto displayProductByName(String productName) throws ProductNotFoundException;
    public List<ProductResponseDto> displayAllProducts() throws ProductNotFoundException;
    public void AddProduct (NewProductRequestDto newProductRequestDto) throws ProductNameAlreadyExist;
    public void UpdateProductName(ChangeProductNameRequestDto changeProductNameRequestDto) throws ProductNotFoundException;
    public void UpdateProductDescription(ChangeProductDescriptionDto changeProductDescriptionDto) throws ProductNotFoundException;
    public void UpdateProductPrice(ChangeProductPriceDto changeProductPriceDto) throws ProductNotFoundException;
    public void UpdateProductCategory(ChangeProductCategoryRequest changeProductCategoryRequest) throws ProductNotFoundException, CategoryNotFoundException;
    public void DeleteProductByName(String name) throws ProductNotFoundException;

}
