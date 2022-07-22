package com.example.es.service;

import org.elasticsearch.action.bulk.BulkProcessor;

import javax.xml.bind.Unmarshaller;
import java.util.*;

public interface IndexOperation {

    void batchInsert(String var1, String var2, List<Map<String,Object>> var3, BulkProcessor.Listener var4);
}
