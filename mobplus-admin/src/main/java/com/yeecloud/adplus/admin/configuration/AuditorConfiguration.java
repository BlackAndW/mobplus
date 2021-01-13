package com.yeecloud.adplus.admin.configuration;

import com.yeecloud.adplus.admin.security.UserPrincipal;
import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * Title
 *
 * Date: 2019-11-09 03:50:12
 * Copyright (c) 2019-2099 YeeCloud
 *
 * @author ybbk
 * @version 1.0.01
 */
@Configuration
public class AuditorConfiguration implements AuditorAware<Integer> {

    @Override
    public Optional<Integer> getCurrentAuditor() {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) SecurityUtils.getSubject().getPrincipal();
            if (userPrincipal != null) {
                return Optional.of(userPrincipal.getId());
            }
        } catch (Throwable e) {
        }
        return Optional.of(0);
    }
}

