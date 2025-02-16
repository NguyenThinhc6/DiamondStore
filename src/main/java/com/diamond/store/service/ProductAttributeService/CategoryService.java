package com.diamond.store.service.ProductAttributeService;

import com.diamond.store.exception.ResourceNotFoundException;
import com.diamond.store.model.Category;
import com.diamond.store.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends ProductAttributeServiceImpl<Category, Integer> {

    private final CategoryRepository categoryRepository;


    public CategoryService(CategoryRepository repository) {
        super(repository);
        this.categoryRepository = repository;
    }

    @Override
    public void create(Category model) {
        categoryRepository.findByCategoryName(model.getCategoryName()).ifPresentOrElse(
                category -> {
                    throw new ResourceNotFoundException("Collection not found");
                }, () -> {
                    categoryRepository.save(model);
                }
        );
    }
}
