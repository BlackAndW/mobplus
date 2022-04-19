package com.yeecloud.adplus.gateway.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.apache.commons.beanutils.NewBeanUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.dal.entity.*;
import com.yeecloud.adplus.dal.repository.*;
import com.yeecloud.adplus.gateway.controller.form.BookForm;
import com.yeecloud.adplus.gateway.controller.form.TranslateForm;
import com.yeecloud.adplus.gateway.controller.vo.BookChapterContentVO;
import com.yeecloud.adplus.gateway.controller.vo.BookChapterVO;
import com.yeecloud.adplus.gateway.controller.vo.BookDetailVO;
import com.yeecloud.adplus.gateway.controller.vo.BookVO;
import com.yeecloud.adplus.gateway.service.BookDataService;
import com.yeecloud.adplus.gateway.util.BaiduTransUtil;
import com.yeecloud.adplus.gateway.util.FileUtil;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import com.yeecloud.meeto.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Leonard
 * @create: 2022/3/1
 */
@Service
public class BookDataServiceImpl implements BookDataService {

    @Autowired
    AppRepository appRepository;

    @Autowired
    BookDataRepository bookDataRepository;

    @Autowired
    BookChapterRepository bookChapterRepository;

    @Autowired
    BookChannelReository bookChannelReository;

    @Autowired
    BookTypeReository bookTypeReository;

    @Autowired
    BookSexReository bookSexReository;

    @Autowired
    BookLabelReository bookLabelReository;

    @Autowired
    Booki18nRepository booki18nRepository;

    @Autowired
    BookChapteri18nRepository bookChapteri18nRepository;

    private static final String TMP_FILE = "/tmp";

    private static final Integer CHANNEL_1 = 1;

    public static final String LANG = "en";

    @Override
    public List<BookVO> queryBookList(BookForm form) throws ServiceException {
        QBookData qBookData = QBookData.bookData;
        Predicate predicate = qBookData.deleted.eq(false);
        String appId = form.getAppId();
        if (StringUtils.isEmpty(appId)) {
            throw new ServiceException("appId is empty!");
        }
        predicate = ExpressionUtils.and(predicate, qBookData.app.appId.eq(appId));
        Integer pageNo = form.getPageNo() == null ? 0 : form.getPageNo();
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "readCount"));
        PageRequest pageRequest = PageRequest.of(pageNo, 10, sort);
        List<BookData> bookDataList = bookDataRepository.findAll(predicate, pageRequest).getContent();
        List<BookVO> bookVOList = new ArrayList<>();
        bookDataList.forEach(BookData::fakeData);
        for(BookData bookData: bookDataList) {
            BookVO vo = new BookVO();
            NewBeanUtils.copyProperties(vo, bookData);
            vo.setBookId(bookData.getId());
            bookVOList.add(vo);
        }
        return bookVOList;
    }

    @Override
    public BookDetailVO queryBookDetail(BookForm form) throws ServiceException {
        QBookData qBookData = QBookData.bookData;
        Predicate predicate = qBookData.deleted.eq(false);
        Integer bookId = form.getBookId();
        if (bookId == null || bookId == 0) {
            throw new ServiceException("bookId is empty!");
        }
        predicate = ExpressionUtils.and(predicate, qBookData.id.eq(bookId));
        BookData bookData = bookDataRepository.findOne(predicate).orElse(null);
        if (bookData == null) {
            throw new ServiceException("cannot find data by bookId: " + bookId);
        }
        bookData.fakeData();
        QBookChapter qBookChapter = QBookChapter.bookChapter;
        Predicate predicate2 = qBookChapter.deleted.eq(false);
        predicate2 = ExpressionUtils.and(predicate2, qBookChapter.bookData.id.eq(bookId));
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "chapterNo"), new Sort.Order(Sort.Direction.ASC, "createdAt"));
        List<BookChapter> bookChapterList = (List<BookChapter>) bookChapterRepository.findAll(predicate2, sort);
        List<BookChapterVO> chapterVOList = new ArrayList<>();
        long readCount = 0;
        for (BookChapter chapter : bookChapterList) {
            BookChapterVO chapterVO = new BookChapterVO();
            NewBeanUtils.copyProperties(chapterVO, chapter);
            chapterVO.setChapterId(chapter.getId());
            chapterVOList.add(chapterVO);
            readCount += chapter.getReadCount();
        }
        BookDetailVO detailVO = new BookDetailVO();
        NewBeanUtils.copyProperties(detailVO, bookData);
        detailVO.setBookId(bookData.getId());
        detailVO.setChapterList(chapterVOList);
        detailVO.setReadCount(readCount);
        return detailVO;
    }

    @Override
    public List<BookChapterVO> queryChapterList(BookForm form) throws ServiceException {
        Integer bookId = form.getBookId();
        if (bookId == null || bookId == 0) {
            throw new ServiceException("bookId is empty!");
        }
        QBookChapter qBookChapter = QBookChapter.bookChapter;
        Predicate predicate = qBookChapter.deleted.eq(false);
        predicate = ExpressionUtils.and(predicate, qBookChapter.bookData.id.eq(bookId));
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "chapterNo"), new Sort.Order(Sort.Direction.ASC, "createdAt"));
        List<BookChapter> bookChapterList = (List<BookChapter>) bookChapterRepository.findAll(predicate, sort);
        List<BookChapterVO> chapterVOList = new ArrayList<>();
        for (BookChapter chapter : bookChapterList) {
            BookChapterVO chapterVO = new BookChapterVO();
            NewBeanUtils.copyProperties(chapterVO, chapter);
            chapterVO.setChapterId(chapter.getId());
            chapterVOList.add(chapterVO);
        }
        return chapterVOList;
    }

    @Override
    public BookChapterContentVO queryBookChapterContent(BookForm form) throws ServiceException {
        QBookChapter qBookChapter = QBookChapter.bookChapter;
        Predicate predicate = qBookChapter.deleted.eq(false);
        if (form.getChapterId() == null) {
            throw new ServiceException("chapterId is empty!");
        }
        predicate = ExpressionUtils.and(predicate, qBookChapter.id.eq(form.getChapterId()));
        BookChapter chapter = bookChapterRepository.findOne(predicate).orElse(null);
        if (chapter == null) {
            throw new ServiceException("cannot find chapter by chapterId: " + form.getChapterId());
        }
        BookChapterContentVO vo = new BookChapterContentVO();
        NewBeanUtils.copyProperties(vo, chapter);
        return vo;
    }

    @Override
    public void countBookData(BookForm form) throws ServiceException {
        QBookData qBookData = QBookData.bookData;
        Predicate predicate = qBookData.deleted.eq(false);
        Integer bookId = form.getBookId();
        if (bookId == null || bookId == 0) {
            throw new ServiceException("bookId is empty!");
        }
        predicate = ExpressionUtils.and(predicate, qBookData.id.eq(bookId));
        BookData bookData = bookDataRepository.findOne(predicate).orElse(null);
        if (bookData == null) {
            throw new ServiceException("cannot find data by bookId: " + bookId);
        }
        if (form.getUserCount() != null && form.getUserCount() == 1) {
            bookData.setUserCount(bookData.getUserCount() + 1);
        }
        if (form.getCollection() != null && form.getCollection() == 1) {
            bookData.setCollection(bookData.getCollection() + 1);
        }
        if (form.getLockCount() != null && form.getLockCount() == 1) {
            bookData.setLockCount(bookData.getLockCount() + 1);
        }
        bookDataRepository.save(bookData);
    }

    @Override
    public void countBookChapter(BookForm form) throws ServiceException {
        QBookChapter qBookChapter = QBookChapter.bookChapter;
        Predicate predicate = qBookChapter.deleted.eq(false);
        if (form.getChapterId() == null) {
            throw new ServiceException("chapterId is empty!");
        }
        predicate = ExpressionUtils.and(predicate, qBookChapter.id.eq(form.getChapterId()));
        BookChapter chapter = bookChapterRepository.findOne(predicate).orElse(null);
        if (chapter == null) {
            throw new ServiceException("cannot find chapter by chapterId: " + form.getChapterId());
        }
        if (form.getReadCount() != null && form.getReadCount() == 1) {
            chapter.setReadCount(chapter.getReadCount() + 1);
        }
        if (form.getExitCount() != null && form.getExitCount() == 1) {
            chapter.setExitCount(chapter.getExitCount() + 1);
        }
        bookChapterRepository.save(chapter);
    }

    @Override
    public void crawlerBookUpload(MultipartFile book) throws Exception {
        String suffix = FileUtil.getExtensionName(book.getOriginalFilename());
        if (!FileUtil.TXT.equals(FileUtil.getFileType(suffix))) {
            throw new ServiceException("文件格式不符");
        }
        InputStream inputStream = book.getInputStream();
        byte[] b = new byte[inputStream.available()];
        while (inputStream.read(b) != -1) {
            String bookInfo = new String(b);
            JSONObject jsonObject = JSON.parseObject(bookInfo);
            String title = jsonObject.getString("title");
            String author = jsonObject.getString("author");
            String category = jsonObject.getString("category");
            String label = jsonObject.getString("label");
            label = StringUtils.isNotEmpty(label) ? label : "未分类";
            Integer isfinish = jsonObject.getInteger("isfinish");
            String lastUpdate = jsonObject.getString("lastUpdate");
            String description = jsonObject.getString("descriptions");
            String picture = jsonObject.getString("picture");

            Integer sexCode = jsonObject.getInteger("sexCode");

            BookData data = bookDataRepository.findByNameAndAuthorAndDeleted(title, author, false);
            if (data != null) {
                throw new ServiceException("duplicate data for : " + title + "-" + author);
            }
            BookData bookData = new BookData();
            App app = appRepository.findByAppId("620cac29e4b0012de4da0f4b");
            bookData.setApp(app);
            bookData.setName(title);
            bookData.setAuthor(author);
            bookData.setStatus(isfinish);
            bookData.setCover(picture);
            bookData.setDescription(description);
            BookChannel channel = bookChannelReository.findById(CHANNEL_1).orElse(null);
            if (channel == null) {
                throw new ServiceException("cannot find channel by id : 1");
            }
            bookData.setBookChannel(channel);
            BookType type = bookTypeReository.findByMapRule(category);
            bookData.setTypeOrg(category);
            bookData.setBookType(type);

            List<String> labelList = Arrays.asList(label.split(" "));
            QBookLabel qBookLabel = QBookLabel.bookLabel;
            Predicate predicate = qBookLabel.deleted.eq(false);
            for (String item : labelList) {
                predicate = ExpressionUtils.or(predicate, qBookLabel.zhName.eq(item));
            }
            List<BookLabel> bookLabels = (List<BookLabel>) bookLabelReository.findAll(predicate);
            StringBuilder labelIds = new StringBuilder();
            bookLabels.forEach(bookLabel -> {
                labelIds.append("|").append(bookLabel.getId());
            });
            bookData.setLabels(labelIds.toString().substring(1));

            BookSex bookSex = bookSexReository.findById(sexCode).orElse(null);
            if (bookSex == null) {
                throw new ServiceException("cannot find sex by sexCode:" + sexCode);
            }
            bookData.setBookSex(bookSex);
            bookData.setCover(picture);
            BookData savedData = bookDataRepository.save(bookData);
            List list = (List) jsonObject.get("chapterArr");

            Booki18n booki18n = new Booki18n();
            booki18n.setLang(LANG);
            booki18n.setBookData(savedData);
//            String title_trans = BaiduTransUtil.translateByDomain(new TranslateForm(title, LANG), "novel");
//            booki18n.setName(title_trans);
//            String author_trans = BaiduTransUtil.translateByDomain(new TranslateForm(author, LANG), "novel");
//            booki18n.setAuthor(author_trans);
//            String description_trans = "It was said that there was no time travel in the world, so there were many people who jumped into the river. [陈飞] didn't think about it, because he didn't need to think about it at all. But when he fell into the river and kept drinking water, he suddenly remembered this sentence. He began to think if it was better to travel through time than drowning. Miraculously, [陈龙] didn't call for help while sinking. Instead, he kept thinking about where he would pass through. Was he out of his mind? A normal person would know that he would die right away, but [陈飞] jumped into the water with a smile. It was all because of a dream he had";
//            String description_trans = BaiduTransUtil.translateByDomain(new TranslateForm(description, LANG), "novel");
//            description_trans = replaceHanziName(description_trans);
//            booki18n.setDescription(description_trans);
            String label_trans = "";
            if (!label.equals("未分类")) {
                label_trans = BaiduTransUtil.translateByDomain(new TranslateForm(label, LANG), "novel");
            }
            booki18n.setLabel(label_trans);
            booki18n.setCatalogue(list.toString());
            booki18nRepository.save(booki18n);
        }
    }

    @Override
    public synchronized void crawlerChapterUpload(MultipartFile folder) throws Exception {
        String fileName = folder.getOriginalFilename();
        System.out.println(fileName);
        String suffix = FileUtil.getExtensionName(fileName);
        if (!FileUtil.TXT.equals(FileUtil.getFileType(suffix))) {
            throw new ServiceException("文件格式不正确");
        }
        String bookName = fileName.substring(0, fileName.indexOf("/"));
        Integer chapterNo = Integer.valueOf(fileName.substring(fileName.indexOf("/") + 1, fileName.indexOf("-")));
        String chapterName = fileName.substring(fileName.indexOf("-") + 1,fileName.indexOf("."));
        BookData bookData = bookDataRepository.findByNameAndDeleted(bookName, false);
        if (bookData == null) {
            throw new ServiceException("cannot find book by name: " + bookName);
        }

        BookChapter chapterCheck = bookChapterRepository.findByBookDataAndNameAndDeleted(bookData, chapterName, false);
        if (chapterCheck != null) {
            throw new ServiceException("duplicate chapter for book: " + bookName
                                        + " and chapter: " + chapterName);
        }
        BookChapter chapter = new BookChapter();
        chapter.setChapterNo(chapterNo);
        chapter.setName(chapterName);
        InputStream inputStream = folder.getInputStream();
        byte[] b = new byte[inputStream.available()];
        String content = "";
        while (inputStream.read(b) != -1) {
            content = new String(b);
        }
        int size = content.length();
        chapter.setSize(size);
        chapter.setContent(content);
        chapter.setBookData(bookData);
        if (chapterNo > 8) {
            chapter.setIsLock(1);
        }
        bookChapterRepository.save(chapter);
        if (chapterNo < 100) {
            String content_trans = "";
            if (size < 2000) {
                content_trans = BaiduTransUtil.translateByDomain(new TranslateForm(content, "en"), "novel");
            }
            else {
                int index = content.indexOf("\r\n", 2000);
                String part1 = content.substring(0, index);
                String part2 = content.substring(index + 1, size);
                String part1_trans = BaiduTransUtil.translateByDomain(new TranslateForm(part1, "en"), "novel");
                String part2_trans = BaiduTransUtil.translateByDomain(new TranslateForm(part2, "en"), "novel");
                content_trans = part1_trans + part2_trans;
            }
            BookChapteri18n bookChapteri18n = new BookChapteri18n();
            bookChapteri18n.setBookChapter(chapter);
            bookChapteri18n.setChapterNo(chapterNo);
            String title_trans = BaiduTransUtil.translateByDomain(new TranslateForm(chapterName, "en"), "novel");
            bookChapteri18n.setTitle(title_trans);
            bookChapteri18n.setContent(replaceHanziName(content_trans));
            bookChapteri18nRepository.save(bookChapteri18n);
        }
    }

    private String replaceHanziName(String src) {
        Pattern pattern = Pattern.compile("\\[.*?]");
        Matcher matcher = pattern.matcher(src);
        while(matcher.find()) {
            String replaceStr = matcher.group();
            String hanziName = replaceStr.substring(1, replaceStr.length() - 1);
            String pinyinName = BaiduTransUtil.Hanzi2Pinyin(hanziName);
            src = src.replace(replaceStr, pinyinName);
            matcher.reset(src);
        }
        return src;
    }
}
