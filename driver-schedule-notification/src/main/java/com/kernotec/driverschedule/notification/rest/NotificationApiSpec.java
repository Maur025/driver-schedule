package com.kernotec.driverschedule.notification.rest;

import com.kernotec.driverschedule.common.rest.ApiSpec;

public class NotificationApiSpec {

    public static final class NotificationConfigurationSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/notification-configurations";
        public static final String TAG_NAME = "NOTIFICATION CONFIGURATION";
        public static final String TAG_DESCRIPTION = "Notification Configuration management";

        private NotificationConfigurationSpec() {
        }
    }

    public static final class PersonNotificationSpec {

        public static final String BASE_PATH = ApiSpec.ROOT_PATH + "/person-notifications";
        public static final String TAG_NAME = "PERSON NOTIFICATION";
        public static final String TAG_DESCRIPTION = "Person Notification management";

        private PersonNotificationSpec() {
        }
    }
}
