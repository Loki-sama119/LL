# CodeGym - Gọi MySQL Stored Procedures từ JDBC (CRUD)

Ứng dụng Java Web MVC dùng Java 17+, Maven WAR, JSP/JSTL (Jakarta Tags 3), Servlet 6 và MySQL Connector/J. Dựa trên bài Quản lý User trước đó, giữ CRUD, tìm kiếm country, sắp xếp name, transaction và bài thực hành Employee.

## Bài tập này bổ sung

- `get_all_users()` + `UserDAO.selectAllUsers()` gọi qua `CallableStatement` để hiển thị danh sách khi không lọc.
- `update_user(id,name,email,country)` + `UserDAO.updateUser()` gọi qua `CallableStatement`, trả `affected_rows`.
- `delete_user(id)` + `UserDAO.deleteUser()` gọi qua `CallableStatement`, xóa quan hệ `user_permission` và user **trong một giao dịch** để không bị lỗi khóa ngoại.
- `UserServlet`: danh sách mặc định dùng procedure; tìm kiếm/sắp xếp giữ PreparedStatement của bài trước; sửa và xóa dùng các DAO procedure mới.

## 1. Khởi tạo database demo (MySQL Workbench hoặc mysql client)

Chạy đúng thứ tự, trên database demo **dành riêng cho bài học**, không trên database thật:

1. `database/demo.sql` (tạo DB và bảng users)
2. `database/stored_procedures.sql` (procedure của bài cũ)
3. `database/transaction_permissions.sql` (bảng permissions của bài cũ)
4. `database/stored_procedures_crud.sql` (**procedure của bài mới**)

Nếu đã có `demo` và bảng chứa dữ liệu, không chạy lại `demo.sql` nếu không muốn thêm dữ liệu seed. Hai procedure update/delete nhận `id` và trả kết quả dạng `affected_rows` để Servlet xử lý trường hợp không tìm thấy.

## 2. Cấu hình MySQL

Dùng biến môi trường `JDBC_URL`, `JDBC_USERNAME`, `JDBC_PASSWORD` (xem `.env.example`). Không commit mật khẩu lên GitHub.

## 3. Build và chạy Tomcat 10.1

Tại thư mục **gốc** chứa `pom.xml`:

```powershell
mvn clean package
```

Đưa `target/user-management.war` vào `webapps/` của Tomcat 10.1; truy cập:

`http://localhost:8080/user-management/users`

Thử danh sách, bấm Sửa, lưu thông tin, bấm Xóa và xác nhận. Có thể kiểm chứng trực tiếp bằng `CALL get_all_users()` và `SELECT * FROM users` trong MySQL.

## 4. Nộp GitHub tránh bị báo thiếu file

Giải nén ZIP rồi **upload nội dung bên trong** lên gốc repository. Trên GitHub phải thấy ngay `pom.xml`, `src/`, `database/`, `README.md` và `.github/`. Tuyệt đối không đặt chúng trong thư mục con mang tên dự án.

## 5. Lưu ý

- `get_all_users`, `update_user` và `delete_user` là Stored Procedures; lớp DAO thực sự dùng `Connection.prepareCall()` và `CallableStatement`.
- Hàm `delete_user` dùng transaction vì có bảng `user_permission` mang khóa ngoại đến `users`.
- Các endpoint thử nghiệm Employee trong bài cũ có thể **xóa/tạo lại bảng Employee**; không gọi trên CSDL thật.
- Cần chạy `mvn clean package` và kiểm tra trực tiếp Tomcat/MySQL tại máy của bạn trước khi nộp. 
