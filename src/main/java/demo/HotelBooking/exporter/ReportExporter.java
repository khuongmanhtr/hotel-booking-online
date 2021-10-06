package demo.HotelBooking.exporter;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import demo.HotelBooking.entity.BookingCode;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportExporter {
    private List<BookingCode> bookingCodeList;
    private Date dateFrom;
    private Date dateTo;
    private double revenue = 0;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public ReportExporter(List<BookingCode> bookingCodeList, Date dateFrom, Date dateTo) {
        this.bookingCodeList = bookingCodeList;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public void writeHeadingFile(Document document) {
        Paragraph hotelTitle = new Paragraph("Hotel Booking Online", FontFactory.getFont(FontFactory.HELVETICA_BOLD));
        hotelTitle.setSpacingAfter(5);
        document.add(hotelTitle);

        LocalDate localDate = LocalDate.now();

        Paragraph today = new Paragraph("Date: " + formatter.format(localDate));
        document.add(today);

        Paragraph heading = new Paragraph(new Chunk("REVENUE REPORT",FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
        heading.setAlignment(Element.ALIGN_CENTER);
        heading.setSpacingBefore(10);
        heading.setSpacingAfter(5);
        document.add(heading);
    }

    public void writeBasicInformation(Document document) {
        Paragraph title = new Paragraph("All booking code that book date",FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
        title.setSpacingBefore(10);
        document.add(title);

        Paragraph dateFrom = new Paragraph("From: ");
        dateFrom.setSpacingBefore(10);
        dateFrom.add(formatter.format(this.dateFrom.toLocalDate()));
        document.add(dateFrom);

        Paragraph dateTo = new Paragraph("To: ");
        dateTo.setSpacingBefore(10);
        dateTo.add(formatter.format(this.dateTo.toLocalDate()));
        document.add(dateTo);
    }

    public void writeBookingCodeTable(Document document) {
        Paragraph title = new Paragraph("BOOKING CODE LIST",FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
        title.setSpacingBefore(15);
        document.add(title);

        PdfPTable table = new PdfPTable(6);
        table.setSpacingBefore(10);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1.6f, 2f, 1.6f, 1.6f, 1.6f, 1.6f});

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new Color(88, 138, 206));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        cell.setPhrase(new Phrase("Booking Code",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Customer",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Book Date", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Book From", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Book To", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Payment Advance", font));
        table.addCell(cell);

        for (BookingCode bc : this.bookingCodeList) {
            this.revenue += bc.getCustomer().getAdvancePayment();

            table.getDefaultCell().setPaddingTop(5);
            table.getDefaultCell().setPaddingBottom(5);

            table.addCell(bc.getBookingCode());
            table.addCell(bc.getCustomer().getName());
            table.addCell(formatter.format(bc.getBookingDate().toLocalDate()));
            table.addCell(formatter.format(bc.getDateFrom().toLocalDate()));
            table.addCell(formatter.format(bc.getDateTo().toLocalDate()));
            table.addCell(String.format("%,.0f", bc.getCustomer().getAdvancePayment()));
        }

        document.add(table);
    }

    public void writeRevenue(Document document) {
        Paragraph revenueInfo = new Paragraph("Total revue:   ", FontFactory.getFont(FontFactory.HELVETICA_BOLD));
        revenueInfo.add(String.format("%,.2f",this.revenue) + " VND");

        revenueInfo.setSpacingBefore(30);
        revenueInfo.setAlignment(Element.ALIGN_RIGHT);
        revenueInfo.setIndentationRight(25);

        document.add(revenueInfo);
    }

    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        //open
        document.open();

        writeHeadingFile(document);
        writeBasicInformation(document);
        writeBookingCodeTable(document);
        writeRevenue(document);

        document.close();
    }
}
