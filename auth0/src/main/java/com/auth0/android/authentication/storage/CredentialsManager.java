package com.auth0.android.authentication.storage;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.AuthenticationCallback;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.result.Credentials;

import static android.text.TextUtils.isEmpty;

/**
 * Class that handles credentials and allows to save and retrieve them.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class CredentialsManager {
    private static final String KEY_ACCESS_TOKEN = "com.auth0.access_token";
    private static final String KEY_REFRESH_TOKEN = "com.auth0.refresh_token";
    private static final String KEY_ID_TOKEN = "com.auth0.id_token";
    private static final String KEY_TOKEN_TYPE = "com.auth0.token_type";
    private static final String KEY_EXPIRATION_TIME = "com.auth0.expiration_time";

    private final AuthenticationAPIClient authClient;
    private final Storage storage;

    /**
     * Creates a new instance of the manager that will store the credentials in the given Storage.
     *
     * @param authenticationClient the Auth0 Authentication client to refresh credentials with.
     * @param storage              the storage to use for the credentials.
     */
    public CredentialsManager(@NonNull AuthenticationAPIClient authenticationClient, @NonNull Storage storage) {
        this.authClient = authenticationClient;
        this.storage = storage;
    }

    /**
     * Stores the given credentials in the storage. Must have an access_token or id_token and a expires_in value.
     *
     * @param credentials the credentials to save in the storage.
     */
    public void setCredentials(@NonNull Credentials credentials) {
        if ((isEmpty(credentials.getAccessToken()) && isEmpty(credentials.getIdToken())) || credentials.getExpiresIn() == null) {
            throw new CredentialsManagerException("Credentials must have a valid expires_in value and a valid access_token or id_token value.");
        }
        storage.store(KEY_ACCESS_TOKEN, credentials.getAccessToken());
        storage.store(KEY_REFRESH_TOKEN, credentials.getRefreshToken());
        storage.store(KEY_ID_TOKEN, credentials.getIdToken());
        storage.store(KEY_TOKEN_TYPE, credentials.getType());

        long expirationTime = getCurrentTimeInMillis() + (credentials.getExpiresIn() * 1000);
        storage.store(KEY_EXPIRATION_TIME, Long.toString(expirationTime));
    }

    /**
     * Retrieves the credentials from the storage and refresh them if they have already expired.
     * It will fail with {@link CredentialsManagerException} if the saved access_token or id_token is null,
     * or if the tokens have already expired and the refresh_token is null.
     *
     * @param callback the callback that will receive a valid {@link Credentials} or the {@link CredentialsManagerException}.
     */
    public void getCredentials(@NonNull final BaseCallback<Credentials, CredentialsManagerException> callback) {
        String accessToken = storage.retrieve(KEY_ACCESS_TOKEN);
        String refreshToken = storage.retrieve(KEY_REFRESH_TOKEN);
        String idToken = storage.retrieve(KEY_ID_TOKEN);
        String tokenType = storage.retrieve(KEY_TOKEN_TYPE);
        String expirationTimeValue = storage.retrieve(KEY_EXPIRATION_TIME);

        if (isEmpty(accessToken) && isEmpty(idToken) || isEmpty(expirationTimeValue)) {
            callback.onFailure(new CredentialsManagerException("No Credentials were previously set."));
            return;
        }
        long expirationTime = Long.parseLong(expirationTimeValue);
        if (expirationTime > getCurrentTimeInMillis()) {
            long expiresIn = (expirationTime - getCurrentTimeInMillis()) / 1000;
            callback.onSuccess(new Credentials(idToken, accessToken, tokenType, refreshToken, expiresIn));
            return;
        }
        if (refreshToken == null) {
            callback.onFailure(new CredentialsManagerException("Credentials have expired and no Refresh Token was available to renew them."));
            return;
        }

        authClient.renewAuth(refreshToken).start(new AuthenticationCallback<Credentials>() {
            @Override
            public void onSuccess(Credentials freshCredentials) {
                callback.onSuccess(freshCredentials);
            }

            @Override
            public void onFailure(AuthenticationException error) {
                callback.onFailure(new CredentialsManagerException("An error occurred while trying to use the Refresh Token to renew the Credentials.", error));
            }
        });
    }

    @VisibleForTesting
    long getCurrentTimeInMillis() {
        return System.currentTimeMillis();
    }

}
