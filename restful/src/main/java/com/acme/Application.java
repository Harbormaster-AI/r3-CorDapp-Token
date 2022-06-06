/*******************************************************************************
  Oldsmobile Motor Corporation Confidential
  
  2018 Oldsmobile Motor Corporation
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Oldsmobile Motor Corporation - General Release
 ******************************************************************************/
package com.acme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;	
@SpringBootApplication
@ComponentScan(basePackages = "com.acme.*")
@EnableJpaRepositories(basePackages = "com.acme.repository")
public class Application {

    public static void main(String[] args) {
    	ApplicationContext context = SpringApplication.run(Application.class, args);
    	
		System.out.println( "=================================" );
		System.out.println( "Checking in ApplicationContext for discovered handler components:\n" );
        System.out.println( "- Contains chassis-handler = " + context.containsBeanDefinition("chassis-handler"));
        System.out.println( "- Contains body-handler = " + context.containsBeanDefinition("body-handler"));
        System.out.println( "- Contains engine-handler = " + context.containsBeanDefinition("engine-handler"));
        System.out.println( "- Contains transmission-handler = " + context.containsBeanDefinition("transmission-handler"));
        System.out.println( "- Contains braking-handler = " + context.containsBeanDefinition("braking-handler"));
        System.out.println( "- Contains interior-handler = " + context.containsBeanDefinition("interior-handler"));
        System.out.println( "=================================" );
    }
    
}