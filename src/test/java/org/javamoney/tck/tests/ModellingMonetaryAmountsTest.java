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
import org.testng.annotations.Test;

import javax.money.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.testng.AssertJUnit.*;

@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class ModellingMonetaryAmountsTest{

    private final static String DEFAULT_CURRENCY = "CHF";

    private final static String ADDITIONAL_CURRENCY = "USD";

    /**
     * Ensure at least one javax.money.MonetaryAmount implementation is registered,
     * by calling MonetaryAmounts.getAmountTypes();
     */
    @SpecAssertion(section = "4.2.2", id = "422-0")
    @Test
    public void testEnsureMonetaryAmount(){
        assertNotNull("MonetaryAmounts.getAmountTypes() must never return null.", MonetaryAmounts.getAmountTypes());
        assertTrue("At least one type must be registered with MonetaryAmounts (see getAmountTypes()).",
                   MonetaryAmounts.getAmountTypes().size() > 0);
    }

    /**
     * For each MonetaryAmount implementation: Ensure getCurrencyCode
     * returns correct results.
     */
    @SpecAssertion(section = "4.2.2", id = "422-A1")
    @Test
    public void testCurrencyCode(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            for(Currency jdkCur : Currency.getAvailableCurrencies()){
                MonetaryAmount amount =
                        MonetaryAmounts.getAmountFactory().setCurrency(jdkCur.getCurrencyCode()).setNumber(10.15)
                                .create();
                assertNotNull("Amount factory returned null for new amount,m type: " + type.getName(), amount);
                assertNotNull("Amount factory returned new amount with null currency, type: " + type.getName(),
                              amount.getCurrency());
                assertEquals("Amount factory returned new amount with invalid currency, type: " + type.getName(),
                             jdkCur.getCurrencyCode(), amount.getCurrency().getCurrencyCode());
            }
        }
    }

    /**
     * For each MonetaryAmount implementation: Ensure getNumber()
     * returns correct results.
     */
    @SpecAssertion(section = "4.2.2", id = "422-A2")
    @Test
    public void testGetNumber(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
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

            for(int i = 0; i < moneys.length; i++){
                NumberValue nv = moneys[i].getNumber();
                assertNotNull("Amount returned returns null for getNumber(), type: " + moneys[i].getClass().getName(),
                              nv);
                assertEquals("getNumber().numberValue(BigDecimal.class) incorrect for " + type.getName(),
                             numbers[i].stripTrailingZeros(), nv.numberValue(BigDecimal.class).stripTrailingZeros());
                assertEquals("getNumber().intValue() incorrect for " + type.getName(), intNums[i], nv.intValue());
                assertEquals("getNumber().longValue() incorrect for " + type.getName(), longNums[i], nv.longValue());
                assertEquals("getNumber().doubleValue() incorrect for " + type.getName(), doubleNums[i],
                             nv.doubleValue(), 0.0d);
                assertEquals("getNumber().floatValue() incorrect for " + type.getName(), floatNums[i], nv.floatValue(),
                             0.0d);
            }
        }
    }

    /**
     * For each MonetaryAmount implementation: Ensure
     * getMonetaryContext() returns correct results.
     */
    @SpecAssertion(section = "4.2.2", id = "422-A3")
    @Test
    public void testGetMonetaryContext(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryContext defCtx = f.getDefaultMonetaryContext();
            MonetaryContext maxCtx = f.getMaximalMonetaryContext();
            MonetaryContext mc = f.setNumber(1).create().getMonetaryContext();
            assertEquals("Invalid MonetaryContext(amountType) for " + type.getName(), mc.getAmountType(), type);
            if(maxCtx.getPrecision() > 0){
                assertTrue("Invalid MonetaryContext(precision) for " + type.getName(),
                           mc.getPrecision() <= maxCtx.getPrecision());
            }
            if(maxCtx.getMaxScale() > 0){
                assertTrue("Invalid MonetaryContext(maxScale) for " + type.getName(),
                           mc.getMaxScale() <= maxCtx.getMaxScale());
            }
            assertEquals("Invalid MonetaryContext(amountType) for " + type.getName(),
                         f.setNumber(0.34746d).create().getMonetaryContext().getAmountType(), type);
            mc = f.setNumber(0).create().getMonetaryContext();
            assertEquals("Invalid MonetaryContext(amountType) for " + type.getName(), mc.getAmountType(), type);
            if(maxCtx.getPrecision() > 0){
                assertTrue("Invalid MonetaryContext(precision) for " + type.getName(),
                           mc.getPrecision() <= maxCtx.getPrecision());
            }
            if(maxCtx.getMaxScale() > 0){
                assertTrue("Invalid MonetaryContext(maxScale) for " + type.getName(),
                           mc.getMaxScale() <= maxCtx.getMaxScale());
            }
            assertEquals("Invalid MonetaryContext(amountType) for " + type.getName(),
                         f.setNumber(100034L).create().getMonetaryContext().getAmountType(), type);
            mc = f.setNumber(0).create().getMonetaryContext();
            assertEquals("Invalid MonetaryContext(amountType) for " + type.getName(), mc.getAmountType(), type);
            if(maxCtx.getPrecision() > 0){
                assertTrue("Invalid MonetaryContext(precision) for " + type.getName(),
                           mc.getPrecision() <= maxCtx.getPrecision());
            }
            if(maxCtx.getMaxScale() > 0){
                assertTrue("Invalid MonetaryContext(maxScale) for " + type.getName(),
                           mc.getMaxScale() <= maxCtx.getMaxScale());
            }
        }
    }

    /**
     * For each MonetaryAmount implementation: Ensure isNegative()
     * returns correct results.
     */
    @SpecAssertion(section = "4.2.2", id = "422-A4")
    @Test
    public void testIsNegative(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount[] moneys = new MonetaryAmount[]{f.setNumber(0).create(), f.setNumber(0.0).create(),
                    f.setNumber(BigDecimal.ZERO).create(), f.setNumber(new BigDecimal("0.00000000000000000")).create(),
                    f.setNumber(100).create(), f.setNumber(34242344).create(), f.setNumber(23123213.435).create()};
            for(MonetaryAmount m : moneys){
                assertFalse("Invalid isNegative (expected false) for " + type.getName(), m.isNegative());
            }
            moneys = new MonetaryAmount[]{f.setNumber(-100).create(), f.setNumber(-34242344).create(),
                    f.setNumber(-23123213.435).create()};
            for(MonetaryAmount m : moneys){
                assertTrue("Invalid isNegative (expected true) for " + type.getName(), m.isNegative());
            }
        }
    }

    /**
     * For each MonetaryAmount implementation: Ensure isPositive()
     * returns correct results.
     */
    @SpecAssertion(section = "4.2.2", id = "422-A5")
    @Test
    public void testIsPositive(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount[] moneys = new MonetaryAmount[]{f.setNumber(100).create(), f.setNumber(34242344).create(),
                    f.setNumber(23123213.435).create()};
            for(MonetaryAmount m : moneys){
                assertTrue("Invalid isPositive (expected true) for " + type.getName(), m.isPositive());
            }
            moneys = new MonetaryAmount[]{f.setNumber(0).create(), f.setNumber(0.0).create(),
                    f.setNumber(BigDecimal.ZERO).create(), f.setNumber(new BigDecimal("0.00000000000000000")).create(),
                    f.setNumber(-100).create(), f.setNumber(-34242344).create(), f.setNumber(-23123213.435).create()};
            for(MonetaryAmount m : moneys){
                assertFalse("Invalid isPositive (expected false) for " + type.getName(), m.isPositive());
            }
        }
    }


    /**
     * For each MonetaryAmount implementation: Ensure isZero()
     * returns correct results.
     */
    @SpecAssertion(section = "4.2.2", id = "422-A6")
    @Test
    public void testIsZero(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount[] moneys = new MonetaryAmount[]{f.setNumber(100).create(), f.setNumber(34242344).create(),
                    f.setNumber(23123213.435).create(), f.setNumber(-100).create(),
                    f.setNumber(-723527.36532).create()};
            for(MonetaryAmount m : moneys){
                assertFalse("Invalid isZero (expected false) for " + type.getName(), m.isZero());
            }
            moneys = new MonetaryAmount[]{f.setNumber(0).create(), f.setNumber(0.0).create(),
                    f.setNumber(BigDecimal.ZERO).create(), f.setNumber(new BigDecimal("0.00000000000000000")).create()};
            for(MonetaryAmount m : moneys){
                assertTrue("Invalid isZero (expected true) for " + type.getName(), m.isZero());
            }
        }
    }

    /**
     * For each MonetaryAmount implementation: Ensure isZero()
     * returns correct results (-0, +0 == 0).
     */
    @SpecAssertion(section = "4.2.2", id = "422-A6")
    @Test
    public void testIsZeroAdvanced(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount[] moneys =
                    new MonetaryAmount[]{f.setNumber(-0).create(), f.setNumber(0).create(), f.setNumber(-0.0f).create(),
                            f.setNumber(0.0f).create(), f.setNumber(-0.0d).create(), f.setNumber(0.0d).create()};
            for(MonetaryAmount m : moneys){
                assertTrue("Invalid isZero (expected true) for " + type.getName(), m.isZero());
            }
        }
    }

    /**
     * For each MonetaryAmount implementation: signum() function is
     * implemented correctly.
     */
    @SpecAssertion(section = "4.2.2", id = "422-A7")
    @Test
    public void testSignum(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount m = f.setNumber(100).create();
            assertEquals("Invalid signum of 100 for " + type.getName(), 1, m.signum());
            m = f.setNumber(-100).create();
            assertEquals("signum of -100 for " + type.getName(), -1, m.signum());
            m = f.setNumber(100.3435).create();
            assertEquals("signum of 100.3435 for " + type.getName(), 1, m.signum());
            m = f.setNumber(-100.3435).create();
            assertEquals("signum of -100.3435 for " + type.getName(), -1, m.signum());
            m = f.setNumber(0).create();
            assertEquals("signum of 0 for " + type.getName(), 0, m.signum());
            m = f.setNumber(-0).create();
            assertEquals("signum of - for " + type.getName(), 0, m.signum());
        }
    }

    /**
     * For each MonetaryAmount implementation: Ensure isNegativeOrZero()
     * returns correct results.
     */
    @SpecAssertion(section = "4.2.2", id = "422-A8")
    @Test
    public void testIsNegativeOrZero(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount[] moneys = new MonetaryAmount[]{f.setNumber(100).create(), f.setNumber(34242344).create(),
                    f.setNumber(23123213.435).create()};
            for(MonetaryAmount m : moneys){
                assertFalse("Invalid negativeOrZero (expected false) for " + type.getName(), m.isNegativeOrZero());
            }
            moneys = new MonetaryAmount[]{f.setNumber(0).create(), f.setNumber(0.0).create(),
                    f.setNumber(BigDecimal.ZERO).create(), f.setNumber(new BigDecimal("0.0000")).create(),
                    f.setNumber(-100).create(), f.setNumber(-34242344).create(), f.setNumber(-23123213.435).create()};
            for(MonetaryAmount m : moneys){
                assertTrue("Invalid negativeOrZero (expected true) for " + type.getName(), m.isNegativeOrZero());
            }
        }
    }

    /**
     * For each MonetaryAmount implementation: Ensure isPositiveOrZero()
     * returns correct results.
     */
    @SpecAssertion(section = "4.2.2", id = "422-A9")
    @Test
    public void testIsPositiveOrZero(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount[] moneys = new MonetaryAmount[]{f.setNumber(0).create(), f.setNumber(0.0).create(),
                    f.setNumber(BigDecimal.ZERO).create(), f.setNumber(new BigDecimal("0.00000000000000000")).create(),
                    f.setNumber(100).create(), f.setNumber(34242344).create(), f.setNumber(23123213.435).create()};
            for(MonetaryAmount m : moneys){
                assertTrue("Invalid positiveOrZero (expected true): for " + type.getName(), m.isPositiveOrZero());
            }
            moneys = new MonetaryAmount[]{f.setNumber(-100).create(), f.setNumber(-34242344).create(),
                    f.setNumber(-23123213.435).create()};
            for(MonetaryAmount m : moneys){
                assertFalse("Invalid positiveOrZero (expected false) for " + type.getName() + m, m.isPositiveOrZero());
            }
        }
    }


    // ********************* B. Prototyping Support *****************************

    /**
     * Ensure getFactory returns a MonetaryAmountFactory and that
     * instances created are of the same type.
     */
    @SpecAssertion(section = "4.2.2", id = "422-B1")
    @Test
    public void testMonetaryAmountFactories(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            assertNotNull(f);
            MonetaryAmount m = f.setCurrency("CHF").setNumber(10).create();
            assertEquals("Invalid class for created amount, expected: " + type.getName(), m.getClass(), type);
            m = f.setCurrency("CHF").setNumber(-10).create();
            assertEquals("Invalid class for created amount, expected: " + type.getName(), m.getClass(), type);
            m = f.setCurrency("CHF").setNumber(10.3).create();
            assertEquals("Invalid class for created amount, expected: " + type.getName(), m.getClass(), type);
            m = f.setCurrency("CHF").setNumber(-10.3).create();
            assertEquals("Invalid class for created amount, expected: " + type.getName(), m.getClass(), type);
            m = f.setCurrency("CHF").setNumber(0.0).create();
            assertEquals("Invalid class for created amount, expected: " + type.getName(), m.getClass(), type);
            m = f.setCurrency("CHF").setNumber(-0.0).create();
            assertEquals("Invalid class for created amount, expected: " + type.getName(), m.getClass(), type);
        }
    }

    /**
     * Call getFactory(), create a new MonetaryAmount instance, with
     * same input. The instances must
     * be equal (or even be identical!).
     */
    @SpecAssertion(section = "4.2.2", id = "422-B2")
    @Test
    public void testMonetaryAmountFactories_InstancesMustBeEqual(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(10).create();
            f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("CHF").setNumber(10).create();
            assertEquals("Expected equal instances for " + type.getName(), m1, m2);
        }

        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(10.5d).create();
            f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("CHF").setNumber(10.5d).create();
            assertEquals("Expected equal types created by same factory for " + type.getName(), m1, m2);
        }

        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(new BigDecimal("10.52")).create();
            f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("CHF").setNumber(new BigDecimal("10.52")).create();
            assertEquals("Expected equal types created by same factory for " + type.getName(), m1, m2);
        }
    }

    /**
     * Call getFactory(), create a new MonetaryAmount instance with a
     * new number
     * value. The instances must
     * be non equal and have the
     * according
     * numeric value.
     */
    @SpecAssertion(section = "4.2.2", id = "422-B3")
    @Test
    public void testMonetaryAmountFactories_InstantesMustBeNotEqual(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(10).create();
            f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("CHF").setNumber(11).create();
            assertNotSame("Expected non equal instances for " + type.getName(), m1, m2);
        }

        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(10.5d).create();
            f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("CHF").setNumber(10.4d).create();
            assertNotSame("Expected non equal instances for " + type.getName(), m1, m2);
        }

        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(new BigDecimal("10.52")).create();
            f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("CHF").setNumber(new BigDecimal("10.11")).create();
            assertNotSame("Expected non equal instances for " + type.getName(), m1, m2);
        }
    }

    /**
     * Call getFactory(),create a new MonetaryAmount instance
     * with a new currency value.The instances must
     * be non  equal and have the according currency value .Do this by passing a literal code
     * and by passing a CurrencyUnit.
     */
    @SpecAssertion(section = "4.2.2", id = "422-B4")
    @Test
    public void testMonetaryAmountFactories_CreateWithCurrencies(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(10).create();
            f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("USD").setNumber(10).create();
            assertNotSame("Expected non equal instances for " + type.getName(), m1, m2);
        }

        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(10.5d).create();
            f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("USD").setNumber(10.5d).create();
            assertNotSame("Expected non equal instances for " + type.getName(), m1, m2);
        }

        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(new BigDecimal("10.52")).create();
            f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("USD").setNumber(new BigDecimal("10.52")).create();
            assertNotSame("Expected non equal instances for " + type.getName(), m1, m2);
        }
    }

    /**
     * Call getFactory(),create a  new MonetaryAmount instance
     * with a new  monetary context(if possible-check the max context). The
     * instances must be non equal and have the same currency and number value.
     */
    @SpecAssertion(section = "4.2.2", id = "422-B5")
    @Test
    public void testMonetaryAmountFactories_CreateWithMonetaryContext(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryContext mc1 = f.getDefaultMonetaryContext();
            MonetaryContext mc2 = f.getMaximalMonetaryContext();
            MonetaryAmount m1 = null;
            MonetaryAmount m2 = null;
            if(mc1.equals(mc2)){
                // In this cases both amount must be equals
                m1 = f.setCurrency("CHF").setContext(mc1).setNumber(10).create();
                m2 = f.setCurrency("CHF").setContext(mc2).setNumber(10).create();
                assertNotSame("Expected non equal instances for " + type.getName(), m1, m2);
            }else{
                // In this cases both amount must be non equals
                m1 = f.setCurrency("CHF").setContext(mc1).setNumber(10).create();
                m2 = f.setCurrency("CHF").setContext(mc2).setNumber(10).create();
                assertNotSame("Expected non equal instances for " + type.getName(), m1, m2);
            }
            assertTrue("Expected equality for " + type.getName(), m1.equals(m1));
            assertTrue("Expected equality for " + type.getName(), m2.equals(m2));
        }
    }

    /**
     * Call getFactory(),create a new MonetaryAmount instance with a new monetary context, a
     * new number and a new currency. The instances must be non equal.
     */
    @SpecAssertion(section = "4.2.2", id = "422-B6")
    @Test
    public void testMonetaryAmountFactories_CreateWithMonetaryContextNumberAndCurrency(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryContext mc1 = f.getDefaultMonetaryContext();
            MonetaryContext mc2 = f.getMaximalMonetaryContext();
            MonetaryAmount m1 = f.setCurrency("CHF").setContext(mc1).setNumber(10).create();
            MonetaryAmount m2 = f.setCurrency("USD").setContext(mc2).setNumber(11).create();
            assertNotSame("Expected not same for " + type.getName(), m1, m2);
            assertTrue("Expected isEqualTo==true for " + type.getName(), m1.isEqualTo(m1));
            assertTrue("Expected isEqualTo==true for " + type.getName(), m2.isEqualTo(m2));
            assertTrue("Expected equality for " + type.getName(), m1.equals(m1));
            assertTrue("Expected equality for " + type.getName(), m2.equals(m2));
        }
    }

    // ***************************** C.Comparison Methods *********************************

    /**
     * Test isGreaterThan() is implemented correctly for each amount type regardless of trailing zeroes.
     */
    @SpecAssertion(section = "4.2.2", id = "422-C1")
    @Test
    public void testMonetaryAmount_isGreaterThan(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            assertFalse("isGreaterThan failed for " + type.getName(), f.setNumber(BigDecimal.valueOf(0d)).create()
                    .isGreaterThan(f.setNumber(BigDecimal.valueOf(0)).create()));
            assertTrue("isGreaterThan failed for " + type.getName(), f.setNumber(BigDecimal.valueOf(0.00001d)).create()
                               .isGreaterThan(f.setNumber(BigDecimal.valueOf(0d)).create())
            );
            assertTrue("isGreaterThan failed for " + type.getName(),
                       f.setNumber(15).create().isGreaterThan(f.setNumber(10).create()));
            assertTrue("isGreaterThan failed for " + type.getName(),
                       f.setNumber(15.546).create().isGreaterThan(f.setNumber(10.34).create()));
            assertFalse("isGreaterThan failed for " + type.getName(),
                        f.setNumber(5).create().isGreaterThan(f.setNumber(10).create()));
            assertFalse("isGreaterThan failed for " + type.getName(),
                        f.setNumber(5.546).create().isGreaterThan(f.setNumber(10.34).create()));
        }
    }

    /**
     * Test isGreaterThanOrEquals() is implemented correctly for each amount type regardless of trailing
     * zeroes.
     */
    @SpecAssertion(section = "4.2.2", id = "422-C2")
    @Test
    public void testMonetaryAmount_isGreaterThanOrEquals(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            assertTrue("isGreaterThanOrEqualTo failed for " + type.getName(),
                       f.setNumber(BigDecimal.valueOf(0d)).create()
                               .isGreaterThanOrEqualTo(f.setNumber(BigDecimal.valueOf(0)).create())
            );
            assertTrue("isGreaterThanOrEqualTo failed for " + type.getName(),
                       f.setNumber(BigDecimal.valueOf(0.00001d)).create()
                               .isGreaterThanOrEqualTo(f.setNumber(BigDecimal.valueOf(0d)).create())
            );
            assertTrue("isGreaterThanOrEqualTo failed for " + type.getName(),
                       f.setNumber(15).create().isGreaterThanOrEqualTo(f.setNumber(10).create()));
            assertTrue("isGreaterThanOrEqualTo failed for " + type.getName(),
                       f.setNumber(15.546).create().isGreaterThanOrEqualTo(f.setNumber(10.34).create()));
            assertFalse("isGreaterThanOrEqualTo failed for " + type.getName(),
                        f.setNumber(5).create().isGreaterThanOrEqualTo(f.setNumber(10).create()));
            assertFalse("isGreaterThanOrEqualTo failed for " + type.getName(),
                        f.setNumber(5.546).create().isGreaterThanOrEqualTo(f.setNumber(10.34).create()));
        }
    }

    /**
     * Test isLessThan() is implemented correctly for each amount type regardless of trailing
     * zeroes.
     */
    @SpecAssertion(section = "4.2.2", id = "422-C3")
    @Test
    public void testMonetaryAmount_isLessThan(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            assertFalse("isLessThan failed for " + type.getName(), f.setNumber(BigDecimal.valueOf(0d)).create()
                    .isLessThan(f.setNumber(BigDecimal.valueOf(0)).create()));
            assertFalse("isLessThan failed for " + type.getName(), f.setNumber(BigDecimal.valueOf(0.00001d)).create()
                                .isLessThan(f.setNumber(BigDecimal.valueOf(0d)).create())
            );
            assertFalse("isLessThan failed for " + type.getName(),
                        f.setNumber(15).create().isLessThan(f.setNumber(10).create()));
            assertFalse("isLessThan failed for " + type.getName(),
                        f.setNumber(15.546).create().isLessThan(f.setNumber(10.34).create()));
            assertTrue("isLessThan failed for " + type.getName(),
                       f.setNumber(5).create().isLessThan(f.setNumber(10).create()));
            assertTrue("isLessThan failed for " + type.getName(),
                       f.setNumber(5.546).create().isLessThan(f.setNumber(10.34).create()));
        }
    }

    /**
     * Test isLessThanOrEquals() is implemented correctly for each amount type regardless of trailing
     * zeroes.
     */
    @SpecAssertion(section = "4.2.2", id = "422-C4")
    @Test
    public void testMonetaryAmount_isLessThanOrEqualTo(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            assertTrue("isLessThanOrEqualTo failed for " + type.getName(), f.setNumber(BigDecimal.valueOf(0d)).create()
                    .isLessThanOrEqualTo(f.setNumber(BigDecimal.valueOf(0)).create()));
            assertFalse("isLessThanOrEqualTo failed for " + type.getName(),
                        f.setNumber(BigDecimal.valueOf(0.00001d)).create()
                                .isLessThanOrEqualTo(f.setNumber(BigDecimal.valueOf(0d)).create())
            );
            assertFalse("isLessThanOrEqualTo failed for " + type.getName(),
                        f.setNumber(15).create().isLessThanOrEqualTo(f.setNumber(10).create()));
            assertFalse("isLessThan failed for " + type.getName(),
                        f.setNumber(15.546).create().isLessThan(f.setNumber(10.34).create()));
            assertTrue("isLessThanOrEqualTo failed for " + type.getName(),
                       f.setNumber(5).create().isLessThanOrEqualTo(f.setNumber(10).create()));
            assertTrue("isLessThanOrEqualTo failed for " + type.getName(),
                       f.setNumber(5.546).create().isLessThanOrEqualTo(f.setNumber(10.34).create()));
        }
    }

    /**
     * Test isEqualTo() is implemented correctly for each amount type regardless of trailing
     * zeroes.
     */
    @SpecAssertion(section = "4.2.2", id = "422-C5")
    @Test
    public void testMonetaryAmount_isEqualTo(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            assertTrue("isEqualTo failed for " + type.getName(), f.setNumber(BigDecimal.valueOf(0d)).create()
                    .isEqualTo(f.setNumber(BigDecimal.valueOf(0)).create()));
            assertFalse("isEqualTo failed for " + type.getName(), f.setNumber(BigDecimal.valueOf(0.00001d)).create()
                                .isEqualTo(f.setNumber(BigDecimal.valueOf(0d)).create())
            );
            assertTrue("isEqualTo failed for " + type.getName(), f.setNumber(BigDecimal.valueOf(5d)).create()
                    .isEqualTo(f.setNumber(BigDecimal.valueOf(5)).create()));
            assertTrue("isEqualTo failed for " + type.getName(), f.setNumber(BigDecimal.valueOf(1d)).create()
                    .isEqualTo(f.setNumber(BigDecimal.valueOf(1.00)).create()));
            assertTrue("isEqualTo failed for " + type.getName(),
                       f.setNumber(BigDecimal.valueOf(1d)).create().isEqualTo(f.setNumber(BigDecimal.ONE).create()));
            assertTrue("isEqualTo failed for " + type.getName(),
                       f.setNumber(BigDecimal.valueOf(1)).create().isEqualTo(f.setNumber(BigDecimal.ONE).create()));
            assertTrue("isEqualTo failed for " + type.getName(), f.setNumber(new BigDecimal("1.0000")).create()
                    .isEqualTo(f.setNumber(new BigDecimal("1.00")).create()));
            // Test with different scales, but numeric equal values
            assertTrue("isEqualTo failed for " + type.getName(), f.setNumber(BigDecimal.valueOf(0d)).create()
                    .isEqualTo(f.setNumber(BigDecimal.valueOf(0)).create()));
            assertTrue("isEqualTo failed for " + type.getName(),
                       f.setNumber(BigDecimal.ZERO).create().isEqualTo(f.setNumber(BigDecimal.valueOf(0)).create()));
            assertTrue("isEqualTo failed for " + type.getName(), f.setNumber(BigDecimal.valueOf(5)).create()
                    .isEqualTo(f.setNumber(new BigDecimal("5.0")).create()));
            assertTrue("isEqualTo failed for " + type.getName(), f.setNumber(BigDecimal.valueOf(5)).create()
                    .isEqualTo(f.setNumber(new BigDecimal("5.00")).create()));
            assertTrue("isEqualTo failed for " + type.getName(), f.setNumber(BigDecimal.valueOf(5)).create()
                    .isEqualTo(f.setNumber(new BigDecimal("5.000")).create()));
            assertTrue("isEqualTo failed for " + type.getName(), f.setNumber(BigDecimal.valueOf(5)).create()
                    .isEqualTo(f.setNumber(new BigDecimal("5.0000")).create()));
            assertTrue("isEqualTo failed for " + type.getName(), f.setNumber(new BigDecimal("-1.23")).create()
                    .isEqualTo(f.setNumber(new BigDecimal("-1.23")).create()));
            try{
                assertTrue("isEqualTo failed for " + type.getName(), f.setNumber(new BigDecimal("-1.23")).create()
                        .isEqualTo(f.setNumber(new BigDecimal("-1.230")).create()));
                assertTrue("isEqualTo failed for " + type.getName(), f.setNumber(new BigDecimal("-1.23")).create()
                        .isEqualTo(f.setNumber(new BigDecimal("-1.2300")).create()));
                assertTrue("isEqualTo failed for " + type.getName(), f.setNumber(new BigDecimal("-1.23")).create()
                        .isEqualTo(f.setNumber(new BigDecimal("-1.23000")).create()));
                assertTrue("isEqualTo failed for " + type.getName(), f.setNumber(new BigDecimal("-1.23")).create()
                        .isEqualTo(f.setNumber(new BigDecimal("-1.230000000000000000000")).create()));
            }
            catch(MonetaryException e){
                // happens if we exceed the limits...
            }
        }
    }

    /**
     * For two amounts with same numeric value and currency:
     * {@code }isEqualTo()} return true, regardless of MonetaryContext.
     */
    @SpecAssertion(section = "4.2.2", id = "422-C6")
    @Test
    public void testMonetaryAmount_isEqualToRegardlessMonetaryContext(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryContext mc1 = f.getDefaultMonetaryContext();
            MonetaryContext mc2 = f.getMaximalMonetaryContext();
            MonetaryAmount m1 = null;
            MonetaryAmount m2 = null;
            if(mc1.equals(mc2)){
                // In this cases both amount must be equals
                m1 = f.setCurrency("CHF").setContext(mc1).setNumber(10).create();
                m2 = f.setCurrency("CHF").setContext(mc2).setNumber(10).create();
                assertEquals(m1, m2);
                m1 = f.setCurrency("CHF").setContext(mc1).setNumber(10.5d).create();
                m2 = f.setCurrency("CHF").setContext(mc2).setNumber(10.5d).create();
                assertEquals(m1, m2);
                m1 = f.setCurrency("CHF").setContext(mc1).setNumber(BigDecimal.TEN).create();
                m2 = f.setCurrency("CHF").setContext(mc2).setNumber(BigDecimal.TEN).create();
                assertEquals(m1, m2);
            }else{
                // In this cases both amount must be non equals
                m1 = f.setCurrency("CHF").setContext(mc1).setNumber(10).create();
                m2 = f.setCurrency("CHF").setContext(mc2).setNumber(10).create();
                assertNotSame(m1, m2);
                assertTrue(m1.isEqualTo(m2));
                assertTrue(m2.isEqualTo(m1));
                m1 = f.setCurrency("CHF").setContext(mc1).setNumber(10.5d).create();
                m2 = f.setCurrency("CHF").setContext(mc2).setNumber(10.5d).create();
                assertNotSame(m1, m2);
                assertTrue(m1.isEqualTo(m2));
                assertTrue(m2.isEqualTo(m1));
                m1 = f.setCurrency("CHF").setContext(mc1).setNumber(BigDecimal.TEN).create();
                m2 = f.setCurrency("CHF").setContext(mc2).setNumber(BigDecimal.TEN).create();
                assertNotSame(m1, m2);
                assertTrue(m1.isEqualTo(m2));
                assertTrue(m2.isEqualTo(m1));
            }
            assertTrue(m1.isEqualTo(m1));
            assertTrue(m2.isEqualTo(m2));
        }
    }

    /**
     * For two amounts with same numeric value and currency:
     * {@code }isEqualTo()} return true, regardless of iumplementation type.
     */
    @SpecAssertion(section = "4.2.2", id = "422-C7")
    @Test
    public void testMonetaryAmount_isEqualToRegardlessType(){
        List<MonetaryAmount> instances = new ArrayList<>();
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            instances.add(f.setNumber(10).create());
            instances.add(f.setNumber(10.0d).create());
            instances.add(f.setNumber(BigDecimal.TEN).create());
        }
        // compare each other...
        for(int i = 0; i < instances.size(); i++){
            for(int j = 0; j < instances.size(); j++){
                MonetaryAmount mi = instances.get(i);
                MonetaryAmount mj = instances.get(j);
                assertTrue(mi.isEqualTo(mj));
            }
        }
    }

    /**
     * Tests that add() correctly adds two values, using positive integers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D1")
    @Test
    public void testAddPositiveIntegers(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(20).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(30).create();
            assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that add() correctly adds two values, using negative integers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D1")
    @Test
    public void testAddNegativeIntegers(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-20).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-30).create();
            assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that add() correctly adds two values, using fractions.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D1")
    @Test
    public void testAddPositiveFractions(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(1.5).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(2.85).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(4.35).create();
            assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that add() correctly adds two values, using positive and negative integers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D1")
    @Test
    public void testAddMixedIntegers(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(20).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-20).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10).create();
            assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create();
            assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that add() correctly adds two values, using positive and negative fractions.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D1")
    @Test
    public void testAddMixedFractions(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(1.5).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-2.85).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-1.35).create();
            assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-1.5).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(+2.85).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(1.35).create();
            assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-2.85).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(+2.85).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create();
            assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that add() with non matching currencies throws a
     * MonetaryException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D2")
    @Test
    public void testAdd_IncompatibleCurrencies(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(ADDITIONAL_CURRENCY).setNumber(20).create();
            try{
                MonetaryAmount mActualResult = mAmount1.add(mAmount2);
                fail("Exception expected");
            }
            catch(MonetaryException ex){
                // Expected
            }
        }
    }

    /**
     * Tests that add(0) should return itself.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D3")
    @Test
    public void testAdd_Zero(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            assertEquals("Adding zero", mAmount1, mActualResult);
        }
    }

    /**
     * Tests that add(), which results in an amount exceeding the max MonetaryContext throws
     * a MonetaryException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D4")
    @Test
    public void testAdd_ExceedsCapabilities(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryContext maxCtx = f.getMaximalMonetaryContext();
            if(maxCtx.getPrecision() > 0){
                MonetaryAmount m = f.setNumber(f.getMaxNumber()).create();
                MonetaryAmount ms = m;
                try{
                    for(int i = 0; i < 20; i++){
                        ms = ms.add(ms);
                    }
                    fail("Exception expected, since adding 20x " + m + " to " + m +
                                 " exceeds capabilities (precision) for " +
                                 type.getName());
                }
                catch(MonetaryException ex){
                    // Expected
                }
            }
        }
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryContext maxCtx = f.getMaximalMonetaryContext();
            if(maxCtx.getMaxScale() >= 0){
                try{
                    MonetaryAmount m = f.setNumber(1).create();
                    MonetaryAmount m2 =
                            f.setNumber(TestUtils.createNumberWithScale(f, maxCtx.getMaxScale() + 5)).create();
                    m.add(m2);
                    fail("Exception expected, since adding " + m2 + " to " + m + " exceeds capabilities (scale) for " +
                                 type.getName());
                }
                catch(MonetaryException ex){
                    // Expected
                }
            }
        }
    }

    private BigDecimal createNumberWithScale(MonetaryAmountFactory f, int scale){
        StringBuilder b = new StringBuilder(scale + 2);
        b.append("1.");
        for(int i = 0; i < scale; i++){
            b.append(String.valueOf(i % 10));
        }
        return new BigDecimal(b.toString(), MathContext.UNLIMITED);
    }


    /**
     * Tests that add(), which results in an amount exceeding the max MonetaryContext throws
     * a MonetaryException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D5")
    @Test
    public void testAdd_Null(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try{
                MonetaryAmount mActualResult = mAmount1.add(null);
                fail("Exception expected");
            }
            catch(NullPointerException ex){
                // Expected
            }
        }
    }


    /**
     * Tests that subtract() correctly adds two values, using positive integers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D6")
    @Test
    public void testSubtractPositiveIntegers(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(20).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10).create();
            assertEquals("Subtracting two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that subtract() correctly adds two values, using negative integers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D6")
    @Test
    public void testSubtractNegativeIntegers(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-20).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            assertEquals("Subtracting two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that subtract() correctly adds two values, using fractions.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D6")
    @Test
    public void testSubtractPositiveFractions(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(1.5).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(2.85).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-1.35).create();
            assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that subtract() correctly adds two values, using positive and negative integers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D6")
    @Test
    public void testSubtractMixedIntegers(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(20).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create();
            assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-20).create();
            assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that subtract() correctly adds two values, using positive and negative fractions.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D6")
    @Test
    public void testSubtractMixedFractions(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(1.5).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-2.85).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(4.35).create();
            assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-1.5).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(+2.85).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-4.35).create();
            assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(2.85).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(+2.85).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create();
            assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that subtract() with non matching currencies throws a
     * MonetaryException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D8")
    @Test
    public void testSubtract_IncompatibleCurrencies(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(ADDITIONAL_CURRENCY).setNumber(20).create();
            try{
                MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
                fail("Exception expected");
            }
            catch(MonetaryException ex){
                // Expected
            }
        }
    }

    /**
     * Tests that subtract(0) should return itself.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D7")
    @Test
    public void testSubtract_Zero(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            assertEquals("Subtract zero", mAmount1, mActualResult);
        }
    }

    /**
     * Tests that subtract(), which results in an amount exceeding the max MonetaryContext throws
     * a MonetaryException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D9")
    @Test
    public void testSubtract_ExceedsCapabilities(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryContext maxCtx = f.getMaximalMonetaryContext();
            MonetaryAmount m = null;
            if(maxCtx.getPrecision() > 0){
                MonetaryAmount mAmount1 = f.setNumber(f.getMinNumber()).create().negate();
                m = TestUtils.createAmountWithPrecision(maxCtx.getPrecision() + 1);
                try{
                    mAmount1 = mAmount1.subtract(m);
                    fail("Exception expected on subtraction that exceeds capabilities for " + type.getName());
                }
                catch(MonetaryException ex){
                    // Expected
                }
            }
        }

        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount mAmount1 = f.setNumber(0).create();
            MonetaryContext maxCtx = f.getMaximalMonetaryContext();
            MonetaryAmount m = null;
            if(maxCtx.getMaxScale() >= 0){
                m = TestUtils.createAmountWithScale(maxCtx.getMaxScale() + 1);
            }
            if(m != null){
                try{
                    mAmount1.subtract(m);
                    fail("Exception expected on subtraction that exceeds capabilities for " + type.getName());
                }
                catch(MonetaryException ex){
                    // Expected
                }
            }
        }
    }

    /**
     * Tests that subtract(), which results in an amount exceeding the max MonetaryContext throws
     * a MonetaryException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D10")
    @Test
    public void testSubtract_Null(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try{
                MonetaryAmount mActualResult = mAmount1.subtract(null);
                fail("Exception expected");
            }
            catch(NullPointerException ex){
                // Expected
            }
        }
    }

    /**
     * Test multiply() allow to multiply numbers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D11")
    @Test
    public void testMultiply_Integral(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.multiply(2L);
            assertTrue("Multiplication with 2 does not return correct value for " + type.getName(),
                       mActualResult.getNumber().longValueExact() == 20);
            mActualResult = mAmount1.multiply(Double.valueOf(-3));
            assertTrue("Multiplication with -3 does not return correct value for " + type.getName(),
                       mActualResult.getNumber().longValueExact() == -30);
            mActualResult = mAmount1.multiply(BigDecimal.ONE);
            assertTrue("Multiplication with 1 does not return correct value for " + type.getName(),
                       mActualResult.getNumber().longValueExact() == 10);
            assertTrue("Multiplication with 1 does not return identity value for " + type.getName(),
                       mActualResult == mAmount1);
            mActualResult = mAmount1.multiply(BigDecimal.ZERO);
            assertTrue("Multiplication with 0 does not return correct value for " + type.getName(),
                       mActualResult.getNumber().longValueExact() == 0);
        }
    }

    /**
     * Test multiply() allow to multiply numbers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D11")
    @Test
    public void testMultiply_Decimals(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.multiply(1.5d);
            assertTrue("Multiplication with 1.5 does not return correct value for " + type.getName(),
                       mActualResult.getNumber().longValueExact() == 15);
            mActualResult = mAmount1.multiply(Double.valueOf(-1.5));
            assertTrue("Multiplication with -3 does not return correct value for " + type.getName(),
                       mActualResult.getNumber().longValueExact() == -15);
            mActualResult = mAmount1.multiply(0.0);
            assertTrue("Multiplication with 0 does not return correct value for " + type.getName(),
                       mActualResult.getNumber().longValueExact() == 0);
            mActualResult = mAmount1.multiply(1.0);
            assertTrue("Multiplication with 0 does not return correct value for " + type.getName(),
                       mActualResult.getNumber().longValueExact() == 10);
            assertTrue("Multiplication with 0 does not return identity value for " + type.getName(),
                       mActualResult == mAmount1);
        }
    }

    /**
     * Test multiply(1) returns this.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D12")
    @Test
    public void testMultiplyOne(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.multiply(1);
            assertTrue("Multiplication with 1 does not return identity value for " + type.getName(),
                       mActualResult == mAmount1);
            mActualResult = mAmount1.multiply(1.0);
            assertTrue("Multiplication with 1 does not return identity value for " + type.getName(),
                       mActualResult == mAmount1);
            mActualResult = mAmount1.multiply(BigDecimal.ONE);
            assertTrue("Multiplication with 1 does not return identity value for " + type.getName(),
                       mActualResult == mAmount1);
        }
    }


    /**
     * Test multiply, which results in an amount exceeding the max
     * MonetaryContext must throw a
     * MonetaryException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D13")
    @Test
    public void testMultiplyExceedsCapabilities(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<?> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryContext ctx = f.getMaximalMonetaryContext();
            if(ctx.getMaxScale() >= 0){
                BigDecimal num = TestUtils.createNumberWithScale(f, ctx.getMaxScale() + 5);
                MonetaryAmount m = f.setNumber(10).setCurrency("USD").create();
                try{
                    m.multiply(num);
                    fail("Multiplication of amount 10 with " + num + " exceeds max monetary context (scale), " +
                                 "but did not throw an ArithmeticException, type was " +
                                 type);
                }
                catch(MonetaryException e){
                    // OK
                }
                catch(Exception e){
                    fail("Multiplication of amount 10 with " + num + " exceeds max monetary context(scale), " +
                                 "but did not throw an ArithmeticException (exception thrown was " +
                                 e.getClass().getName() + "), type was " +
                                 type);
                }
            }
        }
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<?> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryContext ctx = f.getMaximalMonetaryContext();
            if(ctx.getPrecision() > 0){
                BigDecimal num = TestUtils.createNumberWithPrecision(f, ctx.getPrecision() + 5);
                MonetaryAmount m = f.setNumber(10).setCurrency("USD").create();
                try{
                    m.multiply(num);
                    fail("Multiplication of amount " + num +
                                 " with 10000000 exceeds max monetary context(precision), " +
                                 "but did not throw an ArithmeticException, type was " +
                                 type.getName());
                }
                catch(MonetaryException e){
                    // OK
                }
                catch(Exception e){
                    fail("Multiplication of amount " + num +
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
    @Test
    public void testMultiplyNull(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try{
                MonetaryAmount mActualResult = mAmount1.multiply(null);
                fail("NullPointerException expected for multiplication with null, type was " + type.getName());
            }
            catch(NullPointerException e){
                // expected
            }
        }
    }

    /**
     * Test divide() function allow to divide numbers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D15")
    @Test
    public void testDivide(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount m =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount m2 = m.divide(10);
            assertEquals("Currency not equal after division, type was " + type.getName(), DEFAULT_CURRENCY,
                         m2.getCurrency().getCurrencyCode());
            assertEquals("Division result is not correct for " + type.getName(), 1, m2.getNumber().longValueExact());
            // TODO iterate over array of different numbers and divisors, use BD to check results
        }
    }

    /**
     * Test divideToIntegralValue() function allow to divide numbers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D15")
    @Test
    public void testDivideToIntegralValue(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            CurrencyUnit euro = MonetaryCurrencies.getCurrency("EUR");
            MonetaryAmount money1 =
                    MonetaryAmounts.getAmountFactory(type).setNumber(BigDecimal.ONE).setCurrency(euro).create();
            MonetaryAmount result = money1.divideToIntegralValue(new BigDecimal("0.5001"));
            assertEquals("divideToIntegralValue returned incorrect result for " + type.getName(),
                         result.getNumber().numberValue(BigDecimal.class).stripTrailingZeros(),
                         new BigDecimal("1.0").stripTrailingZeros());
            result = money1.divideToIntegralValue(new BigDecimal("0.2001"));
            assertEquals("divideToIntegralValue returned incorrect result for " + type.getName(),
                         result.getNumber().numberValue(BigDecimal.class).stripTrailingZeros(),
                         new BigDecimal("4.0").stripTrailingZeros());
            result = money1.divideToIntegralValue(new BigDecimal("5.0"));
            assertTrue("divideToIntegralValue returned incorrect result for " + type.getName(),
                       result.getNumber().numberValue(BigDecimal.class).intValueExact() == 0);
        }
    }

    /**
     * Test divide(0) function must throw an ArithmeticException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D16")
    @Test
    public void testDivideZero(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try{
                MonetaryAmount mActualResult = mAmount1.divide(0);
                fail("ArithmeticException expected on division by 0, type was " + type.getName());
            }
            catch(ArithmeticException ex){
                // expected
            }
        }
    }

    /**
     * Test divide(1) should return this.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D17")
    @Test
    public void testDivideOne(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.divide(1);
            assertTrue("Division by 1 does not return identity value for " + type.getName(), mActualResult == mAmount1);
            mActualResult = mAmount1.divide(1.0);
            assertTrue("Division by 1 does not return identity value for " + type.getName(), mActualResult == mAmount1);
            mActualResult = mAmount1.divide(BigDecimal.ONE);
            assertTrue("Division by 1 does not return identity value for " + type.getName(), mActualResult == mAmount1);
        }
    }

    /**
     * Test  divide(null)must throw a NullPointerException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D18")
    @Test
    public void testDivideNull(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try{
                MonetaryAmount mActualResult = mAmount1.divide(null);
                fail("NullPointerException expected for division by null, type was " + type.getName());
            }
            catch(NullPointerException e){
                // expected
            }
        }
    }

    /**
     * Test  remainder()allow to calculate the remainder.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D19")
    @Test
    public void testRemainder(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<?> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount[] moneys = new MonetaryAmount[]{f.setNumber(100).create(), f.setNumber(34242344).create(),
                    f.setNumber(23123213.435).create(), f.setNumber(0).create(), f.setNumber(-100).create(),
                    f.setNumber(-723527.36532).create()};
            for(MonetaryAmount m : moneys){
                assertEquals("Invalid remainder of " + 10.50 + " for " + type.getName(), m.getFactory().setNumber(
                                     m.getNumber().numberValue(BigDecimal.class).remainder(BigDecimal.valueOf(10.50)))
                                     .create(), m.remainder(10.50)
                );
                assertEquals("Invalid remainder of " + -30.20 + " for " + type.getName(), m.getFactory().setNumber(
                                     m.getNumber().numberValue(BigDecimal.class).remainder(BigDecimal.valueOf(-30.20)))
                                     .create(), m.remainder(-30.20)
                );
                assertEquals("Invalid remainder of " + -3 + " for " + type.getName(), m.getFactory().setNumber(
                                     m.getNumber().numberValue(BigDecimal.class).remainder(BigDecimal.valueOf(-3)))
                                     .create(), m.remainder(-3)
                );
                assertEquals("Invalid remainder of " + 3 + " for " + type.getName(), m.getFactory().setNumber(
                                     m.getNumber().numberValue(BigDecimal.class).remainder(BigDecimal.valueOf(3)))
                                     .create(), m.remainder(3)
                );
            }
        }
    }

    /**
     * Test remainder(0) must throw an ArithmeticException
     */
    @SpecAssertion(section = "4.2.2", id = "422-D20")
    @Test
    public void testRemainderZero_Double(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount m =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try{
                m.remainder(0.0d);
                fail("remainder(0) did not throw an ArithmeticException for " + type.getName());
            }
            catch(ArithmeticException e){
                // OK, ignore
            }
            catch(Exception e){
                fail("remainder(0.0d) did not throw an ArithmeticException for " + type.getName() + ", but " + e);
            }
        }
    }

    /**
     * Test remainder(0) must throw an ArithmeticException
     */
    @SpecAssertion(section = "4.2.2", id = "422-D20")
    @Test
    public void testRemainderZero_Long(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount m =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try{
                m.remainder(0L);
                fail("remainder(0L) did not throw an ArithmeticException for " + type.getName());
            }
            catch(ArithmeticException e){
                // OK, ignore
            }
            catch(Exception e){
                fail("remainder(0L) did not throw an ArithmeticException for " + type.getName() + ", but " + e);
            }
        }
    }

    /**
     * Test remainder(0) must throw an ArithmeticException
     */
    @SpecAssertion(section = "4.2.2", id = "422-D20")
    @Test
    public void testRemainderZero_Number(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount m =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try{
                m.remainder(BigDecimal.ZERO);
                fail("remainder(BigDecimal.ZERO) did not throw an ArithmeticException for " + type.getName());
            }
            catch(ArithmeticException e){
                // OK, ignore
            }
            catch(Exception e){
                fail("remainder(BigDecimal.ZERO) did not throw an ArithmeticException for " + type.getName() +
                             ", but " + e);
            }
        }
    }

    /**
     * Test remainder(null) must throw a NullPointerException
     */
    @SpecAssertion(section = "4.2.2", id = "422-D21")
    @Test
    public void testRemainderNull(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try{
                MonetaryAmount mActualResult = mAmount1.remainder(null);
                fail("NullPointerException expected for remainder with null, type was " + type.getName());
            }
            catch(NullPointerException e){
                // expected
            }
        }
    }

    /**
     * Test  divideAndRemainder()allow to divide/remind numbers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D22")
    @Test
    public void testDivideAndRemainder(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<?> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount money1 = f.setNumber(BigDecimal.ONE).setCurrency("EUR").create();
            if(f.getDefaultMonetaryContext().getMaxScale() < 5){
                MonetaryAmount[] divideAndRemainder = money1.divideAndRemainder(new BigDecimal("0.6"));
                assertEquals("divideAndRemainder(0.6)[0] failed for " + type.getName(),
                             divideAndRemainder[0].getNumber().numberValue(BigDecimal.class).stripTrailingZeros(),
                             BigDecimal.ONE.stripTrailingZeros());
                assertEquals("divideAndRemainder(0.6)[1] failed for " + type.getName(),
                             divideAndRemainder[1].getNumber().numberValue(BigDecimal.class).stripTrailingZeros(),
                             new BigDecimal("0.4").stripTrailingZeros());
            }else{
                MonetaryAmount[] divideAndRemainder = money1.divideAndRemainder(new BigDecimal("0.50001"));
                assertEquals("divideAndRemainder(0.50001)[0] failed for " + type.getName(),
                             divideAndRemainder[0].getNumber().numberValue(BigDecimal.class).stripTrailingZeros(),
                             BigDecimal.ONE.stripTrailingZeros());
                assertEquals("divideAndRemainder(0.50001)[1] failed for " + type.getName(),
                             divideAndRemainder[1].getNumber().numberValue(BigDecimal.class).stripTrailingZeros(),
                             new BigDecimal("0.49999").stripTrailingZeros());
            }
        }
    }

    /**
     * Test  divideAndRemainder(0) throws an ArithmeticException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D23")
    @Test
    public void testDivideAndRemainderZero(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try{
                mAmount1.divideAndRemainder(BigDecimal.ZERO);
                fail("divideAndRemainder(0) for " + type.getName() + ", should throw ArithmeticException!");
            }
            catch(ArithmeticException e){
                // expected
            }
            catch(Exception e){
                fail("Unexpected exception for divideAndRemainder(0) for " + type.getName() +
                             ", should be ArithmeticException, but was " + e);
            }
        }
    }

    /**
     * Test  divideAndRemainder(null) throws an NullPointerException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D24")
    @Test
    public void testDivideAndRemainderNull(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try{
                MonetaryAmount[] mActualResult = mAmount1.divideAndRemainder(null);
                fail("NullPointerException expected for divideAndRemainder with null, type was " + type.getName());
            }
            catch(NullPointerException e){
                // expected
            }
        }
    }

    /**
     * Test  divideAndRemainder(1) returns this/ZERO.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D25")
    @Test
    public void testDivideAndRemainderOne(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<?> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m = f.setNumber(100).setCurrency("CHF").create();
            assertEquals("DivideAndRemainder not returning correct result for type: " + type.getName(),
                         BigDecimal.valueOf(33),
                         m.divideAndRemainder(3)[0].getNumber().numberValue(BigDecimal.class).stripTrailingZeros());
            assertEquals("DivideAndRemainder not returning correct result for type: " + type.getName(),
                         BigDecimal.valueOf(1),
                         m.divideAndRemainder(3)[1].getNumber().numberValue(BigDecimal.class).stripTrailingZeros());
            assertEquals("DivideAndRemainder not returning correct result for type: " + type.getName(), BigDecimal.ONE,
                         m.divideAndRemainder(BigDecimal.valueOf(3))[1].getNumber().numberValue(BigDecimal.class)
                                 .stripTrailingZeros()
            );
        }
    }

    /**
     * Test scaleByPowerOfTen()allow to scale by power of 10.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D26")
    @Test
    public void testScaleByPowerOfTen(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<?> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount[] amounts = new MonetaryAmount[]{f.setNumber(100).create(), f.setNumber(342444).create(),
                    f.setNumber(2312213.435).create(), f.setNumber(BigDecimal.ZERO).create(),
                    f.setNumber(-100).create(), f.setNumber(-723527.3653).create()};

            for(MonetaryAmount m : amounts){
                for(int p = -3; p < 3; p++){
                    BigDecimal bdExpected = m.scaleByPowerOfTen(p).getNumber().numberValue(BigDecimal.class);
                    BigDecimal bdCalculated = m.getNumber().numberValue(BigDecimal.class).scaleByPowerOfTen(p);
                    if(bdExpected.signum() == 0){
                        assertTrue("Invalid " + m + " -> scaleByPowerOfTen(" + p + ") for " + type.getName(),
                                   bdCalculated.signum() == 0);
                    }else{
                        assertEquals("Invalid " + m + " -> scaleByPowerOfTen(" + p + ") for " + type.getName(),
                                     bdExpected.stripTrailingZeros(), bdCalculated.stripTrailingZeros());
                    }
                }
            }
        }
    }

    /**
     * Test abs() for getting the absolute value.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D27")
    @Test
    public void testAbsolute(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount m = f.setNumber(10).create();
            assertEquals("abs(m) !equals m, if m > 0 for type: " + type.getName(), m, m.abs());
            assertTrue("abs(m) != m, if m > 0 for type: " + type.getName(), m == m.abs());
            m = f.setNumber(0).create();
            assertEquals("abs(m) != equals, if m == 0 for type: " + type.getName(), m, m.abs());
            assertTrue("abs(m) != m, if m == 0 for type: " + type.getName(), m == m.abs());
            m = f.setNumber(-10).create();
            assertEquals("abs(m) == m, if m < 0 for type: " + type.getName(), m.negate(), m.abs());
            assertTrue("abs(m) == m, if m < 0 for type: " + type.getName(), m != m.abs());
        }
    }

    /**
     * Test negate() for negating a value.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D28")
    @Test
    public void testNegate(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount m = f.setNumber(100).create();
            assertEquals("negate(-x) failed for " + type.getName(), f.setNumber(-100).create(), m.negate());
            m = f.setNumber(-123.234).create();
            assertEquals("negate(+x) failed for " + type.getName(), f.setNumber(123.234).create(), m.negate());
        }
    }

    /**
     * Ensure with(MonetaryOperator) can be called and produces
     * amounts of the same type and correct value.
     */
    @SpecAssertion(section = "4.2.2", id = "422-E1")
    @Test
    public void testWith(){
        MonetaryOperator op = (amount) -> amount;
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount amount = MonetaryAmounts.getAmountFactory(type).setCurrency("CHF").setNumber(10).create();
            MonetaryAmount amount2 = amount.with(op);
            assertTrue("MonetaryAmount returned from operator is wrapped by implementation of type: " + type.getName(),
                       amount == amount2);
            final MonetaryAmount result =
                    MonetaryAmounts.getAmountFactory(type).setCurrency("CHF").setNumber(4).create();
            MonetaryOperator op2 = (m) -> result;
            amount2 = amount.with(op);
            assertTrue("MonetaryAmount returned from operator is wrapped by implementation of type: " + type.getName(),
                       amount == amount2);
        }
    }

    /**
     * Test with(m) throws a MonetaryException, if m throws any exception.
     */
    @SpecAssertion(section = "4.2.2", id = "422-E2")
    @Test
    public void testWithInvalidOperator(){
        MonetaryOperator op = new MonetaryOperator(){
            @Override
            public MonetaryAmount apply(MonetaryAmount value){
                throw new IllegalStateException();
            }
        };
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory factory = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount amount = factory.setCurrency("XXX").setNumber(1).create();
            try{
                amount.with(op);
                fail("MonetaryException expected as operator fails, type was " + type.getName());
            }
            catch(MonetaryException e){
                // OK, everything else makes the test fail!
            }
        }
    }

    /**
     * Test with(null) throws a NullPointerException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-E2")
    @Test
    public void testWithNull(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmountFactory factory = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount amount = factory.setCurrency("XXX").setNumber(1).create();
            try{
                amount.with(null);
                fail("NullPointerException expected as operator applied is null, type was " + type.getName());
            }
            catch(NullPointerException e){
                // OK, everything else makes the test fail!
            }
        }
    }

    /**
     * Ensure query(MonetaryQUery) can be called and produces
     * valuable results.
     */
    @SpecAssertion(section = "4.2.2", id = "422-E3")
    @Test
    public void testQuery(){
        MonetaryQuery<Integer> query = new MonetaryQuery<Integer>(){
            @Override
            public Integer queryFrom(MonetaryAmount amount){
                return amount.getNumber().intValue();
            }
        };
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount amount = MonetaryAmounts.getAmountFactory(type).setCurrency("CHF").setNumber(10).create();
            Integer value = amount.query(query);
            assertTrue("Value returned from MonetaryAmount Query is not correct for " + type.getName(), value == 10);
            amount = MonetaryAmounts.getAmountFactory(type).setCurrency("CHF").setNumber(4.5).create();
            value = amount.query(query);
            assertTrue("Value returned from MonetaryAmount Query is not correct for " + type.getName(), value == 4);
        }
    }

    /**
     * Test query(q) throws a MonetaryException, if q throws any exception.
     */
    @SpecAssertion(section = "4.2.2", id = "422-E4")
    @Test
    public void testQueryInvalidQuery(){
        MonetaryQuery<Integer> query = new MonetaryQuery<Integer>(){
            @Override
            public Integer queryFrom(MonetaryAmount amount){
                throw new IllegalStateException();
            }
        };
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            TestUtils.testComparable(type);
            MonetaryAmountFactory factory = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount amount = factory.setCurrency("XXX").setNumber(1).create();
            try{
                amount.query(query);
                fail("MonetaryException expected as query applied is failing, type was " + type.getName());
            }
            catch(MonetaryException e){
                // OK, everything else makes the test fail!
            }
        }
    }

    /**
     * Test query(null) throws a NullPointerException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-E4")
    @Test
    public void testQueryNull(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            TestUtils.testComparable(type);
            MonetaryAmountFactory factory = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount amount = factory.setCurrency("XXX").setNumber(1).create();
            try{
                amount.query(null);
                fail("NullPointerException expected as query applied is null, type was " + type.getName());
            }
            catch(NullPointerException e){
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
    @Test
    public void testImplementsHashCode(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount amount = MonetaryAmounts.getAmountFactory(type).setCurrency("USD").setNumber(0).create();
            TestUtils.testHasPublicMethod(type, type, "hashCode");
            MonetaryAmount amount2 = MonetaryAmounts.getAmountFactory(type).setCurrency("USD").setNumber(0).create();
            assertEquals("hashCode() for equal amounts differ for type " + type.getName(), amount.hashCode(),
                         amount2.hashCode());
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
    @Test
    public void testImplementsEquals(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount amount = MonetaryAmounts.getAmountFactory(type).setCurrency("XXX").setNumber(0).create();
            TestUtils.testHasPublicMethod(type, type, "equals", Object.class);
            MonetaryAmount amount2 = MonetaryAmounts.getAmountFactory(type).setCurrency("XXX").setNumber(0).create();
            assertEquals("equals(Object) for equal amounts returns false for " + type.getName(), amount, amount2);
        }
    }

    /**
     * Implementations of MonetaryAmount must be Comparable.
     */
    @SpecAssertion(section = "4.2.2", id = "422-F3")
    @Test
    public void testImplementComparable(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            TestUtils.testComparable(type);
            MonetaryAmountFactory factory = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount amount = factory.setCurrency("XXX").setNumber(0).create();
            MonetaryAmount amount2 = factory.setCurrency("XXX").setNumber(0).create();
            MonetaryAmount amount3 = factory.setCurrency("CHF").setNumber(1).create();
            MonetaryAmount amount4 = factory.setCurrency("XXX").setNumber(1).create();

            assertTrue("Comparable failed for: " + type.getName(), ((Comparable) amount).compareTo(amount3) > 0);

            assertTrue("Comparable failed for: " + type.getName(), ((Comparable) amount3).compareTo(amount) < 0);

            assertTrue("Comparable failed for: " + type.getName(), ((Comparable) amount).compareTo(amount4) < 0);

            assertTrue("Comparable failed for: " + type.getName(), ((Comparable) amount4).compareTo(amount) > 0);
        }
    }

    /**
     * Implementations of MonetaryAmount must be Serializable.
     */
    @SpecAssertion(section = "4.2.2", id = "422-F4")
    @Test
    public void testSerializable(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            TestUtils.testComparable(type);
            MonetaryAmountFactory factory = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount amount = factory.setCurrency("XXX").setNumber(1).create();
            TestUtils.testSerializable(amount);
        }
    }

}
