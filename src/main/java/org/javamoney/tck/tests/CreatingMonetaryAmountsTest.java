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

import org.javamoney.tck.TestUtils;
import org.javamoney.tck.tests.internal.TestAmount;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import javax.money.*;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * Created by Anatole on 10.03.14.
 */
@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class CreatingMonetaryAmountsTest {

    // ************************ A. Accessing MonetaryAmount Factories ************************

    /**
     * Access a MonetaryAmountFactory for each registered type.
     */
    @Test(description = "4.2.6 Ensure MonetaryAmountFactory instances are accessible for all amount types under test.")
    @SpecAssertion(section = "4.2.6", id = "426-A1")
    public void testAccessToMonetaryAmountFactory() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            AssertJUnit.assertNotNull("Section 4.2.6: No MonetaryAmountFactory available for " + type.getName(),
                    MonetaryAmounts.getAmountFactory(type));
        }
    }

    /**
     * For each MonetaryAmountFactory: Check if getAmountType returns the correct type.
     */
    @Test(description =
            "4.2.6 Ensure MonetaryAmountFactory instances accessible for all amount types under test return " +
                    "correct amount type.")
    @SpecAssertion(section = "4.2.6", id = "426-A2")
    public void testMonetaryAmountFactoryReturnsCorrectType() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            AssertJUnit.assertEquals(
                    "Section 4.2.6: MonetaryAmountFactory declares invalid amount type for " + type.getName(), type,
                    MonetaryAmounts.getAmountFactory(type).getAmountType());
        }
    }

    /**
     * For each MonetaryAmountFactory: Check if capabilities of default MonetaryContext are less, or equal
     * than Max
     * MonetaryContext.
     */
    @Test(description =
            "4.2.6 Ensure MonetaryAmountFactory instances accessible for all amount types under test return " +
                    "correct min/max MonetaryContext.")
    @SpecAssertion(section = "4.2.6", id = "426-A3")
    public void testMonetaryAmountFactoryMinMaxCapabilities() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            MonetaryAmountFactory<?> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryContext defCtx = f.getDefaultMonetaryContext();
            MonetaryContext maxCts = f.getMaximalMonetaryContext();
            AssertJUnit.assertTrue("Section 4.2.6: MonetaryAmountFactory default/max declares invalid precisions for " +
                            type.getName(),
                    maxCts.getPrecision() == 0 || defCtx.getPrecision() <= maxCts.getPrecision());
            AssertJUnit.assertTrue(
                    "Section 4.2.6: MonetaryAmountFactory default/max declares invalid scales for " + type.getName(),
                    maxCts.getMaxScale() == -1 || defCtx.getMaxScale() <= maxCts.getMaxScale());
        }
    }

    /**
     * Checks if capabilities of default MonetaryContext are less than Max MonetaryContext.
     */
    @Test(description =
            "4.2.6 Ensure MonetaryAmountFactory instances accessible for all amount types under test return " +
                    "correct min/max MonetaryContext (min <= max).")
    @SpecAssertion(section = "4.2.6", id = "426-A4")
    public void testMonetaryAmountFactoryMinMaxCapabilities_Compare() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            MonetaryAmountFactory<?> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryContext defCtx = f.getDefaultMonetaryContext();
            MonetaryContext maxCtx = f.getMaximalMonetaryContext();
            if (f.getDefaultMonetaryContext().getMaxScale() > -1) {
                AssertJUnit.assertTrue(
                        "Section 4.2.6: MonetaryAmountFactory maximal MonetaryContext cannot be less capable than the" +
                                " default " +
                                "(maxScale default/max=" +
                                f.getDefaultMonetaryContext().getMaxScale() + '/' +
                                f.getMaximalMonetaryContext().getMaxScale() + " for " + type.getName(),
                        maxCtx.getMaxScale() == -1 || defCtx.getMaxScale() <= maxCtx.getMaxScale());
            }
            if (f.getDefaultMonetaryContext().getMaxScale() == -1) {
                AssertJUnit.assertTrue(
                        "Section 4.2.6: MonetaryAmountFactory maximal MonetaryContext cannot be less capable than the" +
                                " default " +
                                "(maxScale default/max=" +
                                f.getDefaultMonetaryContext().getMaxScale() + '/' +
                                f.getMaximalMonetaryContext().getMaxScale() + " for " + type.getName(),
                        maxCtx.getMaxScale() == -1);
            }
            if (f.getDefaultMonetaryContext().getPrecision() > 0) {
                AssertJUnit.assertTrue(
                        "Section 4.2.6: MonetaryAmountFactory maximal MonetaryContext cannot be less capable than the" +
                                " default " +
                                "(precision default/max=" +
                                f.getDefaultMonetaryContext().getPrecision() + '/' +
                                f.getMaximalMonetaryContext().getPrecision() + " for " + type.getName(),
                        maxCtx.getPrecision() == 0 || defCtx.getPrecision() <= maxCtx.getPrecision());
            }
            if (f.getDefaultMonetaryContext().getPrecision() == 0) {
                AssertJUnit.assertTrue(
                        "Section 4.2.6: MonetaryAmountFactory maximal MonetaryContext cannot be less capable than the" +
                                " default " +
                                "(precision default/max=" +
                                f.getDefaultMonetaryContext().getPrecision() + '/' +
                                f.getMaximalMonetaryContext().getPrecision() + " for " + type.getName(),
                        maxCtx.getPrecision() == 0);
            }
        }
    }

    // **************** B. Testing Creation of Amounts with zero values ******************

    /**
     * For each MonetaryAmountFactory: Create zero amounts from a
     * factory with currencies.
     */
    @Test(description = "4.2.6 Ensure MonetaryAmountFactory instances support creation of 0 amounts.")
    @SpecAssertion(section = "4.2.6", id = "426-B1")
    public void testMonetaryAmountFactoryCreateZeroAmountsWithDiffCurrencies() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            for (Currency cur : Currency.getAvailableCurrencies()) {
                CurrencyUnit cu = MonetaryCurrencies.getCurrency(cur.getCurrencyCode());
                MonetaryAmountFactory<?> f = MonetaryAmounts.getAmountFactory(type);
                f.setCurrency(cu);
                f.setNumber(0);
                MonetaryAmount m = f.create();
                AssertJUnit.assertEquals(
                        "Section 4.2.6: Amount created with factory has invalid currency for " + type.getName(), cu,
                        m.getCurrency());
                AssertJUnit.assertEquals(
                        "Section 4.2.6: Amount created with factory returns invalid amount type " + type.getName(),
                        type, m.getClass());
                AssertJUnit.assertTrue(
                        "Section 4.2.6: Amount created with factory has invalid value for " + type.getName(),
                        m.isZero());
                AssertJUnit.assertTrue("Amount created with factory has invalid value for " + type.getName(),
                        m.signum() == 0);
                AssertJUnit.assertTrue(
                        "Section 4.2.6: Amount created with factory has invalid value for " + type.getName(),
                        m.getNumber().intValueExact() == 0);
            }
        }
    }

    /**
     * For each MonetaryAmount Factory: Create zero amounts from a
     * factory with monetary contexts.
     */
    @Test(description = "4.2.6 Ensure MonetaryAmountFactory instances support creation of 0 amounts, with explicit " +
            "MonetaryContext.")
    @SpecAssertion(section = "4.2.6", id = "426-B2")
    public void testMonetaryAmountFactoryCreateZeroAmountsWithDiffContexts() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<?> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("GBP");
            f.setNumber(0);
            MonetaryContext maxCtx = f.getMaximalMonetaryContext();
            if (maxCtx.getPrecision() != 0) {
                for (int p = maxCtx.getPrecision(); p > 0; p--) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setPrecision(p).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getPrecision() == 0 || m.getMonetaryContext().getPrecision() >= p);
                }
            } else {
                for (int p = 0; p < 100; p += 10) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setPrecision(p).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getPrecision() == 0 || m.getMonetaryContext().getPrecision() >= p);
                }
            }
            if (maxCtx.getMaxScale() != -1) {
                for (int s = maxCtx.getMaxScale(); s >= 0; s--) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setMaxScale(s).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the scale setTyped on the context for " + type.getName(),
                            m.getMonetaryContext().getMaxScale() == -1 || m.getMonetaryContext().getMaxScale() >= s);
                }
            } else {
                for (int s = 0; s < 100; s += 10) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setMaxScale(s).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getMaxScale() == -1 || m.getMonetaryContext().getMaxScale() >= s);
                }
            }
        }
    }

    /**
     * For each MonetaryAmount Factory: Create zero amounts from a
     * factory with monetary contexts.
     */
    @Test(description = "4.2.6 Ensure MonetaryAmountFactory instances support creation of 0 amounts, with different " +
            "explicit MonetaryContext.")
    @SpecAssertion(section = "4.2.6", id = "426-B2")
    public void testMonetaryAmountFactoryCreateZeroAmountsWithDiffContexts2() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<?> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("GBP");
            f.setNumber(0.0d);
            MonetaryContext maxCtx = f.getMaximalMonetaryContext();
            if (maxCtx.getPrecision() != 0) {
                for (int p = maxCtx.getPrecision(); p > 0; p--) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setPrecision(p).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getPrecision() == 0 || m.getMonetaryContext().getPrecision() >= p);
                }
            } else {
                for (int p = 0; p < 100; p += 10) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setPrecision(p).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getPrecision() == 0 || m.getMonetaryContext().getPrecision() >= p);
                }
            }
            if (maxCtx.getMaxScale() != -1) {
                for (int s = maxCtx.getMaxScale(); s >= 0; s--) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setMaxScale(s).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the scale setTyped on the context for " + type.getName(),
                            m.getMonetaryContext().getMaxScale() == -1 || m.getMonetaryContext().getMaxScale() >= s);
                }
            } else {
                for (int s = 0; s < 100; s += 10) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setMaxScale(s).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getMaxScale() == -1 || m.getMonetaryContext().getMaxScale() >= s);
                }
            }
        }
    }

    /**
     * For each MonetaryAmount Factory: Create zero amounts from a
     * factory with monetary contexts.
     */
    @Test(description = "4.2.6 Ensure MonetaryAmountFactory instances support creation of 0 amounts, with different " +
            "explicit MonetaryContext (precision, scale).")
    @SpecAssertion(section = "4.2.6", id = "426-B2")
    public void testMonetaryAmountFactoryCreateZeroAmountsWithDiffContexts3() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<?> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("GBP");
            f.setNumber(BigDecimal.ZERO);
            MonetaryContext maxCtx = f.getMaximalMonetaryContext();
            if (maxCtx.getPrecision() != 0) {
                for (int p = maxCtx.getPrecision(); p > 0; p--) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setPrecision(p).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getPrecision() == 0 || m.getMonetaryContext().getPrecision() >= p);
                }
            } else {
                for (int p = 0; p < 100; p += 10) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setPrecision(p).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getPrecision() == 0 || m.getMonetaryContext().getPrecision() >= p);
                }
            }
            if (maxCtx.getMaxScale() != -1) {
                for (int s = maxCtx.getMaxScale(); s >= 0; s--) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setMaxScale(s).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the scale setTyped on the context for " + type.getName(),
                            m.getMonetaryContext().getMaxScale() == -1 || m.getMonetaryContext().getMaxScale() >= s);
                }
            } else {
                for (int s = 0; s < 100; s += 10) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setMaxScale(s).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getMaxScale() == -1 || m.getMonetaryContext().getMaxScale() >= s);
                }
            }
        }
    }


    /**
     * For each MonetaryAmount Factory: Bad Case: Create zero amounts
     * from a factory with an invalid currency.
     */
    @Test(description = "4.2.6 Bad case: For each MonetaryAmount Factory: Create zero amounts" +
            " from a factory with an invalid currency.")
    @SpecAssertion(section = "4.2.6", id = "426-B3")
    public void testMonetaryAmountFactoryCreateAmountsWithInvalidCurrency() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            for (Currency cur : Currency.getAvailableCurrencies()) {
                MonetaryCurrencies.getCurrency(cur.getCurrencyCode());
                MonetaryAmountFactory<?> f = MonetaryAmounts.getAmountFactory(type);
                try {
                    f.setCurrency("shjgssgsjgsj");
                    AssertJUnit
                            .fail("Section 4.2.6: Factory should throw UnknownCurrencyException for invalid currency," +
                                    " type was " +
                                    type.getName());
                } catch (UnknownCurrencyException e) {
                    // OK
                }
            }
        }
    }

    /**
     * For each MonetaryAmount Factory: Bad Case: Create zero amounts
     * from a factory with an invalid contexts.
     */
    @Test(description = "4.2.6 Bad case: For each MonetaryAmount Factory: Create zero amounts" +
            " from a factory with an invalid MonetaryContext.")
    @SpecAssertion(section = "4.2.6", id = "426-B4")
    public void testMonetaryAmountFactoryCreateAmountsWithInvalidMonetaryContext() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<?> f = MonetaryAmounts.getAmountFactory(type);
            try {
                f.setCurrency("USD");
                MonetaryContext maxCtx = f.getMaximalMonetaryContext();
                if (maxCtx.getPrecision() != 0) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setPrecision(maxCtx.getPrecision() + 1)
                            .build());
                    AssertJUnit.fail("Section 4.2.6: Factory should throw MonetaryException for invalid context " +
                            "(exceeding precision), " +
                            "type was " +
                            type.getName());
                }
                if (maxCtx.getMaxScale() != -1) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setMaxScale(maxCtx.getMaxScale() + 1)
                            .build());
                    AssertJUnit.fail("Section 4.2.6: Factory should throw MonetaryException for invalid context " +
                            "(exceeding scale), " +
                            "type was " +
                            type.getName());
                }
            } catch (MonetaryException e) {
                // OK
            }
        }
    }


    // ********************* C. Testing Creation of Amounts with positive values **************

    /**
     * For each MonetaryAmount Factory: Create positive amounts from
     * a factory with currencies.
     */
    @Test(description = "4.2.6 For each MonetaryAmount Factory: Create positive amounts.")
    @SpecAssertion(section = "4.2.6", id = "426-C1")
    public void testMonetaryAmountFactoryCreatePositiveAmountsWitCurrencies() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            for (Currency cur : Currency.getAvailableCurrencies()) {
                CurrencyUnit cu = MonetaryCurrencies.getCurrency(cur.getCurrencyCode());
                MonetaryAmountFactory<?> f = MonetaryAmounts.getAmountFactory(type);
                f.setCurrency(cu);
                f.setNumber(1);
                MonetaryAmount m = f.create();
                AssertJUnit.assertEquals(
                        "Section 4.2.6: Amount created with factory has invalid currency for " + type.getName(), cu,
                        m.getCurrency());
                AssertJUnit.assertEquals(
                        "Section 4.2.6: Amount created with factory returns invalid amount type " + type.getName(),
                        type, m.getClass());
                AssertJUnit.assertTrue(
                        "Section 4.2.6: Amount created with factory has invalid value for " + type.getName(),
                        m.isPositive());
                AssertJUnit.assertTrue(
                        "Section 4.2.6: Amount created with factory has invalid value for " + type.getName(),
                        m.signum() == 1);
                AssertJUnit.assertTrue(
                        "Section 4.2.6: Amount created with factory has invalid value for " + type.getName(),
                        m.getNumber().intValueExact() == 1);
            }
        }
    }

    /**
     * For each MonetaryAmount Factory: Create positive amounts from
     * a factory with monetary contexts.
     */
    @Test(description = "4.2.6 For each MonetaryAmount Factory: Create positive amounts with explicit MonetaryContext.")
    @SpecAssertion(section = "4.2.6", id = "426-C2")
    public void testMonetaryAmountFactoryCreatePositiveAmountsWithContexts() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<?> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("GBP");
            f.setNumber(1);
            MonetaryContext maxCtx = f.getMaximalMonetaryContext();
            if (maxCtx.getPrecision() != 0) {
                for (int p = maxCtx.getPrecision(); p > 0; p--) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setPrecision(p).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getPrecision() == 0 || m.getMonetaryContext().getPrecision() >= p);
                }
            } else {
                for (int p = 0; p < 100; p += 10) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setPrecision(p).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getPrecision() == 0 || m.getMonetaryContext().getPrecision() >= p);
                }
            }
            if (maxCtx.getMaxScale() != -1) {
                for (int s = maxCtx.getMaxScale(); s >= 0; s--) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setMaxScale(s).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the scale setTyped on the context for " + type.getName(),
                            m.getMonetaryContext().getMaxScale() == -1 || m.getMonetaryContext().getMaxScale() >= s);
                }
            } else {
                for (int s = 0; s < 100; s += 10) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setMaxScale(s).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getMaxScale() == -1 || m.getMonetaryContext().getMaxScale() >= s);
                }
            }
        }
    }

    /**
     * For each MonetaryAmount Factory: Create positive amounts from
     * a factory with monetary contexts.
     */
    @Test(description =
            "4.2.6 For each MonetaryAmount Factory: Create positive amounts using doubles with explicit " +
                    "MonetaryContext " +
                    "(precision/scale).")
    @SpecAssertion(section = "4.2.6", id = "426-C2")
    public void testMonetaryAmountFactoryCreatePositiveAmountsWithContexts2() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<?> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("GBP");
            f.setNumber(1.0d);
            MonetaryContext maxCtx = f.getMaximalMonetaryContext();
            if (maxCtx.getPrecision() != 0) {
                for (int p = maxCtx.getPrecision(); p > 0; p--) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setPrecision(p).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getPrecision() == 0 || m.getMonetaryContext().getPrecision() >= p);
                }
            } else {
                for (int p = 0; p < 100; p += 10) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setPrecision(p).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getPrecision() == 0 || m.getMonetaryContext().getPrecision() >= p);
                }
            }
            if (maxCtx.getMaxScale() != -1) {
                for (int s = maxCtx.getMaxScale(); s >= 0; s--) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setMaxScale(s).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the scale setTyped on the context for " + type.getName(),
                            m.getMonetaryContext().getMaxScale() == -1 || m.getMonetaryContext().getMaxScale() >= s);
                }
            } else {
                for (int s = 0; s < 100; s += 10) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setMaxScale(s).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getMaxScale() == -1 || m.getMonetaryContext().getMaxScale() >= s);
                }
            }
        }
    }

    /**
     * For each MonetaryAmount Factory: Create positive amounts from
     * a factory with monetary contexts.
     */
    @Test(description =
            "4.2.6 For each MonetaryAmount Factory: Create positive amounts using BigDecimal with explicit " +
                    "MonetaryContext " +
                    "(precision/scale).")
    @SpecAssertion(section = "4.2.6", id = "426-C2")
    public void testMonetaryAmountFactoryCreatePositiveAmountsWithContexts3() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<?> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("GBP");
            f.setNumber(BigDecimal.ONE);
            MonetaryContext maxCtx = f.getMaximalMonetaryContext();
            if (maxCtx.getPrecision() != 0) {
                for (int p = maxCtx.getPrecision(); p > 0; p--) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setPrecision(p).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getPrecision() == 0 || m.getMonetaryContext().getPrecision() >= p);
                }
            } else {
                for (int p = 0; p < 100; p += 10) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setPrecision(p).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getPrecision() == 0 || m.getMonetaryContext().getPrecision() >= p);
                }
            }
            if (maxCtx.getMaxScale() != -1) {
                for (int s = maxCtx.getMaxScale(); s >= 0; s--) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setMaxScale(s).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the scale setTyped on the context for " + type.getName(),
                            m.getMonetaryContext().getMaxScale() == -1 || m.getMonetaryContext().getMaxScale() >= s);
                }
            } else {
                for (int s = 0; s < 100; s += 10) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setMaxScale(s).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getMaxScale() == -1 || m.getMonetaryContext().getMaxScale() >= s);
                }
            }
        }
    }

    /**
     * For each MonetaryAmount Factory: Bad Case: Create positive
     * amounts from a factory with an invalid numeric value (exceeding max
     * MonetaryContext).
     */
    @Test(description =
            "4.2.6 Bad case: For each MonetaryAmount Factory: Create positive amounts using invalid numbers," +
                    " expecting ArithemticException thrown.")
    @SpecAssertion(section = "4.2.6", id = "426-C3")
    public void testMonetaryAmountFactoryCreatePositiveAmountsWithInvalidNumber() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("INR");
            MonetaryContext ctx = f.getMaximalMonetaryContext();
            if (ctx.getPrecision() != 0) {
                try {
                    f.setNumber(TestUtils.createNumberWithPrecision(f, ctx.getPrecision() + 5));
                    f.create();
                    AssertJUnit.fail("Section 4.2.6: MonetaryAmountFactory must throw an ArithmeticException, " +
                            "when an amount with exceeding precision is " +
                            "tried" +

                            " being created, type: " +
                            type.getName());
                } catch (ArithmeticException e) {
                    // OK
                }
            }
            if (ctx.getMaxScale() != -1) {
                try {
                    f.setNumber(TestUtils.createNumberWithScale(f, ctx.getMaxScale() + 5));
                    f.create();
                    AssertJUnit.fail("Section 4.2.6: MonetaryAmountFactory must throw an ArithmeticException, " +
                            "when an amount with exceeding scale is tried" +
                            " being created, type: " +
                            type.getName());
                } catch (ArithmeticException e) {
                    // OK
                }
            }
        }
    }

    /**
     * For each MonetaryAmount Factory: Bad Case: Create negative amounts from a factory with an no currency.
     */
    @Test(description = "4.2.6 Bad case: For each MonetaryAmount Factory: Create negative amounts without currency," +
            " expecting MonetaryException thrown.")
    @SpecAssertion(section = "4.2.6", id = "426-C4")
    public void testMonetaryAmountFactoryCreatePositiveNoCurrency_BadCase() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory f = MonetaryAmounts.getAmountFactory(type);
            try {
                if (f.getDefaultMonetaryContext().getPrecision() == 0) {
                    f.setNumber(TestUtils.createNumberWithPrecision(f, 5));
                } else {
                    f.setNumber(TestUtils.createNumberWithPrecision(f, f.getDefaultMonetaryContext().getPrecision()));
                }
                f.create();
                AssertJUnit.fail("Section 4.2.6: MonetaryAmountFactory must throw a MonetaryException, " +
                        "when a positive amount without a currency" +
                        " is" +
                        " tried to be created, type: " +
                        type.getName());
            } catch (MonetaryException e) {
                // OK
            } catch (Exception e) {
                AssertJUnit.fail("Section 4.2.6: MonetaryAmountFactory must throw a MonetaryException, " +
                        "when a positive amount without a currency" +
                        " is" +
                        " tried to be created, but threw " + e.getClass() + " type: " +
                        type.getName());
            }
        }
    }

    /**
     * For each MonetaryAmount Factory: Bad Case: Create negative amounts from a factory with an invalid currency.
     */
    @Test(description =
            "4.2.6 Bad case: For each MonetaryAmount Factory: Create negative amounts with an invalid currency," +
                    " expecting MonetaryException thrown.")
    @SpecAssertion(section = "4.2.6", id = "426-C4")
    public void testMonetaryAmountFactoryCreatePositiveInvalidCurrency_BadCase() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory f = MonetaryAmounts.getAmountFactory(type);
            try {
                if (f.getDefaultMonetaryContext().getPrecision() == 0) {
                    f.setNumber(TestUtils.createNumberWithPrecision(f, 5));
                } else {
                    f.setNumber(TestUtils.createNumberWithPrecision(f, f.getDefaultMonetaryContext().getPrecision()));
                }
                f.setCurrency("FooBar_foobar_fOobAr_foObaR");
                f.create();
                AssertJUnit.fail("Section 4.2.6: MonetaryAmountFactory must throw a MonetaryException, " +
                        "when a positive amount with an invalid " +
                        "currency" +
                        " is" +
                        " tried to be created, type: " +
                        type.getName());
            } catch (MonetaryException e) {
                // OK
            }
        }
    }

    /**
     * For each MonetaryAmount Factory: Bad Case: Create negative amounts from a factory with an invalid currency.
     */
    @Test(description =
            "4.2.6 Bad case: For each MonetaryAmount Factory: Create negative amounts with an invalid currency," +
                    " expecting MonetaryException thrown.")
    @SpecAssertion(section = "4.2.6", id = "426-C5")
    public void testMonetaryAmountFactoryCreatePositiveInvalidContext_BadCase() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory f = MonetaryAmounts.getAmountFactory(type);
            MonetaryContext mc = f.getMaximalMonetaryContext();
            try {
                MonetaryContextBuilder b = mc.toBuilder();
                boolean runTest = false; // only run check, if we are able to construct an exceeding MonetaryContext
                if (mc.getMaxScale() != -1) {
                    b.setMaxScale(mc.getMaxScale() + 10);
                    runTest = true;
                }
                if (mc.getPrecision() != -0) {
                    b.setPrecision(mc.getPrecision() + 10);
                    runTest = true;
                }
                if (runTest) {
                    f.setNumber(TestUtils.createNumberWithPrecision(f, f.getDefaultMonetaryContext().getPrecision()));
                    f.setCurrency("FooBar_foobar_fOobAr_foObaR");
                    f.create();
                    AssertJUnit.fail("Section 4.2.6: MonetaryAmountFactory must throw a MonetaryException, " +
                            "when a positive amount without an " +
                            "invalid MonetaryContext is" +
                            " tried to be created, type: " +
                            type.getName());
                }
            } catch (MonetaryException e) {
                // OK
            }
        }
    }


    // **************  D. Testing Creation of Amounts with negative values *******************

    /**
     * For each MonetaryAmount Factory: Create negative amounts from
     * a factory with currencies.
     */
    @Test(description = "4.2.6 For each MonetaryAmount Factory: Create negative amounts.")
    @SpecAssertion(section = "4.2.6", id = "426-D1")
    public void testMonetaryAmountFactoryNegativePositiveAmountsWitCurrencies() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            for (Currency cur : Currency.getAvailableCurrencies()) {
                CurrencyUnit cu = MonetaryCurrencies.getCurrency(cur.getCurrencyCode());
                MonetaryAmountFactory<?> f = MonetaryAmounts.getAmountFactory(type);
                f.setCurrency(cu);
                f.setNumber(-3);
                MonetaryAmount m = f.create();
                AssertJUnit.assertEquals(
                        "Section 4.2.6: Amount created with factory has invalid currency for " + type.getName(), cu,
                        m.getCurrency());
                AssertJUnit.assertEquals(
                        "Section 4.2.6: Amount created with factory returns invalid amount type " + type.getName(),
                        type, m.getClass());
                AssertJUnit.assertTrue(
                        "Section 4.2.6: Amount created with factory has invalid value for " + type.getName(),
                        m.isNegative());
                AssertJUnit.assertTrue(
                        "Section 4.2.6: Amount created with factory has invalid value for " + type.getName(),
                        m.signum() == -1);
                AssertJUnit.assertTrue(
                        "Section 4.2.6: Amount created with factory has invalid value for " + type.getName(),
                        m.getNumber().intValueExact() == -3);
            }
        }
    }

    /**
     * For each MonetaryAmount Factory: Create negative amounts from
     * a factory with monetary contexts.
     */
    @Test(description = "4.2.6 For each MonetaryAmount Factory: Create negative amounts, with explicit" +
            " MonetaryContext.")
    @SpecAssertion(section = "4.2.6", id = "426-D2")
    public void testMonetaryAmountFactoryNegativePositiveAmountsWithContexts() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<?> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("GBP");
            f.setNumber(1);
            MonetaryContext maxCtx = f.getMaximalMonetaryContext();
            if (maxCtx.getPrecision() != 0) {
                for (int p = maxCtx.getPrecision(); p > 0; p--) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setPrecision(p).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getPrecision() == 0 || m.getMonetaryContext().getPrecision() >= p);
                }
            } else {
                for (int p = 0; p < 100; p += 10) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setPrecision(p).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getPrecision() == 0 || m.getMonetaryContext().getPrecision() >= p);
                }
            }
            if (maxCtx.getMaxScale() != -1) {
                for (int s = maxCtx.getMaxScale(); s >= 0; s--) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setMaxScale(s).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the scale setTyped on the context for " + type.getName(),
                            m.getMonetaryContext().getMaxScale() == -1 || m.getMonetaryContext().getMaxScale() >= s);
                }
            } else {
                for (int s = 0; s < 100; s += 10) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setMaxScale(s).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getMaxScale() == -1 || m.getMonetaryContext().getMaxScale() >= s);
                }
            }
        }
    }

    /**
     * For each MonetaryAmount Factory: Create negative amounts from
     * a factory with monetary contexts.
     */
    @Test(description = "4.2.6 For each MonetaryAmount Factory: Create negative amounts, with explicit " +
            "MonetaryContext.")
    @SpecAssertion(section = "4.2.6", id = "426-D2")
    public void testMonetaryAmountFactoryNegativePositiveAmountsWithContexts2() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<?> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("GBP");
            f.setNumber(11.2);
            MonetaryContext maxCtx = f.getMaximalMonetaryContext();
            if (maxCtx.getPrecision() != 0) {
                for (int p = maxCtx.getPrecision(); p > 0; p--) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setPrecision(p).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getPrecision() == 0 || m.getMonetaryContext().getPrecision() >= p);
                }
            } else {
                for (int p = 0; p < 100; p += 10) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setPrecision(p).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getPrecision() == 0 || m.getMonetaryContext().getPrecision() >= p);
                }
            }
            if (maxCtx.getMaxScale() != -1) {
                for (int s = maxCtx.getMaxScale(); s >= 0; s--) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setMaxScale(s).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the scale setTyped on the context for " + type.getName(),
                            m.getMonetaryContext().getMaxScale() == -1 || m.getMonetaryContext().getMaxScale() >= s);
                }
            } else {
                for (int s = 0; s < 100; s += 10) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setMaxScale(s).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getMaxScale() == -1 || m.getMonetaryContext().getMaxScale() >= s);
                }
            }
        }
    }

    /**
     * For each MonetaryAmount Factory: Create negative amounts from
     * a factory with monetary contexts.
     */
    @Test(description = "4.2.6 For each MonetaryAmount Factory: Create negative amounts, with explicit " +
            "MonetaryContext.")
    @SpecAssertion(section = "4.2.6", id = "426-D2")
    public void testMonetaryAmountFactoryNegativePositiveAmountsWithContexts3() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<?> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("GBP");
            f.setNumber(BigDecimal.TEN);
            MonetaryContext maxCtx = f.getMaximalMonetaryContext();
            if (maxCtx.getPrecision() != 0) {
                for (int p = maxCtx.getPrecision(); p > 0; p--) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setPrecision(p).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getPrecision() == 0 || m.getMonetaryContext().getPrecision() >= p);
                }
            } else {
                for (int p = 0; p < 100; p += 10) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setPrecision(p).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getPrecision() == 0 || m.getMonetaryContext().getPrecision() >= p);
                }
            }
            if (maxCtx.getMaxScale() != -1) {
                for (int s = maxCtx.getMaxScale(); s >= 0; s--) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setMaxScale(s).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the scale setTyped on the context for " + type.getName(),
                            m.getMonetaryContext().getMaxScale() == -1 || m.getMonetaryContext().getMaxScale() >= s);
                }
            } else {
                for (int s = 0; s < 100; s += 10) {
                    f.setContext(MonetaryContextBuilder.of(MonetaryAmount.class).setMaxScale(s).build());
                    MonetaryAmount m = f.create();
                    AssertJUnit.assertTrue(
                            "Section 4.2.6: Factory did not honor the precision setTyped on the context for " +
                                    type.getName(),
                            m.getMonetaryContext().getMaxScale() == -1 || m.getMonetaryContext().getMaxScale() >= s);
                }
            }
        }
    }

    /**
     * For each MonetaryAmount Factory: Bad Case: Create negative
     * amounts from a factory with an invalid numeric value (exceeding max
     * MonetaryContext).
     */
    @Test(description = "4.2.6 Bad case: For each MonetaryAmount Factory: Create negative amounts, with invalid " +
            "numeric value, expect ArithmeticException.")
    @SpecAssertion(section = "4.2.6", id = "426-D3")
    public void testMonetaryAmountFactoryNegativePositiveAmountsWithInvalidNumber() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("INR");
            MonetaryContext ctx = f.getMaximalMonetaryContext();
            if (ctx.getPrecision() != 0) {
                try {
                    f.setNumber(TestUtils.createNumberWithPrecision(f, ctx.getPrecision() + 5).negate());
                    f.create();
                    AssertJUnit.fail("Section 4.2.6: MonetaryAmountFactory must throw an ArithmeticException, " +
                            "when an amount with exceeding precision is " +
                            "tried" +

                            " being created, type: " +
                            type.getName());
                } catch (ArithmeticException e) {
                    // OK
                }
            }
            if (ctx.getMaxScale() != -1) {
                try {
                    f.setNumber(TestUtils.createNumberWithScale(f, ctx.getMaxScale() + 5).negate());
                    f.create();
                    AssertJUnit.fail("Section 4.2.6: MonetaryAmountFactory must throw an ArithmeticException, " +
                            "when an amount with exceeding scale is tried" +
                            " being created, type: " +
                            type.getName());
                } catch (ArithmeticException e) {
                    // OK
                }
            }
        }
    }

    /**
     * For each MonetaryAmount Factory: Bad Case: Create negative amounts from a factory with an no currency.
     */
    @Test(description = "4.2.6 Bad case: For each MonetaryAmount Factory: Create negative amounts, with no " +
            "currency, expect MonetaryException.")
    @SpecAssertion(section = "4.2.6", id = "426-D4")
    public void testMonetaryAmountFactoryCreateNegativeNoCurrency_BadCase() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory f = MonetaryAmounts.getAmountFactory(type);
            try {
                if (f.getDefaultMonetaryContext().getPrecision() == 0) {
                    f.setNumber(TestUtils.createNumberWithPrecision(f, 5).negate());
                } else {
                    f.setNumber(TestUtils.createNumberWithPrecision(f, f.getDefaultMonetaryContext().getPrecision())
                            .negate());
                }
                f.create();
                AssertJUnit.fail("Section 4.2.6: MonetaryAmountFactory must throw a MonetaryException, " +
                        "when an amount without a currency is" +
                        " tried to be created, type: " +
                        type.getName());
            } catch (MonetaryException e) {
                // OK
            } catch (Exception e) {
                AssertJUnit.fail("Section 4.2.6: MonetaryAmountFactory must throw a MonetaryException, " +
                        "when an amount without a currency is" +
                        " tried to be created, but threw " + e.getClass() + " type: " +
                        type.getName());
            }
        }
    }

    /**
     * For each MonetaryAmount Factory: Bad Case: Create negative amounts from a factory with an invalid currency.
     */
    @Test(description = "4.2.6 Bad case: For each MonetaryAmount Factory: Create negative amounts, with invalid " +
            "currency, expect MonetaryException.")
    @SpecAssertion(section = "4.2.6", id = "426-D4")
    public void testMonetaryAmountFactoryCreateNegativeInvalidCurrency_BadCase() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory f = MonetaryAmounts.getAmountFactory(type);
            try {
                if (f.getDefaultMonetaryContext().getPrecision() == 0) {
                    f.setNumber(TestUtils.createNumberWithPrecision(f, 5).negate());
                } else {
                    f.setNumber(TestUtils.createNumberWithPrecision(f, f.getDefaultMonetaryContext().getPrecision())
                            .negate());
                }
                f.setCurrency("Section 4.2.6: FooBar_foobar_fOobAr_foObaR");
                f.create();
                AssertJUnit.fail("Section 4.2.6: MonetaryAmountFactory must throw a MonetaryException, " +
                        "when an amount with an invalid " +
                        "currency is tried to be created, type: " +
                        type.getName());
            } catch (MonetaryException e) {
                // OK
            }
        }
    }

    /**
     * For each MonetaryAmount Factory: Bad Case: Create negative amounts from a factory with an invalid currency.
     */
    @Test(description = "4.2.6 Bad case: For each MonetaryAmount Factory: Create negative amounts, with no " +
            "currency, expect MonetaryException.")
    @SpecAssertion(section = "4.2.6", id = "426-D5")
    public void testMonetaryAmountFactoryCreateNegativeInvalidContext_BadCase() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory f = MonetaryAmounts.getAmountFactory(type);
            MonetaryContext mc = f.getMaximalMonetaryContext();
            try {
                MonetaryContextBuilder b = mc.toBuilder();
                boolean runTest = false; // only run check, if we are able to construct an exceeding MonetaryContext
                if (mc.getMaxScale() != -1) {
                    b.setMaxScale(mc.getMaxScale() + 10);
                    runTest = true;
                }
                if (mc.getPrecision() != -0) {
                    b.setPrecision(mc.getPrecision() + 10);
                    runTest = true;
                }
                if (runTest) {
                    if (f.getDefaultMonetaryContext().getPrecision() == 0) {
                        f.setNumber(TestUtils.createNumberWithPrecision(f, 5).negate());
                    } else {
                        f.setNumber(TestUtils.createNumberWithPrecision(f, f.getDefaultMonetaryContext().getPrecision())
                                .negate());
                    }
                    f.setCurrency("FooBar_foobar_fOobAr_foObaR");
                    f.create();
                    AssertJUnit.fail("Section 4.2.6: MonetaryAmountFactory must throw a MonetaryException, " +
                            "when an amount with an invalid " +
                            "MonetaryContext is tried to be created, type: " +
                            type.getName());
                }
            } catch (MonetaryException e) {
                // OK
            }
        }
    }

}
