package com.taotao.rest.controller;

import com.taotao.common.utils.JsonUtils;
import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 商品分类列表
 * Created by hao on 2018/6/19.
 */
@Controller
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    //指定字符集为json,编码为utf-8
//    @RequestMapping(value = "/itemcat/list", produces = APPLICATION_JSON_VALUE + ";charset=utf-8")
//    @ResponseBody
//    public String getItemCatList(String callback){
//        CatResult itemCatList = itemCatService.getItemCatList();
//        //把pojo转换成字符串
//        String json = JsonUtils.objectToJson(itemCatList);
//        //拼装返回值
//        String result = callback + "(" + json + ");";
//        return result;
//    }

    @RequestMapping("/itemcat/list")
    @ResponseBody
    public Object getItemCatList(String callback){
        CatResult itemCatList = itemCatService.getItemCatList();
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(itemCatList);
        mappingJacksonValue.setJsonpFunction(callback);
        return mappingJacksonValue;
    }
}
