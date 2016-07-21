package SpringTest;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by antong on 16/1/21.
 */
public class SpringTest {
    public static void main(String[] args){
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("spring.xml");
        System.out.print(beanFactory.getBean("name"));
    }
}
