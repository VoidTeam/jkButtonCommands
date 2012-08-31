package no.HON95.ButtonCommands;

import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;


public class Alias {

	static final HashMap<String, Alias> BCA = new HashMap<String, Alias>();

	public final String NAME;
	public final boolean ENAB;
	public final String[] CMDS;
	public final boolean C_MP;
	public final boolean C_HP;
	public final String PERM;
	public final boolean IGPM;
	public final boolean IGWL;
	public final int COOL;
	public final HashMap<String, Long> LOG;

	Alias(ConfigurationSection cs) {

		NAME = cs.getName();
		ENAB = cs.getBoolean("enable");
		List<String> ls = cs.getStringList("commands");
		CMDS = ls.toArray(new String[ls.size()]);
		C_MP = cs.getBoolean("current.missingPerm");
		C_HP = cs.getBoolean("current.hasPerm");
		PERM = cs.getString("permission");
		IGPM = cs.getBoolean("ignoreOtherPerms");
		IGWL = cs.getBoolean("ignoreWhiteList");
		COOL = cs.getInt("cooldown");
		LOG = new HashMap<String, Long>();

		BCA.put(NAME.toLowerCase(), this);
	}
}
