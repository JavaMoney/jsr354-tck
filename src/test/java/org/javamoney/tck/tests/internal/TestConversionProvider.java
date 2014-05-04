package org.javamoney.tck.tests.internal;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.MonetaryCurrencies;
import javax.money.convert.*;

/**
 * Created by Anatole on 26.04.2014.
 */
// TODO why not call it TestRateProvider?
public class TestConversionProvider implements ExchangeRateProvider{

    private ProviderContext PC = new ProviderContext.Builder("TestConversionProvider").create();
    private ConversionContext CC = new ConversionContext.Builder(PC, RateType.OTHER).create();
    private CurrencyUnit TERM = new TestCurrencyUnit("FOO");

    private CurrencyConversion CONVERSION = new CurrencyConversion(){

        @Override
        public CurrencyUnit getTermCurrency(){
            return TERM;
        }

        @Override
        public ConversionContext getConversionContext(){
            return CC;
        }

        @Override
        public ExchangeRate getExchangeRate(MonetaryAmount sourceAmount){
            return new ExchangeRate.Builder(CC).setFactor(new TestNumberValue(2)).setBase(sourceAmount.getCurrency())
                    .setTerm(TERM).create();
        }

        @Override
        public CurrencyConversion with(ConversionContext conversionContext){
            return this;
        }

        @Override
        public <T extends MonetaryAmount> T apply(T value){
            return (T)value.multiply(2).getFactory().setCurrency(TERM).create();
        }
    };

    @Override
    public ProviderContext getProviderContext(){
        return PC;
    }

    @Override
    public boolean isAvailable(CurrencyUnit base, CurrencyUnit term){
        return "FOO".equals(term.getCurrencyCode());
    }

    @Override
    public boolean isAvailable(CurrencyUnit base, CurrencyUnit term, ConversionContext conversionContext){
        return "FOO".equals(term.getCurrencyCode());
    }

    @Override
    public boolean isAvailable(String baseCode, String termCode){
        return false;
    }

    @Override
    public boolean isAvailable(String baseCode, String termCode, ConversionContext conversionContext){
        return false;
    }

    @Override
    public ExchangeRate getExchangeRate(CurrencyUnit base, CurrencyUnit term){
        if(isAvailable(base, term)){
            return new ExchangeRate.Builder(CC).setFactor(new TestNumberValue(2)).setBase(base)
                    .setTerm(TERM).create();
        }
        return null;
    }

    @Override
    public ExchangeRate getExchangeRate(CurrencyUnit base, CurrencyUnit term, ConversionContext conversionContext){
        if(isAvailable(base, term)){
            return getExchangeRate(base,term);
        }
        return null;
    }

    @Override
    public ExchangeRate getExchangeRate(String baseCode, String termCode){
        if(TERM.getCurrencyCode().equals(termCode)){
            return getExchangeRate(MonetaryCurrencies.getCurrency(baseCode),TERM);
        }
        return null;
    }

    @Override
    public ExchangeRate getExchangeRate(String baseCode, String termCode, ConversionContext conversionContext){
        if(TERM.getCurrencyCode().equals(termCode)){
            return getExchangeRate(MonetaryCurrencies.getCurrency(baseCode),TERM);
        }
        return null;
    }

    @Override
    public ExchangeRate getReversed(ExchangeRate rate){
        return null;
    }

    @Override
    public CurrencyConversion getCurrencyConversion(CurrencyUnit term){
        if(TERM.getCurrencyCode().equals(term.getCurrencyCode())){
            return CONVERSION;
        }
        return null;
    }

    @Override
    public CurrencyConversion getCurrencyConversion(CurrencyUnit term, ConversionContext conversionContext){
        if(TERM.getCurrencyCode().equals(term.getCurrencyCode())){
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

    @Override
    public CurrencyConversion getCurrencyConversion(String termCode, ConversionContext conversionContext){
        if(TERM.getCurrencyCode().equals(termCode)){
            return CONVERSION;
        }
        return null;
    }
}
