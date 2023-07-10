package com.lllebin.controller;

import com.lllebin.domain.ShoppingCart;
import com.lllebin.response.CommonResponse;
import com.lllebin.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: ShoppingCartController
 * Package: com.lllebin.controller
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public CommonResponse<List<ShoppingCart>> list() {
        log.info("查询所有购物车菜品");
        List<ShoppingCart> shoppingCartList = shoppingCartService.listByUserId();
        return CommonResponse.success(shoppingCartList);
    }

    @PostMapping("/add")
    public CommonResponse<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info("购物车添加菜品, {}", shoppingCart);
        ShoppingCart newItem = shoppingCartService.add(shoppingCart);
        return CommonResponse.success(newItem);
    }

    @PostMapping("/sub")
    public CommonResponse<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        log.info("购物车减少菜品, {}", shoppingCart);
        ShoppingCart newItem = shoppingCartService.sub(shoppingCart);
        return CommonResponse.success(newItem);
    }

    @DeleteMapping("/clean")
    public CommonResponse<String> delete() {
        log.info("购物车清空菜品");
        shoppingCartService.deleteByUserId();
        return CommonResponse.success("清空购物车成功");
    }

}
