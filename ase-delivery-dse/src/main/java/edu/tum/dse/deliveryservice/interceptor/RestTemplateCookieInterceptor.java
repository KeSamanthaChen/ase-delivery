package edu.tum.dse.deliveryservice.interceptor;

import edu.tum.dse.deliveryservice.client.CookieManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.Optional;

public class RestTemplateCookieInterceptor implements ClientHttpRequestInterceptor {

    @Autowired
    private CookieManager cookieManager;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        Optional<Cookie> jwt = cookieManager.getUserCookie("jwt");
        Optional<Cookie> csrf = cookieManager.getUserCookie("XSRF-TOKEN");
        Optional<String> xCsrf = cookieManager.getHeaderField("X-XSRF-TOKEN");

        jwt.ifPresent(cookie -> request.getHeaders().add("Cookie", "jwt=" + cookie.getValue()));
        csrf.ifPresent(cookie ->
                request.getHeaders().add("Cookie", "XSRF-TOKEN=" + cookie.getValue())
        );
        xCsrf.ifPresent(headerValue -> request.getHeaders().add("X-XSRF-TOKEN", headerValue));
        return execution.execute(request, body);
    }
}
