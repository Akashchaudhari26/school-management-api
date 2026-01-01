package com.sms.modules.fees.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.sms.modules.fees.domain.Fee;
import com.sms.modules.fees.domain.FeePayment;
import com.sms.modules.student.domain.Student;
import com.sms.modules.student.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Optional;

@Service
public class FeeReceiptPdfService {
    
    public byte[] generateReceipt(Fee fee, FeePayment payment, Student student) {
	
	
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4, 40, 40, 40, 40);
        PdfWriter.getInstance(document, out);
        document.open();

        // Fonts
        Font schoolNameFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 11);
        Font smallFont = new Font(Font.HELVETICA, 9, Font.ITALIC);

        // ================= HEADER (LOGO + SCHOOL INFO) =================

        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{1.2f, 4f});

        try {
            ClassPathResource logoResource =
                    new ClassPathResource("static/schoolLogo.png");

            InputStream logoStream = logoResource.getInputStream();
            byte[] logoBytes = logoStream.readAllBytes();
            Image logo = Image.getInstance(logoBytes);
            
            logo.scaleToFit(60, 60);

            PdfPCell logoCell = new PdfPCell(logo);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setRowspan(2);
            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            headerTable.addCell(logoCell);

        } catch (Exception e) {
            // VERY IMPORTANT: log this once
            System.out.println("Logo not loaded: " + e.getMessage());

            PdfPCell emptyCell = new PdfPCell(new Phrase(""));
            emptyCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(emptyCell);
        }


        PdfPCell schoolNameCell = new PdfPCell(
                new Phrase("MATRUTVA LITTLE BUDS", schoolNameFont));
        schoolNameCell.setBorder(Rectangle.NO_BORDER);
        schoolNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        headerTable.addCell(schoolNameCell);

        PdfPCell addressCell = new PdfPCell(
                new Phrase("Near Vitthal Mandir, Sangrampur, Maharashtra\nPhone: 70283 01416",
                        normalFont));
        addressCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(addressCell);

        document.add(headerTable);

        document.add(new Paragraph(" "));
        document.add(new LineSeparator());

        // ================= RECEIPT TITLE =================

        Paragraph title = new Paragraph("FEE PAYMENT RECEIPT", headerFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingBefore(10);
        title.setSpacingAfter(15);
        document.add(title);

        // ================= RECEIPT DETAILS =================

        PdfPTable detailsTable = new PdfPTable(2);
        detailsTable.setWidthPercentage(100);
        detailsTable.setSpacingBefore(10);
        detailsTable.setWidths(new float[]{2, 3});

        addRow(detailsTable, "Receipt No", payment.getReceiptNo(), normalFont);
        addRow(detailsTable, "Student Name", student.getFirstName() + " " + student.getLastName(), normalFont);
        addRow(detailsTable, "Academic Year", fee.getAcademicYear(), normalFont);
        addRow(detailsTable, "Amount Paid", "₹" + payment.getAmountPaid(), normalFont);
        addRow(detailsTable, "Payment Mode", payment.getMode(), normalFont);
        addRow(detailsTable, "Collected By", payment.getCollectedBy(), normalFont);
        addRow(detailsTable, "Payment Date", payment.getPaidAt().toString(), normalFont);

        document.add(detailsTable);

        // ================= FOOTER =================

        document.add(new Paragraph(" "));
        Paragraph footer = new Paragraph(
                "This is a system generated receipt. No signature required.",
                smallFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();
        return out.toByteArray();
    }

    private void addRow(PdfPTable table, String key, String value, Font font) {
        PdfPCell keyCell = new PdfPCell(new Phrase(key, font));
        keyCell.setPadding(6);
        table.addCell(keyCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, font));
        valueCell.setPadding(6);
        table.addCell(valueCell);
    }
}
