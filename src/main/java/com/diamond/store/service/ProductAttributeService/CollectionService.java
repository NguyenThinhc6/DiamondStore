package com.diamond.store.service.ProductAttributeService;

import com.diamond.store.exception.ResourceNotFoundException;
import com.diamond.store.model.Collection;
import com.diamond.store.repository.CollectionRepository;
import org.springframework.stereotype.Service;

@Service
public class CollectionService extends ProductAttributeServiceImpl<Collection, Integer> {

    private final CollectionRepository collectionRepository;

    public CollectionService(CollectionRepository collectionRepository) {
        super(collectionRepository);
        this.collectionRepository = collectionRepository;
    }


    @Override
    public void create(Collection model) {

        collectionRepository.findByCollectionName(model.getCollectionName()).ifPresentOrElse(
                collection -> {
                    throw new ResourceNotFoundException("Collection not found");
                }, () -> {
                    collectionRepository.save(model);
                }
        );
    }
}
