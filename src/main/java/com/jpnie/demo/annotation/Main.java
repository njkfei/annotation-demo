package com.jpnie.demo.annotation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by njp on 18/5/3.
 */
public class Main {
    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("application-context.xml");
        DemoService demoService = (DemoService)ac.getBean(DemoService.class);

        demoService.test1(11);
        demoService.test2("hello annotaion");


    }
}
