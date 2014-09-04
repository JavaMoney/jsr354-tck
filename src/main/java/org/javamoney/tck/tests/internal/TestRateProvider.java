/*
 * Copyright (c) 2012, 2013, Werner Keil, Credit Suisse (Anatole Tresch). Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. Contributors: Anatole Tresch - initial version.
 */
package org.javamoney.tck.tests.internal;

import org.javamoney.tck.tests.conversion.TestExchangeRate;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.MonetaryCurrencies;
import javax.money.convert.*;
import java.util.Objects;

/**
 * Test ExchangeProvider. Created by Anatole on 26.04.2014.
 */
public class TestRateProvider implements ExchangeRateProvider{

    private ProviderContext PC = ProviderContextBuilder.of("TestRateProvider", RateType.OTHER).build();
    private ConversionContext CC = ConversionContextBuilder.create(PC, RateType.OTHER).build();
    private CurrencyUnit TERM = new TestCurrencyUnit("FOO");

    private CurrencyConversion CONVERSION = new CurrencyConversion(){

        @Override
        public CurrencyUnit getCurrency(){
            return TERM;
        }

        @Override
        public ConversionContext getConversionContext(){
            return CC;
        }

        @Override
        public ExchangeRate getExchangeRate(MonetaryAmount sourceAmount){
            return new TestExchangeRate.Builder(CC).setFactor(new TestNumberValue(2))
                    .setBase(sourceAmount.getCurrency()).setTerm(TERM).build();
        }

        @Override
        public MonetaryAmount apply(MonetaryAmount value){
            return value.multiply(2).getFactory().setCurrency(TERM).create();
        }
    };

    @Override
    public ProviderContext getProviderContext(){
        return PC;
    }

    @Override
    public boolean isAvailable(CurrencyUnit base, CurrencyUnit term){
        Objects.requireNonNull(base);
        Objects.requireNonNull(term);
        return "FOO".equals(term.getCurrencyCode()) || "XXX".equals(term.getCurrencyCode());
    }

    @Override
    public boolean isAvailable(ConversionQuery conversionContext){
        Objects.requireNonNull(conversionContext);
        Objects.requireNonNull(conversionContext.getCurrency());
        return "FOO".equals(conversionContext.getCurrency().getCurrencyCode()) ||
                "XXX".equals(conversionContext.getCurrency().getCurrencyCode());
    }

    @Override
    public boolean isAvailable(String baseCode, String termCode){
        return "Foo".equals(termCode) || "XXX".equals(termCode);
    }


    @Override
    public ExchangeRate getExchangeRate(CurrencyUnit base, CurrencyUnit term){
        if(isAvailable(base, term)){
            return new TestExchangeRate.Builder(CC).setFactor(new TestNumberValue(2)).setBase(base).setTerm(term)
                    .build();
        }
        return null;
    }

    @Override
    public ExchangeRate getExchangeRate(ConversionQuery conversionQuery){
        if(isAvailable(conversionQuery)){
            return new TestExchangeRate.Builder(
                    ConversionContextBuilder.create(getProviderContext(), RateType.OTHER).importContext(conversionQuery)
                            .build()).setFactor(new TestNumberValue(2)).setBase(conversionQuery.getBaseCurrency())
                    .setTerm(conversionQuery.getCurrency()).build();
        }
        return null;
    }

    @Override
    public ExchangeRate getExchangeRate(String baseCode, String termCode){
        if(isAvailable(baseCode, termCode)){
            return getExchangeRate(MonetaryCurrencies.getCurrency(baseCode), TERM);
        }
        return null;
    }

    @Override
    public ExchangeRate getReversed(ExchangeRate rate){
        return null;
    }

    @Override
    public CurrencyConversion getCurrencyConversion(CurrencyUnit term){
        return CONVERSION;
    }

    @Override
    public CurrencyConversion getCurrencyConversion(ConversionQuery conversionQuery){
        if(isAvailable(conversionQuery)){
            return CONVERSION;
        }
        return null;
    }

    @Override
    public CurrencyConversion getCurrencyConversion(String termCode){
        if(TERM.getCurrencyCode().equals(termCode)){
            return CONVERSION;
        }
        return null;
    }

}
