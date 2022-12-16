package com.mypay.queries;

public interface ProductQueries {
	public String ADD_PRODUCT = "insert into product values(?,?,?,?,?);";

	public String DELETE_PRODUCT = "delete FROM product where pro_id = ?";

	public String VIEW_PRODUCT = "select * from product where pro_id = ?;";

	public String UPDATE_PRODUCT = "update product set pro_name = ?, pro_qty = ?, pro_price = ? where pro_id = ?;";

	public String VIEWALL_PRODUCTS = "SELECT * FROM product";

	public String UPDATE_QTY = "update product set pro_qty = (pro_qty - ?) where pro_id = ?;";

}
