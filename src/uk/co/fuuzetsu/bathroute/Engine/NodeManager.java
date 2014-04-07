package uk.co.fuuzetsu.bathroute.Engine;

import android.location.Location;
import fj.P;
import fj.P2;
import fj.data.Option;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
import org.jgrapht.WeightedGraph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class NodeManager {
    private final List<Node> nodes;

    public NodeManager(final List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Node> getNodes() {
        return this.nodes;
    }

    private WeightedGraph<Node, DefaultWeightedEdge> makeGraph(final Node start) {
        WeightedGraph<Node, DefaultWeightedEdge> g =
            new SimpleWeightedGraph<Node, DefaultWeightedEdge>(DefaultWeightedEdge.class);

        List<Node> nss = new LinkedList<Node>();
        nss.add(start);
        for (Node n : getNodes()) {
            nss.add(n);
        }

        for (Node n : nss) {
            g.addVertex(n);
        }

        for (Node n : nss) {
            for (Node ne : neighbours(n)) {

                DefaultWeightedEdge e = g.addEdge(n, ne);
                if (!(e == null)) {
                    g.setEdgeWeight(e, n.getLocation().distanceTo(ne.getLocation()));
                }
            }
        }
        return g;
    }

    public Option<Node> getNodeById(final Integer i) {
        for (Node n : getNodes()) {
            if (n.getId().equals(i)) {
                return Option.some(n);
            }
        }
        return Option.none();
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
        Option<Node> co = getClosestNode(l);
        if (co.isNone()) {
            return Option.none();
        }

        Node c = co.some();
        List<Integer> ns = new ArrayList<Integer>(1);
        ns.add(c.getId());
        Node s = new Node(-1, l, ns, Option.some("User location node"));

        WeightedGraph<Node, DefaultWeightedEdge> g = makeGraph(s);


        Option<DijkstraShortestPath<Node, DefaultWeightedEdge>> dip =
            Option.none();

        try {
            dip = Option.some(new DijkstraShortestPath<Node, DefaultWeightedEdge>(g, s, d));
        } catch (IllegalArgumentException e) {
            return Option.none();
        }

        DijkstraShortestPath<Node, DefaultWeightedEdge> di = dip.some();

        GraphPath<Node, DefaultWeightedEdge> gp = di.getPath();
        if (gp == null) {
            return Option.none();
        }
        List<DefaultWeightedEdge> ps = gp.getEdgeList();

        List<Node> path = new LinkedList<Node>();
        path.add(s);

        for (DefaultWeightedEdge e : ps) {
            path.add(gp.getGraph().getEdgeTarget(e));
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
