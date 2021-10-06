package demo.HotelBooking.exporter;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import demo.HotelBooking.entity.*;
import demo.HotelBooking.helper.IServiceCustom;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class CheckOutExporter {
    private Map<Object, Object> map;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private int totalService = 0;
    private double subtotal = 0;
    private double totalDiscount = 0;
    private double totalPayment = 0;

    public CheckOutExporter(Map<Object,Object> map) {
        this.map = map;
    }

    public void writeHeadingFile(Document document) {
        Paragraph hotelTitle = new Paragraph("Hotel Booking Online", FontFactory.getFont(FontFactory.HELVETICA_BOLD));
        hotelTitle.setSpacingAfter(5);
        document.add(hotelTitle);

        LocalDate localDate = LocalDate.now();

        Paragraph today = new Paragraph("Date: " + formatter.format(localDate));
        document.add(today);

        Paragraph heading = new Paragraph(new Chunk("HOTEL CHECK OUT DETAIL INFORMATION",FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
        heading.setAlignment(Element.ALIGN_CENTER);
        heading.setSpacingBefore(10);
        heading.setSpacingAfter(5);
        document.add(heading);
    }

    public void writeCustomerInformation(Document document) {
        BookingCode bookingCode = (BookingCode) map.get("bookingCode");
        String promoType = (String) map.get("promoType");
        String promoCode = "Not Have";

        if (promoType != null) {
            if (promoType.equals("Room Discount")) {
                RoomPromotionCode roomPromoCode = (RoomPromotionCode) map.get("promoCode");
                promoCode = roomPromoCode.getCode();
            } else if (promoType.equals("Service Discount")) {
                ServicePromotionCode servicePromoCode = (ServicePromotionCode) map.get("promoCode");
                promoCode = servicePromoCode.getCode();
            }
        }


        PdfPTable customerTable = new PdfPTable(4);
        customerTable.setWidthPercentage(100);
        customerTable.setSpacingBefore(15);

        customerTable.getDefaultCell().setBorderColor(Color.WHITE);
        customerTable.getDefaultCell().setPaddingTop(5);
        customerTable.getDefaultCell().setPaddingBottom(5);
        customerTable.setWidths(new float[]{2.3f, 2.7f, 2.3f, 2.7f});

        customerTable.addCell("Customer:");
        customerTable.addCell(bookingCode.getCustomer().getName());
        customerTable.addCell("Booking Code:");
        customerTable.addCell(bookingCode.getBookingCode());
        customerTable.addCell("Book Date:");
        customerTable.addCell(formatter.format(bookingCode.getBookingDate().toLocalDate()));
        customerTable.addCell("Book From:");
        customerTable.addCell(formatter.format(bookingCode.getDateFrom().toLocalDate()));
        customerTable.addCell("Total Room:");
        customerTable.addCell(String.valueOf(bookingCode.getCheckInDetailList().size()));
        customerTable.addCell("Book To:");
        customerTable.addCell(formatter.format(bookingCode.getDateTo().toLocalDate()));
        customerTable.addCell("Payment Advance:");
        customerTable.addCell(String.format("%,.2f",bookingCode.getCustomer().getAdvancePayment()));
        customerTable.addCell("Check in:");
        customerTable.addCell(formatter.format(bookingCode.getCheckInDate().toLocalDate()));
        customerTable.addCell("Promo Code:");
        customerTable.addCell(promoCode);
        customerTable.addCell("Check out:");
        customerTable.addCell(formatter.format(LocalDate.now()));

        document.add(customerTable);
    }

    public void writeServiceInPerRoomTable(Document document) {
        BookingCode bookingCode = (BookingCode) map.get("bookingCode");
        List<CheckInDetail> checkInDetailList = (List<CheckInDetail>) map.get("checkInDetailList");
        List<ServicePerRoom> servicePerRoomList = bookingCode.getServicePerRoomList();

        Paragraph serviceListHeading= new Paragraph("SERVICE DETAIL LIST IN PER ROOM",FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
        serviceListHeading.setSpacingBefore(15);
        document.add(serviceListHeading);

        PdfPTable serviceTable = new PdfPTable(3);
        serviceTable.setSpacingBefore(10);
        serviceTable.setWidthPercentage(100);
        serviceTable.setWidths(new float[]{1f, 3f, 2f});

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new Color(88, 138, 206));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
//        font.setColor(Color.BLACK);

        cell.setPhrase(new Phrase("Room",font));
        serviceTable.addCell(cell);

        cell.setPhrase(new Phrase("Service Name",font));
        serviceTable.addCell(cell);

        cell.setPhrase(new Phrase("Registry Date", font));
        serviceTable.addCell(cell);

        PdfPCell cellData = new PdfPCell();
        cellData.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellData.setPaddingTop(2.5f);
        cellData.setPaddingBottom(2.5f);
        // Write Data
        for (CheckInDetail cid : checkInDetailList) {
            String roomNameCID = cid.getRoom().getName();
            for (ServicePerRoom spr : servicePerRoomList) {
                String roomNameSPR = spr.getRoom().getName();
                if (roomNameCID.equals(roomNameSPR)) {
                    cellData.setPhrase(new Phrase(spr.getRoom().getName()));
                    serviceTable.addCell(cellData);

                    cellData.setPhrase(new Phrase(upperCaseFirstLetter(spr.getServiceEntity().getName())));
                    serviceTable.addCell(cellData);

                    cellData.setPhrase(new Phrase(formatter.format(spr.getRegistryDate().toLocalDate())));
                    serviceTable.addCell(cellData);
                }
            }
        }

        document.add(serviceTable);
    }

    public void writeServicePaymentTable(Document document) {
        Paragraph servicePayment = new Paragraph("ALL SERVICE PAYMENT SUMMARY",FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
        servicePayment.setSpacingBefore(15);
        document.add(servicePayment);

        PdfPTable serviceListTable = new PdfPTable(6);
        serviceListTable.setSpacingBefore(10);
        serviceListTable.setWidthPercentage(100);

        PdfPCell cell1 = new PdfPCell();
        cell1.setBackgroundColor(new Color(88, 138, 206));
        cell1.setPaddingTop(5);
        cell1.setPaddingBottom(5);
        cell1.setVerticalAlignment(Element.ALIGN_CENTER);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);

        Font font2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

        cell1.setPhrase(new Phrase("Service Name", font2));
        serviceListTable.addCell(cell1);

        cell1.setPhrase(new Phrase("Total Quantity",font2));
        serviceListTable.addCell(cell1);

        cell1.setPhrase(new Phrase("Unit Price (VND)", font2));
        serviceListTable.addCell(cell1);

        cell1.setPhrase(new Phrase("Total Price (VND)", font2));
        serviceListTable.addCell(cell1);

        cell1.setPhrase(new Phrase("Discount (%)", font2));
        serviceListTable.addCell(cell1);

        cell1.setPhrase(new Phrase("After Discount (VND)", font2));
        serviceListTable.addCell(cell1);

        //Write Data
        List<IServiceCustom> allTotalService = (List<IServiceCustom>) map.get("allTotalService");
        for (IServiceCustom serviceCustom : allTotalService) {
            int totalQuantity = Integer.parseInt(serviceCustom.getTotalQuantity());
            double unitPrice = Double.parseDouble(serviceCustom.getPrice());
            double totalPrice = unitPrice * totalQuantity;
            double discountPercent = Double.parseDouble(serviceCustom.getDiscount());
            double discountAmount = totalPrice * discountPercent / 100;
            double afterDiscount = totalPrice - discountAmount;

            this.totalService +=totalQuantity;
            this.subtotal += totalPrice;
            this.totalDiscount += discountAmount;

            serviceListTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);


            serviceListTable.addCell(upperCaseFirstLetter(serviceCustom.getServiceName()));
            serviceListTable.addCell(serviceCustom.getTotalQuantity());
            serviceListTable.addCell(String.format("%,.0f", unitPrice));
            serviceListTable.addCell(String.format("%,.0f", totalPrice));
            serviceListTable.addCell(String.format("%,.2f", discountPercent));
            serviceListTable.addCell(String.format("%,.0f", afterDiscount));
        }
        this.totalPayment = this.subtotal - this.totalDiscount;

        document.add(serviceListTable);
    }

    public void writeSumPaymentTable(Document document) {
        PdfPTable paymentTable = new PdfPTable(2);
        paymentTable.setSpacingBefore(30);
        paymentTable.setWidthPercentage(50);
        paymentTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        paymentTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        paymentTable.getDefaultCell().setPaddingTop(5);
        paymentTable.getDefaultCell().setPaddingBottom(5);
        paymentTable.getDefaultCell().setBorderColor(Color.WHITE);

        PdfPCell cell1 = new PdfPCell();
        cell1.setPaddingTop(5);
        cell1.setPaddingBottom(5);
        cell1.setBorderColor(Color.WHITE);

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        PdfPCell cell2 = new PdfPCell();
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setPaddingTop(5);
        cell2.setPaddingBottom(5);
        cell2.setBorderColor(Color.WHITE);

        cell1.setPhrase(new Phrase("Total Service:", font));
        paymentTable.addCell(cell1);

        cell2.setPhrase(new Phrase(String.valueOf(this.totalService)));
        paymentTable.addCell(cell2);

        cell1.setPhrase(new Phrase("Subtotal (VND):", font));
        paymentTable.addCell(cell1);

        cell2.setPhrase(new Phrase(String.format("%,.2f", this.subtotal)));
        paymentTable.addCell(cell2);

        cell1.setPhrase(new Phrase("Total Discount (VND):", font));
        paymentTable.addCell(cell1);

        cell2.setPhrase(new Phrase(String.format("%,.2f", this.totalDiscount)));
        paymentTable.addCell(cell2);

        cell1.setPhrase(new Phrase("Total Payment (VND):", font));
        paymentTable.addCell(cell1);

        cell2.setPhrase(new Phrase(String.format("%,.2f", this.totalPayment)));
        paymentTable.addCell(cell2);

        document.add(paymentTable);
    }

    public void writeCustomerSignature(Document document) {
        Paragraph customerSignature = new Paragraph("Confirmation of customer",FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
        customerSignature.setAlignment(Element.ALIGN_RIGHT);
        customerSignature.setIndentationRight(50);
        customerSignature.setSpacingBefore(30);
        document.add(customerSignature);
    }

    public String upperCaseFirstLetter (String string) {
        return string.substring(0,1).toUpperCase() + string.substring(1);
    }

    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        //open
        document.open();

        writeHeadingFile(document);
        writeCustomerInformation(document);
        writeServiceInPerRoomTable(document);
        writeServicePaymentTable(document);
        writeSumPaymentTable(document);
        writeCustomerSignature(document);

        document.close();
    }
}
