package com.codegym.dao;
import com.codegym.model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ProductDAO {
  public List<Product> findAll() throws SQLException {
    List<Product> list=new ArrayList<>();
    String sql="SELECT id,name,price,quantity FROM products ORDER BY id DESC";
    try(Connection c=Database.getConnection();PreparedStatement s=c.prepareStatement(sql);ResultSet rs=s.executeQuery()) {
      while(rs.next()) list.add(map(rs));
    }
    return list;
  }
  public Product findById(int id) throws SQLException {
    try(Connection c=Database.getConnection();PreparedStatement s=c.prepareStatement("SELECT id,name,price,quantity FROM products WHERE id=?")) {
      s.setInt(1,id);
      try(ResultSet rs=s.executeQuery()) {return rs.next()?map(rs):null;}
    }
  }
  public void insert(Product p) throws SQLException {
    try(Connection c=Database.getConnection();PreparedStatement s=c.prepareStatement("INSERT INTO products(name,price,quantity) VALUES(?,?,?)")) {
      bind(s,p);s.executeUpdate();
    }
  }
  public boolean update(Product p) throws SQLException {
    try(Connection c=Database.getConnection();PreparedStatement s=c.prepareStatement("UPDATE products SET name=?,price=?,quantity=? WHERE id=?")) {
      bind(s,p);s.setInt(4,p.getId());return s.executeUpdate()==1;
    }
  }
  public boolean delete(int id) throws SQLException {
    try(Connection c=Database.getConnection();PreparedStatement s=c.prepareStatement("DELETE FROM products WHERE id=?")) {
      s.setInt(1,id);return s.executeUpdate()==1;
    }
  }
  private void bind(PreparedStatement s,Product p) throws SQLException {
    s.setString(1,p.getName());s.setBigDecimal(2,p.getPrice());s.setInt(3,p.getQuantity());
  }
  private Product map(ResultSet r) throws SQLException {
    return new Product(r.getInt("id"),r.getString("name"),r.getBigDecimal("price"),r.getInt("quantity"));
  }
}
