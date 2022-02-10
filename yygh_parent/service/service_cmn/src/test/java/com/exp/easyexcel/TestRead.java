package com.exp.easyexcel;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

public class TestRead {
    @Test
    public void testRead(){
        String filename = "D:\\excel\\01.xlsx";
        EasyExcel.read(filename,UserData.class,new ExcelListener()).sheet().doRead();
    }
}
