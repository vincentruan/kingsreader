/*
 * Copyright [2018] [vincentruan]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.springtg.bot.service;

import com.github.springtg.bot.BeanDefinitionProperty;
import com.github.springtg.bot.ThreadPoolExecutorRejectionPolicy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.beans.Introspector;
import java.util.Collection;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * BeanDefinition是一个接口，用来描述一个Bean实例，例如是SINGLETON还是PROTOTYPE，属性的值是什么，构造函数的参数是什么等。
 * 简单来说，通过一个BeanDefinition我们就可以完成一个Bean实例化。 BeanDefinition及其主要的子类：
 * <p>
 * RootBeanDefinition和ChildBeanDefinition： 这2个BeanDefinition是相对的关系，自Spring 2.5 出来以后，已经被GenericBeanDefinition代替。因为这样强迫我们在编写代码的时候就必须知道他们之间的关系。
 * GenericBeanDefinition: 相比于RootBeanDefinition和ChildBeanDefinition在定义的时候就必须硬编码，GenericBeanDefinition的优点可以动态的为GenericBeanDefinition设置parent。
 * AnnotatedBeanDefinition：看名字就是知道是用来读取通过注解定义Bean。
 * </p>
 * @link https://www.cnblogs.com/lizo/p/6759080.html
 * @author vincentruan
 * @version 1.0.0
 */
@Component
@Slf4j
public class CustomRegisterBeanService {

    @Autowired
    private ApplicationContext applicationContext;

    public Object getAndRegisterBean(String beanName, String beanClassName) {
        Assert.hasText(beanClassName, "beanClassName must has text!");

        Class<?> beanClass;
        try {
            beanClass = Class.forName(beanClassName);
        } catch (ClassNotFoundException e) {
            throw new BeanCreationException("Class [" + beanClassName + "] not found!", e);
        }

        return getAndRegisterBean(beanName, beanClass, null);
    }

    public <T> T getAndRegisterBean(String beanName, Class<T> beanClass) {
        return getAndRegisterBean(beanName, beanClass, null);
    }

    public <T> T getAndRegisterBean(String beanName, Class<T> beanClass, Collection<BeanDefinitionProperty> beanDefinitionProperties) {
        Assert.notNull(beanClass, "beanClass must not null!");

        try {
            T bean = applicationContext.getBean(beanClass);
            if (null != bean) {
                return bean;
            }
        } catch (NoUniqueBeanDefinitionException e) {
            log.error("Bean[" + beanClass + "] not unique", e);
            throw e;
        } catch (NoSuchBeanDefinitionException e) {
            log.info("Bean[{}] not definition", beanClass);
        } catch (BeansException e) {
            log.info("Bean[" + beanClass + "] can not be found", e);
        }

        AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();

        //理论上这段代码执行不到，因为现在所有的bean生成都依赖DefaultListableBeanFactory
        if (!(autowireCapableBeanFactory instanceof DefaultListableBeanFactory)) {
            String error = "Error creating bean with beanClass '"
                    + beanClass + "' defined in class path Cannot convert value of type ["
                    + autowireCapableBeanFactory.getClass() + "] to required type ["
                    + DefaultListableBeanFactory.class + "]";
            throw new BeanCreationException(error);
        }

        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) autowireCapableBeanFactory;
        //1.创建bean
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClass);

        //2.设置属性
        if (!CollectionUtils.isEmpty(beanDefinitionProperties)) {
            for (BeanDefinitionProperty beanDefinitionProperty : beanDefinitionProperties) {
                if (null != beanDefinitionProperty) {
                    if (!StringUtils.hasText(beanDefinitionProperty.getPropertyName())) {
                        log.warn("Bean [{}] propertyName has not content, skip!", beanClass);
                        continue;
                    }

                    Object propertyValue = beanDefinitionProperty.getPropertyValue();
                    if (null != propertyValue) {
                        if (beanDefinitionProperty.isBeanRef()) {
                            if (!(propertyValue instanceof String)) {
                                log.error("Bean [{}] Property[{}], value[{}] had been defined as a reference bean ID, but its type cannot be converted!", beanClass, beanDefinitionProperty.getPropertyName(), propertyValue);
                                continue;
                            }

                            // 是bean引用且类型为class
                            if (beanDefinitionProperty.isClassType()) {
                                Class<?> propertyValueBeanClass;
                                try {
                                    propertyValueBeanClass = Class.forName((String) propertyValue);
                                } catch (ClassNotFoundException e) {
                                    throw new BeanCreationException("Class [" + propertyValue + "] not found!", e);
                                }
                                beanDefinitionBuilder.addPropertyValue(beanDefinitionProperty.getPropertyName(), applicationContext.getBean(propertyValueBeanClass));
                            } else {
                                beanDefinitionBuilder.addPropertyReference(beanDefinitionProperty.getPropertyName(), (String) propertyValue);
                            }

                        } else if (beanDefinitionProperty.isClassType()) {
                            if (propertyValue instanceof String) {
                                beanDefinitionBuilder.addPropertyValue(beanDefinitionProperty.getPropertyName(), getAndRegisterBean(null, (String) propertyValue));
                            } else if (propertyValue instanceof Class) {
                                beanDefinitionBuilder.addPropertyValue(beanDefinitionProperty.getPropertyName(), getAndRegisterBean(null, (Class<?>) propertyValue));
                            } else {
                                log.error("Bean [{}] Property[{}], value[{}] had been defined as a class type, but its type cannot be converted!", beanClass, beanDefinitionProperty.getPropertyName(), propertyValue);
                            }
                        } else {
                            beanDefinitionBuilder.addPropertyValue(beanDefinitionProperty.getPropertyName(), propertyValue);
                        }
                    }

                }
            }
        }

        if (!StringUtils.hasText(beanName)) {
            beanName = Introspector.decapitalize(beanClass.getSimpleName());
        }
        //3.注册到spring
        beanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
        return beanFactory.getBean(beanName, beanClass);
    }

    public RejectedExecutionHandler getRejectedExecutionHandlerBeanDefinition(String rejectionPolicy) {
        if (StringUtils.hasText(rejectionPolicy)) {
            ThreadPoolExecutorRejectionPolicy threadPoolExecutorRejectionPolicy = ThreadPoolExecutorRejectionPolicy.getRejectionPolicyByName(rejectionPolicy);
            try {
                if (null == threadPoolExecutorRejectionPolicy) {
                    // 自定义的信息
                    log.info("rejectionPolicy[{}] not defined in {}, search in bean definition list", rejectionPolicy, ThreadPoolExecutorRejectionPolicy.class);
                    return applicationContext.getBean(rejectionPolicy, RejectedExecutionHandler.class);
                }

                Class<? extends RejectedExecutionHandler> policyClass = threadPoolExecutorRejectionPolicy.getPolicyClass();
                return getAndRegisterBean(ThreadPoolExecutor.class.getSimpleName() + "$" + policyClass.getSimpleName(), policyClass);
            } catch (BeansException e) {
                log.warn("Failed to find RejectedExecutionHandler bean definition", e);
            }
        }

        return null;
    }

}
