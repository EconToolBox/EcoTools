package org.kaiaccount.account.eco;

import org.bukkit.plugin.java.JavaPlugin;
import org.kaiaccount.AccountInterface;

public class EcoToolPlugin extends JavaPlugin {


	private static EcoToolPlugin plugin;

	public EcoToolPlugin() {
		plugin = this;
	}

	@Override
	public void onLoad() {
		AccountInterface.setGlobal(new EcoManager());
	}

	@Override
	public void onEnable() {

	}

	public static EcoToolPlugin getPlugin() {
		return plugin;
	}
}
