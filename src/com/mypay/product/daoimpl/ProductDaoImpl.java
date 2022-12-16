package com.mypay.product.daoimpl;

import java.nio.channels.SelectableChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.mypay.admin.AdminLogin;
import com.mypay.conetionutilities.ConncetionUtilities;
import com.mypay.product.dao.ProductDao;
import com.mypay.product.dto.Product;
import com.mypay.queries.ProductQueries;

public class ProductDaoImpl implements ProductDao {
	List<Product> products = new ArrayList<Product>();
	Scanner scanner = new Scanner(System.in);

	static int id = 1;
	static String key = "PRO";

	Connection connection = null;
	PreparedStatement pStatement = null;
	ResultSet resultSet = null;

	@Override
	public void addProduct() {

		try {
			connection = ConncetionUtilities.getConnection();
			System.out.println("Enter product name : ");
			String name = scanner.next();
			System.out.println("Enter product qty : ");
			int qty = scanner.nextInt();
			System.out.println("Enter product price : ");
			double price = scanner.nextDouble();
			int idNum = ProductDaoImpl.id;
			ProductDaoImpl.id++;
			String id = key + idNum;
			Product product = null;
			if (qty >= 0 && price >= 0) {
				product = new Product(id, name, qty, price, AdminLogin.adminId);
				products.add(product);
			}
			int executeUpdate = 0;
			if (product != null) {
				pStatement = connection.prepareStatement(ProductQueries.ADD_PRODUCT);
				pStatement.setString(1, product.getProductId());
				pStatement.setString(2, product.getProductName());
				pStatement.setInt(3, product.getQty());
				pStatement.setDouble(4, product.getPrice());
				pStatement.setString(5, product.getAdminId());
				executeUpdate = pStatement.executeUpdate();
			}
			if (executeUpdate != 0) {
				System.out.println(name + " successfully added..!");
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ConncetionUtilities.closeConnection(connection, pStatement);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		/*
		 * System.out.println("Enter product name : "); String name = scanner.next();
		 * System.out.println("Enter product qty : "); int qty = scanner.nextInt();
		 * System.out.println("Enter product price : "); double price =
		 * scanner.nextDouble(); int idNum = ProductDaoImpl.id; ProductDaoImpl.id++;
		 * String id = key + idNum; if (qty >= 0 && price >= 0) { Product product = new
		 * Product(id, name, qty, price, AdminLogin.adminId); products.add(product); }
		 */
	}

	@Override
	public boolean deleteProduct(String id) {
		boolean executeUpdate = false;
		try {
			connection = ConncetionUtilities.getConnection();
			pStatement = connection.prepareStatement(ProductQueries.DELETE_PRODUCT);
			pStatement.setString(1, id);
			int execute = pStatement.executeUpdate();
			if (execute != 0) {
				executeUpdate = true;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ConncetionUtilities.closeConnection(connection, pStatement);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		/*
		 * if (!products.isEmpty()) { for (Product pro : products) { if
		 * (pro.getProductId().equals(id)) { products.remove(pro); return true; } } }
		 */
		return executeUpdate;
	}

	@Override
	public Product viewProduct(String id) {
		Product product = null;
		try {
			connection = ConncetionUtilities.getConnection();
			pStatement = connection.prepareStatement(ProductQueries.VIEW_PRODUCT);
			pStatement.setString(1, id);
			resultSet = pStatement.executeQuery();
			while (resultSet.next()) {
				String proId = resultSet.getString(1);
				String proName = resultSet.getString(2);
				int proQty = resultSet.getInt(3);
				double proPrice = resultSet.getDouble(4);
				String adminId = resultSet.getString(5);
				product = new Product(proId, proName, proQty, proPrice, adminId);
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ConncetionUtilities.closeConnection(connection, pStatement, resultSet);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
//		if (!products.isEmpty()) {
//			for (Product pro : products) {
//				if (pro.getProductId().equals(id)) {
//					return pro;
//				}
//			}
//		}
		return product;
	}

	@Override
	public boolean updateProduct(String id) {
		boolean execute = false;
		try {
			connection = ConncetionUtilities.getConnection();
			pStatement = connection.prepareStatement(ProductQueries.UPDATE_PRODUCT);

			System.out.println("update name");
			String pName = scanner.next();
			pStatement.setString(1, pName);

			System.out.println("update qty");
			int pQty = scanner.nextInt();
			pStatement.setInt(2, pQty);

			System.out.println("update price");
			double pPrice = scanner.nextDouble();
			pStatement.setDouble(3, pPrice);

			pStatement.setString(4, id);
			int executeUpdate = pStatement.executeUpdate();
			if (executeUpdate != 0) {
				execute = true;
			}
			/*
			 * pro.setProductName(scanner.next()); pro.setQty(scanner.nextInt());
			 * pro.setPrice(scanner.nextDouble());
			 */
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ConncetionUtilities.closeConnection(connection, pStatement);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		/*
		 * if (!products.isEmpty()) { for (Product pro : products) { if
		 * (pro.getProductId().equals(id)) { System.out.println("update name");
		 * pro.setProductName(scanner.next()); System.out.println("update qty");
		 * pro.setQty(scanner.nextInt()); System.out.println("update price");
		 * pro.setPrice(scanner.nextDouble()); return true; } } }
		 */
		return execute;
	}

	@Override
	public boolean updateQty(String id) {
		if (!products.isEmpty()) {
			for (Product pro : products) {
				if (pro.getProductId().equals(id)) {
					System.out.println("update qty");
					pro.setQty(scanner.nextInt());
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean updatePrice(String id) {
		if (!products.isEmpty()) {
			for (Product pro : products) {
				if (pro.getProductId().equals(id)) {
					System.out.println("update price");
					pro.setPrice(scanner.nextDouble());
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public List<Product> viewAllProducts() {
		List<Product> list = new ArrayList<Product>();
		try {
			connection = ConncetionUtilities.getConnection();
			pStatement = connection.prepareStatement(ProductQueries.VIEWALL_PRODUCTS);
			resultSet = pStatement.executeQuery();
			while (resultSet.next()) {
				String proId = resultSet.getNString(1);
				String proName = resultSet.getNString(2);
				int proQty = resultSet.getInt(3);
				double proPrice = resultSet.getDouble(4);
				String adminId = resultSet.getNString(5);
				Product product = new Product(proId, proName, proQty, proPrice, adminId);
				list.add(product);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ConncetionUtilities.closeConnection(connection, pStatement, resultSet);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

//		if (!products.isEmpty()) {
//			return products;
//		}
//		return null;

		return list;
	}

	@Override
	public double buyProduct(String id) {
		try {
			connection = ConncetionUtilities.getConnection();
			List<Product> viewAllProducts = viewAllProducts();
			if (!viewAllProducts.isEmpty()) {
				for (Product pro : viewAllProducts) {
					if (pro.getProductId().equals(id)) {
						System.out.println("Avilable qty of " + pro.getProductName() + " is : " + pro.getQty());
						System.out.println("Now enter qty..!");
						int qty = scanner.nextInt();
						if (qty <= pro.getQty()) {
							double amt = qty * pro.getPrice();
//							pro.setQty(pro.getQty() - qty);
							System.out.println("Net Amount : " + pro.getProductName() + " : " + qty + " x "
									+ pro.getPrice() + " ₹" + " = " + amt + " ₹");
							return amt;
						}
					}
				}
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				ConncetionUtilities.closeConnection(connection, pStatement);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
//		if (!products.isEmpty()) {
//			for (Product pro : products) {
//				if (pro.getProductId().equals(id)) {
//					System.out.println("Avilable qty of " + pro.getProductName() + " is : " + pro.getQty());
//					System.out.println("Now enter qty..!");
//					int qty = scanner.nextInt();
//					if (qty <= pro.getQty()) {
//						double amt = qty * pro.getPrice();
//						pro.setQty(pro.getQty() - qty);
//						System.out.println("Net Amount : " + pro.getProductName() + " : " + qty + " x " + pro.getPrice()
//								+ " ₹" + " = " + amt + " ₹");
//						return amt;
//					}
//				}
//			}
//		}
		return 0;

	}

}
