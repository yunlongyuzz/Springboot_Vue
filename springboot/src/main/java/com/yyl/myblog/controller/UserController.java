package com.yyl.myblog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yyl.myblog.entity.User;
import com.yyl.myblog.service.UserService;
import com.yyl.myblog.util.ResultEntity;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Wrapper;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 余云龙
 * @since 2022-01-19
 */
@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/user/save")
    public ResultEntity<User> save(@Validated @RequestBody User user) {
        return ResultEntity.successWithData(user);
    }


}
