package API.com.example.E_COMMERCY.service.product;

import API.com.example.E_COMMERCY.dto.category.CategoryMapper;
import API.com.example.E_COMMERCY.dto.product.ProductMapper;
import API.com.example.E_COMMERCY.dto.product.ProductResponseDto;
import API.com.example.E_COMMERCY.dto.product.request.*;
import API.com.example.E_COMMERCY.exception.CategoryNotFoundException;
import API.com.example.E_COMMERCY.exception.ProductNameAlreadyExist;
import API.com.example.E_COMMERCY.exception.ProductNotFoundException;
import API.com.example.E_COMMERCY.model.Category;
import API.com.example.E_COMMERCY.model.Product;
import API.com.example.E_COMMERCY.repository.CategoryRepository;
import API.com.example.E_COMMERCY.repository.ProductRepository;
import API.com.example.E_COMMERCY.service.deletion.DeletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService implements ProductInterface{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final DeletionService deletionService;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper, CategoryMapper categoryMapper, CategoryRepository categoryRepository, DeletionService deletionService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
        this.deletionService = deletionService;
    }


    @Override
    public ProductResponseDto displayProductById(Long id) throws ProductNotFoundException {
        Product product = productRepository.findById(Long.valueOf(id))
                .orElseThrow (() -> new ProductNotFoundException("No Product found with id: " + id));
        return productMapper.toDto(product);
    }

    @Override
    public ProductResponseDto displayProductByName(String productName) throws ProductNotFoundException {
        Product product = productRepository.findByName(productName)
                .orElseThrow (() -> new ProductNotFoundException("No Product found with name: " + productName));
        return productMapper.toDto(product);
    }

    @Override
    public List<ProductResponseDto> displayAllProducts() throws ProductNotFoundException {
        List<Product> products=productRepository.findAll();
        if (products.isEmpty()) {
           throw  new ProductNotFoundException("product list is empty ");
        }
        return products.stream().map(productMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void AddProduct(NewProductRequestDto newProductRequestDto) throws ProductNameAlreadyExist {
        String productName=newProductRequestDto.getName();
        if (productRepository.findByName(productName).isPresent()) {
            throw new ProductNameAlreadyExist("this name "+productName+" Already Exist");
        }
        Product product = productMapper.toEntity(newProductRequestDto);
        product.getCategory().getProducts().add(product);
        productRepository.save(product);
    }

    @Override
    public void UpdateProductName(ChangeProductNameRequestDto changeProductNameRequestDto) throws ProductNotFoundException {
        String productName=changeProductNameRequestDto.getName();
        Product product = productRepository.findByName(productName)
                .orElseThrow (() -> new ProductNotFoundException("No Product found with name: " + productName));
        product.setName(changeProductNameRequestDto.getNewName());
        productRepository.save(product);
    }

    @Override
    public void UpdateProductDescription(ChangeProductDescriptionDto changeProductDescriptionDto) throws ProductNotFoundException {
        String productName= changeProductDescriptionDto.getName();
        Product product = productRepository.findByName(productName)
                .orElseThrow (() -> new ProductNotFoundException("No Product found with name: " + productName));
        product.setDescription(changeProductDescriptionDto.getDescription());
        productRepository.save(product);
    }

    @Override
    public void UpdateProductPrice(ChangeProductPriceDto changeProductPriceDto) throws ProductNotFoundException {
        String productName= changeProductPriceDto.getName();
        Product product = productRepository.findByName(productName)
                .orElseThrow (() -> new ProductNotFoundException("No Product found with name: " + productName));
        product.setPrice(changeProductPriceDto.getPrice());
        productRepository.save(product);
    }

    @Override
    public void UpdateProductCategory(ChangeProductCategoryRequest changeProductCategoryRequest) throws ProductNotFoundException, CategoryNotFoundException {
        String productName= changeProductCategoryRequest.getName();
        Product product = productRepository.findByName(productName)
                .orElseThrow (() -> new ProductNotFoundException("No Product found with name: " + productName));
        String categoryName=changeProductCategoryRequest
                        .getCategoryResponseDto()
                        .getName();

        Category category=categoryRepository.findByName(categoryName)
                .orElseThrow (() -> new CategoryNotFoundException("No Category found with name: " + categoryName));
        product.setCategory(category);
        category.getProducts().add(product);
        productRepository.save(product);
    }

    @Override
    public void DeleteProductByName(String productName) throws ProductNotFoundException {
        Product product = productRepository.findByName(productName)
                .orElseThrow (() -> new ProductNotFoundException("No Product found with name: " + productName));
        deletionService.deleteProduct(product);
    }
}
