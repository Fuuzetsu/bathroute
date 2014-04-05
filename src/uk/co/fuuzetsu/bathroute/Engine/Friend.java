package uk.co.fuuzetsu.bathroute.Engine;

import java.util.List;
import java.security.interfaces.RSAPublicKey;

public class Friend {
    private final RSAPublicKey key;
    private final List<String> aliases;

    public Friend(final RSAPublicKey key, final List<String> aliases) {
        this.key = key;
        this.aliases = aliases;
    }

    public RSAPublicKey getKey() {
        return this.key;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

}
