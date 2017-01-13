package com.appster.dentamatch.network.retrofit;

import com.appster.dentamatch.network.request.auth.LicenceRequest;
import com.appster.dentamatch.network.request.auth.LoginRequest;
import com.appster.dentamatch.network.response.auth.FileUploadResponse;
import com.appster.dentamatch.network.response.auth.JobTitleResponse;
import com.appster.dentamatch.network.response.auth.LicenceUpdateResponse;
import com.appster.dentamatch.network.response.auth.LoginResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;


public interface AuthWebServices {
    String SIGN_IN = "users/sign-in";
    String SIGN_UP = "users/sign-up";
    String FORGOT_PASSWORD = "users/forgot-password";
    String UPDATE_LICENCE = "users/update-license";
    String JOB_TITLE_LIST = "list-jobtitle";
    String SKILLS_LIST = "list-skills";
    String IMAGE_UPLOAD="users/upload-image";

    @POST(SIGN_IN)
    Call<LoginResponse> signIn(@Body LoginRequest loginRequest);

    @POST(SIGN_UP)
    Call<LoginResponse> signUp(@Body LoginRequest loginRequest);

    @PUT(FORGOT_PASSWORD)
    Call<LoginResponse> forgotPassword(@Body LoginRequest loginRequest);

    @GET(JOB_TITLE_LIST)
    Call<JobTitleResponse> jobTitle();

    @GET(SKILLS_LIST)
    Call<LoginResponse> getSkillsList(@Body LoginRequest loginRequest);

    @PUT(UPDATE_LICENCE)
    Call<LicenceUpdateResponse> updateLicence(@Body LicenceRequest licenceRequest);

    @Multipart
    @POST(IMAGE_UPLOAD)
    Call<FileUploadResponse> uploadImage(@Part("type") RequestBody type,
                                         @Part("image\"; filename=\"denta_img.jpg\"") RequestBody file);

    /*@POST(AUTHENTICATION_URL)
    Call<LoginResponse> userAuthenticate(@Body LoginRequest params);

    @POST(LOGOUT)
    Call<BaseResponse> logout(@Body LoginRequest params);

    @POST(REFRESH_TOKEN)
    Call<RefreshTokenResponse> refreshToken(@Body RefreshTokenRequest params);*/
}
