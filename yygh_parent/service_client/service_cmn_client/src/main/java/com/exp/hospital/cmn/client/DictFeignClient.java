package com.exp.hospital.cmn.client;

import com.exp.hospital.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient("service-cmn") // 配置文件中写的服务名
@Repository
public interface DictFeignClient {
    // 复制要调用的方法
    /**
     * 根据dict_code 和 value 查询
     */
    @GetMapping("/admin/cmn/dict/getName/{dictCode}/{value}")
    Result getName(@PathVariable("dictCode") String dictCode,
                   @PathVariable("value") String value);
    /**
     * 根据value查询
     */
    @GetMapping("/admin/cmn/dict/getName/{value}")
    Result getName(@PathVariable("value") String value);
}
