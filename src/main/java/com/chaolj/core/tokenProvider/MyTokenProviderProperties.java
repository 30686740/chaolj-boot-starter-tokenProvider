package com.chaolj.core.tokenProvider;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "myproviders.mytokenprovider")
public class MyTokenProviderProperties {
    private String encryptKey = "POWERCN";
    private String defaultClientToken = "dev";
    private String serverHostUrl = "https://deverp.ztzs.cn/_global";
}
