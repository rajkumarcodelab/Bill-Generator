package com.rng.billgenerator;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfWriter;

public class BillGeneratorUI {

	private static final List<Item> itemlist = new ArrayList<Item>();

	private JFrame frame;
	private JTextField customerNameField;
	private JTextField itemNameField;
	private JTextField quantityField;
	private JTextField weightField;
	private JTextField rateField;
	private JTable billTable;
	private JTextField totalAmountField;
	private JTextField taxAmountField;
	private JTextField finalAmountField;
	private DefaultTableModel tableModel;
	private double taxRate = 0.08; // Example tax rate

	public BillGeneratorUI() {
		initializeComponents();
		layoutComponents();
		frame.setVisible(true);
	}

	private void initializeComponents() {
		frame = new JFrame("Bill Generator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		customerNameField = new JTextField(20);
		itemNameField = new JTextField(15);
		quantityField = new JTextField(5);
		weightField = new JTextField(5);
		rateField = new JTextField(5);

		tableModel = new DefaultTableModel(new String[] { "Item Name", "Quantity", "Weight", "Rate", "Subtotal" }, 0);
		billTable = new JTable(tableModel);

		totalAmountField = new JTextField(10);
		totalAmountField.setEditable(false);
		taxAmountField = new JTextField(10);
		taxAmountField.setEditable(false);
		finalAmountField = new JTextField(10);
		finalAmountField.setEditable(false);
	}

	private void layoutComponents() {
		frame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5); // Padding

		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 0, 0);
		frame.add(new JLabel("Customer Name:"), c);
		c.gridx = 1;
		c.insets = new Insets(5, 5, 0, 5);
		frame.add(customerNameField, c);

		c.gridx = 3;
		c.gridy = 0;
		frame.add(new JLabel("Date:"), c);
		c.gridx = 4;
		frame.add(new JLabel(LocalDate.now().toString()), c);

		c.gridx = 0;
		c.gridy = 1;
		frame.add(new JLabel("Item Name:"), c);
		c.gridx = 1;
		frame.add(itemNameField, c);

		c.gridx = 0;
		c.gridy = 2;
		frame.add(new JLabel("Qty:"), c);
		c.gridx = 1;
		frame.add(quantityField, c);

		c.gridx = 2;
		c.gridy = 2;
		frame.add(new JLabel("Weight:"), c);
		c.gridx = 3;
		frame.add(weightField, c);

		c.gridx = 4;
		c.gridy = 2;
		frame.add(new JLabel("Rate:"), c);
		c.gridx = 5;
		frame.add(rateField, c);

		// JPanel for Buttons

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

		JButton addItemButton = new JButton("Add Item");
		addItemButton.addActionListener(new AddItemListener());
		buttonPanel.add(addItemButton);

		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(e -> resetUI());
		buttonPanel.add(resetButton);

		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 6;
		c.anchor = GridBagConstraints.CENTER;
		frame.add(buttonPanel, c);

		// Bill Display
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 3;
		frame.add(new JScrollPane(billTable), c);

		// Totals
		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 1;
		frame.add(new JLabel("Total:"), c);
		c.gridx = 1;
		frame.add(totalAmountField, c);

		c.gridx = 2;
		c.gridy = 7;
		c.gridwidth = 1;
		frame.add(new JLabel("TAX:"), c);
		c.gridx = 3;
		frame.add(taxAmountField, c);

		c.gridx = 4;
		c.gridy = 7;
		c.gridwidth = 1;
		frame.add(new JLabel("FINAL Total:"), c);
		c.gridx = 5;
		frame.add(finalAmountField, c);

		// Generate Bill Button
		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.CENTER; // Center the button
		JButton generateBillButton = new JButton("Generate Bill");
		generateBillButton.addActionListener(new GenerateBillListener());
		frame.add(generateBillButton, c);

		frame.pack();
	}

	private void resetUI() {
		customerNameField.setText("");
		itemNameField.setText("");
		quantityField.setText("");
		weightField.setText("");
		rateField.setText("");
		tableModel.setRowCount(0);
		totalAmountField.setText("");
		taxAmountField.setText("");
		finalAmountField.setText("");
		itemlist.clear(); // Clear Item List
	}

	class AddItemListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String itemName = itemNameField.getText();
				int quantity = Integer.parseInt(quantityField.getText());
				double weight = Double.parseDouble(weightField.getText());
				double rate = Double.parseDouble(rateField.getText());

				Item item = new Item(itemName, quantity, weight, rate);
				item.calculateSubtotal();

				itemlist.add(item);

				System.out.println(itemlist);

				tableModel.addRow(new Object[] { itemName, quantity, weight, rate, item.getSubtotal() });

				updateTotalAmount();

				// Clear the input fields
				itemNameField.setText("");
				quantityField.setText("");
				weightField.setText("");
				rateField.setText("");
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid numbers.");
			}
		}

		private void updateTotalAmount() {
			double total = 0;
			for (int i = 0; i < tableModel.getRowCount(); i++) {
				total += (double) tableModel.getValueAt(i, 4); // Get subtotal from the table
			}
			totalAmountField.setText(String.valueOf(total));
			Double tax = total * taxRate;
			taxAmountField.setText(String.valueOf(tax));
			finalAmountField.setText(String.valueOf(total + tax));
		}
	}

	class GenerateBillListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String customerName = customerNameField.getText();
			if (customerName.isBlank()) {
				JOptionPane.showMessageDialog(frame, "Please fill in the customer name.");
				return; // Stop execution if validation fails
			}

			// 1. Calculate final amounts
			double total = Double.parseDouble(totalAmountField.getText());
			double tax = total * taxRate;
			double finalAmount = total + tax;

			taxAmountField.setText(String.valueOf(tax));
			finalAmountField.setText(String.valueOf(finalAmount));

			// 2. Generate PDF
			Bill bill = new Bill(customerName, LocalDate.now(), itemlist, total, tax, finalAmount); // ... Populate with
																									// customer data,
			// items

			try {
				generateReceipt(bill, customerName + "_" + LocalDate.now().toString() + "_bill.pdf"); // Example
																										// filename
				JOptionPane.showMessageDialog(frame,
						"Bill generated as " + customerName + "_" + LocalDate.now().toString() + "_bill.pdf");
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Error generating bill: " + ex.getMessage());
			}
		}
	}

	private void generateReceipt(Bill bill, String outputFilename) throws IOException {
		String htmlTemplate = loadTemplate("temp.html");
		htmlTemplate = populateTemplate(htmlTemplate, bill);

		try (PdfWriter writer = new PdfWriter(outputFilename)) {
			HtmlConverter.convertToPdf(htmlTemplate, writer);
		}
	}

	private String loadTemplate(String filename) throws IOException {
		StringBuilder contentBuilder = new StringBuilder();

		try (BufferedReader in = new BufferedReader(
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filename)))) {
			String str;
			while ((str = in.readLine()) != null) {
				contentBuilder.append(str);
			}
		}
		return contentBuilder.toString();
	}

	private String populateTemplate(String template, Bill bill) {
		template = template.replace("$CUSTOMER_NAME", bill.getCustomerName());
		template = template.replace("$DATE",
				bill.getDateOfBill().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
		template = template.replace("$TAX_AMOUNT", String.valueOf(bill.getTaxAmount()));
		template = template.replace("$TOTAL_AMOUNT", String.valueOf(bill.getSubTotalAmount()));
		template = template.replace("$FINAL_AMOUNT", String.valueOf(bill.getTotalAmount()));

		// Generate table rows for items
		StringBuilder itemRows = new StringBuilder();
		System.out.println(bill.getItems());
		for (Item item : bill.getItems()) {
			itemRows.append("<tr>");
			itemRows.append("<td valign='top' style='font-size: 12px;'>").append("  " + item.getItemName() + "  ")
					.append("</td>");
			itemRows.append("<td valign='top' style='font-size: 12px;'>").append("  " + item.getQuantity() + "  ")
					.append("</td>");
			itemRows.append("<td valign='top' style='font-size: 12px;'>").append("  " + item.getTotalWeight() + "  ")
					.append("</td>");
			itemRows.append("<td valign='top' style='font-size: 12px;'>").append("  " + item.getRate() + "  ")
					.append("</td>");
			itemRows.append("<td valign='top' style='font-size: 12px;'>").append("  " + item.getSubtotal() + "  ")
					.append("</td>");
			itemRows.append("</tr>");
		}

		// Replace placeholder for the items table section
		template = template.replace("$ITEMS_TABLE_ROWS", itemRows.toString());

		return template;
	}

	public static void main(String[] args) {
		new BillGeneratorUI();
	}
}
