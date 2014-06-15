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
public class TestRateProvider2 implements ExchangeRateProvider{

    public static final int FACTOR = 2;
    private static ProviderContext PC = new ProviderContext.Builder("TestConversionProvider2", RateType.OTHER).build();
    private static ConversionContext CC = new ConversionContext.Builder(PC, RateType.OTHER).build();

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
            return new TestExchangeRate.Builder(CC).setFactor(new TestNumberValue(FACTOR)).setBase(sourceAmount.getCurrency())
                    .setTerm(term).build();
        }

        @Override
        public CurrencyConversion with(ConversionContext conversionContext){
            return this;
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
    public boolean isAvailable(CurrencyUnit base, CurrencyUnit term, ConversionContext conversionContext){
        Objects.requireNonNull(conversionContext);
        Objects.requireNonNull(base);
        Objects.requireNonNull(term);
        return "EUR".equals(base.getCurrencyCode());
    }

    @Override
    public ExchangeRate getExchangeRate(CurrencyUnit base, CurrencyUnit term, ConversionContext conversionContext){
        Objects.requireNonNull(conversionContext);
        Objects.requireNonNull(base);
        Objects.requireNonNull(term);
        if(isAvailable(base, term, conversionContext)){
            if(conversionContext.getTimestampMillis()!=null){
                return new TestExchangeRate.Builder(conversionContext).setFactor(new TestNumberValue(FACTOR * 100)).setBase(base)
                        .setTerm(term).build();
            }
            return new TestExchangeRate.Builder(conversionContext).setFactor(new TestNumberValue(FACTOR)).setBase(base)
                    .setTerm(term).build();
        }
        return null;
    }

    @Override
    public CurrencyConversion getCurrencyConversion(CurrencyUnit term, ConversionContext conversionContext){
        Objects.requireNonNull(conversionContext);
        Objects.requireNonNull(term);
        return new Conversion(term);
    }

}