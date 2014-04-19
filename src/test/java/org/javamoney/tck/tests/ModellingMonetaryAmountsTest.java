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

import org.javamoney.tck.ClassTester;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.Assert;
import org.junit.Test;

import javax.money.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.*;

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
        assertNotNull(MonetaryAmounts.getAmountTypes());
        assertTrue(MonetaryAmounts.getAmountTypes().size() > 0);
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
                assertNotNull(amount);
                assertNotNull(amount.getCurrency());
                assertEquals(jdkCur.getCurrencyCode(), amount.getCurrency().getCurrencyCode());
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
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount[] moneys = new MonetaryAmount[]{f.setNumber(100).create(), f.setNumber(34242344).create(),
                    f.setNumber(new BigDecimal("23123213.435")).create(),
                    f.setNumber(new BigDecimal("-23123213.435")).create(), f.setNumber(-23123213).create(),
                    f.setNumber(0).create()};
            BigDecimal[] numbers = new BigDecimal[]{new BigDecimal("100"), new BigDecimal("23123213.435"),
                    new BigDecimal("-23123213.435"), new BigDecimal("-23123213"), BigDecimal.ZERO};
            int[] intNums = new int[]{100, 23123213, -23123213, -23123213, 0};
            long[] longNums = new long[]{100, 23123213, -23123213, -23123213, 0};
            double[] doubleNums = new double[]{100, 34242344, 23123213.435, -23123213.435, -23123213, 0};
            float[] floatNums = new float[]{100f, 34242344f, 23123213.435f, -23123213.435f, -23123213, 0f};

            for(int i = 0; i < moneys.length; i++){
                NumberValue nv = moneys[i].getNumber();
                assertNotNull(nv);
                assertEquals("getNumber() incorrect.", numbers[i], nv.numberValue(BigDecimal.class));
                assertEquals("getNumber().intValue() incorrect.", intNums[i], nv.intValue());
                assertEquals("getNumber().longValue() incorrect.", longNums[i], nv.longValue());
                assertEquals("getNumber().doubleValue() incorrect.", doubleNums[i], nv.doubleValue(), 0.0d);
                assertEquals("getNumber().floatValue() incorrect.", floatNums[i], nv.floatValue(), 0.0d);
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
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryContext defCtx = f.getDefaultMonetaryContext();
            MonetaryContext maxCtx = f.getDefaultMonetaryContext();
            MonetaryContext mc = f.setNumber(0).create().getMonetaryContext();
            assertEquals(mc.getAmountType(), type);
            assertEquals(mc.getAmountFlavor(), defCtx.getAmountFlavor());
            assertTrue(mc.getPrecision() <= maxCtx.getPrecision());
            assertTrue(mc.getMaxScale() <= maxCtx.getMaxScale());
            assertEquals(f.setNumber(0.34746d).create().getMonetaryContext().getAmountType(), type);
            mc = f.setNumber(0).create().getMonetaryContext();
            assertEquals(mc.getAmountType(), type);
            assertEquals(mc.getAmountFlavor(), defCtx.getAmountFlavor());
            assertTrue(mc.getPrecision() <= maxCtx.getPrecision());
            assertTrue(mc.getMaxScale() <= maxCtx.getMaxScale());
            assertEquals(f.setNumber(100034L).create().getMonetaryContext().getAmountType(), type);
            mc = f.setNumber(0).create().getMonetaryContext();
            assertEquals(mc.getAmountType(), type);
            assertEquals(mc.getAmountFlavor(), defCtx.getAmountFlavor());
            assertTrue(mc.getPrecision() <= maxCtx.getPrecision());
            assertTrue(mc.getMaxScale() <= maxCtx.getMaxScale());
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
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount[] moneys = new MonetaryAmount[]{f.setNumber(0).create(), f.setNumber(0.0).create(),
                    f.setNumber(BigDecimal.ZERO).create(), f.setNumber(new BigDecimal("0.00000000000000000")).create(),
                    f.setNumber(100).create(), f.setNumber(34242344).create(), f.setNumber(23123213.435).create()};
            for(MonetaryAmount m : moneys){
                assertFalse("Invalid isNegative (expected false): " + m, m.isNegative());
            }
            moneys = new MonetaryAmount[]{f.setNumber(-100).create(), f.setNumber(-34242344).create(),
                    f.setNumber(-23123213.435).create()};
            for(MonetaryAmount m : moneys){
                assertTrue("Invalid isNegative (expected true): " + m, m.isNegative());
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
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount[] moneys = new MonetaryAmount[]{f.setNumber(100).create(), f.setNumber(34242344).create(),
                    f.setNumber(23123213.435).create()};
            for(MonetaryAmount m : moneys){
                assertTrue(m.isPositive());
            }
            moneys = new MonetaryAmount[]{f.setNumber(0).create(), f.setNumber(0.0).create(),
                    f.setNumber(BigDecimal.ZERO).create(), f.setNumber(new BigDecimal("0.00000000000000000")).create(),
                    f.setNumber(-100).create(), f.setNumber(-34242344).create(), f.setNumber(-23123213.435).create()};
            for(MonetaryAmount m : moneys){
                assertFalse(m.isPositive());
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
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount[] moneys = new MonetaryAmount[]{f.setNumber(100).create(), f.setNumber(34242344).create(),
                    f.setNumber(23123213.435).create(), f.setNumber(-100).create(),
                    f.setNumber(-723527.36532).create()};
            for(MonetaryAmount m : moneys){
                assertFalse(m.isZero());
            }
            moneys = new MonetaryAmount[]{f.setNumber(0).create(), f.setNumber(0.0).create(),
                    f.setNumber(BigDecimal.ZERO).create(), f.setNumber(new BigDecimal("0.00000000000000000")).create()};
            for(MonetaryAmount m : moneys){
                assertTrue(m.isZero());
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
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount[] moneys =
                    new MonetaryAmount[]{f.setNumber(-0).create(), f.setNumber(0).create(), f.setNumber(-0.0f).create(),
                            f.setNumber(0.0f).create(), f.setNumber(-0.0d).create(), f.setNumber(0.0d).create()};
            for(MonetaryAmount m : moneys){
                assertTrue(m.isZero());
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
            assertEquals("signum of " + m, 1, m.signum());
            m = f.setNumber(-100).create();
            assertEquals("signum of " + m, -1, m.signum());
            m = f.setNumber(100.3435).create();
            assertEquals("signum of " + m, 1, m.signum());
            m = f.setNumber(-100.3435).create();
            assertEquals("signum of " + m, -1, m.signum());
            m = f.setNumber(0).create();
            assertEquals("signum of " + m, 0, m.signum());
            m = f.setNumber(-0).create();
            assertEquals("signum of " + m, 0, m.signum());
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
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount[] moneys = new MonetaryAmount[]{f.setNumber(100).create(), f.setNumber(34242344).create(),
                    f.setNumber(23123213.435).create()};
            for(MonetaryAmount m : moneys){
                assertFalse("Invalid negativeOrZero (expected false): " + m, m.isNegativeOrZero());
            }
            moneys = new MonetaryAmount[]{f.setNumber(0).create(), f.setNumber(0.0).create(),
                    f.setNumber(BigDecimal.ZERO).create(), f.setNumber(new BigDecimal("0.0000")).create(),
                    f.setNumber(-100).create(), f.setNumber(-34242344).create(), f.setNumber(-23123213.435).create()};
            for(MonetaryAmount m : moneys){
                assertTrue("Invalid negativeOrZero (expected true): " + m, m.isNegativeOrZero());
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
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount[] moneys = new MonetaryAmount[]{f.setNumber(0).create(), f.setNumber(0.0).create(),
                    f.setNumber(BigDecimal.ZERO).create(), f.setNumber(new BigDecimal("0.00000000000000000")).create(),
                    f.setNumber(100).create(), f.setNumber(34242344).create(), f.setNumber(23123213.435).create()};
            for(MonetaryAmount m : moneys){
                assertTrue("Invalid positiveOrZero (expected true): " + m, m.isPositiveOrZero());
            }
            moneys = new MonetaryAmount[]{f.setNumber(-100).create(), f.setNumber(-34242344).create(),
                    f.setNumber(-23123213.435).create()};
            for(MonetaryAmount m : moneys){
                assertFalse("Invalid positiveOrZero (expected false): " + m, m.isPositiveOrZero());
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
            assertEquals(m.getClass(), type);
            m = f.setCurrency("CHF").setNumber(-10).create();
            assertEquals(m.getClass(), type);
            m = f.setCurrency("CHF").setNumber(10.3).create();
            assertEquals(m.getClass(), type);
            m = f.setCurrency("CHF").setNumber(-10.3).create();
            assertEquals(m.getClass(), type);
            m = f.setCurrency("CHF").setNumber(0.0).create();
            assertEquals(m.getClass(), type);
            m = f.setCurrency("CHF").setNumber(-0.0).create();
            assertEquals(m.getClass(), type);
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
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(10).create();
            f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("CHF").setNumber(10).create();
            assertEquals(m1, m2);
        }

        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(10.5d).create();
            f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("CHF").setNumber(10.5d).create();
            assertEquals(m1, m2);
        }

        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(new BigDecimal("10.52")).create();
            f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("CHF").setNumber(new BigDecimal("10.52")).create();
            assertEquals(m1, m2);
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
            assertNotSame(m1, m2);
        }

        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(10.5d).create();
            f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("CHF").setNumber(10.4d).create();
            assertNotSame(m1, m2);
        }

        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(new BigDecimal("10.52")).create();
            f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("CHF").setNumber(new BigDecimal("10.11")).create();
            assertNotSame(m1, m2);
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
            assertNotSame(m1, m2);
        }

        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(10.5d).create();
            f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("USD").setNumber(10.5d).create();
            assertNotSame(m1, m2);
        }

        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m1 = f.setCurrency("CHF").setNumber(new BigDecimal("10.52")).create();
            f = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount m2 = f.setCurrency("USD").setNumber(new BigDecimal("10.52")).create();
            assertNotSame(m1, m2);
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
                assertNotSame(m1, m2);
            }else{
                // In this cases both amount must be non equals
                m1 = f.setCurrency("CHF").setContext(mc1).setNumber(10).create();
                m2 = f.setCurrency("CHF").setContext(mc2).setNumber(10).create();
                assertNotSame(m1, m2);
            }
            assertTrue(m1.equals(m1));
            assertTrue(m2.equals(m2));
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
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            MonetaryContext mc1 = f.getDefaultMonetaryContext();
            MonetaryContext mc2 = f.getMaximalMonetaryContext();
            MonetaryAmount m1 = f.setCurrency("CHF").setContext(mc1).setNumber(10).create();
            MonetaryAmount m2 = f.setCurrency("USD").setContext(mc2).setNumber(11).create();
            assertNotSame(m1, m2);
            assertTrue(m1.isEqualTo(m1));
            assertTrue(m2.isEqualTo(m2));
            assertTrue(m1.equals(m1));
            assertTrue(m2.equals(m2));
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
            assertFalse(f.setNumber(BigDecimal.valueOf(0d)).create()
                                .isGreaterThan(f.setNumber(BigDecimal.valueOf(0)).create()));
            assertTrue(f.setNumber(BigDecimal.valueOf(0.00000000001d)).create()
                               .isGreaterThan(f.setNumber(BigDecimal.valueOf(0d)).create()));
            assertTrue(f.setNumber(15).create().isGreaterThan(f.setNumber(10).create()));
            assertTrue(f.setNumber(15.546).create().isGreaterThan(f.setNumber(10.34).create()));
            assertFalse(f.setNumber(5).create().isGreaterThan(f.setNumber(10).create()));
            assertFalse(f.setNumber(5.546).create().isGreaterThan(f.setNumber(10.34).create()));
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
            assertTrue(f.setNumber(BigDecimal.valueOf(0d)).create()
                               .isGreaterThanOrEqualTo(f.setNumber(BigDecimal.valueOf(0)).create()));
            assertTrue(f.setNumber(BigDecimal.valueOf(0.00000000001d)).create()
                               .isGreaterThanOrEqualTo(f.setNumber(BigDecimal.valueOf(0d)).create()));
            assertTrue(f.setNumber(15).create().isGreaterThanOrEqualTo(f.setNumber(10).create()));
            assertTrue(f.setNumber(15.546).create().isGreaterThanOrEqualTo(f.setNumber(10.34).create()));
            assertFalse(f.setNumber(5).create().isGreaterThanOrEqualTo(f.setNumber(10).create()));
            assertFalse(f.setNumber(5.546).create().isGreaterThanOrEqualTo(f.setNumber(10.34).create()));
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
            assertFalse(f.setNumber(BigDecimal.valueOf(0d)).create()
                                .isLessThan(f.setNumber(BigDecimal.valueOf(0)).create()));
            assertFalse(f.setNumber(BigDecimal.valueOf(0.00000000001d)).create()
                                .isLessThan(f.setNumber(BigDecimal.valueOf(0d)).create()));
            assertFalse(f.setNumber(15).create().isLessThan(f.setNumber(10).create()));
            assertFalse(f.setNumber(15.546).create().isLessThan(f.setNumber(10.34).create()));
            assertTrue(f.setNumber(5).create().isLessThan(f.setNumber(10).create()));
            assertTrue(f.setNumber(5.546).create().isLessThan(f.setNumber(10.34).create()));
        }
    }

    /**
     * Test isLessThanOrEquals() is implemented correctly for each amount type regardless of trailing
     * zeroes.
     */
    @SpecAssertion(section = "4.2.2", id = "422-C4")
    @Test
    public void testMonetaryAmount_isLessThanOrEquals(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            assertTrue(f.setNumber(BigDecimal.valueOf(0d)).create()
                               .isLessThanOrEqualTo(f.setNumber(BigDecimal.valueOf(0)).create()));
            assertFalse(f.setNumber(BigDecimal.valueOf(0.00000000001d)).create()
                                .isLessThanOrEqualTo(f.setNumber(BigDecimal.valueOf(0d)).create()));
            assertFalse(f.setNumber(15).create().isLessThanOrEqualTo(f.setNumber(10).create()));
            assertFalse(f.setNumber(15.546).create().isLessThan(f.setNumber(10.34).create()));
            assertTrue(f.setNumber(5).create().isLessThanOrEqualTo(f.setNumber(10).create()));
            assertTrue(f.setNumber(5.546).create().isLessThanOrEqualTo(f.setNumber(10.34).create()));
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
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            assertTrue(f.setNumber(BigDecimal.valueOf(0d)).create()
                               .isEqualTo(f.setNumber(BigDecimal.valueOf(0)).create()));
            assertFalse(f.setNumber(BigDecimal.valueOf(0.00000000001d)).create()
                                .isEqualTo(f.setNumber(BigDecimal.valueOf(0d)).create()));
            assertTrue(f.setNumber(BigDecimal.valueOf(5d)).create()
                               .isEqualTo(f.setNumber(BigDecimal.valueOf(5)).create()));
            assertTrue(f.setNumber(BigDecimal.valueOf(1d)).create()
                               .isEqualTo(f.setNumber(BigDecimal.valueOf(1.00)).create()));
            assertTrue(f.setNumber(BigDecimal.valueOf(1d)).create().isEqualTo(f.setNumber(BigDecimal.ONE).create()));
            assertTrue(f.setNumber(BigDecimal.valueOf(1)).create().isEqualTo(f.setNumber(BigDecimal.ONE).create()));
            assertTrue(f.setNumber(new BigDecimal("1.0000")).create()
                               .isEqualTo(f.setNumber(new BigDecimal("1.00")).create()));
            // Test with different scales, but numeric equal values
            assertTrue(f.setNumber(BigDecimal.valueOf(0d)).create()
                               .isEqualTo(f.setNumber(BigDecimal.valueOf(0)).create()));
            assertTrue(f.setNumber(BigDecimal.ZERO).create().isEqualTo(f.setNumber(BigDecimal.valueOf(0)).create()));
            assertTrue(
                    f.setNumber(BigDecimal.valueOf(5)).create().isEqualTo(f.setNumber(new BigDecimal("5.0")).create()));
            assertTrue(f.setNumber(BigDecimal.valueOf(5)).create()
                               .isEqualTo(f.setNumber(new BigDecimal("5.00")).create()));
            assertTrue(f.setNumber(BigDecimal.valueOf(5)).create()
                               .isEqualTo(f.setNumber(new BigDecimal("5.000")).create()));
            assertTrue(f.setNumber(BigDecimal.valueOf(5)).create()
                               .isEqualTo(f.setNumber(new BigDecimal("5.0000")).create()));
            assertTrue(f.setNumber(new BigDecimal("-1.23")).create()
                               .isEqualTo(f.setNumber(new BigDecimal("-1.230")).create()));
            assertTrue(f.setNumber(new BigDecimal("-1.23")).create()
                               .isEqualTo(f.setNumber(new BigDecimal("-1.2300")).create()));
            assertTrue(f.setNumber(new BigDecimal("-1.23")).create()
                               .isEqualTo(f.setNumber(new BigDecimal("-1.23000")).create()));
            assertTrue(f.setNumber(new BigDecimal("-1.23")).create()
                               .isEqualTo(f.setNumber(new BigDecimal("-1.230000000000000000000")).create()));
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
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(20).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(30).create();
            Assert.assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that add() correctly adds two values, using negative integers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D1")
    @Test
    public void testAddNegativeIntegers(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-20).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-30).create();
            Assert.assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that add() correctly adds two values, using fractions.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D1")
    @Test
    public void testAddPositiveFractions(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(1.5).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(2.85).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(4.75).create();
            Assert.assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that add() correctly adds two values, using positive and negative integers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D1")
    @Test
    public void testAddMixedIntegers(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(20).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            Assert.assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-20).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10).create();
            Assert.assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create();
            Assert.assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that add() correctly adds two values, using positive and negative fractions.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D1")
    @Test
    public void testAddMixedFractions(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(1.5).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-2.85).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-1.35).create();
            Assert.assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-1.5).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(+2.85).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(1.35).create();
            Assert.assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-2.85).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(+2.85).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create();
            Assert.assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that add() with non matching currencies throws a
     * MonetaryException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D2")
    @Test
    public void testAdd_IncompatibleCurrencies() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(ADDITIONAL_CURRENCY).setNumber(20).create();
            try {
                MonetaryAmount mActualResult = mAmount1.add(mAmount2);
                fail("Exception expected");
            } catch (MonetaryException ex) {
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
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            Assert.assertEquals("Adding zero", mAmount1, mActualResult);
        }
    }

    /**
     * Tests that add(), which results in an amount exceeding the max MonetaryContext throws
     * a MonetaryException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D4")
    @Test
    public void testAdd_ExceedsCapabilities() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount mAmount1 = f.setNumber(0).create();
            MonetaryContext maxCtx = f.getMaximalMonetaryContext();
            MonetaryAmount mAmount2 = null;
            if (maxCtx.getPrecision() > 0) {
                mAmount2 = f.setNumber(createNumberWithPrecision(f, maxCtx.getPrecision())).create();
            }
            if (maxCtx.getMaxScale() >= 0) {
                MonetaryContext tgtContext =
                        new MonetaryContext.Builder(maxCtx).setMaxScale(maxCtx.getMaxScale() + 1).create();
                Class<? extends MonetaryAmount> exceedingType = null;
                try {
                    exceedingType = MonetaryAmounts.queryAmountType(tgtContext);
                    assertNotNull(exceedingType);
                    MonetaryAmountFactory<? extends MonetaryAmount> bigFactory =
                            MonetaryAmounts.getAmountFactory(exceedingType);
                    mAmount2 = bigFactory.setCurrency("CHF").setNumber(createNumberWithScale(f, maxCtx.getMaxScale()))
                            .create();
                } catch (MonetaryException e) {
                    // we have to abort the test...
                }

            }
            if (mAmount2 != null) {
                try {
                    mAmount1.add(mAmount2);
                    fail("Exception expected");
                } catch (MonetaryException ex) {
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

    private BigDecimal createNumberWithPrecision(MonetaryAmountFactory f, int precision){
        StringBuilder b = new StringBuilder(precision + 1);
        for(int i = 0; i < precision; i++){
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
    public void testAdd_Null() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try {
                MonetaryAmount mActualResult = mAmount1.add(null);
                fail("Exception expected");
            } catch (NullPointerException ex) {
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
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(20).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10).create();
            Assert.assertEquals("Subtracting two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that subtract() correctly adds two values, using negative integers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D6")
    @Test
    public void testSubtractNegativeIntegers(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-20).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            Assert.assertEquals("Subtracting two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that subtract() correctly adds two values, using fractions.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D6")
    @Test
    public void testSubtractPositiveFractions(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(1.5).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(2.85).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-1.35).create();
            Assert.assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that subtract() correctly adds two values, using positive and negative integers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D6")
    @Test
    public void testSubtractMixedIntegers(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(20).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            Assert.assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create();
            Assert.assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-20).create();
            Assert.assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that subtract() correctly adds two values, using positive and negative fractions.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D6")
    @Test
    public void testSubtractMixedFractions(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(1.5).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-2.85).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(4.35).create();
            Assert.assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-1.5).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(+2.85).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-4.35).create();
            Assert.assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(2.85).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(+2.85).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create();
            Assert.assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    /**
     * Tests that subtract() with non matching currencies throws a
     * MonetaryException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D8")
    @Test
    public void testSubtract_IncompatibleCurrencies() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(ADDITIONAL_CURRENCY).setNumber(20).create();
            try {
                MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
                fail("Exception expected");
            } catch (MonetaryException ex) {
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
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            Assert.assertEquals("Subtract zero", mAmount1, mActualResult);
        }
    }

    /**
     * Tests that subtract(), which results in an amount exceeding the max MonetaryContext throws
     * a MonetaryException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D9")
    @Test
    public void testSubtract_ExceedsCapabilities() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount mAmount1 = f.setNumber(0).create();
            MonetaryContext maxCtx = f.getMaximalMonetaryContext();
            MonetaryAmount mAmount2 = null;
            if (maxCtx.getPrecision() > 0) {
                mAmount2 = f.setNumber(createNumberWithPrecision(f, maxCtx.getPrecision())).create();
            }
            if (maxCtx.getMaxScale() >= 0) {
                MonetaryContext tgtContext =
                        new MonetaryContext.Builder(maxCtx).setMaxScale(maxCtx.getMaxScale() + 1).create();
                Class<? extends MonetaryAmount> exceedingType = null;
                try {
                    exceedingType = MonetaryAmounts.queryAmountType(tgtContext);
                    assertNotNull(exceedingType);
                    MonetaryAmountFactory<? extends MonetaryAmount> bigFactory =
                            MonetaryAmounts.getAmountFactory(exceedingType);
                    mAmount2 = bigFactory.setCurrency("CHF").setNumber(createNumberWithScale(f, maxCtx.getMaxScale()))
                            .create();
                } catch (MonetaryException e) {
                    // we have to abort the test...
                }

            }
            if (mAmount2 != null) {
                try {
                    mAmount1.subtract(mAmount2);
                    fail("Exception expected");
                } catch (MonetaryException ex) {
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
    public void testSubtract_Null() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try {
                MonetaryAmount mActualResult = mAmount1.subtract(null);
                fail("Exception expected");
            } catch (NullPointerException ex) {
                // Expected
            }
        }
    }

    /**
     * Test multiply() allow to multiply numbers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D11")
    @Test
    public void testMultiply(){
        fail("Exception expected");
    }

    /**
     * Test multiply(1) returns this.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D12")
    @Test
    public void testMultiplyOne(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.multiply(1);
            assertTrue(mActualResult == mAmount1);
            mActualResult = mAmount1.multiply(1.0);
            assertTrue(mActualResult == mAmount1);
            mActualResult = mAmount1.multiply(BigDecimal.ONE);
            assertTrue(mActualResult == mAmount1);
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
        fail("Not yet implemented");
    }

    /**
     * Test multiply(null) must throw an NullPointerException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D14")
    @Test
    public void testMultiplyNull(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try{
                MonetaryAmount mActualResult = mAmount1.multiply(null);
                fail("Exception expected");
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
        fail("Not yet implemented");
    }

    /**
     * Test divide(0) function must throw an ArithmeticException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D16")
    @Test
    public void testDivideZero() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try {
                MonetaryAmount mActualResult = mAmount1.divide(0);
                fail("Exception expected");
            } catch (ArithmeticException ex) {
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
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.divide(1);
            assertTrue(mActualResult == mAmount1);
            mActualResult = mAmount1.divide(1.0);
            assertTrue(mActualResult == mAmount1);
            mActualResult = mAmount1.divide(BigDecimal.ONE);
            assertTrue(mActualResult == mAmount1);
        }
    }

    /**
     * Test  divide(null)must throw a NullPointerException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D18")
    @Test
    public void testDivideNull(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try{
                MonetaryAmount mActualResult = mAmount1.divide(null);
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
        fail("Not yet implemented");
    }

    /**
     * Test remainder(0) must throw an ArithmeticException
     */
    @SpecAssertion(section = "4.2.2", id = "422-D20")
    @Test
    public void testRemainderZero(){
        fail("Not yet implemented");
    }

    /**
     * Test remainder(null) must throw a NullPointerException
     */
    @SpecAssertion(section = "4.2.2", id = "422-D21")
    @Test
    public void testRemainderNull(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try{
                MonetaryAmount mActualResult = mAmount1.remainder(null);
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
        fail("Not yet implemented");
    }

    /**
     * Test  divideAndRemainder(0) throws an ArithmeticException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D23")
    @Test
    public void testDivideAndRemainderZero(){
        fail("Not yet implemented");
    }

    /**
     * Test  divideAndRemainder(null) throws an NullPointerException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D24")
    @Test
    public void testDivideAndRemainderNull(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            try{
                MonetaryAmount[] mActualResult = mAmount1.divideAndRemainder(null);
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
        fail("Not yet implemented");
    }

    /**
     * Test scaleByPowerOfTen()allow to scale by power of 10.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D26")
    @Test
    public void testScaleByPowerOfTen(){
        fail("Not yet implemented");
    }

    /**
     * Test abs() for getting the absolute value.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D27")
    @Test
    public void testAbsolute(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount m = f.setNumber(10).create();
            assertEquals(m, m.abs());
            assertTrue(m == m.abs());
            m = f.setNumber(0).create();
            assertEquals(m, m.abs());
            assertTrue(m == m.abs());
            m = f.setNumber(-10).create();
            assertEquals(m.negate(), m.abs());
            assertTrue(m != m.abs());
        }
    }

    /**
     * Test negate() for negating a value.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D28")
    @Test
    public void testNegate(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmountFactory<MonetaryAmount> f = MonetaryAmounts.getAmountFactory(type);
            f.setCurrency("CHF");
            MonetaryAmount m = f.setNumber(100).create();
            assertEquals(f.setNumber(-100), m.negate());
            m = f.setNumber(-123.234).create();
            assertEquals(f.setNumber(123.234), m.negate());
        }
    }

    /**
     * Ensure with(MonetaryOperator) can be called and produces
     * amounts of the same type and correct value.
     */
    @SpecAssertion(section = "4.2.2", id = "422-E1")
    @Test
    public void testWith(){
        MonetaryOperator op = new MonetaryOperator(){
            @Override
            public <T extends MonetaryAmount> T apply(T value){
                return value;
            }
        };
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount amount = MonetaryAmounts.getAmountFactory(type).setCurrency("CHF").setNumber(10).create();
            MonetaryAmount amount2 = amount.with(op);
            assertTrue(amount == amount2);
            final MonetaryAmount result =
                    MonetaryAmounts.getAmountFactory(type).setCurrency("CHF").setNumber(4).create();
            MonetaryOperator op2 = new MonetaryOperator(){
                @Override
                public <T extends MonetaryAmount> T apply(T value){
                    return (T) result;
                }
            };
            amount2 = amount.with(op);
            assertTrue(amount == amount2);
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
            public <T extends MonetaryAmount> T apply(T value){
                throw new IllegalStateException();
            }
        };
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmountFactory factory = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount amount = factory.setCurrency("XXX").setNumber(1).create();
            try{
                amount.with(op);
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
            MonetaryAmountFactory factory = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount amount = factory.setCurrency("XXX").setNumber(1).create();
            try{
                amount.with(null);
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
            MonetaryAmount amount = MonetaryAmounts.getAmountFactory(type).setCurrency("CHF").setNumber(10).create();
            Integer value = amount.query(query);
            assertTrue(value == 10);
            amount = MonetaryAmounts.getAmountFactory(type).setCurrency("CHF").setNumber(4.5).create();
            value = amount.query(query);
            assertTrue(value == 4);
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
            ClassTester.testComparable(type);
            MonetaryAmountFactory factory = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount amount = factory.setCurrency("XXX").setNumber(1).create();
            try{
                amount.query(query);
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
            ClassTester.testComparable(type);
            MonetaryAmountFactory factory = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount amount = factory.setCurrency("XXX").setNumber(1).create();
            try{
                amount.query(null);
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
            MonetaryAmount amount = MonetaryAmounts.getAmountFactory(type).setCurrency("USD").setNumber(0).create();
            ClassTester.testHasPublicMethod(type, type, "hashCode");
            MonetaryAmount amount2 = MonetaryAmounts.getAmountFactory(type).setCurrency("USD").setNumber(0).create();
            assertEquals(amount.hashCode(), amount2.hashCode());
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
            MonetaryAmount amount = MonetaryAmounts.getAmountFactory(type).setCurrency("XXX").setNumber(0).create();
            ClassTester.testHasPublicMethod(type, type, "equals", Object.class);
            MonetaryAmount amount2 = MonetaryAmounts.getAmountFactory(type).setCurrency("XXX").setNumber(0).create();
            assertEquals(amount, amount2);
        }
    }

    /**
     * Implementations of MonetaryAmount must be Comparable.
     */
    @SpecAssertion(section = "4.2.2", id = "422-F3")
    @Test
    public void testImplementComparable(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            ClassTester.testComparable(type);
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
            ClassTester.testComparable(type);
            MonetaryAmountFactory factory = MonetaryAmounts.getAmountFactory(type);
            MonetaryAmount amount = factory.setCurrency("XXX").setNumber(1).create();
            ClassTester.testSerializable(amount);
        }
    }

}
