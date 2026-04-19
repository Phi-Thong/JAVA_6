package asm.poly.asm_java6.dto;

public class FeaturedProductDTO {
    private Long id;
    private String tenSanPham;
    private Double gia;
    private String anhChinh;
    private String brand; // tên thương hiệu
    private Long totalAddedToCart; // tổng số lần được thêm vào giỏ

    // Constructors
    public FeaturedProductDTO() {
    }

    public FeaturedProductDTO(Long id, String tenSanPham, Double gia, String anhChinh, String brand, Long totalAddedToCart) {
        this.id = id;
        this.tenSanPham = tenSanPham;
        this.gia = gia;
        this.anhChinh = anhChinh;
        this.brand = brand;
        this.totalAddedToCart = totalAddedToCart;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public Double getGia() {
        return gia;
    }

    public void setGia(Double gia) {
        this.gia = gia;
    }

    public String getAnhChinh() {
        return anhChinh;
    }

    public void setAnhChinh(String anhChinh) {
        this.anhChinh = anhChinh;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getTotalAddedToCart() {
        return totalAddedToCart;
    }

    public void setTotalAddedToCart(Long totalAddedToCart) {
        this.totalAddedToCart = totalAddedToCart;
    }
}