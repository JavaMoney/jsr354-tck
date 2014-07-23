/*
 * Copyright (c) 2012, 2013, Werner Keil, Credit Suisse (Anatole Tresch). Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. Contributors: Anatole Tresch - initial version.
 */
package org.javamoney.tck.tests.conversion;

import org.javamoney.tck.tests.internal.TestCurrencyUnit;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import javax.money.CurrencyUnit;
import javax.money.MonetaryCurrencies;
import javax.money.UnknownCurrencyException;
import javax.money.convert.*;
import java.util.Locale;

/**
 * Tests for Exchange Rates and Rate Providers.
 * Created by Anatole on 10.03.14.
 */
@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class ExchangeRatesAndRateProvidersTest{

    // *************************** A. Test Basic MonetaryConversions Accessors *********************************

    private static final CurrencyUnit FOO_UNIT = new TestCurrencyUnit("FOO");

    /**
     * Test access to conversion rates.<p>
     * Hint: this assertion will require multiple tests to be written!
     */
    @Test
    @SpecAssertion(id = "433-A1", section = "4.3.3")
    public void testAccessKnownRates(){
        ExchangeRateProvider prov = MonetaryConversions.getExchangeRateProvider("TestRateProvider");
        // Use test provider
        for(CurrencyUnit base : MonetaryCurrencies.getCurrencies()){
            if(base.equals(FOO_UNIT)){
                continue;
            }
            ExchangeRate rate = prov.getExchangeRate(base, FOO_UNIT);
            AssertJUnit.assertNotNull(
                    "Identity rate, accessed by getExchangeRate(CurrencyUnit, CurrencyUnit), is not defined for " +
                            base.getCurrencyCode() + " -> " + FOO_UNIT.getCurrencyCode()
            );
            AssertJUnit.assertEquals(rate.getBase().getCurrencyCode(), base.getCurrencyCode());
            AssertJUnit.assertEquals(rate.getTerm().getCurrencyCode(), FOO_UNIT.getCurrencyCode());
            AssertJUnit.assertEquals(rate.getFactor().intValueExact(), 2);
        }
    }

    /**
     * Test access to conversion rates.<p>
     * Hint: this assertion will require multiple tests to be written!
     */
    @Test
    @SpecAssertion(id = "433-A1", section = "4.3.3")
    public void testAccessKnownRatesWithCodes(){
        ExchangeRateProvider prov = MonetaryConversions.getExchangeRateProvider("TestConversionProvider");
        // Use test provider
        for(CurrencyUnit base : MonetaryCurrencies.getCurrencies()){
            if(base.equals(FOO_UNIT)){
                continue;
            }
            ExchangeRate rate = prov.getExchangeRate(base.getCurrencyCode(), "XXX");
            AssertJUnit
                    .assertNotNull("Identity rate, accessed by getExchangeRate(String, String), is not defined for " +
                                           base.getCurrencyCode() + " -> " + FOO_UNIT.getCurrencyCode(), rate
                    );
            AssertJUnit.assertEquals(rate.getBase().getCurrencyCode(), base.getCurrencyCode());
            AssertJUnit.assertEquals(rate.getTerm().getCurrencyCode(), "FOO");
            AssertJUnit.assertEquals(rate.getFactor().intValueExact(), 2);
        }
    }

    /**
     * Test access to conversion rates.<p>
     * Hint: this assertion will require multiple tests to be written!
     */
    @Test
    @SpecAssertion(id = "433-A1", section = "4.3.3")
    public void testAccessKnownRatesAndContext(){
        ExchangeRateProvider prov = MonetaryConversions.getExchangeRateProvider("TestConversionProvider");
        // Use test provider
        for(CurrencyUnit base : MonetaryCurrencies.getCurrencies()){
            if(base.equals(FOO_UNIT)){
                continue;
            }
            ExchangeRate rate = prov.getExchangeRate(base, FOO_UNIT);
            AssertJUnit.assertNotNull(
                    "Identity rate, accessed by getExchangeRate(CurrencyUnit, CurrencyUnit, ConversionContext), " +
                            "is not defined for " +
                            base.getCurrencyCode() + " -> " + FOO_UNIT.getCurrencyCode()
            );
            AssertJUnit.assertEquals(rate.getBase().getCurrencyCode(), base.getCurrencyCode());
            AssertJUnit.assertEquals(rate.getTerm().getCurrencyCode(), FOO_UNIT.getCurrencyCode());
            AssertJUnit.assertEquals(rate.getFactor().intValueExact(), 2);
        }
    }

    /**
     * Test access to conversion rates.<p>
     * Hint: this assertion will require multiple tests to be written!
     */
    @Test
    @SpecAssertion(id = "433-A1", section = "4.3.3")
    public void testAccessKnownRatesWithCodesAndContext(){
        ExchangeRateProvider prov = MonetaryConversions.getExchangeRateProvider("TestConversionProvider");
        // Use test provider
        for(CurrencyUnit base : MonetaryCurrencies.getCurrencies()){
            ExchangeRate rate = prov.getExchangeRate(base.getCurrencyCode(), "XXX");
            AssertJUnit
                    .assertNotNull("Identity rate, accessed by getExchangeRate(String, String, ConversionContext), " +
                                           "is not defined for " +
                                           base.getCurrencyCode() + " -> " + FOO_UNIT.getCurrencyCode(), rate
                    );
            AssertJUnit.assertEquals(rate.getBase().getCurrencyCode(), base.getCurrencyCode());
            AssertJUnit.assertEquals(rate.getTerm().getCurrencyCode(), "XXX");
            AssertJUnit.assertEquals(rate.getFactor().intValueExact(), 2);
        }
    }

    /**
     * Test access to conversion rates.<p>
     * Hint: this assertion will require multiple tests to be written!
     */
    @Test
    @SpecAssertion(id = "433-A1", section = "4.3.3")
    public void testAccessRates_IdentityRatesWithUnits(){
        ExchangeRateProvider prov = MonetaryConversions.getExchangeRateProvider(); // Use default provider
        for(CurrencyUnit unit : MonetaryCurrencies.getCurrencies()){
            ExchangeRate rate = prov.getExchangeRate(unit, unit);
            AssertJUnit.assertNotNull(
                    "Identity rate, accessed by getExchangeRate(CurrencyUnit, CurrencyUnit), is not defined for " +
                            unit.getCurrencyCode()
            );
        }
    }

    /**
     * Test access to conversion rates.<p>
     * Hint: this assertion will require multiple tests to be written!
     */
    @Test
    @SpecAssertion(id = "433-A1", section = "4.3.3")
    public void testAccessRates_IdentityRatesWithCodes(){
        ExchangeRateProvider prov = MonetaryConversions.getExchangeRateProvider(); // Use default provider
        for(CurrencyUnit unit : MonetaryCurrencies.getCurrencies()){
            ExchangeRate rate = prov.getExchangeRate(unit.getCurrencyCode(), unit.getCurrencyCode());
            AssertJUnit.assertNotNull(
                    "Identity rate, accessed by getExchangeRate(String, String), is not defined for " +
                            unit.getCurrencyCode()
            );
        }
    }

    /**
     * Test access to conversion rates.<p>
     * Hint: this assertion will require multiple tests to be written!
     */
    @Test
    @SpecAssertion(id = "433-A1", section = "4.3.3")
    public void testAccessRates_IdentityRatesWithUnitsAndContext(){
        ExchangeRateProvider prov = MonetaryConversions.getExchangeRateProvider(); // Use default provider
        for(CurrencyUnit unit : MonetaryCurrencies.getCurrencies()){
            ExchangeRate rate = prov.getExchangeRate(unit, unit);
            AssertJUnit.assertNotNull(
                    "Identity rate, accessed by getExchangeRate(CurrencyUnit, CurrencyUnit, ConversionContext), " +
                            "is not defined for " +
                            unit.getCurrencyCode()
            );
        }
    }

    /**
     * Test access to conversion rates.<p>
     * Hint: this assertion will require multiple tests to be written!
     */
    @Test
    @SpecAssertion(id = "433-A1", section = "4.3.3")
    public void testAccessRates_IdentityRatesWithCodesAndContext(){
        ExchangeRateProvider prov = MonetaryConversions.getExchangeRateProvider(); // Use default provider
        for(CurrencyUnit unit : MonetaryCurrencies.getCurrencies()){
            ExchangeRate rate = prov.getExchangeRate(unit.getCurrencyCode(), unit.getCurrencyCode());
            AssertJUnit
                    .assertNotNull("Identity rate, accessed by getExchangeRate(String, String, ConversionContext), " +
                                           "is not defined for " +
                                           unit.getCurrencyCode()
                    );
        }
    }

    /**
     * Ensure additional ConversionContext is passed correctly to SPIs.<p>
     * Hint: this assertion will require some custom SPIs to be registered and selected for chain inclusion!
     */
    @Test
    @SpecAssertion(id = "433-A2", section = "4.3.3")
    public void testPassingOverConversionContextToSPIs(){
        ExchangeRateProvider prov = MonetaryConversions.getExchangeRateProvider("TestConversionProvider");
        ConversionQuery ctx =
                new ConversionQuery.Builder().set(Locale.CANADA).set("Foo", "bar").setBaseCurrency(FOO_UNIT)
                        .setTermCurrency(MonetaryCurrencies.getCurrency("XXX")).build();
        ExchangeRate rate = prov.getExchangeRate(ctx);
        AssertJUnit.assertNotNull(
                "No test rate returned by getExchangeRate(CurrencyUnit, CurrencyUnit, ConversionContext), " +
                        "probably TestProvider is not correct registered."
        );
        AssertJUnit.assertEquals(
                "Text parameter Locale.class was not correctly passed to ExchangeRateProvider implementation.", "bar",
                rate.getConversionContext().getText("Foo"));
        AssertJUnit.assertEquals(
                "Object parameter Locale.class was not correctly passed to ExchangeRateProvider implementation.",
                Locale.CANADA, rate.getConversionContext().get(Locale.class));
    }


    /**
     * Bad case: try accessing rates with inconsistent/invalid data.<p>
     * Hint: this assertion will require multiple tests to be written!
     */
    @Test
    @SpecAssertion(id = "433-A3", section = "4.3.3")
    public void testInvalidUsage_InvalidSourceCurrency(){
        for(String providerID : MonetaryConversions.getProviderNames()){
            if("TestConversionProvider".equals(providerID)){
                continue;
            }
            ExchangeRateProvider prov = MonetaryConversions.getExchangeRateProvider(providerID);
            try{
                prov.getExchangeRate("dhdjbdjd", "CHF");
                Assert.fail(
                        "ExchangeRateProvider should throw UnknownCurrencyException when an invalid source currency " +
                                "is passed to getExchangeRate(String,String), provider: " +
                                providerID
                );
            }
            catch(UnknownCurrencyException e){
                // OK
            }
        }
    }

    /**
     * Bad case: try accessing rates with inconsistent/invalid data.<p>
     * Hint: this assertion will require multiple tests to be written!
     */
    @Test
    @SpecAssertion(id = "433-A3", section = "4.3.3")
    public void testInvalidUsage_NullSourceCurrency(){
        for(String providerID : MonetaryConversions.getProviderNames()){
            if("TestConversionProvider".equals(providerID)){
                continue;
            }
            ExchangeRateProvider prov = MonetaryConversions.getExchangeRateProvider(providerID);
            try{
                prov.getExchangeRate(null, "CHF");
                Assert.fail("ExchangeRateProvider should throw NullPointerException when an null source currency " +
                                    "is passed to getExchangeRate(String,String), provider: " +
                                    providerID
                );
            }
            catch(NullPointerException e){
                // OK
            }
        }
    }

    /**
     * Bad case: try accessing rates with inconsistent/invalid data.<p>
     * Hint: this assertion will require multiple tests to be written!
     */
    @Test
    @SpecAssertion(id = "433-A3", section = "4.3.3")
    public void testInvalidUsage_InvalidTargetCurrency(){
        for(String providerID : MonetaryConversions.getProviderNames()){
            if("TestConversionProvider".equals(providerID)){
                continue;
            }
            ExchangeRateProvider prov = MonetaryConversions.getExchangeRateProvider(providerID);
            try{
                prov.getExchangeRate("CHF", "dhdjbdjd");
                Assert.fail(
                        "ExchangeRateProvider should throw UnknownCurrencyException when an invalid target currency " +
                                "is passed to getExchangeRate(String,String), provider: " +
                                providerID
                );
            }
            catch(UnknownCurrencyException e){
                // OK
            }
        }
    }

    /**
     * Bad case: try accessing rates with inconsistent/invalid data.<p>
     * Hint: this assertion will require multiple tests to be written!
     */
    @Test
    @SpecAssertion(id = "433-A3", section = "4.3.3")
    public void testInvalidUsage_NullTargetCurrency(){
        for(String providerID : MonetaryConversions.getProviderNames()){
            if("TestConversionProvider".equals(providerID)){
                continue;
            }
            ExchangeRateProvider prov = MonetaryConversions.getExchangeRateProvider(providerID);
            try{
                prov.getExchangeRate("CHF", null);
                Assert.fail("ExchangeRateProvider should throw NullPointerException when an null target currency " +
                                    "is passed to getExchangeRate(String,String), provider: " +
                                    providerID
                );
            }
            catch(NullPointerException e){
                // OK
            }
        }
    }

    /**
     * Bad case: try accessing rates with inconsistent/invalid data.<p>
     * Hint: this assertion will require multiple tests to be written!
     */
    @Test
    @SpecAssertion(id = "433-A3", section = "4.3.3")
    public void testInvalidUsage_InvalidSourceCurrencyAndContext(){
        for(String providerID : MonetaryConversions.getProviderNames()){
            if("TestConversionProvider".equals(providerID)){
                continue;
            }
            ExchangeRateProvider prov = MonetaryConversions.getExchangeRateProvider(providerID);
            try{
                prov.getExchangeRate("dhdjbdjd", "CHF");
                Assert.fail(
                        "ExchangeRateProvider should throw UnknownCurrencyException when an invalid source currency " +
                                "is passed to getExchangeRate(String,String,ConversionContext), provider: " +
                                providerID
                );
            }
            catch(UnknownCurrencyException e){
                // OK
            }
        }
    }

    /**
     * Bad case: try accessing rates with inconsistent/invalid data.<p>
     * Hint: this assertion will require multiple tests to be written!
     */
    @Test
    @SpecAssertion(id = "433-A3", section = "4.3.3")
    public void testInvalidUsage_NullSourceCurrencyAndContext(){
        for(String providerID : MonetaryConversions.getProviderNames()){
            if("TestConversionProvider".equals(providerID)){
                continue;
            }
            ExchangeRateProvider prov = MonetaryConversions.getExchangeRateProvider(providerID);
            try{
                prov.getExchangeRate(null, "CHF");
                Assert.fail("ExchangeRateProvider should throw UnknownCurrencyException when an null source currency " +
                                    "is passed to getExchangeRate(String,String,ConversionContext), provider: " +
                                    providerID
                );
            }
            catch(NullPointerException e){
                // OK
            }
        }
    }

    /**
     * Bad case: try accessing rates with inconsistent/invalid data.<p>
     * Hint: this assertion will require multiple tests to be written!
     */
    @Test
    @SpecAssertion(id = "433-A3", section = "4.3.3")
    public void testInvalidUsage_InvalidTargetCurrencyAndContext(){
        for(String providerID : MonetaryConversions.getProviderNames()){
            if("TestConversionProvider".equals(providerID)){
                continue;
            }
            ExchangeRateProvider prov = MonetaryConversions.getExchangeRateProvider(providerID);
            try{
                prov.getExchangeRate("CHF", "dhdjbdjd");
                Assert.fail(
                        "ExchangeRateProvider should throw UnknownCurrencyException when an invalid target currency " +
                                "is passed to getExchangeRate(String,String,ConversionContext), provider: " +
                                providerID
                );
            }
            catch(UnknownCurrencyException e){
                // OK
            }
        }
    }

    /**
     * Bad case: try accessing rates with inconsistent/invalid data.<p>
     * Hint: this assertion will require multiple tests to be written!
     */
    @Test
    @SpecAssertion(id = "433-A3", section = "4.3.3")
    public void testInvalidUsage_NullTargetCurrencyAndContext(){
        for(String providerID : MonetaryConversions.getProviderNames()){
            if("TestConversionProvider".equals(providerID)){
                continue;
            }
            ExchangeRateProvider prov = MonetaryConversions.getExchangeRateProvider(providerID);
            try{
                prov.getExchangeRate("CHF", null);
                Assert.fail("ExchangeRateProvider should throw NullPointerException when an null target currency " +
                                    "is passed to getExchangeRate(String,String,ConversionContext), provider: " +
                                    providerID
                );
            }
            catch(NullPointerException e){
                // OK
            }
        }
    }

    /**
     * Bad case: try accessing rates with inconsistent/invalid data.<p>
     * Hint: this assertion will require multiple tests to be written!
     */
    @Test
    @SpecAssertion(id = "433-A3", section = "4.3.3")
    public void testInvalidUsage_NullContext(){
        for(String providerID : MonetaryConversions.getProviderNames()){
            if("TestConversionProvider".equals(providerID)){
                continue;
            }
            ExchangeRateProvider prov = MonetaryConversions.getExchangeRateProvider(providerID);
            try{
                prov.getExchangeRate(null);
                Assert.fail("ExchangeRateProvider should throw NullPointerException when an null ConversionQuery " +
                                    "is passed to getExchangeRate(ConversionQuery), provider: " +
                                    providerID
                );
            }
            catch(NullPointerException e){
                // OK
            }
        }
    }


    /**
     * Bad case: try accessing rates with inconsistent/invalid data.<p>
     * Hint: this assertion will require multiple tests to be written!
     */
    @Test
    @SpecAssertion(id = "433-A3", section = "4.3.3")
    public void testInvalidUsage_NullSourceCurrencyUnit(){
        for(String providerID : MonetaryConversions.getProviderNames()){
            ExchangeRateProvider prov = MonetaryConversions.getExchangeRateProvider(providerID);
            try{
                prov.getExchangeRate(null, MonetaryCurrencies.getCurrency("CHF"));
                Assert.fail("ExchangeRateProvider should throw NullPointerException when an null source currency " +
                                    "is passed to getExchangeRate(CurrencyUnit,CurrencyUnit), provider: " +
                                    providerID
                );
            }
            catch(NullPointerException e){
                // OK
            }
        }
    }

    /**
     * Bad case: try accessing rates with inconsistent/invalid data.<p>
     * Hint: this assertion will require multiple tests to be written!
     */
    @Test
    @SpecAssertion(id = "433-A3", section = "4.3.3")
    public void testInvalidUsage_NullTargetCurrencyUnit(){
        for(String providerID : MonetaryConversions.getProviderNames()){
            ExchangeRateProvider prov = MonetaryConversions.getExchangeRateProvider(providerID);
            try{
                prov.getExchangeRate(MonetaryCurrencies.getCurrency("CHF"), null);
                Assert.fail(
                        "ExchangeRateProvider should throw NullPointerException when an invalid target currency " +
                                "is passed to getExchangeRate(CurrencyUnit,CurrencyUnit), provider: " +
                                providerID
                );
            }
            catch(NullPointerException e){
                // OK
            }
        }
    }

}
