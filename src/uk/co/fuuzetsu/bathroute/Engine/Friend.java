package uk.co.fuuzetsu.bathroute.Engine;

import java.util.ArrayList;
import java.util.List;

public class Friend {
    private final Integer key;
    private final List<String> aliases;

    public Friend(final Integer key, final List<String> aliases) {
        this.key = key;
        this.aliases = aliases;
    }

    public Friend(final Integer key) {
        this(key, new ArrayList<String>());
    }


    public Integer getKey() {
        return this.key;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

}
