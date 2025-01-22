package com.sanjeet.chat.utils;

import java.util.Arrays;

public class Constant {

    public static final String H_MAC_ALGORITHM = "HmacSHA256";

    public static final String [] PUBLIC_URLS =
                                                {
                                                    "/api/v1/client/register",
                                                    "/api/v1/client/login",
                                                     "/api/v1/admin/register",
                                                      "/api/v1/admin/login",
                                                        "/api/v1/user/register",
                                                };
    public static final String SESSION_TOKEN = "sessionToken";
    public static final String CLAIM_SESSION_TOKEN = "claim_session_token";
    public static final String USER = "USER";
    public static final String CLIENT = "CLIENT";
    public static final String ADMIN = "ADMIN";
    public static final String ROLE = "role";
    public static final String USER_NAME = "user_name";
    public static final String CLIENT_ID = "clientId";
    public static final String API_KEY = "apiKey";


    static {
        System.out.println("PUBLIC_URLS: " + Arrays.toString(PUBLIC_URLS)); // Log the URLs
    }

}
