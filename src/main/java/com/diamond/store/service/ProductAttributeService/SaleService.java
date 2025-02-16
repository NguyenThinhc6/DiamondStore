package com.diamond.store.service.ProductAttributeService;

import com.diamond.store.exception.ResourceConflictException;
import com.diamond.store.model.Sale;
import com.diamond.store.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SaleService extends ProductAttributeServiceImpl<Sale, String> {

    private final SaleRepository saleRepository;

    public SaleService(SaleRepository repository) {
        super(repository);
        this.saleRepository = repository;
    }


    @Override
    public void create(Sale model) {
        Optional<Sale> existingSale = saleRepository.findByTimeStartLessThanEqualAndTimeEndGreaterThanEqual(
                model.getTimeStart(), model.getTimeEnd()
        );

        if (existingSale.isPresent() && existingSale.get().isEnable()) {
            throw new ResourceConflictException("Sale already exists in event");
        }

        saleRepository.save(model);
    }
}
