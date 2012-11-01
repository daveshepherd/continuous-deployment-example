package uk.co.daveshepherd.maven.plugin.jboss.deployer.jsch.model;

import com.jcraft.jsch.UserInfo;

public class BasicPasswordUserInfo implements UserInfo {

    private final String password;

    public BasicPasswordUserInfo(final String password) {
        this.password = password;
    }

    public void showMessage(final String message) {
    }

    public boolean promptYesNo(final String message) {
        return true;
    }

    public boolean promptPassword(final String message) {
        return true;
    }

    public boolean promptPassphrase(final String message) {
        return true;
    }

    public String getPassword() {
        return password;
    }

    public String getPassphrase() {
        return null;
    }
}
