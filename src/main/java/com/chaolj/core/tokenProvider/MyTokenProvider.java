package com.chaolj.core.tokenProvider;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.TypeReference;
import org.springframework.context.ApplicationContext;
import com.chaolj.core.MyApp;
import com.chaolj.core.commonUtils.myDto.DataResultDto;
import com.chaolj.core.commonUtils.myServer.Interface.ITokenServer;
import com.chaolj.core.commonUtils.myServer.Models.TokenContextDto;

import java.util.List;

public class MyTokenProvider implements ITokenServer {
    private ApplicationContext applicationContext;
    private MyTokenProviderProperties properties;

    public MyTokenProvider(ApplicationContext applicationContext, MyTokenProviderProperties properties) {
        this.applicationContext = applicationContext;
        this.properties = properties;
    }

    private String getHost() {
        return this.properties.getServerHostUrl();
    }

    public String EncryptToken(String client, String uname, Integer days) {
        return MyApp.Helper().EncryptHelper().EncryptToken(client, uname, days, this.properties.getEncryptKey());
    }

    public List<String> DecryptToken(String tokenValue, boolean oneday) {
        return MyApp.Helper().EncryptHelper().DecryptToken(tokenValue, oneday, this.properties.getEncryptKey());
    }

    public DataResultDto<String> GetToken(String client, String username, String password) {
        var myresult = DataResultDto.<String>Empty();

        if (StrUtil.isBlank(client)) {
            myresult.setResult(false);
            myresult.setMessage("TokenServer.GetToken，处理失败！client为空！");
            myresult.setData("");

            return myresult;
        }

        if (StrUtil.isBlank(username)) {
            myresult.setResult(false);
            myresult.setMessage("TokenServer.GetToken，处理失败！username为空！");
            myresult.setData("");

            return myresult;
        }

        var url = this.getHost() + "/api/apptoken/GetToken/";
        try {
            myresult = MyApp.Http().url(url)
                    .buildGet()
                    .query("client", client)
                    .query("username", username)
                    .query("password", password)
                    .execute().toJavaObject(new TypeReference<>(){});
        } catch (Exception ex) {
            myresult.setResult(false);
            myresult.setMessage("TokenServer.GetToken，处理失败！" + ex.getMessage());
            myresult.setData("");
        }

        return myresult;
    }

    public DataResultDto<TokenContextDto> GetTokenContext(String ssotoken, String appno) {
        var myresult = DataResultDto.<TokenContextDto>Empty();

        if (StrUtil.isBlank(ssotoken)) {
            myresult.setResult(false);
            myresult.setMessage("TokenServer.GetTokenContext，处理失败！ssotoken为空！");
            myresult.setData(TokenContextDto.getDefault());

            return myresult;
        }

        var url = this.getHost() + "/api/apptoken/gettokencontext/";
        try {
            myresult = MyApp.Http().url(url)
                    .buildGet().query("ssotoken", ssotoken).query("appno", appno)
                    .execute().toJavaObject(new TypeReference<>(){});
        } catch (Exception ex) {
            myresult.setResult(false);
            myresult.setMessage("TokenServer.GetTokenContext，处理失败！" + ex.getMessage());
            myresult.setData(TokenContextDto.getDefault());
        }

        return myresult;
    }

    public DataResultDto<TokenContextDto> GetTokenContextByUsername(String client, String username, String appno) {
        if (StrUtil.isBlank(username)) {
            var myresult = DataResultDto.<TokenContextDto>Empty();
            myresult.setResult(false);
            myresult.setMessage("TokenServer.GetTokenContextByUsername，处理失败！username为空！");
            myresult.setData(TokenContextDto.getDefault());

            return myresult;
        }

        var ssotoken = this.EncryptToken(client, username);
        return this.GetTokenContext(ssotoken, appno);
    }
}
