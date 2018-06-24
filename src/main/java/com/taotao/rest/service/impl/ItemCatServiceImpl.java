package com.taotao.rest.service.impl;

import com.sun.org.apache.regexp.internal.RE;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.pojo.CatNode;
import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hao on 2018/6/19.
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper catMapper;
    @Autowired
    private JedisClient jedisClient;

    @Value("${INDEX_ITEMCAT_REDIS_KEY}")
    private String INDEX_ITEMCAT_REDIS_KEY;

    @Override
    public CatResult getItemCatList() {
        CatResult catResult = new CatResult();

        try{
            String result = jedisClient.hget(INDEX_ITEMCAT_REDIS_KEY, "itemCatList");
            if(!StringUtils.isBlank(result)){
                System.out.println("命中缓存");
                List<CatNode> resultList = JsonUtils.jsonToList(result,CatNode.class);
                catResult.setData(resultList);
                return catResult;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("未命中缓存");

        //查询分类列表
        List<?> itemCatList = getCatList(0);
        catResult.setData(itemCatList);

        try{
            String cacheString = JsonUtils.objectToJson(itemCatList);
            jedisClient.hset(INDEX_ITEMCAT_REDIS_KEY,"itemCatList",cacheString);
        }catch (Exception e){
            e.printStackTrace();
        }

        return catResult;
    }

    /**
     * 查询分类列表
     * @param parentId
     * @return
     */
    private List<?> getCatList(long parentId){
        //创建查询条件
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbItemCat> tbItemCats = catMapper.selectByExample(example);
        //返回值list
        List result = new ArrayList<>();
        //向list中添加节点
        int count = 0;
        for (TbItemCat tbItemCat:tbItemCats){
            //判断是否为叶子节点
            if(tbItemCat.getIsParent()){
                CatNode catNode = new CatNode();
                if(parentId == 0){
                    catNode.setName("<a href='/product/" + tbItemCat.getId() + ".html'>" + tbItemCat.getName() + "</a>");
                }else{
                    catNode.setName(tbItemCat.getName());
                }
                catNode.setUrl("/product/" + tbItemCat.getId() + ".html");
                catNode.setItem(getCatList(tbItemCat.getId()));
                result.add(catNode);
                count++;
                //第一层只取14条记录
                if(parentId == 0 && count > 13){
                    break;
                }
            }else{
                //如果是叶子节点
                result.add("/product/" + tbItemCat.getId() + ".html|" + tbItemCat.getName());
            }
        }
        return result;
    }
}
