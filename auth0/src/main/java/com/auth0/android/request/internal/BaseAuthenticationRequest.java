package com.auth0.android.request.internal;

import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.request.AuthenticationRequest;
import com.auth0.android.result.Credentials;
import com.google.gson.Gson;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;

import java.util.Map;

import static com.auth0.android.authentication.ParameterBuilder.ACCESS_TOKEN_KEY;
import static com.auth0.android.authentication.ParameterBuilder.AUDIENCE_KEY;
import static com.auth0.android.authentication.ParameterBuilder.CONNECTION_KEY;
import static com.auth0.android.authentication.ParameterBuilder.DEVICE_KEY;
import static com.auth0.android.authentication.ParameterBuilder.GRANT_TYPE_KEY;
import static com.auth0.android.authentication.ParameterBuilder.SCOPE_KEY;

class BaseAuthenticationRequest extends SimpleRequest<Credentials, AuthenticationException> implements AuthenticationRequest {

    public BaseAuthenticationRequest(HttpUrl url, OkHttpClient client, Gson gson, String httpMethod, Class<Credentials> clazz) {
        super(url, client, gson, httpMethod, clazz, new AuthenticationErrorBuilder());
    }

    /**
     * Sets the 'grant_type' parameter
     *
     * @param grantType grant type
     * @return itself
     */
    @Override
    public AuthenticationRequest setGrantType(String grantType) {
        addParameter(GRANT_TYPE_KEY, grantType);
        return this;
    }

    /**
     * Sets the 'connection' parameter
     *
     * @param connection name of the connection
     * @return itself
     */
    @Override
    public AuthenticationRequest setConnection(String connection) {
        addParameter(CONNECTION_KEY, connection);
        return this;
    }

    /**
     * Sets the 'scope' parameter.
     *
     * @param scope a scope value
     * @return itself
     */
    public AuthenticationRequest setScope(String scope) {
        addParameter(SCOPE_KEY, scope);
        return this;
    }

    /**
     * Sets the 'device' parameter
     *
     * @param device a device name
     * @return itself
     */
    public AuthenticationRequest setDevice(String device) {
        addParameter(DEVICE_KEY, device);
        return this;
    }

    /**
     * Sets the 'audience' parameter.
     *
     * @param audience an audience value
     * @return itself
     */
    @Override
    public AuthenticationRequest setAudience(String audience) {
        addParameter(AUDIENCE_KEY, audience);
        return this;
    }

    /**
     * Sets the 'access_token' parameter
     *
     * @param accessToken a access token
     * @return itself
     */
    public AuthenticationRequest setAccessToken(String accessToken) {
        addParameter(ACCESS_TOKEN_KEY, accessToken);
        return this;
    }

    @Override
    public AuthenticationRequest addAuthenticationParameters(Map<String, Object> parameters) {
        addParameters(parameters);
        return this;
    }
}
