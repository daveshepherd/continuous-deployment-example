package uk.co.daveshepherd.examples.continuousdeployment.web;

import static java.lang.Thread.sleep;
import static org.junit.Assert.fail;

import java.util.concurrent.Callable;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

public class DeploymentTest {

    @Test
    public void testDeployment() throws Exception {

        final HttpGet request = new HttpGet("http://ds-dev.j.layershift.co.uk/continous-deployment/");
        try {
            final DefaultHttpClient httpclient = new DefaultHttpClient();

            retry(new RetryCall() {
                public Boolean call() {
                    HttpResponse response;
                    try {
                        response = httpclient.execute(request);
                    } catch (final Exception e) {
                        System.err.println("Exception executing http request: " + e.getMessage());
                        return false;
                    }

                    final int status = response.getStatusLine().getStatusCode();

                    System.out.println("HTTP status code: " + status);
                    return HttpStatus.SC_OK == status;
                }
            });

        } finally {
            request.releaseConnection();
        }
    }

    public void retry(final RetryCall block) throws InterruptedException {

        int retry = 8;
        long intervalBetweenRetriesInMillis = 1000;

        for (; retry > 0; retry--) {
            System.out.println("Retry block, iteration: " + retry);
            if (block.call()) {
                return;
            }
            sleep(intervalBetweenRetriesInMillis);
            intervalBetweenRetriesInMillis = intervalBetweenRetriesInMillis * 2;
        }
        fail("failed to do retry");
    }

    private interface RetryCall extends Callable<Boolean> {

        /**
         * @return true if the call was successful
         */
        Boolean call();
    }
}
