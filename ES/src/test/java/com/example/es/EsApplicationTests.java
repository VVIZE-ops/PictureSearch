package com.example.es;

import cn.hutool.json.JSON;


import cn.hutool.json.JSONArray;
import com.example.es.clients.EsRestUtils;
import com.example.es.service.AIServicel;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
class EsApplicationTests {

    EsRestUtils esRestUtils = new EsRestUtils();
    @Autowired
    AIServicel aiServicel;

    @Test
    void contextLoads() {
    }

    @Test
    protected  void testGetTermsAgg2() {
        EsRestUtils esRestUtils = new EsRestUtils();
        //构建query条件
        SearchRequest searchRequest = new SearchRequest("zlw_test");
        QueryBuilder queryBuilder = new MatchAllQueryBuilder();
        //query条件作为查询条件,size表示返回结果的条数
        SearchSourceBuilder builder = new SearchSourceBuilder().query(queryBuilder).size(10);
        searchRequest.source(builder).searchType(SearchType.DEFAULT);
        String index = "zlw_test";
        try {
          long total  = esRestUtils.count(queryBuilder, index);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    protected  void testGetTermsAgg1() {
        EsRestUtils esRestUtils = new EsRestUtils();
        //构建query条件
        SearchRequest searchRequest = new SearchRequest("test");
        QueryBuilder queryBuilder = new MatchAllQueryBuilder();
        //query条件作为查询条件,size表示返回结果的条数
        SearchSourceBuilder builder = new SearchSourceBuilder().query(queryBuilder).size(10);
        searchRequest.source(builder).searchType(SearchType.DEFAULT);
        String index = "my-index";
//        try {
//            Object res  = esRestUtils.add(index);
//            System.out.println(res);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    @Test
    protected void aitest(){


        String mainBody = "F:\\python\\AlexNet\\sunflower.jpg";
        String url = "http://127.0.0.1:5000/api/getvector";

        Map vector = aiServicel.getVector(mainBody, url);
//        String index = "picture-search-test";
        String index = "zlw-picture-search";
        List vecs = Collections.singletonList(vector.get("res"));

        List tes = new ArrayList();
        for(int i=0;i<5;i++){
            tes.add(1.11);
        }

        try {
            Object res  = esRestUtils.add(index,vecs,mainBody);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    protected void query(){
        String index = "picture-search-11";
        float feature[] = new float[5];
        for(int i = 0;i<feature.length;i++){
            feature[i] = i;
        }
        List<Map<String, Object>> byVector = new ArrayList<>();
        try {
            byVector = esRestUtils.getByVector(index, feature);
        }catch (Exception e){
            log.warn("query es error : {}",e.getMessage(),e);
        }
        for(int i = 0;i<byVector.size();i++){
            log.info(byVector.get(i).toString());
        }
    }
    @Test
    protected void pictureQuery(){
        String mainBody = "F:\\python\\AlexNet\\sunflower.jpg";
        String url = "http://127.0.0.1:5000/api/getvector";

        Map vector = aiServicel.getVector(mainBody, url);
//        String index = "picture-search-test";
        String index = "zlw-picture-search";
//        String index = "picture-search-11";
        List vecs = Collections.singletonList(vector.get("res"));
        JSONArray r = new JSONArray();
        for(int i = 0;i<5;i++){
            r.add(1.235);
        }
        try {
             esRestUtils.pictureQuery((JSONArray) ((JSONArray) vecs.get(0)).get(0),index);
//            esRestUtils.pictureQuery(r,index);
        }catch (Exception e){
            log.warn("query es error : {}",e.getMessage(),e);
        }

    }

            }
