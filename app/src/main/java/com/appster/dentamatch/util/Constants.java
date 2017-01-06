package com.appster.dentamatch.util;

/**
 *
 */
public class Constants {

    private Constants() {
    }

    public static final int PASSWORD_MIN_LENGTH = 6;
    public static final int USER_NAME_MIN_LENGTH = 6;
    public static final int FIRST_NAME_MIN_LENGTH = 2;
    public static final int IMAGE_DIMEN = 102;



    public enum ACTIVITIES{
        SWITCH_ACTIVITY
    }

    public enum FRAGMENTS {
        TEST_FRAGMENT
    }

    public interface BundleKey{
        String LAYOUT_ID = "layoutResId";
        String INDEX = "INDEX";
    }

    public interface REQUEST_CODE {
        int REQUEST_CODE_CAMERA = 1901;
        int REQUEST_CODE_GALLERY = 1902;

        int REQUEST_CODE_WRITE_SD_CARD = 200;
        int REQUEST_CODE_LOCATION_ACCESS = 201;
        int REQUEST_CODE_READ_PHONE_STATE = 202;
        int REQUEST_CODE_JOYRIDE = 203;
    }

    public interface INTENT_KEY{
        String FROM_WHERE="from_where";
        String IMAGE_PATH="image_path";
    }
}
