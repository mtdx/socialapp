package com.ninja.socialapp.service.util;


/**
 * Utility class twitter errors codes.
 */
public final class TwitterErrorCode {

    private static final int BAD_TOKEN = 89; // Invalid or expired token.
    private static final int BAD_CREDENTIALS = 99; // Unable to verify your credentials.
    private static final int BAD_AUTH = 32; // could not authenticate you.
    private static final int SUSPENDED = 64; // Your account is suspended and is not permitted to access this feature
    private static final int LOCKED = 326; // To protect our users from spam and other malicious activity, this account is temporarily locked

    private TwitterErrorCode() {
    }

    public static boolean authError(Integer errorCode){
        return errorCode == BAD_TOKEN || errorCode == BAD_CREDENTIALS || errorCode == BAD_AUTH;
    }

    public static boolean suspended(Integer errorCode){
        return errorCode == SUSPENDED;
    }

    public static boolean locked(Integer errorCode){
        return errorCode == LOCKED;
    }
}
