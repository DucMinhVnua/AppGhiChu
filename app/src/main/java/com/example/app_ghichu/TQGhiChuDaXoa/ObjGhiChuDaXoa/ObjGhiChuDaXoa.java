package com.example.app_ghichu.TQGhiChuDaXoa.ObjGhiChuDaXoa;

public class ObjGhiChuDaXoa {
    private String ghiChu;
    private String ngayThang;
    private int id;

    public ObjGhiChuDaXoa(String ghiChu, String ngayThang, int id) {
        this.ghiChu = ghiChu;
        this.ngayThang = ngayThang;
        this.id = id;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getNgayThang() {
        return ngayThang;
    }

    public void setNgayThang(String ngayThang) {
        this.ngayThang = ngayThang;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
