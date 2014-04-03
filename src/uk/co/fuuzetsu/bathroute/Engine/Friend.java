package uk.co.fuuzetsu.bathroute.Engine;

import java.util.List;
import java.security.PublicKey;

public class Friend {
    private final PublicKey key;
    private final List<String> aliases;

    public Friend(final PublicKey key, final List<String> aliases) {
        this.key = key;
        this.aliases = aliases;
    }

    public PublicKey getKey() {
        return this.key;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

}
