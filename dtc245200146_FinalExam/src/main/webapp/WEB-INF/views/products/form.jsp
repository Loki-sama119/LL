<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Thông tin sản phẩm</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css"></head><body>
<main class="wrap narrow"><a class="back" href="${pageContext.request.contextPath}/products">← Danh sách sản phẩm</a><div class="panel form"><h1>${empty product ? 'Thêm sản phẩm mới' : 'Cập nhật sản phẩm'}</h1><p class="muted">Nhập đầy đủ thông tin bên dưới</p>
<c:if test="${not empty error}"><p class="error"><c:out value="${error}"/></p></c:if>
<form method="post" action="${pageContext.request.contextPath}/products?action=${empty product ? 'create' : 'edit'}">
<c:if test="${not empty product}"><input type="hidden" name="id" value="${product.id}"></c:if>
<label>Tên sản phẩm<input name="name" maxlength="200" required value="<c:out value='${not empty formName ? formName : product.name}'/>" placeholder="Ví dụ: Laptop Dell"></label>
<label>Giá (VNĐ)<input name="price" type="number" min="0" step="0.01" required value="<c:out value='${not empty formPrice ? formPrice : product.price}'/>" placeholder="0.00"></label>
<label>Số lượng<input name="quantity" type="number" min="0" step="1" required value="<c:out value='${not empty formQuantity ? formQuantity : product.quantity}'/>" placeholder="0"></label>
<div class="form-actions"><button class="btn primary" type="submit">${empty product ? 'Tạo sản phẩm' : 'Lưu thay đổi'}</button><a class="btn" href="${pageContext.request.contextPath}/products">Hủy</a></div></form></div></main></body></html>
