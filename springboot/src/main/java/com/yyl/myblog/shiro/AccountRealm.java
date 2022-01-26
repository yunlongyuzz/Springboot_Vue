package com.yyl.myblog.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.yyl.myblog.entity.User;
import com.yyl.myblog.service.UserService;
import com.yyl.myblog.util.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    /**
     * 看一下从JwtFilter传过来的AuthenticationToken是不是JwtToken
     * @param token
     * @return 如果是，返回true
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 角色的权限信息集合，授权时使用，这里没有角色功能
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /**
     * 经过jwtFilter的createToken之后转到这里的登录认证功能
     * @param token：经过jwtFilter的createToken方法返回的AuthenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {


        //token传过来只能保证没有为空和过期，但是不能保证token是对的，还要进一步进行验证
        JwtToken jwtToken = (JwtToken) token;

        //从token中解密，取出subject里面的userID
        Claims claim = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal());
        String userId = claim.getSubject();

        //从token里面解密sub，得到userid信息，看看这个userid是不是对应的user
        User user = userService.getById(Long.valueOf(userId));
        if (user == null) {
            throw new UnknownAccountException("账户不存在");
        }

        if (user.getStatus() == -1) {
            throw new LockedAccountException("账户已被锁定");
        }

        //如果是对应的user，把信息拷贝到这个类里面
        AccountProfile profile = new AccountProfile();
        BeanUtil.copyProperties(user, profile);

        //返回用户信息，token信息，和名字
        //执行登录
        return new SimpleAuthenticationInfo(profile, jwtToken.getPrincipal(), getName());
    }
}


