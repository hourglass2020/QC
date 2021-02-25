package Utilities;

import BackEndPackage.QCConstantValues;
import CompanyPackage.Company;
import DeviecPackage.Attribute;
import DeviecPackage.Device;
import com.jfoenix.controls.JFXTextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.Units;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xddf.usermodel.text.FontAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Utils
{
    private Company company = Company.getCompany();

    private XSSFWorkbook workbook = new XSSFWorkbook();

    private Map<String , Object[]> infoTitles = new TreeMap<>();

    ///////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////

    public void make_firstSheet()
    {
        XSSFSheet first_sheet = workbook.createSheet("main_sheet");
        XSSFRow row;
        int rowId = 0;

        makeInfo();

        first_sheet.setColumnWidth(0, 2700);

        for(int i = 1; i< 8 ; i++)
        {
            first_sheet.setColumnWidth(i, 2000);
        }

        first_sheet.setColumnWidth(8, 5000);

        first_sheet.addMergedRegion(
                new CellRangeAddress(1, 1, 1, 5)
        );

        first_sheet.addMergedRegion(
                new CellRangeAddress(0, 0, 1, 5)
        );

        first_sheet.addMergedRegion(
                new CellRangeAddress(2, 2, 1, 5)
        );

        first_sheet.addMergedRegion(
                new CellRangeAddress(3, 3, 1, 5)
        );

        first_sheet.addMergedRegion(
                new CellRangeAddress(4, 4, 1, 5)
        );

        first_sheet.addMergedRegion(
                new CellRangeAddress(5, 5, 1, 5)
        );


        Set<String> keyId = infoTitles.keySet();

        for(String key : keyId)
        {
            row = first_sheet.createRow(rowId++);
            row.setHeight((short) 300);

            Object[] objArray = infoTitles.get(String.valueOf(rowId));

            int cellId = 0;

            for(Object obj : objArray)
            {
                Cell cell = row.createCell(cellId++);
                cell.setCellValue(obj.toString());

                switch (obj.toString()) {
                    case "آدرس":
                    case "مرکز":
                    case "تاریخ":
                    case "Total":
                    case "دستگاه":
                    case "سازنده":
                    case "مدل":
                    case "محل استقرار دستگاه":
                    case "سریال":
                    case "شماره اموال":
                    case "کنترل کننده":
                    case "نتیجه":
                    case "دلیل":
                        cell.setCellStyle(cellStyleFactory("titleStyle"));
                        break;
                    case "Pass":
                        cell.setCellStyle(cellStyleFactory("passStyle"));
                        break;
                    case "Fail":
                        cell.setCellStyle(cellStyleFactory("failStyle"));
                        break;
                    case "Conditional":
                        cell.setCellStyle(cellStyleFactory("condStyle"));
                        break;
                    default:
                        cell.setCellStyle(cellStyleFactory("cellStyle2"));
                        break;
                }
            }
        }

        for(Device device :company.getMainHealthCenter().getInputDevices())
        {
            row = first_sheet.createRow(rowId++);
            row.setHeight((short) 300);

            Object[] objArray = makePack(device);

            int cellId = 0;

            for(Object obj : objArray)
            {
                Cell cell = row.createCell(cellId++);
                if(obj == null)
                    obj = (String) "N/A";

                cell.setCellValue(obj.toString());

                switch (obj.toString()) {
                    case "آدرس":
                    case "مرکز":
                    case "تاریخ":
                    case "Total":
                    case "دستگاه":
                    case "سازنده":
                    case "مدل":
                    case "محل استقرار دستگاه":
                    case "سریال":
                    case "شماره اموال":
                    case "کنترل کننده":
                    case "نتیجه":
                    case "دلیل":
                        cell.setCellStyle(cellStyleFactory("titleStyle"));
                        break;
                    case "Pass":
                        cell.setCellStyle(cellStyleFactory("passStyle"));
                        break;
                    case "Fail":
                        cell.setCellStyle(cellStyleFactory("failStyle"));
                        break;
                    case "Conditional":
                        cell.setCellStyle(cellStyleFactory("condStyle"));
                        break;
                    default:
                        cell.setCellStyle(cellStyleFactory("cellStyle2"));
                        break;
                }
            }
        }


        for(int index = 0 ; index < 6 ; index++)
        {
            for(int j = 2 ; j < 6 ; j++)
            {
                Cell cell = first_sheet.getRow(index).createCell(j);
                cell.setCellStyle(cellStyleFactory("cellStyle2"));
            }
        }

    }

    public void make_secondSheet()
    {
        XSSFSheet secondSheet = workbook.createSheet("DevicesPieChart");
        XSSFRow row = secondSheet.createRow(0);

        Cell cell;
        int cellId = 0;

        for(Device device : company.getMainHealthCenter().getCategory_devices())
        {
            cell = row.createCell(cellId);
            cell.setCellValue(device.getEngNameOfDevice());
            cell.setCellStyle(cellStyleFactory("titleStyle"));
            cellId++;
        }

        row = secondSheet.createRow(1);
        cellId = 0;
        for(Device device : company.getMainHealthCenter().getCategory_devices())
        {
            cell = row.createCell(cellId);
            cell.setCellValue(device.size);
            cell.setCellStyle(cellStyleFactory("cellStyle"));
            cellId++;
        }

        XSSFDrawing drawing = secondSheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 4, 8, 25);

        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleText("Devices");
        chart.setTitleOverlay(false);

        XDDFChartLegend legend = chart.getOrAddLegend();
        (legend).setPosition(LegendPosition.TOP_RIGHT);

        int company_devices_size = company.getMainHealthCenter().getCategory_devices().size();

        if(company_devices_size == 0)
        {
            company_devices_size++;
        }

        XDDFDataSource<String> countries = XDDFDataSourcesFactory.fromStringCellRange(secondSheet,
                new CellRangeAddress(0, 0, 0, company_devices_size-1));

        XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromNumericCellRange(secondSheet,
                new CellRangeAddress(1, 1, 0, company_devices_size-1));

//          XDDFChartData data = chart.createData(ChartTypes.PIE, null, null);
        XDDFChartData data = new XDDFPieChartData(chart.getCTChart().getPlotArea().addNewPieChart());
        data.setVaryColors(true);

        data.setVaryColors(true);
        data.addSeries(countries, values);
        chart.plot(data);
    }

    public void make_devicesSheets()
    {
        XSSFSheet deviceSheet;
        XSSFRow row;
        XSSFCell cell;

        for(Device device : company.getMainHealthCenter().getCategory_devices())
        {
            deviceSheet = workbook.createSheet(device.getEngNameOfDevice());

            row = deviceSheet.createRow(0);

            cell = row.createCell(0);
            cell.setCellValue("Pass");
            cell.setCellStyle(cellStyleFactory("passStyle"));

            cell = row.createCell(1);
            cell.setCellValue("Fail");
            cell.setCellStyle(cellStyleFactory("failStyle"));

            cell = row.createCell(2);
            cell.setCellValue("Conditional");
            cell.setCellStyle(cellStyleFactory("condStyle"));

            row = deviceSheet.createRow(1);

            cell = row.createCell(0);
            cell.setCellValue(device.pass);
            cell.setCellStyle(cellStyleFactory("cellStyle"));

            cell = row.createCell(1);
            cell.setCellValue(device.fail);
            cell.setCellStyle(cellStyleFactory("cellStyle"));

            cell = row.createCell(2);
            cell.setCellValue(device.cond);
            cell.setCellStyle(cellStyleFactory("cellStyle"));

            XSSFDrawing drawing = deviceSheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 4, 6, 16);

            XSSFChart chart = drawing.createChart(anchor);
            chart.setTitleText(device.getEngNameOfDevice());
            chart.setTitleOverlay(false);

            XDDFChartLegend legend = chart.getOrAddLegend();
            (legend).setPosition(LegendPosition.TOP_RIGHT);

            XDDFDataSource<String> countries = XDDFDataSourcesFactory.fromStringCellRange(deviceSheet,
                    new CellRangeAddress(0, 0, 0, 2));

            XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromNumericCellRange(deviceSheet,
                    new CellRangeAddress(1, 1, 0, 2));

//          XDDFChartData data = chart.createData(ChartTypes.PIE, null, null);
            XDDFChartData data = new XDDFPieChartData(chart.getCTChart().getPlotArea().addNewPieChart());
            data.setVaryColors(true);

            data.setVaryColors(true);
            data.addSeries(countries, values);
            chart.plot(data);
        }
    }

    private XSSFCellStyle cellStyleFactory(String styleName)
    {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 9);
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setFontName("Parastoo Print");

        Font font1 = workbook.createFont();
        font1.setFontHeightInPoints((short) 9);
        font1.setFontName("Parastoo Print");

        if (styleName.equalsIgnoreCase("cellStyle"))
        {
            XSSFColor color = new XSSFColor(new java.awt.Color(34, 102, 102), null);
            XSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBottomBorderColor(color);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setLeftBorderColor(color);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setRightBorderColor(color);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setTopBorderColor(color);
            cellStyle.setFont(font1);
            return cellStyle;
        }

        else if (styleName.equalsIgnoreCase("cellStyle2"))
        {
            XSSFCellStyle cellStyle2 = workbook.createCellStyle();
            cellStyle2.setAlignment(HorizontalAlignment.CENTER);
            cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle2.setFont(font1);

            XSSFColor color = new XSSFColor(new java.awt.Color(63, 63, 90), null);

            cellStyle2.setBorderBottom(BorderStyle.THIN);
            cellStyle2.setBottomBorderColor(color);
            cellStyle2.setBorderLeft(BorderStyle.THIN);
            cellStyle2.setLeftBorderColor(color);
            cellStyle2.setBorderRight(BorderStyle.THIN);
            cellStyle2.setRightBorderColor(color);
            cellStyle2.setBorderTop(BorderStyle.THIN);
            cellStyle2.setTopBorderColor(color);

            return cellStyle2;
        }

        else if (styleName.equalsIgnoreCase("passStyle"))
        {
            XSSFCellStyle passStyle = workbook.createCellStyle();
            XSSFColor pass_color = new XSSFColor(new java.awt.Color(38, 115, 77), null);
            passStyle.setFillForegroundColor(pass_color);
            passStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            passStyle.setAlignment(HorizontalAlignment.CENTER);
            passStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            passStyle.setFont(font);

            passStyle.setBorderBottom(BorderStyle.THIN);
            passStyle.setBottomBorderColor(pass_color);
            passStyle.setBorderLeft(BorderStyle.THIN);
            passStyle.setLeftBorderColor(pass_color);
            passStyle.setBorderRight(BorderStyle.THIN);
            passStyle.setRightBorderColor(pass_color);
            passStyle.setBorderTop(BorderStyle.THIN);
            passStyle.setTopBorderColor(pass_color);

            return passStyle;
        }

        else if (styleName.equalsIgnoreCase("failStyle"))
        {
            XSSFCellStyle failStyle = workbook.createCellStyle();
            XSSFColor fail_color = new XSSFColor(new java.awt.Color(213, 46, 46), null);
            failStyle.setFillForegroundColor(fail_color);
            failStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            failStyle.setAlignment(HorizontalAlignment.CENTER);
            failStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            failStyle.setFont(font);

            failStyle.setBorderBottom(BorderStyle.THIN);
            failStyle.setBottomBorderColor(fail_color);
            failStyle.setBorderLeft(BorderStyle.THIN);
            failStyle.setLeftBorderColor(fail_color);
            failStyle.setBorderRight(BorderStyle.THIN);
            failStyle.setRightBorderColor(fail_color);
            failStyle.setBorderTop(BorderStyle.THIN);
            failStyle.setTopBorderColor(fail_color);

            return failStyle;
        }

        else if (styleName.equalsIgnoreCase("condStyle"))
        {
            XSSFCellStyle condStyle = workbook.createCellStyle();
            XSSFColor cond_color = new XSSFColor(new java.awt.Color(255, 224, 0), null);
            condStyle.setFillForegroundColor(cond_color);
            condStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            condStyle.setAlignment(HorizontalAlignment.CENTER);
            condStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            condStyle.setFont(font1);

            condStyle.setBorderBottom(BorderStyle.THIN);
            condStyle.setBottomBorderColor(cond_color);
            condStyle.setBorderLeft(BorderStyle.THIN);
            condStyle.setLeftBorderColor(cond_color);
            condStyle.setBorderRight(BorderStyle.THIN);
            condStyle.setRightBorderColor(cond_color);
            condStyle.setBorderTop(BorderStyle.THIN);
            condStyle.setTopBorderColor(cond_color);

            return condStyle;
        }

        else if (styleName.equalsIgnoreCase("titleStyle"))
        {
            XSSFCellStyle titleSection = workbook.createCellStyle();
            XSSFColor title_color = new XSSFColor(new java.awt.Color(63, 63, 90), null);
            titleSection.setFillForegroundColor(title_color);
            titleSection.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            titleSection.setAlignment(HorizontalAlignment.CENTER);
            titleSection.setVerticalAlignment(VerticalAlignment.CENTER);
            titleSection.setFont(font);

            titleSection.setBorderBottom(BorderStyle.THIN);
            titleSection.setBottomBorderColor(title_color);
            titleSection.setBorderLeft(BorderStyle.THIN);
            titleSection.setLeftBorderColor(title_color);
            titleSection.setBorderRight(BorderStyle.THIN);
            titleSection.setRightBorderColor(title_color);
            titleSection.setBorderTop(BorderStyle.THIN);
            titleSection.setTopBorderColor(title_color);

            return titleSection;
        }

        return style;
    }

    private Object[] makePack(Device device)
    {
        Object[] array = new Object[9];
        array[0] = device.getPerNameOfDevice();
        array[1] = device.getSerialNo();

        for(int index = 2 ; index < 9 ; index++)
        {

            if(index == 7)
            {
                array[index] = device.getResult();
                array[index + 1] = device.getDefaultAttributes().get(index-1).getMainValue();

                break;
            }
            array[index] = device.getDefaultAttributes().get(index-1).getMainValue();
        }

        return array;
    }

    private void makeInfo()
    {
        infoTitles.put("1",new Object[] {"مرکز" , company.getMainHealthCenter().getName()});
        infoTitles.put("2", new Object[]{"آدرس" , company.getMainHealthCenter().getAddress()});
        infoTitles.put("3", new Object[]{"تاریخ" , company.getMainHealthCenter().getDate()});
//        QC_Map.info.put("4", new Object[]{"Total" , total});
        infoTitles.put("4", new Object[]{"Pass" , company.getMainHealthCenter().pass});
        infoTitles.put("5", new Object[]{"Fail" , company.getMainHealthCenter().fail});
        infoTitles.put("6", new Object[]{"Conditional" , company.getMainHealthCenter().cond});
        infoTitles.put("7", new Object[]{});
        infoTitles.put("8", new Object[] {"دستگاه" , "شماره سریال" , "سازنده" , "مدل" , "شماره اموال" ,
                "محل استقرار دستگاه" , "کنترل کننده", "نتیجه آزمون" , "دلیل"});

    }

    public void get_excel(File dir) throws IOException
    {
        make_firstSheet();
        make_secondSheet();
        make_devicesSheets();

        String filename = company.getMainHealthCenter().getName().trim() + ".xlsx";

        FileOutputStream output = new FileOutputStream(new File(dir , filename));

        workbook.write(output);
        output.close();
    }

    ///////////////////////////////////////////////////////


    public void get_AllDocumentations(File dir) throws IOException, InvalidFormatException {


        for(Device device : company.getMainHealthCenter().getInputDevices())
        {
            get_SingleDocumetation(device , dir);
        }
    }

    public void get_SingleDocumetation(Device device , File dir) throws IOException, InvalidFormatException
    {
        int unknownCounter = 1;

        XWPFDocument document = new XWPFDocument();

        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(document, sectPr);

        //write header content
        CTP ctpHeader = CTP.Factory.newInstance();
        CTR ctrHeader = ctpHeader.addNewR();
        CTText ctHeader = ctrHeader.addNewT();
        String headerText = "شرکت ارکــان کیفیت گستر البــرز";
        ctHeader.setStringValue(headerText);
        XWPFParagraph headerParagraph = new XWPFParagraph(ctpHeader, document);
        XWPFRun headerRun = headerParagraph.createRun();
        headerParagraph.setAlignment(ParagraphAlignment.RIGHT);
        headerRun.setFontFamily("REVOLUTION");
        XWPFParagraph[] parsHeader = new XWPFParagraph[1];
        parsHeader[0] = headerParagraph;
        policy.createHeader(XWPFHeaderFooterPolicy.DEFAULT, parsHeader);

        //write footer content
        CTP ctpFooter = CTP.Factory.newInstance();
        CTR ctrFooter = ctpFooter.addNewR();
        CTText ctFooter = ctrFooter.addNewT();
        String footerText = "آدرس : تهران- پارک علم و فناوری پردیس-نوآوری 16-دانش 15-قطعه 206 ب              تلفکس:81060-021 ";
        ctFooter.setStringValue(footerText);
        XWPFParagraph footerParagraph = new XWPFParagraph(ctpFooter, document);
        footerParagraph.setAlignment(ParagraphAlignment.RIGHT);
        XWPFRun footerRun = headerParagraph.createRun();
        footerRun.setFontFamily("Sahel");
        XWPFParagraph[] parsFooter = new XWPFParagraph[1];
        parsFooter[0] = footerParagraph;
        policy.createFooter(XWPFHeaderFooterPolicy.DEFAULT, parsFooter);

        XWPFParagraph paragraph1 = document.createParagraph();
        XWPFRun run1 = paragraph1.createRun();

        File LogoImage = new File("QC.png");
        BufferedImage bLogoImage = ImageIO.read(LogoImage);

        int width = 210;
        int height = 230;

        String imgFile = LogoImage.getName();
        int imgFormat = getImageFormat(imgFile);

        run1.addPicture(new FileInputStream(LogoImage) , imgFormat , imgFile ,
                Units.toEMU(width) , Units.toEMU(height));

        paragraph1.setAlignment(ParagraphAlignment.CENTER);

        run1.addBreak();
        run1.addBreak();

        run1.setText("Quality Control Program");
        run1.setFontFamily("REVOLUTION");
        run1.setFontSize(20);
//            run1.setColor("#009999");

        run1.addBreak();

        XWPFParagraph paragraph2 = document.createParagraph();
        XWPFRun run2 = paragraph2.createRun();
        run2.setFontFamily("Parastoo Print");
        run2.setFontSize(19);
        run2.setText("گواهی آزمون کنترل کیفیت و نتایج کالیبراسیون");
        run2.addBreak();

        paragraph2.setAlignment(ParagraphAlignment.CENTER);

        XWPFParagraph paragraph3 = document.createParagraph();
        XWPFRun run3 = paragraph3.createRun();
        run2.setFontFamily("Parastoo Print");
        run2.setFontSize(14);
        run2.setText( " : استانداردهای مرجع و قابلیت ردیابی" );
        run2.addBreak();
        run2.setText(device.getIsoNo());

        paragraph3.setAlignment(ParagraphAlignment.RIGHT);


        XWPFParagraph paragraph31 = document.createParagraph();
        XWPFRun run31 = paragraph31.createRun();
        run31.setFontFamily("Parastoo Print");
        run31.setFontSize(14);
        run31.setText(company.getMainHealthCenter().getName() + " : مرکز درمانی" );
//        run31.addBreak();

        paragraph31.setAlignment(ParagraphAlignment.CENTER);

        XWPFParagraph paragraph32 = document.createParagraph();
        XWPFRun run32 = paragraph32.createRun();
        run32.setFontFamily("Parastoo Print");
        run32.setFontSize(14);
        run32.setText(company.getMainHealthCenter().getDate() + " : تاریخ آزمون" );
//        run32.addBreak();

        paragraph32.setAlignment(ParagraphAlignment.CENTER);

        XWPFParagraph paragraph33 = document.createParagraph();
        XWPFRun run33 = paragraph33.createRun();
        run33.setFontFamily("Parastoo Print");
        run33.setFontSize(14);
        run33.setText(device.getResult() + " : نتیجه آزمون" );
        run33.addBreak();

        paragraph33.setAlignment(ParagraphAlignment.CENTER);

        XWPFTable table1 = document.createTable();
        table1.setTableAlignment(TableRowAlign.CENTER);

        XWPFTableRow row1 = table1.getRow(0);
        row1.getCell(0).setText("Serial No.");
        row1.addNewTableCell().setText(device.getSerialNo());

        row1.getCell(0).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2700));
        row1.getCell(1).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2700));

        int test_1 = 0;

        for(Attribute attribute : device.getDefaultAttributes())
        {
            if(test_1 == 0)
            {
                test_1++;
                continue;
            }

            row1 = table1.createRow();
            String val = attribute.getMainValue();

            if(val == null || val.equalsIgnoreCase(""))
            {
                val = "N/A";
            }

            row1.getCell(1).setText(val);
            row1.getCell(1).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
            row1.getCell(0).setText(attribute.getName());
            row1.getCell(0).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        }

        XWPFParagraph paragraph4 = document.createParagraph();
        XWPFRun run4 = paragraph4.createRun();

        run4.addBreak();

        test_1 = 0;

        for(int index = 0 ; index < device.getCategories().size() ; index++)
        {
            String category = device.getCategories().get(index);

            if(category.equalsIgnoreCase("Default"))
            {
                continue;
            }

            XWPFParagraph cate_paragraph = document.createParagraph();
            XWPFRun cate_run = cate_paragraph.createRun();
            cate_run.setFontFamily("Parastoo Print");
            cate_run.setFontSize(19);
            cate_run.addBreak();
            cate_run.setText(category);
            cate_paragraph.setAlignment(ParagraphAlignment.RIGHT);
//            cate_run.addBreak();

//            XWPFParagraph tempParagraph = document.createParagraph();
//            XWPFRun tempRun = tempParagraph.createRun();
            XWPFTable tempTable = document.createTable();
            tempTable.setTableAlignment(TableRowAlign.CENTER);

            XWPFTableRow tempRow = tempTable.getRow(0);
            tempRow.getCell(0).setText("Attribute");
            tempRow.addNewTableCell().setText("Value");

            tempRow.getCell(0).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2700));
            tempRow.getCell(1).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2700));

            int count_attributes = 0;

            for(Attribute attribute : device.getAddedAttributes())
            {
                if(attribute.getCategory().equalsIgnoreCase(category) &&
                        attribute.getMode().equalsIgnoreCase(Attribute.RADIO_BTN_GP)) {

                    tempRow = tempTable.createRow();

                    String val = attribute.getMainValue();

                    if (val == null || val.equalsIgnoreCase("")) {
                        val = "N/A";
                    }

                    tempRow.getCell(1).setText(val);
                    tempRow.getCell(1).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                    tempRow.getCell(0).setText(attribute.getName());
                    tempRow.getCell(0).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

                    count_attributes++;
                }
            }

            if (count_attributes == 0)
            {
                tempTable.removeRow(0);
            }

            count_attributes = 0;

//            XWPFParagraph tempParagraph2 = document.createParagraph();
//            XWPFRun tempRun2 = tempParagraph2.createRun();
            XWPFTable tempTable2 = document.createTable();
            tempTable2.setTableAlignment(TableRowAlign.CENTER);

            XWPFTableRow tempRow2 = tempTable2.getRow(0);
            tempRow2.getCell(0).setText("Attribute");
            tempRow2.addNewTableCell().setText("Value");
            tempRow2.addNewTableCell().setText("min");
            tempRow2.addNewTableCell().setText("max");

            tempRow2.getCell(0).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(1700));
            tempRow2.getCell(1).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(1500));
            tempRow2.getCell(2).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(1500));
            tempRow2.getCell(3).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(1500));

            for(Attribute attribute : device.getAddedAttributes())
            {
                if(attribute.getCategory().equalsIgnoreCase(category) &&
                        attribute.getMode().equalsIgnoreCase(Attribute.TEXT_FIELD)) {

                    tempRow2 = tempTable2.createRow();

                    String val = attribute.getMainValue();

                    if (val == null || val.equalsIgnoreCase("")) {
                        val = "N/A";
                    }

                    tempRow2.getCell(0).setText(attribute.getName());
                    tempRow2.getCell(0).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                    tempRow2.getCell(1).setText(val);
                    tempRow2.getCell(1).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                    tempRow2.getCell(2).setText(String.valueOf(attribute.getMin()));
                    tempRow2.getCell(2).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                    tempRow2.getCell(3).setText(String.valueOf(attribute.getMax()));
                    tempRow2.getCell(3).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

                    count_attributes++;
                }
            }

            if (count_attributes == 0)
            {
                tempTable2.removeRow(0);
            }

        }

        XWPFParagraph paragraph43 = document.createParagraph();
        XWPFRun run43 = paragraph43.createRun();
        run43.setFontFamily("Parastoo Print");
        run43.setFontSize(14);
        run43.addBreak();
        run43.setText("کالیبره کننده : علی اقدم پور" );
        run43.setBold(true);
        paragraph43.setAlignment(ParagraphAlignment.RIGHT);


        XWPFParagraph paragraph53 = document.createParagraph();
        XWPFRun run53 = paragraph53.createRun();
        run53.setFontFamily("Parastoo Print");
        run53.setFontSize(14);
        run53.setText(" مدیر فنی : حسان زین العابدینی رفیع" );
        run53.setBold(true);
        paragraph53.setAlignment(ParagraphAlignment.RIGHT);


        XWPFParagraph paragraph63 = document.createParagraph();
        XWPFRun run63 = paragraph63.createRun();
        run63.setFontFamily("Parastoo Print");
        run63.setFontSize(14);
        run63.setText("آزمون براساس توافق با مشتری تنظیم گردیده و استفاده کننده بایددرفاصله زمانی معین نسبت به آزمون و .کالیبراسیون مجدداقدام نماید" );
        paragraph63.setAlignment(ParagraphAlignment.RIGHT);


        XWPFParagraph paragraph73 = document.createParagraph();
        XWPFRun run73 = paragraph73.createRun();
        run73.setFontFamily("Parastoo Print");
        run73.setFontSize(14);
        run73.setText("نتایج مذکور مربوط به وضعیت دستگاه تحت تست در تاریخ کنترل کیفی است ودلالتی برپایداری بلندمدت .دستگاه تحت تست ندارد" );
        paragraph73.setAlignment(ParagraphAlignment.RIGHT);
        paragraph73.setIndentationRight(0);

        XWPFParagraph paragraph83 = document.createParagraph();
        XWPFRun run83 = paragraph83.createRun();
        run83.setFontFamily("Parastoo Print");
        run83.setFontSize(14);
        run83.setText(".هرگونه نسخه برداری ازاین گواهی بایدبه طورکامل و از تمامی صفحات باشد" );
        paragraph83.setIndentationLeft(0);
        paragraph83.setAlignment(ParagraphAlignment.RIGHT);


        try
        {
            String fileName = device.getSerialNo();

            if(fileName == null || fileName.equalsIgnoreCase(""))
            {
                fileName = "Unknown" + unknownCounter;
                unknownCounter++;
            }

            OutputStream stream = new FileOutputStream(new File(dir , fileName + ".docx"));
            document.write(stream);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void get_DevicesIdentification(File dir) throws IOException, InvalidFormatException
    {
        XWPFDocument document = new XWPFDocument();

        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(document, sectPr);

        //write header content
        CTP ctpHeader = CTP.Factory.newInstance();
        CTR ctrHeader = ctpHeader.addNewR();
        CTText ctHeader = ctrHeader.addNewT();
        String headerText = "Pixel Digital Market Company";
        ctHeader.setStringValue(headerText);
        XWPFParagraph headerParagraph = new XWPFParagraph(ctpHeader, document);
        XWPFRun headerRun = headerParagraph.createRun();
        headerRun.setFontFamily("REVOLUTION");
        XWPFParagraph[] parsHeader = new XWPFParagraph[1];
        parsHeader[0] = headerParagraph;
        policy.createHeader(XWPFHeaderFooterPolicy.DEFAULT, parsHeader);

        //write footer content
        CTP ctpFooter = CTP.Factory.newInstance();
        CTR ctrFooter = ctpFooter.addNewR();
        CTText ctFooter = ctrFooter.addNewT();
        String footerText = "Phone : 098-9229677343  Telegram : @Sawed_Nazari";
        ctFooter.setStringValue(footerText);
        XWPFParagraph footerParagraph = new XWPFParagraph(ctpFooter, document);
        XWPFRun footerRun = headerParagraph.createRun();
        footerRun.setFontFamily("Sahel");
        XWPFParagraph[] parsFooter = new XWPFParagraph[1];
        parsFooter[0] = footerParagraph;
        policy.createFooter(XWPFHeaderFooterPolicy.DEFAULT, parsFooter);


        for (Device device : company.getMainHealthCenter().getInputDevices())
        {
            XWPFParagraph paragraph4 = document.createParagraph();
            XWPFRun run4 = paragraph4.createRun();

            run4.addBreak();

            XWPFTable tempTable = document.createTable();
            tempTable.setTableAlignment(TableRowAlign.CENTER);

            XWPFTableRow row1 = tempTable.getRow(0);
            row1.getCell(0).setText("Device Name : " + device.getEngNameOfDevice());
            row1.addNewTableCell().setText("Manufacture : " + device.getDefaultAttributes().get(1).getMainValue());

            row1.getCell(0).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2700));
            row1.getCell(1).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2700));

            row1 = tempTable.createRow();
            row1.getCell(1).setText("Model : " + device.getDefaultAttributes().get(2).getMainValue());
            row1.getCell(1).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
            row1.getCell(0).setText("Serial No. : " + device.getDefaultAttributes().get(0).getMainValue());
            row1.getCell(0).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

            row1 = tempTable.createRow();
            row1.getCell(1).setText("Section : " + device.getDefaultAttributes().get(4).getMainValue());
            row1.getCell(1).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
            row1.getCell(0).setText("Property No. : " + device.getDefaultAttributes().get(3).getMainValue());
            row1.getCell(0).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        }

        try
        {
            String fileName = company.getMainHealthCenter().getName().trim();
            OutputStream stream = new FileOutputStream(new File(dir , fileName + ".docx"));
            document.write(stream);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static int getImageFormat(String imgFileName) {
        int format;
        if (imgFileName.endsWith(".emf"))
            format = XWPFDocument.PICTURE_TYPE_EMF;
        else if (imgFileName.endsWith(".wmf"))
            format = XWPFDocument.PICTURE_TYPE_WMF;
        else if (imgFileName.endsWith(".pict"))
            format = XWPFDocument.PICTURE_TYPE_PICT;
        else if (imgFileName.endsWith(".jpeg") || imgFileName.endsWith(".jpg"))
            format = XWPFDocument.PICTURE_TYPE_JPEG;
        else if (imgFileName.endsWith(".png"))
            format = XWPFDocument.PICTURE_TYPE_PNG;
        else if (imgFileName.endsWith(".dib"))
            format = XWPFDocument.PICTURE_TYPE_DIB;
        else if (imgFileName.endsWith(".gif"))
            format = XWPFDocument.PICTURE_TYPE_GIF;
        else if (imgFileName.endsWith(".tiff"))
            format = XWPFDocument.PICTURE_TYPE_TIFF;
        else if (imgFileName.endsWith(".eps"))
            format = XWPFDocument.PICTURE_TYPE_EPS;
        else if (imgFileName.endsWith(".bmp"))
            format = XWPFDocument.PICTURE_TYPE_BMP;
        else if (imgFileName.endsWith(".wpg"))
            format = XWPFDocument.PICTURE_TYPE_WPG;
        else {
            return 0;
        }
        return format;
    }

    public void make_WarningAlert(String message) throws FileNotFoundException
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(new FileInputStream(QCConstantValues.TITLES[16])));
        alert.showAndWait();
    }

    public void make_ExceptionAlert(Exception ex)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("");
        alert.setContentText("");

// Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new JFXTextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setBackground(new Background(QCConstantValues.background_fill));
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

// Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

}
