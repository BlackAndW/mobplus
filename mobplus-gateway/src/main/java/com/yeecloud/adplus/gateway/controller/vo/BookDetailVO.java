package com.yeecloud.adplus.gateway.controller.vo;

import lombok.Data;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2022/3/1
 */
@Data
public class BookDetailVO extends BookVO{

    private long size;

    private long readCount;

    private long fakeReadCount;

    private List<BookChapterVO> chapterList;

}
