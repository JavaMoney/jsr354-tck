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
import javax.money.convert.ConversionContext;
import javax.money.convert.ConversionQuery;
import javax.money.convert.ConversionQueryBuilder;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;
import javax.money.convert.ProviderContext;

/**
 * Created by Anatole on 10.03.14.
 */
@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class MonetaryConversionsTest {

    // ************************************* A. Test Basic MonetaryConversions Accessors *****************************

    /**
     * Ensure at least one conversion provider is available.<p>
     * Hint: ignore all TCK test providers, only count up productive providers.
     */
    @Test(description = "4.3.1 Ensure at least one conversion provider is available, TestRateProvider must be present.")
    @SpecAssertion(id = "431-A1", section = "4.3.1")
    public void testProvidersAvailable() {
        int providerCount = 0;
        for (String providername : MonetaryConversions.getProviderNames()) {
            if (!"TestRateProvider".equals(providername)) {
                providerCount++;
            }
        }
        AssertJUnit.assertTrue("At least one conversion provider must be available/registered.", providerCount > 0);
    }

    /**
     * Access and test different Currency Conversions for the provider in place.<p>
     * Test TCK providers, but also test implementation providers. Doing the ladder it
     * is not possible to test the rates quality, just that rates are returned if necessary.
     */
    @Test(description =
            "4.3.1 Access Conversion to term currency code XXX for all providers that support according " +
                    "conversion, if" +
                    "available a non-null CurrencyConversion must be provided.")
    @SpecAssertion(id = "431-A2", section = "4.3.1")
    public void testConversionsAreAvailable() {
        for (String providerName : MonetaryConversions.getProviderNames()) {
            try {
                if (MonetaryConversions.isConversionAvailable("XXX", providerName)) {
                    CurrencyConversion conv = MonetaryConversions.getConversion("XXX", providerName);
                    AssertJUnit.assertNotNull(
                            "CurrencyConversion returned from MonetaryConversions.getConversion(String, " +
                                    "String...) should never be null: " +
                                    providerName, conv);
                    AssertJUnit.assertTrue("CurrencyConversion is not flagged as available, " +
                                    "though it was returned from MonetaryConversions.getConversion" +
                                    "(String," +
                                    " String...): " +
                                    providerName,
                            MonetaryConversions.isConversionAvailable("XXX", providerName));
                }
            } catch (MonetaryException e) {
                AssertJUnit.assertFalse(
                        "CurrencyConversion is not flagged as NOT available, though it is not accessible from " +
                                "MonetaryConversions.getConversion(String, String...): " +
                                providerName, MonetaryConversions.isConversionAvailable("XXX", providerName));
            }
        }
    }

    /**
     * Access and test different Currency Conversions for the provider in place.<p>
     * Test TCK providers, but also test implementation providers. Doing the ladder it
     * is not possible to test the rates quality, just that rates are returned if necessary.
     */
    @Test(description =
            "4.3.1 Access Conversion by query to term currency XXX for all providers that support according " +
                    "conversion, if" +
                    "available a non-null CurrencyConversion must be provided.")
    @SpecAssertion(id = "431-A2", section = "4.3.1")
    public void testConversionsAreAvailableWithQuery() {
        for (String providerName : MonetaryConversions.getProviderNames()) {
            ConversionQuery query =
                    ConversionQueryBuilder.of().setTermCurrency("XXX").setProviderNames(providerName).build();
            try {
                if (MonetaryConversions.isConversionAvailable(query)) {
                    CurrencyConversion conv = MonetaryConversions.getConversion(query);
                    AssertJUnit.assertNotNull(
                            "CurrencyConversion returned from MonetaryConversions.getConversion(ConversionQuery) must" +
                                    " " +
                                    "never be null: " +
                                    providerName, conv);
                    AssertJUnit
                            .assertTrue("CurrencyConversion is not flagged as available though it was returned from " +
                                    "MonetaryConversions.getConversion(ConversionQuery): " +
                                    providerName, MonetaryConversions.isConversionAvailable(query));
                }
            } catch (MonetaryException e) {
                // OK, possible!
                AssertJUnit.assertFalse(
                        "CurrencyConversion is not flagged as not available, though it was not returned from " +
                                "MonetaryConversions.getConversion(ConversionQuery): " +
                                providerName, MonetaryConversions.isConversionAvailable(query));
            }
        }
    }

    /**
     * Test if all providers returns valid meta data.
     *
     * @see javax.money.convert.ProviderContext
     */
    @Test(description = "4.3.1 Test if all ExchangeRateProvider instances returns valid ProviderContext.")
    @SpecAssertion(id = "431-A3", section = "4.3.1")
    public void testProviderMetadata() {
        for (String providerName : MonetaryConversions.getProviderNames()) {
            ExchangeRateProvider prov = MonetaryConversions.getExchangeRateProvider(providerName);
            AssertJUnit.assertNotNull("Provider mot accessible: " + providerName, prov);
            ProviderContext ctx = prov.getContext();
            AssertJUnit.assertNotNull(
                    "ExchangeProvider must return a valid ProviderContext, but returned null: " + providerName, ctx);
            AssertJUnit.assertEquals("ExchangeProvider's ProviderContext returns invalid name: " + providerName,
                    providerName, ctx.getProviderName());
            AssertJUnit.assertNotNull(
                    "ExchangeProvider's ProviderContext declares invalid RateTypes to be returned (null): " +
                            providerName, ctx.getRateTypes());
            AssertJUnit.assertFalse(
                    "ExchangeProvider's ProviderContext declares empty RateTypes to be returned: " + providerName,
                    ctx.getRateTypes().isEmpty());
        }
    }

    /**
     * Test if all providers returns valid meta data.
     *
     * @see javax.money.convert.ProviderContext
     */
    @Test(description = "4.3.1 Test if all CurrencyConversion instances returns valid ConversionContext, accessed by " +
            "currency code.")
    @SpecAssertion(id = "431-A3", section = "4.3.1")
    public void testProviderMetadata2() {
        for (String providerName : MonetaryConversions.getProviderNames()) {
            if (MonetaryConversions.isConversionAvailable("XXX", providerName)) {
                CurrencyConversion conv = MonetaryConversions.getConversion("XXX", providerName);
                ConversionContext ctx = conv.getContext();
                AssertJUnit.assertNotNull(
                        "ExchangeProvider must return a valid ProviderContext, but returned null: " + providerName,
                        ctx);
                AssertJUnit.assertEquals("ExchangeProvider's ProviderContext returns invalid name: " + providerName,
                        providerName, ctx.getProviderName());
                AssertJUnit.assertNotNull(
                        "ExchangeProvider's ProviderContext declares invalid RateTypes to be returned (null): " +
                                providerName, ctx.getRateType());
            }
        }
    }

    /**
     * Test if all providers returns valid meta data.
     *
     * @see javax.money.convert.ProviderContext
     */
    @Test(description = "4.3.1 Test if all CurrencyConversion instances returns valid ConversionContext, accessed by " +
            "CurrencyUnit.")
    @SpecAssertion(id = "431-A3", section = "4.3.1")
    public void testProviderMetadata3() {
        for (String providerName : MonetaryConversions.getProviderNames()) {
            if (MonetaryConversions.isConversionAvailable(MonetaryCurrencies.getCurrency("XXX"), providerName)) {
                CurrencyConversion conv =
                        MonetaryConversions.getConversion(MonetaryCurrencies.getCurrency("XXX"), providerName);
                ConversionContext ctx = conv.getContext();
                AssertJUnit.assertNotNull(
                        "ExchangeProvider must return a valid ProviderContext, but returned null: " + providerName,
                        ctx);
                AssertJUnit.assertEquals("ExchangeProvider's ProviderContext returns invalid name: " + providerName,
                        providerName, ctx.getProviderName());
                AssertJUnit.assertNotNull(
                        "ExchangeProvider's ProviderContext declares invalid RateTypes to be returned (null): " +
                                providerName, ctx.getRateType());
            }
        }
    }

    /**
     * Test if all providers returns valid meta data.
     *
     * @see javax.money.convert.ProviderContext
     */
    @Test(description = "4.3.1 Test if all CurrencyConversion instances returns valid ConversionContext, accessed by " +
            "ConversionQuery/currency code.")
    @SpecAssertion(id = "431-A3", section = "4.3.1")
    public void testProviderMetadata2WithContext() {
        for (String providerName : MonetaryConversions.getProviderNames()) {
            ConversionQuery query = ConversionQueryBuilder.of().
                    setTermCurrency("XXX").setProviderNames(providerName).build();
            if (MonetaryConversions.isConversionAvailable(query)) {
                CurrencyConversion conv = MonetaryConversions.getConversion(query);
                ConversionContext ctx = conv.getContext();
                AssertJUnit.assertNotNull(
                        "ExchangeProvider must return a valid ProviderContext, but returned null: " + providerName,
                        ctx);
                AssertJUnit.assertEquals("ExchangeProvider's ProviderContext returns invalid name: " + providerName,
                        providerName, ctx.getProviderName());
                AssertJUnit.assertNotNull(
                        "ExchangeProvider's ProviderContext declares invalid RateTypes to be returned (null): " +
                                providerName, ctx.getRateType());
            }
        }
    }

    /**
     * Test if all providers returns valid meta data.
     *
     * @see javax.money.convert.ProviderContext
     */
    @Test(description = "4.3.1 Test if all CurrencyConversion instances returns valid ConversionContext, accessed by " +
            "ConversionQuery/CurrencyUnit.")
    @SpecAssertion(id = "431-A3", section = "4.3.1")
    public void testProviderMetadata3WithContext() {
        for (String providerName : MonetaryConversions.getProviderNames()) {
            ConversionQuery query = ConversionQueryBuilder.of().
                    setTermCurrency(MonetaryCurrencies.getCurrency("XXX")).setProviderName(providerName).build();
            if (MonetaryConversions.isConversionAvailable(query)) {
                CurrencyConversion conv = MonetaryConversions.getConversion(query);
                ConversionContext ctx = conv.getContext();
                AssertJUnit.assertNotNull(
                        "ExchangeProvider must return a valid ProviderContext, but returned null: " + providerName,
                        ctx);
                AssertJUnit.assertEquals("ExchangeProvider's ProviderContext returns invalid name: " + providerName,
                        providerName, ctx.getProviderName());
                AssertJUnit.assertNotNull(
                        "ExchangeProvider's ProviderContext declares invalid RateTypes to be returned (null): " +
                                providerName, ctx.getRateType());
            }
        }
    }


    /**
     * Access the default provider chain. Compare with entries from javamoney.properties. The chain must not be empty!
     */
    @Test(description = "4.3.1 Access and test the default conversion provider chain.")
    @SpecAssertion(id = "431-A4", section = "4.3.1")
    public void testDefaultProviderChainIsDefined() {
        ExchangeRateProvider prov = MonetaryConversions.getExchangeRateProvider();
        AssertJUnit.assertNotNull("No default ExchangeRateProvider returned.", prov);
        // we cannot test more here...
    }

    /**
     * Access the default provider chain. Compare with entries from javamoney.properties. The chain must not be empty!
     */
    @Test(description = "4.3.1 Access and test the default conversion provider chain, by accessing a default" +
            "CurrencyConversion for term CurrencyUnit CHF.")
    @SpecAssertion(id = "431-A4", section = "4.3.1")
    public void testDefaultProviderChainIsDefinedDefault() {
        CurrencyConversion conv = MonetaryConversions.getConversion(MonetaryCurrencies.getCurrency("CHF"));
        AssertJUnit.assertNotNull("No default CurrencyConversion returned.", conv);
        // we cannot test more here...
    }

    /**
     * Access the default provider chain. Compare with entries from javamoney.properties. The chain must not be empty!
     */
    @Test(description = "4.3.1 Access and test the default conversion provider chain, by accessing a default" +
            "CurrencyConversion for term currency code CHF.")
    @SpecAssertion(id = "431-A4", section = "4.3.1")
    public void testDefaultProviderChainIsDefinedDefault2() {
        CurrencyConversion conv = MonetaryConversions.getConversion("CHF");
        AssertJUnit.assertNotNull("No default CurrencyConversion returned.", conv);
        // we cannot test more here...
    }

    /**
     * Access the default provider chain. Compare with entries from javamoney.properties. The chain must not be empty!
     */
    @Test(description = "4.3.1 Access and test the default conversion provider chain, by accessing a default" +
            "CurrencyConversion for ConversionQuery.")
    @SpecAssertion(id = "431-A4", section = "4.3.1")
    public void testDefaultProviderChainIsDefinedDefaultWithContext() {
        ConversionQuery query =
                ConversionQueryBuilder.of().setTermCurrency(MonetaryCurrencies.getCurrency("CHF")).build();
        CurrencyConversion conv = MonetaryConversions.getConversion(query);
        AssertJUnit.assertNotNull("No default CurrencyConversion returned.", conv);
        // we cannot test more here...
    }

    /**
     * Access the conversion using the default conversion chain.
     */
    @Test(description = "4.3.1 Access and test conversion using the default provider chain.")
    @SpecAssertion(id = "431-A5", section = "4.3.1")
    public void testDefaultConversion() {
        ConversionQuery query =
                ConversionQueryBuilder.of().setTermCurrency(MonetaryCurrencies.getCurrency("USD")).build();
        CurrencyConversion conv = MonetaryConversions.getConversion(query);
        AssertJUnit.assertNotNull("No default CurrencyConversion returned for USD.", conv);
        query =
                ConversionQueryBuilder.of().setTermCurrency(MonetaryCurrencies.getCurrency("EUR")).build();
        conv = MonetaryConversions.getConversion(query);
        AssertJUnit.assertNotNull("No default CurrencyConversion returned for EUR.", conv);
    }


    /**
     * Bad case: Test access of an inexistent provider. Should throw a MonetaryException
     */
    @Test(expectedExceptions = MonetaryException.class, description = "4.3.1 Bad case: Access invalid " +
            "ExchangeRateProvider, expect MonetaryException thrown, using default provider chain.")
    @SpecAssertion(id = "431-A6", section = "4.3.1",
            note = "Accessing an invalid provider name, should throw a MonetaryException.")
    public void testUseInvalidProvider() {
        MonetaryConversions.getExchangeRateProvider("Dhdkjdhskljdsudgsdkjgjk sgdsjdg");
    }

    /**
     * Bad case: Test access of an inexistent provider within a chain of providers (all other providers must be valid).
     * Should throw a MonetaryException
     */
    @Test(expectedExceptions = MonetaryException.class, description = "4.3.1 Bad case: Access invalid " +
            "ExchangeRateProvider, expect MonetaryException thrown, using explicit provider.")
    @SpecAssertion(id = "431-A7", section = "4.3.1",
            note = "Accessing an invalid provider name within a name chain, should throw a MonetaryException.")
    public void testUseInvalidProviderWithinChain() {
        MonetaryConversions.getExchangeRateProvider("TestRateProvider", "Dhdkjdhskljdsudgsdkjgjk sgdsjdg");
    }

}
