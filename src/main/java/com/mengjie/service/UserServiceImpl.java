package com.mengjie.service;

import com.mengjie.spring.*;

/**
 * @BelongsProject: SpringDemo
 * @BelongsPackage: com.mengjie.service
 * @Author: 王梦杰
 * @CreateTime: 2023-11-06  14:34
 * @Description: TODO
 * @Version: 1.0
 */
@Component("userService")
//@Scope("prototype")
public class UserServiceImpl implements UserService,BeanNameAware,InitializingBean {
    @Autowired
    private OrderService orderService;
    private String beanName;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public void test(){
        System.out.println(orderService);
        System.out.println(beanName);
        System.out.println(name);
    }

    @Override
    public void setBeanName(String name) {
        beanName = name;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("初始化");
    }


}
