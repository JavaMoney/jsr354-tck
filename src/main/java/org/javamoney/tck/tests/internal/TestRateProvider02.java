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
 * Created by Anatole on 26.04.2014.
 */
public class TestRateProvider02 implements ExchangeRateProvider{

    public static final double FACTOR = 0.2;
    private static ProviderContext PC =
            ProviderContextBuilder.create("TestRateProvider02", RateType.OTHER).build();
    private static ConversionContext CC = ConversionContextBuilder.create(PC, RateType.OTHER).build();

    private static final class Conversion implements CurrencyConversion{

        private CurrencyUnit term;

        private Conversion(CurrencyUnit term){
            Objects.requireNonNull(term);
            this.term = term;
        }

        @Override
        public CurrencyUnit getTermCurrency(){
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
        Objects.requireNonNull(conversionQuery.getTermCurrency());
        return true;
    }

    @Override
    public ExchangeRate getExchangeRate(ConversionQuery conversionQuery){
        Objects.requireNonNull(conversionQuery.getBaseCurrency());
        if(isAvailable(conversionQuery)){
            return new TestExchangeRate.Builder(getProviderContext().getProvider(), RateType.OTHER)
                    .setFactor(new TestNumberValue(FACTOR)).setBase(conversionQuery.getBaseCurrency())
                    .setTerm(conversionQuery.getTermCurrency()).build();
        }
        return null;
    }

    @Override
    public CurrencyConversion getCurrencyConversion(ConversionQuery conversionQuery){
        if(isAvailable(conversionQuery)){
            return new Conversion(conversionQuery.getTermCurrency());
        }
        return null;
    }

}
