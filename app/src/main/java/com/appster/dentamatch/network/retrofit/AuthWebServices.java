package com.appster.dentamatch.network.retrofit;

import com.appster.dentamatch.BuildConfig;


public interface AuthWebServices {

    String AUTHENTICATION_URL = BuildConfig.BASE_URL + "authenticateUser";
    String LOGOUT = BuildConfig.BASE_URL + "logout";
    String REFRESH_TOKEN = BuildConfig.BASE_URL + "refreshToken";

    /*@POST(AUTHENTICATION_URL)
    Call<LoginResponse> userAuthenticate(@Body LoginRequest params);

    @POST(LOGOUT)
    Call<BaseResponse> logout(@Body LoginRequest params);

    @POST(REFRESH_TOKEN)
    Call<RefreshTokenResponse> refreshToken(@Body RefreshTokenRequest params);*/
}
