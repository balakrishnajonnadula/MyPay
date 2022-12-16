package com.mypay.product.dao;

import java.util.List;

import com.mypay.product.dto.Product;

public interface ProductDao {
	public void addProduct();

	public boolean deleteProduct(String id);

	public Product viewProduct(String id);

	public boolean updateProduct(String id);

	public boolean updateQty(String id);

	public boolean updatePrice(String id);

	public List<Product> viewAllProducts();

	public double buyProduct(String id);
}
