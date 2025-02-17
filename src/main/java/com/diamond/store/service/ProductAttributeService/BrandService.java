package com.diamond.store.service.ProductAttributeService;

import com.diamond.store.exception.ResourceNotFoundException;
import com.diamond.store.model.Brand;
import com.diamond.store.repository.BrandRepository;
import org.springframework.stereotype.Service;

@Service
public class BrandService extends ProductAttributeServiceImpl<Brand, Integer> {

    private final BrandRepository brandRepository;

    public BrandService(BrandRepository repository) {
        super(repository);
        this.brandRepository = repository;
    }

    @Override
    public void create(Brand model) {
        brandRepository.findByBrandName(model.getBrandName()).ifPresentOrElse(
                brand -> {
                    throw new ResourceNotFoundException("Collection not found");
                }, () -> {
                    brandRepository.save(model);
                }
        );
    }

    public Brand findOrCreateByName(String name) {

        return brandRepository.findByBrandName(name)
                .orElseGet(() -> {
                    Brand newBrand = Brand.builder()
                            .brandName(name)
                            .build();
                    return brandRepository.save(newBrand);
                });

    }
}
