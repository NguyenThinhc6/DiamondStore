package com.diamond.store.service.ProductService;

import com.diamond.store.dto.request.ProductRequest;
import com.diamond.store.exception.ResourceConflictException;
import com.diamond.store.exception.ResourceNotFoundException;
import com.diamond.store.model.*;
import com.diamond.store.repository.ProductRepository;
import com.diamond.store.service.ProductAttributeService.BrandService;
import com.diamond.store.service.ProductAttributeService.CategoryService;
import com.diamond.store.service.ProductAttributeService.CollectionService;
import com.diamond.store.util.ApplicationMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final CollectionService collectionService;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(int id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ApplicationMessage.DATA_NOTFOUND));
    }

    @Transactional
    @Override
    public void createProduct(ProductRequest productRequest) {
        productRepository.findByProductName(productRequest.getProductName()).ifPresentOrElse(product ->
        {
            throw new ResourceConflictException(ApplicationMessage.PRODUCT_CONFLICT);
        }, () -> {

            Product product = new Product();

            Category category = categoryService.findOrCreateByName(productRequest.getCategory());
            if (productRequest.getCollection() != null) {
                Collection collection = collectionService.findOrCreateByName(productRequest.getCollection());
                product.setCollection(collection);
            }
            Brand brand = brandService.findOrCreateByName(productRequest.getBrand());


            if (productRequest.getCollection() != null) {
                Collection collection = collectionService.findOrCreateByName(productRequest.getCollection());
            }
            List<Size> sizes = productRequest.getSizes().stream().map(s -> {
                return Size.builder().sizeName(s).product(product).build();
            }).toList();


            product.setBrand(brand);
            product.setCategory(category);
            product.setSizes(sizes);
            product.setDescription(productRequest.getDescription());
            product.setProductName(productRequest.getProductName());
            product.setPrice(productRequest.getPrice());
            product.setStock(productRequest.getStock());

            productRepository.save(product);


        });
    }

    @Override
    public void updateProduct(ProductRequest productRequest, int id) {
        productRepository.findById(id).ifPresentOrElse(product -> {

            product.setProductName(productRequest.getProductName());
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());
            product.setStock(productRequest.getStock());

            List<Size> sizes = productRequest.getSizes().stream().map(s -> {
                return Size.builder().sizeName(s).product(product).build();
            }).toList();

            product.setSizes(sizes);

            productRepository.save(product);


        }, () -> {
            throw new ResourceNotFoundException(ApplicationMessage.DATA_NOTFOUND);
        });
    }

    @Override
    public void deleteProduct(int id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete, () -> {
            throw new ResourceNotFoundException(ApplicationMessage.DATA_NOTFOUND);
        });
    }
}
