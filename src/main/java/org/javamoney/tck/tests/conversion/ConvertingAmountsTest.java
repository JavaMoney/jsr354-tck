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

import org.javamoney.tck.tests.internal.TestCurrencyUnit;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.Monetary;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.CurrencyConversionException;
import javax.money.convert.ExchangeRate;
import javax.money.convert.MonetaryConversions;

/**
 * Test for converting amounts.
 * Created by Anatole on 10.03.14.
 */
@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class ConvertingAmountsTest {

    // ******************************* A. Test Basic MonetaryConversions Accessors ******************************

    /**
     * Test successful conversion for possible currency pairs.<p>
     * Hint: you may only check for rate factory, when using a hardcoded ExchangeRateProvider, such a provider
     * must be also implemented and registered as an SPI.
     */
    @Test(description = "4.3.2 Test successful conversion for CHF -> FOO, using TestRateProvider.")
    @SpecAssertion(id = "432-A1", section = "4.3.2")
    public void testConversion() {
        CurrencyUnit cu = new TestCurrencyUnit("FOO");
        CurrencyConversion conv = MonetaryConversions.getConversion(cu, "TestRateProvider");
        MonetaryAmount m = Monetary.getDefaultAmountFactory().setNumber(10).setCurrency("CHF").create();
        MonetaryAmount m2 = m.with(conv);
        AssertJUnit.assertEquals(m2.getCurrency().getCurrencyCode(), "FOO");
        AssertJUnit.assertEquals(20L, m2.getNumber().longValueExact());
        m2 = m.with(conv);
        AssertJUnit.assertEquals(m2.getCurrency().getCurrencyCode(), "FOO");
        AssertJUnit.assertEquals(20L, m2.getNumber().longValueExact());
    }

    /**
     * Compare conversions done with exchange rates provided for same currency pair.
     */
    @Test(description = "4.3.2 Test correct ExchangeRate is returned for CHF -> FOO, using TestRateProvider.")
    @SpecAssertion(id = "432-A2", section = "4.3.2")
    public void testConversionComparedWithRate() {
        final CurrencyUnit FOO = new TestCurrencyUnit("FOO");
        ExchangeRate rate = MonetaryConversions.getExchangeRateProvider("TestRateProvider")
                .getExchangeRate(Monetary.getCurrency("CHF"), FOO);
        AssertJUnit.assertEquals(rate.getBaseCurrency(), Monetary.getCurrency("CHF"));
        AssertJUnit.assertEquals(rate.getCurrency().getCurrencyCode(), FOO.getCurrencyCode());
        AssertJUnit.assertEquals(rate.getFactor().intValueExact(), 2);
        AssertJUnit.assertEquals("TestRateProvider", rate.getContext().getProviderName());
    }

    /**
     * Bad case: try converting from/to an inconvertible (custom) currency, ensure CurrencyConversionException is
     * thrown.
     *
     * @see org.javamoney.tck.tests.internal.TestCurrencyUnit } for creating a custom currency,
     * with some fancy non-ISO currency code.
     */
    @Test(description = "4.3.2 Bad case: Try CurrencyConversion to an inconvertible (custom) " +
            "currency (FOOANY), ensure CurrencyConversionException is thrown.")
    @SpecAssertion(id = "432-A3", section = "4.3.2")
    public void testUnsupportedConversion() {
        MonetaryAmount m = Monetary.getDefaultAmountFactory().setNumber(10).setCurrency("CHF").create();
        CurrencyUnit cu = new TestCurrencyUnit("FOOANY");
        try {
            CurrencyConversion conv = MonetaryConversions.getConversion(cu);
            m.with(conv);
        } catch (CurrencyConversionException e) {
            // expected
        }
    }

    /**
     * Bad case: try converting from/to a null currency, ensure NullPointerException is thrown.
     */
    @Test(expectedExceptions = NullPointerException.class,
            description = "4.3.2 Bad case: Access CurrencyConversion " +
                    "with a CurrencyUnit==null, ensure NullPointerException is thrown.")
    @SpecAssertion(id = "432-A4", section = "4.3.2")
    public void testNullConversion1() {
        MonetaryConversions.getConversion((CurrencyUnit) null);
    }

    /**
     * Bad case: try converting from/to a null currency, ensure NullPointerException is thrown.
     */
    @Test(expectedExceptions = NullPointerException.class,
            description = "4.3.2 Bad case: Access CurrencyConversion with a currency code==null, ensure " +
                    "NullPointerException is thrown.")
    @SpecAssertion(id = "432-A4", section = "4.3.2")
    public void testNullConversion2() {
        MonetaryConversions.getConversion((String) null);
    }

}
