# CodeGym – Luyện tập MySQL JDBC Transaction (Rollback)

Bài này **kế thừa đầy đủ** CRUD, tìm kiếm country, sắp xếp name, Stored Procedures và các bài Employee transaction trước đó. Nội dung chính: sửa/kiểm chứng `addUserTransaction()` trong `UserDAO` để đảm bảo SQL lỗi thì `rollback()` hoàn tác cả INSERT users và INSERT user_permission.

## File quan trọng cho người chấm

- `pom.xml` ngay **gốc repository**, packaging `war`, artifactId `user-management`.
- `src/main/java/com/codegym/dao/IUserDAO.java` – `addUserTransaction(User, int[])`.
- `src/main/java/com/codegym/dao/UserDAO.java` – một Connection, `setAutoCommit(false)`, `RETURN_GENERATED_KEYS`, chèn user và permissions, `commit()`, `rollback()` trong `catch`, đóng kết nối.
- `src/main/java/com/codegym/tools/TransactionRollbackDemo.java` – **cố tình kích hoạt lỗi khóa ngoại SQL**, xác minh user vừa thử chèn không còn trong DB.
- `database/demo.sql`, `database/transaction_permissions.sql`, `database/stored_procedures.sql`, `database/stored_procedures_crud.sql` – lệnh thiết lập MySQL.
- `src/main/webapp/user/create.jsp` – checkbox gán quyền; `src/main/java/com/codegym/controller/UserServlet.java` – gọi `addUserTransaction` khi thêm User.
- `src/main/webapp/WEB-INF/web.xml` và các View JSP.

## Chuẩn bị MySQL

Dùng một DB **demo riêng**. Chạy theo thứ tự:

```sql
SOURCE database/demo.sql;
SOURCE database/transaction_permissions.sql;
SOURCE database/stored_procedures.sql;
SOURCE database/stored_procedures_crud.sql;
```

Nếu dùng MySQL Workbench thay vì `SOURCE`, mở từng file và chạy lần lượt. Không dùng database thật. Cần xác nhận cả bảng `users`, `user_permission` dùng InnoDB và có khóa ngoại. Không chạy lại `demo.sql` trên dữ liệu cần giữ.

### Cấu hình JDBC (PowerShell)

```powershell
$env:JDBC_URL = 'jdbc:mysql://localhost:3306/demo?useUnicode=true&characterEncoding=UTF-8'
$env:JDBC_USERNAME = 'root'
$env:JDBC_PASSWORD = 'MAT_KHAU_MYSQL_CUA_BAN'
```

## Build và chạy demo Rollback

Tại **thư mục gốc chứa pom.xml**:

```powershell
mvn clean package
mvn exec:java "-Dexec.mainClass=com.codegym.tools.TransactionRollbackDemo"
```

### Tại sao chắc chắn xuất hiện lỗi SQL?

Demo gọi chính phương thức `addUserTransaction()` đang được ứng dụng dùng:

```java
User user = new User("Rollback Demo", uniqueEmail, "Viet Nam");
dao.addUserTransaction(user, new int[] {1, Integer.MAX_VALUE});
```

Quyền `1` có thật trong bảng `permission`; ID `2147483647` **không tồn tại**, dẫn tới lỗi khóa ngoại khi INSERT vào `user_permission`. Hai lần INSERT dùng **cùng Connection** và đang `setAutoCommit(false)`. SQL exception sẽ đi vào `catch` của DAO → `connection.rollback()` → ném lại exception. Java demo bắt exception dự kiến, kiểm tra `SELECT COUNT(*) FROM users WHERE email = ?` bằng kết nối mới và in `PASS` nếu vẫn là 0. Test không sửa/xóa user hiện hữu và không để lại dữ liệu demo nếu rollback thành công.

**Kết quả mong đợi:**

```text
EXPECTED SQLException: ... foreign key constraint fails ...
User rows before: 0, after: 0
PASS: addUserTransaction() rolled back the full operation.
```

Nếu in `FAILED` hoặc không nhận SQL exception, cần kiểm tra cấu trúc FK/InnoDB, không coi là đạt.

Để quan sát trên MySQL, sử dụng chính email hiển thị trong bản sửa demo nếu muốn chạy kiểm chứng riêng. `TransactionRollbackDemo` tạo email UUID mới ở mỗi lượt, vì vậy có thể chạy nhiều lần mà không làm thay đổi user thật.

## Kiểm tra luồng thêm User thành công

Chạy Tomcat 10.1 sau khi triển khai `target/user-management.war`. Vào `http://localhost:8080/user-management/users?action=create`, nhập tên/email/quốc gia, chọn quyền Add (1) và View (4), nhấn Lưu. Kiểm tra SQL:

```sql
SELECT u.id, u.name, u.email, up.permission_id
FROM users u LEFT JOIN user_permission up ON u.id = up.user_id
ORDER BY u.id DESC, up.permission_id;
```

Kết quả với user mới: 2 hàng permission IDs `1` và `4`. Tính năng người dùng bình thường không cố tình gây lỗi.

## Lưu ý khi nộp GitHub

Chọn **các nội dung của ZIP** để upload vào gốc repository. Trên trang GitHub phải hiện `pom.xml`, `src/` và `database/` ở gốc, **không đặt tất cả trong một thư mục bọc ngoài**. Nộp đường dẫn repository Public, có thể nhấn vào `src/main/webapp/WEB-INF/web.xml` và `src/main/java/com/codegym/dao/UserDAO.java` để kiểm tra.

## Giới hạn kiểm thử

Gói mã nguồn không tự động kết nối tới MySQL của bạn. Chỉ coi kết quả transaction được xác minh khi chạy lệnh demo và đối chiếu MySQL thực tế. Việc mã Java biên dịch không đồng nghĩa ứng dụng đã được triển khai Tomcat thành công.

---

## Tài liệu lịch sử

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
