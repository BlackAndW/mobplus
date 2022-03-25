package com.yeecloud.adplus.admin.common.convert;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author: Leonard
 * @create: 2022/3/25
 */

@Slf4j
@Component
@Named("ConversionWorker")
public class ConvertWorker {

    @Named("str2list")
    public List<String> str2list(String sourceStr) {
        return Arrays.asList(sourceStr.split("\\|"));
    }
}
