package com.yyl.myblog.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yyl.myblog.entity.Blog;
import com.yyl.myblog.service.BlogService;
import com.yyl.myblog.util.ResultEntity;
import com.yyl.myblog.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 前端控制器
 * </p>
 * <p>
 * 0@author 余云龙
 *
 * @since 2022-01-19
 */
@RestController
public class BlogController {

    @Autowired
    BlogService blogService;


    /**
     * 博客列表
     * @param currentPage
     * @return
     */
    @GetMapping("/blogs")
    public ResultEntity list(@RequestParam(defaultValue = "1") Integer currentPage) {

        Page page = new Page(currentPage, 5);

        IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));

        return ResultEntity.successWithData(pageData);
    }

    /**
     * 查看博客内容
     * @param id
     * @return
     */
    @GetMapping("/blog/{id}")
    public ResultEntity detail(@PathVariable(name = "id") Long id) {
        Blog blog = blogService.getById(id);
        Assert.notNull(blog, "该博客已被删除");

        return ResultEntity.successWithData(blog);
    }

    /**
     * 编辑和添加博客
     * 需要token
     * @param blog
     * @return
     */
    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public ResultEntity edit(@Validated @RequestBody Blog blog) {

        Blog BlogBean = null;
        //判断博客是否有id
        if (blog.getId() != null) {

            //有id，编辑状态
            BlogBean = blogService.getById(blog.getId());

            //看看当前博客的id是不是和当前登录的用户的id一样
            //如果不一样，就没有权限编辑
            Assert.isTrue(BlogBean.getUserId().equals(ShiroUtil.getProfile().getId()),"没有权限编辑");

        } else {

            //没有id，为新建博客
            BlogBean = new Blog();
            BlogBean.setUserId(ShiroUtil.getProfile().getId());
            BlogBean.setCreated(LocalDateTime.now());
            BlogBean.setStatus(0);

        }

        //传过来的内容拷贝到新的blogBean
        BeanUtil.copyProperties(blog, BlogBean, "id", "userId", "created", "status");
        //执行保存或者更新
        blogService.saveOrUpdate(BlogBean);

        return ResultEntity.successWithData("操作成功");
    }

    /**
     * 删除博客
     * @param Id
     * @return
     */
    @RequiresAuthentication
    @PostMapping("/blog/delete")
    public ResultEntity delete(@RequestBody String Id) {

        JSONObject obj = JSONUtil.parseObj(Id);
        Long BlogId = obj.getLong("id");

        //根据传过来的id查询到Blog表中的Blog
        Blog blog = blogService.getById(BlogId);

        System.out.println(blog.getUserId());
        System.out.println(ShiroUtil.getProfile().getId());

        //根据Blog查询里面的userId,如果userId和token中的id相同，执行此Blog删除
        if (blog.getUserId().equals(ShiroUtil.getProfile().getId())){

            UpdateWrapper<Blog> blogUpdateWrapper = new UpdateWrapper<>();
            UpdateWrapper<Blog> object = blogUpdateWrapper.eq("id", BlogId);

            blogService.remove(object);

            return ResultEntity.successWithoutData();
        }else {
            return ResultEntity.failed("没有权限删除");
        }



    }


}

