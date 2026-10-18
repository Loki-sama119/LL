# Thư viện giải phương trình bậc 2 - CodeGym

Dự án Java Library giải phương trình **ax² + bx + c = 0** (a khác 0), đóng gói thành file `.jar`.

## Yêu cầu

- Java JDK 17+ (đã kiểm tra với Java 21)
- Gradle (nếu dùng Gradle) hoặc Maven (nếu dùng Maven)

## Chức năng

- Delta > 0: hai nghiệm thực (x1 <= x2)
- Delta = 0: nghiệm kép
- Delta < 0: vô nghiệm thực
- Nếu `a = 0` hoặc hệ số là `NaN`/vô cực: ném `IllegalArgumentException`
- `Result` chứa loại nghiệm, delta và các nghiệm tương ứng.

## Cấu trúc

`src/main/java/com/codegym/quadratic/QuadraticEquation.java` là mã thư viện; `Demo.java` là chương trình minh họa; `src/test/.../QuadraticEquationSmokeTest.java` là kiểm thử không cần JUnit.

## Đóng gói bằng Gradle

Tại thư mục gốc có `build.gradle` và `settings.gradle`, chạy:

```powershell
gradle clean build
gradle runSmokeTest
gradle runDemo
```

Thư viện ở `build/libs/quadratic-equation-library-1.0.0.jar` (Gradle cũng sinh sources và javadoc jar).

## Hoặc đóng gói bằng Maven

```powershell
mvn clean package
```

Thư viện ở `target/quadratic-equation-library-1.0.0.jar`.

## Kiểm thử mà không cần Gradle/Maven

```powershell
New-Item -ItemType Directory -Force out/main,out/test | Out-Null
javac -d out/main src/main/java/com/codegym/quadratic/*.java
javac -cp out/main -d out/test src/test/java/com/codegym/quadratic/*.java
java -cp 'out/main;out/test' com.codegym.quadratic.QuadraticEquationSmokeTest
java -cp out/main com.codegym.quadratic.Demo
jar cf quadratic-equation-library-1.0.0.jar -C out/main com/codegym/quadratic/QuadraticEquation.class -C out/main 'com/codegym/quadratic/QuadraticEquation$Result.class' -C out/main 'com/codegym/quadratic/QuadraticEquation$RootType.class'
```

Trên Windows PowerShell, lệnh javac `*.java` có thể không được mở rộng trong một số phiên bản; nên ưu tiên Gradle/Maven hoặc nhập đường dẫn đầy đủ.

## Sử dụng thư viện từ dự án khác

```java
import com.codegym.quadratic.QuadraticEquation;

QuadraticEquation.Result r = QuadraticEquation.solve(1, -3, 2);
System.out.println(r.getType()); // TWO_REAL_ROOT
System.out.println(r.getX1());   // 1.0
System.out.println(r.getX2());   // 2.0
```

## Nộp GitHub cho CodeGym

Tạo repo công khai, giải nén ZIP và upload **các nội dung nằm bên trong ZIP** lên ngay gốc repository. Đảm bảo nhìn thấy ngay `build.gradle`, `settings.gradle`, `pom.xml`, `README.md`, `src/` ở trang chính GitHub. Không upload nguyên ZIP hoặc bọc toàn bộ dự án trong thư mục con. Copy URL repo vào phần nộp bài.

Lưu ý: Dự án này là **thư viện Java JAR**, không phải ứng dụng Java Web WAR, nên không cần Tomcat, JSP hay `web.xml`.
