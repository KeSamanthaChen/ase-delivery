package edu.tum.dse.deliveryservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Optional;

@Component
public class CookieManager {

    //@Autowired
    //private HttpServletRequest request;

    private HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        } else
            return null;
    }


    public Optional<Cookie> getUserCookie(String cookieName) {
        HttpServletRequest request = getRequest();
        if (request != null) {
            Cookie[] cookies = request.getCookies();
            return Arrays.stream(cookies).filter((cookie -> cookie.getName().equals(cookieName))).findFirst();
        }
        return Optional.empty();
    }

    public Optional<String> getHeaderField(String fieldName) {
        HttpServletRequest request = getRequest();
        if (request != null) {
            String value = request.getHeader(fieldName);
            return Optional.ofNullable(value);
        } else {
            return Optional.empty();
        }
    }
}
