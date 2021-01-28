package com.kiyotagbangers.springcommandline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringCommandlineApplication implements ApplicationRunner {

    @Value("${message.value}")
    private String message;

    private static Logger logger = LoggerFactory.getLogger(SpringCommandlineApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringCommandlineApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println(message);

        System.out.println("# OptionArgs: " + args.getOptionNames().size());
        System.out.println("OptionArgs:");

        args.getOptionNames().forEach(optionName -> {
            System.out.println(optionName + "=" + args.getOptionValues(optionName));
        });

        System.exit(0);
    }
}
