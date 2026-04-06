package asm.poly.asm_java6.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import asm.poly.asm_java6.repository.productSizeRepository;
import asm.poly.asm_java6.service.ProductSizeService;

@Service
public class ProductSizeServiceImpl implements ProductSizeService {

    @Autowired
    private productSizeRepository productSizeRepository;

    @Override
    public int getTotalStockByProductId(Integer productId) {
        Integer total = productSizeRepository.getTotalStockByProductId(productId);
        return total != null ? total : 0;
    }

    @Override
    public List<Integer> getAvailableSizes(Integer productId) {
        return productSizeRepository.findSizesByProductId(productId);
    }
}