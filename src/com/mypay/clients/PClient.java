package com.mypay.clients;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.mypay.banking.dto.Customer;
import com.mypay.conetionutilities.ConncetionUtilities;
import com.mypay.document.DocumentGen;
import com.mypay.emil.MyEmail;
import com.mypay.product.ProductException.ProductNotFoudException;
import com.mypay.product.ProductException.ProductSourceNotFound;
import com.mypay.product.dao.ProductDao;
import com.mypay.product.daoimpl.ProductDaoImpl;
import com.mypay.product.dto.Product;
import com.mypay.queries.BankingQueries;
import com.mypay.queries.ProductQueries;
import com.mypay.banking.bankexception.CusomerExceptions;
import com.mypay.banking.bankexception.InvalidCredentials;
import com.mypay.banking.bankexception.PaymentFailed;
import com.mypay.banking.dao.BankingDao;
import com.mypay.banking.daoimpl.BankingDaoImpl;

public class PClient {
	static ProductDao pDao = new ProductDaoImpl();
	static Scanner scanner = new Scanner(System.in);
	static BankingDao bDao = new BankingDaoImpl();

	static Connection connection = null;
	static ResultSet resultSet = null;
	static PreparedStatement pStatement = null;

//	----------------------- Admin Product ---------------------

	public static void adminProduct() throws ProductNotFoudException, ProductSourceNotFound {
		while (true) {
			System.out.println("*****Admin Product*****");
			System.out.println("    1)Add product.     ");
			System.out.println("    2)Delete product.  ");
			System.out.println("    3)View product.	   ");
			System.out.println("    4)Update product.  ");
			System.out.println("    5)View all products");
			System.out.println("    6)Generate pdf     ");
			System.out.println("    7)Back.            ");
			System.out.println("************************");
			switch (scanner.nextInt()) {
			case 1: {

				pDao.addProduct();

				break;
			}
			case 2: {
				System.out.println("Enter product id");
				boolean deleteProduct = pDao.deleteProduct(scanner.next());
				if (deleteProduct) {
					System.out.println("Product deleted..!");
				} else {
					System.out.println("Enter valid product Id");
				}
				/*
				 * List<Product> viewAllProducts = pDao.viewAllProducts(); if
				 * (!viewAllProducts.isEmpty()) { boolean dProduct =
				 * pDao.deleteProduct(scanner.next()); if (dProduct) {
				 * System.out.println("Successfully deleted"); } else { throw new
				 * ProductNotFoudException("Product not found..!"); } } else { throw new
				 * ProductSourceNotFound("Source not found...!"); }
				 */
				break;
			}
			case 3: {
				System.out.println("Enter product id");
				Product viewProduct = pDao.viewProduct(scanner.next());
				if (viewProduct != null) {
					System.out.format("%-15s%-15s%-15s%-15s\n", "Id ", "Name ", "Qty", "Price");
					System.out.format("%-15s%-15s%-15s%-15s\n", "-----------", "---------", "------------",
							"------------");
					System.out.format("%-15s%-15s%-15s%-15s\n", viewProduct.getProductId(),
							viewProduct.getProductName(), viewProduct.getQty(), viewProduct.getPrice() + " ₹");
					System.out.println();
				} else {
					System.out.println("Enter valid productId");
				}
				break;
			}
			case 4: {
				System.out.println("Enter product id");
				boolean updateProduct = pDao.updateProduct(scanner.next());
				if (updateProduct) {
					System.out.println("Product updated successfully..!");
				} else {
					System.err.println("Updation failed..!");
				}
				break;
			}
			case 5: {
				List<Product> viewAllProducts = pDao.viewAllProducts();
				if (!viewAllProducts.isEmpty()) {
					System.err.println("------------<<<<<< Welcome to MyCart >>>>>>------------");
					System.out.format("%-15s%-15s%-15s%-15s\n", "Id ", "Name ", "Qty", "Price");
					System.out.format("%-15s%-15s%-15s%-15s\n", "-----------", "---------", "------------",
							"------------");
					for (Product pro : viewAllProducts) {
						System.out.format("%-15s%-15s%-15s%-15s\n", pro.getProductId(), pro.getProductName(),
								pro.getQty(), pro.getPrice() + " ₹");
					}
					System.out.println();
				} else {
//					throw new ProductSourceNotFound("Source not found...!");
					System.out.println("Source not found..!");
				}
				break;
			}
			case 6: {

				List<Product> viewAllProducts = pDao.viewAllProducts();
				DocumentGen.productDocument(viewAllProducts, "src/com/mypay/clients/products.pdf");
				break;
			}
			case 7: {
				try {
					Client.adminOpps();
				} catch (Exception e) {
					System.out.println(e);
				}
				break;
			}
			}
		}
	}

//	----------------------- Customer Product ---------------------

	public static void custProduct() throws CusomerExceptions, InvalidCredentials, ProductNotFoudException,
			ProductSourceNotFound, PaymentFailed {
		while (true) {
			System.out.println("*********Cust Product*********");
			System.out.println("	1) View product. 		  ");
			System.out.println("	2) Buy Product. 		  ");
			System.out.println("	3) View all products.	  ");
			System.out.println("	4) Back.                  ");
			System.out.println("******************************");
			switch (scanner.nextInt()) {
			case 1: {
				System.out.println("Enter product id");
				Product viewProduct = pDao.viewProduct(scanner.next());
				if (viewProduct != null) {
					System.out.format("%-15s%-15s%-15s%-15s\n", "Id ", "Name ", "Qty", "Price");
					System.out.format("%-15s%-15s%-15s%-15s\n", "-----------", "---------", "------------",
							"------------");
					System.out.format("%-15s%-15s%-15s%-15s\n", viewProduct.getProductId(),
							viewProduct.getProductName(), viewProduct.getQty(), viewProduct.getPrice() + " ₹");
					System.out.println();
				}
				break;
			}
			case 2: {
				List<Product> products = PClient.pDao.viewAllProducts();
				if (!products.isEmpty()) {
					System.out.println("Enter product Id");
					String id = scanner.next();
					Product viewProduct = pDao.viewProduct(id);

					if (viewProduct != null) {
						double buyProduct = PClient.pDao.buyProduct(id);
						System.out.println("press y to debit amount from your account");
						String input = scanner.next();

						if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
							Customer customer = BClient.bDao.viewMyDetails();
							
							if (BankingDaoImpl.uName != null && BankingDaoImpl.pass != null) {
								if (buyProduct <= customer.getBalence() && buyProduct > 0) {
									try {
										connection = ConncetionUtilities.getConnection();
										pStatement = connection.prepareStatement(BankingQueries.WITHDRAW_AMOUNT);
										pStatement.setDouble(1, buyProduct);
										pStatement.setString(2, BankingDaoImpl.uName);
										pStatement.setString(3, BankingDaoImpl.pass);
										int executeUpdate = pStatement.executeUpdate();

										if (executeUpdate != 0) {

											int qty = (int) (buyProduct / viewProduct.getPrice());
											pStatement = connection.prepareStatement(ProductQueries.UPDATE_QTY);
											pStatement.setDouble(1, qty);
											pStatement.setString(2, id);
											int executeUpdate2 = pStatement.executeUpdate();
											
											if (executeUpdate2 != 0) {
												System.out.println("Purchaged successfull..!");
											}
//											viewProduct.setQty(viewProduct.getQty() - qty);

											String cust = "Name \t\t:  "
													+ customer.getFirstName().concat(" " + customer.getLastName())
													+ "\nAddress \t\t:  " + customer.getAddress() + "\nPhone \t\t:  "
													+ customer.getPhone() + "\nEmail \t\t:  " + customer.getEmail();
											String reciept = "Product id \t\t: " + viewProduct.getProductId()
													+ "\nProduct Name \t\t: " + viewProduct.getProductName() + " --> " + qty
													+ "No's  x  " + viewProduct.getPrice() + "  =  " + buyProduct
													+ "\nNet Total : " + buyProduct;

											String subject = "MyCart..!";

//											PDF generation
											String fileName = "src/com/mypay/product/documents/"
													+ customer.getFirstName() + ".pdf";
											DocumentGen.productDocument(fileName, cust, reciept);
											MyEmail.main(fileName, customer.getEmail(), subject);
											System.out.println("Reciept generated and sent to email..!");
										}
									} catch (ClassNotFoundException | SQLException e) {
										e.printStackTrace();
									}
//									customer.setBalence(customer.getBalence() - buyProduct);

								} else {
									throw new CusomerExceptions("Have no suffient balence to process");
								}
							} else {
								throw new InvalidCredentials("Invalid credentials");
							}
						} else {
							throw new PaymentFailed("Payment Failed..!");
						}
					} else {
						throw new ProductNotFoudException("Product not Found..!");
					}
				} else {
					throw new ProductSourceNotFound("Source not found...!");
				}
				break;
			}

			case 3: {
				List<Product> viewAllProducts = pDao.viewAllProducts();
				if (viewAllProducts != null) {
					System.err.println("------------<<<<<< Welcome to MyCart >>>>>>------------");
					System.out.format("%-15s%-15s%-15s%-15s\n", "Id ", "Name ", "Qty", "Price");
					System.out.format("%-15s%-15s%-15s%-15s\n", "-----------", "---------", "------------",
							"------------");
					for (Product pro : viewAllProducts) {
						System.out.format("%-15s%-15s%-15s%-15s\n", pro.getProductId(), pro.getProductName(),
								pro.getQty(), pro.getPrice() + " ₹");
					}
					System.out.println();
				} else {
					throw new ProductSourceNotFound("Source not found...!");
				}
				break;
			}
			case 4: {
				Client.custOpps();
				break;
			}
			default: {
				System.err.println("Enter valid input..!");
			}
			}

		}
	}

}
