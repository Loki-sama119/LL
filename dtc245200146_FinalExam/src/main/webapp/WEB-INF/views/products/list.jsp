<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!doctype html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Quản lý sản phẩm</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css"></head>
<body><main class="wrap"><div class="heading"><div><p class="eyebrow">CODEGYM · FINAL EXAM</p><h1>Quản lý sản phẩm</h1><p class="muted">Danh sách sản phẩm lưu trong cơ sở dữ liệu MySQL</p></div><a class="btn primary" href="${pageContext.request.contextPath}/products?action=create">+ Thêm sản phẩm</a></div>
<div class="panel"><div class="table-scroll"><table><thead><tr><th>ID</th><th>Tên sản phẩm</th><th>Giá (VNĐ)</th><th>Số lượng</th><th>Thao tác</th></tr></thead><tbody>
<c:forEach var="p" items="${products}"><tr><td>#<c:out value="${p.id}"/></td><td><strong><c:out value="${p.name}"/></strong></td><td><fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></td><td><c:out value="${p.quantity}"/></td><td class="actions"><a class="btn small" href="${pageContext.request.contextPath}/products?action=edit&amp;id=${p.id}">Sửa</a><a class="btn small danger" href="${pageContext.request.contextPath}/products?action=delete&amp;id=${p.id}">Xóa</a></td></tr></c:forEach>
<c:if test="${empty products}"><tr><td colspan="5" class="empty">Chưa có sản phẩm. Hãy thêm sản phẩm đầu tiên.</td></tr></c:if>
</tbody></table></div></div><p class="foot">JSP · Servlet · JDBC · MySQL</p></main></body></html>
