package com.diamond.store.service.ProductAttributeService;

import com.diamond.store.exception.ResourceConflictException;
import com.diamond.store.exception.ResourceNotFoundException;
import com.diamond.store.model.Product;
import com.diamond.store.model.Sale;
import com.diamond.store.repository.SaleRepository;
import com.diamond.store.service.ProductService.ProductService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SaleService extends ProductAttributeServiceImpl<Sale, String> {

    private final SaleRepository saleRepository;
    private final ProductService productService;
    public SaleService(SaleRepository repository, ProductService productService) {
        super(repository);
        this.saleRepository = repository;
        this.productService = productService;
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

    public void addProductToSale(String saleId, int productId) {

        Sale sale = saleRepository.findById(saleId).orElseThrow(() -> new ResourceNotFoundException("Sale not found"));
        Product product = productService.findById(productId);

        sale.addProduct(product);

        saleRepository.save(sale);

    }
}
