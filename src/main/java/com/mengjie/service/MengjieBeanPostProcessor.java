package com.mengjie.service;

import com.mengjie.spring.BeanPostProcessor;
import com.mengjie.spring.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @BelongsProject: SpringDemo
 * @BelongsPackage: com.mengjie.service
 * @Author: 王梦杰
 * @CreateTime: 2023-11-06  16:05
 * @Description: TODO
 * @Version: 1.0
 */
@Component
public class MengjieBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessorBeforeInitialization(Object bean,String beanName) {
        System.out.println("初始化前");
        if (beanName.equals("userService")){
            ((UserServiceImpl)bean).setName("wmj");
        }
        return bean;
    }

    @Override
    public Object postProcessorAfterInitialization(Object bean,String beanName) {
        System.out.println("初始化后");

        if (beanName.equals("userService")){
            Object proxyInstance = Proxy.newProxyInstance(MengjieBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("代理逻辑");//找切点
                    return method.invoke(bean,args);
                }
            });
            return proxyInstance;
        }
        return bean;
    }
}
