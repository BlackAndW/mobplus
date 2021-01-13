package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.controller.cms.form.TalkCategoryForm;
import com.yeecloud.adplus.admin.controller.cms.form.TalkContentForm;
import com.yeecloud.adplus.admin.controller.cms.form.TalkForm;
import com.yeecloud.adplus.admin.controller.cms.vo.TalkCategoryVO;
import com.yeecloud.adplus.admin.controller.cms.vo.TalkContentVO;
import com.yeecloud.adplus.admin.controller.cms.vo.TalkVO;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;

public interface TalkService {

    PageInfo<TalkCategoryVO> queryCategory(Query query) throws ServiceException;

    void deleteCategory(Integer[] ids) throws ServiceException;

    void createCategory(TalkCategoryForm form) throws ServiceException;

    void updateCategory(Integer id, TalkCategoryForm form) throws ServiceException;

    PageInfo<TalkVO> queryTalk(Query query) throws ServiceException;

    void createTalk(TalkForm form) throws ServiceException;

    void updateTalk(Integer id, TalkForm form) throws ServiceException;

    void deleteTalk(Integer[] ids) throws ServiceException;

    PageInfo<TalkContentVO> queryTalkContent(Query query) throws ServiceException;

    void createTalkContent(TalkContentForm form) throws ServiceException;

    void updateTalkContent(Integer id, TalkContentForm form) throws ServiceException;

    void deleteTalkContent(Integer[] ids);
}
