package org.javamoney;

import org.javamoney.tck.TCKRunner;
import org.testng.annotations.Test;

/**
 * Created by atsticks on 23.08.14.
 */
public class TCKRunnerTest {

    @Test
    public static void testTCKRunner() {
        TCKRunner.main(new String[]{TCKRunner.class.getName()});
    }
}
