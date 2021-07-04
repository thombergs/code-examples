package io.reflectoring.featureflags.web;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Component("userSession")
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSession implements Serializable {

    private static ThreadLocal<UserSession> threadLocalUserSession;

    public static void setThreadLocalSession(UserSession userSession){
        threadLocalUserSession.set(userSession);
    }

    public static UserSession get(){
        return threadLocalUserSession.get();
    }

    private Map<String, Boolean> userClicked = new HashMap<>();
    private String username;

    public void login(String username){
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }

    public boolean hasClicked(){
        if(!userClicked.containsKey(this.username)){
            return false;
        }
        return userClicked.get(username);
    }

    public void recordClick(){
        userClicked.put(this.username, Boolean.TRUE);
    }


}
