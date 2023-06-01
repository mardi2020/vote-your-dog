package com.mardi2020.votedogapi.util;


import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseCookie;

public final class CookieUtil {

    private static final int EXPIRED_TIME = 60 * 60 * 3;

    private static final String COOKIE_NAME = "vote-cookie";

    public static ResponseCookie createCookie(String value) {
        return ResponseCookie.from(COOKIE_NAME, value)
                .httpOnly(true)
                .secure(false)
                .path("/votes")
                .maxAge(EXPIRED_TIME)
                .build();
    }

    public static ResponseCookie removeCookie() {
        return ResponseCookie.from(COOKIE_NAME, "")
                .maxAge(0)
                .path("/votes")
                .build();
    }

    public Cookie of(ResponseCookie responseCookie) {
        Cookie cookie = new Cookie(responseCookie.getName(), responseCookie.getValue());
        cookie.setPath(responseCookie.getPath());
        cookie.setSecure(responseCookie.isSecure());
        cookie.setHttpOnly(responseCookie.isHttpOnly());
        cookie.setMaxAge((int) responseCookie.getMaxAge().getSeconds());
        return cookie;
    }
}
