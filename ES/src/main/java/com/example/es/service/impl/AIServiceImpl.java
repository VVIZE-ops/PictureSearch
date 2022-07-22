package com.example.es.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.es.service.AIServicel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.*;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class AIServiceImpl implements AIServicel {


    private Integer queryTimeOut = 1500;




    private static final Integer OK_CODE = 200;
    private static final String CODE_FIELD = "code";

    @Override
    public Map getVector(String mainBody, String url) {
        JSONObject returnJson = JSONUtil.createObj();
        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(mainBody) ){
            return returnJson;
        }
        try{
            long t1 = System.currentTimeMillis();
            HttpRequest httpRequest = HttpUtil.createPost(url);
            JSONObject json = JSONUtil.createObj();
            json.put("file_path",mainBody);
            httpRequest.body(String.valueOf(json));
            httpRequest.timeout(queryTimeOut);
            HttpResponse response = httpRequest.execute();
            if(response.isOk()){
                String body = response.body();
                JSONObject jsonObject = JSONUtil.parseObj(body);
                if(OK_CODE.equals(jsonObject.getInt(CODE_FIELD))){
                    List vector = Collections.singletonList(jsonObject.get("vector"));
                    returnJson.put("res",vector);
                }

            }

        }catch (Exception e){
            log.warn("get verctor error :{}",e.getMessage(),e);
        }
        return returnJson;

    }
}
