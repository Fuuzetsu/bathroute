package uk.co.fuuzetsu.bathroute.engine;

import android.location.Location;
import fj.data.Option;
import java.util.List;

public class NodeManager {
    private final List<Node> nodes;

    public NodeManager(final List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Node> getNodes() {
        return this.nodes;
    }

    public Option<Node> getClosestNode(final Location l) {
        Option<Node> closest = Option.none();
        for (Node n : getNodes()) {
            if (closest.isNone()) {
                closest = Option.some(n);
            } else if (closest.some().getLocation().distanceTo(l)
                       > n.getLocation().distanceTo(l)) {
                    closest = Option.some(n);
            }
        }
        return closest;
    }

}
