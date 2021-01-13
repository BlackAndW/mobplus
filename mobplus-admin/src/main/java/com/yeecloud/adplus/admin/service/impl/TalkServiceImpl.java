package com.yeecloud.adplus.admin.service.impl;

import com.apache.commons.beanutils.NewBeanUtils;
import com.google.common.collect.Lists;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.yeecloud.adplus.admin.controller.cms.convert.TalkCategoryConvert;
import com.yeecloud.adplus.admin.controller.cms.convert.TalkContentConvert;
import com.yeecloud.adplus.admin.controller.cms.convert.TalkConvert;
import com.yeecloud.adplus.admin.controller.cms.form.TalkCategoryForm;
import com.yeecloud.adplus.admin.controller.cms.form.TalkContentForm;
import com.yeecloud.adplus.admin.controller.cms.form.TalkForm;
import com.yeecloud.adplus.admin.controller.cms.vo.TalkCategoryVO;
import com.yeecloud.adplus.admin.controller.cms.vo.TalkContentVO;
import com.yeecloud.adplus.admin.controller.cms.vo.TalkVO;
import com.yeecloud.adplus.admin.service.TalkService;
import com.yeecloud.adplus.dal.entity.*;
import com.yeecloud.adplus.dal.repository.TalkCategoryRepository;
import com.yeecloud.adplus.dal.repository.TalkContentRepository;
import com.yeecloud.adplus.dal.repository.TalkRepository;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.util.PageInfo;
import com.yeecloud.meeto.common.util.Query;
import com.yeecloud.meeto.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class TalkServiceImpl implements TalkService {

    @Autowired
    private TalkCategoryRepository talkCategoryRepository;

    @Autowired
    private TalkRepository talkRepository;

    @Autowired
    private TalkContentRepository talkContentRepository;

    @Autowired
    private TalkCategoryConvert talkCategoryConvert;

    @Autowired
    private TalkConvert talkConvert;

    @Autowired
    private TalkContentConvert talkContentConvert;

    @Override
    public PageInfo<TalkCategoryVO> queryCategory(Query query) throws ServiceException {
        try {
            QTalkCategory talkCategory = QTalkCategory.talkCategory;
            Predicate predicate = talkCategory.deleted.eq(false);
            Integer status = query.get("status", Integer.class);
            if (status != null && status > 0) {
                predicate = ExpressionUtils.and(predicate, talkCategory.status.eq(status));
            }

            Integer parentId = (Integer) query.get("parentId");
            if (parentId == null) {
                predicate = ExpressionUtils.and(predicate, talkCategory.parentId.eq(0));
            } else if (parentId.equals(999)) {
                predicate = ExpressionUtils.and(predicate, talkCategory.parentId.ne(0));
            } else {
                predicate = ExpressionUtils.and(predicate, talkCategory.parentId.eq(parentId));
            }

            Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "sort"));
            PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
            Page<TalkCategory> list = talkCategoryRepository.findAll(predicate, pagRequest);
            return transform(list);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteCategory(Integer[] ids) throws ServiceException {
        // 删除父类时，会删除子类
        // TODO 删除子类时，子类下的话术内容暂时不删除
        for (Integer id : ids) {
            talkCategoryRepository.deleteByParentId(id);
            talkCategoryRepository.deleteById(id);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void createCategory(TalkCategoryForm form) throws ServiceException {
        try {
            TalkCategory entity = new TalkCategory();
            NewBeanUtils.copyProperties(entity, form, true);
            if (form.getParentId() == 0) {
                entity.setLevel(0);
            } else {
                entity.setLevel(1);
            }
            talkCategoryRepository.save(entity);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void updateCategory(Integer id, TalkCategoryForm form) throws ServiceException {
        try {
            TalkCategory entity = talkCategoryRepository.findById(id).orElse(null);
            if (entity != null && !entity.isDeleted()) {
                NewBeanUtils.copyProperties(entity, form, true);
                talkCategoryRepository.save(entity);
            } else {
                throw new ServiceException("修改失败，该类别不存在！");
            }
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public PageInfo<TalkVO> queryTalk(Query query) throws ServiceException {
        try {
            QTalk talk = QTalk.talk;
            Predicate predicate = talk.deleted.eq(false);

            String keyword = query.get("keyword", String.class);
            if (StringUtils.isNotBlank(keyword)) {
                String express = "%".concat(keyword).concat("%");
                predicate = ExpressionUtils.and(predicate, talk.title.like(express));
            }

            Integer categoryId = (Integer) query.get("categoryId", Integer.class);
            if (categoryId != null) {
                predicate = ExpressionUtils.and(predicate, talk.category.id.eq(categoryId));
            }

            Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));
            PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
            Page<Talk> list = talkRepository.findAll(predicate, pagRequest);
            return transformTalk(list);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void createTalk(TalkForm form) throws ServiceException {
        try {
            Talk entity = new Talk();
            NewBeanUtils.copyProperties(entity, form, true);
            talkRepository.save(entity);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void updateTalk(Integer id, TalkForm form) throws ServiceException {
        try {
            Talk entity = talkRepository.findById(id).orElse(null);
            if (entity == null || entity.isDeleted()) {
                throw new ServiceException("修改失败，该话术不存在！");
            }
            NewBeanUtils.copyProperties(entity, form, true);
            talkRepository.save(entity);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteTalk(Integer[] ids) throws ServiceException {
        talkRepository.deleteById(ids);
    }

    @Override
    public PageInfo<TalkContentVO> queryTalkContent(Query query) throws ServiceException {
        try {
            QTalkContent talkContent = QTalkContent.talkContent;
            Predicate predicate = talkContent.deleted.eq(false);

            Integer talkId = (Integer) query.get("talkId", Integer.class);
            if (talkId != null) {
                predicate = ExpressionUtils.and(predicate, talkContent.talk.id.eq(talkId));
            }

            Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "sort"));
            PageRequest pagRequest = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
            Page<TalkContent> list = talkContentRepository.findAll(predicate, pagRequest);
            return transformTalkContent(list);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void createTalkContent(TalkContentForm form) throws ServiceException {
        try {
            TalkContent entity = new TalkContent();
            NewBeanUtils.copyProperties(entity, form, true);
            talkContentRepository.save(entity);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void updateTalkContent(Integer id, TalkContentForm form) throws ServiceException {
        try {
            TalkContent entity = talkContentRepository.findById(id).orElse(null);
            if (entity == null || entity.isDeleted()) {
                throw new ServiceException("修改失败，该内容不存在！");
            }
            NewBeanUtils.copyProperties(entity, form, true);
            talkContentRepository.save(entity);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteTalkContent(Integer[] ids) {
        talkContentRepository.deleteById(ids);
    }

    private PageInfo<TalkCategoryVO> transform(Page<TalkCategory> result) {
        List<TalkCategoryVO> resultList = convert(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }

    private PageInfo<TalkVO> transformTalk(Page<Talk> result) {
        List<TalkVO> resultList = talkConvert.convert(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }

    private PageInfo<TalkContentVO> transformTalkContent(Page<TalkContent> result) {
        List<TalkContentVO> resultList = talkContentConvert.convert(result.getContent());
        return new PageInfo<>(result.getNumber() + 1, result.getSize(), (int) result.getTotalElements(), resultList);
    }

    private List<TalkCategoryVO> convert(List<TalkCategory> list) {
        List<TalkCategoryVO> resultList = Lists.newArrayList();
        for (TalkCategory entity : list) {
            if (entity.isDeleted()) {
                continue;
            }
            List<TalkCategoryVO> subList = convert(entity.getChildren());
            TalkCategoryVO vo = talkCategoryConvert.convert(entity);
            if (!subList.isEmpty()) {
                vo.setChildren(subList);
            } else {
                vo.setChildren(null);
            }
            resultList.add(vo);
        }
        return resultList;
    }


}
