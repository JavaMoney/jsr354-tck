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
//                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10).create();
//            MonetaryAmount mAmount2 =
//                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(20).create();
//            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
//            MonetaryAmount mExpectedResult =
//                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
//            Assert.assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
//        }
//        for(Class type : MonetaryAmounts.getAmountTypes()){
//            MonetaryAmount mAmount1 =
//                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-20).create();
//            MonetaryAmount mAmount2 =
//                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
//            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
//            MonetaryAmount mExpectedResult =
//                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10).create();
//            Assert.assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
//        }
//        for(Class type : MonetaryAmounts.getAmountTypes()){
//            MonetaryAmount mAmount1 =
//                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(-10).create();
//            MonetaryAmount mAmount2 =
//                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
//            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
//            MonetaryAmount mExpectedResult =
//                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(0).create();
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

}
