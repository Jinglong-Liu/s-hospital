package com.exp.hospital.cmn.controller;

import com.exp.hospital.cmn.service.DictService;
import com.exp.hospital.common.result.Result;
import com.exp.hospital.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api("数据字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
//@CrossOrigin
public class DictController {
    @Autowired
    private DictService dictService;

    // 根据id查询子数据列表
    @ApiOperation(value = "根据id查询子数据列表")
    @GetMapping("findChildData/{id}")
    public Result findChildData(@PathVariable Long id) {
        List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);
    }

    /**
     * 导出数据字典
     */
    @GetMapping("exportData")
    public void exportDict(HttpServletResponse response){
        dictService.exportDictData(response);
        //return Result.ok();
    }
    /**
     * 导入数据字典
     */
    @PostMapping("importData")
    public Result importDict(MultipartFile file){
        dictService.importDictData(file);
        return Result.ok();
    }
    /**
     * 根据dict_code 和 value 查询
     */
    @GetMapping("getName/{dictCode}/{value}")
    public Result getName(@PathVariable String dictCode,@PathVariable String value){
        String dictName = dictService.getDictName(dictCode,value);
        return Result.ok(dictName);
    }
    /**
     * 根据value查询
     */
    @GetMapping("getName/{value}")
    public Result getName(@PathVariable String value){
        String dictName = dictService.getDictName("",value);
        return Result.ok(dictName);
    }

    /**
     * 根据dict_code查下一级
     */
    @ApiOperation("根据dictCode查下级结点")
    @GetMapping("/findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable String dictCode){
        List<Dict> list = dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }
}
