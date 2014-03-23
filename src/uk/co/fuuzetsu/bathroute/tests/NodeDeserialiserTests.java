package uk.co.fuuzetsu.bathroute.tests;

import        fj.data.Option;
import        java.lang.reflect.Field;
import        java.lang.reflect.InvocationTargetException;
import        java.lang.reflect.Method;
import        java.util.ArrayList;
import        java.util.List;
import        junit.framework.TestCase;
import        org.apache.commons.lang3.exception.ExceptionUtils;
import        org.junit.*;
import static org.junit.Assert.*;
import        uk.co.fuuzetsu.bathroute.engine.Node;
import        uk.co.fuuzetsu.bathroute.engine.NodeDeserialiser;

public class NodeDeserialiserTests extends TestCase {

    private <A> Boolean optEq(Option<A> a, Option<A> b) {
        if (a == null || b == null) {
            return false;
        } else if (a.isSome() && b.isSome()) {
            A ao = a.some();
            A bo = b.some();
            if (ao == null || bo == null) {
                return false;
            } else {
                return ao.equals(bo);
            }
        } else {
            return a.isSome() == b.isSome();
        }
    }

    private <A> void optParserTest(String mname, String arg, Option<A> expected) {
        try {
            NodeDeserialiser d = new NodeDeserialiser();

            Method method =
                NodeDeserialiser.class.getDeclaredMethod(mname,
                                                         String.class);
            method.setAccessible(true);

            @SuppressWarnings("unchecked")
            Option<A> result = (Option<A>) method.invoke(d, arg);
            assertTrue(optEq(result, expected));
        } catch (IllegalArgumentException
                 |IllegalAccessException
                 |InvocationTargetException
                 |NoSuchMethodException e) {
            System.out.println("Reflection exception testing " + mname + "\n"
                               + ExceptionUtils.getStackTrace(e));
        }
    }

    public void testIdParser() {
        Option<Integer> n = Option.none();
        Option<Integer> r = Option.some(12);
        optParserTest("parseId", "12", r);
        optParserTest("parseId", "hello", n);
    }

    public void testIntList() {
        List<Integer> r = new ArrayList();
        r.add(1); r.add(2); r.add(3); r.add(4);
        String input = "1 2 3 4";

        try {
            NodeDeserialiser d = new NodeDeserialiser();

            Method method =
                NodeDeserialiser.class.getDeclaredMethod("parseIntList",
                                                         String.class);
            method.setAccessible(true);

            @SuppressWarnings("unchecked")
            List<Integer> result = (List<Integer>) method.invoke(d, input);
            assertEquals(r, result);
        } catch (IllegalArgumentException
                 |IllegalAccessException
                 |InvocationTargetException
                 |NoSuchMethodException e) {
            System.out.println("Reflection exception testing parseIntList\n"
                               + ExceptionUtils.getStackTrace(e));
        }

    }

}
