package com.mengjie.spring;


import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @BelongsProject: SpringDemo
 * @BelongsPackage: com.mengjie.spring
 * @Author: 王梦杰
 * @CreateTime: 2023-11-06  14:29
 * @Description: TODO
 * @Version: 1.0
 */
public class MengjieApplicationContext {
    private Class configClass;
    private ConcurrentHashMap<String,Object> singletonObjects = new ConcurrentHashMap<>();//单例池
    private ConcurrentHashMap<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();//beanDefinitionMap
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();//beanPostProcessorList
    public MengjieApplicationContext(Class configClass) {
        this.configClass = configClass;

        //1、解析配置类
        //ComponentScan注解------>扫描路径------->扫描------->BeanDefinition------->beanDefinitionMap

        //扫描逻辑
        scan(this.configClass);
        //创建bean对象
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getScope().equals("singleton")){
                Object bean = createBean(beanName,beanDefinition); //单例bean
                singletonObjects.put(beanName,bean);
            }
        }
    }
    public Object createBean(String beanName,BeanDefinition beanDefinition){
        Class clazz = beanDefinition.getClazz();
        try {
            Object instance = clazz.getConstructor().newInstance();
            //依赖注入
            for (Field declaredField : clazz.getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(Autowired.class)){
                    Object bean = getBean(declaredField.getName());
                    declaredField.setAccessible(true);
                    declaredField.set(instance,bean);
                }
            }
            //Aware回调
            if (instance instanceof BeanNameAware){
                ((BeanNameAware)instance).setBeanName(beanName);
            }
            //初始化前
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessorBeforeInitialization(instance, beanName);
            }
            //初始化
            if (instance instanceof InitializingBean){
                ((InitializingBean)instance).afterPropertiesSet();
            }
            //初始化后
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessorAfterInitialization(instance, beanName);
            }

            return instance;

        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void scan(Class configClass) {
        //获取ComponentScan注解
        ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
        //拿到扫包的路径
        String path = componentScanAnnotation.value();
        path = path.replace(".","/");
        //扫描
        ClassLoader classLoader = MengjieApplicationContext.class.getClassLoader();
        URL resource = classLoader.getResource(path);
        File file = new File(resource.getFile());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            //遍历所有.class文件
            for (File f : files) {
                String fileName = f.getAbsolutePath();
                if (fileName.endsWith(".class")){
                    String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                    className = className.replace("\\",".");
                    Class<?> aClass = null;
                    try {
                        aClass = classLoader.loadClass(className);
                        //判断是否存在Component注解
                        if (aClass.isAnnotationPresent(Component.class)){
                            //判断是否实现了BeanPostProcessor接口
                            if (BeanPostProcessor.class.isAssignableFrom(aClass)) {
                                BeanPostProcessor instance = (BeanPostProcessor)aClass.getDeclaredConstructor().newInstance();
                                beanPostProcessorList.add(instance);
                            }

                            //表示当前类是一个bean
                            //解析类，判断当前bean是单例bean还是prototype的bean
                            // BeanDefinition
                            Component componentAnnotation = aClass.getDeclaredAnnotation(Component.class);
                            String beanName = componentAnnotation.value();

                            //创建bean对象
                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setClazz(aClass);
                            if (aClass.isAnnotationPresent(Scope.class)){
                                //使用了Scope注解的bean说明是原型bean
                                Scope scopeAnnotation = aClass.getDeclaredAnnotation(Scope.class);
                                beanDefinition.setScope(scopeAnnotation.value());
                            }else{
                                //没有使用说明是单例bean
                                beanDefinition.setScope("singleton");
                            }
                            beanDefinitionMap.put(beanName,beanDefinition);

                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }

                }

            }
        }
    }

    public Object getBean(String beanName) throws Exception {
        //先判断beanDefinitionMap中是否存在bean
        if (beanDefinitionMap.containsKey(beanName)){
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            //判断是否是单例bean
            if (beanDefinition.getScope().equals("singleton")){
                //是单例bean直接从单例beanMap中取出返回
                Object o = singletonObjects.get(beanName);
                return o;
            }else{
                //不是单例bean的话，创建一个新的bean对象出来
                Object bean = createBean(beanName,beanDefinition);
                return bean;
            }
        }else{
            throw new Exception("不存在对应的bean");
        }
    }
}
