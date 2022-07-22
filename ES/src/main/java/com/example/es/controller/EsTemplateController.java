package com.example.es.controller;


import com.example.es.clients.EsRestUtils;
import com.example.es.dao.EsTemplateDto;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/template")
@Slf4j
public class EsTemplateController {

    @Autowired
    private EsRestUtils esRestUtils;


    private RestHighLevelClient client;


    /**
     * es模板对象
     *
     * @param templateid 模板名称
     * @param esTemplateDto 模板对象
     * @return 创建模板结果
     */
    @PostMapping("/createEsTemplate/{templateid}")
    public String createEsTemplate(@PathVariable String templateid, @RequestBody EsTemplateDto esTemplateDto){
        client = esRestUtils.getClient();
        Request scriptRequest = new Request("POST", "_scripts/"+templateid);

        String templateJsonString = esTemplateDto.getTemplateJson().toString();
        scriptRequest.setJsonEntity(templateJsonString);

        RestClient restClient = client.getLowLevelClient();

        try {
            Response response = restClient.performRequest(scriptRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "创建模板成功";

    }







}
