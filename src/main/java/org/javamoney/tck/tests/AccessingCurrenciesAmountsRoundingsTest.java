/*
 * Copyright (c) 2012, 2020, Werner Keil, Anatole Tresch. Licensed under the Apache
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
import org.javamoney.tck.tests.internal.TestMonetaryAmountBuilder;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import javax.money.MonetaryAmountFactoryQuery;
import javax.money.MonetaryAmountFactoryQueryBuilder;
import javax.money.Monetary;
import javax.money.MonetaryException;
import javax.money.MonetaryOperator;
import javax.money.MonetaryRounding;
import javax.money.RoundingQuery;
import javax.money.RoundingQueryBuilder;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Currency;
import java.util.Locale;
import java.util.Set;

import static org.testng.AssertJUnit.assertNotNull;

/**
 * Test class for accessing currencies and amounts.
 */
@SpecVersion(spec = "JSR 354", version = "1.1.0")
public class AccessingCurrenciesAmountsRoundingsTest {

    // ****************** A. Accessing Currencies *******************

    /**
     * Test if Monetary provides all ISO related entries,
     * similar to the JDK.
     */
    @Test(description = "4.2.7 Test if Monetary provides all ISO related entries similar to " +
            "java.util.Currency.")
    @SpecAssertion(section = "4.2.7", id = "427-A1")
    public void testAllISOCurrenciesAvailable() {
        for (Currency currency : Currency.getAvailableCurrencies()) {
            AssertJUnit.assertTrue(
                    "Section 4.2.7: Currency not available [Monetary#isCurrencyAvailable(String)] for JDK " +
                            "currency code: " +
                            currency.getCurrencyCode(),
                    Monetary.isCurrencyAvailable(currency.getCurrencyCode()));
            AssertJUnit.assertNotNull(
                    "Section 4.2.7: Currency null [Monetary#igetCurrency(String)] for JDK currency code: " +
                            currency.getCurrencyCode(), Monetary.getCurrency(currency.getCurrencyCode()));
        }
    }

    /**
     * Test if Monetary provides all Locale related
     * entries, similar to the JDK (for all ISO countries).
     */
    @Test(description = "4.2.7 Test if Monetary provides all locale related entries similar to " +
            "java.util.Currency.")
    @SpecAssertion(section = "4.2.7", id = "427-A2")
    public void testAllLocaleCurrenciesAvailable() {
        for (String country : Locale.getISOCountries()) {
            Locale locale = new Locale("", country);
            if (Currency.getInstance(locale) != null) {
                AssertJUnit.assertTrue(
                        "Section 4.2.7: Currency not available [Monetary#isCurrencyAvailable(Locale)] for " +
                                "locale: " +
                                locale, Monetary.isCurrencyAvailable(locale));
                AssertJUnit
                        .assertNotNull("Currency null [Monetary#igetCurrency(Locale)] for locale: " + locale,
                                Monetary.getCurrencies(locale));
                Collection<CurrencyUnit> units = Monetary.getCurrencies(locale);
                CurrencyUnit requiredCurrency = null;
                for (CurrencyUnit cu : units) {
                    if (Currency.getInstance(locale).getCurrencyCode().equals(cu.getCurrencyCode())) {
                        requiredCurrency = cu;
                        break;
                    }
                }
                AssertJUnit.assertNotNull(
                        "Section 4.2.7: No Currency returned from [Monetary#getCurrencies(Locale)] for " +
                                "locale: " +
                                locale, requiredCurrency);
                AssertJUnit.assertEquals(
                        "Section 4.2.7: Invalid Currency returned from [Monetary#getCurrencies(Locale)] for" +
                                " " +
                                "locale: " +
                                locale +
                                ", expected: " + Currency.getInstance(locale) + ", found: " + requiredCurrency,
                        Monetary.getCurrency(Currency.getInstance(locale).getCurrencyCode()),
                        requiredCurrency);
            }
        }
    }

    /**
     * Test if Monetary provides correct instance with ISO
     * codes.
     */
    @Test(description = "4.2.7 Test if Monetary provides correct ISO related entries similar to " +
            "java.util.Currency.")
    @SpecAssertion(section = "4.2.7", id = "427-A3")
    public void testCorrectISOCodes() {
        for (Currency currency : Currency.getAvailableCurrencies()) {
            CurrencyUnit unit = Monetary.getCurrency(currency.getCurrencyCode());
            AssertJUnit.assertEquals(
                    "Section 4.2.7: Invalid Currency code returned from [Monetary#igetCurrency(String)] for" +
                            " currency code:" +
                            " " +
                            currency.getCurrencyCode() + ", expected: " +
                            Currency.getInstance(currency.getCurrencyCode()).getCurrencyCode() +
                            ", found: " + unit.getCurrencyCode(),
                    Currency.getInstance(currency.getCurrencyCode()).getCurrencyCode(), unit.getCurrencyCode());
            AssertJUnit.assertEquals(
                    "Section 4.2.7: Invalid numeric code returned from [Monetary#igetCurrency(String)] for " +
                            "currency code: " +
                            currency.getCurrencyCode() + ", expected: " +
                            Currency.getInstance(currency.getCurrencyCode()).getNumericCode() +
                            ", found: " + unit.getNumericCode(),
                    Currency.getInstance(currency.getCurrencyCode()).getNumericCode(), unit.getNumericCode());
            AssertJUnit.assertEquals(
                    "Section 4.2.7: Invalid default fraction unit returned from [Monetary#igetCurrency" +
                            "(String)] for " +
                            "currency code: " +
                            currency.getCurrencyCode() + ", expected: " +
                            Currency.getInstance(currency.getCurrencyCode()).getDefaultFractionDigits() +
                            ", found: " + unit.getDefaultFractionDigits(),
                    Currency.getInstance(currency.getCurrencyCode()).getDefaultFractionDigits(),
                    unit.getDefaultFractionDigits());
        }
    }

    /**
     * Test if Monetary provides correct instance with
     * Locales.
     */
    @Test(description = "4.2.7 Test if Monetary provides correct locale related entries similar to " +
            "java.util.Currency.")
    @SpecAssertion(section = "4.2.7", id = "427-A4")
    public void testCorrectLocales() {

        Monetary.getCurrencies(new Locale("", "AD"));

        for (String country : Locale.getISOCountries()) {
            Locale locale = new Locale("", country);
            if (Currency.getInstance(locale) == null) {
                continue;
            }
            Set<CurrencyUnit> units = Monetary.getCurrencies(locale);
            AssertJUnit.assertNotNull(
                    "Section 4.2.7: Invalid Currencies (null) returned from [Monetary#igetCurrencies" +
                            "(Locale)] for" +
                            " locale: " +
                            locale, units);
            AssertJUnit.assertFalse(
                    "Section 4.2.7 Empty Currencies returned from [Monetary#igetCurrencies(Locale)] for" +
                            " locale: " +
                            locale, units.isEmpty());
            CurrencyUnit requiredCurrency = null;
            for (CurrencyUnit cu : units) {
                if (Currency.getInstance(locale).getCurrencyCode().equals(cu.getCurrencyCode())) {
                    requiredCurrency = cu;
                    break;
                }
            }
            AssertJUnit.assertNotNull("Section 4.2.7: Required Currency missing in result returned from " +
                    "[Monetary#igetCurrency(Locale)] for" +
                    " locale: " +
                    locale + ", expected: " + Currency.getInstance(locale).getCurrencyCode() +
                    ", found: " + units, requiredCurrency);
            AssertJUnit.assertEquals(
                    "Section 4.2.7: Invalid numeric code returned from [Monetary#igetCurrency(Locale)] for " +
                            "locale: " +
                            locale + ", expected: " + Currency.getInstance(locale).getNumericCode() +
                            ", found: " + requiredCurrency.getNumericCode(),
                    Currency.getInstance(locale).getNumericCode(), requiredCurrency.getNumericCode());
            AssertJUnit.assertEquals(
                    "Section 4.2.7: Invalid default fraction unit returned from [Monetary#igetCurrency" +
                            "(Locale)] for " +
                            "locale: " +
                            locale + ", expected: " + Currency.getInstance(locale).getDefaultFractionDigits() +
                            ", found: " + requiredCurrency.getDefaultFractionDigits(),
                    Currency.getInstance(locale).getDefaultFractionDigits(),
                    requiredCurrency.getDefaultFractionDigits());
        }
    }

    /**
     * Test for custom Monetary provided, based on the TCK
     * TestProvider.
     */
    @Test(description = "4.2.7 Test if Monetary provides customized locale identified currencies.")
    @SpecAssertion(section = "4.2.7", id = "427-A5")
    public void testCustomCurrencies() {
        Locale testLocale = new Locale("lang", "count", "test");
        Set<CurrencyUnit> cus = Monetary.getCurrencies(testLocale);
        AssertJUnit.assertNotNull("Section 4.2.7: TestCurrency not returned for locale: " + testLocale, cus);
        AssertJUnit.assertFalse("Section 4.2.7: TestCurrency not returned for locale: " + testLocale, cus.isEmpty());
        AssertJUnit.assertEquals("Section 4.2.7: Unexpected CurrencyUnit class returned.", TestCurrencyUnit.class,
                cus.iterator().next().getClass());
        CurrencyUnit cu = Monetary.getCurrency("FOOLS_test");
        AssertJUnit.assertNotNull("Section 4.2.7: TestCurrency not returned for currency code: FOOLS_test", cu);
        AssertJUnit.assertEquals("Section 4.2.7: Unexpected CurrencyUnit class returned.", TestCurrencyUnit.class,
                cu.getClass());
    }

    // ********************************* B. Accessing Monetary Amount Factories ***********************

    /**
     * Ensure amount factories are accessible for all types
     * available,
     * providing also the
     * some test implementations with the
     * TCK.
     */
    @Test(description = "4.2.7 Ensure amount classes to test are setup and registered/available in Monetary.")
    @SpecAssertion(section = "4.2.7", id = "427-B2")
    public void testAmountTypesProvided() {
        Collection<Class> amountClasses = TCKTestSetup.getTestConfiguration().getAmountClasses();
        AssertJUnit.assertNotNull(amountClasses);
        AssertJUnit.assertFalse(amountClasses.isEmpty());
        Collection<Class<? extends MonetaryAmount>> providedClasses = Monetary.getAmountTypes();
        for (Class amountType : amountClasses) {
            AssertJUnit.assertTrue("Section 4.2.7: Amount class not registered: " + amountType.getName(),
                    providedClasses.contains(amountType));
        }
        AssertJUnit.assertTrue("Section 4.2.7: TCK Amount class not registered: " + TestAmount.class,
                providedClasses.contains(TestAmount.class));
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
    @Test(description = "4.2.7 Ensure amount factories are accessible for all types available in Monetary.")
    @SpecAssertion(section = "4.2.7", id = "427-B3")
    public void testAmountTypesInstantiatable() {
        Collection<Class> amountClasses = TCKTestSetup.getTestConfiguration().getAmountClasses();
        for (Class amountType : amountClasses) {
            MonetaryAmountFactory<?> f = Monetary.getAmountFactory(amountType);
            AssertJUnit.assertNotNull("Section 4.2.7: MonetaryAmountFactory returned by Monetary is null for " +
                    amountType.getName(), f);
            MonetaryAmountFactory<?> f2 = Monetary.getAmountFactory(amountType);
            AssertJUnit.assertNotNull("Section 4.2.7: MonetaryAmountFactory returned by Monetary is null for " +
                    amountType.getName(), f2);
            AssertJUnit.assertNotSame("MonetaryAmountFactory instances are not distinct for " + amountType.getName(), f,
                    f2);
            TestCurrencyUnit tc = new TestCurrencyUnit();
            MonetaryAmount m1 = f.setNumber(0L).setCurrency(tc).create();
            AssertJUnit.assertNotNull(
                    "Section 4.2.7: MonetaryAmountFactory creates null amounts for " + amountType.getName(), m1);
            AssertJUnit.assertTrue(
                    "Section 4.2.7: MonetaryAmountFactory creates non zero amounts for " + amountType.getName(),
                    m1.isZero());
            AssertJUnit.assertEquals(
                    "Section 4.2.7: MonetaryAmountFactory creates non zero amounts for " + amountType.getName(), 0L,
                    m1.getNumber().longValue());
            AssertJUnit.assertTrue(
                    "Section 4.2.7: MonetaryAmountFactory creates non assignable amounts instances for " +
                            amountType.getName(), amountType.isAssignableFrom(m1.getClass()));
        }
        MonetaryAmountFactory<?> f = Monetary.getAmountFactory(TestAmount.class);
        AssertJUnit.assertNotNull("Section 4.2.7: MonetaryAmountFactory returned by Monetary is null for " +
                TestAmount.class.getName(), f);
        AssertJUnit.assertEquals("MonetaryAmountFactory returned by Monetary is obfuscated or proxied for " +
                        TestMonetaryAmountBuilder.class.getName(), TestMonetaryAmountBuilder.class,
                f.getClass());
    }

    /**
     * Ensure correct query function implementations, providing also
     * the some test implementations with the TCK.
     */
    @Test(description =
            "4.2.7 Ensure correct query function, Monetary.getAmountFactories should return factory" +
                    "for explicit acquired amount types.")
    @SpecAssertion(section = "4.2.7", id = "427-B4")
    public void testAmountQueryType() {
        MonetaryAmountFactoryQuery ctx = MonetaryAmountFactoryQueryBuilder.of().setTargetType(TestAmount.class).build();
        Collection<MonetaryAmountFactory<?>> factories = Monetary.getAmountFactories(ctx);
        AssertJUnit.assertNotNull("Section 4.2.7: Amount factory query should return explicitly queried factories",
                factories);
        boolean found = false;
        for (MonetaryAmountFactory<?> f : factories) {
            if (f.getAmountType().equals(TestAmount.class)) {
                found = true;
                break;
            }
        }
        AssertJUnit.assertTrue("Section 4.2.7: Amount type query should return same explicitly queried factory", found);
        ctx = MonetaryAmountFactoryQueryBuilder.of().build();
        MonetaryAmountFactory<?> factory = Monetary.getAmountFactory(ctx);
        AssertJUnit.assertNotNull("Section 4.2.7: Amount type must be provided", factory);
    }

    /**
     * Ensure a default factory is returned. Test javamoney.config
     * for  configuring default value.
     */
    @Test(description = "4.2.7 Ensure a default MonetaryAmountFactory is available.")
    @SpecAssertion(section = "4.2.7", id = "427-B5")
    public void testAmountDefaultType() {
        AssertJUnit.assertNotNull("Section 4.2.7: No default MonetaryAmountFactory found.",
                Monetary.getDefaultAmountFactory());
    }

    // ********************************* C. Accessing Roundings *****************************

    /**
     * Access roundings using all defined currencies, including TCK
     * custom currencies.
     */
    @Test(description = "4.2.7 Ensure Monetary instances are available, for all registered currencies.")
    @SpecAssertion(section = "4.2.7", id = "427-C1")
    public void testAccessRoundingsForCustomCurrencies_Default() {
        // Using default roundings...
        TestCurrencyUnit cu = new TestCurrencyUnit("ASDF", 3);
        MonetaryRounding r = Monetary.getDefaultRounding();
        MonetaryAmount m =
                new TestMonetaryAmountBuilder().setNumber(new BigDecimal("12.123456789101222232323")).setCurrency(cu)
                        .create();
        AssertJUnit.assertEquals(
                "Section 4.2.7 Expected ASDF 12.123 with default rounding from ASDF 12.123456789101222232323",
                "ASDF 12.123", m.with(r).toString());
        // should not throw an error!
        for (Currency currency : Currency.getAvailableCurrencies()) {
            m = new TestMonetaryAmountBuilder().setNumber(new BigDecimal("12.123456789101222232323"))
                    .setCurrency(currency.getCurrencyCode()).create();
            if (currency.getDefaultFractionDigits() >= 0) {
                MonetaryAmount rounded = m.with(r); // should not throw an error
                AssertJUnit.assertEquals(
                        "Section 4.2.7: Returned amount class must be the same as the input class to the rounding " +
                                "operator.", TestAmount.class, rounded.getClass());
                AssertJUnit.assertEquals("Section 4.2.7: Rounding did change currency: " + rounded.getClass().getName(),
                        currency.getCurrencyCode(), rounded.getCurrency().getCurrencyCode());
                AssertJUnit.assertNotSame(
                        "Section 4.2.7: Rounding did not have any effect, should use scale==2 as default.",
                        m.getNumber().getScale(), rounded.getNumber().getScale());
            }
        }
    }

    /**
     * Access roundings using all defined currencies, including TCK
     * custom currencies.
     */
    @Test(description = "4.2.7 Ensure Monetary instances are available, also for any custom currency " +
            "(not registered).")
    @SpecAssertion(section = "4.2.7", id = "427-C1")
    public void testAccessRoundingsForCustomCurrencies_Explicit() {
        // Using default roundings...
        TestCurrencyUnit cu = new TestCurrencyUnit("ASDF", 3);
        MonetaryOperator r = Monetary.getRounding(cu);
        MonetaryAmount m =
                new TestMonetaryAmountBuilder().setNumber(new BigDecimal("12.123456789101222232323")).setCurrency(cu)
                        .create();
        AssertJUnit.assertEquals("Section 4.2.7 Expected ASDF 12.123 for custom rounding 12.123456789101222232323.",
                "ASDF 12.123", m.with(r).toString());
        // should not throw an error!
        for (Currency currency : Currency.getAvailableCurrencies()) {
            if (currency.getDefaultFractionDigits() >= 0) {
                r = Monetary.getRounding(cu);
                m = m.with(r); // should not throw an error
                AssertJUnit.assertEquals(
                        "Section 4.2.7 Expected ASDF 12.123 for rounding for Currency" + cu.getCurrencyCode(),
                        "ASDF 12.123", m.with(r).toString());
            } else {
                try {
                    r = Monetary.getRounding(cu);
                    AssertJUnit.assertNotNull(r);
                } catch (MonetaryException e) {
                    // OK
                }
            }
        }
    }

    /**
     * Access roundings using all defined currencies, including TCK
     * custom currencies.
     */
    @Test(expectedExceptions = NullPointerException.class,
            description = "4.2.7 Expected NullPointerException accessing a rounding with " +
                    "'Monetary.getRounding(null)'.")
    @SpecAssertion(section = "4.2.7", id = "427-C1")
    public void testAccessRoundingsForCustomCurrencies_Explicit_Null() {
        Monetary.getRounding((CurrencyUnit) null);
    }


    /**
     * Access roundings using a MonetaryContext. Use different
     * MathContext/RoundingMode, as an attribute, when running
     * on the JDK.
     */
    @Test(description = "4.2.7 Ensure correct MonetaryRounding returned for a mathematical RoundingQuery.")
    @SpecAssertion(section = "4.2.7", id = "427-C2")
    public void testAccessRoundingsWithRoundingContext() {
        RoundingQuery ctx = RoundingQueryBuilder.of().setScale(1).set(RoundingMode.UP).build();
        MonetaryOperator r = Monetary.getRounding(ctx);
        AssertJUnit.assertNotNull("Section 4.2.7: No rounding provided for RoundingQuery: " + ctx, r);
        MonetaryAmount m =
                new TestMonetaryAmountBuilder().setNumber(new BigDecimal("12.123456789101222232323")).setCurrency("CHF")
                        .create();
        AssertJUnit.assertEquals("Section 4.2.7: Invalid rounding provided for RoundingQuery: " + ctx, "CHF 12.2",
                m.with(r).toString());
    }

    /**
     * Access roundings using a RoundingContext, that is null.
     */
    @Test(expectedExceptions = NullPointerException.class,
            description = "4.2.7 Ensure NullPointerException is thrown for " +
                    "'Monetary.getRounding((RoundingContext) null)'.")
    @SpecAssertion(section = "4.2.7", id = "427-C2")
    public void testAccessRoundingsWithMonetaryContext_Null() {
        Monetary.getRounding(null);
    }

    /**
     * Access custom roundings and ensure TCK custom roundings are
     * registered.
     */
    @Test(description = "4.2.7 Access named roundings and ensure TCK named roundings are " + "registered.")
    @SpecAssertion(section = "4.2.7", id = "427-C3")
    public void testAccessCustomRoundings() {
        Set<String> ids = Monetary.getRoundingNames();
        AssertJUnit.assertNotNull("Section 4.2.7: Custom Rounding key are null", ids);
        AssertJUnit
                .assertTrue("Section 4.2.7: At least NOSCALE custom rounding must be present", ids.contains("NOSCALE"));
    }

    /**
     * Test TCK custom roundings.
     */
    @Test(description = "4.2.7 Access custom roundings and ensure correct functionality.")
    @SpecAssertion(section = "4.2.7", id = "427-C4")
    public void testCustomRoundings() {
        MonetaryOperator r = Monetary.getRounding("NOSCALE");
        AssertJUnit.assertNotNull(r);
        MonetaryAmount m =
                new TestMonetaryAmountBuilder().setNumber(new BigDecimal("12.123456789101222232323")).setCurrency("CHF")
                        .create();
        AssertJUnit.assertEquals("Section 4.2.7 Expected CHF 12 for NOSCALE operator on " + m, "CHF 12",
                m.with(r).toString());
    }

    /**
     * Test TCK custom roundings.
     */
    @Test(expectedExceptions = NullPointerException.class,
            description = "4.2.7 Ensure NullPointerException is thrown for Monetary.getRounding((String) null).")
    @SpecAssertion(section = "4.2.7", id = "427-C4")
    public void testCustomRoundings_Null() {
        Monetary.getRounding((String) null);
    }

    /**
     * Test TCK custom roundings.
     */
    @Test(expectedExceptions = MonetaryException.class,
            description = "4.2.7 Ensure MonetaryException is thrown for " + "accessing invalid named rounding.")
    @SpecAssertion(section = "4.2.7", id = "427-C4")
    public void testCustomRoundings_Foo() {
        assertNotNull("Section 4.2.7 Expected custom rounding with name 'foo'.", Monetary.getRounding("foo"));
    }

}
