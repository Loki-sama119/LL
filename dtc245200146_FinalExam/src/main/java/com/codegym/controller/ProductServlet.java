package com.codegym.controller;
import com.codegym.dao.ProductDAO;
import com.codegym.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
  private final ProductDAO dao = new ProductDAO();
  @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {
    req.setCharacterEncoding("UTF-8");
    String action=req.getParameter("action");
    try {
      if("create".equals(action)) { forward(req,resp,"form.jsp");return; }
      if("edit".equals(action) || "delete".equals(action)) {
        int id=readId(req);
        Product p=dao.findById(id);
        if(p==null){resp.sendError(404,"Khong tim thay san pham");return;}
        req.setAttribute("product",p);
        forward(req,resp,"delete".equals(action) ? "delete.jsp":"form.jsp");return;
      }
      req.setAttribute("products",dao.findAll());forward(req,resp,"list.jsp");
    } catch (IllegalArgumentException ex) {resp.sendError(400,"ID khong hop le");}
      catch(SQLException ex){throw new ServletException("Khong the truy van MySQL",ex);}
  }
  @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {
    req.setCharacterEncoding("UTF-8");
    String action=req.getParameter("action");
    try {
      if("delete".equals(action)) {
        dao.delete(readId(req));
      } else if("create".equals(action) || "edit".equals(action)) {
        int id="edit".equals(action)?readId(req):0;
        Product p=parse(req,id);
        if("edit".equals(action) && !dao.update(p)) {resp.sendError(404,"Khong tim thay san pham");return;}
        if("create".equals(action)) dao.insert(p);
      } else {resp.sendError(400,"Thao tac khong hop le");return;}
      resp.sendRedirect(req.getContextPath()+"/products");
    } catch (IllegalArgumentException ex) {
      req.setAttribute("error",ex.getMessage());
      if("delete".equals(action)) {resp.sendError(400,ex.getMessage());return;}
      req.setAttribute("formName",req.getParameter("name"));
      req.setAttribute("formPrice",req.getParameter("price"));
      req.setAttribute("formQuantity",req.getParameter("quantity"));
      if("edit".equals(action)) {
        try{req.setAttribute("product",dao.findById(readId(req)));}catch(Exception ignored){}
      }
      resp.setStatus(400);forward(req,resp,"form.jsp");
    } catch(SQLException ex){throw new ServletException("Khong the luu MySQL",ex);}
  }
  private Product parse(HttpServletRequest req,int id) {
    String name=req.getParameter("name");
    if(name==null || name.isBlank() || name.trim().length()>200) throw new IllegalArgumentException("Ten san pham phai co 1-200 ky tu.");
    try {
      BigDecimal price=new BigDecimal(req.getParameter("price")).setScale(2,java.math.RoundingMode.UNNECESSARY);
      int quantity=Integer.parseInt(req.getParameter("quantity"));
      if(price.signum()<0 || price.precision()>15 || quantity<0) throw new IllegalArgumentException("Gia va so luong phai khong am; gia toi da 15 chu so.");
      return new Product(id,name.trim(),price,quantity);
    } catch(NumberFormatException | ArithmeticException | NullPointerException ex) {throw new IllegalArgumentException("Gia hoac so luong khong hop le.");}
  }
  private int readId(HttpServletRequest req) {
    try{int id=Integer.parseInt(req.getParameter("id"));if(id<=0)throw new NumberFormatException();return id;}
    catch(Exception ex){throw new IllegalArgumentException("ID khong hop le.");}
  }
  private void forward(HttpServletRequest req,HttpServletResponse resp,String jsp) throws ServletException,IOException {
    req.getRequestDispatcher("/WEB-INF/views/products/"+jsp).forward(req,resp);
  }
}
