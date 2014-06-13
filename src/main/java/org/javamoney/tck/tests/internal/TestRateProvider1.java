package org.javamoney.tck.tests.internal;

import org.javamoney.tck.tests.conversion.TestExchangeRate;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.convert.*;
import java.util.Objects;

/**
 * Created by Anatole on 26.04.2014.
 */
public class TestRateProvider1 implements ExchangeRateProvider{

    public static final int FACTOR = 1;
    private static ProviderContext PC = new ProviderContext.Builder("TestConversionProvider1", RateType.OTHER).build();
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
            return value.getFactory().setCurrency(term).create();
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
        return "CHF".equals(base.getCurrencyCode());
    }

    @Override
    public ExchangeRate getExchangeRate(CurrencyUnit base, CurrencyUnit term, ConversionContext conversionContext){
        Objects.requireNonNull(conversionContext);
        Objects.requireNonNull(base);
        Objects.requireNonNull(term);
        if(isAvailable(base, term, conversionContext)){
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
