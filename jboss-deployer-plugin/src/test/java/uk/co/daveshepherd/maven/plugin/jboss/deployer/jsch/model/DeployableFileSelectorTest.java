package uk.co.daveshepherd.maven.plugin.jboss.deployer.jsch.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uk.co.daveshepherd.maven.plugin.jboss.deployer.jsch.model.DeployableFileSelector;
import uk.co.daveshepherd.maven.plugin.jboss.deployer.jsch.model.DeployableType;

public class DeployableFileSelectorTest {

    @Test
    public void testNullFilenameShouldReturnFalse() {
        final DeployableFileSelector selector = new DeployableFileSelector("prefix", DeployableType.WAR);
        final boolean result = selector.doesFilenameMatch(null);
        assertFalse("null filename should not match", result);
    }

    @Test
    public void testEmptyStringFilenameShouldReturnFalse() {
        final DeployableFileSelector selector = new DeployableFileSelector("prefix", DeployableType.WAR);
        final boolean result = selector.doesFilenameMatch("");
        assertFalse("empty filename should not match", result);
    }

    @Test
    public void testNonMatchingFilenameShouldReturnFalse() {
        final DeployableFileSelector selector = new DeployableFileSelector("prefix", DeployableType.WAR);
        final String filename = "somefilename";
        final boolean result = selector.doesFilenameMatch(filename);
        assertFalse(filename + " should not match", result);
    }

    @Test
    public void testExactlyMatchingWarFilenameShouldReturnTrue1() {
        final DeployableFileSelector selector = new DeployableFileSelector("prefix", DeployableType.WAR);
        final String filename = "prefix.war";
        final boolean result = selector.doesFilenameMatch(filename);
        assertTrue(filename + " should match", result);
    }

    @Test
    public void testExactlyMatchingEarFilenameShouldReturnTrue() {
        final DeployableFileSelector selector = new DeployableFileSelector("prefix", DeployableType.EAR);
        final String filename = "prefix.ear";
        final boolean result = selector.doesFilenameMatch(filename);
        assertTrue(filename + " should match", result);
    }

    @Test
    public void testExactlyMatchingJndiFilenameShouldReturnTrue2() {
        final DeployableFileSelector selector = new DeployableFileSelector("prefix", DeployableType.JNDI);
        final String filename = "prefix-jndi-binding-service.xml";
        final boolean result = selector.doesFilenameMatch(filename);
        assertTrue(filename + " should match", result);
    }

    @Test
    public void testMatchingFilenameShouldReturnTrue1() {
        final DeployableFileSelector selector = new DeployableFileSelector("prefix", DeployableType.WAR);
        final String filename = "prefix-0.0.1.war";
        final boolean result = selector.doesFilenameMatch(filename);
        assertTrue(filename + " should match", result);
    }

    @Test
    public void testMatchingFilenameShouldReturnTrue2() {
        final DeployableFileSelector selector = new DeployableFileSelector("prefix", DeployableType.WAR);
        final String filename = "prefix-0.0.1-SNAPSHOT.war";
        final boolean result = selector.doesFilenameMatch(filename);
        assertTrue(filename + " should match", result);
    }

    @Test
    public void testMatchingPrefixOnlyShouldReturnFalse() {
        final DeployableFileSelector selector = new DeployableFileSelector("prefix", DeployableType.WAR);
        final String filename = "prefix.zip";
        final boolean result = selector.doesFilenameMatch(filename);
        assertFalse(filename + " should not match", result);
    }

    @Test
    public void testMatchingTypeOnlyShouldReturnFalse() {
        final DeployableFileSelector selector = new DeployableFileSelector("prefix", DeployableType.WAR);
        final String filename = "somefile.war";
        final boolean result = selector.doesFilenameMatch(filename);
        assertFalse(filename + " should not match", result);
    }
}
