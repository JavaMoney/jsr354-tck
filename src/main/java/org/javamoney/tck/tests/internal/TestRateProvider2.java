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
import javax.money.convert.*;
import java.util.Objects;

/**
 * Test ExchangeProvider. Created by Anatole on 26.04.2014.
 */
public class TestRateProvider2 implements ExchangeRateProvider{

    public static final int FACTOR = 2;
    private static ProviderContext PC = ProviderContextBuilder.create("TestRateProvider2", RateType.OTHER).build();
    private static ConversionContext CC = ConversionContextBuilder.create(PC, RateType.OTHER).build();

    private static final class Conversion implements CurrencyConversion{

        private CurrencyUnit term;

        private Conversion(CurrencyUnit term){
            Objects.requireNonNull(term);
            this.term = term;
        }

        @Override
        public CurrencyUnit getCurrency(){
            return term;
        }

        @Override
        public ConversionContext getConversionContext(){
            return CC;
        }

        @Override
        public ExchangeRate getExchangeRate(MonetaryAmount sourceAmount){
            return new TestExchangeRate.Builder(CC).setFactor(new TestNumberValue(FACTOR))
                    .setBase(sourceAmount.getCurrency()).setTerm(term).build();
        }

        @Override
        public MonetaryAmount apply(MonetaryAmount value){
            return value.multiply(FACTOR).getFactory().setCurrency(term).create();
        }
    }

    @Override
    public ProviderContext getProviderContext(){
        return PC;
    }

    @Override
    public boolean isAvailable(ConversionQuery conversionQuery){
        Objects.requireNonNull(conversionQuery);
        Objects.requireNonNull(conversionQuery.getBaseCurrency());
        Objects.requireNonNull(conversionQuery.getCurrency());
        return "EUR".equals(conversionQuery.getBaseCurrency().getCurrencyCode());
    }

    @Override
    public ExchangeRate getExchangeRate(ConversionQuery conversionQuery){
        if(isAvailable(conversionQuery)){
            if(conversionQuery.getTimestampMillis() != null){
                return new TestExchangeRate.Builder("TestRateProvider2", RateType.OTHER)
                        .setFactor(new TestNumberValue(FACTOR * 100)).setBase(conversionQuery.getBaseCurrency())
                        .setTerm(conversionQuery.getCurrency()).build();
            }
            return new TestExchangeRate.Builder("TestRateProvider2", RateType.OTHER)
                    .setFactor(new TestNumberValue(FACTOR)).setBase(conversionQuery.getBaseCurrency())
                    .setTerm(conversionQuery.getCurrency()).build();
        }
        return null;
    }

    @Override
    public CurrencyConversion getCurrencyConversion(ConversionQuery conversionContext){
        Objects.requireNonNull(conversionContext);
        Objects.requireNonNull(conversionContext.getCurrency());
        return new Conversion(conversionContext.getCurrency());
    }

}
