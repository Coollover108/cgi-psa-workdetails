package org.cgi.cme.psa.workdetails.cgipsaworkdetails.configuration;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cgi.cme.psa.workdetails.cgipsaworkdetails.dto.MemberDetailsDTO;
import org.cgi.cme.psa.workdetails.cgipsaworkdetails.repository.MemberDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class UserExcelExporter {
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
  
  /**
   * Method: createExcelSheet
   * @param memberDetailsDTOList type List<MemberDetailsDTO>
   * return fileName type String
   * 
   * Description: This method will generate Excel sheet report for given DB records
   */
  public String createExcelSheet(List<MemberDetailsDTO>memberDetailsDTOList) {
    String fileName = null;
    try {
      // Create workbook in .xlsx format
      Workbook workbook = new XSSFWorkbook();
      
      // For .xsl workbooks use new HSSFWorkbook();
      // Create Sheet
      Sheet sheet = workbook.createSheet("PSA_Member_WorkDay_Details");
      
      // Create top row with column headings
      String[] columnHeadings = {"Member_Id",
                                  "First Name",
                                  "Middle Name",
                                  "Last Name",
                                  "Member Email Id",
                                  "Member Contact Number",
                                  "Reporting Manager Email Id",
                                  "Number of working days",
                                  "Sunday",
                                  "Monday",
                                  "Tuesday",
                                  "Wednesday",
                                  "Thursday",
                                  "Friday",
                                  "Saturday"};
      
      // We want to make it bold with a foreground color.
      Font headerFont = workbook.createFont();
      headerFont.setBold(true);
      headerFont.setFontHeightInPoints((short) 12);
      headerFont.setColor(IndexedColors.BLACK.index);
      
      // Create a CellStyle with the font
      CellStyle headerStyle = workbook.createCellStyle();
      headerStyle.setFont(headerFont);
      headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
      
      // Create the header row
      Row headerRow = sheet.createRow(0);
      
      // Iterate over the column headings to create columns
      for (int i = 0; i < columnHeadings.length; i++) {
        Cell cell = headerRow.createCell(i);
        cell.setCellValue(columnHeadings[i]);
        cell.setCellStyle(headerStyle);
      }
      
      // Freeze Header Row
      sheet.createFreezePane(0, 1);
      
      // Fill data
      List<MemberDetailsDTO> memberDetailsDTOListObject = memberDetailsDTOList;
      CreationHelper creationHelper = workbook.getCreationHelper();
      CellStyle dateStyle = workbook.createCellStyle();
      dateStyle.setDataFormat(creationHelper.createDataFormat().getFormat("MM/dd/yyyy"));
      int rownum = 1;
      for (MemberDetailsDTO memberDetailsDTO : memberDetailsDTOListObject) {
        log.info("rownum-before" + (rownum));
        Row row = sheet.createRow(rownum++);
        log.info("rownum-after" + (rownum));
        row.createCell(0).setCellValue(memberDetailsDTO.getMemberId());
        row.createCell(1).setCellValue(memberDetailsDTO.getMemberName().getFirstName());
        row.createCell(2).setCellValue(memberDetailsDTO.getMemberName().getMiddleName());
        row.createCell(3).setCellValue(memberDetailsDTO.getMemberName().getLastName());

        row.createCell(4).setCellValue(memberDetailsDTO.getMemberEmailId());
        row.createCell(5).setCellValue(memberDetailsDTO.getMobileNumber());
        row.createCell(6).setCellValue(memberDetailsDTO.getMemberReportingManagerEmail());
        row.createCell(7).setCellValue(memberDetailsDTO.getNoOfWorkingDays());

        row.createCell(8).setCellValue(memberDetailsDTO.getWorkDaysDetails().getSaturday());
        row.createCell(9).setCellValue(memberDetailsDTO.getWorkDaysDetails().getMonday());
        row.createCell(10).setCellValue(memberDetailsDTO.getWorkDaysDetails().getTuesday());
        row.createCell(11).setCellValue(memberDetailsDTO.getWorkDaysDetails().getWednesday());

        row.createCell(12).setCellValue(memberDetailsDTO.getWorkDaysDetails().getThursday());
        row.createCell(13).setCellValue(memberDetailsDTO.getWorkDaysDetails().getFriday());
        row.createCell(14).setCellValue(memberDetailsDTO.getWorkDaysDetails().getSaturday());
      }
      // Group and collapse rows
      int noOfRows = sheet.getLastRowNum();
      sheet.groupRow(1, noOfRows);
      sheet.setRowGroupCollapsed(1, true);
      
           

      // Autosize columns
      for (int i = 0; i < columnHeadings.length; i++) {
        sheet.autoSizeColumn(i);
      }
      Sheet sheet2 = workbook.createSheet("Second");
      // Write the output to file
     
      String currentDate = formatter.format(LocalDate.now());
      fileName="C:\\Users\\shantinath.patil\\Desktop\\reports\\"+"PSAWorkDetails_"+memberDetailsDTOListObject.get(0).getEmailIdToSendReport()+"_"+currentDate+".xlsx";
          
      FileOutputStream fileOut =
          new FileOutputStream(fileName);
      workbook.write(fileOut);
      fileOut.close();
      workbook.close();
      log.info("Completed");
    } catch (Exception exc) {
      log.error("Exception encountered while creating exce sheet for records fetched from DB"+exc.getMessage());
      exc.printStackTrace();
    }
    return fileName;
  }
 
}


