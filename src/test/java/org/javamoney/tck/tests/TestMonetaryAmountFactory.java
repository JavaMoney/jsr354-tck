package org.javamoney.tck.tests;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import javax.money.MonetaryContext;
import javax.money.MonetaryOperator;
import javax.money.MonetaryQuery;
import javax.money.NumberValue;

public class TestMonetaryAmountFactory {

	private Number value;
	private Currency currency;

	public static MonetaryAmount getAmount(final Number value,
			final Currency currency) {
		return new MonetaryAmount() {

			@Override
			public MonetaryAmount with(MonetaryOperator operator) {
				return this;
			}

			@Override
			public MonetaryAmount subtract(MonetaryAmount amount) {
				return this;
			}

			@Override
			public MonetaryAmount stripTrailingZeros() {
				return this;
			}

			@Override
			public int signum() {
				return 0;
			}

			@Override
			public MonetaryAmount scaleByPowerOfTen(int power) {
				return this;
			}

			@Override
			public MonetaryAmount remainder(Number divisor) {
				return this;
			}

			@Override
			public MonetaryAmount remainder(double divisor) {
				return this;
			}

			@Override
			public MonetaryAmount remainder(long divisor) {
				return this;
			}

			@Override
			public <R> R query(MonetaryQuery<R> query) {
				return null;
			}

			@Override
			public MonetaryAmount plus() {
				return this;
			}

			@Override
			public MonetaryAmount negate() {
				return this;
			}

			@Override
			public MonetaryAmount multiply(Number multiplicand) {
				return this;
			}

			@Override
			public MonetaryAmount multiply(double multiplicand) {
				return this;
			}

			@Override
			public MonetaryAmount multiply(long multiplicand) {
				return this;
			}

			@Override
			public boolean isZero() {
				return false;
			}

			@Override
			public boolean isPositiveOrZero() {
				return true;
			}

			@Override
			public boolean isPositive() {
				return true;
			}

			@Override
			public boolean isNegativeOrZero() {
				return false;
			}

			@Override
			public boolean isNegative() {
				return false;
			}

			@Override
			public boolean isLessThanOrEqualTo(MonetaryAmount amt) {
				return false;
			}

			@Override
			public boolean isLessThan(MonetaryAmount amount) {
				return false;
			}

			@Override
			public boolean isGreaterThanOrEqualTo(MonetaryAmount amount) {
				return false;
			}

			@Override
			public boolean isGreaterThan(MonetaryAmount amount) {
				return false;
			}

			@Override
			public boolean isEqualTo(MonetaryAmount amount) {
				return false;
			}

			@Override
			public NumberValue getNumber() {
				return new NumberValue() {

					private static final long serialVersionUID = 1L;

					@Override
					public long longValue() {
						return value.longValue();
					}

					@Override
					public int intValue() {
						return value.intValue();
					}

					@Override
					public float floatValue() {
						return value.floatValue();
					}

					@Override
					public double doubleValue() {
						return value.doubleValue();
					}

					@Override
					public <T extends Number> T numberValueExact(
							Class<T> numberType) {
						return null;
					}

					@Override
					public <T extends Number> T numberValue(Class<T> numberType) {
						if (numberType.equals(Integer.class)) {
							return (T) Integer.valueOf(value.intValue());
						}
						if (numberType.equals(BigInteger.class)) {
							return (T) BigInteger.valueOf(value.intValue());
						}
						if (numberType.equals(BigDecimal.class)) {
							return (T) BigDecimal.valueOf(value.doubleValue());
						}
						throw new UnsupportedOperationException(
								numberType.getCanonicalName());
					}

					@Override
					public long longValueExact() {
						return value.longValue();
					}

					@Override
					public int intValueExact() {
						return value.intValue();
					}

					@Override
					public int getScale() {
						return 0;
					}

					@Override
					public int getPrecision() {
						return 0;
					}

					@Override
					public Class<?> getNumberType() {
						return null;
					}

					@Override
					public double doubleValueExact() {
						return value.doubleValue();
					}
				};
			}

			@Override
			public MonetaryContext getMonetaryContext() {
				return null;
			}

			@Override
			public MonetaryAmountFactory<? extends MonetaryAmount> getFactory() {
				return null;
			}

			@Override
			public CurrencyUnit getCurrency() {
				return new CurrencyUnit() {

					@Override
					public String getCurrencyCode() {
						return currency.getCurrencyCode();
					}

					@Override
					public int getNumericCode() {
						return currency.getNumericCode();
					}

					@Override
					public int getDefaultFractionDigits() {
						return currency.getDefaultFractionDigits();
					}

                    @Override
                    public int compareTo(CurrencyUnit o){
                        return 0;
                    }
                };
			}

			@Override
			public MonetaryAmount divideToIntegralValue(Number divisor) {
				return this;
			}

			@Override
			public MonetaryAmount divideToIntegralValue(double divisor) {
				return this;
			}

			@Override
			public MonetaryAmount divideToIntegralValue(long divisor) {
				return this;
			}

			@Override
			public MonetaryAmount[] divideAndRemainder(Number divisor) {
				return new MonetaryAmount[0];
			}

			@Override
			public MonetaryAmount[] divideAndRemainder(double divisor) {
				return new MonetaryAmount[0];
			}

			@Override
			public MonetaryAmount[] divideAndRemainder(long divisor) {
				return new MonetaryAmount[0];
			}

			@Override
			public MonetaryAmount divide(Number divisor) {
				return this;
			}

			@Override
			public MonetaryAmount divide(double divisor) {
				return this;
			}

			@Override
			public MonetaryAmount divide(long divisor) {
				return this;
			}

			@Override
			public MonetaryAmount add(MonetaryAmount amount) {
				return this;
			}

			@Override
			public MonetaryAmount abs() {
				return this;
			}

            @Override
            public int compareTo(MonetaryAmount o){
                return 0;
            }
        };
	}
}