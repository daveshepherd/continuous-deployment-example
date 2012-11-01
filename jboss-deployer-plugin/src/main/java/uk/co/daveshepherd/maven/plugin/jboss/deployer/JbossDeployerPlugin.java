package uk.co.daveshepherd.maven.plugin.jboss.deployer;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @goal deploy
 */
public class JbossDeployerPlugin extends AbstractMojo
{
    public void execute() throws MojoExecutionException
    {
        getLog().info( "Hello, world." );
    }
}