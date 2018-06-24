package com.taotao.rest.service;

import com.taotao.common.utils.TaotaoResult;

/**
 * Created by hao on 2018/6/24.
 */
public interface RedisService {

    TaotaoResult syncContent(long contentCid);
}
