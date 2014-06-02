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

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import javax.money.MonetaryCurrencies;
import javax.money.MonetaryException;
import javax.money.convert.*;

import static org.testng.AssertJUnit.*;

/**
 * Created by Anatole on 10.03.14.
 */
@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class MonetaryConversionsTest{

    // ************************************* A. Test Basic MonetaryConversions Accessors *****************************

    /**
     * Ensure at least one conversion provider is available.<br/>
     * Hint: ignore all TCK test providers, only count up productive providers.
     */
    @Test
    @SpecAssertion(id = "431-A1", section = "4.3.1")
    public void testProvidersAvailable(){
        int providerCount = 0;
        for(String providername : MonetaryConversions.getProviderNames()){
            if(!"TestRateProvider".equals(providername)){
                providerCount++;
            }
        }
        assertTrue("At least one conversion provider must be available/registered.", providerCount > 0);
    }

    /**
     * Access and test different Currency Conversions for the provider in place.<br/>
     * Test TCK providers, but also test implementation providers. Doing the ladder it
     * is not possible to test the rates quality, just that rates are returned if necessary.
     */
    @Test
    @SpecAssertion(id = "431-A2", section = "4.3.1")
    public void testConversionsAreAvailable(){
        for(String providerName : MonetaryConversions.getProviderNames()){
            CurrencyConversion conv = MonetaryConversions.getConversion("XXX", providerName);
            assertNotNull("CurrencyConversion mot accessible: " + providerName, conv);
        }
    }

    /**
     * Access and test different Currency Conversions for the provider in place.<br/>
     * Test TCK providers, but also test implementation providers. Doing the ladder it
     * is not possible to test the rates quality, just that rates are returned if necessary.
     */
    @Test
    @SpecAssertion(id = "431-A2", section = "4.3.1")
    public void testConversionsAreAvailableWithContext(){
        for(String providerName : MonetaryConversions.getProviderNames()){
            CurrencyConversion conv = MonetaryConversions.getConversion("XXX", ConversionContext.of(), providerName);
            assertNotNull("CurrencyConversion mot accessible: " + providerName, conv);
        }
    }

    /**
     * Test if all providers returns valid meta data.
     *
     * @see javax.money.convert.ProviderContext
     */
    @Test
    @SpecAssertion(id = "431-A3", section = "4.3.1")
    public void testProviderMetadata(){
        for(String providerName : MonetaryConversions.getProviderNames()){
            ExchangeRateProvider prov = MonetaryConversions.getExchangeRateProvider(providerName);
            assertNotNull("Provider mot accessible: " + providerName, prov);
            ProviderContext ctx = prov.getProviderContext();
            assertNotNull("ExchangeProvider must return a valid ProviderContext, but returned null: " + providerName,
                          ctx);
            assertEquals("ExchangeProvider's ProviderContext returns invalid name: " + providerName,
                         providerName, ctx.getProvider());
            assertNotNull("ExchangeProvider's ProviderContext declares invalid RateTypes to be returned (null): " +
                                  providerName, ctx.getRateTypes());
            assertFalse("ExchangeProvider's ProviderContext declares empty RateTypes to be returned: " + providerName,
                       ctx.getRateTypes().isEmpty());
        }
    }

    /**
     * Test if all providers returns valid meta data.
     *
     * @see javax.money.convert.ProviderContext
     */
    @Test
    @SpecAssertion(id = "431-A3", section = "4.3.1")
    public void testProviderMetadata2(){
        for(String providerName : MonetaryConversions.getProviderNames()){
            CurrencyConversion conv = MonetaryConversions.getConversion("XXX", providerName);
            ConversionContext ctx = conv.getConversionContext();
            assertNotNull("ExchangeProvider must return a valid ProviderContext, but returned null: " + providerName,
                          ctx);
            assertEquals("ExchangeProvider's ProviderContext returns invalid name: " + providerName,
                         providerName, ctx.getProvider());
            assertNotNull("ExchangeProvider's ProviderContext declares invalid RateTypes to be returned (null): " +
                                  providerName, ctx.getRateType());
        }
    }

    /**
     * Test if all providers returns valid meta data.
     *
     * @see javax.money.convert.ProviderContext
     */
    @Test
    @SpecAssertion(id = "431-A3", section = "4.3.1")
    public void testProviderMetadata3(){
        for(String providerName : MonetaryConversions.getProviderNames()){
            CurrencyConversion conv = MonetaryConversions.getConversion(MonetaryCurrencies.getCurrency("XXX"), providerName);
            ConversionContext ctx = conv.getConversionContext();
            assertNotNull("ExchangeProvider must return a valid ProviderContext, but returned null: " + providerName,
                          ctx);
            assertEquals("ExchangeProvider's ProviderContext returns invalid name: " + providerName,
                         providerName, ctx.getProvider());
            assertNotNull("ExchangeProvider's ProviderContext declares invalid RateTypes to be returned (null): " +
                                  providerName, ctx.getRateType());
        }
    }

    /**
     * Test if all providers returns valid meta data.
     *
     * @see javax.money.convert.ProviderContext
     */
    @Test
    @SpecAssertion(id = "431-A3", section = "4.3.1")
    public void testProviderMetadata2WithContext(){
        for(String providerName : MonetaryConversions.getProviderNames()){
            CurrencyConversion conv =
                    MonetaryConversions.getConversion("XXX", ConversionContext.of(), providerName);
            ConversionContext ctx = conv.getConversionContext();
            assertNotNull("ExchangeProvider must return a valid ProviderContext, but returned null: " + providerName,
                          ctx);
            assertEquals("ExchangeProvider's ProviderContext returns invalid name: " + providerName,
                         providerName, ctx.getProvider());
            assertNotNull("ExchangeProvider's ProviderContext declares invalid RateTypes to be returned (null): " +
                                  providerName, ctx.getRateType());
        }
    }

    /**
     * Test if all providers returns valid meta data.
     *
     * @see javax.money.convert.ProviderContext
     */
    @Test
    @SpecAssertion(id = "431-A3", section = "4.3.1")
    public void testProviderMetadata3WithContext(){
        for(String providerName : MonetaryConversions.getProviderNames()){
            CurrencyConversion conv = MonetaryConversions
                    .getConversion(MonetaryCurrencies.getCurrency("XXX"), ConversionContext.of(), providerName);
            ConversionContext ctx = conv.getConversionContext();
            assertNotNull("ExchangeProvider must return a valid ProviderContext, but returned null: " + providerName,
                          ctx);
            assertEquals("ExchangeProvider's ProviderContext returns invalid name: " + providerName,
                         providerName, ctx.getProvider());
            assertNotNull("ExchangeProvider's ProviderContext declares invalid RateTypes to be returned (null): " +
                                  providerName, ctx.getRateType());
        }
    }


    /**
     * Access the default provider chain. Compare with entries from javamoney.properties. The chain must not be empty!
     */
    @Test
    @SpecAssertion(id = "431-A4", section = "4.3.1")
    public void testDefaultProviderChainIsDefined(){
        ExchangeRateProvider prov = MonetaryConversions.getExchangeRateProvider();
        assertNotNull("No default ExchangeRateProvider returned.", prov);
        // we cannot test more here...
    }

    /**
     * Access the default provider chain. Compare with entries from javamoney.properties. The chain must not be empty!
     */
    @Test
    @SpecAssertion(id = "431-A4", section = "4.3.1")
    public void testDefaultProviderChainIsDefinedDefault(){
        CurrencyConversion conv = MonetaryConversions
                .getConversion(MonetaryCurrencies.getCurrency("CHF"));
        assertNotNull("No default CurrencyConversion returned.", conv);
        // we cannot test more here...
    }

    /**
     * Access the default provider chain. Compare with entries from javamoney.properties. The chain must not be empty!
     */
    @Test
    @SpecAssertion(id = "431-A4", section = "4.3.1")
    public void testDefaultProviderChainIsDefinedDefault2(){
        CurrencyConversion conv = MonetaryConversions
                .getConversion("CHF");
        assertNotNull("No default CurrencyConversion returned.", conv);
        // we cannot test more here...
    }

    /**
     * Access the default provider chain. Compare with entries from javamoney.properties. The chain must not be empty!
     */
    @Test
    @SpecAssertion(id = "431-A4", section = "4.3.1")
    public void testDefaultProviderChainIsDefinedDefaultWithContext(){
        CurrencyConversion conv = MonetaryConversions
                .getConversion(MonetaryCurrencies.getCurrency("CHF"), ConversionContext.of());
        assertNotNull("No default CurrencyConversion returned.", conv);
        // we cannot test more here...
    }

    /**
     * Access the default provider chain. Compare with entries from javamoney.properties. The chain must not be empty!
     */
    @Test
    @SpecAssertion(id = "431-A4", section = "4.3.1")
    public void testDefaultProviderChainIsDefinedDefault2WithContext(){
        CurrencyConversion conv = MonetaryConversions
                .getConversion("CHF", ConversionContext.of());
        assertNotNull("No default CurrencyConversion returned.", conv);
        // we cannot test more here...
    }

    /**
     * Bad case: Test access of an inexistent provider. Should throw a MonetaryException
     */
    @Test(expectedExceptions = MonetaryException.class)
    @SpecAssertion(id = "431-A6", section = "4.3.1",
                   note = "Accessing an invalid provider name, should throw a MonetaryException.")
    public void testUseInvalidProvider(){
        MonetaryConversions.getExchangeRateProvider("Dhdkjdhskljdsudgsdkjgjk sgdsjdg");
    }

    /**
     * Bad case: Test access of an inexistent provider within a chain of providers (all other providers must be valid).
     * Should throw a MonetaryException
     */
    @Test(expectedExceptions = MonetaryException.class)
    @SpecAssertion(id = "431-A7", section = "4.3.1",
                   note = "Accessing an invalid provider name within a name chain, should throw a MonetaryException.")
    public void testUseInvalidProviderWithinChain(){
        MonetaryConversions.getExchangeRateProvider("TestRateProvider", "Dhdkjdhskljdsudgsdkjgjk sgdsjdg");
    }

}
