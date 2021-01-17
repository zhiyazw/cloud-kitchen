package com.css.cloudkitchen.controller;

import com.css.cloudkitchen.common.result.Result;
import com.css.cloudkitchen.common.result.ResultBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Reserved for future use. Since currently kitchen instantly cook upon receiving an order
@Deprecated

@RestController
@RequestMapping("/v1/kitchen")
public class KitchenController {
    @GetMapping("/cook/{id}")
    public Result cook(@RequestParam String id) {
        return ResultBuilder.error("Not implement");
    }

    @GetMapping("/finish/{id}")
    public Result finish(@RequestParam String id) {
        return ResultBuilder.error("Not implement");
    }
}
