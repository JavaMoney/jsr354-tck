package org.javamoney.tck.tests;

import org.javamoney.tck.ClassTester;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.Assert;
import org.junit.Test;

import javax.money.*;
import java.util.Currency;

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
        fail();
    }

    /**
     * For each MonetaryAmount implementation: Ensure
     * getMonetaryContext() returns correct results.
     */
    @SpecAssertion(section = "4.2.2", id = "422-A3")
    @Test
    public void testGetMonetaryContext(){
        fail();
    }

    /**
     * For each MonetaryAmount implementation: Ensure isNegative()
     * returns correct results.
     */
    @SpecAssertion(section = "4.2.2", id = "422-A4")
    @Test
    public void testIsNegative(){
        fail();
    }

    /**
     * For each MonetaryAmount implementation: Ensure isPositive()
     * returns correct results.
     */
    @SpecAssertion(section = "4.2.2", id = "422-A5")
    @Test
    public void testIsZero(){
        fail();
    }

    /**
     * For each MonetaryAmount implementation: Ensure isZero()
     * returns correct results (-0, +0 == 0).
     */
    @SpecAssertion(section = "4.2.2", id = "422-A6")
    @Test
    public void testIsZeroAdvanced(){
        fail();
    }

    /**
     * For each MonetaryAmount implementation: signum() function is
     * implemented correctly.
     */
    @SpecAssertion(section = "4.2.2", id = "422-A7")
    @Test
    public void testSignum(){
        fail();
    }


    // ********************* B. Prototyping Support *****************************

    /**
     * Ensure getFactory returns a MonetaryAmountFactory and that
     * instances created are of the same type.
     */
    @SpecAssertion(section = "4.2.2", id = "422-B1")
    @Test
    public void testMonetaryAmountFactories(){
        fail("Not implemented");
    }

    /**
     * Call getFactory(), create a new MonetaryAmount instance, with
     * same input. The instances must
     * be equal (or even be identical!).
     */
    @SpecAssertion(section = "4.2.2", id = "422-B2")
    @Test
    public void testMonetaryAmountFactories_InstantesMustBeEqual(){
        fail("Not implemented");
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
        fail("Not implemented");
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
        fail("Not implemented");
    }

    /**
     * Call getFactory(),create a  new MonetaryAmount instance
     * with a new  monetary context(if possible-check the max context). The
     * instances must be non equal and have the same currency and number value.
     */
    @SpecAssertion(section = "4.2.2", id = "422-B5")
    @Test
    public void testMonetaryAmountFactories_CreateWithMonetaryContext(){
        fail("Not implemented");
    }

    /**
     * Call getFactory(),create a new MonetaryAmount instance with a new monetary context, a
     * new number and a new currency. The instances must be non equal.
     */
    @SpecAssertion(section = "4.2.2", id = "422-B6")
    @Test
    public void testMonetaryAmountFactories_CreateWithMonetaryContextNumberAndCurrency(){
        fail("Not implemented");
    }

    // ***************************** C.Comparison Methods *********************************

    /**
     * Test isGreaterThan() is implemented correctly for each amount type regardless of trailing zeroes.
     */
    @SpecAssertion(section = "4.2.2", id = "422-C1")
    @Test
    public void testMonetaryAmount_isGreaterThan(){
        fail("Not implemented");
    }

    /**
     * Test isGreaterThanOrEquals() is implemented correctly for each amount type regardless of trailing
     * zeroes.
     */
    @SpecAssertion(section = "4.2.2", id = "422-C2")
    @Test
    public void testMonetaryAmount_isGreaterThanOrEquals(){
        fail("Not implemented");
    }

    /**
     * Test isLessThan() is implemented correctly for each amount type regardless of trailing
     * zeroes.
     */
    @SpecAssertion(section = "4.2.2", id = "422-C3")
    @Test
    public void testMonetaryAmount_isLessThan(){
        fail("Not implemented");
    }

    /**
     * Test isLessThanOrEquals() is implemented correctly for each amount type regardless of trailing
     * zeroes.
     */
    @SpecAssertion(section = "4.2.2", id = "422-C4")
    @Test
    public void testMonetaryAmount_isLessThanOrEquals(){
        fail("Not implemented");
    }

    /**
     * Test isEqualTo() is implemented correctly for each amount type regardless of trailing
     * zeroes.
     */
    @SpecAssertion(section = "4.2.2", id = "422-C5")
    @Test
    public void testMonetaryAmount_isEqualTo(){
        fail("Not implemented");
    }

    /**
     * For two amounts with same numeric value and currency:
     * {@code }isEqualTo()} return true, regardless of MonetaryContext.
     */
    @SpecAssertion(section = "4.2.2", id = "422-C6")
    @Test
    public void testMonetaryAmount_isEqualToRegardlessMonetaryContext(){
        fail("Not implemented");
    }

    /**
     * For two amounts with same numeric value and currency:
     * {@code }isEqualTo()} return true, regardless of iumplementation type.
     */
    @SpecAssertion(section = "4.2.2", id = "422-C7")
    @Test
    public void testMonetaryAmount_isEqualToRegardlessType(){
        fail("Not implemented");
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
    @Test(expected = MonetaryException.class)
    public void testAdd_IncompatibleCurrencies(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(ADDITIONAL_CURRENCY).setNumber(20).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            fail("Exception expected");
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
    @Test(expected = MonetaryException.class)
    public void testAdd_ExceedsCapabilities(){
        fail("Not yet implemented");
    }

    /**
     * Tests that add(), which results in an amount exceeding the max MonetaryContext throws
     * a MonetaryException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D5")
    @Test(expected = NullPointerException.class)
    public void testAdd_Null(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.add(null);
            fail("Exception expected");
        }
    }


    /**
     * Tests that subtract() correctly adds two values, using positive integers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D6")
    @Test
    public void testSubtractPositiveIntegers(){
        fail();
    }

    /**
     * Tests that subtract() correctly adds two values, using negative integers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D6")
    @Test
    public void testSubtractNegativeIntegers(){
        fail();
    }

    /**
     * Tests that subtract() correctly adds two values, using fractions.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D6")
    @Test
    public void testSubtractPositiveFractions(){
        fail();
    }

    /**
     * Tests that subtract() correctly adds two values, using positive and negative integers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D6")
    @Test
    public void testSubtractMixedIntegers(){
        //        for(Class type : MonetaryAmounts.getAmountTypes()){
        //            MonetaryAmount mAmount1 =
        //                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10)
        // .create();
        //            MonetaryAmount mAmount2 =
        //                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(20)
        // .create();
        //            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
        //            MonetaryAmount mExpectedResult =
        //                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10)
        // .create();
        //            Assert.assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        //        }
        //        for(Class type : MonetaryAmounts.getAmountTypes()){
        //            MonetaryAmount mAmount1 =
        //                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-20)
        // .create();
        //            MonetaryAmount mAmount2 =
        //                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10)
        // .create();
        //            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
        //            MonetaryAmount mExpectedResult =
        //                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10)
        // .create();
        //            Assert.assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        //        }
        //        for(Class type : MonetaryAmounts.getAmountTypes()){
        //            MonetaryAmount mAmount1 =
        //                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10)
        // .create();
        //            MonetaryAmount mAmount2 =
        //                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10)
        // .create();
        //            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
        //            MonetaryAmount mExpectedResult =
        //                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0)
        // .create();
        //            Assert.assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        //        }
        fail();
    }

    /**
     * Tests that subtract() correctly adds two values, using positive and negative fractions.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D6")
    @Test
    public void testSubtractMixedFractions(){
        fail();
    }

    /**
     * Tests that subtract() with non matching currencies throws a
     * MonetaryException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D8")
    @Test(expected = MonetaryException.class)
    public void testSubtract_IncompatibleCurrencies(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(ADDITIONAL_CURRENCY).setNumber(20).create();
            MonetaryAmount mActualResult = mAmount1.subtract(mAmount2);
            fail("Exception expected");
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
    @Test(expected = MonetaryException.class)
    public void testSubtract_ExceedsCapabilities(){
        fail("Not yet implemented");
    }

    /**
     * Tests that subtract(), which results in an amount exceeding the max MonetaryContext throws
     * a MonetaryException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D10")
    @Test(expected = NullPointerException.class)
    public void testSubtract_Null(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            MonetaryAmount mActualResult = mAmount1.subtract(null);
            fail("Exception expected");
        }
    }

    /**
     * Test multiply() allow to multiply numbers.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D11")
    @Test
    public void testMultiply(){
        fail("Not yet implemented");
    }

    /**
     * Test multiply(1) returns this.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D12")
    @Test
    public void testMultiplyOne(){
        fail("Not yet implemented");
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
        fail("Not yet implemented");
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
    public void testDivideZero(){
        fail("Not yet implemented");
    }

    /**
     * Test divide(1) should return this.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D17")
    @Test
    public void testDivideOne(){
        fail("Not yet implemented");
    }

    /**
     * Test  divide(null)must throw a NullPointerException.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D18")
    @Test
    public void testDivideNull(){
        fail("Not yet implemented");
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
        fail("Not yet implemented");
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
        fail("Not yet implemented");
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
        fail("Not yet implemented");
    }

    /**
     * Test negate() for negating a value.
     */
    @SpecAssertion(section = "4.2.2", id = "422-D28")
    @Test
    public void testNegate(){
        fail("Not yet implemented");
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
