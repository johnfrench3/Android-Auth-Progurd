/*
 * UserProfile.java
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

package com.auth0.android.result;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that holds the information of a user's profile in Auth0.
 * Used both in {@link com.auth0.android.management.UsersAPIClient} and {@link com.auth0.android.authentication.AuthenticationAPIClient}.
 */
public class UserProfile implements Serializable {
    private String id;
    private String name;
    private String nickname;
    private String pictureURL;

    private String email;
    private boolean emailVerified;
    private String givenName;
    private String familyName;
    private Map<String, Object> userMetadata;
    private Map<String, Object> appMetadata;
    private Date createdAt;
    private List<UserIdentity> identities;

    private Map<String, Object> extraInfo;

    public UserProfile(String id, String name, String nickname, String pictureURL, String email, boolean emailVerified, String familyName, Date createdAt, List<UserIdentity> identities, Map<String, Object> extraInfo, Map<String, Object> userMetadata, Map<String, Object> appMetadata, String givenName) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.pictureURL = pictureURL;
        this.email = email;
        this.emailVerified = emailVerified;
        this.givenName = givenName;
        this.familyName = familyName;
        this.userMetadata = userMetadata;
        this.appMetadata = appMetadata;
        this.createdAt = createdAt;
        this.identities = identities;
        this.extraInfo = extraInfo;
    }

    /**
     * Getter for the unique Identifier of the user. If this represents a Full User Profile (Management API) the 'id' field will be returned.
     * If the value is not present, it will be considered a User Information and the id will be obtained from the 'sub' claim.
     *
     * @return the unique identifier of the user.
     */
    public String getId() {
        if (id != null) {
            return id;
        }
        return getExtraInfo().containsKey("sub") ? (String) getExtraInfo().get("sub") : null;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public Map<String, Object> getUserMetadata() {
        return userMetadata != null ? userMetadata : Collections.<String, Object>emptyMap();
    }

    public Map<String, Object> getAppMetadata() {
        return appMetadata != null ? appMetadata : Collections.<String, Object>emptyMap();
    }

    /**
     * Returns extra information of the profile that is not part of the normalized profile
     *
     * @return a map with user's extra information found in the profile
     */
    public Map<String, Object> getExtraInfo() {
        return extraInfo != null ? new HashMap<>(extraInfo) : Collections.<String, Object>emptyMap();
    }

    /**
     * List of the identities from a Identity Provider associated to the user.
     *
     * @return a list of identity provider information.
     */
    public List<UserIdentity> getIdentities() {
        return identities;
    }
}
