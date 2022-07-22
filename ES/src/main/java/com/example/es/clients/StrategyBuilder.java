package com.example.es.clients;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONArray;
import com.alibaba.fastjson.JSON;
import com.klibisz.elastiknn.ElastiknnNearestNeighborsQueryBuilder;
import com.klibisz.elastiknn.api4j.ElastiknnNearestNeighborsQuery;
import com.klibisz.elastiknn.api4j.Similarity;
import com.klibisz.elastiknn.api4j.Vector.DenseFloat;
import java.util.*;

/**
 * @Description:
 * @Author: Zhu Lianwei
 * @CreateDate: 2022/7/21 11:35
 * @Version: 0.0.1-SHAPSHOT
 **/
public class StrategyBuilder {

    public ElastiknnNearestNeighborsQueryBuilder getBuilderOfFeature(JSONArray feature){
        if(CollectionUtil.isEmpty(feature)){
            return null;
        }else {
            List<Float> lis = JSON.parseArray(feature.toString(),Float.class);
            Float[] floats = lis.toArray(new Float[0]);
            float[] floats1 = new float[floats.length];

            for(int i=0;i<floats.length;i++){
                floats1[i] = floats[i];
            }
            DenseFloat denseFloat = new DenseFloat(floats1);
            ElastiknnNearestNeighborsQuery query = new ElastiknnNearestNeighborsQuery.Exact(denseFloat, Similarity.COSINE);
            return new ElastiknnNearestNeighborsQueryBuilder(query, "featureVector");
        }
    }
}
