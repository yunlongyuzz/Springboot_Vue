package com.yyl.myblog.shiro;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import com.yyl.myblog.util.JwtUtils;
import com.yyl.myblog.util.ResultEntity;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * filter过滤器，所有请求都要经过这里
 * 这里我们继承的是Shiro内置的AuthenticatingFilter，认证filter
 * 还有其它很多的filter
 */
@Component
public class JwtFilter extends AuthenticatingFilter {

    @Autowired
    JwtUtils jwtUtils;

    /**
     * 看看有没有带token过来
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI = request.getRequestURI();
        String[] requestURIArray = {"/login"};

        //请求地址是不是包含在内，如果是，无需验证，如果不是，则需要验证
        if (ArrayUtil.contains(requestURIArray, requestURI)) {
            return true;

        }else {
            // 获取请求头中的token
            String jwt = request.getHeader("Authorization");

            //不为空，返回true，有token
            if (StringUtils.hasLength(jwt)) {

                Claims claim = jwtUtils.getClaimByToken(jwt);

                //如果校验token时候为空或者过期了
                if (claim == null || jwtUtils.isTokenExpired(claim.getExpiration())) {
                    throw new ExpiredCredentialsException("token已失效，请重新登录");
                }
                // 执行登录，去到createToken
                return executeLogin(servletRequest, servletResponse);

            } else {

                //没有token，放行
                return true;

            }
        }
    }


        /**
         * 有token，token验证成功了
         * @param servletRequest 请求
         * @param servletResponse 响应
         * @return 返回的是一个AuthenticationToken类型的token，jwt实现了AuthenticationToken这个接口
         * @throws Exception
         */
        @Override
        protected AuthenticationToken createToken (ServletRequest servletRequest, ServletResponse servletResponse) throws
        Exception {

            //获取这个请求的请求头
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String jwt = request.getHeader("Authorization");

            //不为空，返回true
            if (StringUtils.hasLength(jwt)) {
            //把带过来的这个token放到这个类里面，适应参数，进行存储，然后去到doGetAuthenticationInfo方法，进行登录判断
                return new JwtToken(jwt);

            }
            //token为空
            return null;
        }


        /**
         * 如果登录失败，进入这个方法
         * @param token
         * @param e
         * @param request
         * @param response
         * @return
         */
        @Override
        protected boolean onLoginFailure (AuthenticationToken token, AuthenticationException e, ServletRequest
        request, ServletResponse response){

            HttpServletResponse httpServletResponse = (HttpServletResponse) response;

            Throwable throwable = e.getCause() == null ? e : e.getCause();

            ResultEntity<Object> result = ResultEntity.failed(throwable.getMessage());
            String json = JSONUtil.toJsonStr(result);

            try {
                //把抛出异常的结果转化为json字符串的形式进行抛出
                httpServletResponse.getWriter().print(json);
            } catch (IOException ioException) {

            }
            return false;
        }


        //跨域问题
        @Override
        protected boolean preHandle (ServletRequest request, ServletResponse response) throws Exception {

            HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
            HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
            httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
            httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));

            // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
            if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
                httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
                return false;
            }

            return super.preHandle(request, response);
        }
    }
