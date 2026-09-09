package kr.gsm.minibank.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Component
public class BeanPrinter implements CommandLineRunner {
    private final ApplicationContext applicationContext;
    public BeanPrinter(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    @Override
    public void run(String... args) throws Exception{
        System.out.println("--- 등록된 Account 관련 Bean 목록");
        Arrays.stream(applicationContext.getBeanDefinitionNames())
                .filter(name->name.toLowerCase().contains("account"))
                .forEach(System.out::println);
    }
}
