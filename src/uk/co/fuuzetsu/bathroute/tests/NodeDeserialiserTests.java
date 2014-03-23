package uk.co.fuuzetsu.bathroute.tests;

import        fj.data.Option;
import        java.lang.reflect.Field;
import        java.lang.reflect.InvocationTargetException;
import        java.lang.reflect.Method;
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

    public void testDeserialiser() {
        try {
            NodeDeserialiser d = new NodeDeserialiser();

            Method method =
                NodeDeserialiser.class.getDeclaredMethod("parseId",
                                                         String.class);
            method.setAccessible(true);

            @SuppressWarnings("unchecked")
            Option<Integer> result = (Option<Integer>) method.invoke(d, "12");
            assertTrue(optEq(result, Option.some(12)));
        } catch (IllegalArgumentException
                 |IllegalAccessException
                 |InvocationTargetException
                 |NoSuchMethodException e) {
            System.out.println("Reflection exception in testDeserialiser\n"
                               + ExceptionUtils.getStackTrace(e));
        }
    }
}
