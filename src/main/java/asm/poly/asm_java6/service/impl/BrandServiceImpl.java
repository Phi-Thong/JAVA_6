package asm.poly.asm_java6.service.impl;

import asm.poly.asm_java6.enity.Brand;
import asm.poly.asm_java6.repository.BrandRepository;
import asm.poly.asm_java6.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandRepository brandRepository;

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }
}
