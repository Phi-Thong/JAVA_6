package asm.poly.asm_java6.service;

import java.util.List;

public interface ProductSizeService {
    int getTotalStockByProductId(Integer productId);
    List<Integer> getAvailableSizes(Integer productId);
}