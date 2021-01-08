/*
 * ParameterBuilder.java
 *
 * Copyright (c) 2015 Auth0 (http://auth0.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.auth0.android.authentication

import java.util.*

/**
 * Builder for Auth0 Authentication API parameters
 * You can build your parameters like this
 * ```
 * val parameters = ParameterBuilder.newBuilder()
 * .setClientId("{CLIENT_ID}")
 * .setConnection("{CONNECTION}")
 * .set("{PARAMETER_NAME}", "{PARAMETER_VALUE}")
 * .asDictionary()
 * ```
 *
 * @see ParameterBuilder.newBuilder
 * @see ParameterBuilder.newAuthenticationBuilder
 */
public class ParameterBuilder private constructor(parameters: Map<String, String>) {
    private val parameters: MutableMap<String, String>

    /**
     * Sets the 'client_id' parameter
     *
     * @param clientId the application's client id
     * @return itself
     */
    public fun setClientId(clientId: String): ParameterBuilder {
        return set(CLIENT_ID_KEY, clientId)
    }

    /**
     * Sets the 'grant_type' parameter
     *
     * @param grantType grant type
     * @return itself
     */
    public fun setGrantType(grantType: String): ParameterBuilder {
        return set(GRANT_TYPE_KEY, grantType)
    }

    /**
     * Sets the 'connection' parameter
     *
     * @param connection name of the connection
     * @return itself
     */
    public fun setConnection(connection: String): ParameterBuilder {
        return set(CONNECTION_KEY, connection)
    }

    /**
     * Sets the 'realm' parameter. A realm identifies the host against which the authentication will be made, and usually helps to know which username and password to use.
     *
     * @param realm name of the realm
     * @return itself
     */
    public fun setRealm(realm: String): ParameterBuilder {
        return set(REALM_KEY, realm)
    }

    /**
     * Sets the 'scope' parameter.
     *
     * @param scope a scope value
     * @return itself
     */
    public fun setScope(scope: String): ParameterBuilder {
        return set(SCOPE_KEY, scope)
    }

    /**
     * Sets the 'audience' parameter.
     *
     * @param audience an audience value
     * @return itself
     */
    public fun setAudience(audience: String): ParameterBuilder {
        return set(AUDIENCE_KEY, audience)
    }

    /**
     * Sets the 'refresh_token' parameter
     *
     * @param refreshToken a access token
     * @return itself
     */
    public fun setRefreshToken(refreshToken: String): ParameterBuilder {
        return set(REFRESH_TOKEN_KEY, refreshToken)
    }

    /**
     * Sets the 'send' parameter
     *
     * @param passwordlessType the type of passwordless login
     * @return itself
     */
    public fun setSend(passwordlessType: PasswordlessType): ParameterBuilder {
        return set(SEND_KEY, passwordlessType.value)
    }

    /**
     * Sets a parameter
     *
     * @param key   parameter name
     * @param value parameter value. A null value will remove the key if present.
     * @return itself
     */
    public operator fun set(key: String, value: String?): ParameterBuilder {
        if (value == null) {
            parameters.remove(key)
        } else {
            parameters[key] = value
        }
        return this
    }

    /**
     * Adds all parameter from a map
     *
     * @param parameters map with parameters to add. Null values will be skipped.
     * @return itself
     */
    public fun addAll(parameters: Map<String, String?>): ParameterBuilder {
        parameters.filterValues { it != null }.map { this.parameters.put(it.key, it.value!!) }
        return this
    }

    /**
     * Clears all existing parameters
     *
     * @return itself
     */
    public fun clearAll(): ParameterBuilder {
        parameters.clear()
        return this
    }

    /**
     * Create a [Map] with all the parameters
     *
     * @return all parameters added previously as a [Map]
     */
    public fun asDictionary(): Map<String, String> {
        return parameters.toMap()
    }

    public companion object {
        public const val GRANT_TYPE_REFRESH_TOKEN: String = "refresh_token"
        public const val GRANT_TYPE_PASSWORD: String = "password"
        public const val GRANT_TYPE_PASSWORD_REALM: String =
            "http://auth0.com/oauth/grant-type/password-realm"
        public const val GRANT_TYPE_AUTHORIZATION_CODE: String = "authorization_code"
        public const val GRANT_TYPE_MFA_OTP: String = "http://auth0.com/oauth/grant-type/mfa-otp"
        public const val GRANT_TYPE_PASSWORDLESS_OTP: String =
            "http://auth0.com/oauth/grant-type/passwordless/otp"
        public const val GRANT_TYPE_TOKEN_EXCHANGE: String =
            "urn:ietf:params:oauth:grant-type:token-exchange"
        public const val SCOPE_OPENID: String = "openid"
        public const val SCOPE_OFFLINE_ACCESS: String = "openid offline_access"
        public const val SCOPE_KEY: String = "scope"
        public const val REFRESH_TOKEN_KEY: String = "refresh_token"
        public const val CONNECTION_KEY: String = "connection"
        public const val REALM_KEY: String = "realm"
        public const val SEND_KEY: String = "send"
        public const val CLIENT_ID_KEY: String = "client_id"
        public const val GRANT_TYPE_KEY: String = "grant_type"
        public const val AUDIENCE_KEY: String = "audience"

        /**
         * Creates a new instance of the builder using default values for login request, e.g. 'openid' for scope.
         *
         * @return a new builder
         */
        @JvmStatic
        public fun newAuthenticationBuilder(): ParameterBuilder {
            return newBuilder()
                .setScope(SCOPE_OPENID)
        }
        /**
         * Creates a new instance of the builder.
         *
         * @param parameters an optional map of initial parameters
         * @return a new builder
         */
        @JvmStatic
        @JvmOverloads
        public fun newBuilder(parameters: Map<String, String> = mutableMapOf()): ParameterBuilder {
            return ParameterBuilder(parameters)
        }
    }

    init {
        this.parameters = parameters.toMutableMap()
    }
}