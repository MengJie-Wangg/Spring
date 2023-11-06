package com.mengjie.spring;

/**
 * @BelongsProject: SpringDemo
 * @BelongsPackage: com.mengjie.spring
 * @Author: 王梦杰
 * @CreateTime: 2023-11-06  15:52
 * @Description: TODO
 * @Version: 1.0
 */
public interface InitializingBean {
    void afterPropertiesSet() throws Exception;
}
