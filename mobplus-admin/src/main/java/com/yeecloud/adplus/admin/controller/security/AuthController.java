package com.yeecloud.adplus.admin.controller.security;

import com.google.common.collect.Maps;
import com.yeecloud.adplus.admin.controller.security.form.LoginForm;
import com.yeecloud.adplus.admin.controller.sys.form.SysPasswdForm;
import com.yeecloud.adplus.admin.controller.sys.form.SysUserForm;
import com.yeecloud.adplus.admin.controller.sys.vo.SysMenuTreeVO;
import com.yeecloud.adplus.admin.controller.sys.vo.SysUserVO;
import com.yeecloud.adplus.admin.security.CurrentUser;
import com.yeecloud.adplus.admin.security.UserPrincipal;
import com.yeecloud.adplus.admin.service.SysMenuService;
import com.yeecloud.adplus.admin.service.SysPermissionService;
import com.yeecloud.adplus.admin.service.SysRoleService;
import com.yeecloud.adplus.admin.service.SysUserService;
import com.yeecloud.meeto.common.exception.ServiceException;
import com.yeecloud.meeto.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Title
 *
 * Date: 2019-11-09 01:48:00
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysPermissionService sysPermissionService;


    @PostMapping("/login")
    public Result login(@RequestBody @Valid LoginForm form, BindingResult bindingResult) {
        log.info("LoginForm: " + form);
//        开发环境省去验证码验证
        if (!CaptchaController.validate(form.getCaptcha())) {
            bindingResult.addError(new FieldError("LoginForm", "captcha", "请输入正确的验证码"));
        }

        if (bindingResult.hasErrors()) {
            String message = String.format("登陆失败,详细信息:[%s]。", bindingResult.getFieldError().getDefaultMessage());
            return Result.FAILURE(message);
        }
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(form.getUserName().trim(), form.getPasswd().trim());
        token.setHost(request.getRemoteAddr());
        try {
            currentUser.login(token);
        } catch (UnknownAccountException uae) {
            return Result.FAILURE("用户名或密码错误!");
        } catch (IncorrectCredentialsException ice) {
            return Result.FAILURE("密码不正确!");
        } catch (LockedAccountException lae) {
            return Result.FAILURE("账户已锁定!");
        } catch (ExcessiveAttemptsException eae) {
            return Result.FAILURE("用户名或密码错误次数过多!");
        } catch (AuthenticationException ae) {
            return Result.FAILURE("用户名或密码错误!");
        }
        //验证是否登录成功
        if (currentUser.isAuthenticated()) {
            return Result.SUCCESS(currentUser.getSession().getId());
        } else {
            return Result.FAILURE("用户名或密码错误!");
        }
    }

    @GetMapping("/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.SUCCESS();
    }

    @GetMapping("/profile")
    public Result<SysUserVO> profile(@CurrentUser UserPrincipal user) throws ServiceException {
        SysUserVO result = sysUserService.findById(user.getId());
        return Result.SUCCESS(result);
    }

    @GetMapping("/perms")
    public Result<Map<String, List<String>>> permissions(@CurrentUser UserPrincipal user) throws ServiceException {
        List<String> permissionList = sysPermissionService.findCodesByUserId(user.getId());
        List<String> roleList = sysRoleService.findCodesByUserId(user.getId());
        if (permissionList.isEmpty()) {
            permissionList.add("no.permissions");
        }

        Map<String, List<String>> result = Maps.newHashMap();
        result.put("permissionList", permissionList);
        result.put("roleList", roleList);

        return Result.SUCCESS(result);
    }

    @GetMapping("/menus")
    public Result<List<SysMenuTreeVO>> menus(@CurrentUser UserPrincipal user) throws ServiceException {
        List<SysMenuTreeVO> menus = sysMenuService.findMenusByUserId(user.getId());
        return Result.SUCCESS(menus);
    }

    @PostMapping("/passwd")
    public Result updatePasswd(@CurrentUser UserPrincipal user, @RequestBody SysPasswdForm form) throws ServiceException {
        sysUserService.updatePasswd(user.getId(), form);
        return Result.SUCCESS();
    }

    @PutMapping("/profile")
    public Result profile(@CurrentUser UserPrincipal user, @RequestBody SysUserForm form) throws ServiceException {
        sysUserService.update(user.getId(), form);
        return Result.SUCCESS();
    }
}
