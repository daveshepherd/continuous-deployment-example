package com.jcraft.jsch;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class JSchTest {

    private JSch jsch;

    private String username;
    private String host;
    private String password;
    private String privateKeyFilename;

    @Before
    public void setup() {
        jsch = new JSch();

        username = "jboss-deployer";
        host = "10.180.8.27";
        password = "password";
        privateKeyFilename = "";
    }

    @Test
    public void testConnectToSshServerUsingPassword() throws JSchException {

        final Session session = jsch.getSession(username, host);

        session.setUserInfo(new BasicPasswordUserInfo(password));
        session.connect();

        assertTrue("session not connected", session.isConnected());

        session.disconnect();
    }

    @Test
    @Ignore
    public void testConnectToSshServerUsingKey() throws JSchException {

        jsch.addIdentity(privateKeyFilename);

        final Session session = jsch.getSession(username, host);

        session.connect();

        assertTrue("session not connected", session.isConnected());

        session.disconnect();
    }
}
