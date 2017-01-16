package com.appster.dentamatch.network.retrofit;

import com.appster.dentamatch.BuildConfig;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.request.auth.DeleteRequest;
import com.appster.dentamatch.network.request.auth.LicenceRequest;
import com.appster.dentamatch.network.request.auth.LoginRequest;
import com.appster.dentamatch.network.request.auth.WorkExpListRequest;
import com.appster.dentamatch.network.request.auth.WorkExpRequest;
import com.appster.dentamatch.network.response.auth.AffiliationResponse;
import com.appster.dentamatch.network.response.auth.CertificateResponse;
import com.appster.dentamatch.network.response.auth.FileUploadResponse;
import com.appster.dentamatch.network.response.auth.JobTitleResponse;
import com.appster.dentamatch.network.response.auth.LicenceUpdateResponse;
import com.appster.dentamatch.network.response.auth.LoginResponse;
import com.appster.dentamatch.network.response.skills.SkillsResponse;
import com.appster.dentamatch.network.response.auth.WorkExpResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface AuthWebServices {
    String SIGN_IN = "users/sign-in";
    String SIGN_UP = "users/sign-up";
    String FORGOT_PASSWORD = "users/forgot-password";
    String UPDATE_LICENCE = "users/update-license";
    String JOB_TITLE_LIST = "list-jobtitle";
    String SKILLS_LIST = "list-skills";
    String CERTIFICATION_LIST = "list-certifications";
    String IMAGE_UPLOAD = "users/upload-image";
    String ADD_WORK_EXP = "users/work-experience-save";
    String WORK_EXP_LIST = "users/work-experience-list";
    //    String WORK_EXP_DELETE="users/work-experience-delete{id}";
    String WORK_EXP_DELETE = "users/work-experience-delete";
    String AFFILIATION_LIST = "users/affiliation-list";

    @POST(SIGN_IN)
    Call<LoginResponse> signIn(@Body LoginRequest loginRequest);

    @POST(SIGN_UP)
    Call<LoginResponse> signUp(@Body LoginRequest loginRequest);

    @PUT(FORGOT_PASSWORD)
    Call<LoginResponse> forgotPassword(@Body LoginRequest loginRequest);

    @DELETE(WORK_EXP_DELETE)
//    Call<BaseResponse> workExpDelete(@Path("id") int id);
    Call<BaseResponse> workExpDelete(@Query("id") int id);

    //    Call<BaseResponse> workExpDelete(DeleteRequest deleteRequest);
//
    @POST(ADD_WORK_EXP)
    Call<WorkExpResponse> addWorkExp(@Body WorkExpRequest workExpRequest);

    @POST(WORK_EXP_LIST)
    Call<WorkExpResponse> workExpList(@Body WorkExpListRequest workExpListRequest);


    @GET(JOB_TITLE_LIST)
    Call<JobTitleResponse> jobTitle();

    @GET(SKILLS_LIST)
    Call<SkillsResponse> getSkillsList();

    @GET(CERTIFICATION_LIST)
    Call<CertificateResponse> getCertificationList();

    @GET(AFFILIATION_LIST)
    Call<AffiliationResponse> getAffiliationList();

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
