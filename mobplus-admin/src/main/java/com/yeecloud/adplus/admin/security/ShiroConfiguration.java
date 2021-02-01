package com.yeecloud.adplus.admin.security;

import com.yeecloud.adplus.admin.security.shiro.AjaxAuthFilter;
import com.yeecloud.adplus.admin.security.shiro.AjaxSessionManager;
import com.yeecloud.adplus.admin.security.shiro.CredentialsMatcher;
import com.yeecloud.adplus.admin.service.AuthService;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Title
 *
 * Date: 2019-11-09 01:49:51
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Configuration
public class ShiroConfiguration {

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);

        //配置访问权限
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/**/*.html", "anon");
        filterChainDefinitionMap.put("/**/*.jpg", "anon");
        filterChainDefinitionMap.put("/**/*.png", "anon");
        filterChainDefinitionMap.put("/**/*.js", "anon");
        filterChainDefinitionMap.put("/**/*.css", "anon");
        filterChainDefinitionMap.put("/**/*.webp", "anon");
        filterChainDefinitionMap.put("/**/*.ico", "anon");

        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/fonts/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");



        filterChainDefinitionMap.put("/v/**", "anon");
        filterChainDefinitionMap.put("/**/login", "anon");
        filterChainDefinitionMap.put("/**/logout", "anon");

        filterChainDefinitionMap.put("/actuator/**", "anon");



        filterChainDefinitionMap.put("/api/**", "ajaxFilter");

        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("ajaxFilter", new AjaxAuthFilter());
        //配置登录的url和登录成功的url
        bean.setLoginUrl("/login");
        bean.setSuccessUrl("/index");
        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        bean.setFilters(filterMap);
        return bean;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    //配置自定义的密码比较器
    @Bean(name = "credentialsMatcher")
    public CredentialsMatcher hashedCredentialsMatcher() {
        CredentialsMatcher credentialsMatcher = new CredentialsMatcher();
//        credentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
//        credentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
        return credentialsMatcher;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager manager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(manager);
        return advisor;
    }

    //配置核心安全事务管理器
    @Bean(name = "securityManager")
    public SecurityManager securityManager(@Qualifier("authRealm") AuthService authRealm, @Qualifier("sessionManager") SessionManager sessionManager) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(authRealm);
        manager.setSessionManager(sessionManager);
        return manager;
    }

    @Bean(name = "sessionManager")
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        // 设置session过期时间3600s
        sessionManager.setGlobalSessionTimeout(3600000L);
        return sessionManager;
    }

    //配置自定义的权限登录器
    @Bean(name = "authRealm")
    public AuthService authRealm(@Qualifier("credentialsMatcher") CredentialsMatcher matcher) {
        AuthService authRealm = new AuthService();
        authRealm.setCredentialsMatcher(matcher);
        return authRealm;
    }

    @Bean(name = "sessionManager")
    public SessionManager createSessionManager() {
        AjaxSessionManager sessionManager = new AjaxSessionManager();
        return sessionManager;
    }
}
