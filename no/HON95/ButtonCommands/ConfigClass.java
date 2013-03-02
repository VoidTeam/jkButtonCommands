package no.HON95.ButtonCommands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

class ConfigClass
{

	private static BCMain PLUGIN;
	private static YamlConfiguration config;
	private static YamlConfiguration whiteList;
	private static YamlConfiguration alias;
	private static File configFile;
	private static File whiteListFile;
	private static File aliasFile;
	private final String n = System.getProperty("line.separator");

	private final String CONFIG_HEADER = "ButtonCommands v<v> by HON95 #" + n + "BukkitDEV page: http://dev.bukkit.org/server-mods/buttoncommands/" + n + "Config help page: http://dev.bukkit.org/server-mods/buttoncommands/pages/config-file/";

	private final String WHITELIST_HEADER = "ButtonCommands - Redstone & Console Command White-List" + n + "White-List help page: http://dev.bukkit.org/server-mods/buttoncommands/pages/white-list-config/" + n + "" + n + "This config lets you define what redstone and console commands" + n + "you want to white-list. It supports normal YAML lists." + n + "" + n + "console: Commands from \"/console\" or \"/c\" command signs." + n + "redstone: Commands from \"/redstone\" or \"/r\" command signs." + n + "console: Shared for both console and redstone commands signs.";

	private final String ALIAS_HEADER = "ButtonCommands - Alias Command Config" + n + "Alias help page: http://dev.bukkit.org/server-mods/buttoncommands/pages/alias-config/" + n + "" + n + "This config lets you add aliases, or custom commands, to be executed" + n + "from alias command signs. To use them, write \"/alias <theAlias>\"" + n + "on the command sign, where <theAlias> is the alias you wantto execute." + n + "" + n + "You most likely want to check out the help page.";

	ConfigClass(BCMain instance)
	{
		PLUGIN = instance;
	}

	void load()
	{
		initConfig();
		initWhiteList();
		initAlias();
	}

	void initConfig()
	{
		if (!PLUGIN.getDataFolder().exists())
		{
			PLUGIN.getDataFolder().mkdirs();
		}
		configFile = new File(PLUGIN.getDataFolder().getAbsolutePath() + File.separator + "config.yml");
		config = YamlConfiguration.loadConfiguration(configFile);

		config.options().copyDefaults(true);
		config.options().copyHeader(true);
		config.options().header(CONFIG_HEADER.replace("<v>", PLUGIN.getDescription().getVersion()));

		config.addDefault("enable", true);
		config.addDefault("enabledSigns.normal", true);
		config.addDefault("enabledSigns.console", true);
		config.addDefault("enabledSigns.redstone", true);
		config.addDefault("enabledSigns.alias", true);
		config.addDefault("current.hasPerm", false);
		config.addDefault("current.missingPerm", true);
		config.addDefault("ignoreWhiteLists", false);
		config.addDefault("output.console", true);
		config.addDefault("output.redstone", true);
		config.addDefault("protect.sign", true);
		config.addDefault("protect.block", true);
		config.addDefault("signTitleUnderlined", false);
		config.addDefault("signTitleCentered", true);
		config.addDefault("interact.left_click", true);
		config.addDefault("interact.right_click", true);
		config.addDefault("pluginUpdateCheck", true);
		config.addDefault("bonus.ghostLever", false);

		try
		{
			if (!configFile.exists())
				PLUGIN.getDataFolder().mkdirs();
			config.save(configFile);
		}
		catch (Exception ex)
		{
			PLUGIN.getLogger().warning("Unable to save config.yml!");
			PLUGIN.getLogger().warning("Error: " + ex.getMessage());
		}

		PLUGIN.enable = config.getBoolean("pluginUpdateCheck");
		PLUGIN.updateCheck = config.getBoolean("enable");
		PLUGIN.SCL.titleUnderlined = config.getBoolean("signTitleUnderlined");
		PLUGIN.SCL.titleCentered = config.getBoolean("signTitleCentered");
		PLUGIN.PIL.enableNormal = config.getBoolean("enabledSigns.normal");
		PLUGIN.PIL.enableConsole = config.getBoolean("enabledSigns.console");
		PLUGIN.PIL.enableAlias = config.getBoolean("enabledSigns.alias");
		PLUGIN.PIL.enableBL = config.getBoolean("bonus.ghostLever");
		PLUGIN.PIL.curPerm = config.getBoolean("current.hasPerm");
		PLUGIN.PIL.curNoPerm = config.getBoolean("current.missingPerm");
		PLUGIN.PIL.ignoreWhiteLists = config.getBoolean("ignoreWhiteLists");
		PLUGIN.PIL.outputInfo = config.getBoolean("output.redstone");
		PLUGIN.PIL.rightClick = config.getBoolean("interact.right_click");
		PLUGIN.REL.enableRedstone = config.getBoolean("enabledSigns.redstone");
		PLUGIN.REL.ignoreWhiteLists = config.getBoolean("ignoreWhiteLists");
		PLUGIN.REL.outputInfo = config.getBoolean("output.redstone");
		PLUGIN.BBL.protectSign = config.getBoolean("protect.sign");
		PLUGIN.BBL.protectBlock = config.getBoolean("protect.block");
	}

	void initWhiteList()
	{

		whiteListFile = new File(PLUGIN.getDataFolder() + File.separator + "white-list.yml");
		whiteList = YamlConfiguration.loadConfiguration(whiteListFile);
		whiteList.options().copyHeader(true);
		whiteList.options().header(WHITELIST_HEADER);

		boolean saveC = this.checkWhiteListList("console");
		boolean saveR = this.checkWhiteListList("redstone");
		boolean saveS = this.checkWhiteListList("shared");

		if (saveC || saveR || saveS)
		{
			try
			{
				whiteList.save(whiteListFile);
			}
			catch (IOException e)
			{
				PLUGIN.getLogger().warning("Failed to save white-list.yml!");
				PLUGIN.getLogger().warning("Caused by: " + e.getMessage());
			}
		}

		List<String> sha = whiteList.getStringList("shared");
		List<String> con = whiteList.getStringList("console");
		List<String> red = whiteList.getStringList("redstone");
		con.addAll(sha);
		red.addAll(sha);

		PLUGIN.PIL.whiteList = new HashSet<String>(con);
		PLUGIN.REL.whiteList = new HashSet<String>(red);
	}

	void initAlias()
	{

		Alias.BCA.clear();

		aliasFile = new File(PLUGIN.getDataFolder() + File.separator + "alias.yml");
		alias = YamlConfiguration.loadConfiguration(aliasFile);
		alias.options().copyHeader(true);
		alias.options().header(ALIAS_HEADER);

		boolean save = false;
		HashSet<String> cmds = new HashSet<String>();
		cmds.add("cmdA Herp");
		cmds.add("cmdB Derp");

		if (!aliasFile.exists())
		{
			alias.set("derp.enable", false);
			alias.set("derp.commands", new ArrayList<String>(cmds));
			alias.set("derp.current.hasPerm", false);
			alias.set("derp.current.missingPerm", true);
			alias.set("derp.permission", "");
			alias.set("derp.ignoreOtherPerms", false);
			alias.set("derp.ignoreWhiteList", false);
			alias.set("derp.cooldown", 0);

			save = true;
		}
		else
		{
			Iterator<String> ks = alias.getKeys(false).iterator();
			ConfigurationSection cs;
			String ke;
			String nk;
			while (ks.hasNext())
			{
				ke = ks.next();

				if (alias.get(ke) instanceof String)
				{
					if (alias.getString(ke).equalsIgnoreCase("new"))
						alias.set(ke + ".enable", true);
				}

				if (!(alias.get(ke) instanceof ConfigurationSection))
					continue;

				cs = alias.getConfigurationSection(ke);
				if (!cs.isBoolean("enable"))
				{
					cs.set("enable", true);
					save = true;
				}
				if (!cs.isList("commands"))
				{
					cs.set("commands", new ArrayList<String>(cmds));
					save = true;
				}
				if (!cs.isBoolean("current.missingPerm"))
				{
					cs.set("current.missingPerm", true);
					save = true;
				}
				if (!cs.isBoolean("current.hasPerm"))
				{
					cs.set("current.hasPerm", false);
					save = true;
				}
				if (!cs.isString("permission"))
				{
					cs.set("permission", "");
					save = true;
				}
				if (!cs.isBoolean("ignoreOtherPerms"))
				{
					cs.set("ignoreOtherPerms", false);
					save = true;
				}
				if (!cs.isBoolean("ignoreWhiteList"))
				{
					cs.set("ignoreWhiteList", false);
					save = true;
				}
				if (!cs.isInt("cooldown"))
				{
					cs.set("cooldown", 0);
					save = true;
				}
				nk = ke.trim();
				if (!ke.equalsIgnoreCase(nk))
				{
					alias.set(nk, cs);
					alias.set(ke, null);
				}

				new Alias(cs);
			}
		}

		if (save)
		{
			try
			{
				alias.save(aliasFile);
			}
			catch (IOException e)
			{
				PLUGIN.getLogger().warning("Failed to save alias.yml!");
				PLUGIN.getLogger().warning("Caused by: " + e.getMessage());
			}
		}
	}

	private boolean checkWhiteListList(String key)
	{
		List<String> l = new ArrayList<String>();
		boolean changed = false;
		if (!whiteList.isSet(key))
		{
			l.add("randomCommand");
			changed = true;
		}
		else if (!whiteList.isList(key))
		{
			for (String x : whiteList.getConfigurationSection(key).getKeys(false))
			{
				if (whiteList.isBoolean(key + "." + x))
				{
					if (whiteList.getBoolean(key + "." + x))
						l.add(x.trim().toLowerCase());
				}
				else
					l.add(x.trim().toLowerCase());
			}
			changed = true;
		}
		else
		{
			Iterator<String> it = whiteList.getStringList(key).iterator();
			String val;
			while (it.hasNext())
			{
				val = it.next();
				l.add(val.trim().toLowerCase());
				if (!val.equals(val.trim().toLowerCase()))
					changed = true;
			}
		}
		if (changed)
			whiteList.set(key, l);
		return changed;
	}
}
