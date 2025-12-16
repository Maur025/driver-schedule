package com.kernotec.driverscheduleauth.rest;

public class ApiSpec {

    public static final String ROOT_PATH = "/realms";
    public static final String DEFAULT_REALM_PATH = ROOT_PATH + "/driver-schedule-auth";

    public static class OpenIdConfigurationSpec {

        public static final String BASE_PATH = ".well-known/openid-configuration";
        public static final String TAG_NAME = "OPENID CONFIGURATION";
        public static final String TAG_DESCRIPTION = "OpenID Connect discovery document";

        private OpenIdConfigurationSpec() {
        }
    }

    public static class OpenIdConnectSpec {

        public static final String BASE_PATH = DEFAULT_REALM_PATH + "/protocol/openid-connect";
        public static final String TAG_NAME = "OPENID CONNECT";
        public static final String TAG_DESCRIPTION = "OpenID Connect endpoints";

        private OpenIdConnectSpec() {
        }
    }

    public static class UserSpec {

        public static final String BASE_PATH = ROOT_PATH + "/users";
        public static final String TAG_NAME = "USERS";
        public static final String TAG_DESCRIPTION = "User management";

        private UserSpec() {
        }
    }

    public static class RoleSpec {

        public static final String BASE_PATH = ROOT_PATH + "/roles";
        public static final String TAG_NAME = "ROLES";
        public static final String TAG_DESCRIPTION = "Role management";

        private RoleSpec() {
        }
    }

    public static class RealmSpec {

        public static final String BASE_PATH = ROOT_PATH + "/realms-management";
        public static final String TAG_NAME = "REALMS";
        public static final String TAG_DESCRIPTION = "Realm management";

        private RealmSpec() {
        }
    }
}
