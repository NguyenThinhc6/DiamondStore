package com.diamond.store.service.ProductAttributeService;

import java.util.List;

public interface ProductAttributeService<T, Id> {

    List<T> findAll();

    T findById(Id id);

    void create (T model);

    void update (T model, Id id);

    void delete (Id id  );

}
