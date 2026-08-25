package kr.co.koscom.minibank.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class BeanPrinter implements CommandLineRunner {

    private final ApplicationContext context;

    // 생성자를 통한 DI (Constructor Injection)
    public BeanPrinter(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void run(String... args) {
        // Spring IoC 컨테이너에 등록된 Bean 중 'account'를 포함하는 것들만 출력
        System.out.println("--- 등록된 Account 관련 Bean 목록 ---");
        Arrays.stream(context.getBeanDefinitionNames())
                .filter(name -> name.toLowerCase().contains("account"))
                .forEach(System.out::println);
    }
}
