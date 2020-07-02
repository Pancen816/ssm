package com.bruce.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "用户名为必填项，不可为空！")
    private String username;

    @NotBlank(message = "密码为必填项，不可为空！")
    private String password;

    private String salt;

    @NotBlank(message = "手机号为必填项，不可为空！")
    private String phone;

    private java.sql.Date birthday;

    private String email;

    private java.util.Date created;


}
