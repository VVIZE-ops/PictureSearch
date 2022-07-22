package com.example.es.clients;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.example.es.config.CommonConfig;
import com.klibisz.elastiknn.ElastiknnNearestNeighborsQueryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EsRestUtils {

    @Autowired
    private CommonConfig commonConfig;

    @Value("${common.hosts:123.xom}")
    private String hosts;


    private RestHighLevelClient client;


    private static final String type = "_doc";

    /**
     *
     * @return
     */
    public RestHighLevelClient getClient() {
        if (client == null) {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials("", ""));  //es账号密码
            client = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost(hosts, 9600, "http"))
                            .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                                @Override
                                public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                                    httpClientBuilder.disableAuthCaching();
                                    return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                                }
                            }));
        }
        return client;
    }


    /**
     *
     * @param index
     * @param ids
     * @return
     * @throws IOException
     */
    protected List<Map<String, Object>> getByIds(String index, List<String> ids) throws IOException {
        getClient();
        List<Map<String, Object>> results = new ArrayList<>();
        MultiGetRequest request = new MultiGetRequest();
        ids.stream().forEach(id -> {
            request.add(new MultiGetRequest.Item(index, type, id));
        });
        MultiGetResponse response = client.mget(request, RequestOptions.DEFAULT);
        GetResponse getResponse;
        for (int i = 0; i < response.getResponses().length; i++) {
            getResponse = response.getResponses()[i].getResponse();
            if (getResponse.isExists()) {
                results.add(getResponse.getSourceAsMap());
            }
        }
        return results;
    }



    /**
     *
     * @param index
     * @param feature
     * @return
     * @throws IOException
     */
    public List<Map<String, Object>> getByVector(String index, float feature[]) throws IOException {
        getClient();
        List<Map<String, Object>> results = new ArrayList<>();



        //        MultiGetRequest request = new MultiGetRequest();
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        searchRequest.indices(index);
//        sourceBuilder.query(matchQueryBuilder);
        searchRequest.source(sourceBuilder);
        SearchResponse response = client.search(searchRequest,RequestOptions.DEFAULT);



//        MultiGetResponse response = client.mget(request, RequestOptions.DEFAULT);
//        GetResponse getResponse;
//        for (int i = 0; i < response.getResponses().length; i++) {
//            getResponse = response.getResponses()[i].getResponse();
//            if (getResponse.isExists()) {
//                results.add(getResponse.getSourceAsMap());
//            }
//        }
        return results;
    }

    /**
     *
     * @param index
     * @param queryBuilder
     * @param pageNo
     * @param pageSize
     * @return
     * @throws IOException
     */
    public  List<Map<String, Object>> getByWhere(String index, QueryBuilder queryBuilder, int pageNo, int pageSize) throws IOException {
        getClient();
        List<Map<String, Object>> results = new ArrayList<>();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from(pageNo);
        searchSourceBuilder.size(pageSize);

        SearchRequest searchRequest = new SearchRequest(index).types(type);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            results.add(hit.getSourceAsMap());
        }
        return results;
    }

    /**
     *
     * @param queryBuilder
     * @param indexs
     * @return
     * @throws IOException
     */
    public long count(QueryBuilder queryBuilder, String indexs) throws IOException {
        getClient();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);

        CountRequest countRequest = new CountRequest(indexs);
        countRequest.source(searchSourceBuilder);

        CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);
        long count = countResponse.getCount();
        return count;
    }

    public Object add(String index,List vecs,String file_path){
        getClient();

        float feature[] = new float[512];
        JSONArray resJsonArray = (JSONArray) ((JSONArray) vecs.get(0)).get(0);
        for (int i = 0; i < resJsonArray.size();i++){
            feature[i] = Float.parseFloat(resJsonArray.get(i).toString());
        }

        Map<String, Object> res = new HashMap<>();
//        res.put("String_fields",file_path);
        res.put("filePath",file_path);
        res.put("featureVector",feature);
        JSONObject jsonObject = new JSONObject(res);

        IndexRequest indexRequest = new IndexRequest(index);

        indexRequest.source(res);
        IndexResponse index1 = null;
        try {
            index1 = client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return index1;
    }


    public Object pictureQuery(JSONArray feature,String index){
        StrategyBuilder strategyBuilder = new StrategyBuilder();
        ElastiknnNearestNeighborsQueryBuilder builderOfFeature = strategyBuilder.getBuilderOfFeature(feature);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        QueryBuilder queryBuilder = boolQueryBuilder.must(builderOfFeature);

        getClient();
        SearchRequest searchRequest = new SearchRequest(index).types(type);
        searchRequest.routing("query: " + queryBuilder.toString());
        SearchResponse search = null;
        try{
            search = client.search(searchRequest, RequestOptions.DEFAULT);
        }catch (Exception e){
            log.error("query es error:{}",e.getMessage(),e);
        }finally {
            return search;
        }

    }



}




