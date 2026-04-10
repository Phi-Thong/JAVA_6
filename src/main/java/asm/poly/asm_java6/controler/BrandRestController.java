package asm.poly.asm_java6.controler;

import asm.poly.asm_java6.enity.Brand;
import asm.poly.asm_java6.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands2")
public class BrandRestController {

    @Autowired
    private BrandService brandService;

    @GetMapping
    public List<Brand> getAllBrands() {
        return brandService.getAllBrands();
    }
}