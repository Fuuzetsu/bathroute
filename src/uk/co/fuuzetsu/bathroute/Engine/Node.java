package uk.co.fuuzetsu.bathroute.Engine;

import android.location.Location;
import java.util.List;
import uk.co.fuuzetsu.bathroute.Engine.Utils;
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
    public int hashCode() {
        int result = 1;
        result = 37 * result + id.hashCode();
        result = 37 * result + loc.hashCode();
        result = 37 * result + neighbours.hashCode();
        return result;
    }

    @Override
    public String toString() {
    return
        "Node id " + id.toString() + "\n" +
        "Name: " + Utils.optP(name) + "\n" +
        "Location: " + loc.toString() + "\n" +
        "Neighbours: " + neighbours.toString() + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Node)) {
            return false;
        }

        Node n = (Node) o;
        return loc.equals(n.getLocation()) && id.equals(n.getId());
    }

    public Option<String> getName() {
        return this.name;
    }
}
