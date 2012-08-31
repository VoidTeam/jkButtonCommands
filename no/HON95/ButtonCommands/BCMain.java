package no.HON95.ButtonCommands;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * ButtonCommands plugin for Bukkit.
 * 
 * @author HON95
 * @version 1.8
 * 
 */

public class BCMain extends JavaPlugin {

	public static final String UC_URL = "http://hon95.kodingen.com/Bukkit/Version";

	final PlayerInteractListener playerInteractListener = new PlayerInteractListener(this);
	final RedstoneListener redstoneListener = new RedstoneListener(this);
	final BlockBreakListener blockBreakListener = new BlockBreakListener(this);
	final SignChangeListener signChangeListener = new SignChangeListener(this);
	final Commands commands = new Commands(this);
	final ConfigClass configClass = new ConfigClass(this);

	boolean enable = false;
	boolean updateCheck = false;

	@Override
	public void onEnable() {

		configClass.load();

		if (!enable) {
			this.getLogger().warning("Plugin will be disabled!");
			this.getPluginLoader().disablePlugin(this);
			return;
		}

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(playerInteractListener, this);
		pm.registerEvents(redstoneListener, this);
		pm.registerEvents(blockBreakListener, this);
		pm.registerEvents(signChangeListener, this);

		this.getCommand("buttoncommands").setExecutor(commands);
		this.getCommand("get").setExecutor(commands);
		this.getCommand("creative").setExecutor(commands);
		this.getCommand("survival").setExecutor(commands);
		this.getCommand("invclear").setExecutor(commands);
		this.getCommand("iteminfo").setExecutor(commands);
		this.getCommand("chat").setExecutor(commands);

		if (updateCheck)
			Misc.checkVersion(this, UC_URL);
	}
}
