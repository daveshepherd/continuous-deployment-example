package uk.co.daveshepherd.maven.plugin.jboss.deployer.jsch.model;

public enum DeployableType {

    WAR(".war"),
    EAR(".ear"),
    JAR(".jar"),
    JNDI("-jndi-binding-service.xml");

    private final String suffix;

    private DeployableType(final String suffix) {
        this.suffix = suffix;
    }

    public String getFileSuffix() {
        return suffix;
    }
}
