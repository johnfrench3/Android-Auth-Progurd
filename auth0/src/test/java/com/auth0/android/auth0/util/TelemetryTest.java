package com.auth0.android.auth0.util;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class TelemetryTest {

    private Telemetry telemetry;

    @Before
    public void setUp() throws Exception {
        telemetry = new Telemetry("auth0-java", "1.0.0");
    }

    @Test
    public void shouldReturnBase64() throws Exception {
        assertThat(telemetry.getValue(), is(notNullValue()));
    }

    @Test
    public void shouldReturnNullWhenNoInfoIsProvided() throws Exception {
        telemetry = new Telemetry(null, null);
        assertThat(telemetry.getValue(), is(nullValue()));
    }
}