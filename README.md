Sinh viên thực hiện

Nguyễn Bảo Tân

MSSV: 12523079 Lớp: 12523T.1

Lê Văn Long

MSSV: 10123211 Lớp: 12523T.1 Giảng viên hướng dẫn

Bùi Đức Thọ
# GIỚI THIỆU ĐỀ TÀI

## Bối cảnh

Hiện nay các trò chơi kiến thức trên điện thoại ngày càng được nhiều người yêu thích bởi tính giải trí kết hợp học tập. Trong đó, chương trình "Ai Là Triệu Phú" là một gameshow nổi tiếng giúp người chơi kiểm tra kiến thức ở nhiều lĩnh vực khác nhau.

## Vấn đề hiện tại

Người dùng thường chỉ có thể trải nghiệm trò chơi thông qua truyền hình hoặc các website trực tuyến. Điều này gây hạn chế khi không có kết nối Internet hoặc muốn chơi trên thiết bị di động một cách độc lập.

## Giải pháp

Xây dựng ứng dụng Ai Là Triệu Phú trên nền tảng Android cho phép người chơi tham gia trả lời câu hỏi, sử dụng các quyền trợ giúp và lưu kết quả trực tiếp trên thiết bị bằng SQLite.

# BÀI TOÁN

Xây dựng hệ thống trò chơi Ai Là Triệu Phú cho phép:

* Người dùng đăng nhập.
* Tham gia trả lời các câu hỏi trắc nghiệm.
* Kiểm tra đáp án đúng sai.
* Tính điểm và tiền thưởng.
* Sử dụng các quyền trợ giúp.
* Lưu điểm số và bảng xếp hạng.
* Admin quản lý ngân hàng câu hỏi.

# MỤC TIÊU ĐỀ TÀI

Ứng dụng hỗ trợ:

* Đăng nhập hệ thống.
* Quản lý tài khoản.
* Hiển thị câu hỏi trắc nghiệm.
* Kiểm tra đáp án.
* Tính điểm người chơi.
* Hỗ trợ 50:50.
* Hỗ trợ gọi điện thoại cho người thân.
* Hỗ trợ hỏi ý kiến khán giả.
* Lưu điểm người chơi.
* Hiển thị bảng xếp hạng.
* Quản lý câu hỏi bằng tài khoản Admin.

# PHẠM VI HỆ THỐNG

## Đối tượng sử dụng

* Người chơi.
* Quản trị viên (Admin).

## Chức năng hỗ trợ

### Người chơi

* Đăng nhập.
* Chơi game.
* Sử dụng quyền trợ giúp.
* Xem kết quả.
* Xem bảng xếp hạng.
* Truy cập Fanpage.

### Admin

* Quản lý câu hỏi.
* Thêm câu hỏi.
* Sửa câu hỏi.
* Xóa câu hỏi.
* Quản lý dữ liệu trò chơi.

## Giới hạn hệ thống

* Hoạt động trên Android.
* Dữ liệu lưu cục bộ bằng SQLite.
* Chưa hỗ trợ đồng bộ trực tuyến.
* Chưa hỗ trợ nhiều người chơi cùng lúc.

# THIẾT KẾ CƠ SỞ DỮ LIỆU

Ứng dụng sử dụng SQLite.

## Bảng TaiKhoan

* MaTK
* TenDangNhap
* MatKhau
* VaiTro

## Bảng CauHoi

* MaCH
* NoiDung
* DapAnA
* DapAnB
* DapAnC
* DapAnD
* DapAnDung

## Bảng DiemSo

* MaDiem
* TenNguoiChoi
* Diem
* ThoiGian

## Bảng XepHang

* MaXepHang
* TenNguoiChoi
* Diem

# LUỒNG CHỨC NĂNG

## Người chơi

* Đăng nhập.
* Bắt đầu trò chơi.
* Trả lời câu hỏi.
* Sử dụng trợ giúp.
* Nhận kết quả.
* Xem bảng xếp hạng.

## Admin

* Đăng nhập quản trị.
* Quản lý ngân hàng câu hỏi.
* Thêm, sửa, xóa dữ liệu câu hỏi.

# CÔNG NGHỆ VÀ KIẾN TRÚC

## Nền tảng

Android

## Ngôn ngữ

Java

## Giao diện

XML Layout

## Cơ sở dữ liệu

SQLite

## IDE

Android Studio

## Kiến trúc

Three Layer Architecture

### Presentation Layer

* StartActivity
* LoginActivity
* HomeActivity
* AdminActivity
* GameActivity

### Business Layer

* Xử lý đăng nhập.
* Kiểm tra đáp án.
* Tính điểm.
* Xử lý trợ giúp.

### Data Access Layer

* Kết nối SQLite.
* CRUD dữ liệu.
* Truy xuất câu hỏi.
* Lưu điểm.

# CHỨC NĂNG HỆ THỐNG

## Đăng nhập

* Kiểm tra tài khoản.
* Kiểm tra mật khẩu.
* Phân quyền người dùng.

## Chơi game

* Hiển thị câu hỏi.
* Hiển thị đáp án.
* Kiểm tra đáp án.
* Chuyển câu hỏi tiếp theo.

## Trợ giúp 50:50

* Loại bỏ hai đáp án sai.

## Gọi điện thoại cho người thân

* Đưa ra gợi ý đáp án.

## Hỏi ý kiến khán giả

* Hiển thị tỷ lệ bình chọn.

## Lưu điểm

* Lưu kết quả người chơi.

## Xếp hạng

* Hiển thị danh sách điểm cao nhất.

## Fanpage

* Chuyển người dùng tới trang Facebook của ứng dụng bằng Intent ACTION_VIEW.

# KẾT QUẢ ĐẠT ĐƯỢC

✔ Xây dựng thành công trò chơi Ai Là Triệu Phú trên Android.

✔ Thiết kế giao diện trực quan.

✔ Xây dựng hệ thống câu hỏi bằng SQLite.

✔ Hỗ trợ các quyền trợ giúp.

✔ Lưu điểm và xếp hạng người chơi.

✔ Chức năng Admin hoạt động ổn định.

✔ Ứng dụng chạy độc lập không cần Internet.

# HƯỚNG PHÁT TRIỂN

* Bổ sung nhiều bộ câu hỏi hơn.
* Kết nối Firebase.
* Đồng bộ dữ liệu trực tuyến.
* Thêm âm thanh và hiệu ứng.
* Thêm chế độ chơi online.
* Thêm bảng xếp hạng toàn cầu.
* Chia sẻ kết quả lên mạng xã hội.

# TÀI KHOẢN DEMO

Tên đăng nhập: admin

Mật khẩu: admin123

(Có thể thay đổi tùy dữ liệu khởi tạo trong SQLite)

