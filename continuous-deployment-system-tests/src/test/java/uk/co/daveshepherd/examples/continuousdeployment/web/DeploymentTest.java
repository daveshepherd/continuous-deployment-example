package uk.co.daveshepherd.examples.continuousdeployment.web;

import static org.junit.Assert.assertEquals;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

public class DeploymentTest {

    @Test
    public void testDeployment() throws Exception {
        final HttpGet request = new HttpGet("https://ds-dev.j.layershift.co.uk/continous-deployment/");
        try {
            final DefaultHttpClient httpclient = new DefaultHttpClient();

            final HttpResponse response = httpclient.execute(request);

            int status = -1;

            status = response.getStatusLine().getStatusCode();

            assertEquals("status should be success", HttpStatus.SC_OK, status);
        } finally {
            request.releaseConnection();
        }
    }

}
