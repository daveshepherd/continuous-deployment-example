package uk.co.daveshepherd.maven.plugin.jboss.deployer.jsch;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import uk.co.daveshepherd.maven.plugin.jboss.deployer.jsch.model.BasicPasswordUserInfo;

public class SshConnectionTest {

    private JSch jsch;

    private String username;
    private String host;
    private String password;
    private String privateKeyFilename;

    @Before
    public void setup() {
        jsch = new JSch();

        username = "jboss-deployer";
        host = "10.180.9.25";
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
