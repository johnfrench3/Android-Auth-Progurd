package com.auth0.android.request;

import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.result.Credentials;

import java.util.Map;

/**
 * Request to authenticate a user with Auth0 Authentication API
 */
public interface AuthenticationRequest extends Request<Credentials, AuthenticationException> {

    /**
     * Sets the 'grant_type' parameter
     *
     * @param grantType grant type
     * @return itself
     */
    AuthenticationRequest setGrantType(String grantType);

    /**
     * Sets the 'connection' parameter
     *
     * @param connection name of the connection
     * @return itself
     */
    AuthenticationRequest setConnection(String connection);

    /**
     * Sets the 'scope' parameter.
     *
     * @param scope a scope value
     * @return itself
     */
    AuthenticationRequest setScope(String scope);

    /**
     * Sets the 'device' parameter
     *
     * @param device a device name
     * @return itself
     */
    AuthenticationRequest setDevice(String device);

    /**
     * Sets the 'audience' parameter.
     *
     * @param audience an audience value
     * @return itself
     */
    AuthenticationRequest setAudience(String audience);

    /**
     * Sets the 'access_token' parameter
     *
     * @param accessToken a access token
     * @return itself
     */
    AuthenticationRequest setAccessToken(String accessToken);

    /**
     * All all entries of the map as parameters of this request
     *
     * @param parameters to be added to the request
     * @return itself
     */
    AuthenticationRequest addAuthenticationParameters(Map<String, Object> parameters);

}
