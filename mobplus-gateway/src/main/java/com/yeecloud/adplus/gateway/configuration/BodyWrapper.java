package com.yeecloud.adplus.gateway.configuration;

import com.yeecloud.adplus.gateway.util.Result;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * wrapper类解决过滤只能读取一次流的问题
 * @author: Leonard
 * @create: 2021/12/9
 */
public class BodyWrapper extends HttpServletRequestWrapper {
    private final byte[] body;

    public BodyWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = decodeBodyString(request).getBytes(Charset.forName("UTF-8"));
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);

        return new ServletInputStream(){
            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener arg0) {
            }
        };
    }

    // body参数解密
    public static String decodeBodyString(ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try (InputStream inputStream = request.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return Result.DECODE(sb.toString());
    }
}
