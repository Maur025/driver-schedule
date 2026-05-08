package com.kernotec.driverschedule.person.rest;

import com.kernotec.driverschedule.common.rest.ApiSpec;

public class ResourceApiSpec {

    public static final class ContactCategorySpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/contact-categories";
        public static final String TAG_NAME = "CONTACT CATEGORY";
        public static final String TAG_DESCRIPTION = "Contact Category management";

        private ContactCategorySpec() {
        }
    }

    public static final class ContactSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/contacts";
        public static final String TAG_NAME = "CONTACT";
        public static final String TAG_DESCRIPTION = "Contact management";

        private ContactSpec() {
        }
    }

    public static final class LabelTypeSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/label-types";
        public static final String TAG_NAME = "LABEL TYPE";
        public static final String TAG_DESCRIPTION = "Label Type management";

        private LabelTypeSpec() {
        }
    }

    public static final class PersonSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/persons";
        public static final String TAG_NAME = "PERSON";
        public static final String TAG_DESCRIPTION = "Person management";

        private PersonSpec() {
        }
    }

    public static final class PersonTypeSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/person-types";
        public static final String TAG_NAME = "PERSON TYPE";
        public static final String TAG_DESCRIPTION = "Person type management";

        private PersonTypeSpec() {
        }
    }
}
