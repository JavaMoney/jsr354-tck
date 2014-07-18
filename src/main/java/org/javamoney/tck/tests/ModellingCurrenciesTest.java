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
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import javax.money.CurrencyUnit;
import javax.money.MonetaryCurrencies;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;


@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class ModellingCurrenciesTest{

    /**
     * Ensure at least one javax.money.CurrencyUnit implementation
     * is available and registered/accessible from MonetaryCurrencies.
     */
    @SpecAssertion(section = "4.2.1", id = "421-A1")
    @Test
    public void testEnsureCurrencyUnit(){
        AssertJUnit.assertTrue("Section 4.2.1: TCK Configuration not available.",
                               TCKTestSetup.getTestConfiguration() != null);
        AssertJUnit.assertTrue(TCKTestSetup.getTestConfiguration().getCurrencyClasses().size() > 0);
    }

    /**
     * Tests that currencies returned for same ISO currency code are
     * equal, ensure when listing all available currencies, that the
     * code
     * is unique.
     */
    @SpecAssertion(section = "4.2.1", id = "421-A2")
    @Test
    public void testEqualISOCurrencies(){
        for(Currency currency : Currency.getAvailableCurrencies()){
            CurrencyUnit unit = MonetaryCurrencies.getCurrency(currency.getCurrencyCode());
            AssertJUnit.assertNotNull(
                    "Section 4.2.1: MonetaryCurrencies does not provide CurrencyUnit similar to Currency: " +
                            currency.getCurrencyCode(), unit);
            CurrencyUnit unit2 = MonetaryCurrencies.getCurrency(currency.getCurrencyCode());
            AssertJUnit.assertNotNull("Section 4.2.1: MonetaryCurrencies does not provide CurrencyUnit similar to Currency: " +
                                              currency.getCurrencyCode(), unit2);
            AssertJUnit.assertEquals(
                    "Section 4.2.1: MonetaryCurrencies does provide different instances of CurrencyUnit for same " +
                            "Currency: " +
                            currency.getCurrencyCode(), unit, unit2);
        }
    }

    /**
     * Ensure all ISO 3-letters codes as defined by the JDK are also
     * available from MonetaryCurrencies.
     */
    @SpecAssertion(section = "4.2.1", id = "421-A3")
    @Test
    public void testEnforce3LetterCode4ISO(){
        for(Currency currency : Currency.getAvailableCurrencies()){
            CurrencyUnit unit = MonetaryCurrencies.getCurrency(currency.getCurrencyCode());
            AssertJUnit.assertNotNull("Section 4.2.1: MonetaryCurrencies does not provide CurrencyUnit for ISO code: " +
                                              currency.getCurrencyCode(), unit);
            AssertJUnit.assertEquals("Section 4.2.1: MonetaryCurrencies does provide a CurrencyUnit with invalid code, expected: " +
                                             currency.getCurrencyCode(), currency.getCurrencyCode(), unit.getCurrencyCode());
        }
    }

    /**
     * Test that JDK currencies returned match the values of corresponding JDK Currency (code, numeric code,
     * default fraction digits).
     */
    @SpecAssertion(section = "4.2.1", id = "421-A4")
    @Test
    public void testISOCodes(){
        for(Currency currency : Currency.getAvailableCurrencies()){
            CurrencyUnit unit = MonetaryCurrencies.getCurrency(currency.getCurrencyCode());
            AssertJUnit.assertEquals("Section 4.2.1: MonetaryCurrencies does provide a CurrencyUnit with invalid code for: " +
                                             currency.getCurrencyCode(), currency.getCurrencyCode(), unit.getCurrencyCode());
            AssertJUnit.assertEquals("Section 4.2.1: MonetaryCurrencies does provide a CurrencyUnit with invalid default fraction units for: " +
                                             currency.getCurrencyCode(), currency.getDefaultFractionDigits(), unit.getDefaultFractionDigits());
            AssertJUnit.assertEquals("Section 4.2.1: MonetaryCurrencies does provide a CurrencyUnit with invalid numeric code for: " +
                                             currency.getCurrencyCode(), currency.getNumericCode(), unit.getNumericCode());
        }
    }

    /**
     * Test that CurrencyUnit implementations implement hashCode.
     */
    @SpecAssertion(section = "4.2.1", id = "421-B1")
    @Test
    public void testCurrencyClassesEqualsHashcode(){
        for(Class type : TCKTestSetup.getTestConfiguration().getCurrencyClasses()){
            TestUtils.testHasPublicMethod("Section 4.2.1", type, int.class, "hashCode");
        }
        for(String code : new String[]{"CHF", "USD", "EUR", "GBP", "USS"}){
            CurrencyUnit unit = MonetaryCurrencies.getCurrency(code);
            TestUtils.testHasPublicMethod("Section 4.2.1", unit.getClass(), int.class, "hashCode");
        }
    }

    /**
     * Test that CurrencyUnit implementations implement equals.
     */
    @SpecAssertion(section = "4.2.1", id = "421-B2")
    @Test
    public void testImplementsEquals(){
        List<CurrencyUnit> firstUnits = new ArrayList<CurrencyUnit>();
        List<CurrencyUnit> secondUnits = new ArrayList<CurrencyUnit>();
        for(String code : new String[]{"CHF", "USD", "EUR", "GBP", "USS"}){
            CurrencyUnit unit = MonetaryCurrencies.getCurrency(code);
            AssertJUnit.assertNotNull(unit);
            TestUtils.testHasPublicMethod("Section 4.2.1", unit.getClass(), boolean.class, "equals", Object.class);
            firstUnits.add(unit);
            CurrencyUnit unit2 = MonetaryCurrencies.getCurrency(code);
            AssertJUnit.assertNotNull(unit);
            secondUnits.add(unit);
        }
        for(String code : new String[]{"CHF", "USD", "EUR", "GBP", "USS"}){
            CurrencyUnit unit = MonetaryCurrencies.getCurrency(code);
            TestUtils.testHasPublicMethod("Section 4.2.1", unit.getClass(), boolean.class, "equals", Object.class);
        }
        for(int i = 0; i < firstUnits.size(); i++){
            AssertJUnit.assertEquals(firstUnits.get(i), secondUnits.get(i));
        }
    }

    /**
     * Test that CurrencyUnit implementations are comparable.
     */
    @SpecAssertion(section = "4.2.1", id = "421-B3")
    @Test
    public void testCurrencyClassesComparable(){
        for(Class type : TCKTestSetup.getTestConfiguration().getCurrencyClasses()){
            TestUtils.testComparable("Section 4.2.1", type);
        }
        for(String code : new String[]{"CHF", "USD", "EUR", "GBP", "USS"}){
            CurrencyUnit unit = MonetaryCurrencies.getCurrency(code);
            TestUtils.testComparable("Section 4.2.1", unit.getClass());
        }
    }

    /**
     * Test that CurrencyUnit implementations are immutable.
     */
    @SpecAssertion(section = "4.2.1", id = "421-B4")
    @Test
    public void testIsImmutable(){
        for(Class type : TCKTestSetup.getTestConfiguration().getCurrencyClasses()){
            TestUtils.testImmutable("Section 4.2.1", type);
        }
        for(String code : new String[]{"CHF", "USD", "EUR", "GBP", "USS"}){
            CurrencyUnit unit = MonetaryCurrencies.getCurrency(code);
            TestUtils.testImmutable("Section 4.2.1", unit.getClass());
        }
    }

    /**
     * Test that CurrencyUnit implementations are serializable.
     */
    @SpecAssertion(section = "4.2.1", id = "421-B6")
    @Test
    public void testImplementsSerializable(){
        for(Class type : TCKTestSetup.getTestConfiguration().getCurrencyClasses()){
            TestUtils.testSerializable("Section 4.2.1", type);
        }
        for(String code : new String[]{"CHF", "USD", "EUR", "GBP", "USS"}){
            CurrencyUnit unit = MonetaryCurrencies.getCurrency(code);
            TestUtils.testSerializable("Section 4.2.1", unit);
        }
    }

}
