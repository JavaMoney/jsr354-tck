package org.javamoney.tck.tests.internal;

import javax.money.NumberValue;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Created by Anatole on 26.04.2014.
 */
public final class TestNumberValue extends NumberValue{
    private static final long serialVersionUID = 1L;

    private BigDecimal value;

    public TestNumberValue(Number value){
        Objects.requireNonNull(value);
        this.value = new BigDecimal(String.valueOf(value));
    }

    @Override
    public long longValue(){
        return value.longValue();
    }

    @Override
    public int intValue(){
        return value.intValue();
    }

    @Override
    public float floatValue(){
        return value.floatValue();
    }

    @Override
    public double doubleValue(){
        return value.doubleValue();
    }

    @Override
    public <T extends Number> T numberValueExact(Class<T> numberType){
        return null;
    }

    @Override
    public <T extends Number> T numberValue(Class<T> numberType){
        if(numberType.equals(Integer.class)){
            return (T) Integer.valueOf(value.intValue());
        }
        if(numberType.equals(BigInteger.class)){
            return (T) BigInteger.valueOf(value.intValue());
        }
        if(numberType.equals(BigDecimal.class)){
            return (T) BigDecimal.valueOf(value.doubleValue());
        }
        throw new UnsupportedOperationException(numberType.getCanonicalName());
    }

    @Override
    public long longValueExact(){
        return value.longValue();
    }

    @Override
    public int intValueExact(){
        return value.intValue();
    }

    @Override
    public int getScale(){
        return value.scale();
    }

    @Override
    public int getPrecision(){
        return value.precision();
    }

    @Override
    public Class<?> getNumberType(){
        return BigDecimal.class;
    }

    @Override
    public double doubleValueExact(){
        return value.doubleValue();
    }

    @Override
    public String toString(){
        return this.value.toString();
    }
}
