package com.diamond.store.service.ProductAttributeService;

import com.diamond.store.exception.ResourceNotFoundException;
import com.diamond.store.util.ApplicationMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public class ProductAttributeServiceImpl<T, Id> implements ProductAttributeService<T, Id> {

    protected final JpaRepository<T, Id> repository;

    public ProductAttributeServiceImpl(JpaRepository<T, Id> repository) {
        this.repository = repository;
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public T findById(Id id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ApplicationMessage.DATA_NOTFOUND));
    }

    @Override
    public void create(T model) {

        repository.save(model);
    }

    @Override
    public void update(T model, Id id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(ApplicationMessage.DATA_NOTFOUND);
        }
        repository.save(model);
    }

    @Override
    public void delete(Id id) {
        repository.findById(id).ifPresentOrElse(repository::delete, () -> {
            throw new ResourceNotFoundException(ApplicationMessage.DATA_NOTFOUND);
        });
    }
}
