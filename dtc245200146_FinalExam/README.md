# DTC245200146 – Final Exam – Quản lý sản phẩm JSP/Servlet

Dự án Java Web quản lý sản phẩm, lưu dữ liệu thật trong MySQL thông qua JDBC. Không dùng dữ liệu giả lập. Cấu trúc Maven WAR chuẩn, tương thích JDK 17+/Tomcat 10.1.

## Chức năng
- Danh sách sản phẩm (ID, tên, giá, số lượng) đọc từ database
- Thêm sản phẩm, chỉnh sửa, xóa có trang xác nhận
- Validate tên, giá và số lượng; thông báo lỗi ID 400/404
- Servlet controller `/products`, DAO dùng PreparedStatement để ngăn SQL injection, JSP đặt dưới `/WEB-INF/views`

## Chuẩn bị
1. Cài JDK 17+; Maven 3.9+; Apache Tomcat 10.1; MySQL 8.
2. Mở MySQL Workbench và chạy toàn bộ `database/products.sql`. File SQL tạo database `product_management`, bảng `products`, 3 dòng demo. Không chạy lại file SQL sau mỗi lần làm bài vì có thể bổ sung lại dữ liệu mẫu đã xóa.
3. Đặt các biến môi trường trong **cùng terminal chạy Tomcat** (hoặc cấu hình biến môi trường của hệ điều hành):

PowerShell:
```powershell
$env:DB_URL='jdbc:mysql://localhost:3306/product_management?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Ho_Chi_Minh'
$env:DB_USER='root'
$env:DB_PASSWORD='MAT_KHAU_MYSQL_CUA_BAN'
```

Nếu chạy Tomcat như Windows service, hãy cấu hình biến môi trường trong môi trường của service rồi khởi động lại service; biến đặt trong PowerShell thông thường sẽ không tự truyền vào service đã chạy.

## Build / Deploy
```powershell
mvn clean package
```
Build thành công tạo `target/product-management.war`. Dừng Tomcat (nếu cần), sao chép WAR vào `<TOMCAT_HOME>/webapps/` và chạy Tomcat. Truy cập:

**http://localhost:8080/product-management/products**

Nếu Tomcat dùng cổng khác hãy đổi 8080. Nếu đã deploy WAR cũ, thay WAR và restart/redeploy theo cách triển khai của bạn.

## Kiểm thử chức năng bằng tay
1. Mở danh sách, kiểm tra ba sản phẩm mẫu.
2. Nhấn Thêm sản phẩm, nhập tên/giá/số lượng hợp lệ rồi lưu; danh sách phải xuất hiện sản phẩm mới.
3. Bấm Sửa; biểu mẫu hiện dữ liệu hiện tại; đổi giá và lưu, xác minh danh sách.
4. Bấm Xóa; phải hiện trang hỏi xác nhận. Hủy không xóa. Xác nhận mới xóa.
5. Thử tên trắng, giá âm, số lượng âm và ID không tồn tại.
6. Kiểm tra MySQL: `SELECT id,name,price,quantity FROM product_management.products ORDER BY id;`.

## Thư mục gốc khi nộp ZIP
`pom.xml`, `README.md`, `database/`, `src/` phải nằm ngay **gốc ZIP**, không bọc trong thư mục `dtc245200146_FinalExam_project/`.

## Ghi chú
Đây là mã nguồn được tạo riêng theo yêu cầu đề bài. Phụ thuộc tải từ Maven Central theo giấy phép tương ứng. Dự án chưa được kiểm thử tích hợp với máy chủ MySQL và Tomcat trên máy của người nộp; cần kiểm tra theo checklist trước khi nộp.
