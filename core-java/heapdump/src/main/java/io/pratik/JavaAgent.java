/**
 * 
 */
package io.pratik;

import java.lang.instrument.Instrumentation;

/**
 * @author Pratik Das
 *
 */
public class JavaAgent {
    private static volatile Instrumentation instrumentation;
    public static void premain(final String agentArgs, final Instrumentation instrumentation) {
        JavaAgent.instrumentation = instrumentation;
    }
    public static long getObjectSize(final Object object) {
        if (instrumentation == null) {
            return -1L;
        }
        return instrumentation.getObjectSize(object);
    }
}
