package com.umc.zipcock.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
@Api(tags = {"테스트용 API"})
public class HelloController {

    @GetMapping(value = "/hello")
    @ApiOperation(value = "HELLOW")
    public String hellow() {
        return "테스트용 API입니다.";
    }
}