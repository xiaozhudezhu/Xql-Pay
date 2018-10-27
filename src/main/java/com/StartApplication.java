package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.swinginwind.xql.pay.config.StartupRunner;
import com.swinginwind.xql.pay.config.TaskRunner;

@SpringBootApplication
@MapperScan("com.swinginwind.xql.pay.mapper")
public class StartApplication {

	public static void main(String[] args) {
		SpringApplication.run(StartApplication.class, args);
//		SpringApplication springApplication = new SpringApplication(DemoApplication.class);
//		//禁止命令行设置参数
//		springApplication.setAddCommandLineProperties(false);
//		springApplication.run(args);
	}
	
	 @Bean
     public StartupRunner startupRunner(){
         return new StartupRunner();
     }

     @Bean
     public TaskRunner taskRunner(){
         return new TaskRunner();
     }
}
