package uk.co.daveshepherd.maven.plugin.jboss.deployer.jsch.model;

import java.util.ArrayList;
import java.util.Collection;

import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.ChannelSftp.LsEntrySelector;

public class DeployableFileSelector implements LsEntrySelector {
    private final Collection<LsEntry> matchingEntries = new ArrayList<LsEntry>();
    private final String prefix;
    private final DeployableType type;

    public DeployableFileSelector(final String prefix, final DeployableType type) {
        this.prefix = prefix;
        this.type = type;
    }

    public int select(final LsEntry entry) {
        final String filename = entry.getFilename();
        if (doesFilenameMatch(filename)) {
            matchingEntries.add(entry);
        }
        return CONTINUE;
    }

    protected boolean doesFilenameMatch(final String filename) {
        if (filename == null) {
            return false;
        }
        return filename.endsWith(type.getFileSuffix()) && filename.startsWith(prefix);
    }

    public Collection<LsEntry> getMatchingEntries() {
        return matchingEntries;
    }
}