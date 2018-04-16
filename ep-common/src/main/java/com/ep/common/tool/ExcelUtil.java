package com.ep.common.tool;

import com.ep.common.component.SpringComponent;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jooq.EnumType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.List;


/**
 * @Description: excel工具类
 * @Author: CC.F
 * @Date: 9:24 2018/4/16/016
 */
public class ExcelUtil {

    /**
     * 导出excel文件
     */
    public static <T> void exportExcel(HttpServletRequest request, HttpServletResponse response, String fileName, int colNum, List<T> list, List<String> fieldNameStrs, String[] titles) throws Exception {
        //创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        //在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(fileName);
        //在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow rowTitle = sheet.createRow(0);
        //设置默认列宽
        sheet.setDefaultColumnWidth(20);
        //创建合并单元格对象,起始行,结束行,起始列,结束列
        CellRangeAddress callRangeAddress = new CellRangeAddress(0, 0, 0, colNum - 1);
        sheet.addMergedRegion(callRangeAddress);
        //创建标题行
        HSSFCell cellTitle = rowTitle.createCell(0);
        //标题行内容
        cellTitle.setCellValue("用户列表");
        //标题行样式
        HSSFCellStyle styleTitle = wb.createCellStyle();
        //标题行样式字体
        HSSFFont fontTitle = wb.createFont();
        fontTitle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        fontTitle.setFontHeight((short) 400);
        styleTitle.setFont(fontTitle);
        //居中格式
        styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellTitle.setCellStyle(styleTitle);
        //创建列名行
        HSSFRow rowCol = sheet.createRow(1);
        for (int i = 0; i < titles.length; i++) {
            //创建列名行样式
            HSSFCell cellCol = rowCol.createCell(i);
            cellCol.setCellValue(titles[i]);
            HSSFCellStyle styleCol = wb.createCellStyle();
            HSSFFont fontCol = wb.createFont();
            fontCol.setFontHeight((short) 240);
            styleCol.setFont(fontCol);
            cellCol.setCellStyle(styleCol);
        }
        for (int j = 0; j < list.size(); j++) {
            HSSFRow row3 = sheet.createRow(j + 2);
            for (int k = 0; k < fieldNameStrs.size(); k++) {
                HSSFCell cell1 = row3.createCell(k);
                Field field = null;
                Class<?> clazz = list.get(j).getClass();
                try {
                    field = clazz.getDeclaredField(fieldNameStrs.get(k));
                } catch (Exception e) {
                }
                if (field == null) {
                    field = clazz.getSuperclass().getDeclaredField(fieldNameStrs.get(k));
                }
                field.setAccessible(true);
                Object obj = field.get(list.get(j));
                if (obj instanceof EnumType) {
                    //字段是枚举
                    String className = obj.getClass().getName();
                    className = className.substring(className.lastIndexOf(".") + 1, className.length());
                    String cellValue = ((EnumType) obj).getLiteral();
                    cell1.setCellValue(SpringComponent.messageSource(className + "." + cellValue));
                } else if (obj instanceof Timestamp) {
                    cell1.setCellValue(DateTools.timestampToString((Timestamp) obj, DateTools.TIME_PATTERN));
                } else {
                    cell1.setCellValue(obj == null ? "" : obj.toString());
                }
            }
        }
        //输出Excel文件
        OutputStream output = response.getOutputStream();
        response.reset();
        String agent = request.getHeader("USER-AGENT").toLowerCase();
        String encodeFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        if (agent.contains("firefox")) {
            response.setCharacterEncoding("utf-8");
            response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1") + ".xls");
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + encodeFileName + ".xls");
        }
        response.setContentType("application/ms-excel");
        wb.write(output);
        output.close();
    }
}
