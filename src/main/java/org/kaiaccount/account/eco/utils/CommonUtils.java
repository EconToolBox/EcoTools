package org.kaiaccount.account.eco.utils;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.function.BiFunction;

public class CommonUtils {

	public static BigDecimal calculate(@NotNull Iterator<BigDecimal> bigDecimals,
			@NotNull BiFunction<BigDecimal, BigDecimal, BigDecimal> function) {
		if (!bigDecimals.hasNext()) {
			return BigDecimal.ZERO;
		}
		BigDecimal total = bigDecimals.next();
		while (bigDecimals.hasNext()) {
			total = function.apply(total, bigDecimals.next());
		}
		return total;
	}

	public static BigDecimal sumOf(@NotNull Iterator<BigDecimal> bigDecimals) {
		return calculate(bigDecimals, BigDecimal::add);
	}
}
