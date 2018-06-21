package com.taotao.rest.service;

import com.taotao.pojo.TbContent;

import java.util.List;

/**
 * Created by hao on 2018/6/20.
 */
public interface ContentService {
    List<TbContent> getContentList(long contentCid);
}
