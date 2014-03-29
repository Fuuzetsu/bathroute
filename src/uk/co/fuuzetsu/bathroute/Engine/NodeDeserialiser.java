package uk.co.fuuzetsu.bathroute.Engine;

import android.location.Location;
import fj.F2;
import fj.F;
import fj.Unit;
import fj.data.Option;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import uk.co.fuuzetsu.bathroute.Engine.Node;

public class NodeDeserialiser {

    /* This method attempts to parse list of Node out of an XML file.
     * The way it works is that we take note of what opening tags
     * we're seeing and assigning an appropriate parser to the text
     * that follows. We use a private class to emulate a Node which
     * has missing information. Each parser mutates this incomplete
     * node, filling in its own part of the info. By the time we see
     * the closing "node" tag, we check whether we have all the
     * information we needed. If a tag was missing or its content
     * couldn't be parsed, we end up simply ignoring that node and
     * progressing as if it didn't exist. Note that this parser will
     * ignore any tags it isn't explicitly looking for. */
    public List<Node> deserialise(String s) throws XmlPullParserException,
                                                   IOException {

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser p = factory.newPullParser();
        p.setInput(new StringReader (s));
        int eventType = p.getEventType();

        List<Node> nodes = new ArrayList<Node>();
        Option<IncompleteNode> currentNode = Option.none();
        Option<F2<IncompleteNode, String, Unit>> pf = Option.none();
        String observedTag = "";

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
            case XmlPullParser.START_TAG:
                observedTag = p.getName();

                if (observedTag.equals("node")) {
                    currentNode = Option.some(new IncompleteNode());
                } else if (currentNode.isSome()) {
                    F2<IncompleteNode, String, Unit> pg =
                        new F2<IncompleteNode, String, Unit>() {
                        /* Do nothing but don't risk NPE either. */
                        @Override
                        public Unit f(IncompleteNode n, String s) {
                            return Unit.unit();
                        }
                    };
                    if ("id".equals(observedTag)) {
                        pg = new F2<IncompleteNode, String, Unit>() {

                            @Override
                            public Unit f(IncompleteNode n, String s) {
                                n.id = parseId(s);
                                return Unit.unit();
                            }
                        };
                        pf = Option.some(pg);
                    } else if ("latitude".equals(observedTag)) {
                        pg = new F2<IncompleteNode, String, Unit>() {

                            @Override
                            public Unit f(IncompleteNode n, String s) {
                                n.lat = parseLocDouble(s);
                                return Unit.unit();
                            }
                        };
                        pf = Option.some(pg);
                    } else if ("longitude".equals(observedTag)) {
                        pg = new F2<IncompleteNode, String, Unit>() {

                            @Override
                            public Unit f(IncompleteNode n, String s) {
                                n.lon = parseLocDouble(s);
                                return Unit.unit();
                            }
                        };
                        pf = Option.some(pg);
                    } else if ("neighbours".equals(observedTag)) {
                        pg = new F2<IncompleteNode, String, Unit>() {

                            @Override
                            public Unit f(IncompleteNode n, String s) {
                                n.neighbours = Option.some(parseIntList(s));
                                return Unit.unit();
                            }
                        };
                        pf = Option.some(pg);
                    } else if ("name".equals(observedTag)) {
                        pg = new F2<IncompleteNode, String, Unit>() {

                            @Override
                            public Unit f(IncompleteNode n, String s) {
                                n.name = Option.some(s);
                                return Unit.unit();
                            }
                        };
                        pf = Option.some(pg);
                    } else {
                        pf = Option.none();
                    }
                }

                eventType = p.next();
                break;
            case XmlPullParser.END_TAG:
                if (p.getName().equals("node") && currentNode.isSome()) {
                    Option<Node> np = currentNode.some().toNode();

                    if (np.isSome()) {
                        nodes.add(np.some());
                    }

                    /* We're finished with this node no matter what */
                    currentNode = Option.none();
                }

                /* Clear out the tag parser on each end tag */
                pf = Option.none();
                eventType = p.next();
                break;
            case XmlPullParser.TEXT:
                if (pf.isSome() && currentNode.isSome()) {
                    pf.some().f(currentNode.some(), p.getText());
                }

                eventType = p.next();
                break;
            default:
                eventType = p.next();
            }
        }

        return nodes;
    }

    private <A, E extends Throwable> Option<A>
        parseWith(F<String, A> g, String s) {
        try {
            return Option.some(g.f(s));
        } catch (Throwable e) {
            return Option.none();
        }
    }

    private Option<Integer> parseId(String s) {
        try {
            return Option.some(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            return Option.none();
        }
    }

    private Option<Double> parseLocDouble(String s) {
        try {
            return Option.some(Location.convert(s));
        } catch (IllegalArgumentException e) {
            return Option.none();
        } catch (NullPointerException e) {
            /* Remind me again why libs throw this shit? */
            return Option.none();
        }
    }

    private List<Integer> parseIntList(String s) {
        List<String> sp = Arrays.asList(s.split("\\s+"));
        sp.removeAll(Arrays.asList(""));
        List<Integer> il = new ArrayList<Integer>();
        for (String ss : sp) {
            Option<Integer> r = parseId(ss);
            if (r.isSome()) {
                il.add(r.some());
            }
        }

        return il;
    }


    private class IncompleteNode {
        public Option<String> name = Option.none();
        public Option<Double> lat = Option.none();
        public Option<Double> lon = Option.none();
        public Option<List<Integer>> neighbours = Option.none();
        public Option<Integer> id = Option.none();

        @Override
        public String toString() {
            return
                "Incomplete Node id " + Utils.optP(id) + "\n" +
                "Name: " + Utils.optP(name) + "\n" +
                "Latitude: " + Utils.optP(lat) + "\n" +
                "Longitude: " + Utils.optP(lon) + "\n" +
                "Neighbours: " + Utils.optP(neighbours) + "\n";
        }

        public Option<Node> toNode() {
            if (neighbours.isSome() && id.isSome()
                && lat.isSome() && lon.isSome()) {
                return Option.some
                    (new Node(id.some(),
                              Utils.makeLocation(lon.some(), lat.some()),
                              neighbours.some(), name));
            } else {
                return Option.none();
            }

        }
    }

}
