package org.javamoney.tck;

import org.javamoney.tck.TCKRunnerJava7;
import org.testng.annotations.Test;

/**
 * Created by atsticks on 23.08.14.
 */
public class TCKRunnerTest {

    @Test
    public static void testTCKRunner() {
        TCKRunnerJava7.main(new String[]{TCKRunnerJava7.class.getName()});
    }
}
