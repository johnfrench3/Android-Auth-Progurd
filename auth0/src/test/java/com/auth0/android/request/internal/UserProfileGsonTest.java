package com.auth0.android.request.internal;

import com.auth0.android.result.UserProfile;
import com.google.gson.JsonParseException;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Locale;

import static com.auth0.android.util.UserIdentityMatcher.isUserIdentity;
import static com.auth0.android.util.UserProfileMatcher.isNormalizedProfile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.collection.IsMapWithSize.aMapWithSize;
import static org.hamcrest.collection.IsMapWithSize.anEmptyMap;

public class UserProfileGsonTest extends GsonBaseTest {
    private static final String NICKNAME = "a0";
    private static final String NAME = "info @ auth0";
    private static final String ID = "auth0|1234567890";
    private static final String PROFILE_OAUTH = "src/test/resources/profile_oauth.json";
    private static final String PROFILE_FULL = "src/test/resources/profile_full.json";
    private static final String PROFILE_BASIC = "src/test/resources/profile_basic.json";
    private static final String PROFILE = "src/test/resources/profile.json";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        gson = GsonProvider.buildGson();
    }

    @Test
    public void shouldFailWithInvalidJson() throws Exception {
        expectedException.expect(JsonParseException.class);
        pojoFrom(json(INVALID), UserProfile.class);
    }

    @Test
    public void shouldFailWithEmptyJson() throws Exception {
        expectedException.expect(JsonParseException.class);
        pojoFrom(json(EMPTY_OBJECT), UserProfile.class);
    }

    @Test
    public void shouldNotRequireUserId() throws Exception {
        UserProfile userProfile = pojoFrom(new StringReader("{\n" +
                "  \"picture\": \"https://secure.gravatar.com/avatar/cfacbe113a96fdfc85134534771d88b4?s=480&r=pg&d=https%3A%2F%2Fssl.gstatic.com%2Fs2%2Fprofiles%2Fimages%2Fsilhouette80.png\",\n" +
                "  \"name\": \"info @ auth0\",\n" +
                "  \"nickname\": \"a0\",\n" +
                "  \"identities\": [\n" +
                "    {\n" +
                "      \"user_id\": \"1234567890\",\n" +
                "      \"provider\": \"auth0\",\n" +
                "      \"connection\": \"Username-Password-Authentication\",\n" +
                "      \"isSocial\": false\n" +
                "    }\n" +
                "  ],\n" +
                "  \"created_at\": \"2014-07-06T18:33:49.005Z\"\n" +
                "}"
        ), UserProfile.class);
        assertThat(userProfile, is(notNullValue()));
    }

    @Test
    public void shouldNotRequireName() throws Exception {
        UserProfile userProfile = pojoFrom(new StringReader("{\n" +
                "  \"picture\": \"https://secure.gravatar.com/avatar/cfacbe113a96fdfc85134534771d88b4?s=480&r=pg&d=https%3A%2F%2Fssl.gstatic.com%2Fs2%2Fprofiles%2Fimages%2Fsilhouette80.png\",\n" +
                "  \"nickname\": \"a0\",\n" +
                "  \"user_id\": \"auth0|1234567890\",\n" +
                "  \"identities\": [\n" +
                "    {\n" +
                "      \"user_id\": \"1234567890\",\n" +
                "      \"provider\": \"auth0\",\n" +
                "      \"connection\": \"Username-Password-Authentication\",\n" +
                "      \"isSocial\": false\n" +
                "    }\n" +
                "  ],\n" +
                "  \"created_at\": \"2014-07-06T18:33:49.005Z\"\n" +
                "}"
        ), UserProfile.class);
        assertThat(userProfile, is(notNullValue()));
    }

    @Test
    public void shouldNotRequireNickname() throws Exception {
        UserProfile userProfile = pojoFrom(new StringReader("{\n" +
                "  \"picture\": \"https://secure.gravatar.com/avatar/cfacbe113a96fdfc85134534771d88b4?s=480&r=pg&d=https%3A%2F%2Fssl.gstatic.com%2Fs2%2Fprofiles%2Fimages%2Fsilhouette80.png\",\n" +
                "  \"name\": \"info @ auth0\",\n" +
                "  \"user_id\": \"auth0|1234567890\",\n" +
                "  \"identities\": [\n" +
                "    {\n" +
                "      \"user_id\": \"1234567890\",\n" +
                "      \"provider\": \"auth0\",\n" +
                "      \"connection\": \"Username-Password-Authentication\",\n" +
                "      \"isSocial\": false\n" +
                "    }\n" +
                "  ],\n" +
                "  \"created_at\": \"2014-07-06T18:33:49.005Z\"\n" +
                "}"
        ), UserProfile.class);
        assertThat(userProfile, is(notNullValue()));
    }

    @Test
    public void shouldNotRequirePicture() throws Exception {
        UserProfile userProfile = pojoFrom(new StringReader("{\n" +
                "  \"name\": \"info @ auth0\",\n" +
                "  \"nickname\": \"a0\",\n" +
                "  \"user_id\": \"auth0|1234567890\",\n" +
                "  \"identities\": [\n" +
                "    {\n" +
                "      \"user_id\": \"1234567890\",\n" +
                "      \"provider\": \"auth0\",\n" +
                "      \"connection\": \"Username-Password-Authentication\",\n" +
                "      \"isSocial\": false\n" +
                "    }\n" +
                "  ],\n" +
                "  \"created_at\": \"2014-07-06T18:33:49.005Z\"\n" +
                "}"
        ), UserProfile.class);
        assertThat(userProfile, is(notNullValue()));
    }

    @Test
    public void shouldReturnOAuthProfile() throws Exception {
        UserProfile profile = pojoFrom(json(PROFILE_OAUTH), UserProfile.class);
        assertThat(profile.getId(), is("google-oauth2|9883254263433883220"));
        assertThat(profile.getName(), is(nullValue()));
        assertThat(profile.getNickname(), is(nullValue()));
        assertThat(profile.getPictureURL(), is(nullValue()));
        assertThat(profile.getIdentities(), is(nullValue()));
        assertThat(profile.getUserMetadata(), anEmptyMap());
        assertThat(profile.getAppMetadata(), anEmptyMap());
        assertThat(profile.getExtraInfo(), aMapWithSize(1));
        assertThat(profile.getExtraInfo(), hasEntry("sub", (Object) "google-oauth2|9883254263433883220"));
    }

    @Test
    public void shouldReturnProfileWithOnlyRequiredValues() throws Exception {
        UserProfile profile = pojoFrom(json(PROFILE_BASIC), UserProfile.class);
        assertThat(profile, isNormalizedProfile(ID, NAME, NICKNAME));
        assertThat(profile.getIdentities(), hasSize(1));
        assertThat(profile.getIdentities(), hasItem(isUserIdentity("1234567890", "auth0", "Username-Password-Authentication")));
        assertThat(profile.getUserMetadata(), anEmptyMap());
        assertThat(profile.getAppMetadata(), anEmptyMap());
        assertThat(profile.getExtraInfo(), anEmptyMap());
    }

    @Test
    public void shouldReturnNormalizedProfile() throws Exception {
        UserProfile profile = pojoFrom(json(PROFILE), UserProfile.class);
        assertThat(profile, isNormalizedProfile(ID, NAME, NICKNAME));
        assertThat(profile.getIdentities(), hasSize(1));
        assertThat(profile.getIdentities(), hasItem(isUserIdentity("1234567890", "auth0", "Username-Password-Authentication")));
        assertThat(profile.getUserMetadata(), anEmptyMap());
        assertThat(profile.getAppMetadata(), anEmptyMap());
        assertThat(profile.getExtraInfo(), notNullValue());
    }

    @Test
    public void shouldReturnProfileWithOptionalFields() throws Exception {
        UserProfile profile = pojoFrom(json(PROFILE_FULL), UserProfile.class);
        assertThat(profile, isNormalizedProfile(ID, NAME, NICKNAME));
        assertThat(profile.getEmail(), equalTo("info@auth0.com"));
        assertThat(profile.getGivenName(), equalTo("John"));
        assertThat(profile.getFamilyName(), equalTo("Foobar"));
        assertThat(profile.isEmailVerified(), is(false));
        assertThat(profile.getCreatedAt(), equalTo(new SimpleDateFormat(GsonProvider.DATE_FORMAT, Locale.US).parse("2014-07-06T18:33:49.005Z")));
    }

    @Test
    public void shouldReturnProfileWithMultipleIdentities() throws Exception {
        UserProfile profile = pojoFrom(json(PROFILE_FULL), UserProfile.class);
        assertThat(profile, isNormalizedProfile(ID, NAME, NICKNAME));
        assertThat(profile.getIdentities(), hasItem(isUserIdentity("1234567890", "auth0", "Username-Password-Authentication")));
        assertThat(profile.getIdentities(), hasItem(isUserIdentity("999997950999976", "facebook", "facebook")));
    }

    @Test
    public void shouldReturnProfileWithExtraInfo() throws Exception {
        UserProfile profile = pojoFrom(json(PROFILE_FULL), UserProfile.class);
        assertThat(profile, isNormalizedProfile(ID, NAME, NICKNAME));
        assertThat(profile.getExtraInfo(), hasEntry("multifactor", (Object) Collections.singletonList("google-authenticator")));
        assertThat(profile.getExtraInfo(), not(anyOf(new Matcher[]{hasKey("user_id"), hasKey("name"), hasKey("nickname"), hasKey("picture"), hasKey("email"), hasKey("created_at")})));
        assertThat(profile.getExtraInfo(), not(anyOf(new Matcher[]{hasKey("identities"), hasKey("user_metadata"), hasKey("app_metadata")})));
    }

    @Test
    public void shouldReturnProfileWithMetadata() throws Exception {
        UserProfile profile = pojoFrom(json(PROFILE_FULL), UserProfile.class);
        assertThat(profile, isNormalizedProfile(ID, NAME, NICKNAME));
        assertThat(profile.getUserMetadata(), hasEntry("first_name", (Object) "Info"));
        assertThat(profile.getUserMetadata(), hasEntry("last_name", (Object) "Auth0"));
        assertThat(profile.getUserMetadata(), hasEntry("first_name", (Object) "Info"));
        assertThat(profile.getAppMetadata(), hasEntry("role", (Object) "admin"));
        assertThat(profile.getAppMetadata(), hasEntry("tier", (Object) 2.0));
        assertThat(profile.getAppMetadata(), hasEntry("blocked", (Object) false));
    }

}
