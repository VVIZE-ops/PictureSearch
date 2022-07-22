package com.example.es.service;


import com.example.es.model.SearchModel;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

@Service
public class QueryService {

    public Object esConnet(String index, String startTime, String endTime){

        BoolQueryBuilder totalQuery = QueryBuilders.boolQuery();


        String key = "aaa";//保持相同查询条件查询结果一致查


        //设置时间范围
//        totalQuery.filter(QueryBuilders.rangeQuery("captureTime").gte(startTime).lte(endTime));
//        final SearchResponse[] searchResponses = {esClient.prepareSearch(index)
//                .setSize(10).setPreference(key).setPostFilter(totalQuery)
//                .execute().actionGet()};

//        return searchResponses;
        return "";
    }

    public Object query(SearchModel searchModel){
        Object result = esConnet(searchModel.getIndex(),searchModel.getStartTime(),searchModel.getEndTime());
        return result;
    }

}
