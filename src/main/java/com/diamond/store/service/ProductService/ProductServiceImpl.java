package com.diamond.store.service.ProductService;

import com.diamond.store.dto.request.FileRequest;
import com.diamond.store.dto.request.ProductRequest;
import com.diamond.store.dto.response.FileResponse;
import com.diamond.store.exception.ResourceConflictException;
import com.diamond.store.exception.ResourceNotFoundException;
import com.diamond.store.model.*;
import com.diamond.store.repository.ProductRepository;
import com.diamond.store.service.ProductAttributeService.BrandService;
import com.diamond.store.service.ProductAttributeService.CategoryService;
import com.diamond.store.service.ProductAttributeService.CollectionService;
import com.diamond.store.service.UploadService.UploadService;
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
    private final UploadService uploadService;

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

            Product product = buildProduct(productRequest);
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

    @Transactional
    @Override
    public void createProductWithImage(ProductRequest productRequest) {
        productRepository.findByProductName(productRequest.getProductName()).ifPresentOrElse(product -> {
            throw new ResourceConflictException(ApplicationMessage.PRODUCT_CONFLICT);
        }, () -> {
            Product product = buildProduct(productRequest);

            FileRequest fileRequest = new FileRequest();
            fileRequest.setFiles(productRequest.getImages());
            fileRequest.setOwnerId(String.valueOf(product.getProductId()));
            fileRequest.setTags(new String[]{"image", "product"});

            List<FileResponse> responses = uploadService.uploadProductImages(fileRequest);

            product.setImages(responses.stream().map(fileResponse -> Image.builder()
                            .imageId(fileResponse.getFileId())
                            .imageUrl(fileResponse.getUrl())
                            .tag(String.join(",", fileRequest.getTags()))
                            .build())
                    .toList());

            productRepository.save(product);
        });
    }

    private Product buildProduct(ProductRequest productRequest) {
        Product product = new Product();

        Category category = categoryService.findOrCreateByName(productRequest.getCategory());
        Brand brand = brandService.findOrCreateByName(productRequest.getBrand());

        product.setCategory(category);
        product.setBrand(brand);

        // Nếu có Collection thì gán vào Product
        if (productRequest.getCollection() != null) {
            Collection collection = collectionService.findOrCreateByName(productRequest.getCollection());
            product.setCollection(collection);
        }

        // Tạo danh sách Size
        List<Size> sizes = productRequest.getSizes().stream()
                .map(s -> Size.builder().sizeName(s).product(product).build())
                .toList();
        product.setSizes(sizes);

        // Gán các thuộc tính còn lại
        product.setDescription(productRequest.getDescription());
        product.setProductName(productRequest.getProductName());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());

        return product;
    }
}
