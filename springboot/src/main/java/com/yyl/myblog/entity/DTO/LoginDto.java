package com.yyl.myblog.entity.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

//封装前端过来的数据
@Data
public class LoginDto implements Serializable {

    @NotBlank(message = "昵称不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
