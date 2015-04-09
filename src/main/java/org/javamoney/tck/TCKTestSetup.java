/*
 * Copyright (c) 2012, 2013, Werner Keil, Credit Suisse (Anatole Tresch). Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. Contributors: Anatole Tresch - initial version.
 */
package org.javamoney.tck;

import java.util.ServiceLoader;

/**
 * TCK bootstrap class loading the {@link org.javamoney.tck.JSR354TestConfiguration}.
 */
public final class TCKTestSetup {

    private static final JSR354TestConfiguration TEST_CONFIG = loadTestConfiguration();

    private TCKTestSetup() {
    }

    /**
     * Loads the test configuration setup from the ServiceLoader.
     * @return
     */
    private static JSR354TestConfiguration loadTestConfiguration() {
        try {
            return ServiceLoader.load(JSR354TestConfiguration.class).iterator()
                    .next();
        } catch (Exception e) {
            throw new IllegalStateException("No valid implementation of "
                    + JSR354TestConfiguration.class.getName()
                    + " is registered with the ServiceLoader.");
        }
    }

    /**
     * Get the current test configuration setup.
     * @return the test configuration, not null.
     */
    public static JSR354TestConfiguration getTestConfiguration() {
        return TEST_CONFIG;
    }

}
