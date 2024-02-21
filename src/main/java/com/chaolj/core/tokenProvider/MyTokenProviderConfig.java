package com.chaolj.core.tokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.chaolj.core.commonUtils.myServer.Interface.ITokenServer;

@Configuration
@EnableConfigurationProperties(MyTokenProviderProperties.class)
public class MyTokenProviderConfig {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    MyTokenProviderProperties myTokenProviderProperties;

    @Bean(name = "myTokenProvider")
    public ITokenServer MyTokenProvider(){
        return new MyTokenProvider(this.applicationContext, this.myTokenProviderProperties);
    }
}
