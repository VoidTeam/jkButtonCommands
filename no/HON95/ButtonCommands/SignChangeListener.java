package no.HON95.ButtonCommands;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;


class SignChangeListener implements Listener {

	private final BCMain PLUGIN;
	boolean titleUnderlined = false;
	boolean titleCentered = false;

	private final ChatColor GN = ChatColor.GREEN;
	private final ChatColor R = ChatColor.RED;
	private final ChatColor DP = ChatColor.DARK_PURPLE;
	private final ChatColor DB = ChatColor.DARK_BLUE;
	private final ChatColor UL = ChatColor.UNDERLINE;

	SignChangeListener(BCMain instance) {
		PLUGIN = instance;
	}

	@EventHandler
	public void onSignChange(SignChangeEvent ev) {

		if (ev.isCancelled())
			return;

		Block block = ev.getBlock();
		String[] lines = ev.getLines();
		Player player = ev.getPlayer();

		if (lines[1].startsWith("/")) {

			String[] cmd = Misc.concatCmd(lines);

			if (cmd[1].equalsIgnoreCase("/c") || cmd[1].equalsIgnoreCase("/console")
					|| cmd[1].equalsIgnoreCase("/r") || cmd[1].equalsIgnoreCase("/redstone")
					|| cmd[1].equalsIgnoreCase("/a") || cmd[1].equalsIgnoreCase("/alias"))
				player.sendMessage(DP + "Command signs must contain a command!");

			if (cmd[0].startsWith("/redstone ") || cmd[0].startsWith("/r ")) {

				if (player.hasPermission("buttoncommands.create.redstone")) {
					lines[0] = ft(lines[0]);
					PLUGIN.getLogger().info(player.getName()
							+ " created a redstone command sign at"
							+ " X" + block.getX()
							+ " Y" + block.getY()
							+ " Z" + block.getZ());
					player.sendMessage(GN + "Redstone console command sign created!");
				}
				else {
					ev.setCancelled(true);
					player.sendMessage(R + "You are not allowed to create redstone command signs!");
				}
			}
			else if (cmd[0].startsWith("/console ") || cmd[0].startsWith("/c ")) {

				if (player.hasPermission("buttoncommands.create.console")) {
					lines[0] = ft(lines[0]);
					PLUGIN.getLogger().info(player.getName()
							+ " created a console command sign at"
							+ " X" + block.getX()
							+ " Y" + block.getY()
							+ " Z" + block.getZ());
					player.sendMessage(GN + "Console command sign created!");
				}
				else {
					ev.setCancelled(true);
					player.sendMessage(R + "You are not allowed to create console command signs!");
				}
			}
			else if (cmd[0].startsWith("/alias ") || cmd[0].startsWith("/a ")) {

				if (player.hasPermission("buttoncommands.create.alias")) {
					lines[0] = ft(lines[0]);
					PLUGIN.getLogger().info(player.getName()
							+ " created a alias command sign at"
							+ " X" + block.getX()
							+ " Y" + block.getY()
							+ " Z" + block.getZ());
					player.sendMessage(GN + "Alias command sign created!");
				}
				else {
					ev.setCancelled(true);
					player.sendMessage(R + "You are not allowed to create alias command signs!");
				}
			}
			else {

				if (player.hasPermission("buttoncommands.create.normal")) {
					lines[0] = ft(lines[0]);
					PLUGIN.getLogger().info(player.getName()
							+ " created a command sign at"
							+ " X" + block.getX()
							+ " Y" + block.getY()
							+ " Z" + block.getZ());
					player.sendMessage(GN + "Command sign created!");
				}
				else {
					ev.setCancelled(true);
					player.sendMessage(R + "You are not allowed to create command signs!");
				}
			}
		}
	}

	private String ft(String title) {
		return (titleCentered ? "  " + (titleUnderlined ? "  " : "") : "") + DB + (titleUnderlined ? UL + "" : "") + title;
	}
}
