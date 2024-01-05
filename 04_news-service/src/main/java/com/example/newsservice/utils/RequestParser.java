package com.example.newsservice.utils;

import com.example.newsservice.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Base64;
import java.util.Map;

@UtilityClass
public class RequestParser {

    public String getCurrentUserName() {
        String userName = "";
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String credentials = request.getHeader(HttpHeaders.AUTHORIZATION)
                    .replace(SecurityConstants.TOKEN_PREFIX, "");
            String decodedCredentials = new String(Base64.getDecoder().decode(credentials));
            userName = decodedCredentials.split(":")[0];
        }
        return userName;
    }

    public long getIdFromRequest() {
        long id = 0L;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            var pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

            id = Long.parseLong(pathVariables.get(User.Fields.id));

        }
        return id;
    }
}
