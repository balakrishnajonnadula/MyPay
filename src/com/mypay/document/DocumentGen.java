package com.mypay.document;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.mypay.product.dto.Product;

public class DocumentGen {

//	Purchasing product
	public static void productDocument(String print, String cust, String reciept) {
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(print));
			document.open();
			document.add(new Paragraph("************ MyCart Purchage Reciept *********"));
			document.add(new Paragraph(cust));
			document.add(new Paragraph(reciept));
			document.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}

//	view all products..!
	public static void productDocument(List<Product> viewAllProducts, String path) {
		try {
			Document document = new Document();
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(path));
			document.open();
			document.add(new Paragraph("******************** Products in MyCart ********************"));
			document.add(new Paragraph("id                 name               qty               price "));

			for (Product pro : viewAllProducts) {
				document.add(new Paragraph(pro.getProductId() + "           " + pro.getProductName() + "          "
						+ pro.getQty() + "             " + pro.getPrice()));
			}

			document.close();
			System.out.println("Successfully generated pdf..!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}

//	Customer Registration Details
	public static void custReg(String custDetails, String path) {
		try {
			Document document = new Document();
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(path));
			document.open();
			document.add(new Paragraph("************ MyBanking *********"));
			document.add(new Paragraph(custDetails));
			document.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

//	Metro Ticket
	public static void metroTicket(String cust, String stations, String fare, String path) {
		try {
			Document document = new Document();
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(path));
			document.open();
			document.add(new Paragraph("************ MyMetro *********"));
			document.add(new Paragraph(cust));
			document.add(new Paragraph(stations));
			document.add(new Paragraph(fare));
			document.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

}
