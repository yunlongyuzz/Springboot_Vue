package com.yyl.myblog.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yyl.myblog.entity.DTO.LoginDto;
import com.yyl.myblog.entity.User;
import com.yyl.myblog.service.UserService;
import com.yyl.myblog.util.JwtUtils;
import com.yyl.myblog.util.ResultEntity;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class AccountController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;


    @PostMapping("/login")
    public ResultEntity login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {

        //查找出这个username的用户
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        QueryWrapper<User> username = wrapper.eq("username", loginDto.getUsername());
        User user = userService.getOne(username);

        Assert.notNull(user, "用户不存在");

        if(!user.getPassword().equals(loginDto.getPassword())){
            return ResultEntity.failed("密码不正确");
        }

        //生成token，放入请求头
        String jwt = jwtUtils.generateToken(user.getId());
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-control-Expose-Headers", "Authorization");

        return ResultEntity.successWithData(MapUtil.builder()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .put("avatar", user.getAvatar())
                .put("email", user.getEmail())
                .map()
        );
    }


    @RequiresAuthentication
    @GetMapping("/logout")
    public ResultEntity logout() {
        //交给shiro退出
        SecurityUtils.getSubject().logout();
        return ResultEntity.successWithoutData();
    }

}
