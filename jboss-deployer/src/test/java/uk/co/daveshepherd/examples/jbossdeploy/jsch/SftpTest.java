package uk.co.daveshepherd.examples.jbossdeploy.jsch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.co.daveshepherd.examples.jbossdeploy.jsch.model.BasicPasswordUserInfo;
import uk.co.daveshepherd.examples.jbossdeploy.jsch.model.DeployableFileSelector;
import uk.co.daveshepherd.examples.jbossdeploy.jsch.model.DeployableType;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SftpTest {

    private static final String TEST_RESOURCE_DIR = "src/test/resources/";
    private static final String TEST_FILE = "test-file";

    private JSch jsch;
    private Session session;
    private ChannelSftp sftpChannel;

    private String username;
    private String host;
    private String password;
    private String remoteDeployDirectory;

    @Before
    public void setup() throws JSchException, SftpException {
        jsch = new JSch();

        initialiseConnectionDetails();

        session = connect();
        assertTrue("session not connected", session.isConnected());

        sftpChannel = (ChannelSftp) session.openChannel("sftp");
        sftpChannel.connect();
        assertTrue("channel not connected", sftpChannel.isConnected());
        removeAllRemoteFiles();
    }

    @After
    public void tearDown() throws SftpException {
        if (sftpChannel.isConnected()) {
            removeAllRemoteFiles();
        }
        sftpChannel.disconnect();
        session.disconnect();
    }

    @Test
    public void testUploadFile() throws SftpException {
        uploadFile(TEST_FILE);

        assertFileExistsOnServer(TEST_FILE);
    }

    @Test
    public void testFileDeletion() throws SftpException {
        uploadFile(TEST_FILE);

        removeRemoteFile(TEST_FILE);

        assertFileDoesNotExistOnServer(TEST_FILE);
    }

    @Test
    public void testDirectoryContentsDeletion() throws SftpException {
        uploadFile(TEST_FILE);

       removeAllRemoteFiles();

        @SuppressWarnings("unchecked")
        final Vector<LsEntry> remoteFiles2 = sftpChannel.ls(remoteDeployDirectory);

        // expect two because of current and parent directories
        assertEquals("files have not been deleted", 2, remoteFiles2.size());
    }

    @Test
    public void testListDirectoryContents() throws JSchException, SftpException {
        uploadFile("non-matching-file");
        uploadFile("non-matching-file.war");
        uploadFile("test-file");
        uploadFile("test-file.war");
        uploadFile("test-file-0.0.1.war");
        uploadFile("test-file-0.0.1-SNAPSHOT.war");
        

        final DeployableFileSelector fileSelector = new DeployableFileSelector(TEST_FILE, DeployableType.WAR);

        sftpChannel.ls(remoteDeployDirectory, fileSelector);

        Collection<LsEntry> matchingEntries = fileSelector.getMatchingEntries();
        assertResultsMatchCriteria(matchingEntries);
        assertEquals("number of matching files", 3, matchingEntries.size());
    }

    private void initialiseConnectionDetails() {
        username = "jboss-deployer";
        host = "10.180.8.27";
        password = "password";
        remoteDeployDirectory = "/home/jboss-deployer/deploy/";
    }

    private Session connect() throws JSchException {
        final Session session = jsch.getSession(username, host);

        session.setUserInfo(new BasicPasswordUserInfo(password));
        session.connect();

        assertTrue("session not connected", session.isConnected());
        return session;
    }

    private void uploadFile(final String filename) throws SftpException {
        sftpChannel.put(TEST_RESOURCE_DIR + filename, remoteDeployDirectory + filename, ChannelSftp.OVERWRITE);
    }

    private void removeRemoteFile(String filename) throws SftpException {
        sftpChannel.rm(remoteDeployDirectory + filename);
    }

    private void removeAllRemoteFiles() throws SftpException {
        sftpChannel.rm(remoteDeployDirectory + "*");
    }

    private boolean findFileInList(final Vector<LsEntry> fileList, final String expectedfile) {
        boolean found = false;
        for (final LsEntry entry : fileList) {
            if (entry.getFilename().equals(expectedfile)) {
                found = true;
            }
        }
        return found;
    }

    private void assertFileExistsOnServer(String filename) throws SftpException {
        @SuppressWarnings("unchecked")
        final Vector<LsEntry> remoteFiles = sftpChannel.ls(remoteDeployDirectory);
    
        assertTrue("file should be on server", findFileInList(remoteFiles, filename));
    }

    private void assertFileDoesNotExistOnServer(String filename) throws SftpException {
        @SuppressWarnings("unchecked")
        final Vector<LsEntry> remoteFiles = sftpChannel.ls(remoteDeployDirectory);
    
        assertFalse("file should not be on server", findFileInList(remoteFiles, filename));
    }

    private void assertResultsMatchCriteria(final Collection<LsEntry> directoryContents) {
        for (final LsEntry entry : directoryContents) {
            assertTrue("incorrect prefix", entry.getFilename().startsWith(TEST_FILE));
            assertTrue("incorrect type", entry.getFilename().endsWith(DeployableType.WAR.getFileSuffix()));
        }
    }
}
