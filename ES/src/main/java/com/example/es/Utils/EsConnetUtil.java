package com.example.es.Utils;


import javafx.beans.binding.ObjectExpression;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import javax.annotation.Resource;

public class EsConnetUtil {
    @Resource
    private TransportClient transportClient;

    public Object esConnet(String index, String startTime, String endTime){

        BoolQueryBuilder totalQuery = QueryBuilders.boolQuery();
        TransportClient esClient = transportClient;
        String key = "aaa";//保持相同查询条件查询结果一致查

        //设置时间范围
        totalQuery.filter(QueryBuilders.rangeQuery("captureTime").gte(startTime).lte(endTime));
        final SearchResponse[] searchResponses = {esClient.prepareSearch(index)
                .setSize(10).setPreference(key).setPostFilter(totalQuery)
                .execute().actionGet()};

        return searchResponses;

    }



}
