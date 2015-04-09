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

import javax.money.convert.ExchangeRateProvider;
import javax.money.spi.CurrencyProviderSpi;
import javax.money.spi.MonetaryAmountFactoryProviderSpi;
import javax.money.spi.MonetaryAmountsSingletonSpi;
import javax.money.spi.MonetaryConversionsSingletonSpi;
import javax.money.spi.RoundingProviderSpi;
import java.util.ServiceLoader;

/**
 * Tests for the core SPI implementation.
 * Created by Anatole on 10.03.14.
 */
@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class CoreSPITests {


    // ***************************************** A. Registering Currencies ***********************************

    /**
     * Test registered CurrencyProviderSpi (at least one instance
     * required). Test behaviour,
     * especially bad case behaviour for invalid
     * input.
     */
    @Test(description = "4.5.1 Test if a CurrencyProviderSpi is registered.")
    @SpecAssertion(id = "451-A1", section = "4.5.1")
    public void testCurrencyProviderSpi() {
        ServiceLoader l = null;
        try {
            l = ServiceLoader.load(CurrencyProviderSpi.class);
        } catch (Exception e) {
            Assert.fail("Failure during check for loaded CurrencyProviderSpi.", e);
        }
        Assert.assertTrue(l.iterator().hasNext(), "No instance of CurrencyProviderSpi provided by implementation.");
    }

    /**
     * Test registered MonetarySingletonSpi (at least one instance
     * required). Test behaviour,
     * especially bad case behaviour for invalid
     * input.
     */
    @Test(description = "4.5.1 Test if a MonetaryAmountsSingletonSpi is registered.")
    @SpecAssertion(id = "451-A2", section = "4.5.1")
    public void testMonetaryAmountsSingletonSpi2() {
        // Duplicate test.
    }


    // ***************************************** A. Registering Monetary Amount Factories **************************

    /**
     * Test registered MonetarySpi (at least one instance
     * required). Test behaviour,
     * especially bad case behaviour for invalid
     * input.
     */
    @Test(description = "4.5.1 Test if a MonetaryAmountFactoryProviderSpi is registered.")
    @SpecAssertion(id = "451-B1", section = "4.5.1")
    public void testMonetaryAmountFactoryProviderSpis() {
        ServiceLoader l = null;
        try {
            l = ServiceLoader.load(MonetaryAmountFactoryProviderSpi.class);
        } catch (Exception e) {
            Assert.fail("Failure during check for loaded MonetaryAmountFactoryProviderSpi.", e);
        }
        Assert.assertTrue(l.iterator().hasNext(),
                "No instance of MonetaryAmountFactoryProviderSpi provided by implementation.");
    }

    // ************************************ C. Backing the Monetary Singleton ******************************

    /**
     * Test registered MonetarySingletonSpi (at least one instance
     * required). Test behaviour,
     * especially bad case behaviour for invalid
     * input.
     */
    @Test(description = "4.5.1 Test if a MonetaryAmountsSingletonSpi is registered.")
    @SpecAssertion(id = "451-C1", section = "4.5.1")
    public void testMonetaryAmountsSingletonSpi() {
        ServiceLoader l = null;
        try {
            l = ServiceLoader.load(MonetaryAmountsSingletonSpi.class);
        } catch (Exception e) {
            Assert.fail("Failure during check for loaded MonetaryAmountsSingletonSpi.", e);
        }
        Assert.assertTrue(l.iterator().hasNext(),
                "No instance of MonetaryAmountsSingletonSpi provided by implementation.");
    }

    // ************************************ D. Registering Roundings ******************************

    /**
     * Test registered RoundingProviderSpi (at least one instance
     * required). Test behaviour,
     * especially bad case behaviour for invalid
     * input.
     */
    @Test(description = "4.5.1 Test if a RoundingProviderSpi is registered.")
    @SpecAssertion(id = "451-D1", section = "4.5.1")
    public void testRoundingProviderSpi() {
        ServiceLoader l = null;
        try {
            l = ServiceLoader.load(RoundingProviderSpi.class);
        } catch (Exception e) {
            Assert.fail("Failure during check for loaded RoundingProviderSpi.", e);
        }
        Assert.assertTrue(l.iterator().hasNext(), "No instance of RoundingProviderSpi provided by implementation.");
    }

    // ************************************ E. Adapting Currency Conversion ******************************

    /**
     * Test registered ConversionProviderSpi (at least one instance
     * required). Test behaviour,
     * especially bad case behaviour for invalid
     * input.
     */
    @Test(description = "4.5.2 Test if any ExchangeRateProvider instances are registered.")
    @SpecAssertion(id = "452-A1", section = "4.5.2")
    public void testExchangeRateProviderSpi() {
        ServiceLoader l = null;
        try {
            l = ServiceLoader.load(ExchangeRateProvider.class);
        } catch (Exception e) {
            Assert.fail("Failure during check for loaded ExchangeRateProvider.", e);
        }
        Assert.assertTrue(l.iterator().hasNext(), "No instance of ExchangeRateProvider provided by implementation.");
    }

    /**
     * Test registered MonetaryConversionsSingletonSpi (at least one instance
     * required). Test behaviour,
     * especially bad case behaviour for invalid
     * input.
     */
    @Test(description = "4.5.2 Test if a MonetaryConversionsSingletonSpi instance is registered.")
    @SpecAssertion(id = "452-A2", section = "4.5.2")
    public void testMonetaryConversionsSingletonSpi() {
        ServiceLoader l = null;
        try {
            l = ServiceLoader.load(MonetaryConversionsSingletonSpi.class);
        } catch (Exception e) {
            Assert.fail("Failure during check for loaded MonetaryConversionsSingletonSpi.", e);
        }
        Assert.assertTrue(l.iterator().hasNext(),
                "No instance of MonetaryConversionsSingletonSpi provided by implementation.");
    }

}
