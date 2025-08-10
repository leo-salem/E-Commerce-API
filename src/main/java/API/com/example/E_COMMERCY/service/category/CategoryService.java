package API.com.example.E_COMMERCY.service.category;

import API.com.example.E_COMMERCY.dto.category.CategoryMapper;
import API.com.example.E_COMMERCY.dto.category.CategoryResponseDto;
import API.com.example.E_COMMERCY.dto.category.request.ChangeCategoryNameRequestDto;
import API.com.example.E_COMMERCY.dto.category.request.ChangeCategoryProductsSetRequestDto;
import API.com.example.E_COMMERCY.dto.category.request.NewCategoryRequestDto;
import API.com.example.E_COMMERCY.dto.product.ProductMapper;
import API.com.example.E_COMMERCY.dto.product.ProductResponseDto;
import API.com.example.E_COMMERCY.dto.slimDTOs.ProductResponseSlimDto;
import API.com.example.E_COMMERCY.exception.customExceptions.CategoryNameAlreadyExist;
import API.com.example.E_COMMERCY.exception.customExceptions.CategoryNotFoundException;
import API.com.example.E_COMMERCY.exception.customExceptions.ProductNotFoundException;
import API.com.example.E_COMMERCY.model.Category;
import API.com.example.E_COMMERCY.model.Product;
import API.com.example.E_COMMERCY.repository.CategoryRepository;
import API.com.example.E_COMMERCY.repository.ProductRepository;
import API.com.example.E_COMMERCY.service.deletion.DeletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryService implements CategoryInterface {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final DeletionService deletionService;
    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper, ProductRepository productRepository, ProductMapper productMapper, DeletionService deletionService) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.deletionService = deletionService;
    }
    @Override
    public CategoryResponseDto displayCategoryById(Long id) throws CategoryNotFoundException {
        Category category= categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("no category found with this id :"+id));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryResponseDto displayCategoryByName(String categoryName) throws CategoryNotFoundException {
        Category category=categoryRepository.findByName(categoryName)
                .orElseThrow (() -> new CategoryNotFoundException("No Category found with name: " + categoryName));
                return categoryMapper.toDto(category);
    }

    @Override
    public List<CategoryResponseDto> displayAllCategories() throws CategoryNotFoundException {
       List<Category> categoryList=categoryRepository.findAll();
       if (categoryList.isEmpty()) {
           throw new CategoryNotFoundException("CategoryList is Empty ");
       }
       return categoryList.stream().map(categoryMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void AddCategory(NewCategoryRequestDto newCategory) throws  CategoryNameAlreadyExist {
        String categoryName = newCategory.getName();
        if (categoryRepository.findByName(categoryName).isPresent()) {
            throw new CategoryNameAlreadyExist("this name "+categoryName+" Already Exist");
        }
        Category category=categoryMapper.toEntity(newCategory);
       /*
        Set<Product>products =category.getProducts();
        for (Product product : products){
            product.setCategory(category);
            productRepository.save(product);
        }
        */
        categoryRepository.save(category);
    }

    @Override
    public void UpdateCategoryName(ChangeCategoryNameRequestDto categoryName) throws CategoryNotFoundException {
        Category category=categoryRepository.findByName(categoryName.getOldName())
                .orElseThrow (() -> new CategoryNotFoundException("No Category found with name: " + categoryName));
        category.setName(categoryName.getNewName());
        categoryRepository.save(category);
    }

    @Override
    public void UpdateCategoryProducts(ChangeCategoryProductsSetRequestDto changeCategoryProductsSetRequestDto) throws CategoryNotFoundException, ProductNotFoundException {
        String categoryName = changeCategoryProductsSetRequestDto.getName();
        Category category=categoryRepository.findByName(categoryName)
                .orElseThrow (() -> new CategoryNotFoundException("No Category found with name: " + categoryName));
        Set<ProductResponseSlimDto> productResponseDtoSet=
                changeCategoryProductsSetRequestDto.getProductResponseSlimDtos();
        for (ProductResponseSlimDto productResponseDto : productResponseDtoSet) {
            String productName = productResponseDto.getName();
            if (!productRepository.findByName(productName).isPresent()) {
                throw new ProductNotFoundException("No Product found with name: " + productName);
            }
        }
        Set<Product> products=productResponseDtoSet
                .stream()
                .map(productResponseDto -> {
                    try {
                        return productRepository.findByName(productResponseDto.getName())
                                .orElseThrow(() -> new ProductNotFoundException(
                                        "No Product found with name: " + productResponseDto.getName()
                                ));
                    } catch (ProductNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet());
        category.setProducts(products);
        for (Product product : products){
            product.setCategory(category);
        }
        categoryRepository.save(category);
    }

    @Override
    public void DeleteCategory(String categoryName) throws CategoryNotFoundException {
        Category category=categoryRepository.findByName(categoryName)
                .orElseThrow (() -> new CategoryNotFoundException("No Category found with name: " + categoryName));
        deletionService.deleteCategory(category);
    }
}
