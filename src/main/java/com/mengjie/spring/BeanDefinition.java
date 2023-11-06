package com.mengjie.spring;

/**
 * @BelongsProject: SpringDemo
 * @BelongsPackage: com.mengjie.spring
 * @Author: 王梦杰
 * @CreateTime: 2023-11-06  15:15
 * @Description: TODO
 * @Version: 1.0
 */
public class BeanDefinition {
    private Class clazz;
    private String scope;
    public BeanDefinition(){

    }

    public BeanDefinition(Class clazz, String scope) {
        this.clazz = clazz;
        this.scope = scope;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
