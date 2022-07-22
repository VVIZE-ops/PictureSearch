package com.example.es.service.impl;

import com.example.es.clients.EsRestUtils;
import com.example.es.service.IndexOperation;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class IndexOperationImpl implements IndexOperation {

    private RestHighLevelClient client;


    @Override
    public void batchInsert(String var1, String var2, List<Map<String, Object>> var3, BulkProcessor.Listener var4) {

    }
}
