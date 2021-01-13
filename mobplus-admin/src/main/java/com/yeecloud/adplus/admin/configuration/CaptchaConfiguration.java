package com.yeecloud.adplus.admin.configuration;

import com.google.code.kaptcha.BackgroundProducer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.google.code.kaptcha.util.Configurable;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Title
 *
 * Date: 2019-11-09 03:51:14
 * Copyright (c) 2019-2099 YeeCloud
 * captcha.background.pattern
 *
 * @author ybbk
 * @version 1.0.01
 */
@Slf4j
@Configuration
public class CaptchaConfiguration {

    private static final String CAPTCHA_BACKGROUND_PATH = "kaptcha.background.pattern";
    private static final String CAPTCHA_BACKGROUND_COUNT = "captcha.background.count";

    @Value("${captcha.background.count}")
    private int backgroundImageCount;

    @Value("${captcha.background.pattern}")
    private String backgroundImagePattern;

    @Bean
    public DefaultKaptcha getDefaultKaptcha() {

        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "no");

        properties.setProperty("kaptcha.image.width", "90");
        properties.setProperty("kaptcha.image.height", "34");

        properties.setProperty("kaptcha.textproducer.font.color", "white");
        properties.setProperty("kaptcha.textproducer.font.size", "28");
        properties.setProperty("kaptcha.textproducer.char.length", "4");

        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy");
        properties.setProperty("kaptcha.background.impl", CaptchaBackgroundImpl.class.getName());
        properties.setProperty(CAPTCHA_BACKGROUND_PATH, backgroundImagePattern);
        properties.setProperty(CAPTCHA_BACKGROUND_COUNT, String.valueOf(backgroundImageCount));

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);

        return defaultKaptcha;
    }

    public static class CaptchaBackgroundImpl extends Configurable implements BackgroundProducer {

        private static final List<BufferedImage> BACKGROUND_IMAGES_CACHE = new CopyOnWriteArrayList<>();

        @Override
        public BufferedImage addBackground(BufferedImage baseImage) {
            List<BufferedImage> backgroundImages = getBackgroundImages();
            if (CollectionUtils.isEmpty(backgroundImages)) {
                return baseImage;
            }

            Graphics2D graphics2D = null;
            try {
                int width = baseImage.getWidth();
                int height = baseImage.getHeight();
                BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                graphics2D = destImage.createGraphics();
                int random = RandomUtils.nextInt(backgroundImages.size());
                BufferedImage backgroundImage = backgroundImages.get(random);
                graphics2D.drawImage(backgroundImage, 0, 0, width, height, null);
                graphics2D.drawImage(baseImage, 0, 0, null);
                return destImage;
            } finally {
                if (graphics2D != null) {
                    graphics2D.dispose();
                }
            }
        }

        private List<BufferedImage> getBackgroundImages() {
            if (CollectionUtils.isNotEmpty(BACKGROUND_IMAGES_CACHE)) {
                return BACKGROUND_IMAGES_CACHE;
            }
            Properties properties = getConfig().getProperties();
            Integer count = Integer.parseInt((String) properties.get(CAPTCHA_BACKGROUND_COUNT));
            String bgPattern = properties.getProperty(CAPTCHA_BACKGROUND_PATH);
            if (StringUtils.isNotBlank(bgPattern)) {
                try {
                    for (int idx = 0; idx < count; idx++) {
                        String bg = String.format(bgPattern, String.valueOf(idx));
                        ClassPathResource resource = new ClassPathResource(bg);
                        @Cleanup InputStream inputStream = resource.getInputStream();
                        BufferedImage backgroundImage = ImageIO.read(inputStream);
                        if (backgroundImage != null) {
                            BACKGROUND_IMAGES_CACHE.add(backgroundImage);
                        }
                    }
                } catch (Throwable e) {
                    log.error(e.getMessage(), e);
                }
            }
            return BACKGROUND_IMAGES_CACHE;
        }
    }
}
