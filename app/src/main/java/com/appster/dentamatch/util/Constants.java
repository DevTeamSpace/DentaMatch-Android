package com.appster.dentamatch.util;

/**
 * Class to hold constant values.
 */
public class Constants {
    public static final String DEVICE_TYPE = "ANDROID";
    public static String CHAT_SERVER_URL = "http://172.16.16.188:3000";

    public static final String EXTRA_PLACE_NAME = "place_name";
    public static final String EXTRA_POSTAL_CODE = "postal_code";
    public static final String EXTRA_LATITUDE = "latitude";
    public static final String EXTRA_LONGITUDE = "longitude";
    public static final String EXTRA_SUB_SKILLS = "sub_skills";
    public static final String EXTRA_CHOSEN_JOB_TITLES = "EXTRA_CHOSEN_JOB_TITLES";
    public static final String EXTRA_SEARCH_JOB = "EXTRA_SEARCH_JOB";
    public static final String EXTRA_JOB_LIST = "EXTRA_JOB_LIST";
    public static final String EXTRA_JOB_DETAIL_ID = "EXTRA_JOB_DETAIL_ID";
    public static final String EXTRA_IS_LOGIN = "display_login";
    public static final String EXTRA_JOB_ID = "EXTRA_JOB_ID";

    public static final String OTHERS = "Other";
    public static final String EXTRA_PROFILE_DATA = "EXTRA_PROFILE_DATA";

    public static final int PASSWORD_MIN_LENGTH = 6;
    public static final int WORK_EXP_LIST_LIMIT = 50;
    public static final int LICENCE_MAX_LENGTH = 15;
    public static final int DEFAULT_FIELD_LENGTH = 30;
    public static final int PASSWORD_MAX_LENGTH = 25;
    public static final int USER_NAME_MIN_LENGTH = 6;
    public static final int FIRST_NAME_MIN_LENGTH = 2;
    public static final int IMAGE_DIMEN = 102;
    public static final int IMAGE_DIME_CERTIFICATE = 142;
    public static final int EDUCATION_HISTORY_YEARS = 50;
    public static final int MAP_ZOOM_LEVEL = 15;

    public static final String DISCONNECTED = "DISCONNECTED";
    public static final String CONNECTION_TIMED_OUT = "CONNECTION_TIMED_OUT";
    public static final String CONNECTED = "CONNECTED";

    public enum ACTIVITIES {
        SWITCH_ACTIVITY
    }

    public enum FRAGMENTS {
        TEST_FRAGMENT
    }

    public enum JOBTYPE {
        FULL_TIME(1),
        PART_TIME(2),
        TEMPORARY(3);

        private int value;

        JOBTYPE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum SEARCHJOBTYPE {
        SAVED(1),
        APPLIED(2),
        SHORTLISTED(3);

        private int value;

        SEARCHJOBTYPE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public interface BundleKey {
        String LAYOUT_ID = "layoutResId";
        String INDEX = "INDEX";
        String SUB_SKILLS = "sub_skills";
    }

    public interface APIS {
        String PRIVACY_POLICY = "privacy-policy";
        String TERM_CONDITION = "term-condition";
        String IMAGE_TYPE_PIC = "profile_pic";
        String IMAGE_TYPE_STATE_BOARD = "dental_state_board";
        String ACTION_ADD = "add";
        String ACTION_EDIT = "edit";
    }

    public interface REQUEST_CODE {
        int REQUEST_CODE_CAMERA = 1901;
        int REQUEST_CODE_GALLERY = 1902;

        int REQUEST_CODE_WRITE_SD_CARD = 200;
        int REQUEST_CODE_LOCATION_ACCESS = 201;
        int REQUEST_CODE_READ_PHONE_STATE = 202;
        int REQUEST_CODE_JOB_TITLE = 210;
        int REQUEST_CODE_JOYRIDE = 203;
        int REQUEST_CODE_SKILLS = 303;
        int REQUEST_CODE_PASS_INTENT = 304;
        int REQUEST_CODE_LOCATION = 101;
        int REQUEST_CODE_JOB_DETAIL = 121;
    }

    public interface FRAGMENT_NAME {
        String PROFILE_FRAGMENT = "Profile Fragment";
    }

    public interface INTENT_KEY {
        String FROM_WHERE = "from_where";
        String IMAGE_PATH = "image_path";
        String F_NAME = "fname";
        String L_NAME = "lname";
        String POSITION = "position";
        String DATA = "DATA";
        String JOB_TITLE = "job_title";
    }

    public interface PROFILE_PERCENTAGE {
        int PROFILE_2 = 15;
        int WORK_EXPERIENCE = 25;
        int SCHOOLING = 50;
        int SKILLS = 65;
        int AFFILIATION = 80;
        int CERTIFICATE = 90;
        int COMPLETE = 100;
    }
}
