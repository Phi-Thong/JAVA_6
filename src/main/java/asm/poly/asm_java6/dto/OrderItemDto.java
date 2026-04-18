package asm.poly.asm_java6.dto;

public class OrderItemDto {
    private Long productId;
    private String tenSanPham;
    private String anhChinh;
    private int soLuong;
    private long gia;
    private long tongTienSanPham;

    public OrderItemDto() {
    }

    public OrderItemDto(Long productId, String tenSanPham, String anhChinh, int soLuong, long gia, long tongTienSanPham) {
        this.productId = productId;
        this.tenSanPham = tenSanPham;
        this.anhChinh = anhChinh;
        this.soLuong = soLuong;
        this.gia = gia;
        this.tongTienSanPham = tongTienSanPham;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getAnhChinh() {
        return anhChinh;
    }

    public void setAnhChinh(String anhChinh) {
        this.anhChinh = anhChinh;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public long getGia() {
        return gia;
    }

    public void setGia(long gia) {
        this.gia = gia;
    }

    public long getTongTienSanPham() {
        return tongTienSanPham;
    }

    public void setTongTienSanPham(long tongTienSanPham) {
        this.tongTienSanPham = tongTienSanPham;
    }
}