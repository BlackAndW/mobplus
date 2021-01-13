package com.yeecloud.adplus.admin.util;

import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Title
 *
 * Date: 2020-01-01 22:29:58
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Slf4j
public class ExcelUtil {

    public static String BIGDECIMAL = "BigDecimal";
    public static String DATE = "Date";

    /**
     * 将数据写入excel工作簿
     *
     * @param sheet     写入的工作簿的对象
     * @param list      写入的数据
     * @param cols      列名数组，对应对象的字段
     * @param startRow  起始行数，不同于Excel文件，基数为0
     * @param startCell 起始列数，不同于Excel文件，基数为0
     */
    public static void writeListToExcel(Sheet sheet, List list, String[] cols, int startRow, int startCell) {
        if (list == null || list.isEmpty()) {
            return;
        }
        CellStyle numberStyle = createNumberStyle(sheet);
        CellStyle normalStyle = createNormalStyle(sheet);
        int rowCounter = 0;
        Class myClazz = list.get(0).getClass();
        for (Object obj : list) {
            rowCounter++;
            Row row = createRow(sheet, startRow++);
            for (int i = 0; i < cols.length; i++) {
                try {
                    if (cols[i].equalsIgnoreCase("sn")) {
                        row.createCell(startCell + i).setCellValue(i + 1);
                    } else {
                        Method getMethod = myClazz.getMethod("get" + cols[i]);
                        Object value = getMethod.invoke(obj, new Object[0]);
                        String pattern = "";
                        if (getMethod.getReturnType() == BigDecimal.class) {
                            pattern = BIGDECIMAL;
                        } else if (getMethod.getReturnType() == Date.class) {
                            pattern = DATE;
                        }
                        Cell cell = row.createCell(startCell + i);
                        writeCell(sheet, cell, value, pattern, i == cols.length - 1, rowCounter == list.size(), numberStyle, normalStyle);
                    }
                } catch (IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                } catch (InvocationTargetException e) {
                    log.error(e.getMessage(), e);
                } catch (NoSuchMethodException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public static void writeBeanToExcel(Sheet sheet, List list, String[] cols, String[] patterns, int startRow, int startCell) {
        if (list == null || list.isEmpty()) {
            return;
        }
        CellStyle numberStyle = createNumberStyle(sheet);
        CellStyle normalStyle = createNormalStyle(sheet);
        int rowCounter = 0;
        for (Object obj : list) {
            rowCounter++;
            Row row = createRow(sheet, startRow++);
            for (int j = 0; j < cols.length; j++) {
                try {
                    Object value = BeanUtilsBean.getInstance().getPropertyUtils().getNestedProperty(obj, cols[j]);
                    String pattern = null == patterns || j >= patterns.length ? "" : patterns[j];
                    Cell cell = row.createCell(startCell + j);
                    writeCell(sheet, cell, value, pattern, j == cols.length - 1, rowCounter == list.size(), numberStyle, normalStyle);
                } catch (Throwable e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }


    /**
     * list中存放的为HashMap 添加单元格式patterns（BigDecimal、Date）
     *
     * @param sheet
     * @param list
     * @param cols
     * @param startRow
     * @param startCell
     */
    public static void writeHashMapToExcel(Sheet sheet, List<Map<String, Object>> list, String[] cols, String[] patterns, int startRow, int startCell) {
        if (list == null || list.isEmpty()) {
            return;
        }
        CellStyle numberStyle = createNumberStyle(sheet);
        CellStyle normalStyle = createNormalStyle(sheet);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            Row row = createRow(sheet, startRow++);
            for (int j = 0; j < cols.length; j++) {
                Object value = map.get(cols[j]);
                String pattern = null == patterns || j >= patterns.length ? "" : patterns[j];
                Cell cell = row.createCell(startCell + j);
                writeCell(sheet, cell, value, pattern, j == cols.length - 1, i == list.size() - 1, numberStyle, normalStyle);
            }
        }
    }

    /* ========================================================= */


    /**
     * 设置导出excel文件的相应头
     *
     * @param resp
     * @param fileName
     * @return
     */
    public static boolean setExportExcelResponseHeader(HttpServletResponse resp, String fileName) {
        try {
            resp.setContentType("application/msexcel;charset=UTF-8");
            resp.setHeader("Content-Disposition", "attachment;filename=" + StringUtils.urlencode(fileName));
            resp.addHeader("Pargam", "no-cache");
            resp.addHeader("Cache-Control", "no-cache");
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    private static void writeCell(Sheet sheet, Cell cell, Object value, String pattern, boolean right, boolean bottom, CellStyle numberStyle, CellStyle normalStyle) {
        if (value == null) {
            value = "";
        }
        if (StringUtils.equals(BIGDECIMAL, pattern)) { // 货比格式
            cell.setCellValue((new BigDecimal(String.valueOf(value))).doubleValue());

            numberStyle.setBorderRight(right ? BorderStyle.MEDIUM : BorderStyle.THIN);
            numberStyle.setBorderBottom(bottom ? BorderStyle.MEDIUM : BorderStyle.THIN);
            cell.setCellStyle(numberStyle);
            return;
        } else if (StringUtils.equals(DATE, pattern)) {// 日期格式
            if (value instanceof Long) {
                value = new Date((Long) value);
            }
            String val = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
            if ("00:00:00".equals(val.substring(11, val.length()))) {
                val = val.substring(0, 10);
            }
            cell.setCellValue(val);
        } else {
            cell.setCellValue(String.valueOf(value));
        }

        normalStyle.setBorderRight(right ? BorderStyle.MEDIUM : BorderStyle.THIN);
        normalStyle.setBorderBottom(bottom ? BorderStyle.MEDIUM : BorderStyle.THIN);
        cell.setCellStyle(normalStyle);
    }

    private static Row createRow(Sheet sheet, int rowIndex) {
        Row row = sheet.createRow(rowIndex);
        row.setHeightInPoints((short) 18);
        return row;
    }

    private static CellStyle createNumberStyle(Sheet sheet) {
        CellStyle style = createStyle(sheet, false, false, true);
        DataFormat format = sheet.getWorkbook().createDataFormat();
        style.setDataFormat(format.getFormat("#,##0.00"));
        return style;
    }

    private static CellStyle createNormalStyle(Sheet sheet) {
        return createStyle(sheet, false, false, false);
    }


    private static CellStyle createStyle(Sheet sheet, boolean right, boolean bottom, boolean number) {
        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(right ? BorderStyle.MEDIUM : BorderStyle.THIN);
        style.setBorderBottom(bottom ? BorderStyle.MEDIUM : BorderStyle.THIN);
        if (number) {
            style.setAlignment(HorizontalAlignment.RIGHT);
        } else {
            style.setAlignment(HorizontalAlignment.CENTER);
        }
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        Font font = sheet.getWorkbook().createFont();
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        return style;
    }
}
