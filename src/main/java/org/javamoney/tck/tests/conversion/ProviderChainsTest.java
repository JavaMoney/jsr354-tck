/*
 * Copyright (c) 2012, 2013, Werner Keil, Credit Suisse (Anatole Tresch). Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. Contributors: Anatole Tresch - initial version.
 */
package org.javamoney.tck.tests.conversion;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import javax.money.convert.*;

import java.util.Collection;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Tests for conversion provider chains.
 * Created by Anatole on 10.03.14.
 */
@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class ProviderChainsTest{

    // ********************** A. Test Basic MonetaryConversions Accessors *********************************

    /**
     * Test correct rate evaluation for different provider chains, providers defined by the TCK.<p>
     * Hint do not use non TCK provider for this test, it will make results undeterministic.
     */
    @Test
    @SpecAssertion(id = "434-A1", section = "4.3.4")
    public void testCorrectRateEvaluationInChain_diffProviders(){
        ExchangeRateProvider prov1 = MonetaryConversions
                .getExchangeRateProvider("TestRateProvider1", "TestRateProvider2",
                                         "TestRateProvider3");
        ExchangeRate rate = prov1.getExchangeRate("CHF", "EUR");
        AssertJUnit.assertEquals("Invalid ExchangeRateProvider selected.", rate.getFactor().intValueExact(), 1);
        prov1 = MonetaryConversions.getExchangeRateProvider("TestRateProvider1", "TestRateProvider2");
        rate = prov1.getExchangeRate("EUR", "USD");
        AssertJUnit.assertEquals("Invalid ExchangeRateProvider selected.", rate.getFactor().intValueExact(), 2);
        prov1 = MonetaryConversions.getExchangeRateProvider("TestRateProvider3");
        rate = prov1.getExchangeRate("USD", "INR");
        AssertJUnit.assertEquals("Invalid ExchangeRateProvider selected.", rate.getFactor().intValueExact(), 3);
        prov1 = MonetaryConversions.getExchangeRateProvider("TestRateProvider1", "TestRateProvider3",
                                                            "TestRateProvider2");
        rate = prov1.getExchangeRate("CHF", "EUR");
        AssertJUnit.assertEquals("Invalid ExchangeRateProvider selected.", rate.getFactor().intValueExact(), 1);
        rate = prov1.getExchangeRate("EUR", "USD");
        AssertJUnit.assertEquals("Invalid ExchangeRateProvider selected.", rate.getFactor().intValueExact(), 2);
        rate = prov1.getExchangeRate("USD", "INR");
        AssertJUnit.assertEquals("Invalid ExchangeRateProvider selected.", rate.getFactor().intValueExact(), 3);

        prov1 = MonetaryConversions.getExchangeRateProvider("TestRateProvider3", "TestRateProvider2",
                                                            "TestRateProvider1");
        rate = prov1.getExchangeRate("CHF", "EUR");
        AssertJUnit.assertEquals("Invalid ExchangeRateProvider selected.", rate.getFactor().intValueExact(), 1);
        prov1 = MonetaryConversions.getExchangeRateProvider("TestRateProvider2", "TestRateProvider1",
                                                            "TestRateProvider3");
        rate = prov1.getExchangeRate("EUR", "USD");
        AssertJUnit.assertEquals("Invalid ExchangeRateProvider selected.", rate.getFactor().intValueExact(), 2);
        rate = prov1.getExchangeRate("USD", "INR");
        AssertJUnit.assertEquals("Invalid ExchangeRateProvider selected.", rate.getFactor().intValueExact(), 3);

        prov1 = MonetaryConversions.getExchangeRateProvider("TestRateProvider3", "TestRateProvider2",
                                                            "TestRateProvider1", "TestRateProvider02");
        rate = prov1.getExchangeRate("CHF", "EUR");
        AssertJUnit.assertEquals("Invalid ExchangeRateProvider selected.", rate.getFactor().intValueExact(), 1);
        rate = prov1.getExchangeRate("EUR", "USD");
        AssertJUnit.assertEquals("Invalid ExchangeRateProvider selected.", rate.getFactor().intValueExact(), 2);
        rate = prov1.getExchangeRate("USD", "INR");
        AssertJUnit.assertEquals("Invalid ExchangeRateProvider selected.", rate.getFactor().intValueExact(), 3);
        rate = prov1.getExchangeRate("INR", "GBP");
        AssertJUnit.assertEquals("Invalid ExchangeRateProvider selected.", rate.getFactor().doubleValue(), 0.2);
    }

    /**
     * Test correct rate evaluation for different provider chains, providers defined by the TCK.<p>
     * Hint do not use non TCK provider for this test, it will make results undeterministic.
     */
    @Test
    @SpecAssertion(id = "434-A1", section = "4.3.4")
    public void testCorrectRateEvaluationInChain_sameProviders(){
        ExchangeRateProvider prov1 = MonetaryConversions
                .getExchangeRateProvider("TestRateProvider1", "TestRateProvider1",
                                         "TestRateProvider1");
        ExchangeRate rate = prov1.getExchangeRate("CHF", "EUR");
        AssertJUnit.assertEquals("Invalid ExchangeRateProvider selected.", rate.getFactor().intValueExact(), 1);
        prov1 = MonetaryConversions.getExchangeRateProvider("TestRateProvider1", "TestRateProvider1");
        rate = prov1.getExchangeRate("CHF", "EUR");
        AssertJUnit.assertEquals("Invalid ExchangeRateProvider selected.", rate.getFactor().intValueExact(), 1);
        prov1 = MonetaryConversions.getExchangeRateProvider("TestRateProvider1");
        rate = prov1.getExchangeRate("CHF", "EUR");
        AssertJUnit.assertEquals("Invalid ExchangeRateProvider selected.", rate.getFactor().intValueExact(), 1);
    }


    /**
     * Test correct rate evaluation for different provider chains, providers defined by the TCK,
     * with historic rates.<p>
     * Hint do not use non TCK provider for this test, it will make results undeterministic.
     */
    @Test
    @SpecAssertion(id = "434-A2", section = "4.3.4")
    public void testCorrectRateEvaluationInChainHistoric(){
        ExchangeRateProvider prov1 = MonetaryConversions
                .getExchangeRateProvider("TestRateProvider1", "TestRateProvider2",
                                         "TestRateProvider3");
        ExchangeRate rate = prov1.getExchangeRate("CHF", "EUR");
        AssertJUnit.assertEquals("Invalid ExchangeRateProvider selected.", rate.getFactor().intValueExact(), 1);
        rate = prov1.getExchangeRate("EUR", "USD");
        AssertJUnit.assertEquals("Invalid ExchangeRateProvider selected.", rate.getFactor().intValueExact(), 2);
        rate = prov1.getExchangeRate(ConversionQueryBuilder.create().setTimestampMillis(10L).setBaseCurrency("EUR").setTermCurrency("USD").build());
        AssertJUnit.assertEquals("Invalid ExchangeRateProvider selected.", 200, rate.getFactor().intValueExact());
    }

    /**
     * Test availability of providers defined by the TCK.<p>
     * Hint do not use non TCK provider for this test, it will make results undeterministic.
     */
    @Test
    @SpecAssertion(id = "434-A3", section = "4.3.4")
    public void testTCKRateChainAvailability(){
        Collection<String> provNames = MonetaryConversions.getProviderNames();
        AssertJUnit.assertTrue("TCK ExchangeRateProvider is not registered: TestRateProvider",
                               provNames.contains("TestRateProvider"));
        AssertJUnit.assertTrue("TCK ExchangeRateProvider is not registered:TestRateProvider1",
                               provNames.contains("TestRateProvider1"));
        AssertJUnit.assertTrue("TCK ExchangeRateProvider is not registered: TestRateProvider2",
                               provNames.contains("TestRateProvider2"));
        AssertJUnit.assertTrue("TCK ExchangeRateProvider is not registered: TestRateProvider3",
                               provNames.contains("TestRateProvider3"));
        AssertJUnit.assertTrue("TCK ExchangeRateProvider is not registered: TestRateProvider02",
                               provNames.contains("TestRateProvider02"));
    }

}
