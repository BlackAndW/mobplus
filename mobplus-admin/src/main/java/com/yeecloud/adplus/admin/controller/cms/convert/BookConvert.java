package com.yeecloud.adplus.admin.controller.cms.convert;

import com.yeecloud.adplus.admin.common.convert.GeneralConvert;
import com.yeecloud.adplus.admin.controller.cms.vo.BookChapterVO;
import com.yeecloud.adplus.admin.controller.cms.vo.BookDataVO;
import com.yeecloud.adplus.dal.entity.BookChapter;
import com.yeecloud.adplus.dal.entity.BookData;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author: Leonard
 * @create: 2022/2/18
 */
@Mapper(componentModel = "spring", uses = GeneralConvert.class)
public interface BookConvert {

    BookDataVO convertBookData(BookData bookData);

    List<BookDataVO> convertBookData(List<BookData> bookDataList);

    BookChapterVO convertBookChapter(BookChapter bookChapter);

    List<BookChapterVO> convertBookChapter(List<BookChapter> bookChapterList);
}
