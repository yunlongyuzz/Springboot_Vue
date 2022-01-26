package com.yyl.myblog.util;

import com.yyl.myblog.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

public class ShiroUtil {

    public static AccountProfile getProfile() {

        //取出AccountRealm
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }

}
