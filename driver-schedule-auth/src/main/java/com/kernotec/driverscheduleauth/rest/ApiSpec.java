package com.kernotec.driverscheduleauth.rest;

public class ApiSpec {

    public static final String ROOT_PATH = "/realms";
    public static final String REALM_CONTEXT = ROOT_PATH + "/{realm}";

    public static class OpenIdConfigurationSpec {

        public static final String BASE_PATH = ".well-known/openid-configuration";
        public static final String TAG_NAME = "OPENID CONFIGURATION";
        public static final String TAG_DESCRIPTION = "OpenID Connect discovery document";

        private OpenIdConfigurationSpec() {
        }
    }

    public static class OpenIdConnectSpec {

        public static final String BASE_PATH = REALM_CONTEXT + "/protocol/openid-connect";
        public static final String TAG_NAME = "OPENID CONNECT";
        public static final String TAG_DESCRIPTION = "OpenID Connect endpoints";

        private OpenIdConnectSpec() {
        }
    }

    public static class UserSpec {

        public static final String BASE_PATH = REALM_CONTEXT + "/users";
        public static final String TAG_NAME = "USERS";
        public static final String TAG_DESCRIPTION = "User management";

        private UserSpec() {
        }
    }

    public static class RoleSpec {

        public static final String BASE_PATH = REALM_CONTEXT + "/roles";
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

    public static class AccountSpec {

        public static final String BASE_PATH = REALM_CONTEXT + "/account";
        public static final String TAG_NAME = "ACCOUNT";
        public static final String TAG_DESCRIPTION = "User account management";

        private AccountSpec() {
        }
    }
}
