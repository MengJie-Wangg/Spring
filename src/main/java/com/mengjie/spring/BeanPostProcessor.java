package com.mengjie.spring;

/**
 * @BelongsProject: SpringDemo
 * @BelongsPackage: com.mengjie.spring
 * @Author: 王梦杰
 * @CreateTime: 2023-11-06  15:59
 * @Description: TODO
 * @Version: 1.0
 */
public interface BeanPostProcessor {
    default Object postProcessorBeforeInitialization(Object bean,String beanName){
        return null;
    };
    default Object postProcessorAfterInitialization(Object bean,String beanName){
        return null;
    };
}
