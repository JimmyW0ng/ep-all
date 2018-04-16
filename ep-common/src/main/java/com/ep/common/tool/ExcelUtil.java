package com.ep.common.tool;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;

//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @Description: excel工具类
 * @Author: CC.F
 * @Date: 9:24 2018/4/16/016
 */
public class ExcelUtil {

    /**
     * 导出excel文件
     */
    public static <T> void exportExcel(HttpServletResponse response, String fileName, int colNum, List<T> list, List<String> fieldNameStrs, String[] titles) throws Exception {
//        Class <T> entityClass = (Class <T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        //1.1创建合并单元格对象
        CellRangeAddress callRangeAddress = new CellRangeAddress(0, 0, 0, colNum);//起始行,结束行,起始列,结束列
//        //1.2头标题样式
//        HSSFCellStyle headStyle = createCellStyle(workbook,(short)16);
//        //1.3列标题样式
//        HSSFCellStyle colStyle = createCellStyle(workbook,(short)13);
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(fileName);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow(0);
        sheet.addMergedRegion(callRangeAddress);
        //设置默认列宽
        sheet.setDefaultColumnWidth(25);
        //3.创建行
        //3.1创建头标题行;并且设置头标题
        HSSFCell cell = row.createCell(0);

        //加载单元格样式
        cell.setCellValue("用户列表");
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        //1.1创建合并单元格对象
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        //3.2创建列标题;并且设置列标题
        HSSFRow row2 = sheet.createRow(1);
        for (int i = 0; i < titles.length; i++) {
            HSSFCell cell2 = row2.createCell(i);
            //加载单元格样式
//            cell2.setCellStyle(colStyle);
            cell2.setCellValue(titles[i]);
        }

        //4.操作单元格;将用户列表写入excel

        if (list != null) {
            for (int j = 0; j < list.size(); j++) {
                //创建数据行,前面有两行,头标题行和列标题行
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

//                    Field field = list.get(j).getClass().getField(fieldNameStrs.get(k));
                    field.setAccessible(true);
                    cell1.setCellValue((String) field.get(list.get(j)));
                }

            }
        }

//        HSSFCell cell = row.createCell(0);
//        cell.setCellValue("姓名");
//        cell.setCellStyle(style);
////        cell = row.createCell(1);
////        cell.setCellValue("班级");
////        cell.setCellStyle(style);
////        cell = row.createCell(2);
////        cell.setCellValue("分数");
////        cell.setCellStyle(style);
////        cell = row.createCell(3);
////        cell.setCellValue("时间");
////        cell.setCellStyle(style);
//
//        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
//        List<Integer> list = Lists.newArrayList();
//        list.add(1);
////                studentMapper.selectAll();
//
//        for (int i = 0; i < list.size(); i++){
//            row = sheet.createRow(i + 1);
////            Student stu = list.get(i);
//            // 第四步，创建单元格，并设置值
//            row.createCell(0).setCellValue(list.get(i));
////            row.createCell(1).setCellValue(stu.getClasses());
////            row.createCell(2).setCellValue(stu.getScore());
////            cell = row.createCell(3);
////            cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(stu.getTime()));
//        }
        //第六步,输出Excel文件
        OutputStream output = response.getOutputStream();
        response.reset();
//        long filename = System.currentTimeMillis();
//        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
//        String fileName = df.format(new Date());// new Date()为获取当前系统时间
        response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
        response.setContentType("application/ms-excel");
        wb.write(output);
        output.close();
    }


}
