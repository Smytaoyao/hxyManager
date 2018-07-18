package io.renren.common.utils;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Songxiaoyan
 * @since 2.0.0 2018/5/15
 */
public class ExcelUtil<T> {

    public void exportExcel(String title, String[] headers,
                            Collection<T> dataset, OutputStream out, String pattern)
    {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 15);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);//SOLID_FOREGROUND
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBorderLeft(BorderStyle.THIN);
        style2.setBorderRight(BorderStyle.THIN);
        style2.setBorderTop(BorderStyle.THIN);
        style2.setAlignment(HorizontalAlignment.CENTER);
        style2.setVerticalAlignment(VerticalAlignment.CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBold(true);
        // 把字体应用到当前的样式
        style2.setFont(font2);

        // 声明一个画图的顶级管理器
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        // 定义注释的大小和位置,详见文档
        HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
                0, 0, 0, (short) 4, 2, (short) 6, 5));
        // 设置注释内容
        comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
        // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
        comment.setAuthor("admin");

        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for (short i = 0; i < headers.length; i++)
        {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 0;
        while (it.hasNext())
        {
            index++;
            row = sheet.createRow(index);
            T t = (T) it.next();
            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            Field[] fields = t.getClass().getDeclaredFields();
            for (short i = 0; i < fields.length; i++)
            {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(style2);
                Field field = fields[i];
                String fieldName = field.getName();
                String getMethodName = "get"
                        + fieldName.substring(0, 1).toUpperCase()
                        + fieldName.substring(1);
                try
                {
                    Class tCls = t.getClass();
                    Method getMethod = tCls.getMethod(getMethodName,
                            new Class[]
                                    {});
                    Object value = getMethod.invoke(t, new Object[]
                            {});
                    // 判断值的类型后进行强制类型转换
                    String textValue = null;

                    if (value instanceof Boolean)
                    {
                        boolean bValue = (Boolean) value;
                        textValue = "男";
                        if (!bValue)
                        {
                            textValue = "女";
                        }
                    }
                    else if (value instanceof Date)
                    {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        textValue = sdf.format(date);
                    }
                    else if (value instanceof byte[])
                    {
                        // 有图片时，设置行高为60px;
                        row.setHeightInPoints(60);
                        // 设置图片所在列宽度为80px,注意这里单位的一个换算
                        sheet.setColumnWidth(i, (short) (35.7 * 80));
                        // sheet.autoSizeColumn(i);
                        byte[] bsValue = (byte[]) value;
                        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,
                                1023, 255, (short) 6, index, (short) 6, index);
                        anchor.setAnchorType(ClientAnchor.AnchorType.byId(2));
                        patriarch.createPicture(anchor, workbook.addPicture(
                                bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
                    }
                    else
                    {
                        // 其它数据类型都当作字符串简单处理
                        textValue = value.toString();
                    }
                    // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
                    if (textValue != null)
                    {
                        Pattern p = Pattern.compile("^//d+(//.//d+)?$");
                        Matcher matcher = p.matcher(textValue);
                        if (matcher.matches())
                        {
                            // 是数字当作double处理
                            cell.setCellValue(Double.parseDouble(textValue));
                        }
                        else
                        {
                            HSSFRichTextString richString = new HSSFRichTextString(
                                    textValue);
                            HSSFFont font3 = workbook.createFont();
                            font3.setColor(HSSFColor.BLUE.index);
                            richString.applyFont(font3);
                            cell.setCellValue(richString);
                        }
                    }
                }
                catch (SecurityException e)
                {
                    e.printStackTrace();
                }
                catch (NoSuchMethodException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalArgumentException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
                catch (InvocationTargetException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    // 清理资源
                }
            }
        }
        try
        {
            workbook.write(out);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void exportExcelByMap(String title, String[] headers,
                            List<Map<String,Object>> mapList, OutputStream out, String pattern)
    {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 15);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.WHITE.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        //font.setFontHeightInPoints((short) 20);
        font.setBold(true);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.WHITE.index);
        style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBorderLeft(BorderStyle.THIN);
        style2.setBorderRight(BorderStyle.THIN);
        style2.setBorderTop(BorderStyle.THIN);
        style2.setAlignment(HorizontalAlignment.CENTER);
        style2.setVerticalAlignment(VerticalAlignment.CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBold(true);
        font2.setColor(HSSFColor.BLACK.index);
        // 把字体应用到当前的样式
        style2.setFont(font2);

        // 声明一个画图的顶级管理器
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for (short i = 0; i < headers.length; i++)
        {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        // 遍历集合数据，产生数据行
        for(short i=0; i < mapList.size(); i++){
            row = sheet.createRow(i+1);
            for (short j=0; j<headers.length; j++){
                HSSFCell cell = row.createCell(j);
                cell.setCellStyle(style2);

                try{
                    Object value = mapList.get(i).get(headers[j]);
                    //System.out.println(String.format("i=%s and j=%s and mapList.get(i)=%s and headers[j]=%s",i,j,JSON.toJSONString(mapList.get(i)),headers[j]));

                    String textValue = null;

                    if (value instanceof Boolean)
                    {
                        boolean bValue = (Boolean) value;
                        textValue = "男";
                        if (!bValue)
                        {
                            textValue = "女";
                        }
                    }
                    else if (value instanceof Date)
                    {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        textValue = sdf.format(date);
                    }
                    else if (value instanceof byte[])
                    {
                        // 有图片时，设置行高为60px;
                        row.setHeightInPoints(60);
                        // 设置图片所在列宽度为80px,注意这里单位的一个换算
                        sheet.setColumnWidth(i, (short) (35.7 * 80));
                        // sheet.autoSizeColumn(i);
                        byte[] bsValue = (byte[]) value;
                        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,
                                1023, 255, (short) j, i+1, (short) j, i+1);
                        anchor.setAnchorType(ClientAnchor.AnchorType.byId(2));
                        patriarch.createPicture(anchor, workbook.addPicture(
                                bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
                    }
                    else
                    {
                        // 其它数据类型都当作字符串简单处理
                        if(value!=null)
                            textValue = value.toString();
                        else textValue="";
                    }
                    // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
                    if (textValue != null)
                    {
                        Pattern p = Pattern.compile("^//d+(//.//d+)?$");
                        Matcher matcher = p.matcher(textValue);
                        if (matcher.matches())
                        {
                            // 是数字当作double处理
                            cell.setCellValue(Double.parseDouble(textValue));
                        }
                        else
                        {
                            HSSFRichTextString richString = new HSSFRichTextString(
                                    textValue);
                            HSSFFont font3 = workbook.createFont();
                            font3.setColor(HSSFColor.BLUE.index);
                            richString.applyFont(font3);
                            cell.setCellValue(richString);
                        }
                    }
                }catch (SecurityException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalArgumentException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    // 清理资源
                }

            }
        }
        try
        {
            workbook.write(out);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * 填充数据到普通的excel文件中
     * @param rs
     * @param wb
     * @param headers
     * @throws Exception
     */
    public static void fillExcelData(ResultSet rs,Workbook wb,String[] headers)throws Exception{
        Sheet sheet=wb.createSheet();
        Row row=sheet.createRow(0);

        //先填充行头 : "编号","姓名","电话","Email","QQ","出生日期"
        for(int i=0;i<headers.length;i++){
            row.createCell(i).setCellValue(headers[i]);
        }

        //再填充数据
        int rowIndex=1;
        while(rs.next()){
            row=sheet.createRow(rowIndex++);
            for(int i=0;i<headers.length;i++){
                Object objVal=rs.getObject(i+1);
                row.createCell(i).setCellValue(objVal.toString());
            }
        }
    }

    /**
     * 填充数据到模板excel文件
     * @param rs
     * @param templateFileName
     * @return
     * @throws Exception
     */
    public static Workbook fillExcelDataWithTemplate(ResultSet rs, String templateFileName)throws Exception{
        //首先:从本地磁盘读取模板excel文件,然后读取第一个sheet
        InputStream inp=ExcelUtil.class.getResourceAsStream("/com/steadyjack/template/"+templateFileName);
        POIFSFileSystem fs=new POIFSFileSystem(inp);
        Workbook wb=new HSSFWorkbook(fs);
        Sheet sheet=wb.getSheetAt(0);

        //开始写入数据到模板中: 需要注意的是,因为行头以及设置好,故而需要跳过行头
        int cellNums=sheet.getRow(0).getLastCellNum();
        int rowIndex=1;
        while(rs.next()){
            Row row=sheet.createRow(rowIndex++);
            for(int i=0;i<cellNums;i++){
                Object objVal=rs.getObject(i+1);
                row.createCell(i).setCellValue(objVal.toString());
            }
        }
        return wb;
    }

    /**
     * 处理单元格格式的简单方式
     * @param hssfCell
     * @return
     */
    public static String formatCell(XSSFCell hssfCell){
        if(hssfCell==null){
            return "";
        }else{
            if(hssfCell.getCellType()==XSSFCell.CELL_TYPE_BOOLEAN){
                return String.valueOf(hssfCell.getBooleanCellValue());
            }else if(hssfCell.getCellType()==XSSFCell.CELL_TYPE_NUMERIC){
                return String.valueOf(hssfCell.getNumericCellValue());
            }else{
                return String.valueOf(hssfCell.getStringCellValue());
            }
        }
    }

    /**
     * 处理单元格格式的第二种方式: 包括如何对单元格内容是日期的处理
     * @param cell
     * @return
     */
    public static String formatCell2(HSSFCell cell) {
        if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {

            //针对单元格式为日期格式
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
            }
            return String.valueOf(cell.getNumericCellValue());
        } else {
            return cell.getStringCellValue();
        }
    }

    /**
     * 处理单元格格式的第三种方法:比较全面
     * @param cell
     * @return
     */
    public static String formatCell3(XSSFCell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:

                //日期格式的处理
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    return sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
                }

                return String.valueOf(cell.getNumericCellValue());

            //字符串
            case HSSFCell.CELL_TYPE_STRING:
                return cell.getStringCellValue();

            // 公式
            case HSSFCell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();

            // 空白
            case HSSFCell.CELL_TYPE_BLANK:
                return "";

            // 布尔取值
            case HSSFCell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue() + "";

            //错误类型
            case HSSFCell.CELL_TYPE_ERROR:
                return cell.getErrorCellValue() + "";
        }

        return "";
    }


    /**
     * @Title: createExcelTemplate
     * @Description: 生成Excel导入模板
     * @param @param filePath  Excel文件路径
     * @param @param handers   Excel列标题(数组)
     * @param @param downData  下拉框数据(数组)
     * @param @param downRows  下拉列的序号(数组,序号从0开始)
     * @return void
     * @throws
     */
    public static void createExcelTemplate(String[] handers,List<String[]> downData, String[] downRows,OutputStream out){

        Workbook wb = new XSSFWorkbook();//创建工作薄

        //表头样式
        XSSFCellStyle style = ((XSSFWorkbook) wb).createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式
        //字体样式
        XSSFFont fontStyle = ((XSSFWorkbook) wb).createFont();
        fontStyle.setFontName("微软雅黑");
        fontStyle.setFontHeightInPoints((short)12);
        fontStyle.setBold(true);
        style.setFont(fontStyle);

        //新建sheet
        XSSFSheet sheet1 = ((XSSFWorkbook) wb).createSheet("Sheet1");
        XSSFSheet sheet2 = ((XSSFWorkbook) wb).createSheet("Sheet2");
        XSSFSheet sheet3 = ((XSSFWorkbook) wb).createSheet("Sheet3");

        //生成sheet1内容
        XSSFRow rowFirst = sheet1.createRow(0);//第一个sheet的第一行为标题
        //写标题
        for(int i=0;i<handers.length;i++){
            XSSFCell cell = rowFirst.createCell(i); //获取第一行的每个单元格
            sheet1.setColumnWidth(i, 4000); //设置每列的列宽
            cell.setCellStyle(style); //加样式
            cell.setCellValue(handers[i]); //往单元格里写数据
        }

        //设置下拉框数据
        String[] arr = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        int index = 0;
        XSSFRow row = null;
        //设置单元格格式为文本格式
        XSSFCellStyle textStyle=((XSSFWorkbook) wb).createCellStyle();
        XSSFDataFormat dataFormat = ((XSSFWorkbook) wb).createDataFormat();
        textStyle.setDataFormat(dataFormat.getFormat("@"));
        for(int i=1;i<5000;i++){
            XSSFRow currow = sheet1.createRow(i);
            for(int j=0;j<handers.length;j++){
                XSSFCell cell=currow.createCell(j);
                cell.setCellStyle(textStyle);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            }
        }
        for(int r=0;r<downRows.length;r++){
            String[] dlData = downData.get(r);//获取下拉对象
            int rownum = Integer.parseInt(downRows[r]);

            if(dlData.length<500){ //255以内的下拉
                //255以内的下拉,参数分别是：作用的sheet、下拉内容数组、起始行、终止行、起始列、终止列
                sheet1.addValidationData(SetXSSFDataValidation(sheet1, dlData, 1, 50000, rownum ,rownum)); //超过255个报错

            } else { //255以上的下拉，即下拉列表元素很多的情况

                //1、设置有效性
                //String strFormula = "Sheet2!$A$1:$A$5000" ; //Sheet2第A1到A5000作为下拉列表来源数据
                String strFormula = "Sheet2!$"+arr[index]+"$1:$"+arr[index]+"$5000"; //Sheet2第A1到A5000作为下拉列表来源数据
                sheet2.setColumnWidth(r, 4000); //设置每列的列宽
                //设置数据有效性加载在哪个单元格上,参数分别是：从sheet2获取A1到A5000作为一个下拉的数据、起始行、终止行、起始列、终止列
                sheet1.addValidationData(SetDataValidation(strFormula, 1, 50000, rownum, rownum)); //下拉列表元素很多的情况

                //2、生成sheet2内容
                for(int j=0;j<dlData.length;j++){
                    if(index==0){ //第1个下拉选项，直接创建行、列
                        row = sheet2.createRow(j); //创建数据行
                        sheet2.setColumnWidth(j, 4000); //设置每列的列宽
                        row.createCell(0).setCellValue(dlData[j]); //设置对应单元格的值

                    } else { //非第1个下拉选项

                        int rowCount = sheet2.getLastRowNum();
                        //System.out.println("========== LastRowNum =========" + rowCount);
                        if(j<=rowCount){ //前面创建过的行，直接获取行，创建列
                            //获取行，创建列
                            sheet2.getRow(j).createCell(index).setCellValue(dlData[j]); //设置对应单元格的值

                        } else { //未创建过的行，直接创建行、创建列
                            sheet2.setColumnWidth(j, 4000); //设置每列的列宽
                            //创建行、创建列
                            sheet2.createRow(j).createCell(index).setCellValue(dlData[j]); //设置对应单元格的值
                        }
                    }
                }
                index++;
            }
        }

        try {
            wb.write(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @Title: SetDataValidation
     * @Description: 下拉列表元素很多的情况 (255以上的下拉)
     * @param @param strFormula
     * @param @param firstRow   起始行
     * @param @param endRow     终止行
     * @param @param firstCol   起始列
     * @param @param endCol     终止列
     * @param @return
     * @return HSSFDataValidation
     * @throws
     */
    private static HSSFDataValidation SetDataValidation(String strFormula,
                                                        int firstRow, int endRow, int firstCol, int endCol) {

        // 设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(strFormula);
        HSSFDataValidation dataValidation = new HSSFDataValidation(regions,constraint);

        dataValidation.createErrorBox("Error", "Error");
        dataValidation.createPromptBox("", null);

        return dataValidation;
    }

    private static XSSFDataValidation SetXSSFDataValidation(Sheet sheet, String[] textList, int firstRow, int endRow, int firstCol, int endCol){
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet)sheet);
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper
                .createExplicitListConstraint(textList);
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
        validation.setSuppressDropDownArrow(true);
        validation.setShowErrorBox(true);
        return validation;
    }


    /**
     *
     * @Title: setDataValidation
     * @Description: 下拉列表元素不多的情况(255以内的下拉)
     * @param @param sheet
     * @param @param textList
     * @param @param firstRow
     * @param @param endRow
     * @param @param firstCol
     * @param @param endCol
     * @param @return
     * @return DataValidation
     * @throws
     */
    private static DataValidation setDataValidation(Sheet sheet, String[] textList, int firstRow, int endRow, int firstCol, int endCol) {

        DataValidationHelper helper = sheet.getDataValidationHelper();
        //加载下拉列表内容
        DataValidationConstraint constraint = helper.createExplicitListConstraint(textList);
        //DVConstraint constraint = new DVConstraint();
        constraint.setExplicitListValues(textList);

        //设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList((short) firstRow, (short) endRow, (short) firstCol, (short) endCol);

        //数据有效性对象
        DataValidation data_validation = helper.createValidation(constraint, regions);
        //DataValidation data_validation = new DataValidation(regions, constraint);

        return data_validation;
    }


    /**
     * 获取Excel2003图片
     * @param sheetNum 当前sheet编号
     * @param sheet 当前sheet对象
     * @param workbook 工作簿对象
     * @return Map key:图片单元格索引（0_1_1）String，value:图片流PictureData
     * @throws IOException
     */
    public static Map<String, PictureData> getSheetPictrues03(int sheetNum,
                                                              HSSFSheet sheet, HSSFWorkbook workbook) {

        Map<String, PictureData> sheetIndexPicMap = new HashMap<String, PictureData>();
        List<HSSFPictureData> pictures = workbook.getAllPictures();
        if (pictures.size() != 0) {
            for (HSSFShape shape : sheet.getDrawingPatriarch().getChildren()) {
                HSSFClientAnchor anchor = (HSSFClientAnchor) shape.getAnchor();
                if (shape instanceof HSSFPicture) {
                    HSSFPicture pic = (HSSFPicture) shape;
                    int pictureIndex = pic.getPictureIndex() - 1;
                    HSSFPictureData picData = pictures.get(pictureIndex);
                    String picIndex = String.valueOf(sheetNum) + "_"
                            + String.valueOf(anchor.getRow1()) + "_"
                            + String.valueOf(anchor.getCol1());
                    sheetIndexPicMap.put(picIndex, picData);
                }
            }
            return sheetIndexPicMap;
        } else {
            return null;
        }
    }

    /**
     * 获取Excel2007图片
     * @param sheetNum 当前sheet编号
     * @param sheet 当前sheet对象
     * @param workbook 工作簿对象
     * @return Map key:图片单元格索引（0_1_1）String，value:图片流PictureData
     */
    public static Map<String, PictureData> getSheetPictrues07(int sheetNum,
                                                              XSSFSheet sheet, XSSFWorkbook workbook) {
        Map<String, PictureData> sheetIndexPicMap = new HashMap<String, PictureData>();

        for (POIXMLDocumentPart dr : sheet.getRelations()) {
            if (dr instanceof XSSFDrawing) {
                XSSFDrawing drawing = (XSSFDrawing) dr;
                List<XSSFShape> shapes = drawing.getShapes();
                for (XSSFShape shape : shapes) {
                    XSSFPicture pic = (XSSFPicture) shape;
                    XSSFClientAnchor anchor = pic.getPreferredSize();
                    CTMarker ctMarker = anchor.getFrom();
                    String picIndex = String.valueOf(sheetNum) + "_"
                            + ctMarker.getRow() + "_" + ctMarker.getCol();
                    sheetIndexPicMap.put(picIndex, pic.getPictureData());
                }
            }
        }

        return sheetIndexPicMap;
    }

    public static Map<String, List<XSSFPictureData>> getSheetPictures(XSSFSheet sheet){
        Map<String, List<XSSFPictureData>> dataMap = new HashMap<String, List<XSSFPictureData>>();

        List<XSSFPictureData> pictureDataList = null;

        XSSFDrawing drawing = sheet.getDrawingPatriarch();
        if(drawing == null) return dataMap;
        List<XSSFShape> shapes = drawing.getShapes();
        if(shapes == null || shapes.size() == 0)
            return dataMap;
        for (XSSFShape shape : shapes) {
            XSSFPicture pic = (XSSFPicture) shape;
            XSSFClientAnchor anchor = pic.getPreferredSize();
            int row1 = anchor.getRow1();
            String rowNum = String.valueOf(row1);
            if(dataMap.get(rowNum)!=null){
                pictureDataList = dataMap.get(rowNum);
            }else{
                pictureDataList = new ArrayList<XSSFPictureData>();
            }
            pictureDataList.add(pic.getPictureData());
            dataMap.put(rowNum, pictureDataList);
        }

        return dataMap;
    }



}
