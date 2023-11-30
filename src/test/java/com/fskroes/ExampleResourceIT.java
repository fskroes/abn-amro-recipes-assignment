package com.fskroes;

import com.fskroes.boundary.RecipeBoundary;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;

import java.net.URL;

@QuarkusIntegrationTest
public class ExampleResourceIT extends ExampleResourceTest {
    // Execute the same tests but in packaged mode.

    @TestHTTPEndpoint(RecipeBoundary.class)
    @TestHTTPResource
    URL url;
}
