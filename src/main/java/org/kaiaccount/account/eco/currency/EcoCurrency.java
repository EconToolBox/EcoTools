package org.kaiaccount.account.eco.currency;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kaiaccount.account.inter.currency.AbstractCurrency;
import org.kaiaccount.account.inter.currency.CurrencyBuilder;
import org.kaiaccount.account.inter.io.Serializer;

import java.io.File;

public class EcoCurrency extends AbstractCurrency<EcoCurrency> {

	public EcoCurrency(@NotNull CurrencyBuilder builder) {
		super(builder);
	}

	@Nullable
	String getRawDisplayNameShort() {
		return this.shortDisplay;
	}

	@Nullable
	String getRawDisplayNameSingle() {
		return this.singleDisplay;
	}

	@Nullable
	String getRawDisplayNameMultiple() {
		return this.multiDisplay;
	}

	@Override
	public Serializer<EcoCurrency> getSerializer() {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public File getFile() {
		return new File("plugins/eco/currencies/" + this.getPlugin().getName() + "/" + this.getKeyName() + ".yml");
	}
}
