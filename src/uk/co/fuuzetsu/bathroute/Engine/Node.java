package uk.co.fuuzetsu.bathroute.engine;

import android.location.Location;
import java.util.List;
import uk.co.fuuzetsu.bathroute.engine.Pair;
import uk.co.fuuzetsu.bathroute.engine.Utils;
import fj.data.Option;

public class Node {
    private final Integer id;
    private final Location loc;

    /* Double is for cost */
    private final List<Integer> neighbours;

    private final Option<String> name;

    public Node(final Integer id, final Location loc,
                final List<Integer> n, final Option<String> name) {
        this.id = id;
        this.loc = loc;
        this.neighbours = n;
        this.name = name;
    }

    public Location getLocation() {
        return this.loc;
    }

    public Integer getId() {
        return this.id;
    }

    public List<Integer> getNeighbours() {
        return this.neighbours;
    }

    @Override
    public String toString() {
    return
        "Incomplete Node id " + id.toString() + "\n" +
        "Name: " + Utils.optP(name) + "\n" +
        "Location: " + loc.toString() + "\n" +
        "Neighbours: " + neighbours.toString() + "\n";
    }


}
