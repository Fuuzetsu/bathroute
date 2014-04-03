package uk.co.fuuzetsu.bathroute.Engine;

import android.location.Location;
import fj.P;
import fj.P2;
import fj.data.Option;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.List;
import java.util.Set;

public class NodeManager {
    private final List<Node> nodes;

    public NodeManager(final List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Node> getNodes() {
        return this.nodes;
    }

    /* Useful when we want a map parametrised by Integer such as for
       ListViews. This ignores nodes without a name. */
    public Map<Integer, Node> toSortedMap() {
        List<Node> namedNodes = new ArrayList<Node>(getNodes().size());
        for (Node n : getNodes()) {
            if (n.getName().isSome()) {
                namedNodes.add(n);
            }
        }



        Collections.sort(namedNodes, new Comparator<Node>() {
                @Override
                public int compare(Node n, Node m) {
                    return n.getName().some().compareTo(m.getName().some());
                }
            });

        Map<Integer, Node> m = new HashMap<Integer, Node>(namedNodes.size());
        for (Integer i = 0; i < namedNodes.size(); i++) {
            m.put(i, namedNodes.get(i));
        }

        return m;
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

    public Option<List<Node>> findPath(final Location l, final Node d) {
        final Option<Node> closest = getClosestNode(l);
        if (closest.isNone()) {
            return Option.none();
        }

        final Node c = closest.some();
        final Map<Node, Float> dist = new HashMap<Node, Float>();
        final Set<Node> q = new HashSet<Node>();
        final Map<Node, Node> previous = new HashMap<Node, Node>();

        for (Node n : getNodes()) {
            dist.put(n, Float.MAX_VALUE);
            q.add(n);
        }

        dist.put(c, 0f);

        while (!q.isEmpty()) {
            Option<Node> up = Option.none();
            Option<Float> f = Option.none();
            for (Node n : q) {
                if (f.isNone() || f.some() > dist.get(n)) {
                    up = Option.some(n);
                    f = Option.some(dist.get(n));
                }
            }

            if (up.isNone()) {
                return Option.none(); /* How did we manage this? */
            }

            Node u = up.some();

            q.remove(u);

            if (dist.get(u) == Float.MAX_VALUE) {
                break;
            }

            for (Node v : neighbours(u)) {
                if (!q.contains(v)) {
                    continue;
                }

                Float alt = dist.get(u)
                    + u.getLocation().distanceTo(v.getLocation());

                if (alt < dist.get(v)) {
                    dist.put(v, alt);
                    previous.put(v, u);
                }
            }
        }

        List<Node> path = new ArrayList<Node>(dist.size());
        Node u = d;
        while (previous.containsKey(u)) {
            path.add(u);
            u = previous.get(u);
        }
        return Option.some(path);
    }

    private List<Node> neighbours(final Node n) {
        List<Integer> ns = n.getNeighbours();
        List<Node> rn = new ArrayList<Node>(ns.size());
        for (Integer i : ns) {
            for (Node ni : getNodes()) {
                if (ni.getId() == i && !ni.equals(n)) {
                    rn.add(ni);
                    break;
                }
            }
        }
        return rn;
    }

}
