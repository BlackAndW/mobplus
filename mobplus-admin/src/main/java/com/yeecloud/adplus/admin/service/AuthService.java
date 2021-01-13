package com.yeecloud.adplus.admin.service;

import com.yeecloud.adplus.admin.Constants;
import com.yeecloud.adplus.admin.controller.sys.vo.SysUserVO;
import com.yeecloud.adplus.admin.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

/**
 * Title
 * <p>
 * Date: 2019-11-09 01:50:16
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Slf4j
public class AuthService extends AuthorizingRealm {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysPermissionService sysPermissionService;


    private final static String AUTHORIZE = "AUTHORIZE";

    /**
     * 授权
     *
     * @param principal
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        try {
            SimpleAuthorizationInfo info = (SimpleAuthorizationInfo) SecurityUtils.getSubject().getSession().getAttribute(AUTHORIZE);
            if (info == null) {
                UserPrincipal user = (UserPrincipal) super.getAvailablePrincipal(principal);
                info = new SimpleAuthorizationInfo();
                List<String> roles = sysRoleService.findCodesByUserId(user.getId());
                List<String> perms = sysPermissionService.findCodesByUserId(user.getId());
                info.addRoles(roles);
                info.addStringPermissions(perms);

                SecurityUtils.getSubject().getSession().setAttribute(AUTHORIZE, info);
            }
            return info;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 认证.登录
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        try {
            if (token == null) {
                throw new ServiceException("token失效，请重新登录");
            }
            UsernamePasswordToken userToken = (UsernamePasswordToken) token;//获取用户输入的token
            SysUserVO account = sysUserService.findByUserName(userToken.getUsername());
            if (account == null) {
                throw new AccountNotFoundException();
            }
            if (account.getStatus() != Constants.STATE_ON) {
                throw new LockedAccountException();
            }
            UserPrincipal principal = new UserPrincipal();
            principal.setId(account.getId());
            principal.setUserName(account.getUserName());
            principal.setDisplayName(account.getDisplayName());

            //TODO:保存最近登录日志
            ByteSource credentialsSalt = ByteSource.Util.bytes(account.getUserName().concat(account.getSalt()));
            return new SimpleAuthenticationInfo(principal, account.getPasswd(), credentialsSalt, getName());
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
