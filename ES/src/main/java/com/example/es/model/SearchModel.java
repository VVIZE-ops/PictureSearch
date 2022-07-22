package com.example.es.model;

import lombok.Data;

import java.util.Map;

@Data
public class SearchModel {

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 索引
     */
    private String index;

    /**
     * 排序字段
     */
    private Map<String,Integer> sortField;



}
