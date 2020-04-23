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
import org.javamoney.tck.TestUtils;
import org.javamoney.tck.tests.internal.TestAmount;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.mutabilitydetector.unittesting.MutabilityAssertionError;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import javax.money.MonetaryContext;
import javax.money.MonetaryException;
import javax.money.MonetaryOperator;
import javax.money.MonetaryQuery;
import javax.money.NumberValue;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;


@SpecVersion(spec = "JSR 354", version = "1.1.0")
public class ModellingMonetaryAmountsTest {

    private final static String DEFAULT_CURRENCY = "CHF";

    private final static String ADDITIONAL_CURRENCY = "USD";

    /**
     * Ensure at least one MonetaryAmount implementation is registered,
     * by calling Monetary.getAmountTypes();
     */
    @SpecAssertion(section = "4.2.2", id = "422-0")
    @Test(description = "4.2.2 Ensure Monetary.getAmountTypes() is not null and not empty.")
    public void testEnsureMonetaryAmount() {
        AssertJUnit.assertNotNull("Section 4.2.2: Monetary.getAmountTypes() must never return null.",
                Monetary.getAmountTypes());
        AssertJUnit.assertTrue(
                "Section 4.2.2: At least one type must be registered with Monetary (see getAmountTypes()).",
                !Monetary.getAmountTypes().isEmpty());
    }

    /**
     * For each MonetaryAmount implementation: Ensure getCurrencyCode
     * returns correct results.
     */
    @SpecAssertion(section = "4.2.2", id = "422-A1")
    @Test(description = "4.2.2 Ensure amount can be created with all default currencies.")
    public void testCurrencyCode() {
        for (Class type : Monetary.getAmountTypes()) {
            for (Currency jdkCur : Currency.getAvailableCurrencies()) {
                MonetaryAmount amount =
                        Monetary.getDefaultAmountFactory().setCurrency(jdkCur.getCurrencyCode()).setNumber(10.15)
                                .create();
                AssertJUnit.assertNotNull(
                        "Section 4.2.2: Amount factory returned null for new amount type: " + type.getName(), amount);
                AssertJUnit.assertNotNull(
                        "Section 4.2.2: Amount factory returned new amount with null currency, type: " + type.getName(),
                        amount.getCurrency());
                AssertJUnit.assertEquals(
                        "Section 4.2.2: Amount factory returned new amount with invalid currency, type: " +
                                type.getName(), jdkCur.getCurrencyCode(), amount.getCurrency().getCurrencyCode());
            }
        }
    }

    /**
     * For each MonetaryAmount implementation: Ensure getNumber()
     * returns correct results.
     */
    @SpecAssertion(section = "4.2.2", id = "422-A2")
    @Test(description = "4.2.2 Ensure amounts created return correct getNumber().")
    public void testGetNumber() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount[] moneys = new MonetaryAmount[]{f.setNumber(100).create(),
                    f.setNumber(new BigDecimal("23123213.435")).create(),
                    f.setNumber(new BigDecimal("-23123213.435")).create(), f.setNumber(-23123213).create(),
                    f.setNumber(0).create()};
            BigDecimal[] numbers = new BigDecimal[]{new BigDecimal("100"), new BigDecimal("23123213.435"),
                    new BigDecimal("-23123213.435"), new BigDecimal("-23123213"), BigDecimal.ZERO};
            int[] intNums = new int[]{100, 23123213, -23123213, -23123213, 0};
            long[] longNums = new long[]{100, 23123213, -23123213, -23123213, 0};
            double[] doubleNums = new double[]{100, 23123213.435, -23123213.435, -23123213, 0};
            float[] floatNums = new float[]{100f, 23123213.435f, -23123213.435f, -23123213, 0f};

            for (int i = 0; i < moneys.length; i++) {
                NumberValue nv = moneys[i].getNumber();
                AssertJUnit.assertNotNull("Section 4.2.2: Amount returned returns null for getNumber(), type: " +
                        moneys[i].getClass().getName(), nv);
                AssertJUnit.assertEquals(
                        "Section 4.2.2: getNumber().numberValue(BigDecimal.class) incorrect for " + type.getName(),
                        numbers[i].stripTrailingZeros(), nv.numberValue(BigDecimal.class).stripTrailingZeros());
                AssertJUnit.assertEquals("Section 4.2.2: getNumber().intValue() incorrect for " + type.getName(),
                        intNums[i], nv.intValue());
                AssertJUnit.assertEquals("Section 4.2.2: getNumber().longValue() incorrect for " + type.getName(),
                        longNums[i], nv.longValue());
                AssertJUnit.assertEquals("Section 4.2.2: getNumber().doubleValue() incorrect for " + type.getName(),
                        doubleNums[i], nv.doubleValue(), 0.0d);
                AssertJUnit.assertEquals("Section 4.2.2: getNumber().floatValue() incorrect for " + type.getName(),
                        floatNums[i], nv.floatValue(), 0.0d);
            }
        }
    }

    /**
     * For each MonetaryAmount implementation: Ensure
     * getContext() returns correct results.
     */
    @SpecAssertion(section = "4.2.2", id = "422-A3")
    @Test(description = "4.2.2 Ensure amounts created return correct getContext().")
    public void testGetMonetaryContext() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryContext defCtx = f.getDefaultMonetaryContext();
            MonetaryContext maxCtx = f.getMaximalMonetaryContext();
            MonetaryContext mc = f.setNumber(1).create().getContext();
            AssertJUnit.assertEquals("Section 4.2.2: Invalid MonetaryContext(amountType) for " + type.getName(),
                    mc.getAmountType(), type);
            if (maxCtx.getPrecision() > 0) {
                AssertJUnit.assertTrue("Section 4.2.2: Invalid MonetaryContext(precision) for " + type.getName(),
                        mc.getPrecision() <= maxCtx.getPrecision());
            }
            if (maxCtx.getMaxScale() > 0) {
                AssertJUnit.assertTrue("Section 4.2.2: Invalid MonetaryContext(maxScale) for " + type.getName(),
                        mc.getMaxScale() <= maxCtx.getMaxScale());
            }
            AssertJUnit.assertEquals("Section 4.2.2: Invalid MonetaryContext(amountType) for " + type.getName(),
                    f.setNumber(0.34746d).create().getContext().getAmountType(), type);
            mc = f.setNumber(0).create().getContext();
            AssertJUnit.assertEquals("Section 4.2.2: Invalid MonetaryContext(amountType) for " + type.getName(),
                    mc.getAmountType(), type);
            if (maxCtx.getPrecision() > 0) {
                AssertJUnit.assertTrue("Section 4.2.2: Invalid MonetaryContext(precision) for " + type.getName(),
                        mc.getPrecision() <= maxCtx.getPrecision());
            }
            if (maxCtx.getMaxScale() > 0) {
                AssertJUnit.assertTrue("Section 4.2.2: Invalid MonetaryContext(maxScale) for " + type.getName(),
                        mc.getMaxScale() <= maxCtx.getMaxScale());
            }
            AssertJUnit.assertEquals("Section 4.2.2: Invalid MonetaryContext(amountType) for " + type.getName(),
                    f.setNumber(100034L).create().getContext().getAmountType(), type);
            mc = f.setNumber(0).create().getContext();
            AssertJUnit.assertEquals("Section 4.2.2: Invalid MonetaryContext(amountType) for " + type.getName(),
                    mc.getAmountType(), type);
            if (maxCtx.getPrecision() > 0) {
                AssertJUnit.assertTrue("Section 4.2.2: Invalid MonetaryContext(precision) for " + type.getName(),
                        mc.getPrecision() <= maxCtx.getPrecision());
            }
            if (maxCtx.getMaxScale() > 0) {
                AssertJUnit.assertTrue("Section 4.2.2: Invalid MonetaryContext(maxScale) for " + type.getName(),
                        mc.getMaxScale() <= maxCtx.getMaxScale());
            }
        }
    }

    /**
     * For each MonetaryAmount implementation: Ensure isNegative()
     * returns correct results.
     */
    @SpecAssertion(section = "4.2.2", id = "422-A4")
    @Test(description = "4.2.2 For each amount class, test isNegative().")
    public void testIsNegative() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount[] moneys = new MonetaryAmount[]{f.setNumber(0).create(), f.setNumber(0.0).create(),
                    f.setNumber(BigDecimal.ZERO).create(), f.setNumber(new BigDecimal("0.00000000000000000")).create(),
                    f.setNumber(100).create(), f.setNumber(34242344).create(), f.setNumber(23123213.435).create()};
            for (MonetaryAmount m : moneys) {
                AssertJUnit.assertFalse("Section 4.2.2: Invalid isNegative (expected false) for " + type.getName(),
                        m.isNegative());
            }
            moneys = new MonetaryAmount[]{f.setNumber(-100).create(), f.setNumber(-34242344).create(),
                    f.setNumber(-23123213.435).create()};
            for (MonetaryAmount m : moneys) {
                AssertJUnit.assertTrue("Section 4.2.2: Invalid isNegative (expected true) for " + type.getName(),
                        m.isNegative());
            }
        }
    }

    /**
     * For each MonetaryAmount implementation: Ensure isPositive()
     * returns correct results.
     */
    @SpecAssertion(section = "4.2.2", id = "422-A5")
    @Test(description = "4.2.2 For each amount class, test isPositive().")
    public void testIsPositive() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount[] moneys = new MonetaryAmount[]{f.setNumber(100).create(), f.setNumber(34242344).create(),
                    f.setNumber(23123213.435).create()};
            for (MonetaryAmount m : moneys) {
                AssertJUnit.assertTrue("Section 4.2.2: Invalid isPositive (expected true) for " + type.getName(),
                        m.isPositive());
            }
            moneys = new MonetaryAmount[]{f.setNumber(0).create(), f.setNumber(0.0).create(),
                    f.setNumber(BigDecimal.ZERO).create(), f.setNumber(new BigDecimal("0.00000000000000000")).create(),
                    f.setNumber(-100).create(), f.setNumber(-34242344).create(), f.setNumber(-23123213.435).create()};
            for (MonetaryAmount m : moneys) {
                AssertJUnit.assertFalse("Section 4.2.2: Invalid isPositive (expected false) for " + type.getName(),
                        m.isPositive());
            }
        }
    }


    /**
     * For each MonetaryAmount implementation: Ensure isZero()
     * returns correct results.
     */
    @SpecAssertion(section = "4.2.2", id = "422-A6")
    @Test(description = "4.2.2 For each amount class, test isZero().")
    public void testIsZero() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount[] moneys = new MonetaryAmount[]{f.setNumber(100).create(), f.setNumber(34242344).create(),
                    f.setNumber(23123213.435).create(), f.setNumber(-100).create(),
                    f.setNumber(-723527.36532).create()};
            for (MonetaryAmount m : moneys) {
                AssertJUnit.assertFalse("Section 4.2.2: Invalid isZero (expected false) for " + type.getName(),
                        m.isZero());
            }
            moneys = new MonetaryAmount[]{f.setNumber(0).create(), f.setNumber(0.0).create(),
                    f.setNumber(BigDecimal.ZERO).create(), f.setNumber(new BigDecimal("0.00000000000000000")).create()};
            for (MonetaryAmount m : moneys) {
                AssertJUnit
                        .assertTrue("Section 4.2.2: Invalid isZero (expected true) for " + type.getName(), m.isZero());
            }
        }
    }

    /**
     * For each MonetaryAmount implementation: Ensure isZero()
     * returns correct results (-0, +0 == 0).
     */
    @SpecAssertion(section = "4.2.2", id = "422-A6")
    @Test(description = "4.2.2 For each amount class, test isZero(), advanced.")
    public void testIsZeroAdvanced() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount[] moneys =
                    new MonetaryAmount[]{f.setNumber(-0).create(), f.setNumber(0).create(), f.setNumber(-0.0f).create(),
                            f.setNumber(0.0f).create(), f.setNumber(-0.0d).create(), f.setNumber(0.0d).create()};
            for (MonetaryAmount m : moneys) {
                AssertJUnit
                        .assertTrue("Section 4.2.2: Invalid isZero (expected true) for " + type.getName(), m.isZero());
            }
        }
    }

    /**
     * For each MonetaryAmount implementation: signum() function is
     * implemented correctly.
     */
    @SpecAssertion(section = "4.2.2", id = "422-A7")
    @Test(description = "4.2.2 For each amount class, test signum().")
    public void testSignum() {
        for (Class type : Monetary.getAmountTypes()) {
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount m = f.setNumber(100).create();
            AssertJUnit.assertEquals("Section 4.2.2: Invalid signum of 100 for " + type.getName(), 1, m.signum());
            m = f.setNumber(-100).create();
            AssertJUnit.assertEquals("Section 4.2.2: signum of -100 for " + type.getName(), -1, m.signum());
            m = f.setNumber(100.3435).create();
            AssertJUnit.assertEquals("Section 4.2.2: signum of 100.3435 for " + type.getName(), 1, m.signum());
            m = f.setNumber(-100.3435).create();
            AssertJUnit.assertEquals("Section 4.2.2: signum of -100.3435 for " + type.getName(), -1, m.signum());
            m = f.setNumber(0).create();
            AssertJUnit.assertEquals("Section 4.2.2: signum of 0 for " + type.getName(), 0, m.signum());
            m = f.setNumber(-0).create();
            AssertJUnit.assertEquals("Section 4.2.2: signum of - for " + type.getName(), 0, m.signum());
        }
    }

    /**
     * For each MonetaryAmount implementation: Ensure isNegativeOrZero()
     * returns correct results.
     */
    @SpecAssertion(section = "4.2.2", id = "422-A8")
    @Test(description = "4.2.2 For each amount class, test isNegativeOrZero().")
    public void testIsNegativeOrZero() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount[] moneys = new MonetaryAmount[]{f.setNumber(100).create(), f.setNumber(34242344).create(),
                    f.setNumber(23123213.435).create()};
            for (MonetaryAmount m : moneys) {
                AssertJUnit.assertFalse("Section 4.2.2: Invalid negativeOrZero (expected false) for " + type.getName(),
                        m.isNegativeOrZero());
            }
            moneys = new MonetaryAmount[]{f.setNumber(0).create(), f.setNumber(0.0).create(),
                    f.setNumber(BigDecimal.ZERO).create(), f.setNumber(new BigDecimal("0.0000")).create(),
                    f.setNumber(-100).create(), f.setNumber(-34242344).create(), f.setNumber(-23123213.435).create()};
            for (MonetaryAmount m : moneys) {
                AssertJUnit.assertTrue("Section 4.2.2: Invalid negativeOrZero (expected true) for " + type.getName(),
                        m.isNegativeOrZero());
            }
        }
    }

    /**
     * For each MonetaryAmount implementation: Ensure isPositiveOrZero()
     * returns correct results.
     */
    @SpecAssertion(section = "4.2.2", id = "422-A9")
    @Test(description = "4.2.2 For each amount class, test isPositiveOrZero().")
    public void testIsPositiveOrZero() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount[] moneys = new MonetaryAmount[]{f.setNumber(0).create(), f.setNumber(0.0).create(),
                    f.setNumber(BigDecimal.ZERO).create(), f.setNumber(new BigDecimal("0.00000000000000000")).create(),
                    f.setNumber(100).create(), f.setNumber(34242344).create(), f.setNumber(23123213.435).create()};
            for (MonetaryAmount m : moneys) {
                AssertJUnit.assertTrue("Section 4.2.2: Invalid positiveOrZero (expected true): for " + type.getName(),
                        m.isPositiveOrZero());
            }
            moneys = new MonetaryAmount[]{f.setNumber(-100).create(), f.setNumber(-34242344).create(),
                    f.setNumber(-23123213.435).create()};
            for (MonetaryAmount m : moneys) {
                AssertJUnit
                        .assertFalse("Section 4.2.2: Invalid positiveOrZero (expected false) for " + type.getName() + m,
                                m.isPositiveOrZero());
            }
        }
    }


    // ********************* B. Prototyping Support *****************************

    /**
     * Ensure getFactory returns a MonetaryAmountFactory and that
     * instances created are of the same type.
     */
    @SpecAssertion(section = "4.2.2", id = "422-B1")
    @Test(description = "4.2.2 For each amount class, access factory and of amounts.")
    public void testMonetaryAmountFactories() {
        for (Class type : Monetary.getAmountTypes()) {
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            AssertJUnit.assertNotNull(f);
            MonetaryAmount m = f.setCurrency("CHF").setNumber(10).create();
            AssertJUnit.assertEquals("Section 4.2.2: Invalid class for created amount, expected: " + type.getName(),
                    m.getClass(), type);
            m = f.setCurrency("CHF").setNumber(-10).create();
            AssertJUnit.assertEquals("Section 4.2.2: Invalid class for created amount, expected: " + type.getName(),
                    m.getClass(), type);
            m = f.setCurrency("CHF").setNumber(10.3).create();
            AssertJUnit.assertEquals("Section 4.2.2: Invalid class for created amount, expected: " + type.getName(),
                    m.getClass(), type);
            m = f.setCurrency("CHF").setNumber(-10.3).create();
            AssertJUnit.assertEquals("Section 4.2.2: Invalid class for created amount, expected: " + type.getName(),
                    m.getClass(), type);
            m = f.setCurrency("CHF").setNumber(0.0).create();
            AssertJUnit.assertEquals("Section 4.2.2: Invalid class for created amount, expected: " + type.getName(),
                    m.getClass(), type);
            m = f.setCurrency("CHF").setNumber(-0.0).create();
            AssertJUnit.assertEquals("Section 4.2.2: Invalid class for created amount, expected: " + type.getName(),
                    m.getClass(), type);
        }
    }

    /**
     * Call getFactory(), of a new MonetaryAmount instance, with
     * same input. The instances must
     * be equal (or even be identical!).
     */
    @SpecAssertion(section = "4.2.2", id = "422-B2")
    @Test(description = "4.2.2 For each amount class, access factory and of amounts, ensure amounts are equal if they" +
            "should.")
    public void testMonetaryAmountFactories_InstancesMustBeEqual() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(10).create();
            f = Monetary.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("CHF").setNumber(10).create();
            AssertJUnit.assertEquals("Section 4.2.2: Expected equal instances for " + type.getName(), m1, m2);
        }

        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(10.5d).create();
            f = Monetary.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("CHF").setNumber(10.5d).create();
            AssertJUnit
                    .assertEquals("Section 4.2.2: Expected equal types created by same factory for " + type.getName(),
                            m1, m2);
        }

        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(new BigDecimal("10.52")).create();
            f = Monetary.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("CHF").setNumber(new BigDecimal("10.52")).create();
            AssertJUnit
                    .assertEquals("Section 4.2.2: Expected equal types created by same factory for " + type.getName(),
                            m1, m2);
        }
    }

    /**
     * Call getFactory(), of a new MonetaryAmount instance with a
     * new number
     * value. The instances must
     * be non equal and have the
     * according
     * numeric value.
     */
    @SpecAssertion(section = "4.2.2", id = "422-B3")
    @Test(description = "4.2.2 For each amount class, check new amounts are not equal.")
    public void testMonetaryAmountFactories_InstantesMustBeNotEqual() {
        for (Class type : Monetary.getAmountTypes()) {
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(10).create();
            f = Monetary.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("CHF").setNumber(11).create();
            AssertJUnit.assertNotSame("Section 4.2.2: Expected non equal instances for " + type.getName(), m1, m2);
        }

        for (Class type : Monetary.getAmountTypes()) {
            MonetaryAmountFactory f = Monetary.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(10.5d).create();
            f = Monetary.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("CHF").setNumber(10.4d).create();
            AssertJUnit.assertNotSame("Section 4.2.2: Expected non equal instances for " + type.getName(), m1, m2);
        }

        for (Class type : Monetary.getAmountTypes()) {
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(new BigDecimal("10.52")).create();
            f = Monetary.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("CHF").setNumber(new BigDecimal("10.11")).create();
            AssertJUnit.assertNotSame("Section 4.2.2: Expected non equal instances for " + type.getName(), m1, m2);
        }
    }

    /**
     * Call getFactory(),of a new MonetaryAmount instance
     * with a new currency value.The instances must
     * be non  equal and have the according currency value .Do this by passing a literal code
     * and by passing a CurrencyUnit.
     */
    @SpecAssertion(section = "4.2.2", id = "422-B4")
    @Test(description = "4.2.2 For each amount class, check multiple instances are not equal.")
    public void testMonetaryAmountFactories_CreateWithCurrencies() {
        for (Class type : Monetary.getAmountTypes()) {
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(10).create();
            f = Monetary.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("USD").setNumber(10).create();
            AssertJUnit.assertNotSame("Section 4.2.2: Expected non equal instances for " + type.getName(), m1, m2);
        }

        for (Class type : Monetary.getAmountTypes()) {
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(10.5d).create();
            f = Monetary.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("USD").setNumber(10.5d).create();
            AssertJUnit.assertNotSame("Section 4.2.2: Expected non equal instances for " + type.getName(), m1, m2);
        }

        for (Class type : Monetary.getAmountTypes()) {
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(new BigDecimal("10.52")).create();
            f = Monetary.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("USD").setNumber(new BigDecimal("10.52")).create();
            AssertJUnit.assertNotSame("Section 4.2.2: Expected non equal instances for " + type.getName(), m1, m2);
        }
    }

    /**
     * Call getFactory(),of a  new MonetaryAmount instance
     * with a new  monetary context(if possible-check the max context). The
     * instances must be non equal and have the same currency and number value.
     */
    @SpecAssertion(section = "4.2.2", id = "422-B5")
    @Test(description = "4.2.2 For each amount class, check new amounts with explcit MonetaryContext.")
    public void testMonetaryAmountFactories_CreateWithMonetaryContext() {
        for (Class type : Monetary.getAmountTypes()) {
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            MonetaryContext mc1 = f.getDefaultMonetaryContext();
            MonetaryContext mc2 = f.getMaximalMonetaryContext();
            MonetaryAmount m1;
            MonetaryAmount m2;
            if (mc1.equals(mc2)) {
                // In this cases both amount must be equals
                m1 = f.setCurrency("CHF").setContext(mc1).setNumber(10).create();
                m2 = f.setCurrency("CHF").setContext(mc2).setNumber(10).create();
                AssertJUnit.assertNotSame("Section 4.2.2: Expected non equal instances for " + type.getName(), m1, m2);
            } else {
                // In this cases both amount must be non equals
                m1 = f.setCurrency("CHF").setContext(mc1).setNumber(10).create();
                m2 = f.setCurrency("CHF").setContext(mc2).setNumber(10).create();
                AssertJUnit.assertNotSame("Section 4.2.2: Expected non equal instances for " + type.getName(), m1, m2);
            }
            AssertJUnit.assertTrue("Section 4.2.2: Expected equality for " + type.getName(), m1.equals(m1));
            AssertJUnit.assertTrue("Section 4.2.2: Expected equality for " + type.getName(), m2.equals(m2));
        }
    }

    /**
     * Call getFactory(),of a new MonetaryAmount instance with a new monetary context, a
     * new number and a new currency. The instances must be non equal.
     */
    @SpecAssertion(section = "4.2.2", id = "422-B6")
    @Test(description = "4.2.2 For each amount class, check new amounts are not equal for different currencies and " +
            "contexts.")
    public void testMonetaryAmountFactories_CreateWithMonetaryContextNumberAndCurrency() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            MonetaryContext mc1 = f.getDefaultMonetaryContext();
            MonetaryContext mc2 = f.getMaximalMonetaryContext();
            MonetaryAmount m1 = f.setCurrency("CHF").setContext(mc1).setNumber(10).create();
            MonetaryAmount m2 = f.setCurrency("USD").setContext(mc2).setNumber(11).create();
            AssertJUnit.assertNotSame("Section 4.2.2: Expected not same for " + type.getName(), m1, m2);
            AssertJUnit.assertTrue("Section 4.2.2: Expected isEqualTo==true for " + type.getName(), m1.isEqualTo(m1));
            AssertJUnit.assertTrue("Section 4.2.2: Expected isEqualTo==true for " + type.getName(), m2.isEqualTo(m2));
            AssertJUnit.assertTrue("Section 4.2.2: Expected equality for " + type.getName(), m1.equals(m1));
            AssertJUnit.assertTrue("Section 4.2.2: Expected equality for " + type.getName(), m2.equals(m2));
        }
    }

    // ***************************** C.Comparison Methods *********************************

    /**
     * Test isGreaterThan() is implemented correctly for each amount type regardless of trailing zeroes.
     */
    @SpecAssertion(section = "4.2.2", id = "422-C1")
    @Test(description = "4.2.2 For each amount class, check isGreaterThan().")
    public void testMonetaryAmount_isGreaterThan() {
        for (Class type : Monetary.getAmountTypes()) {
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            f.setCurrency("CHF");
            AssertJUnit.assertFalse("Section 4.2.2: isGreaterThan failed for " + type.getName(),
                    f.setNumber(BigDecimal.valueOf(0d)).create()
                            .isGreaterThan(f.setNumber(BigDecimal.valueOf(0)).create()));
            AssertJUnit.assertTrue("Section 4.2.2: isGreaterThan failed for " + type.getName(),
                    f.setNumber(BigDecimal.valueOf(0.00001d)).create()
                            .isGreaterThan(f.setNumber(BigDecimal.valueOf(0d)).create()));
            AssertJUnit.assertTrue("Section 4.2.2: isGreaterThan failed for " + type.getName(),
                    f.setNumber(15).create().isGreaterThan(f.setNumber(10).create()));
            AssertJUnit.assertTrue("Section 4.2.2: isGreaterThan failed for " + type.getName(),
                    f.setNumber(15.546).create().isGreaterThan(f.setNumber(10.34).create()));
            AssertJUnit.assertFalse("Section 4.2.2: isGreaterThan failed for " + type.getName(),
                    f.setNumber(5).create().isGreaterThan(f.setNumber(10).create()));
            AssertJUnit.assertFalse("Section 4.2.2: isGreaterThan failed for " + type.getName(),
                    f.setNumber(5.546).create().isGreaterThan(f.setNumber(10.34).create()));
        }
    }

    /**
     * Test isGreaterThanOrEquals() is implemented correctly for each amount type regardless of trailing
     * zeroes.
     */
    @SpecAssertion(section = "4.2.2", id = "422-C2")
    @Test(description = "4.2.2 For each amount class, check isGreaterThanOrEquals().")
    public void testMonetaryAmount_isGreaterThanOrEquals() {
        for (Class type : Monetary.getAmountTypes()) {
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            f.setCurrency("CHF");
            AssertJUnit.assertTrue("Section 4.2.2: isGreaterThanOrEqualTo failed for " + type.getName(),
                    f.setNumber(BigDecimal.valueOf(0d)).create()
                            .isGreaterThanOrEqualTo(f.setNumber(BigDecimal.valueOf(0)).create()));
            AssertJUnit.assertTrue("Section 4.2.2: isGreaterThanOrEqualTo failed for " + type.getName(),
                    f.setNumber(BigDecimal.valueOf(0.00001d)).create()
                            .isGreaterThanOrEqualTo(f.setNumber(BigDecimal.valueOf(0d)).create()));
            AssertJUnit.assertTrue("Section 4.2.2: isGreaterThanOrEqualTo failed for " + type.getName(),
                    f.setNumber(15).create().isGreaterThanOrEqualTo(f.setNumber(10).create()));
            AssertJUnit.assertTrue("Section 4.2.2: isGreaterThanOrEqualTo failed for " + type.getName(),
                    f.setNumber(15.546).create().isGreaterThanOrEqualTo(f.setNumber(10.34).create()));
            AssertJUnit.assertFalse("Section 4.2.2: isGreaterThanOrEqualTo failed for " + type.getName(),
                    f.setNumber(5).create().isGreaterThanOrEqualTo(f.setNumber(10).create()));
            AssertJUnit.assertFalse("Section 4.2.2: isGreaterThanOrEqualTo failed for " + type.getName(),
                    f.setNumber(5.546).create().isGreaterThanOrEqualTo(f.setNumber(10.34).create()));
        }
    }

    /**
     * Test isLessThan() is implemented correctly for each amount type regardless of trailing
     * zeroes.
     */
    @SpecAssertion(section = "4.2.2", id = "422-C3")
    @Test(description = "4.2.2 For each amount class, check isLessThan().")
    public void testMonetaryAmount_isLessThan() {
        for (Class type : Monetary.getAmountTypes()) {
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            f.setCurrency("CHF");
            AssertJUnit.assertFalse("Section 4.2.2: isLessThan failed for " + type.getName(),
                    f.setNumber(BigDecimal.valueOf(0d)).create()
                            .isLessThan(f.setNumber(BigDecimal.valueOf(0)).create()));
            AssertJUnit.assertFalse("Section 4.2.2: isLessThan failed for " + type.getName(),
                    f.setNumber(BigDecimal.valueOf(0.00001d)).create()
                            .isLessThan(f.setNumber(BigDecimal.valueOf(0d)).create()));
            AssertJUnit.assertFalse("Section 4.2.2: isLessThan failed for " + type.getName(),
                    f.setNumber(15).create().isLessThan(f.setNumber(10).create()));
            AssertJUnit.assertFalse("Section 4.2.2: isLessThan failed for " + type.getName(),
                    f.setNumber(15.546).create().isLessThan(f.setNumber(10.34).create()));
            AssertJUnit.assertTrue("Section 4.2.2: isLessThan failed for " + type.getName(),
                    f.setNumber(5).create().isLessThan(f.setNumber(10).create()));
            AssertJUnit.assertTrue("Section 4.2.2: isLessThan failed for " + type.getName(),
                    f.setNumber(5.546).create().isLessThan(f.setNumber(10.34).create()));
        }
    }

    /**
     * Test isLessThanOrEquals() is implemented correctly for each amount type regardless of trailing
     * zeroes.
     */
    @SpecAssertion(section = "4.2.2", id = "422-C4")
    @Test(description = "4.2.2 For each amount class, check isLessThanOrEqualTo().")
    public void testMonetaryAmount_isLessThanOrEqualTo() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            f.setCurrency("CHF");
            AssertJUnit.assertTrue("Section 4.2.2: isLessThanOrEqualTo failed for " + type.getName(),
                    f.setNumber(BigDecimal.valueOf(0d)).create()
                            .isLessThanOrEqualTo(f.setNumber(BigDecimal.valueOf(0)).create()));
            AssertJUnit.assertFalse("Section 4.2.2: isLessThanOrEqualTo failed for " + type.getName(),
                    f.setNumber(BigDecimal.valueOf(0.00001d)).create()
                            .isLessThanOrEqualTo(f.setNumber(BigDecimal.valueOf(0d)).create()));
            AssertJUnit.assertFalse("Section 4.2.2: isLessThanOrEqualTo failed for " + type.getName(),
                    f.setNumber(15).create().isLessThanOrEqualTo(f.setNumber(10).create()));
            AssertJUnit.assertFalse("Section 4.2.2: isLessThan failed for " + type.getName(),
                    f.setNumber(15.546).create().isLessThan(f.setNumber(10.34).create()));
            AssertJUnit.assertTrue("Section 4.2.2: isLessThanOrEqualTo failed for " + type.getName(),
                    f.setNumber(5).create().isLessThanOrEqualTo(f.setNumber(10).create()));
            AssertJUnit.assertTrue("Section 4.2.2: isLessThanOrEqualTo failed for " + type.getName(),
                    f.setNumber(5.546).create().isLessThanOrEqualTo(f.setNumber(10.34).create()));
        }
    }

    /**
     * Test isEqualTo() is implemented correctly for each amount type regardless of trailing
     * zeroes.
     */
    @SpecAssertion(section = "4.2.2", id = "422-C5")
    @Test(description = "4.2.2 For each amount class, check isEqualTo().")
    public void testMonetaryAmount_isEqualTo() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            f.setCurrency("CHF");
            AssertJUnit.assertTrue("Section 4.2.2: isEqualTo failed for " + type.getName(),
                    f.setNumber(BigDecimal.valueOf(0d)).create()
                            .isEqualTo(f.setNumber(BigDecimal.valueOf(0)).create()));
            AssertJUnit.assertFalse("Section 4.2.2: isEqualTo failed for " + type.getName(),
                    f.setNumber(BigDecimal.valueOf(0.00001d)).create()
                            .isEqualTo(f.setNumber(BigDecimal.valueOf(0d)).create()));
            AssertJUnit.assertTrue("isEqualTo failed for " + type.getName(),
                    f.setNumber(BigDecimal.valueOf(5d)).create()
                            .isEqualTo(f.setNumber(BigDecimal.valueOf(5)).create()));
            AssertJUnit.assertTrue("Section 4.2.2: isEqualTo failed for " + type.getName(),
                    f.setNumber(BigDecimal.valueOf(1d)).create()
                            .isEqualTo(f.setNumber(BigDecimal.valueOf(1.00)).create()));
            AssertJUnit.assertTrue("Section 4.2.2: isEqualTo failed for " + type.getName(),
                    f.setNumber(BigDecimal.valueOf(1d)).create()
                            .isEqualTo(f.setNumber(BigDecimal.ONE).create()));
            AssertJUnit.assertTrue("Section 4.2.2: isEqualTo failed for " + type.getName(),
                    f.setNumber(BigDecimal.valueOf(1)).create()
                            .isEqualTo(f.setNumber(BigDecimal.ONE).create()));
            AssertJUnit.assertTrue("Section 4.2.2: isEqualTo failed for " + type.getName(),
                    f.setNumber(new BigDecimal("1.0000")).create()
                            .isEqualTo(f.setNumber(new BigDecimal("1.00")).create()));
            // Test with different scales, but numeric equal values
            AssertJUnit.assertTrue("Section 4.2.2: isEqualTo failed for " + type.getName(),
                    f.setNumber(BigDecimal.valueOf(0d)).create()
                            .isEqualTo(f.setNumber(BigDecimal.valueOf(0)).create()));
            AssertJUnit.assertTrue("Section 4.2.2: isEqualTo failed for " + type.getName(),
                    f.setNumber(BigDecimal.ZERO).create()
                            .isEqualTo(f.setNumber(BigDecimal.valueOf(0)).create()));
            AssertJUnit.assertTrue("Section 4.2.2: isEqualTo failed for " + type.getName(),
                    f.setNumber(BigDecimal.valueOf(5)).create()
                            .isEqualTo(f.setNumber(new BigDecimal("5.0")).create()));
            AssertJUnit.assertTrue("Section 4.2.2: isEqualTo failed for " + type.getName(),
                    f.setNumber(BigDecimal.valueOf(5)).create()
                            .isEqualTo(f.setNumber(new BigDecimal("5.00")).create()));
            AssertJUnit.assertTrue("Section 4.2.2: isEqualTo failed for " + type.getName(),
                    f.setNumber(BigDecimal.valueOf(5)).create()
                            .isEqualTo(f.setNumber(new BigDecimal("5.000")).create()));
            AssertJUnit.assertTrue("Section 4.2.2: isEqualTo failed for " + type.getName(),
                    f.setNumber(BigDecimal.valueOf(5)).create()
                            .isEqualTo(f.setNumber(new BigDecimal("5.0000")).create()));
            AssertJUnit.assertTrue("Section 4.2.2: isEqualTo failed for " + type.getName(),
                    f.setNumber(new BigDecimal("-1.23")).create()
                            .isEqualTo(f.setNumber(new BigDecimal("-1.23")).create()));
            try {
                AssertJUnit.assertTrue("Section 4.2.2: isEqualTo failed for " + type.getName(),
                        f.setNumber(new BigDecimal("-1.23")).create()
                                .isEqualTo(f.setNumber(new BigDecimal("-1.230")).create()));
                AssertJUnit.assertTrue("Section 4.2.2: isEqualTo failed for " + type.getName(),
                        f.setNumber(new BigDecimal("-1.23")).create()
                                .isEqualTo(f.setNumber(new BigDecimal("-1.2300")).create()));
                AssertJUnit.assertTrue("Section 4.2.2: isEqualTo failed for " + type.getName(),
                        f.setNumber(new BigDecimal("-1.23")).create()
                                .isEqualTo(f.setNumber(new BigDecimal("-1.23000")).create()));
                AssertJUnit.assertTrue("Section 4.2.2: isEqualTo failed for " + type.getName(),
                        f.setNumber(new BigDecimal("-1.23")).create().isEqualTo(
                                f.setNumber(new BigDecimal("-1.230000000000000000000")).create()));
            } catch (MonetaryException e) {
                // happens if we exceed the limits...
            }
        }
    }

    /**
     * For two amounts with same numeric value and currency:
     * {@code }isEqualTo()} return true, regardless of MonetaryContext.
     */
    @SpecAssertion(section = "4.2.2", id = "422-C6")
    @Test(description = "4.2.2 For each amount class, check isEqualTo(), regardless different MonetaryContext " +
            "instances.")
    public void testMonetaryAmount_isEqualToRegardlessMonetaryContext() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            MonetaryContext mc1 = f.getDefaultMonetaryContext();
            MonetaryContext mc2 = f.getMaximalMonetaryContext();
            MonetaryAmount m1;
            MonetaryAmount m2;
            if (mc1.equals(mc2)) {
                // In this cases both amount must be equals
                m1 = f.setCurrency("CHF").setContext(mc1).setNumber(10).create();
                m2 = f.setCurrency("CHF").setContext(mc2).setNumber(10).create();
                AssertJUnit.assertEquals("Section 4.2.2: m1.equals(m2) must be true for m1=" + m1 + " and m2=" + m2, m1,
                        m2);
                m1 = f.setCurrency("CHF").setContext(mc1).setNumber(10.5d).create();
                m2 = f.setCurrency("CHF").setContext(mc2).setNumber(10.5d).create();
                AssertJUnit.assertEquals("Section 4.2.2: m1.equals(m2) must be true for m1=" + m1 + " and m2=" + m2, m1,
                        m2);
                m1 = f.setCurrency("CHF").setContext(mc1).setNumber(BigDecimal.TEN).create();
                m2 = f.setCurrency("CHF").setContext(mc2).setNumber(BigDecimal.TEN).create();
                AssertJUnit.assertEquals("Section 4.2.2: m1.equals(m2) must be true for m1=" + m1 + " and m2=" + m2, m1,
                        m2);
            } else {
                // In this cases both amount must be non equals
                m1 = f.setCurrency("CHF").setContext(mc1).setNumber(10).create();
                m2 = f.setCurrency("CHF").setContext(mc2).setNumber(10).create();
                AssertJUnit
                        .assertNotSame("Section 4.2.2: m1.equals(m2) must be false for m1=" + m1 + " and m2=" + m2, m1,
                                m2);
                AssertJUnit.assertTrue("Section 4.2.2: m1.isEqualTo(m2) must be true for m1=" + m1 + " and m2=" + m2,
                        m1.isEqualTo(m2));
                AssertJUnit.assertTrue("Section 4.2.2: m1.isEqualTo(m2) must be true for m1=" + m2 + " and m2=" + m1,
                        m2.isEqualTo(m1));
                m1 = f.setCurrency("CHF").setContext(mc1).setNumber(10.5d).create();
                m2 = f.setCurrency("CHF").setContext(mc2).setNumber(10.5d).create();
                AssertJUnit
                        .assertNotSame("Section 4.2.2: m1.equals(m2) must be false for m1=" + m1 + " and m2=" + m2, m1,
                                m2);
                AssertJUnit.assertTrue("Section 4.2.2: m1.isEqualTo(m2) must be true for m1=" + m1 + " and m2=" + m2,
                        m1.isEqualTo(m2));
                AssertJUnit.assertTrue("Section 4.2.2: m1.isEqualTo(m2) must be true for m1=" + m2 + " and m2=" + m1,
                        m2.isEqualTo(m1));
                m1 = f.setCurrency("CHF").setContext(mc1).setNumber(BigDecimal.TEN).create();
                m2 = f.setCurrency("CHF").setContext(mc2).setNumber(BigDecimal.TEN).create();
                AssertJUnit
                        .assertNotSame("Section 4.2.2: m1.equals(m2) must be false for m1=" + m1 + " and m2=" + m2, m1,
                                m2);
                AssertJUnit.assertTrue("Section 4.2.2: m1.isEqualTo(m2) must be true for m1=" + m1 + " and m2=" + m2,
                        m1.isEqualTo(m2));
                AssertJUnit.assertTrue("Section 4.2.2: m1.isEqualTo(m2) must be true for m1=" + m2 + " and m2=" + m1,
                        m2.isEqualTo(m1));
            }
            AssertJUnit.assertTrue("Section 4.2.2: m.isEqualTo(m) must be true for " + m1, m1.isEqualTo(m1));
            AssertJUnit.assertTrue("Section 4.2.2: m.isEqualTo(m) must be true for " + m2, m2.isEqualTo(m2));
        }
    }

    /**
     * For two amounts with same numeric value and currency:
     * {@code }isEqualTo()} return true, regardless of iumplementation type.
     */
    @SpecAssertion(section = "4.2.2", id = "422-C7")
    @Test(description = "4.2.2 For each amount class, check isEqualTo(), regardless implementation type.")
    public void testMonetaryAmount_isEqualToRegardlessType() {
        List<MonetaryAmount> instances = new ArrayList<>();
        for (Class type : Monetary.getAmountTypes()) {
            MonetaryAmountFactory f = Monetary.getAmountFactory(type);
            f.setCurrency("CHF");
            instances.add(f.setNumber(10).create());
            instances.add(f.setNumber(10.0d).create());
            instances.add(f.setNumber(BigDecimal.TEN).create());
        }
        // compare each other...
        for (MonetaryAmount mi: instances) {
            for (MonetaryAmount mj: instances) {
                AssertJUnit
                        .assertTrue("Section 4.2.2: isEqualTo must be true for " + mi + " and " + mj, mi.isEqualTo(mj));
            }
        }
    }

    /**
     * Tests that add() correctly adds two values, using positive integers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D1")
    @Test(description = "4.2.2 For each amount class, check m1.add(m2), m1 >0, m2>0.")
    public void testAddPositiveIntegers() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mAmount2 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(20).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(30).create();
            AssertJUnit.assertEquals(
                    "Section 4.2.2: Adding two simple ammounts failed, " + mAmount1 + " + " + mAmount2 + " != " +
                            mExpectedResult, mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that add() correctly adds two values, using negative integers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D1")
    @Test(description = "4.2.2 For each amount class, check m1.add(m2), m1 <0, m2<0.")
    public void testAddNegativeIntegers() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10).create();
            MonetaryAmount mAmount2 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-20).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-30).create();
            AssertJUnit.assertEquals("Section 4.2.2: Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that add() correctly adds two values, using fractions.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D1")
    @Test(description = "4.2.2 For each amount class, check m1.add(m2), m2 is fraction.")
    public void testAddPositiveFractions() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(1.5).create();
            MonetaryAmount mAmount2 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(2.85).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(4.35).create();
            AssertJUnit.assertEquals("Section 4.2.2: Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that add() correctly adds two values, using positive and negative integers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D1")
    @Test(description = "4.2.2 For each amount class, check m1.add(m2), m1, m2 = mixed ints.")
    public void testAddMixedIntegers() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10).create();
            MonetaryAmount mAmount2 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(20).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            AssertJUnit.assertEquals("Section 4.2.2: Adding two simple ammounts", mExpectedResult, mActualResult);
        }
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-20).create();
            MonetaryAmount mAmount2 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10).create();
            AssertJUnit.assertEquals("Section 4.2.2: Adding two simple ammounts", mExpectedResult, mActualResult);
        }
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10).create();
            MonetaryAmount mAmount2 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create();
            AssertJUnit.assertEquals("Section 4.2.2: Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that add() correctly adds two values, using positive and negative fractions.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D1")
    @Test(description = "4.2.2 For each amount class, check m1.add(m2), m1, m2 = mixed fractions.")
    public void testAddMixedFractions() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(1.5).create();
            MonetaryAmount mAmount2 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-2.85).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-1.35).create();
            AssertJUnit.assertEquals("Section 4.2.2: Adding two simple ammounts", mExpectedResult, mActualResult);
        }
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-1.5).create();
            MonetaryAmount mAmount2 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(+2.85).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(1.35).create();
            AssertJUnit.assertEquals("Section 4.2.2: Adding two simple ammounts", mExpectedResult, mActualResult);
        }
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-2.85).create();
            MonetaryAmount mAmount2 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(+2.85).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create();
            AssertJUnit.assertEquals("Section 4.2.2: Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that add() with non matching currencies throws a
     * MonetaryException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D2")
    @Test(description = "4.2.2 For each amount class, ensure currency compatibility is working.")
    public void testAdd_IncompatibleCurrencies() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mAmount2 = Monetary.getAmountFactory(type).setCurrency(ADDITIONAL_CURRENCY).setNumber(20).create();
            try {
                mAmount1.add(mAmount2);
                AssertJUnit.fail("Section 4.2.2: Exception expected");
            } catch (MonetaryException ex) {
                // Expected
            }
        }
    }

    /**
     * Tests that add(0) should return itself.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D3")
    @Test(description = "4.2.2 For each amount class, ensure m2 = m1,add(0) -> m1==m2.")
    public void testAdd_Zero() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mAmount2 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            AssertJUnit.assertEquals("Section 4.2.2: Adding zero", mAmount1, mActualResult);
        }
    }

    /**
     * Tests that add(), which results in an amount exceeding the max MonetaryContext throws
     * a MonetaryException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D4")
    @Test(description = "4.2.2 For each amount class, ensure ArithemticException is thrown when adding exceeding " +
            "values.")
    public void testAdd_ExceedsCapabilities() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryContext maxCtx = f.getMaximalMonetaryContext();
            if (maxCtx.getPrecision() > 0) {
                MonetaryAmount m = f.setNumber(f.getMaxNumber()).create();
                MonetaryAmount ms = m;
                try {
                    for (int i = 0; i < 20; i++) {
                        ms = ms.add(ms);
                    }
                    AssertJUnit.fail("Section 4.2.2: ArithmeticException expected, since adding 20x " + m + " to " + m +
                            " exceeds capabilities (precision) for " +
                            type.getName());
                } catch (ArithmeticException ex) {
                    // Expected
                } catch (Exception e) {
                    AssertJUnit.fail("Section 4.2.2: Addition of amount " + ms + " to " + ms +
                            " exceeds max monetary context(scale), " +
                            "but did not throw an ArithmeticException (exception thrown was " +
                            e.getClass().getName() + "), type was " +
                            type);
                }
            }
        }
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryContext maxCtx = f.getMaximalMonetaryContext();
            if (maxCtx.getMaxScale() >= 0) {
                try {
                    MonetaryAmount m = f.setNumber(1).create();
                    MonetaryAmount m2 =
                            f.setNumber(TestUtils.createNumberWithScale(maxCtx.getMaxScale() + 5)).create();
                    m.add(m2);
                    AssertJUnit.fail("Section 4.2.2: Exception expected, since adding " + m2 + " to " + m +
                            " exceeds capabilities (scale) for " +
                            type.getName());
                } catch (ArithmeticException ex) {
                    // Expected
                }
            }
        }
    }


    /**
     * Tests that add(), which results in an amount exceeding the max MonetaryContext throws
     * a MonetaryException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D5")
    @Test(description = "4.2.2 For each amount class, ensure NullPointerException is thrown when calling m.add(null).")
    public void testAdd_Null() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try {
                MonetaryAmount mActualResult = mAmount1.add(null);
                AssertJUnit.fail("Section 4.2.2: MonetaryAmount.add(null): NullPointerException expected");
            } catch (NullPointerException ex) {
                // Expected
            }
        }
    }


    /**
     * Tests that subtract() correctly adds two values, using positive integers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D6")
    @Test(description = "4.2.2 For each amount class, ensure correct subtraction of positive ints.")
    public void testSubtractPositiveIntegers() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mAmount2 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(20).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10).create();
            AssertJUnit.assertEquals("Section 4.2.2: Subtracting two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that subtract() correctly adds two values, using negative integers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D6")
    @Test(description = "4.2.2 For each amount class, ensure correct subtraction of negative ints.")
    public void testSubtractNegativeIntegers() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10).create();
            MonetaryAmount mAmount2 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-20).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            AssertJUnit.assertEquals("Section 4.2.2: Subtracting two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that subtract() correctly adds two values, using fractions.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D6")
    @Test(description = "4.2.2 For each amount class, ensure correct subtraction of positive fractions.")
    public void testSubtractPositiveFractions() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(1.5).create();
            MonetaryAmount mAmount2 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(2.85).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-1.35).create();
            AssertJUnit.assertEquals("Section 4.2.2: Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that subtract() correctly adds two values, using positive and negative integers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D6")
    @Test(description = "4.2.2 For each amount class, ensure correct subtraction of mixed ints.")
    public void testSubtractMixedIntegers() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(20).create();
            MonetaryAmount mAmount2 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            AssertJUnit.assertEquals("Section 4.2.2: Adding two simple ammounts", mExpectedResult, mActualResult);
        }
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mAmount2 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create();
            AssertJUnit.assertEquals("Section 4.2.2: Adding two simple ammounts", mExpectedResult, mActualResult);
        }
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10).create();
            MonetaryAmount mAmount2 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-20).create();
            AssertJUnit.assertEquals("Section 4.2.2: Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that subtract() correctly adds two values, using positive and negative fractions.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D6")
    @Test(description = "4.2.2 For each amount class, ensure correct subtraction of mixed fractions.")
    public void testSubtractMixedFractions() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(1.5).create();
            MonetaryAmount mAmount2 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-2.85).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(4.35).create();
            AssertJUnit.assertEquals("Section 4.2.2: Adding two simple ammounts", mExpectedResult, mActualResult);
        }
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-1.5).create();
            MonetaryAmount mAmount2 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(+2.85).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-4.35).create();
            AssertJUnit.assertEquals("Section 4.2.2: Adding two simple ammounts", mExpectedResult, mActualResult);
        }
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(2.85).create();
            MonetaryAmount mAmount2 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(+2.85).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create();
            AssertJUnit.assertEquals("Section 4.2.2: Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that subtract() with non matching currencies throws a
     * MonetaryException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D8")
    @Test(description = "4.2.2 For each amount class, ensure subtraction with invalid currency throws " +
            "MonetaryException.")
    public void testSubtract_IncompatibleCurrencies() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mAmount2 = Monetary.getAmountFactory(type).setCurrency(ADDITIONAL_CURRENCY).setNumber(20).create();
            try {
                MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
                AssertJUnit.fail("Section 4.2.2: Exception expected");
            } catch (MonetaryException ex) {
                // Expected
            }
        }
    }

    /**
     * Tests that subtract(0) should return itself.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D7")
    @Test(description = "4.2.2 For each amount class, ensure subtraction of 0 returns same instance.")
    public void testSubtract_Zero() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mAmount2 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            AssertJUnit.assertEquals("Section 4.2.2: Subtract zero", mAmount1, mActualResult);
        }
    }

    /**
     * Tests that subtract(), which results in an amount exceeding the max MonetaryContext throws
     * a MonetaryException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D9")
    @Test(description = "4.2.2 For each amount class, ensure subtraction with exceeding capabilities throws " +
            "ArithmeticException.")
    public void testSubtract_ExceedsCapabilities() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory f = Monetary.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryContext maxCtx = f.getMaximalMonetaryContext();
            MonetaryAmount m = null;
            if (maxCtx.getPrecision() > 0) {
                MonetaryAmount mAmount1 = null;
                try {
                    mAmount1 = f.setNumber(f.getMinNumber()).create().negate();
                    m = TestUtils.createAmountWithPrecision(maxCtx.getPrecision() + 1);
                    mAmount1 = mAmount1.subtract(m);
                    AssertJUnit.fail("Section 4.2.2: ArithmeticException expected on subtraction that exceeds " +
                            "capabilities for " +
                            type.getName());
                } catch (ArithmeticException ex) {
                    // Expected
                } catch (Exception e) {
                    AssertJUnit.fail("Section 4.2.2: Subtraction of amount " + m + " from " + mAmount1 +
                            " exceeds max monetary context(scale), " +
                            "but did not throw an ArithmeticException (exception thrown was " +
                            e.getClass().getName() + "), type was " +
                            type);
                }
            }
        }

        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount mAmount1 = f.setNumber(0).create();
            MonetaryContext maxCtx = f.getMaximalMonetaryContext();
            MonetaryAmount m = null;
            if (maxCtx.getMaxScale() >= 0) {
                m = TestUtils.createAmountWithScale(maxCtx.getMaxScale() + 1);
            }
            if (m != null) {
                try {
                    mAmount1.subtract(m);
                    AssertJUnit.fail("Section 4.2.2: ArithmeticException expected on subtraction that exceeds " +
                            "capabilities for " +
                            type.getName());
                } catch (ArithmeticException ex) {
                    // Expected
                } catch (Exception e) {
                    AssertJUnit.fail("Section 4.2.2: Subtraction of amount " + m + " from " + mAmount1 +
                            " exceeds max monetary context(scale), " +
                            "but did not throw an ArithmeticException (exception thrown was " +
                            e.getClass().getName() + "), type was " +
                            type);
                }
            }
        }
    }

    /**
     * Tests that subtract(), which results in an amount exceeding the max MonetaryContext throws
     * a MonetaryException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D10")
    @Test(description = "4.2.2 For each amount class, ensure subtraction with null throws NullPointerException.")
    public void testSubtract_Null() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try {
                mAmount1.subtract(null);
                AssertJUnit.fail("Section 4.2.2: MonetaryAmount.subtract(null): NullPointerException expected");
            } catch (NullPointerException ex) {
                // Expected
            }
        }
    }

    /**
     * Test multiply() allow to multiply numbers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D11")
    @Test(description = "4.2.2 For each amount class, ensure correct multiplication of int values.")
    public void testMultiply_Integral() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.multiply(2L);
            AssertJUnit.assertTrue(
                    "Section 4.2.2: Multiplication with 2 does not return correct value for " + type.getName(),
                    mActualResult.getNumber().longValueExact() == 20);
            mActualResult = mAmount1.multiply(Double.valueOf(-3));
            AssertJUnit.assertTrue(
                    "Section 4.2.2: Multiplication with -3 does not return correct value for " + type.getName(),
                    mActualResult.getNumber().longValueExact() == -30);
            mActualResult = mAmount1.multiply(BigDecimal.ONE);
            AssertJUnit.assertTrue(
                    "Section 4.2.2: Multiplication with 1 does not return correct value for " + type.getName(),
                    mActualResult.getNumber().longValueExact() == 10);
            AssertJUnit.assertTrue(
                    "Section 4.2.2: Multiplication with 1 does not return identity value for " + type.getName(),
                    mActualResult == mAmount1);
            mActualResult = mAmount1.multiply(BigDecimal.ZERO);
            AssertJUnit.assertTrue(
                    "Section 4.2.2: Multiplication with 0 does not return correct value for " + type.getName(),
                    mActualResult.getNumber().longValue() == 0);
        }
    }

    /**
     * Test multiply() allow to multiply numbers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D11")
    @Test(description = "4.2.2 For each amount class, ensure correct multiplication of decimal values.")
    public void testMultiply_Decimals() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.multiply(1.5d);
            AssertJUnit.assertTrue(
                    "Section 4.2.2: Multiplication with 1.5 does not return correct value for " + type.getName(),
                    mActualResult.getNumber().longValueExact() == 15);
            mActualResult = mAmount1.multiply(Double.valueOf(-1.5));
            AssertJUnit.assertTrue(
                    "Section 4.2.2: Multiplication with -3 does not return correct value for " + type.getName(),
                    mActualResult.getNumber().longValueExact() == -15);
            mActualResult = mAmount1.multiply(0.0);
            AssertJUnit.assertTrue(
                    "Section 4.2.2: Multiplication with 0 does not return correct value for " + type.getName(),
                    mActualResult.getNumber().longValueExact() == 0);
            mActualResult = mAmount1.multiply(1.0);
            AssertJUnit.assertTrue(
                    "Section 4.2.2: Multiplication with 0 does not return correct value for " + type.getName(),
                    mActualResult.getNumber().longValueExact() == 10);
            AssertJUnit.assertTrue(
                    "Section 4.2.2: Multiplication with 0 does not return identity value for " + type.getName(),
                    mActualResult == mAmount1);
        }
    }

    /**
     * Test multiply(1) returns this.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D12")
    @Test(description = "4.2.2 For each amount class, ensure multiplication by one returns same instance.")
    public void testMultiplyOne() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.multiply(1);
            AssertJUnit.assertTrue(
                    "Section 4.2.2: Multiplication with 1 does not return identity value for " + type.getName(),
                    mActualResult == mAmount1);
            mActualResult = mAmount1.multiply(1.0);
            AssertJUnit.assertTrue(
                    "Section 4.2.2: Multiplication with 1 does not return identity value for " + type.getName(),
                    mActualResult == mAmount1);
            mActualResult = mAmount1.multiply(BigDecimal.ONE);
            AssertJUnit.assertTrue(
                    "Section 4.2.2: Multiplication with 1 does not return identity value for " + type.getName(),
                    mActualResult == mAmount1);
        }
    }


    /**
     * Test multiply, which results in an amount exceeding the max
     * MonetaryContext must throw a
     * ArithmeticException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D13")
    @Test(description = "4.2.2 For each amount class, ensure multiplication with exceeding values throws " +
            "ArithmeticException.")
    public void testMultiplyExceedsCapabilities() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<?> f = Monetary.getAmountFactory(type);
            MonetaryContext ctx = f.getMaximalMonetaryContext();
            if (ctx.getMaxScale() >= 0) {
                BigDecimal num = TestUtils.createNumberWithScale(ctx.getMaxScale() + 5);
                MonetaryAmount m = f.setNumber(10).setCurrency("USD").create();
                try {
                    m.multiply(num);
                } catch (ArithmeticException e) {
                    // OK!
                } catch (Exception e) {
                    AssertJUnit.fail("Section 4.2.2: Multiplication of amount 10 with " + num +
                            " exceeds max monetary context(scale), should be rounded, " +
                            "but did throw an Exception (exception thrown was " +
                            e.getClass().getName() + "), type was " +
                            type);
                }
            }
        }
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<?> f = Monetary.getAmountFactory(type);
            MonetaryContext ctx = f.getMaximalMonetaryContext();
            if (ctx.getPrecision() > 0) {
                BigDecimal num = TestUtils.createNumberWithPrecision(ctx.getPrecision() + 5);
                MonetaryAmount m = f.setNumber(10).setCurrency("USD").create();
                try {
                    m.multiply(num);
                    AssertJUnit.fail("Section 4.2.2: Multiplication of amount " + num +
                            " with 10000000 exceeds max monetary context(precision), " +
                            "but did not throw an ArithmeticException, type was " +
                            type.getName());
                } catch (ArithmeticException e) {
                    // OK
                } catch (Exception e) {
                    AssertJUnit.fail("Section 4.2.2: Multiplication of amount " + num +
                            " with 10000000 exceeds max monetary context(precision), " +
                            "but did not throw an ArithmeticException (exception thrown was " +
                            e.getClass().getName() + "), type was " +
                            type.getName());
                }
            }
        }
    }

    /**
     * Test multiply(null) must throw an NullPointerException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D14")
    @Test(description = "4.2.2 For each amount class, ensure multiplication of null throws NullPointerException.")
    public void testMultiplyNull() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try {
                MonetaryAmount mActualResult = mAmount1.multiply(null);
                AssertJUnit
                        .fail("Section 4.2.2: NullPointerException expected for multiplication with null, type was " +
                                type.getName());
            } catch (NullPointerException e) {
                // expected
            }
        }
    }

    /**
     * Test multiply(Double.NaN) must throw an ArithmeticException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D14")
    @Test(description = "4.2.2 For each amount class, ensure multiplication of Double.NaN throws ArithmeticException.")
    public void testMultiply_DoubleNaN() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try {
                MonetaryAmount mActualResult = mAmount1.multiply(Double.NaN);
                AssertJUnit
                        .fail("Section 4.2.2: ArithmeticException expected for multiplication with Double.NaN, type was " +
                                type.getName());
            } catch (ArithmeticException e) {
                // expected
            }
        }
    }

    /**
     * Test multiply(Double.POSITIVE_INFINITY) must throw an ArithmeticException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D14")
    @Test(description = "4.2.2 For each amount class, ensure multiplication of Double.POSITIVE_INFINITY throws ArithmeticException.")
    public void testMultiply_DoublePOSITIVE_INFINITY() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try {
                MonetaryAmount mActualResult = mAmount1.multiply(Double.POSITIVE_INFINITY);
                AssertJUnit
                        .fail("Section 4.2.2: ArithmeticException expected for multiplication with Double.POSITIVE_INFINITY, type was " +
                                type.getName());
            } catch (ArithmeticException e) {
                // expected
            }
        }
    }

    /**
     * Test multiply(Double.POSITIVE_INFINITY) must throw an ArithmeticException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D14")
    @Test(description = "4.2.2 For each amount class, ensure multiplication of Double.NEGATIVE_INFINITY throws ArithmeticException.")
    public void testMultiply_DoubleNEGATIVE_INFINITY() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try {
                MonetaryAmount mActualResult = mAmount1.multiply(Double.NEGATIVE_INFINITY);
                AssertJUnit
                        .fail("Section 4.2.2: ArithmeticException expected for multiplication with Double.NEGATIVE_INFINITY, type was " +
                                type.getName());
            } catch (ArithmeticException e) {
                // expected
            }
        }
    }

    /**
     * Test divide() function allow to divide numbers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D15")
    @Test(description = "4.2.2 For each amount class, ensure correct division.")
    public void testDivide() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount m = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount m2 = m.divide(10);
            AssertJUnit.assertEquals("Section 4.2.2: Currency not equal after division, type was " + type.getName(),
                    DEFAULT_CURRENCY, m2.getCurrency().getCurrencyCode());
            AssertJUnit.assertEquals("Section 4.2.2: Division result is not correct for " + type.getName(), 1,
                    m2.getNumber().longValueExact());
        }
    }

    /**
     * Test divideToIntegralValue() function allow to divide numbers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D15")
    @Test(description = "4.2.2 For each amount class, ensure correct division with int values.")
    public void testDivideToIntegralValue() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            CurrencyUnit euro = Monetary.getCurrency("EUR");
            MonetaryAmount money1 = Monetary.getAmountFactory(type).setNumber(BigDecimal.ONE).setCurrency(euro).create();
            MonetaryAmount result = money1.divideToIntegralValue(new BigDecimal("0.5001"));
            AssertJUnit.assertEquals(
                    "Section 4.2.2: divideToIntegralValue returned incorrect result for " + type.getName(),
                    result.getNumber().numberValue(BigDecimal.class).stripTrailingZeros(),
                    new BigDecimal("1.0").stripTrailingZeros());
            result = money1.divideToIntegralValue(new BigDecimal("0.2001"));
            AssertJUnit.assertEquals(
                    "Section 4.2.2: divideToIntegralValue returned incorrect result for " + type.getName(),
                    result.getNumber().numberValue(BigDecimal.class).stripTrailingZeros(),
                    new BigDecimal("4.0").stripTrailingZeros());
            result = money1.divideToIntegralValue(new BigDecimal("5.0"));
            AssertJUnit
                    .assertTrue("Section 4.2.2: divideToIntegralValue returned incorrect result for " + type.getName(),
                            result.getNumber().numberValue(BigDecimal.class).intValueExact() == 0);
        }
    }

    /**
     * Test divide(0) function must throw an ArithmeticException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D16")
    @Test(description = "4.2.2 For each amount class, ensure divide(0) throws ArithmeticException.")
    public void testDivideZero() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try {
                MonetaryAmount mActualResult = mAmount1.divide(0);
                AssertJUnit.fail("Section 4.2.2: ArithmeticException expected on division by 0, type was " +
                        type.getName());
            } catch (ArithmeticException ex) {
                // expected
            }
        }
    }

    /**
     * Test divide(0) function must throw an ArithmeticException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D16")
    @Test(description = "4.2.2 For each amount class, ensure divide(Double.NaN) throws ArithmeticException.")
    public void testDivideDoubleNaN() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try {
                MonetaryAmount mActualResult = mAmount1.divide(Double.NaN);
                AssertJUnit.fail("Section 4.2.2: ArithmeticException expected on division by Double.NaN, type was " +
                        type.getName());
            } catch (ArithmeticException ex) {
                // expected
            }
        }
    }

    /**
     * Test divide(0) function must return ZERO amount.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D16")
    @Test(description = "4.2.2 For each amount class, ensure divide(Double.POSITIVE_INFINITY) return ZERO amount.")
    public void testDivideDoublePOSITIVE_INFINITY() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.divide(Double.POSITIVE_INFINITY);
            AssertJUnit.assertEquals("Section 4.2.2: ZERO amount expected on division by Double.POSITIVE_INFINITY, type was " +
                    type.getName(), mActualResult, Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create());
        }
    }

    /**
     * Test divide(Double.NEGATIVE_INFINITY) function must return ZERO amount.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D16")
    @Test(description = "4.2.2 For each amount class, ensure divide(Double.NEGATIVE_INFINITY) return ZERO amount.")
    public void testDivideDoubleNEGATIVE_INFINITY() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.divide(Double.NEGATIVE_INFINITY);
            AssertJUnit.assertEquals("Section 4.2.2: ZERO amount expected on division by Double.POSITIVE_INFINITY, type was " +
                    type.getName(), mActualResult, Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create());
        }
    }

    /**
     * Test divide(1) should return this.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D17")
    @Test(description = "4.2.2 For each amount class, ensure divide 1 returns same instance.")
    public void testDivideOne() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.divide(1);
            AssertJUnit.assertTrue("Section 4.2.2: Division by 1 does not return identity value for " + type.getName(),
                    mActualResult == mAmount1);
            mActualResult = mAmount1.divide(1.0);
            AssertJUnit.assertTrue("Section 4.2.2: Division by 1 does not return identity value for " + type.getName(),
                    mActualResult == mAmount1);
            mActualResult = mAmount1.divide(BigDecimal.ONE);
            AssertJUnit.assertTrue("Section 4.2.2: Division by 1 does not return identity value for " + type.getName(),
                    mActualResult == mAmount1);
        }
    }

    /**
     * Test  divide(null)must throw a NullPointerException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D18")
    @Test(description = "4.2.2 For each amount class, ensure divide by null throws NullPointerException.")
    public void testDivideNull() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try {
                MonetaryAmount mActualResult = mAmount1.divide(null);
                AssertJUnit.fail("Section 4.2.2: NullPointerException expected for division by null, type was " +
                        type.getName());
            } catch (NullPointerException e) {
                // expected
            }
        }
    }

    /**
     * Test  remainder()allow to calculate the remainder.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D19")
    @Test(description = "4.2.2 For each amount class, ensure correct results for remainder.")
    public void testRemainder() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<?> f = Monetary.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount[] moneys = new MonetaryAmount[]{f.setNumber(100).create(), f.setNumber(34242344).create(),
                    f.setNumber(23123213.435).create(), f.setNumber(0).create(), f.setNumber(-100).create(),
                    f.setNumber(-723527.36532).create()};
            for (MonetaryAmount m : moneys) {
                AssertJUnit.assertEquals("Section 4.2.2: Invalid remainder of " + 10.50 + " for " + type.getName(),
                        m.getFactory().setNumber(m.getNumber().numberValue(BigDecimal.class)
                                .remainder(BigDecimal.valueOf(10.50)))
                                .create(), m.remainder(10.50));
                AssertJUnit.assertEquals("Section 4.2.2: Invalid remainder of " + -30.20 + " for " + type.getName(),
                        m.getFactory().setNumber(m.getNumber().numberValue(BigDecimal.class)
                                .remainder(BigDecimal.valueOf(-30.20)))
                                .create(), m.remainder(-30.20));
                AssertJUnit.assertEquals("Section 4.2.2: Invalid remainder of " + -3 + " for " + type.getName(),
                        m.getFactory().setNumber(m.getNumber().numberValue(BigDecimal.class)
                                .remainder(BigDecimal.valueOf(-3))).create(),
                        m.remainder(-3));
                AssertJUnit.assertEquals("Section 4.2.2: Invalid remainder of " + 3 + " for " + type.getName(),
                        m.getFactory().setNumber(m.getNumber().numberValue(BigDecimal.class)
                                .remainder(BigDecimal.valueOf(3))).create(),
                        m.remainder(3));
            }
        }
    }

    /**
     * Test remainder(0) must throw an ArithmeticException
     */
    @SpecAssertion(section = "4.2.2", id = "422-D20")
    @Test(description = "4.2.2 For each amount class, ensure remainder(0), double, throws ArithmeticException.")
    public void testRemainderZero_Double() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount m = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try {
                m.remainder(0.0d);
                AssertJUnit
                        .fail("Section 4.2.2: remainder(0) did not throw an ArithmeticException for " + type.getName());
            } catch (ArithmeticException e) {
                // OK, ignore
            } catch (Exception e) {
                AssertJUnit.fail("Section 4.2.2: remainder(0.0d) did not throw an ArithmeticException for " +
                        type.getName() + ", but " +
                        e);
            }
        }
    }

    /**
     * Test remainder(0) must throw an ArithmeticException
     */
    @SpecAssertion(section = "4.2.2", id = "422-D20")
    @Test(description = "4.2.2 For each amount class, ensure remainder(0), long, throws ArithmeticException.")
    public void testRemainderZero_Long() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount m = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try {
                m.remainder(0L);
                AssertJUnit.fail("Section 4.2.2: remainder(0L) did not throw an ArithmeticException for " +
                        type.getName());
            } catch (ArithmeticException e) {
                // OK, ignore
            } catch (Exception e) {
                AssertJUnit.fail("Section 4.2.2: remainder(0L) did not throw an ArithmeticException for " +
                        type.getName() + ", but " +
                        e);
            }
        }
    }

    /**
     * Test remainder(0) must throw an ArithmeticException
     */
    @SpecAssertion(section = "4.2.2", id = "422-D20")
    @Test(description = "4.2.2 For each amount class, ensure remainder(0), Number, throws ArithmeticException.")
    public void testRemainderZero_Number() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount m = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try {
                m.remainder(BigDecimal.ZERO);
                AssertJUnit.fail("Section 4.2.2: remainder(BigDecimal.ZERO) did not throw an ArithmeticException for " +
                        type.getName());
            } catch (ArithmeticException e) {
                // OK, ignore
            } catch (Exception e) {
                AssertJUnit.fail("Section 4.2.2: remainder(BigDecimal.ZERO) did not throw an ArithmeticException for " +
                        type.getName() +
                        ", but " + e);
            }
        }
    }

    /**
     * Test remainder(null) must throw a NullPointerException
     */
    @SpecAssertion(section = "4.2.2", id = "422-D21")
    @Test(description = "4.2.2 For each amount class, ensure remainder(null), throws NullPointerException.")
    public void testRemainderNull() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try {
                MonetaryAmount mActualResult = mAmount1.remainder(null);
                AssertJUnit.fail("Section 4.2.2: NullPointerException expected for remainder with null, type was " +
                        type.getName());
            } catch (NullPointerException e) {
                // expected
            }
        }
    }

    /**
     * Test remainder(null) must throw a NullPointerException
     */
    @SpecAssertion(section = "4.2.2", id = "422-D21")
    @Test(description = "4.2.2 For each amount class, ensure remainder(Double.NaN), throws ArithmeticException.")
    public void testRemainder_DoubleNaN() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try {
                MonetaryAmount mActualResult = mAmount1.remainder(Double.NaN);
                AssertJUnit.fail("Section 4.2.2: ArithmeticException expected for remainder(Double.NaN), type was " +
                        type.getName());
            } catch (ArithmeticException e) {
                // expected
            }
        }
    }

    /**
     * Test remainder(null) must throw a NullPointerException
     */
    @SpecAssertion(section = "4.2.2", id = "422-D21")
    @Test(description = "4.2.2 For each amount class, ensure remainder(Double.POSITIVE_INFINITY), throws ArithmeticException.")
    public void testRemainder_DoublePOSITIVE_INFINITY() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.remainder(Double.POSITIVE_INFINITY);
            AssertJUnit.assertEquals(Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create(), mActualResult);
        }
    }

    /**
     * Test remainder(null) must throw a NullPointerException
     */
    @SpecAssertion(section = "4.2.2", id = "422-D21")
    @Test(description = "4.2.2 For each amount class, ensure remainder(Double.NEGATIVE_INFINITY), throws ArithmeticException.")
    public void testRemainder_DoubleNEGATIVE_INFINITY() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.remainder(Double.NEGATIVE_INFINITY);
            AssertJUnit.assertEquals(Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create(), mActualResult);
        }
    }


    /**
     * Test  divideAndRemainder()allow to divide/remind numbers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D22")
    @Test(description = "4.2.2 For each amount class, ensure correct divideAndRemainder().")
    public void testDivideAndRemainder() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<?> f = Monetary.getAmountFactory(type);
            MonetaryAmount money1 = f.setNumber(BigDecimal.ONE).setCurrency("EUR").create();
            if (f.getDefaultMonetaryContext().getMaxScale() < 5) {
                MonetaryAmount[] divideAndRemainder = money1.divideAndRemainder(new BigDecimal("0.6"));
                AssertJUnit.assertEquals("Section 4.2.2: divideAndRemainder(0.6)[0] failed for " + type.getName(),
                        divideAndRemainder[0].getNumber().numberValue(BigDecimal.class)
                                .stripTrailingZeros(), BigDecimal.ONE.stripTrailingZeros());
                AssertJUnit.assertEquals("Section 4.2.2: divideAndRemainder(0.6)[1] failed for " + type.getName(),
                        divideAndRemainder[1].getNumber().numberValue(BigDecimal.class)
                                .stripTrailingZeros(), new BigDecimal("0.4").stripTrailingZeros());
            } else {
                MonetaryAmount[] divideAndRemainder = money1.divideAndRemainder(new BigDecimal("0.50001"));
                AssertJUnit.assertEquals("Section 4.2.2: divideAndRemainder(0.50001)[0] failed for " + type.getName(),
                        divideAndRemainder[0].getNumber().numberValue(BigDecimal.class)
                                .stripTrailingZeros(), BigDecimal.ONE.stripTrailingZeros());
                AssertJUnit.assertEquals("Section 4.2.2: divideAndRemainder(0.50001)[1] failed for " + type.getName(),
                        divideAndRemainder[1].getNumber().numberValue(BigDecimal.class)
                                .stripTrailingZeros(), new BigDecimal("0.49999").stripTrailingZeros());
            }
        }
    }

    /**
     * Test  divideAndRemainder(0) throws an ArithmeticException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D23")
    @Test(description = "4.2.2 For each amount class, ensure correct divideAndRemainderZero().")
    public void testDivideAndRemainderZero() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try {
                mAmount1.divideAndRemainder(BigDecimal.ZERO);
                AssertJUnit.fail("Section 4.2.2: divideAndRemainder(0) for " + type.getName() +
                        ", should throw ArithmeticException!");
            } catch (ArithmeticException e) {
                // expected
            } catch (Exception e) {
                AssertJUnit.fail("Section 4.2.2: Unexpected exception for divideAndRemainder(0) for " + type.getName() +
                        ", should be ArithmeticException, but was " + e);
            }
        }
    }

    /**
     * Test  divideAndRemainder(null) throws an NullPointerException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D24")
    @Test(description = "4.2.2 For each amount class, ensure divideAndRemainder(null) throws a NullPointerException.")
    public void testDivideAndRemainderNull() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try {
                MonetaryAmount[] mActualResult = mAmount1.divideAndRemainder(null);
                AssertJUnit.fail("Section 4.2.2: NullPointerException expected for divideAndRemainder with null, " +
                        "type was " +
                        type.getName());
            } catch (NullPointerException e) {
                // expected
            }
        }
    }

    /**
     * Test  divideAndRemainder(null) throws an NullPointerException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D24")
    @Test(description = "4.2.2 For each amount class, ensure divideAndRemainder(Double.NaN) throws a ArithmeticException.")
    public void testDivideAndRemainderDoubleNaN() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try {
                MonetaryAmount[] mActualResult = mAmount1.divideAndRemainder(Double.NaN);
                AssertJUnit.fail("Section 4.2.2: ArithmeticException expected for divideAndRemainder with Double.NaN, " +
                        "type was " +
                        type.getName());
            } catch (ArithmeticException e) {
                // expected
            }
        }
    }

    /**
     * Test  divideAndRemainder(null) throws an NullPointerException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D24")
    @Test(description = "4.2.2 For each amount class, ensure divideAndRemainder(Double.POSITIVE_INFINITY) returns ZERO amount.")
    public void testDivideAndRemainderDoublePOSITIVE_INFINITY() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount[] mActualResult = mAmount1.divideAndRemainder(Double.POSITIVE_INFINITY);
            MonetaryAmount zero = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create();
            AssertJUnit.assertEquals("Section 4.2.2: ZERO amount expected for divideAndRemainder with Double.POSITIVE_INFINITY, " +
                            "type was " +
                            type.getName(), zero,
                    mActualResult[0]);
            AssertJUnit.assertEquals("Section 4.2.2: ZERO amount expected for divideAndRemainder with Double.POSITIVE_INFINITY, " +
                            "type was " +
                            type.getName(), zero,
                    mActualResult[1]);
        }
    }

    /**
     * Test  divideAndRemainder(null) throws an NullPointerException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D24")
    @Test(description = "4.2.2 For each amount class, ensure divideAndRemainder(Double.NEGATIVE_INFINITY) returns ZERO amount.")
    public void testDivideAndRemainderDoubleNEGATIVE_INFINITY() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount[] mActualResult = mAmount1.divideAndRemainder(Double.NEGATIVE_INFINITY);
            MonetaryAmount zero = Monetary.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create();
            AssertJUnit.assertEquals("Section 4.2.2: ZERO amount expected for divideAndRemainder with Double.NEGATIVE_INFINITY, " +
                            "type was " +
                            type.getName(), zero,
                    mActualResult[0]);
            AssertJUnit.assertEquals("Section 4.2.2: ZERO amount expected for divideAndRemainder with Double.NEGATIVE_INFINITY, " +
                            "type was " +
                            type.getName(), zero,
                    mActualResult[1]);
        }
    }


    /**
     * Test  divideAndRemainder(1) returns this/ZERO.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D25")
    @Test(description = "4.2.2 For each amount class, ensure divideAndRemainder(1) returns same instance.")
    public void testDivideAndRemainderOne() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<?> f = Monetary.getAmountFactory(type);
            MonetaryAmount m = f.setNumber(100).setCurrency("CHF").create();
            AssertJUnit.assertEquals(
                    "Section 4.2.2: DivideAndRemainder not returning correct result for type: " + type.getName(),
                    BigDecimal.valueOf(33),
                    m.divideAndRemainder(3)[0].getNumber().numberValue(BigDecimal.class).stripTrailingZeros());
            AssertJUnit.assertEquals(
                    "Section 4.2.2: DivideAndRemainder not returning correct result for type: " + type.getName(),
                    BigDecimal.valueOf(1),
                    m.divideAndRemainder(3)[1].getNumber().numberValue(BigDecimal.class).stripTrailingZeros());
            AssertJUnit.assertEquals(
                    "Section 4.2.2: DivideAndRemainder not returning correct result for type: " + type.getName(),
                    BigDecimal.ONE,
                    m.divideAndRemainder(BigDecimal.valueOf(3))[1].getNumber().numberValue(BigDecimal.class)
                            .stripTrailingZeros());
        }
    }

    /**
     * Test scaleByPowerOfTen()allow to scale by power of 10.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D26")
    @Test(description = "4.2.2 For each amount class, ensure scaleByPowerOfTen(1) throws an ArithmeticException.",
            expectedExceptions = java.lang.ArithmeticException.class)
    public void testScaleByPowerOfTen() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<?> f = Monetary.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount[] amounts = new MonetaryAmount[]{f.setNumber(100).create(), f.setNumber(342444).create(),
                    f.setNumber(2312213.435).create(), f.setNumber(BigDecimal.ZERO).create(),
                    f.setNumber(-100).create(), f.setNumber(-723527.3653).create()};

            for (MonetaryAmount m : amounts) {
                for (int p = -3; p < 3; p++) {
                    BigDecimal bdExpected = m.scaleByPowerOfTen(p).getNumber().numberValue(BigDecimal.class);
                    BigDecimal bdCalculated = m.getNumber().numberValue(BigDecimal.class).scaleByPowerOfTen(p);
                    bdCalculated = bdCalculated.setScale(m.getContext().getMaxScale(), RoundingMode.HALF_EVEN);
                    if (bdExpected.signum() == 0) {
                        AssertJUnit.assertTrue("Section 4.2.2: Invalid " + m + " -> scaleByPowerOfTen(" + p + ") for " +
                                type.getName(), bdCalculated.signum() == 0);
                    } else {
                        AssertJUnit
                                .assertEquals("Section 4.2.2: Invalid " + m + " -> scaleByPowerOfTen(" + p + ") for " +
                                                type.getName(), bdExpected
                                                .setScale(m.getContext().getMaxScale() - 1,
                                                        RoundingMode.HALF_EVEN).stripTrailingZeros(),
                                        bdCalculated.setScale(m.getContext().getMaxScale() - 1,
                                                RoundingMode.HALF_EVEN).stripTrailingZeros());
                    }
                }
            }
        }
    }

    /**
     * Test scaleByPowerOfTen() chaining returns correct results.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D26")
    @Test(description = "4.2.2 For each amount class, ensure scaleByPowerOfTen(-1).scaleByPowerOfTen(1) returns correct results or throws ArithmeticException.")
    public void testScaleByPowerOfTenArithmeticException() {
      for (Class type : Monetary.getAmountTypes()) {
        if (type.equals(TestAmount.class)) {
          continue;
        }
        MonetaryAmountFactory<?> f = Monetary.getAmountFactory(type);
        MonetaryContext maximalContext = f.getMaximalMonetaryContext();
        f.setCurrency("CHF");
        f.setContext(maximalContext);
        f.setNumber(BigDecimal.valueOf(16, maximalContext.getMaxScale()));

        MonetaryAmount m = f.create();
        try {
          AssertJUnit.assertEquals("Section 4.2.2: Invalid " + m + " -> scaleByPowerOfTen(-1) for " +
                  type.getName(), m, m.scaleByPowerOfTen(-1).scaleByPowerOfTen(1));
        } catch (ArithmeticException e) {
          // exceeds numeric capabilities, fine
        }
      }
    }

    /**
     * Test abs() for getting the absolute value.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D27")
    @Test(description = "4.2.2 For each amount class, test absolute().")
    public void testAbsolute() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount m = f.setNumber(10).create();
            AssertJUnit
                    .assertEquals("Section 4.2.2: abs(m) !equals m, if m > 0 for type: " + type.getName(), m, m.abs());
            AssertJUnit.assertTrue("Section 4.2.2: abs(m) != m, if m > 0 for type: " + type.getName(), m == m.abs());
            m = f.setNumber(0).create();
            AssertJUnit
                    .assertEquals("Section 4.2.2: abs(m) != equals, if m == 0 for type: " + type.getName(), m, m.abs());
            AssertJUnit.assertTrue("Section 4.2.2: abs(m) != m, if m == 0 for type: " + type.getName(), m == m.abs());
            m = f.setNumber(-10).create();
            AssertJUnit.assertEquals("Section 4.2.2: abs(m) == m, if m < 0 for type: " + type.getName(), m.negate(),
                    m.abs());
            AssertJUnit.assertTrue("Section 4.2.2: abs(m) == m, if m < 0 for type: " + type.getName(), m != m.abs());
        }
    }

    /**
     * Test negate() for negating a value.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D28")
    @Test(description = "4.2.2 For each amount class, test negate().")
    public void testNegate() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = Monetary.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount m = f.setNumber(100).create();
            AssertJUnit
                    .assertEquals("Section 4.2.2: negate(-x) failed for " + type.getName(), f.setNumber(-100).create(),
                            m.negate());
            m = f.setNumber(-123.234).create();
            AssertJUnit.assertEquals("Section 4.2.2: negate(+x) failed for " + type.getName(),
                    f.setNumber(123.234).create(), m.negate());
        }
    }

    /**
     * Ensure with(MonetaryOperator) can be called and produces
     * amounts of the same type and correct value.
     */
    @SpecAssertion(section = "4.2.2", id = "422-E1")
    @Test(description = "4.2.2 For each amount class, test with().")
    public void testWith() {
        MonetaryOperator op = new MonetaryOperator() {
            @Override
            public MonetaryAmount apply(MonetaryAmount amount) {
                return amount;
            }
        };
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount amount = Monetary.getAmountFactory(type).setCurrency("CHF").setNumber(10).create();
            MonetaryAmount amount2 = amount.with(op);
            AssertJUnit.assertTrue(
                    "Section 4.2.2: MonetaryAmount returned from operator is wrapped by implementation of type: " +
                            type.getName(), amount == amount2);
            final MonetaryAmount result = Monetary.getAmountFactory(type).setCurrency("CHF").setNumber(4).create();
            MonetaryOperator op2 = new MonetaryOperator() {
                @Override
                public MonetaryAmount apply(MonetaryAmount amount) {
                    return result;
                }
            };
            amount2 = amount.with(op);
            AssertJUnit.assertTrue(
                    "Section 4.2.2: MonetaryAmount returned from operator is wrapped by implementation of type: " +
                            type.getName(), amount == amount2);
        }
    }

    /**
     * Ensure with(MonetaryOperator) can be called and produces
     * amounts of the same type and correct value, testing operators provided by TCKTestSetup.
     */
    @SpecAssertion(section = "4.2.2", id = "422-E1")
    @Test(description = "4.2.2 For each amount class, test with().")
    public void testWith4ProvidedOperators() {
        for (MonetaryOperator op : TCKTestSetup.getTestConfiguration().getMonetaryOperators4Test()) {
            for (Class type : Monetary.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount amount = Monetary.getAmountFactory(type).setCurrency("CHF").setNumber(10).create();
                MonetaryAmount amount2 = amount.with(op);
                AssertJUnit.assertTrue(
                        "Section 4.2.2: MonetaryAmount returned from operator is wrapped by implementation of type: " +
                                type.getName(), amount.getClass() == amount2.getClass());
                final MonetaryAmount result = Monetary.getAmountFactory(type).setCurrency("CHF").setNumber(4).create();
                MonetaryOperator op2 = new MonetaryOperator() {
                    @Override
                    public MonetaryAmount apply(MonetaryAmount amount) {
                        return amount;
                    }
                };
                amount2 = amount.with(op2);
                AssertJUnit.assertTrue(
                        "Section 4.2.2: MonetaryAmount returned from operator is wrapped by implementation of type: " +
                                type.getName(), amount == amount2);
            }
        }
    }

    /**
     * Test with(m) throws a MonetaryException, if m throws any exception.
     */
    @SpecAssertion(section = "4.2.2", id = "422-E2")
    @Test(description = "4.2.2 Bad case: For each amount class, test with(), operator throws exception.")
    public void testWithInvalidOperator() {
        MonetaryOperator op = new MonetaryOperator() {
            @Override
            public MonetaryAmount apply(MonetaryAmount value) {
                throw new IllegalStateException();
            }
        };
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory factory = Monetary.getAmountFactory(type);
            MonetaryAmount amount = factory.setCurrency("XXX").setNumber(1).create();
            try {
                amount.with(op);
                AssertJUnit.fail("Section 4.2.2: MonetaryException expected as operator fails, type was " +
                        type.getName());
            } catch (MonetaryException e) {
                // OK, everything else makes the test fail!
            }
        }
    }

    /**
     * Test with(null) throws a NullPointerException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-E2")
    @Test(description = "4.2.2 Bad case: For each amount class, test with(null), expected NullPointerException.")
    public void testWithNull() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory factory = Monetary.getAmountFactory(type);
            MonetaryAmount amount = factory.setCurrency("XXX").setNumber(1).create();
            try {
                amount.with(null);
                AssertJUnit.fail("Section 4.2.2: NullPointerException expected as operator applied is null, type was " +
                        type.getName());
            } catch (NullPointerException e) {
                // OK, everything else makes the test fail!
            }
        }
    }

    /**
     * Test with(null) throws a NullPointerException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-E2")
    @Test(description = "4.2.2 Bad case: For each amount class, test with(), operator throws exception.")
    public void testWithNull4ProvidedOperators() {
        for (MonetaryOperator op : TCKTestSetup.getTestConfiguration().getMonetaryOperators4Test()) {
            try {
                op.apply(null);
                AssertJUnit.fail("Section 4.2.2: NullPointerException expected as operator was applied on null, " +
                        "operator was " +
                        op.getClass().getName());
            } catch (NullPointerException e) {
                // OK, everything else makes the test fail!
            }
        }
    }

    /**
     * Ensure query(MonetaryQUery) can be called and produces
     * valuable results.
     */
    @SpecAssertion(section = "4.2.2", id = "422-E3")
    @Test(description = "4.2.2 For each amount class, test query().")
    public void testQuery() {
        MonetaryQuery<Integer> query = new MonetaryQuery<Integer>() {
            @Override
            public Integer queryFrom(MonetaryAmount amount) {
                return amount.getNumber().intValue();
            }
        };
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount amount = Monetary.getAmountFactory(type).setCurrency("CHF").setNumber(10).create();
            Integer value = amount.query(query);
            AssertJUnit.assertTrue(
                    "Section 4.2.2: Value returned from MonetaryAmount Query is not correct for " + type.getName(),
                    value == 10);
            amount = Monetary.getAmountFactory(type).setCurrency("CHF").setNumber(4.5).create();
            value = amount.query(query);
            AssertJUnit.assertTrue(
                    "Section 4.2.2: Value returned from MonetaryAmount Query is not correct for " + type.getName(),
                    value == 4);
        }
    }

    /**
     * Test query(q) throws a MonetaryException, if q throws any exception.
     */
    @SpecAssertion(section = "4.2.2", id = "422-E4")
    @Test(description = "4.2.2 For each amount class, test query(), MonetaryQuery throws exception, " +
            "MonetaryException expected.")
    public void testQueryInvalidQuery() {
        MonetaryQuery<Integer> query = new MonetaryQuery<Integer>() {
            @Override
            public Integer queryFrom(MonetaryAmount amount) {
                throw new IllegalStateException();
            }
        };
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            TestUtils.testComparable("Section 4.2.2", type);
            MonetaryAmountFactory factory = Monetary.getAmountFactory(type);
            MonetaryAmount amount = factory.setCurrency("XXX").setNumber(1).create();
            try {
                amount.query(query);
                AssertJUnit.fail("Section 4.2.2: MonetaryException expected as query applied is failing, type was " +
                        type.getName());
            } catch (MonetaryException e) {
                // OK, everything else makes the test fail!
            }
        }
    }

    /**
     * Test query(null) throws a NullPointerException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-E4")
    @Test(description = "4.2.2 For each amount class, test query(null), NullPointerException expected.")
    public void testQueryNull() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            TestUtils.testComparable("Section 4.2.2", type);
            MonetaryAmountFactory factory = Monetary.getAmountFactory(type);
            MonetaryAmount amount = factory.setCurrency("XXX").setNumber(1).create();
            try {
                amount.query(null);
                AssertJUnit.fail("Section 4.2.2: NullPointerException expected as query applied is null, type was " +
                        type.getName());
            } catch (NullPointerException e) {
                // OK, everything else makes the test fail!
            }
        }
    }

    /**
     * Implementations of MonetaryAmount must implement hashCode,
     * considering number, currency and implementation type,
     * monetary
     * context.
     */
    @SpecAssertion(section = "4.2.2", id = "422-F1")
    @Test(description = "4.2.2 For each amount class, test implements hashCode().")
    public void testImplementsHashCode() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount amount = Monetary.getAmountFactory(type).setCurrency("USD").setNumber(0).create();
            TestUtils.testHasPublicMethod("Section 4.2.2", type, type, "hashCode");
            MonetaryAmount amount2 = Monetary.getAmountFactory(type).setCurrency("USD").setNumber(0).create();
            AssertJUnit.assertEquals("Section 4.2.2: hashCode() for equal amounts differ for type " + type.getName(),
                    amount.hashCode(), amount2.hashCode());
        }
    }

    /**
     * Implementations of MonetaryAmount must implement
     * equals,
     * considering number, currency and implementation type,
     * monetary
     * context.
     */
    @SpecAssertion(section = "4.2.2", id = "422-F2")
    @Test(description = "4.2.2 For each amount class, test implements equals().")
    public void testImplementsEquals() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount amount = Monetary.getAmountFactory(type).setCurrency("XXX").setNumber(0).create();
            TestUtils.testHasPublicMethod("Section 4.2.2", type, type, "equals", Object.class);
            MonetaryAmount amount2 = Monetary.getAmountFactory(type).setCurrency("XXX").setNumber(0).create();
            AssertJUnit
                    .assertEquals("Section 4.2.2: equals(Object) for equal amounts returns false for " + type.getName(),
                            amount, amount2);
        }
    }

    /**
     * Implementations of MonetaryAmount must be Comparable.
     */
    @SpecAssertion(section = "4.2.2", id = "422-F3")
    @Test(description = "4.2.2 For each amount class, test is Comparable.")
    public void testImplementComparable() {
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            TestUtils.testComparable("Section 4.2.2", type);
            MonetaryAmountFactory factory = Monetary.getAmountFactory(type);
            MonetaryAmount amount = factory.setCurrency("XXX").setNumber(0).create();
            MonetaryAmount amount2 = factory.setCurrency("XXX").setNumber(0).create();
            MonetaryAmount amount3 = factory.setCurrency("CHF").setNumber(1).create();
            MonetaryAmount amount4 = factory.setCurrency("XXX").setNumber(1).create();

            AssertJUnit.assertTrue("Section 4.2.2: Comparable failed for: " + type.getName(),
                    amount.compareTo(amount3) > 0);

            AssertJUnit.assertTrue("Section 4.2.2: Comparable failed for: " + type.getName(),
                    amount3.compareTo(amount) < 0);

            AssertJUnit.assertTrue("Section 4.2.2: Comparable failed for: " + type.getName(),
                    amount.compareTo(amount4) < 0);

            AssertJUnit.assertTrue("Section 4.2.2: Comparable failed for: " + type.getName(),
                    amount4.compareTo(amount) > 0);
        }
    }

    /**
     * Implementations of MonetaryAmount must be Serializable.
     */
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    @SpecAssertion(section = "4.2.2", id = "422-F4")
    @Test(description = "4.2.2 For each amount class, test iis immutable.")
    public void testImmutable() {
        for (Class type : TCKTestSetup.getTestConfiguration().getAmountClasses()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            TestUtils.testImmutable("Section 4.2.2", type);
        }
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            //noinspection ErrorNotRethrown
            try {
                TestUtils.testImmutable("Section 4.2.2", type);
            } catch (MutabilityAssertionError e) {
                System.out
                        .println("Warning: found non immutable MonetaryAmountType: " + type.getName() + ", details: " +
                                e.getMessage());
            }
        }
    }


}
