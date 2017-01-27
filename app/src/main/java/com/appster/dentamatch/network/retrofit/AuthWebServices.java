package com.appster.dentamatch.network.retrofit;

import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.request.auth.ChangePasswordRequest;
import com.appster.dentamatch.network.request.auth.LicenceRequest;
import com.appster.dentamatch.network.request.auth.LoginRequest;
import com.appster.dentamatch.network.request.certificates.CertificateRequest;
import com.appster.dentamatch.network.request.profile.AboutMeRequest;
import com.appster.dentamatch.network.request.profile.UpdateUserProfileRequest;
import com.appster.dentamatch.network.request.schools.AddSchoolRequest;
import com.appster.dentamatch.network.request.skills.SkillsUpdateRequest;
import com.appster.dentamatch.network.request.workexp.WorkExpListRequest;
import com.appster.dentamatch.network.request.workexp.WorkExpRequest;
import com.appster.dentamatch.network.request.affiliation.AffiliationPostRequest;
import com.appster.dentamatch.network.response.affiliation.AffiliationResponse;
import com.appster.dentamatch.network.response.auth.LoginResponse;
import com.appster.dentamatch.network.response.certificates.CertificateResponse;
import com.appster.dentamatch.network.response.fileupload.FileUploadResponse;
import com.appster.dentamatch.network.response.profile.JobTitleResponse;
import com.appster.dentamatch.network.response.profile.LicenceUpdateResponse;
import com.appster.dentamatch.network.response.profile.ProfileResponse;
import com.appster.dentamatch.network.response.schools.SchoolingResponse;
import com.appster.dentamatch.network.response.skills.SkillsResponse;
import com.appster.dentamatch.network.response.workexp.WorkExpResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Interface to declare web service stubs.
 */
public interface AuthWebServices {
    String SIGN_IN = "users/sign-in";
    String SIGN_UP = "users/sign-up";
    String FORGOT_PASSWORD = "users/forgot-password";
    String UPDATE_LICENCE = "users/update-license";
    String JOB_TITLE_LIST = "list-jobtitle";
    String SKILLS_LIST = "list-skills";
    String SKILLS_UPDATE = "users/update-skill";
    String SCHOOL_LIST = "users/school-list";
    String SCHOOL_ADD = "users/school-add";
    String CERTIFICATION_LIST = "list-certifications";
    String CERTIFICATION_UPDATE = "users/update-certificate-validity";
    String SAVE_ABOUT_ME = "users/about-me-save";
    String IMAGE_UPLOAD = "users/upload-image";
    String IMAGE_UPLOAD_CERTIFICATE = "users/update-certificate";
    String ADD_WORK_EXP = "users/work-experience-save";
    String WORK_EXP_LIST = "users/work-experience-list";
    //    String WORK_EXP_DELETE="users/work-experience-delete{id}";
    String WORK_EXP_DELETE = "users/work-experience-delete";
    String AFFILIATION_LIST = "users/affiliation-list";
    String GET_PROFILE = "users/user-profile";
    String AFFILIATION_SAVE = "users/affiliation-save";
    String CHANGE_PASSWORD = "users/change-password";
    String LOGOUT = "users/sign-out";
    String UPDATE_PROFILE = "users/user-profile-update";

    @POST(SIGN_IN)
    Call<LoginResponse> signIn(@Body LoginRequest loginRequest);

    @POST(SIGN_UP)
    Call<LoginResponse> signUp(@Body LoginRequest loginRequest);

    @PUT(FORGOT_PASSWORD)
    Call<LoginResponse> forgotPassword(@Body LoginRequest loginRequest);

    @DELETE(WORK_EXP_DELETE)
    Call<BaseResponse> workExpDelete(@Query("id") int id);

    @DELETE(LOGOUT)
    Call<BaseResponse> logout();

    @POST(ADD_WORK_EXP)
    Call<WorkExpResponse> addWorkExp(@Body WorkExpRequest workExpRequest);

    @POST(AFFILIATION_SAVE)
    Call<BaseResponse> saveAffiliation(@Body AffiliationPostRequest affiliationPostRequest);

    @POST(CHANGE_PASSWORD)
    Call<BaseResponse> changePassword(@Body ChangePasswordRequest changePassowrdRequest);

    @POST(CERTIFICATION_UPDATE)
    Call<BaseResponse> saveCertificate(@Body CertificateRequest certificateRequest);

    @POST(SAVE_ABOUT_ME)
    Call<BaseResponse> saveAboutMe(@Body AboutMeRequest aboutMeRequest);

    @POST(WORK_EXP_LIST)
    Call<WorkExpResponse> workExpList(@Body WorkExpListRequest workExpListRequest);

    @GET(JOB_TITLE_LIST)
    Call<JobTitleResponse> jobTitle();

    @GET(SKILLS_LIST)
    Call<SkillsResponse> getSkillsList();

    @POST(SKILLS_UPDATE)
    Call<BaseResponse> updateSkills(@Body SkillsUpdateRequest skillsUpdateRequest);

    @GET(SCHOOL_LIST)
    Call<SchoolingResponse> getSchoolList();

    @GET(SCHOOL_ADD)
    Call<BaseResponse> addSchooling(@Body AddSchoolRequest schoolRequest);

    @GET(CERTIFICATION_LIST)
    Call<CertificateResponse> getCertificationList();

    @GET(AFFILIATION_LIST)
    Call<AffiliationResponse> getAffiliationList();

    @GET(GET_PROFILE)
    Call<ProfileResponse> getProfile();

    @PUT(UPDATE_LICENCE)
    Call<LicenceUpdateResponse> updateLicence(@Body LicenceRequest licenceRequest);

    @PUT(UPDATE_PROFILE)
    Call<BaseResponse> updateUserProfile(@Body UpdateUserProfileRequest request);

    @Multipart
    @POST(IMAGE_UPLOAD)
    Call<FileUploadResponse> uploadImage(@Part("type") RequestBody type,
                                         @Part("image\"; filename=\"denta_img.jpg\"") RequestBody file);

    @Multipart
    @POST(IMAGE_UPLOAD_CERTIFICATE)
    Call<FileUploadResponse> uploadCertificateImage(@Part("certificateId") RequestBody type,
                                                    @Part("image\"; filename=\"denta_img.jpg\"") RequestBody file);
}
