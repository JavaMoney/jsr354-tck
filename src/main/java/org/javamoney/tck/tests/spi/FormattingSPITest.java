/*
 * Copyright (c) 2012, 2013, Werner Keil, Credit Suisse (Anatole Tresch). Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. Contributors: Anatole Tresch - initial version.
 */
package org.javamoney.tck.tests.spi;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.money.spi.MonetaryAmountFormatProviderSpi;
import java.util.ServiceLoader;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Created by Anatole on 10.03.14.
 */
@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class FormattingSPITest {

    // ********************************* C. Prodivding Amount Formats

    /**
     * Test registered MonetaryAmountFormatProviderSpi (one is
     * required),
     * especially bad case behaviour for
     * invalid
     * input.
     */
    @Test(description = "4.5.3 Test if a MonetaryAmountFormatProviderSpi instance is registered.")
    @SpecAssertion(id = "453-A1", section = "4.5.3")
    public void testMonetaryAmountFormatProviderSpiIsRegistered() {
        ServiceLoader l = null;
        try {
            l = ServiceLoader.load(MonetaryAmountFormatProviderSpi.class);
        } catch (Exception e) {
            Assert.fail("Failure during check for loaded MonetaryAmountFormatProviderSpi.", e);
        }
        Assert.assertTrue(l.iterator().hasNext(),
                "No instance of MonetaryAmountFormatProviderSpi provided by implementation.");
    }

}
