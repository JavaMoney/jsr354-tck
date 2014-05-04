/*
 * Copyright (c) 2012, 2013, Werner Keil, Credit Suisse (Anatole Tresch). Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. Contributors: Anatole Tresch - initial version.
 */
package org.javamoney.tck.tests;

import org.javamoney.tck.TCKTestSetup;
import org.javamoney.tck.tests.internal.TestAmount;
import org.javamoney.tck.tests.internal.TestCurrencyUnit;
import org.javamoney.tck.tests.internal.TestMonetaryAmountFactory;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.Test;

import javax.money.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Currency;
import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Anatole on 10.03.14.
 */
@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class AccessingCurrenciesAmountsRoundingsTest{

    // ****************** A. Accessing Currencies *******************

    /**
     * Test if MonetaryCurrencies provides all ISO related entries,
     * similar to the JDK.
     */
    @Test
    @SpecAssertion(section = "4.2.7", id = "427-A1")
    public void testAllISOCurrenciesAvailable(){
        for(Currency currency : Currency.getAvailableCurrencies()){
            assertTrue(
                    "Currency not available [MonetaryCurrencies#isCurrencyAvailable(String)] for JDK currency code: " +
                            currency.getCurrencyCode(),
                    MonetaryCurrencies.isCurrencyAvailable(currency.getCurrencyCode())
            );
            assertNotNull("Currency null [MonetaryCurrencies#igetCurrency(String)] for JDK currency code: " +
                                  currency.getCurrencyCode(), MonetaryCurrencies.getCurrency(currency.getCurrencyCode())
            );
        }
    }

    /**
     * Test if MonetaryCurrencies provides all Locale related
     * entries, similar to the JDK (for all ISO countries).
     */
    @Test
    @SpecAssertion(section = "4.2.7", id = "427-A2")
    public void testAllLocaleCurrenciesAvailable(){
        for(String country : Locale.getISOCountries()){
            Locale locale = new Locale("", country);
            if(Currency.getInstance(locale) != null){
                assertTrue(
                        "Currency not available [MonetaryCurrencies#isCurrencyAvailable(Locale)] for locale: " + locale,
                        MonetaryCurrencies.isCurrencyAvailable(locale));
                assertNotNull("Currency null [MonetaryCurrencies#igetCurrency(Locale)] for locale: " + locale,
                              MonetaryCurrencies.getCurrency(locale));
                CurrencyUnit unit = MonetaryCurrencies.getCurrency(locale);
                assertEquals("Invalid Currency returned from [MonetaryCurrencies#igetCurrency(Locale)] for locale: " +
                                     locale +
                                     ", expected: " + Currency.getInstance(locale) + ", found: " + unit,
                             MonetaryCurrencies.getCurrency(Currency.getInstance(locale).getCurrencyCode()), unit
                );
            }
        }
    }

    /**
     * Test if MonetaryCurrencies provides correct instance with ISO
     * codes.
     */
    @Test
    @SpecAssertion(section = "4.2.7", id = "427-A3")
    public void testCorrectISOCodes(){
        for(Currency currency : Currency.getAvailableCurrencies()){
            CurrencyUnit unit = MonetaryCurrencies.getCurrency(currency.getCurrencyCode());
            assertEquals(
                    "Invalid Currency code returned from [MonetaryCurrencies#igetCurrency(String)] for currency code:" +
                            " " +
                            currency.getCurrencyCode() + ", expected: " +
                            Currency.getInstance(currency.getCurrencyCode()).getCurrencyCode() +
                            ", found: " + unit.getCurrencyCode(),
                    Currency.getInstance(currency.getCurrencyCode()).getCurrencyCode(), unit.getCurrencyCode()
            );
            assertEquals(
                    "Invalid numeric code returned from [MonetaryCurrencies#igetCurrency(String)] for currency code: " +
                            currency.getCurrencyCode() + ", expected: " +
                            Currency.getInstance(currency.getCurrencyCode()).getNumericCode() +
                            ", found: " + unit.getNumericCode(),
                    Currency.getInstance(currency.getCurrencyCode()).getNumericCode(), unit.getNumericCode()
            );
            assertEquals("Invalid default fraction unit returned from [MonetaryCurrencies#igetCurrency(String)] for " +
                                 "currency code: " +
                                 currency.getCurrencyCode() + ", expected: " +
                                 Currency.getInstance(currency.getCurrencyCode()).getDefaultFractionDigits() +
                                 ", found: " + unit.getDefaultFractionDigits(),
                         Currency.getInstance(currency.getCurrencyCode()).getDefaultFractionDigits(),
                         unit.getDefaultFractionDigits()
            );
        }
    }

    /**
     * Test if MonetaryCurrencies provides correct instance with
     * Locales.
     */
    @Test
    @SpecAssertion(section = "4.2.7", id = "427-A4")
    public void testCorrectLocales(){
        for(String country : Locale.getISOCountries()){
            Locale locale = new Locale("", country);
            if(Currency.getInstance(locale) == null){
                continue;
            }
            CurrencyUnit unit = MonetaryCurrencies.getCurrency(locale);
            assertEquals("Invalid Currency code returned from [MonetaryCurrencies#igetCurrency(Locale)] for locale: " +
                                 locale + ", expected: " + Currency.getInstance(locale).getCurrencyCode() +
                                 ", found: " + unit.getCurrencyCode(), Currency.getInstance(locale).getCurrencyCode(),
                         unit.getCurrencyCode()
            );
            assertEquals("Invalid numeric code returned from [MonetaryCurrencies#igetCurrency(Locale)] for locale: " +
                                 locale + ", expected: " + Currency.getInstance(locale).getNumericCode() +
                                 ", found: " + unit.getNumericCode(), Currency.getInstance(locale).getNumericCode(),
                         unit.getNumericCode()
            );
            assertEquals("Invalid default fraction unit returned from [MonetaryCurrencies#igetCurrency(Locale)] for " +
                                 "locale: " +
                                 locale + ", expected: " + Currency.getInstance(locale).getDefaultFractionDigits() +
                                 ", found: " + unit.getDefaultFractionDigits(),
                         Currency.getInstance(locale).getDefaultFractionDigits(), unit.getDefaultFractionDigits()
            );
        }
    }

    /**
     * Test for custom MonetaryCurrencies provided, based on the TCK
     * TestProvider.
     */
    @Test
    @SpecAssertion(section = "4.2.7", id = "427-A5")
    public void testCustomCurrencies(){
        Locale testLocale = new Locale("lang", "count", "test");
        CurrencyUnit cu = MonetaryCurrencies.getCurrency(testLocale);
        assertNotNull("TestCurrency not returned for locale: " + testLocale, cu);
        assertEquals("Unexpected CurrencyUnit class returned.", TestCurrencyUnit.class, cu.getClass());
        cu = MonetaryCurrencies.getCurrency("FOOLS_test");
        assertNotNull("TestCurrency not returned for currency code: FOOLS_test", cu);
        assertEquals("Unexpected CurrencyUnit class returned.", TestCurrencyUnit.class, cu.getClass());
    }

    // ********************************* B. Accessing Monetary Amount Factories ***********************

    /**
     * Ensure the types available, must be at least one type (if one
     * has a specified AmountFlavor.java, 2 are recommended).
     */
    @Test
    @SpecAssertion(section = "4.2.7", id = "427-B1")
    public void testAmountTypesDefined(){
        Collection<Class> amountClasses = TCKTestSetup.getTestConfiguration().getAmountClasses();
        assertNotNull(amountClasses);
        assertFalse(amountClasses.isEmpty());
        Set<Class<? extends MonetaryAmount>> providedClasses = MonetaryAmounts.getAmountTypes();
        for(Class amountType : amountClasses){
            assertTrue("Amount class not registered: " + amountType.getName(), providedClasses.contains(amountType));
        }
    }

    /**
     * Ensure amount factories are accessible for all types
     * available,
     * providing also the
     * some test implementations with the
     * TCK.
     */
    @Test
    @SpecAssertion(section = "4.2.7", id = "427-B2")
    public void testAmountTypesProvided(){
        Collection<Class> amountClasses = TCKTestSetup.getTestConfiguration().getAmountClasses();
        assertNotNull(amountClasses);
        assertFalse(amountClasses.isEmpty());
        Set<Class<? extends MonetaryAmount>> providedClasses = MonetaryAmounts.getAmountTypes();
        for(Class amountType : amountClasses){
            assertTrue("Amount class not registered: " + amountType.getName(), providedClasses.contains(amountType));
        }
        assertTrue("TCK Amount class not registered: " + TestAmount.class, providedClasses.contains(TestAmount.class));
    }

    /**
     * Ensure amount factories are accessible for all types
     * available,
     * providing also the
     * some test implementations with the
     * TCK,
     * and that
     * every factory accessed
     * is a new instance.
     */
    @Test
    @SpecAssertion(section = "4.2.7", id = "427-B3")
    public void testAmountTypesInstantiatable(){
        Collection<Class> amountClasses = TCKTestSetup.getTestConfiguration().getAmountClasses();
        for(Class amountType : amountClasses){
            MonetaryAmountFactory<?> f = MonetaryAmounts.getAmountFactory(amountType);
            assertNotNull("MonetaryAmountFactory returned by MonetaryAmounts is null for " + amountType.getName(), f);
            MonetaryAmountFactory<?> f2 = MonetaryAmounts.getAmountFactory(amountType);
            assertNotNull("MonetaryAmountFactory returned by MonetaryAmounts is null for " + amountType.getName(), f2);
            assertNotSame("MonetaryAmountFactory instances are not distinct for " + amountType.getName(), f, f2);
            TestCurrencyUnit tc = new TestCurrencyUnit();
            MonetaryAmount m1 = f.setNumber(0L).setCurrency(tc).create();
            assertNotNull("MonetaryAmountFactory creates null amounts for " + amountType.getName(), m1);
            assertTrue("MonetaryAmountFactory creates non zero amounts for " + amountType.getName(), m1.isZero());
            assertEquals("MonetaryAmountFactory creates non zero amounts for " + amountType.getName(), 0L,
                         m1.getNumber().longValue());
            assertTrue("MonetaryAmountFactory creates non assignable amounts instances for " + amountType.getName(),
                       amountType.isAssignableFrom(m1.getClass()));
        }
        MonetaryAmountFactory<?> f = MonetaryAmounts.getAmountFactory(TestAmount.class);
        assertNotNull("MonetaryAmountFactory returned by MonetaryAmounts is null for " + TestAmount.class.getName(), f);
        assertEquals("MonetaryAmountFactory returned by MonetaryAmounts is obfuscated or proxied for " +
                             TestMonetaryAmountFactory.class.getName(), TestMonetaryAmountFactory.class, f.getClass());
    }

    /**
     * Ensure correct query function implementations, providing also
     * the some test implementations with the TCK.
     */
    @Test
    @SpecAssertion(section = "4.2.7", id = "427-B4")
    public void testAmountQueryType(){
        MonetaryContext ctx = new MonetaryContext.Builder(TestAmount.class).build();
        Class type = MonetaryAmounts.queryAmountType(ctx);
        assertNotNull("Amount type query should return explicitly queried type", type);
        assertEquals("Amount type query should return same explicitly queried type", TestAmount.class, type);
        ctx = new MonetaryContext.Builder().setFlavor(AmountFlavor.PRECISION).build();
        type = MonetaryAmounts.queryAmountType(ctx);
        assertNotNull("Amount type for PRECISION amount flavor must be provided", type);
        ctx = new MonetaryContext.Builder().setFlavor(AmountFlavor.PERFORMANCE).build();
        type = MonetaryAmounts.queryAmountType(ctx);
        assertNotNull("Amount type for PERFORMANCE amount flavor must be provided", type);
        ctx = new MonetaryContext.Builder().setFlavor(AmountFlavor.UNDEFINED).build();
        type = MonetaryAmounts.queryAmountType(ctx);
        assertNotNull("Amount type for UNDEFINED amount flavor must be provided", type);
    }

    /**
     * Ensure a default factory is returned. Test javamoney.config
     * for  configuring default value.
     */
    @Test
    @SpecAssertion(section = "4.2.7", id = "427-B5")
    public void testAmountDefaultType(){
        assertNotNull("No default MonetaryAmountFactory found.", MonetaryAmounts.getAmountFactory());
        // TODO check default configuration...
    }

    // ********************************* C. Accessing Roundings *****************************

    /**
     * Access roundings using all defined currencies, including TCK
     * custom currencies.
     */
    @Test
    @SpecAssertion(section = "4.2.7", id = "427-C1")
    public void testAccessRoundingsForCustomCurrencies_Default(){
        // Using default roundings...
        TestCurrencyUnit cu = new TestCurrencyUnit("ASDF", 3);
        MonetaryOperator r = MonetaryRoundings.getRounding();
        MonetaryAmount m =
                new TestMonetaryAmountFactory().setNumber(new BigDecimal("12.123456789101222232323")).setCurrency(cu)
                        .create();
        assertEquals("ASDF 12.123", m.with(r).toString());
        // should not throw an error!
        for(Currency currency : Currency.getAvailableCurrencies()){
            m = new TestMonetaryAmountFactory().setNumber(new BigDecimal("12.123456789101222232323"))
                    .setCurrency(currency.getCurrencyCode()).create();
            BigDecimal numVal = new BigDecimal("12.123456789101222232323");
            if(currency.getDefaultFractionDigits() >= 0){
                MonetaryAmount rounded = m.with(r); // should not throw an error
                assertEquals("Returned amount class must be the same as the input class to the rounding operator.",
                             TestAmount.class, rounded.getClass());
                assertEquals(currency.getCurrencyCode(), rounded.getCurrency().getCurrencyCode());
                assertNotSame("Rounding did not have any effect, should use scale==2 as default.",
                              m.getNumber().getScale(), rounded.getNumber().getScale());
            }
        }
    }

    /**
     * Access roundings using all defined currencies, including TCK
     * custom currencies.
     */
    @Test
    @SpecAssertion(section = "4.2.7", id = "427-C1")
    public void testAccessRoundingsForCustomCurrencies_Explicit(){
        // Using default roundings...
        TestCurrencyUnit cu = new TestCurrencyUnit("ASDF", 3);
        MonetaryOperator r = MonetaryRoundings.getRounding(cu);
        MonetaryAmount m =
                new TestMonetaryAmountFactory().setNumber(new BigDecimal("12.123456789101222232323")).setCurrency(cu)
                        .create();
        assertEquals("ASDF 12.123", m.with(r).toString());
        // should not throw an error!
        for(Currency currency : Currency.getAvailableCurrencies()){
            if(currency.getDefaultFractionDigits() >= 0){
                r = MonetaryRoundings.getRounding(cu);
                m = m.with(r); // should not throw an error
                assertEquals("ASDF 12.123", m.with(r).toString());
            }else{
                try{
                    r = null;
                    r = MonetaryRoundings.getRounding(cu);
                    assertNotNull(r);
                }
                catch(MonetaryException e){
                    // OK
                }
            }
        }
    }

    /**
     * Access roundings using all defined currencies, including TCK
     * custom currencies.
     */
    @Test(expected = NullPointerException.class)
    @SpecAssertion(section = "4.2.7", id = "427-C1")
    public void testAccessRoundingsForCustomCurrencies_Explicit_Null(){
        MonetaryRoundings.getRounding((CurrencyUnit) null);
    }


    /**
     * Access roundings using a MonetaryContext. Use different
     * MathContext/RoundingMode, as an attribute, when running
     * on the JDK.
     */
    @Test
    @SpecAssertion(section = "4.2.7", id = "427-C2")
    public void testAccessRoundingsWithRoundingContext(){
        RoundingContext ctx = new RoundingContext.Builder().setScale(1).setObject(RoundingMode.UP).build();
        MonetaryOperator r = MonetaryRoundings.getRounding(ctx);
        assertNotNull("No rounding provided for MonetaryContext", r);
        MonetaryAmount m =
                new TestMonetaryAmountFactory().setNumber(new BigDecimal("12.123456789101222232323")).setCurrency("CHF")
                        .create();
        assertEquals("CHF 12.2", m.with(r).toString());
    }

    /**
     * Access roundings using a RoundingContext, that is null.
     */
    @Test(expected = NullPointerException.class)
    @SpecAssertion(section = "4.2.7", id = "427-C2")
    public void testAccessRoundingsWithMonetaryContext_Null(){
        MonetaryOperator r = MonetaryRoundings.getRounding((RoundingContext) null);
    }

    /**
     * Access custom roundings and ensure TCK custom roundings are
     * registered.
     */
    @Test
    @SpecAssertion(section = "4.2.7", id = "427-C3")
    public void testAccessCustomRoundings(){
        Set<String> ids = MonetaryRoundings.getRoundingIds();
        assertNotNull("Custom Rounding key are null", ids);
        assertTrue("At least NOSCALE custom rounding must be present", ids.contains("NOSCALE"));
    }

    /**
     * Test TCK custom roundings.
     */
    @Test
    @SpecAssertion(section = "4.2.7", id = "427-C4")
    public void testCustomRoundings(){
        MonetaryOperator r = MonetaryRoundings.getRounding("NOSCALE");
        assertNotNull(r);
        MonetaryAmount m =
                new TestMonetaryAmountFactory().setNumber(new BigDecimal("12.123456789101222232323")).setCurrency("CHF")
                        .create();
        assertEquals("CHF 12", m.with(r).toString());
    }

    /**
     * Test TCK custom roundings.
     */
    @Test(expected = NullPointerException.class)
    @SpecAssertion(section = "4.2.7", id = "427-C4")
    public void testCustomRoundings_Null(){
        MonetaryRoundings.getRounding((String) null);
    }

    /**
     * Test TCK custom roundings.
     */
    @Test(expected = MonetaryException.class)
    @SpecAssertion(section = "4.2.7", id = "427-C4")
    public void testCustomRoundings_Foo(){
        MonetaryRoundings.getRounding("foo");
    }

}
