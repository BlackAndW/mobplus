package com.yeecloud.adplus.gateway.util;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosAlert;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.yeecloud.meeto.common.exception.ServiceException;

import java.util.Map;

/**
 * @author: Leonard
 * @create: 2022/3/16
 */
public class JpushUtil {

    private static JPushClient jPushClient = new JPushClient("aede630cf5776898fb03e4ec","5f2a4f2c167f66def274b307");

    public static void push(String type, String value, String alert, String iosTitle, Map<String, String> extras) {
        PushPayload.Builder builder = PushPayload.newBuilder();
        builder.setPlatform(Platform.ios());
        Options options = Options.sendno();
        builder.setOptions(options);
        //设置推送方式
        if (type.equals("alias")) {
            builder.setAudience(Audience.alias(value));//根据别名推送
        } else if (type.equals("tag")) {
            builder.setAudience(Audience.tag(value));//根据标签推送
        } else {
            builder.setAudience(Audience.all());//Audience设置为all，说明采用广播方式推送，所有用户都可以接收到
        }
        //设置为采用通知的方式发送消息
        IosAlert iosAlert = IosAlert.newBuilder()
                .setTitleAndBody(iosTitle, null, alert)
                .build();
        builder.setNotification(Notification.newBuilder().setAlert(alert).build());
        PushPayload pushPayload = builder.setNotification(Notification.newBuilder()
                .addPlatformNotification(IosNotification.newBuilder().setAlert(iosAlert).addExtra(iosTitle,alert).addExtras(extras).build()).build())
                .build();

        try {
            //进行推送，实际推送就在这一步
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            System.out.println(pushResult.getResponseCode());
            if (pushResult.getResponseCode() != 200) {
                throw new ServiceException("push error,code is: " + pushResult.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
