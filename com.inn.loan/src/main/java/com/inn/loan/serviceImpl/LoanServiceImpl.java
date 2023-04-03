package com.inn.loan.serviceImpl;

import com.inn.loan.JWT.JwtFilter;
import com.inn.loan.POJO.Loan;
import com.inn.loan.constents.LoanConstent;
import com.inn.loan.dao.LoanDao;
import com.inn.loan.service.LoanService;
import com.inn.loan.utils.LoanUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jdk.jfr.Category;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    LoanDao loanDao;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        log.info("Inside generateReport");
        try{
            String fileName;
            if(validateRequestMap(requestMap)){
                if(requestMap.containsKey("isGenerate") && !(Boolean)requestMap.get("isGenerate")){
                    fileName = (String) requestMap.get("uuid");
                }
                else {
                    fileName = LoanUtils.getUUID();
                    requestMap.put("uuid",fileName);
                    insertLoan(requestMap);
                }
                String data = "Name: "+requestMap.get("name") +"\n"+"Email: "+requestMap.get("email") + "\n"+
                        "Installment Plan: "+requestMap.get("insPlan");

                Document document = new Document();
                PdfWriter.getInstance(document,new FileOutputStream(LoanConstent.STORE_LOCATION+"\\"+fileName+".pdf"));
                document.open();
                setTableInPDF(document);

                Paragraph chunk = new Paragraph("Bumble-bee System",getFont("Header"));
                chunk.setAlignment(Element.ALIGN_CENTER);
                document.add(chunk);

                Paragraph paragraph = new Paragraph(data+"\n \n",getFont("Data"));
                document.add(paragraph);

                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                addTableHeader(table);

                JSONArray jsonArray = LoanUtils.getJsonArrayFromString((String)requestMap.get("productDetails"));
                for(int i=0;i<jsonArray.length();i++){
                    addRow(table,LoanUtils.getMapFromJson(jsonArray.getString(i)));
                }
                document.add(table);

                Paragraph footer = new Paragraph("Total : "+requestMap.get("totalAmount")+"\n"
                +"*Please note that you can't get loans again until you settle the payment",getFont("Data"));
                document.add(footer);
                document.close();
                return new ResponseEntity<>("{\"uuid\""+fileName+"\"}",HttpStatus.OK);
            }
            return LoanUtils.getResponseEntity("Request data not found.",HttpStatus.BAD_REQUEST);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return LoanUtils.getResponseEntity(LoanConstent.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void addRow(PdfPTable table, Map<String, Object> data) {
        log.info("Inside addRows");
        table.addCell((String) data.get("name"));
        table.addCell((String) data.get("seller"));
        table.addCell((String) data.get("quantity"));
        table.addCell(Double.toString((Double)data.get("price")));
        table.addCell(Double.toString((Double)data.get("total")));
    }

    private void addTableHeader(PdfPTable table) {
        log.info("Inside addTableHeader");
        Stream.of("Name","Seller","Quantity","Price","SubTotal")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    header.setBackgroundColor(BaseColor.WHITE);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }

    private Font getFont(String type) {
        log.info("Inside getFont");
        switch (type){
            case "Header":
                Font headerFont = FontFactory.getFont(FontFactory.COURIER_BOLDOBLIQUE,1,BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;
            case "Data":
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN,11,BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;
            default:
                return new Font();
        }
    }

    private void setTableInPDF(Document document) throws DocumentException {

        log.info("Inside setTableInPDF");
        Rectangle rect = new Rectangle(577,825,18,15);
        rect.enableBorderSide(1);
        rect.enableBorderSide(2);
        rect.enableBorderSide(3);
        rect.enableBorderSide(4);
        rect.enableBorderSide(8);
        rect.setBorderColor(BaseColor.BLACK);
        rect.setBorderWidth(1);
        document.add(rect);

    }

    private void insertLoan(Map<String, Object> requestMap) {
        //Date date = new Date();
        //SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        //String strDate = formatter.format(date);
        try {
            Loan loan = new Loan();
            loan.setUuid((String) requestMap.get("uuid"));
            loan.setName((String) requestMap.get("name"));
            loan.setEmail((String) requestMap.get("email"));
            //loan.setDate(strDate(requestMap.get("date")));
            loan.setInsPlan((String) requestMap.get("insPlan"));
            loan.setAmount(Integer.parseInt((String)requestMap.get("amount")));
            loan.setBalance(Integer.parseInt((String) requestMap.get("balance")));
            loan.setProductDetail((String) requestMap.get("productDetails"));
            loan.setReceivedby(jwtFilter.getCurrentUser());
            loanDao.save(loan);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private boolean validateRequestMap(Map<String, Object> requestMap) {
        return requestMap.containsKey("name")&&
                //requestMap.containsKey("date")&&
                requestMap.containsKey("email")&&
                requestMap.containsKey("insPlan")&&
                requestMap.containsKey("amount")&&
                requestMap.containsKey("balance")&&
                requestMap.containsKey("productdetails");


    }

    @Override
    public ResponseEntity<List<Loan>> getLoans() {

        List<Loan> list = new ArrayList<>();
        if(jwtFilter.isAdmin()){
            list = loanDao.getAllLoans();
        }
        else {
            list = loanDao.getLoanByUserName(jwtFilter.getCurrentUser());
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        log.info("Inside getPDF : requestMap {}",requestMap);
        try {
           byte[] byteArray = new byte[0];
           if(!requestMap.containsKey("uuid") && validateRequestMap(requestMap))
               return new ResponseEntity<>(byteArray,HttpStatus.BAD_REQUEST);
           String filePath = LoanConstent.STORE_LOCATION+"\\"+(String) requestMap.get("uuid")+".pdf";
           if(LoanUtils.isFileExist(filePath)){
               byteArray = getByteArray(filePath);
               return  new ResponseEntity<>(byteArray, HttpStatus.OK);
           }
           else {
               requestMap.put("isGenerate",false);
               generateReport(requestMap);
               byteArray = getByteArray(filePath);
               return new ResponseEntity<>(byteArray,HttpStatus.OK);
           }

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    private byte[] getByteArray(String filePath) throws Exception {
        File initialFile = new File(filePath);
        InputStream targetStream = new FileInputStream(initialFile);
        byte[] byteArray = IOUtils.toByteArray(targetStream);
        targetStream.close();
        return byteArray;
    }

    @Override
    public ResponseEntity<String> deleteLoan(Integer id) {
        try {
            Optional optional = loanDao.findById(id);
            if(!optional.isEmpty()){
               loanDao.deleteById(id);
               return LoanUtils.getResponseEntity("Bill deleted successfully.",HttpStatus.OK);
            }
            return LoanUtils.getResponseEntity("Loan id does not exist.",HttpStatus.OK);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return LoanUtils.getResponseEntity(LoanConstent.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
