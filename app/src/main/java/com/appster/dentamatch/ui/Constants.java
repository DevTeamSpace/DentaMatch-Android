package com.appster.dentamatch.ui;

/**
 *
 */
public class Constants {

    private Constants() {
    }

    public static final int PASSWORD_MIN_LENGTH = 6;
    public static final int USER_NAME_MIN_LENGTH = 6;
    public static final int FIRST_NAME_MIN_LENGTH = 2;


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
        int REQUEST_CODE_LOGIN = 100;
        int FILTER_REQUEST_CODE = 101;
        int REQUEST_GALLERY = 102;
        int REQUEST_CAMERA = 103;
        int REQUEST_CODE_VOICE_SEARCH = 104;
        int REQUEST_CODE_FIRST_LOGIN = 105;

        int REQUEST_CODE_WRITE_SD_CARD = 200;
        int REQUEST_CODE_LOCATION_ACCESS = 201;
        int REQUEST_CODE_READ_PHONE_STATE = 202;
        int REQUEST_CODE_JOYRIDE = 203;
    }
}
