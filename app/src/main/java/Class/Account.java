package Class;

public class Account {
    private String TaiKhoan;
    private String MatKhau;

    public Account() {
    }

    public Account(String taiKhoan, String matKhau) {
        TaiKhoan = taiKhoan;
        MatKhau = matKhau;
    }

    public String getTaiKhoan() {
        return TaiKhoan;
    }

    public String getMatKhau() {
        return MatKhau;
    }

}
