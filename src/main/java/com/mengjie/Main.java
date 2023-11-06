package com.mengjie;

import com.mengjie.service.UserService;
import com.mengjie.service.UserServiceImpl;
import com.mengjie.spring.MengjieApplicationContext;

/**
 * @BelongsProject: SpringDemo
 * @BelongsPackage: com.mengjie
 * @Author: 王梦杰
 * @CreateTime: 2023-11-06  14:28
 * @Description: TODO
 * @Version: 1.0
 */
public class Main {
    public static void main(String[] args) {
        try {
            MengjieApplicationContext applicationContext = new MengjieApplicationContext(AppConfig.class);
            UserService userService = (UserService)applicationContext.getBean("userService");
            userService.test();//1、先执行代理对象 2、再执行业务对象

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
