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
import org.javamoney.tck.TCKTestSetup;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import javax.money.CurrencyUnit;
import javax.money.MonetaryCurrencies;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.testng.AssertJUnit.*;


@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class ModellingCurrenciesTest{

    /**
     * Ensure at least one javax.money.CurrencyUnit implementation
     * is available and registered/accessible from MonetaryCurrencies.
     */
    @SpecAssertion(section = "4.2.1", id = "421-A1")
    @Test
    public void testEnsureCurrencyUnit(){
        assertTrue("TCK Configuration not available.", TCKTestSetup.getTestConfiguration() != null);
        assertTrue(TCKTestSetup.getTestConfiguration().getCurrencyClasses().size() > 0);
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
        for(Class type : TCKTestSetup.getTestConfiguration().getCurrencyClasses()){
            for(Currency currency : Currency.getAvailableCurrencies()){
                CurrencyUnit unit = MonetaryCurrencies.getCurrency(currency.getCurrencyCode());
                assertNotNull(unit);
                CurrencyUnit unit2 = MonetaryCurrencies.getCurrency(currency.getCurrencyCode());
                assertNotNull(unit2);
                assertEquals(unit, unit2);
            }
        }
    }

    /**
     * Ensure all ISO 3-letters codes as defined by the JDK are also
     * available from MonetaryCurrencies.
     */
    @SpecAssertion(section = "4.2.1", id = "421-A3")
    @Test
    public void testEnforce3LetterCode4ISO(){
        for(Class type : TCKTestSetup.getTestConfiguration().getCurrencyClasses()){
            for(Currency currency : Currency.getAvailableCurrencies()){
                CurrencyUnit unit = MonetaryCurrencies.getCurrency(currency.getCurrencyCode());
                assertNotNull(unit);
                assertEquals(currency.getCurrencyCode(), unit.getCurrencyCode());
            }
        }
    }

    /**
     * Test that JDK currencies returned match the values of corresponding JDK Currency (code, numeric code,
     * default fraction digits).
     */
    @SpecAssertion(section = "4.2.1", id = "421-A4")
    @Test
    public void testISOCodes(){
        for(Class type : TCKTestSetup.getTestConfiguration().getCurrencyClasses()){
            for(Currency currency : Currency.getAvailableCurrencies()){
                CurrencyUnit unit = MonetaryCurrencies.getCurrency(currency.getCurrencyCode());
                assertEquals(currency.getCurrencyCode(), unit.getCurrencyCode());
                assertEquals(currency.getDefaultFractionDigits(), unit.getDefaultFractionDigits());
                assertEquals(currency.getNumericCode(), unit.getNumericCode());
            }
        }
    }

    /**
     * Test that CurrencyUnit implementations implement hashCode.
     */
    @SpecAssertion(section = "4.2.1", id = "421-B1")
    @Test
    public void testCurrencyClassesEqualsHashcode(){
        for(Class type : TCKTestSetup.getTestConfiguration().getCurrencyClasses()){
            TestUtils.testHasPublicMethod(type, int.class, "hashCode");
        }
        for(String code : new String[]{"CHF", "USD", "EUR", "GBP", "USS"}){
            CurrencyUnit unit = MonetaryCurrencies.getCurrency(code);
            TestUtils.testHasPublicMethod(unit.getClass(), int.class, "hashCode");
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
            assertNotNull(unit);
            TestUtils.testHasPublicMethod(unit.getClass(), boolean.class, "equals", Object.class);
            firstUnits.add(unit);
            CurrencyUnit unit2 = MonetaryCurrencies.getCurrency(code);
            assertNotNull(unit);
            secondUnits.add(unit);
        }
        for(String code : new String[]{"CHF", "USD", "EUR", "GBP", "USS"}){
            CurrencyUnit unit = MonetaryCurrencies.getCurrency(code);
            TestUtils.testHasPublicMethod(unit.getClass(), boolean.class, "equals", Object.class);
        }
        for(int i = 0; i < firstUnits.size(); i++){
            assertEquals(firstUnits.get(i), secondUnits.get(i));
        }
    }

    /**
     * Test that CurrencyUnit implementations are comparable.
     */
    @SpecAssertion(section = "4.2.1", id = "421-B3")
    @Test
    public void testCurrencyClassesComparable(){
        for(Class type : TCKTestSetup.getTestConfiguration().getCurrencyClasses()){
            TestUtils.testComparable(type);
        }
        for(String code : new String[]{"CHF", "USD", "EUR", "GBP", "USS"}){
            CurrencyUnit unit = MonetaryCurrencies.getCurrency(code);
            TestUtils.testComparable(unit.getClass());
        }
    }

    /**
     * Test that CurrencyUnit implementations are immutable.
     */
    @SpecAssertion(section = "4.2.1", id = "421-B4")
    @Test
    public void testIsImmutable(){
        for(Class type : TCKTestSetup.getTestConfiguration().getCurrencyClasses()){
            TestUtils.testImmutable(type);
        }
        for(String code : new String[]{"CHF", "USD", "EUR", "GBP", "USS"}){
            CurrencyUnit unit = MonetaryCurrencies.getCurrency(code);
            TestUtils.testImmutable(unit.getClass());
        }
    }

    /**
     * Test that CurrencyUnit implementations are serializable.
     */
    @SpecAssertion(section = "4.2.1", id = "421-B6")
    @Test
    public void testImplementsSerializable(){
        for(Class type : TCKTestSetup.getTestConfiguration().getCurrencyClasses()){
            TestUtils.testSerializable(type);
        }
        for(String code : new String[]{"CHF", "USD", "EUR", "GBP", "USS"}){
            CurrencyUnit unit = MonetaryCurrencies.getCurrency(code);
            TestUtils.testSerializable(unit);
        }
    }

}
