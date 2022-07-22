package com.example.es.controller;


import com.example.es.service.QueryService;
import com.example.es.model.SearchModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@Controller
@RequestMapping("/user")
@CrossOrigin
public class QueryController {

    @Autowired
    QueryService queryService;
    /**
     * 查询
     * @param
     * @return
     */

//    @ApiImplicitParam(name = "keys", value = "关键词", required = true, dataType = "String", paramType = "query")
    @GetMapping("/findByKeys.json")
    public Object qiery(@RequestParam(name = "keys",required = false)SearchModel searchModel, @RequestParam MultipartFile kewordFile){

        return queryService.query(searchModel);
    }


}
