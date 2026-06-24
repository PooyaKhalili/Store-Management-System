package com.storesystem.config;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.Bidi;
import com.storesystem.model.Customer;
import com.storesystem.model.Order;
import com.storesystem.model.OrderItem;
import com.storesystem.service.CustomerService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.awt.Desktop;
import java.io.File;
import java.text.DecimalFormat;

public class InvoicePdfService {

    private final CustomerService customerService;
    private final DecimalFormat priceFormatter = new DecimalFormat("###,###,###");

    private static final String FONT_PATH = "fonts/vazir/Vazirmatn-Regular.ttf";

    private static final float PAGE_MARGIN = 50;
    private static final float START_Y = 790;
    private static final float MIN_Y = 90;

    public InvoicePdfService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public File generateInvoice(Order order) throws Exception {
        if (order == null) {
            throw new RuntimeException("اطلاعات سفارش نامعتبر است.");
        }

        File invoiceFolder = new File("invoice");

        if (!invoiceFolder.exists()) {
            boolean created = invoiceFolder.mkdirs();

            if (!created) {
                throw new RuntimeException("امکان ساخت پوشه invoice وجود ندارد.");
            }
        }

        File pdfFile = new File(invoiceFolder, "invoice_" + order.getOrderId() + ".pdf");
        File fontFile = new File(FONT_PATH);

        if (!fontFile.exists()) {
            throw new RuntimeException("فایل فونت پیدا نشد: " + fontFile.getAbsolutePath());
        }

        Customer customer = customerService.getCustomerById((int) order.getCustomerId());

        String customerName = customer != null
                ? safe(customer.getFirstName()) + " " + safe(customer.getLastName())
                : "مشتری عمومی";

        try (PDDocument document = new PDDocument()) {
            PDType0Font font = PDType0Font.load(document, fontFile);

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream content = new PDPageContentStream(document, page);

            try {
                float pageWidth = page.getMediaBox().getWidth();
                float y = START_Y;

                drawRightText(content, font, "فاکتور فروشگاه", pageWidth - PAGE_MARGIN, y, 22);
                y -= 35;

                drawLine(content, PAGE_MARGIN, y, pageWidth - PAGE_MARGIN, y);
                y -= 35;

                drawRightText(content, font, "نام مشتری: " + customerName, pageWidth - PAGE_MARGIN, y, 13);
                y -= 25;

                drawRightText(content, font, "شماره فاکتور: " + englishNumber(order.getOrderId()), pageWidth - PAGE_MARGIN, y, 13);
                y -= 25;

                String orderDate = order.getOrderDate() == null ? "---" : String.valueOf(order.getOrderDate());
                drawRightText(content, font, "تاریخ ثبت: " + englishNumber(orderDate), pageWidth - PAGE_MARGIN, y, 13);
                y -= 35;

                float tableRight = pageWidth - PAGE_MARGIN;
                float tableLeft = PAGE_MARGIN;
                float rowHeight = 28;

                float col1 = 55;
                float col2 = 180;
                float col3 = 105;
                float col4 = 65;
                float col5 = 105;

                drawTableHeader(
                        content,
                        font,
                        tableLeft,
                        tableRight,
                        y,
                        rowHeight,
                        col1,
                        col2,
                        col3,
                        col4,
                        col5
                );

                y -= rowHeight;

                int index = 1;

                if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                    for (OrderItem item : order.getOrderItems()) {

                        if (y < MIN_Y) {
                            content.close();

                            page = new PDPage(PDRectangle.A4);
                            document.addPage(page);

                            content = new PDPageContentStream(document, page);
                            pageWidth = page.getMediaBox().getWidth();

                            tableRight = pageWidth - PAGE_MARGIN;
                            tableLeft = PAGE_MARGIN;
                            y = START_Y;

                            drawTableHeader(
                                    content,
                                    font,
                                    tableLeft,
                                    tableRight,
                                    y,
                                    rowHeight,
                                    col1,
                                    col2,
                                    col3,
                                    col4,
                                    col5
                            );

                            y -= rowHeight;
                        }

                        String productName = item.getProductName() == null ? "نامشخص" : item.getProductName();
                        double unitPrice = item.getProductUnitPrice();
                        int quantity = item.getProductQuantity();
                        double rowTotal = unitPrice * quantity;

                        drawTableRow(
                                content,
                                font,
                                tableLeft,
                                tableRight,
                                y,
                                rowHeight,
                                col1,
                                col2,
                                col3,
                                col4,
                                col5,
                                englishNumber(index),
                                productName,
                                englishNumber(priceFormatter.format(unitPrice)),
                                englishNumber(quantity),
                                englishNumber(priceFormatter.format(rowTotal))
                        );

                        y -= rowHeight;
                        index++;
                    }
                } else {
                    drawRightText(content, font, "هیچ کالایی برای این فاکتور ثبت نشده است.", tableRight, y - 18, 12);
                    y -= rowHeight;
                }

                if (y < 160) {
                    content.close();

                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);

                    content = new PDPageContentStream(document, page);
                    pageWidth = page.getMediaBox().getWidth();
                    y = START_Y;
                }

                y -= 35;

                String totalAmount = englishNumber(priceFormatter.format(order.getTotalAmount()));
                drawRightText(content, font, "مبلغ قابل پرداخت: " + totalAmount + " ریال", pageWidth - PAGE_MARGIN, y, 15);

                y -= 45;

                drawLine(content, PAGE_MARGIN, y, pageWidth - PAGE_MARGIN, y);
                y -= 25;

                drawCenteredText(content, font, "از خرید شما سپاسگزاریم.", pageWidth / 2, y, 12);

            } finally {
                content.close();
            }

            document.save(pdfFile);
        }

        return pdfFile;
    }

    private void drawTableHeader(
            PDPageContentStream content,
            PDType0Font font,
            float tableLeft,
            float tableRight,
            float y,
            float rowHeight,
            float col1,
            float col2,
            float col3,
            float col4,
            float col5
    ) throws Exception {
        drawTableBorders(content, tableLeft, tableRight, y, rowHeight, col1, col2, col3, col4, col5);

        float x = tableRight;

        drawCellText(content, font, "ردیف", x - col1 / 2, y - 18, 11);
        x -= col1;

        drawCellText(content, font, "نام محصول", x - col2 / 2, y - 18, 11);
        x -= col2;

        drawCellText(content, font, "قیمت واحد", x - col3 / 2, y - 18, 11);
        x -= col3;

        drawCellText(content, font, "تعداد", x - col4 / 2, y - 18, 11);
        x -= col4;

        drawCellText(content, font, "جمع کل", x - col5 / 2, y - 18, 11);
    }

    private void drawTableRow(
            PDPageContentStream content,
            PDType0Font font,
            float tableLeft,
            float tableRight,
            float y,
            float rowHeight,
            float col1,
            float col2,
            float col3,
            float col4,
            float col5,
            String index,
            String productName,
            String unitPrice,
            String quantity,
            String rowTotal
    ) throws Exception {
        drawTableBorders(content, tableLeft, tableRight, y, rowHeight, col1, col2, col3, col4, col5);

        float x = tableRight;

        drawCellText(content, font, index, x - col1 / 2, y - 18, 10);
        x -= col1;

        drawCellText(content, font, productName, x - col2 / 2, y - 18, 10);
        x -= col2;

        drawCellText(content, font, unitPrice, x - col3 / 2, y - 18, 10);
        x -= col3;

        drawCellText(content, font, quantity, x - col4 / 2, y - 18, 10);
        x -= col4;

        drawCellText(content, font, rowTotal, x - col5 / 2, y - 18, 10);
    }

    private void drawTableBorders(
            PDPageContentStream content,
            float tableLeft,
            float tableRight,
            float y,
            float rowHeight,
            float col1,
            float col2,
            float col3,
            float col4,
            float col5
    ) throws Exception {
        float bottomY = y - rowHeight;

        drawLine(content, tableLeft, y, tableRight, y);
        drawLine(content, tableLeft, bottomY, tableRight, bottomY);

        float x = tableRight;

        drawLine(content, x, y, x, bottomY);
        x -= col1;

        drawLine(content, x, y, x, bottomY);
        x -= col2;

        drawLine(content, x, y, x, bottomY);
        x -= col3;

        drawLine(content, x, y, x, bottomY);
        x -= col4;

        drawLine(content, x, y, x, bottomY);
        x -= col5;

        drawLine(content, tableLeft, y, tableLeft, bottomY);
    }

    private void drawRightText(
            PDPageContentStream content,
            PDType0Font font,
            String text,
            float rightX,
            float y,
            float fontSize
    ) throws Exception {
        String shaped = shapePersian(text);
        float textWidth = font.getStringWidth(shaped) / 1000 * fontSize;

        content.beginText();
        content.setFont(font, fontSize);
        content.newLineAtOffset(rightX - textWidth, y);
        content.showText(shaped);
        content.endText();
    }

    private void drawCenteredText(
            PDPageContentStream content,
            PDType0Font font,
            String text,
            float centerX,
            float y,
            float fontSize
    ) throws Exception {
        String shaped = shapePersian(text);
        float textWidth = font.getStringWidth(shaped) / 1000 * fontSize;

        content.beginText();
        content.setFont(font, fontSize);
        content.newLineAtOffset(centerX - textWidth / 2, y);
        content.showText(shaped);
        content.endText();
    }

    private void drawCellText(
            PDPageContentStream content,
            PDType0Font font,
            String text,
            float centerX,
            float y,
            float fontSize
    ) throws Exception {
        drawCenteredText(content, font, text, centerX, y, fontSize);
    }

    private void drawLine(
            PDPageContentStream content,
            float x1,
            float y1,
            float x2,
            float y2
    ) throws Exception {
        content.moveTo(x1, y1);
        content.lineTo(x2, y2);
        content.stroke();
    }

    private String shapePersian(String text) {
        if (text == null || text.isBlank()) {
            return "";
        }

        text = englishNumber(text);

        if (!containsPersianOrArabic(text)) {
            return text;
        }

        try {
            ArabicShaping shaping = new ArabicShaping(
                    ArabicShaping.LETTERS_SHAPE | ArabicShaping.TEXT_DIRECTION_LOGICAL
            );

            String shaped = shaping.shape(text);

            Bidi bidi = new Bidi();
            bidi.setPara(shaped, Bidi.LEVEL_DEFAULT_RTL, null);

            return bidi.writeReordered(Bidi.DO_MIRRORING | Bidi.REMOVE_BIDI_CONTROLS);

        } catch (Exception e) {
            return text;
        }
    }

    private boolean containsPersianOrArabic(String text) {
        return text.matches(".*[\\u0600-\\u06FF].*");
    }

    private String safe(String input) {
        return input == null ? "" : input.trim();
    }

    private String englishNumber(Object value) {
        if (value == null) {
            return "";
        }

        return String.valueOf(value)
                .replace("۰", "0")
                .replace("۱", "1")
                .replace("۲", "2")
                .replace("۳", "3")
                .replace("۴", "4")
                .replace("۵", "5")
                .replace("۶", "6")
                .replace("۷", "7")
                .replace("۸", "8")
                .replace("۹", "9")
                .replace("٠", "0")
                .replace("١", "1")
                .replace("٢", "2")
                .replace("٣", "3")
                .replace("٤", "4")
                .replace("٥", "5")
                .replace("٦", "6")
                .replace("٧", "7")
                .replace("٨", "8")
                .replace("٩", "9");
    }

    public void openPdf(File file) throws Exception {
        if (file == null || !file.exists()) {
            throw new RuntimeException("فایل PDF پیدا نشد.");
        }

        if (!Desktop.isDesktopSupported()) {
            throw new RuntimeException("باز کردن فایل PDF در این سیستم پشتیبانی نمی‌شود.");
        }

        Desktop.getDesktop().open(file);
    }
}
