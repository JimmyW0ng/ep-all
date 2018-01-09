package com.ep.common.tool;

/**
 * @Description: 文件工具类
 * @Author: J.W
 * @Date: 下午3:38 17/11/17
 */
public class FileTools {

    public static final String POINT = ".";

    /**
     * 获取文件扩展名
     *
     * @param fileName 文件名
     * @return
     */
    public static String getFileExt(String fileName) {
        if (StringTools.isBlank(fileName)) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(POINT) + 1);
    }

}
