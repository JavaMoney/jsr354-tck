/*
 * Copyright (c) 2012, 2015, Werner Keil, Credit Suisse (Anatole Tresch). Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. Contributors: Anatole Tresch - initial version.
 */
package org.javamoney.tck;

import static org.testng.Assert.assertEquals;

import javax.tools.Tool;

import org.testng.annotations.Test;

/**
 * Test class for the executing the TCKRunner.
 */
public class TCKRunnerTest {

    @Test
    public void testTCKRunner() {
        final Tool runner = new TCKRunner();
        int returnCode = runner.run(System.in, System.out, System.err, new String[]{TCKRunner.class.getName()});
        assertEquals(0, returnCode);
    }
}
