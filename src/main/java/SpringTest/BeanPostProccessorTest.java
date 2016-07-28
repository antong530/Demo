package SpringTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

import java.beans.PropertyDescriptor;

/**
 * Created by antong on 16/7/28.
 */

/**
 * BeanPostProcessor   初始化bean的前后回调

 InstantiationBeanPostProccessor   实例化bean的前后回调

 @postConstruct  和@preDestory分别对应着bean的init-method和destory-method的属性

 调用顺序为
         postProcessBeforeInstantiation
 postProcessAfterInstantiation
 postProcessBeforeInitialization
 init-method(@postContruct)
 postProcessAfterInitialization
 */
public class BeanPostProccessorTest implements BeanPostProcessor,InstantiationAwareBeanPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(BeanPostProccessorTest.class);
    @Override
    public Object postProcessBeforeInstantiation(Class<?> aClass, String s) throws BeansException {
        LOGGER.info("==========>postProcessBeforeInstantiation");
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object o, String s) throws BeansException {
        LOGGER.info("==========>postProcessAfterInstantiation");
        return false;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues propertyValues, PropertyDescriptor[] propertyDescriptors, Object o, String s) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        LOGGER.info("==========>postProcessBeforeInitialization");
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        LOGGER.info("==========>postProcessAfterInitialization");
        return null;
    }
}
