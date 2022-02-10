package com.exp.easyexcel;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestWrite {
    @Test
    public void testWrite(){
        // 构建list
        List<UserData> list = new ArrayList<>();
        for(int i = 0;i < 10;i++){
            UserData data = new UserData();
            data.setUid(i);
            data.setUsername("name " + i);
            list.add(data);
        }
        // 设置excel文件路径和文件名称
        String filename = "D:\\excel\\01.xlsx";

        // 调用写入方法
        EasyExcel.write(filename,UserData.class)
                .sheet("用户信息")
                .doWrite(list);
    }
}
