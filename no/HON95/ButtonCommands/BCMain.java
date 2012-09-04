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

public class BCMain extends JavaPlugin
{

	public static final String UC_URL = "http://hon95.kodingen.com/Bukkit/Version";

	final PlayerInteractListener PIL = new PlayerInteractListener(this);
	final RedstoneListener REL = new RedstoneListener(this);
	final BlockBreakListener BBL = new BlockBreakListener(this);
	final SignChangeListener SCL = new SignChangeListener(this);
	final Commands CMD = new Commands(this);
	final ConfigClass CNF = new ConfigClass(this);

	boolean enable = false;
	boolean updateCheck = false;

	@Override
	public void onEnable()
	{

		CNF.load();

		if (!enable)
		{
			this.getLogger().warning("Plugin will be disabled!");
			this.getPluginLoader().disablePlugin(this);
			return;
		}

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(PIL, this);
		pm.registerEvents(REL, this);
		pm.registerEvents(BBL, this);
		pm.registerEvents(SCL, this);

		this.getCommand("buttoncommands").setExecutor(CMD);
		this.getCommand("get").setExecutor(CMD);
		this.getCommand("creative").setExecutor(CMD);
		this.getCommand("survival").setExecutor(CMD);
		this.getCommand("invclear").setExecutor(CMD);
		this.getCommand("iteminfo").setExecutor(CMD);
		this.getCommand("chat").setExecutor(CMD);

		if (updateCheck)
			Misc.checkVersion(this, UC_URL);
	}
}
