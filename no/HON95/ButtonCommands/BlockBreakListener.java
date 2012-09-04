package no.HON95.ButtonCommands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

final class BlockBreakListener implements Listener
{

	private final BCMain PLUGIN;
	boolean protectSign = false;
	boolean protectBlock = false;

	private final ChatColor GN = ChatColor.GREEN;
	private final ChatColor R = ChatColor.RED;

	BlockBreakListener(BCMain instance)
	{
		PLUGIN = instance;
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent ev)
	{

		if (ev.isCancelled())
			return;

		Block block = ev.getBlock();
		Player player = ev.getPlayer();

		if (block.getType() == Material.WALL_SIGN)
		{

			if (!protectSign)
				return;

			Sign sign = (Sign) block.getState();
			String[] lines = sign.getLines();
			if (lines[1].startsWith("/"))
				ev.setCancelled(signChecker(player, block, sign));

		}
		else
		{

			if (!protectBlock)
				return;

			Block relative[] = { block.getRelative(BlockFace.NORTH), block.getRelative(BlockFace.EAST), block.getRelative(BlockFace.SOUTH), block.getRelative(BlockFace.WEST) };

			Sign sign;
			boolean found = false;

			if (relative[0].getType() == Material.WALL_SIGN)
			{
				sign = (Sign) relative[0].getState();
				if (sign.getLine(1).startsWith("/"))
				{
					if (((org.bukkit.material.Sign) sign.getData()).getFacing().equals(BlockFace.NORTH))
					{
						found = true;
						ev.setCancelled(signChecker(player, relative[0], sign));
					}
				}
			}
			if (relative[1].getType() == Material.WALL_SIGN && !found)
			{
				sign = (Sign) relative[1].getState();
				if (sign.getLine(1).startsWith("/"))
				{
					if (((org.bukkit.material.Sign) sign.getData()).getFacing().equals(BlockFace.EAST))
					{
						found = true;
						ev.setCancelled(signChecker(player, relative[1], sign));
					}
				}
			}
			if (relative[2].getType() == Material.WALL_SIGN && !found)
			{
				sign = (Sign) relative[2].getState();
				if (sign.getLine(1).startsWith("/"))
				{
					if (((org.bukkit.material.Sign) sign.getData()).getFacing().equals(BlockFace.SOUTH))
					{
						found = true;
						ev.setCancelled(signChecker(player, relative[2], sign));
					}
				}
			}
			if (relative[3].getType() == Material.WALL_SIGN && !found)
			{
				sign = (Sign) relative[3].getState();
				if (sign.getLine(1).startsWith("/"))
				{
					if (((org.bukkit.material.Sign) sign.getData()).getFacing().equals(BlockFace.WEST))
					{
						found = true;
						ev.setCancelled(signChecker(player, relative[3], sign));
					}
				}
			}
		}
	}

	boolean signChecker(Player player, Block block, Sign sign)
	{

		String cmd = Misc.concatCmd(sign.getLines())[0];

		if (cmd.startsWith("/redstone") || cmd.startsWith("/r"))
		{
			if (player.hasPermission("buttoncommands.break.redstone"))
			{
				PLUGIN.getLogger().info(player.getName() + " broke a redstone console command sign at" + " X" + block.getX() + " Y" + block.getY() + " Z" + block.getZ());
				player.sendMessage(GN + "Redstone console command sign broken!");
				return false;
			}
			else
			{
				player.sendMessage(R + "You are not allowed to break redstone console command signs!");
				return true;
			}
		}

		else if (cmd.startsWith("/console") || cmd.startsWith("/c"))
		{
			if (player.hasPermission("buttoncommands.break.console"))
			{
				PLUGIN.getLogger().info(player.getName() + " broke a console command sign at" + " X" + block.getX() + " Y" + block.getY() + " Z" + block.getZ());
				player.sendMessage(GN + "Console command sign broken!");
				return false;
			}
			else
			{
				player.sendMessage(R + "You are not allowed to break console command signs!");
				return true;
			}
		}

		else if (cmd.startsWith("/alias") || cmd.startsWith("/a"))
		{
			if (player.hasPermission("buttoncommands.break.alias"))
			{
				PLUGIN.getLogger().info(player.getName() + " broke a alias command sign at" + " X" + block.getX() + " Y" + block.getY() + " Z" + block.getZ());
				player.sendMessage(GN + "Alias command sign broken!");
				return false;
			}
			else
			{
				player.sendMessage(R + "You are not allowed to break alias command signs!");
				return true;
			}
		}

		else
		{
			if (player.hasPermission("buttoncommands.break.normal"))
			{
				PLUGIN.getLogger().info(player.getName() + " broke a command sign at" + " X" + block.getX() + " Y" + block.getY() + " Z" + block.getZ());
				player.sendMessage(GN + "Command sign broken!");
				return false;
			}
			else
			{
				player.sendMessage(R + "You are not allowed to break command signs!");
				return true;
			}
		}
	}
}
