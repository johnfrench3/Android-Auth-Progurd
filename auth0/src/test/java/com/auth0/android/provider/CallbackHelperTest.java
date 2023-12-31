package com.auth0.android.provider;

import android.net.Uri;

import org.hamcrest.collection.IsMapWithSize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Map;

import static androidx.test.espresso.intent.matcher.UriMatchers.hasHost;
import static androidx.test.espresso.intent.matcher.UriMatchers.hasScheme;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsMapWithSize.aMapWithSize;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(RobolectricTestRunner.class)
public class CallbackHelperTest {

    private static final String PACKAGE_NAME = "com.auth0.lock.android.app";
    private static final String INVALID_DOMAIN = "not.-valid-domain";
    private static final String DOMAIN = "https://my-domain.auth0.com";
    private static final String DOMAIN_WITH_TRAILING_SLASH = "https://my-domain.auth0.com/";
    private static final String DEFAULT_SCHEME = "https";

    @Test
    public void shouldGetCallbackURI() {
        final Uri expected = Uri.parse(DOMAIN + "/android/" + PACKAGE_NAME + "/callback");
        final Uri result = Uri.parse(CallbackHelper.getCallbackUri(DEFAULT_SCHEME, PACKAGE_NAME, DOMAIN));

        assertThat(result, hasScheme("https"));
        assertThat(result, hasHost("my-domain.auth0.com"));
        List<String> path = result.getPathSegments();
        assertThat(path.get(0), is("android"));
        assertThat(path.get(1), is(PACKAGE_NAME));
        assertThat(path.get(2), is("callback"));
        assertThat(result, equalTo(expected));
    }

    @Test
    public void shouldGetCallbackURIWithCustomScheme() {
        final Uri expected = Uri.parse("myapp://" + "my-domain.auth0.com" + "/android/" + PACKAGE_NAME + "/callback");
        final Uri result = Uri.parse(CallbackHelper.getCallbackUri("myapp", PACKAGE_NAME, DOMAIN));

        assertThat(result, hasScheme("myapp"));
        assertThat(result, hasHost("my-domain.auth0.com"));
        List<String> path = result.getPathSegments();
        assertThat(path.get(0), is("android"));
        assertThat(path.get(1), is(PACKAGE_NAME));
        assertThat(path.get(2), is("callback"));
        assertThat(result, equalTo(expected));
    }

    @Test
    public void shouldGetCallbackURIIfDomainEndsWithSlash() {
        final Uri expected = Uri.parse(DOMAIN + "/android/" + PACKAGE_NAME + "/callback");
        final Uri result = Uri.parse(CallbackHelper.getCallbackUri(DEFAULT_SCHEME, PACKAGE_NAME, DOMAIN_WITH_TRAILING_SLASH));

        assertThat(result, hasScheme("https"));
        assertThat(result, hasHost("my-domain.auth0.com"));
        List<String> path = result.getPathSegments();
        assertThat(path.get(0), is("android"));
        assertThat(path.get(1), is(PACKAGE_NAME));
        assertThat(path.get(2), is("callback"));
        assertThat(result, equalTo(expected));
    }

    @Test
    public void shouldGetNullCallbackURIIfInvalidDomain() {
        String uri = CallbackHelper.getCallbackUri(DEFAULT_SCHEME, PACKAGE_NAME, INVALID_DOMAIN);
        assertThat(uri, nullValue());
    }

    @Test
    public void shouldParseQueryValuesWithEqual() {
        String uriString = "testandroid://auth.testandroid.com/android/com.testandroid.app/callback?error=unauthorized&error_description=Please%20verify%20your%20email%20before%20logging%20in.e%3Dfoo%2Bt2%40gmail.com&state=abscefg-QWERTYUIOPasdfgHJKLMNBVCXZdd";
        Uri uri = Uri.parse(uriString);
        final Map<String, String> values = CallbackHelper.getValuesFromUri(uri);

        assertThat(values, is(notNullValue()));
        assertThat(values, aMapWithSize(3));
        assertThat(values, hasEntry("error", "unauthorized"));
        assertThat(values, hasEntry("state", "abscefg-QWERTYUIOPasdfgHJKLMNBVCXZdd"));
        assertThat(values, hasEntry("error_description", "Please verify your email before logging in.e=foo+t2@gmail.com"));
    }

    @Test
    public void shouldParseQueryValues() {
        String uriString = "https://lbalmaceda.auth0.com/android/com.auth0.android.lock.app/callback?code=soMec0d3ML8B&state=810132b-486aa-4aa8-1768-a1dcd3368fae";
        Uri uri = Uri.parse(uriString);
        final Map<String, String> values = CallbackHelper.getValuesFromUri(uri);

        assertThat(values, is(notNullValue()));
        assertThat(values, aMapWithSize(2));
        assertThat(values, hasEntry("code", "soMec0d3ML8B"));
        assertThat(values, hasEntry("state", "810132b-486aa-4aa8-1768-a1dcd3368fae"));
    }

    @Test
    public void shouldParseFragmentValues() {
        String uriString = "https://lbalmaceda.auth0.com/android/com.auth0.android.lock.app/callback#code=soMec0d3ML8B&state=810132b-486aa-4aa8-1768-a1dcd3368fae";
        Uri uri = Uri.parse(uriString);
        final Map<String, String> values = CallbackHelper.getValuesFromUri(uri);

        assertThat(values, is(notNullValue()));
        assertThat(values, aMapWithSize(2));
        assertThat(values, hasEntry("code", "soMec0d3ML8B"));
        assertThat(values, hasEntry("state", "810132b-486aa-4aa8-1768-a1dcd3368fae"));
    }

    @Test
    public void shouldReturnEmptyQueryValues() {
        String uriString = "https://lbalmaceda.auth0.com/android/com.auth0.android.lock.app/callback?";
        Uri uri = Uri.parse(uriString);
        final Map<String, String> values = CallbackHelper.getValuesFromUri(uri);

        assertThat(values, is(notNullValue()));
        assertThat(values, IsMapWithSize.anEmptyMap());
    }

    @Test
    public void shouldReturnEmptyFragmentValues() {
        String uriString = "https://lbalmaceda.auth0.com/android/com.auth0.android.lock.app/callback#";
        Uri uri = Uri.parse(uriString);
        final Map<String, String> values = CallbackHelper.getValuesFromUri(uri);

        assertThat(values, is(notNullValue()));
        assertThat(values, IsMapWithSize.anEmptyMap());
    }

    @Test
    public void shouldGetEmptyValuesWhenQueryOrFragmentIsMissing() {
        String uriString = "https://my.website.com/some/page";
        Uri uri = Uri.parse(uriString);
        final Map<String, String> values = CallbackHelper.getValuesFromUri(uri);
        assertThat(values, notNullValue());
        assertThat(values, IsMapWithSize.anEmptyMap());
    }

    @Test
    public void shouldGetEmptyValuesWhenQueryIsEmpty() {
        String uriString = "https://my.website.com/some/page?";
        Uri uri = Uri.parse(uriString);
        final Map<String, String> values = CallbackHelper.getValuesFromUri(uri);
        assertThat(values, notNullValue());
        assertThat(values, IsMapWithSize.anEmptyMap());
    }

    @Test
    public void shouldGetEmptyValuesWhenQueryBeginsWithAmpersand() {
        String uriString = "https://my.website.com/some/page?&key_without_value";
        Uri uri = Uri.parse(uriString);
        final Map<String, String> values = CallbackHelper.getValuesFromUri(uri);
        assertThat(values, notNullValue());
        assertThat(values, IsMapWithSize.anEmptyMap());
    }

    @Test
    public void shouldGetEmptyValuesWhenFragmentIsEmpty() {
        String uriString = "https://my.website.com/some/page#";
        Uri uri = Uri.parse(uriString);
        final Map<String, String> values = CallbackHelper.getValuesFromUri(uri);
        assertThat(values, notNullValue());
        assertThat(values, IsMapWithSize.anEmptyMap());
    }

    @Test
    public void shouldGetEmptyValuesWhenFragmentBeginsWithAmpersand() {
        String uriString = "https://my.website.com/some/page#&key_without_value";
        Uri uri = Uri.parse(uriString);
        final Map<String, String> values = CallbackHelper.getValuesFromUri(uri);
        assertThat(values, notNullValue());
        assertThat(values, IsMapWithSize.anEmptyMap());
    }

    @Test
    public void shouldGetEmptyValuesWhenUriIsNull() {
        Uri uri = null;
        final Map<String, String> values = CallbackHelper.getValuesFromUri(uri);
        assertThat(values, notNullValue());
        assertThat(values, IsMapWithSize.anEmptyMap());
    }
}