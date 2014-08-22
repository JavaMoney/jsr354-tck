/*
 * Copyright (c) 2012, 2013, Werner Keil, Credit Suisse (Anatole Tresch). Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. Contributors: Anatole Tresch - initial version.
 */
package org.javamoney.tck.tests.format;

import org.javamoney.tck.tests.internal.TestAmount;
import org.javamoney.tck.tests.internal.TestMonetaryAmountFactory;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import javax.money.*;
import javax.money.format.AmountFormatQuery;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class FormattingMonetaryAmountsTest{

    /**
     * Format several amounts, created using the default factory,
     * but
     * also a test instance, provided by the TCK, to ensure no
     * implementation
     * dependencies on the implementation.
     */
    @SpecAssertion(section = "4.4.1", id = "441-A1")
    @Test(description = "Ensures the system.s default locale is supported for MonetaryAmountFormat.")
    public void testNoDepOnAmountImplementation(){
        final Locale defaultLocale = Locale.getDefault();
        MonetaryAmountFormat amountFormat = MonetaryFormats.getAmountFormat(defaultLocale);
        final Number[] values = new Number[]{100, 10000000000000L};// TODO
        // other
        // values
        // and
        // currencies
        for(CurrencyUnit currency : MonetaryCurrencies.getCurrencies()){
            for(Number value : values){
                MonetaryAmount amount =
                        MonetaryAmounts.getDefaultAmountFactory().setCurrency(currency.getCurrencyCode()).setNumber(value)
                                .create();
                String formattedAmount = amountFormat.format(amount);
                MonetaryAmount amountMock = TestMonetaryAmountFactory.getAmount(value, currency);
                String formattedAmountMock = amountFormat.format(amountMock);
                AssertJUnit.assertNotNull(formattedAmountMock);
                AssertJUnit.assertEquals(formattedAmountMock, formattedAmount);
            }
        }
    }

    /**
     * Print several amounts, created using the default factory, but
     also a test instance, provided by the TCK, to ensure no
     implementation
     dependencies on the implementation.
     */
    @SpecAssertion(section = "4.4.1", id = "441-A2")
    @Test(description = "Formats amounts using all available locales.")
    public void testFormattingIsIndependentOfImplementation(){
        for (Locale locale : MonetaryFormats.getAvailableLocales()) {
            MonetaryAmountFormat format = MonetaryFormats.getAmountFormat(locale);
            Set<String> formatsProcuced = new HashSet<>();
            for (MonetaryAmountFactory fact : MonetaryAmounts.getAmountFactories()) {
                if (fact.getAmountType().equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount amt = fact.setCurrency("USD").setNumber(10.5).create();
                String formatProduced = format.format(amt);
                assertNotNull(formatProduced, "No MonetaryAmountFormat returned from MonetaryFormats." +
                        "getMonetaryFormat(Locale,String...) with supported Locale: " + locale);
                assertFalse(formatProduced.isEmpty(), "MonetaryAmountFormat returned empty String for " + amt);
                formatsProcuced.add(formatProduced);
            }
            assertFalse(formatsProcuced.isEmpty(), "No formatted amount available. Are there no amount?");
            assertFalse(formatsProcuced.size() > 1, "Formatter produces different output for different amount classes(+" +
                    format.getClass() + "): " + formatsProcuced);
        }
    }

    /**
     * Parse back several amounts, input created using the
     formatting
     from 'Format_formatAmounts'.
     */
    @SpecAssertion(section = "4.4.1", id = "441-A3")
    @Test(description = "Test formats and parses (round-trip) any supported amount type for each supported Locale.")
    public void testParseIsIndependentOfImplementation(){
        for (Locale locale : MonetaryFormats.getAvailableLocales()) {
            MonetaryAmountFormat format = MonetaryFormats.getAmountFormat(locale);
            for (MonetaryAmountFactory fact : MonetaryAmounts.getAmountFactories()) {
                if (fact.getAmountType().equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount amt = fact.setCurrency("USD").setNumber(10.5).create();
                String formatProduced = format.format(amt);
                assertNotNull(formatProduced, "No MonetaryAmountFormat returned from MonetaryFormats." +
                        "getMonetaryFormat(Locale,String...) with supported Locale: " + locale);
                assertFalse(formatProduced.isEmpty(), "MonetaryAmountFormat returned empty String for " + amt);
                try {
                    MonetaryAmount amtParsed = format.parse(formatProduced);
                    assertNotNull(amtParsed, "Reverse-parsing of MonetaryAmount failed for '" + formatProduced +
                            "' using MonetaryAmountFormat: " + format);
                } catch (MonetaryException e) {
                    System.out.println("WARNING: Reverse-parsing of MonetaryAmount failed for '" + formatProduced +
                            "' using MonetaryAmountFormat: " + format);
                }
            }
        }
    }

    /**
     * Get/set different amount styles (especially patterns, group
     sizes, group characters) and compare results with results as from
     RI.
     Also apply patterns without currency invovled.
     */
    @SpecAssertion(section = "4.4.1", id = "441-A4")
    @Test
    public void testParseDifferentStyles(){
        AssertJUnit.fail("Not implemented.");
    }

    /**
     * Get/set different monetary contexts and compare results with
     results from parsed amounts.
     */
    @SpecAssertion(section = "4.4.1", id = "441-A5")
    @Test
    public void testParseDifferentAmountContexts(){
        AssertJUnit.fail("Not implemented.");
    }

    /**
     * Get/set default currency, try to parse patterns without
     currency information.
     */
    @SpecAssertion(section = "4.4.1", id = "441-A6")
    @Test
    public void testParseWithDifferentCurrencies(){
        AssertJUnit.fail("Not implemented.");
    }


    /**
     * AccessingMonetaryAmountFormat using
     * MonetaryFormats.getAmountFormat(Locale locale), all locales
     * available also from java.text.DecimalFormat must be supported.
     */
    @SpecAssertion(section = "4.4.1", id = "441-B1")
    @Test(description = "Ensures all Locales defined by DecimalFormat.getAvailableLocales() are available for " +
            "monetary formatting.")
    public void testLocalesSupported(){
        Locale[] jdkDecimalFormatLocales = DecimalFormat.getAvailableLocales();
        for(Locale jdkDecimalFormatLocale : jdkDecimalFormatLocales){
            MonetaryAmountFormat amountFormat = MonetaryFormats.getAmountFormat(jdkDecimalFormatLocale);
            AssertJUnit.assertNotNull(amountFormat);
            AssertJUnit.assertEquals(jdkDecimalFormatLocale, amountFormat.getAmountFormatContext().getLocale());
        }
    }

    /**
     * AccessingMonetaryAmountFormat using
     * MonetaryFormats.getAmountFormat(AmountFormatContext style), all locales
     * available also from java.text.DecimalFormat must be supported
     * (using AmountFormatContext.of(Locale)).
     */
    @Test(description = "Ensures for each locale defined by DecimalFormat.getAvailableLocales() a " +
            "MonetaryAmountFormat instance is provided.")
    @SpecAssertion(section = "4.4.1", id = "441-B2")
    public void testGetAmountFormat(){
        for(Locale locale : DecimalFormat.getAvailableLocales()){
            AssertJUnit.assertNotNull(MonetaryFormats.getAmountFormat(AmountFormatQuery.of(locale)));
        }
    }

    /**
     * Test MonetaryFormats.getAvailableLocales, all locales available also from java.text.DecimalFormat must be
     * supported (using AmountFormatContext.of(Locale)), more locales are possible.
     */
    @Test(description = "Ensures for each locale defined by DecimalFormat.getAvailableLocales() a " +
            "MonetaryFormats.isAvailable(Locale) is true.")
    @SpecAssertion(section = "4.4.1", id = "441-B3")
    public void testGetAvailableLocales(){
        Set<Locale> locales = MonetaryFormats.getAvailableLocales();
        for(Locale locale : DecimalFormat.getAvailableLocales()){
            if(Locale.ROOT.equals(locale)){
                continue;
            }
            AssertJUnit.assertTrue(
                    "MonetaryFormats.getAvailableLocales(); Locale supported by JDKs DecimalFormat is not available: " +
                            locale, locales.contains(locale)
            );
        }
    }

    /**
     * Test MonetaryFormats.getAvailableLocales, all locales available also from java.text.DecimalFormat must be
     * supported (using AmountFormatContext.of(Locale)), more locales are possible.
     */
    @Test(description = "Ensures for each locale defined by DecimalFormat.getAvailableLocales() a " +
            "MonetaryFormats.getAmountFormat(AmountFormatQuery) returns a formatter.")
    @SpecAssertion(section = "4.4.1", id = "441-B3")
    public void testAmountStyleOf(){
        for(Locale locale : DecimalFormat.getAvailableLocales()){
            AssertJUnit.assertNotNull(MonetaryFormats.getAmountFormat(AmountFormatQuery.of(locale)));
        }
    }
}
