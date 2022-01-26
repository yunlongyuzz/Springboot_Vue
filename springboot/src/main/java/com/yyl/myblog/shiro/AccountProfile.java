package com.yyl.myblog.shiro;

import lombok.Data;


import java.io.Serializable;

//不对外公开的信息
@Data
public class AccountProfile implements Serializable {

    private Long id;

    private String username;

    private String avatar;

    private String email;

}
