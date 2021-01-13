package com.yeecloud.adplus.gateway;

public class ResultCode extends com.yeecloud.meeto.common.result.ResultCode.DefaultResultCode {
    com.yeecloud.meeto.common.result.ResultCode OK = new com.yeecloud.meeto.common.result.ResultCode.DefaultResultCode(200, "OK");
    public static com.yeecloud.meeto.common.result.ResultCode ERR_MEDIA_NOT_FOUND_APPID = new com.yeecloud.meeto.common.result.ResultCode.DefaultResultCode(5031, "AppID不存在!");


    public ResultCode(int code, String message) {
        super(code, message);
    }
}