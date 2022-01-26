package com.yyl.myblog.service.impl;

import com.yyl.myblog.entity.User;
import com.yyl.myblog.mapper.UserMapper;
import com.yyl.myblog.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 余云龙
 * @since 2022-01-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
