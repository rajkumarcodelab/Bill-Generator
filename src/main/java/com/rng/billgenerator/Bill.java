package com.rng.billgenerator;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Bill {
	private String customerName;
	private LocalDate dateOfBill;
	private List<Item> items = new ArrayList<>();
	private double subTotalAmount;
	private double taxAmount;
	private double totalAmount;

	public Bill(String customerName, LocalDate dateOfBill, List<Item> items, double subTotalAmount, double taxAmount,
			double totalAmount) {
		super();
		this.customerName = customerName;
		this.dateOfBill = dateOfBill;
		this.items = items;
		this.subTotalAmount = subTotalAmount;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public LocalDate getDateOfBill() {
		return dateOfBill;
	}

	public void setDateOfBill(LocalDate dateOfBill) {
		this.dateOfBill = dateOfBill;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public double getSubTotalAmount() {
		return subTotalAmount;
	}

	public void setSubTotalAmount(double subTotalAmount) {
		this.subTotalAmount = subTotalAmount;
	}

	public double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

}
