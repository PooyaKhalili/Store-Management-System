package com.storesystem.config;

import com.storesystem.model.Customer;
import com.storesystem.model.Order;
import com.storesystem.model.OrderItem;
import com.storesystem.service.CustomerService;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

public class InvoiceTxtService {

    private final CustomerService customerService;
    private final DecimalFormat priceFormatter = new DecimalFormat("###,###,###");

    public InvoiceTxtService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public File generateInvoice(Order order) throws Exception {

        if (order == null) {
            throw new RuntimeException("اطلاعات سفارش نامعتبر است.");
        }

        File folder = new File("invoice");

        if (!folder.exists()) {
            boolean created = folder.mkdirs();

            if (!created) {
                throw new RuntimeException("امکان ساخت پوشه invoice وجود ندارد.");
            }
        }

        File txtFile = new File(folder, "invoice_" + order.getOrderId() + ".txt");

        Customer customer = customerService.getCustomerById((int) order.getCustomerId());

        String customerName = customer != null
                ? safe(customer.getFirstName()) + " " + safe(customer.getLastName())
                : "مشتری عمومی";

        try (FileWriter writer = new FileWriter(txtFile, StandardCharsets.UTF_8)) {

            writer.write("==========================================================================\n");
            writer.write("                              فاکتور فروشگاه                              \n");
            writer.write("==========================================================================\n\n");

            writer.write("شماره فاکتور : " + order.getOrderId() + "\n");
            writer.write("نام مشتری    : " + customerName + "\n");
            writer.write("تاریخ ثبت    : " + safe(order.getOrderDate()) + "\n\n");

            writer.write("--------------------------------------------------------------------------\n");
            writer.write(String.format(
                    "%-6s %-25s %-10s %-15s %-15s%n",
                    "ردیف",
                    "نام کالا",
                    "تعداد",
                    "قیمت واحد",
                    "جمع کل"
            ));
            writer.write("--------------------------------------------------------------------------\n");

            int index = 1;

            if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                for (OrderItem item : order.getOrderItems()) {

                    String productName = limitText(safe(item.getProductName()), 25);
                    String quantity = String.valueOf(item.getProductQuantity());
                    String unitPrice = priceFormatter.format(item.getProductUnitPrice());
                    String rowTotal = priceFormatter.format(item.getTotalPrice());

                    writer.write(String.format(
                            "%-6s %-25s %-10s %-15s %-15s%n",
                            index,
                            productName,
                            quantity,
                            unitPrice,
                            rowTotal
                    ));

                    index++;
                }
            } else {
                writer.write("هیچ کالایی برای این فاکتور ثبت نشده است.\n");
            }

            writer.write("--------------------------------------------------------------------------\n");
            writer.write(String.format("%-53s %15s ریال%n", "مبلغ قابل پرداخت:", priceFormatter.format(order.getTotalAmount())));
            writer.write("--------------------------------------------------------------------------\n\n");

            writer.write("                         از خرید شما سپاسگزاریم                          \n");
            writer.write("==========================================================================\n");
        }

        return txtFile;
    }

    private String safe(String input) {
        return input == null ? "" : input.trim();
    }

    private String limitText(String text, int maxLength) {
        if (text == null) {
            return "";
        }

        if (text.length() <= maxLength) {
            return text;
        }

        return text.substring(0, maxLength - 3) + "...";
    }
}
