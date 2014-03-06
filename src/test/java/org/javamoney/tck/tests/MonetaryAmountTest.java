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
public class MonetaryAmountTest{

    private final static String DEFAULT_CURRENCY = "CHF";

    @SpecAssertion(section = "4.2.2", id = "422-0")
    @Test
    public void testEnsureMonetaryAmount(){
        assertNotNull(MonetaryAmounts.getAmountTypes());
        assertTrue(MonetaryAmounts.getAmountTypes().size() > 0);
    }

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

    @SpecAssertion(section = "4.2.2", id = "422-D1")
    @Test
    public void testAddNumber(){
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

    @SpecAssertion(section = "4.2.2", id = "422-D1")
    @Test
    public void testAddDouble(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10d).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(20d).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(30d).create();
            assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }

    @SpecAssertion(section = "4.2.2", id = "422-D1")
    @Test
    public void testAddLong(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10l).create();
            MonetaryAmount mAmount2 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(20l).create();
            MonetaryAmount mActualResult = mAmount1.add(mAmount2);
            MonetaryAmount mExpectedResult =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(30l).create();
            Assert.assertEquals("Adding two simple ammounts", mExpectedResult, mActualResult);
        }
    }


}
