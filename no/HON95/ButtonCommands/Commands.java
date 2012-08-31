package no.HON95.ButtonCommands;

import java.util.Iterator;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


class Commands implements CommandExecutor {

	private final BCMain PLUGIN;

	private final ChatColor UL = ChatColor.UNDERLINE;
	private final ChatColor WI = ChatColor.WHITE;
	private final ChatColor GY = ChatColor.GRAY;
	private final ChatColor GO = ChatColor.GOLD;
	private final ChatColor AQ = ChatColor.AQUA;
	private final ChatColor BL = ChatColor.BLUE;
	private final ChatColor GN = ChatColor.GREEN;
	private final ChatColor RE = ChatColor.RED;

	Commands(BCMain instance) {
		PLUGIN = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {

		if (cmd.getName().equalsIgnoreCase("buttoncommands"))
			this.cmdBC(sender, args);
		else if (sender instanceof Player) {

			Player player = (Player) sender;

			// Update to switch (J7)
			if (cmd.getName().equalsIgnoreCase("get"))
				this.cmdGet(player, args);
			else if (cmd.getName().equalsIgnoreCase("creative"))
				this.cmdCreative(player, args);
			else if (cmd.getName().equalsIgnoreCase("survival"))
				this.cmdSurvival(player, args);
			else if (cmd.getName().equalsIgnoreCase("invclear"))
				this.cmdInvclear(player, args);
			else if (cmd.getName().equalsIgnoreCase("iteminfo"))
				this.cmdIteminfo(player, args);
			else if (cmd.getName().equalsIgnoreCase("chat"))
				this.cmdChat(player, args);
		}
		else if (cmd.getName().equalsIgnoreCase("buttoncommands"))
			this.cmdBC(sender, args);
		else {
			sender.sendMessage("A player is expected.");
			return false;
		}
		return true;

	}

	/** Command: get */
	@SuppressWarnings("deprecation")
	private void cmdGet(Player player, String[] args) {
		if ((player.hasPermission("buttoncommands.get"))) {
			if (args.length > 0) {

				int amount;
				short damage;
				Material material;
				ItemStack items;
				String[] split = args[0].split(":");
				byte data;
				String theStuff;

				if (args.length >= 2) {
					try {
						amount = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						player.sendMessage(RE + "Invalid amount: " + args[1]);
						return;
					}
				}
				else {
					amount = 1;
				}

				if (args.length >= 3) {
					try {
						damage = Short.parseShort(args[2]);
					} catch (NumberFormatException e) {
						player.sendMessage(RE + "Invalid damage: " + args[1]);
						return;
					}
				}
				else {
					damage = 0;
				}

				if (args[0].contains(":")) {

					if (split.length != 2) {
						player.sendMessage(RE + "Invalid format: " + args[0]);
						return;
					}

					try {
						material = Material.getMaterial(Integer.parseInt(split[0]));
					} catch (NumberFormatException e) {
						material = Material.getMaterial(split[0].toUpperCase());
					}
					if (material == null) {
						player.sendMessage(RE + "Invalid material: " + split[0]);
						return;
					}

					try {
						data = Byte.parseByte(split[1]);
						items = new ItemStack(material, amount, damage, data);
					} catch (NumberFormatException e) {
						player.sendMessage(RE + "Invalid material data: " + split[1]);
						return;
					}

					theStuff = material.name() + ":" + data;
				}
				else {

					try {
						material = Material.getMaterial(Integer.parseInt(args[0]));
					} catch (NumberFormatException e) {
						material = Material.getMaterial(args[0].toUpperCase());
					}

					if (material == null) {
						player.sendMessage(RE + "Invalid material: " + split[0]);
						return;
					}

					items = new ItemStack(material, amount, damage);
					theStuff = material.name();
				}

				if (args.length >= 4) {

					String[] enchArg;

					for (int c = 0; c < args.length - 3; c++) {

						enchArg = args[c + 3].split(":");

						if (enchArg.length != 2) {
							player.sendMessage(RE + "Invalid enchantment format: " + args[c + 3]);
							continue;
						}

						Enchantment ench;
						int id;

						try {
							ench = Enchantment.getById(Integer.parseInt(enchArg[0]));
						} catch (NumberFormatException e) {
							ench = Enchantment.getByName(enchArg[0].toUpperCase());
						}
						if (ench == null) {
							player.sendMessage(RE + "Invalid enchantment: " + enchArg[0]);
							continue;
						}
						try {
							id = Integer.parseInt(enchArg[1]);
						} catch (NumberFormatException e) {
							player.sendMessage(RE + "Invalid enchantment level: " + enchArg[1]);
							continue;
						}

						items.addUnsafeEnchantment(ench, id);
					}
				}

				player.getInventory().addItem(items);

				try {
					player.updateInventory();
				} catch (Throwable tr) {
				}

				if (args.length >= 4)
					theStuff = "enchanted " + theStuff;
				if (amount < 0)
					theStuff = amount + "(infinate) " + theStuff;
				else
					theStuff = amount + " " + theStuff;

				PLUGIN.getLogger().info("Added " + theStuff + " to " + player.getName() + "'s inventory.");
				player.sendMessage(BL + theStuff + WI + " added to your inventory.");
			}
			else {
				player.sendMessage(GO + "Syntax:" + WI + "/get <material[:data]> [amount] [damage] [enchantment:lvl]");
				player.sendMessage(GO + "Tip: You can have as many enchantments as you want by adding multiple \"" + WI + "[enchantment:lvl]" + GO + "\" arguments.");
			}
		}
		else {
			player.sendMessage(RE + "You are not allowed to use the get command!");
		}
	}

	/** Command: creative */
	private void cmdCreative(Player player, String[] args) {
		if (player.hasPermission("buttoncommands.gamemode")) {

			if (!player.getGameMode().equals(GameMode.CREATIVE)) {
				player.setGameMode(GameMode.CREATIVE);
				PLUGIN.getLogger().info("Changing player " + player.getName() + "'s gamemode to " + "creative");
				player.sendMessage("Changed gamemode to " + GO + "creative");
			}
			else {
				player.sendMessage("Your gamemode is already " + GO + "creative");
			}
		}
		else {
			player.sendMessage(RE + "You are not allowed to change your gamemode!");
		}
	}

	/** Command: survival */
	private void cmdSurvival(Player player, String[] args) {
		if (player.hasPermission("buttoncommands.gamemode")) {

			if (!player.getGameMode().equals(GameMode.SURVIVAL)) {
				player.setGameMode(GameMode.SURVIVAL);
				PLUGIN.getLogger().info("Changing " + player.getName() + "'s gamemode to " + "survival");
				player.sendMessage("Changed gamemode to " + GO + "survival");
			}
			else {
				player.sendMessage("Your gamemode is already " + GO + "survival");
			}
		}
		else {
			player.sendMessage(RE + "You are not allowed to change your gamemode!");
		}
	}

	/** Command: invclear */
	@SuppressWarnings("deprecation")
	private void cmdInvclear(Player player, String[] args) {
		if (player.hasPermission("buttoncommands.invclear")) {
			player.getInventory().clear();
			try {
				player.updateInventory();
			} catch (Exception ex) {
			}
			PLUGIN.getLogger().info("Cleared " + player.getName() + "'s inventory");
			player.sendMessage(BL + "Inventory cleared!");
		}
		else {
			player.sendMessage(RE + "You are not allowed to use the invclear command!");
		}
	}

	/** Command: itemInfo */
	private void cmdIteminfo(Player player, String[] args) {
		if (player.hasPermission("buttoncommands.iteminfo")) {

			ItemStack item = player.getItemInHand();
			Set<Enchantment> enchantments = item.getEnchantments().keySet();

			player.sendMessage("");
			player.sendMessage(BL + "########" + GN + "  Item Info  " + BL + "########");
			player.sendMessage(BL + "#");
			player.sendMessage(BL + "#" + GY + " Item:         " + WI + item.getType().name() + " (" + item.getType().getId() + ")");
			player.sendMessage(BL + "#" + GY + " Data:        " + WI + item.getData().getData());
			player.sendMessage(BL + "#" + GY + " Amount:     " + WI + item.getAmount());
			player.sendMessage(BL + "#" + GY + " Durability:  " + WI + item.getDurability());
			player.sendMessage(BL + "#" + GY + " Enchanted: " + WI + (enchantments.size() > 0 ? "Yes" : "No"));

			if (item.getEnchantments().size() > 0) {
				player.sendMessage(BL + "#");
				player.sendMessage(BL + "# ##" + AQ + "  Enchantments:  " + BL + "##");
				player.sendMessage(BL + "#");

				Iterator<Enchantment> interator = enchantments.iterator();
				Enchantment ench;

				while (interator.hasNext()) {
					ench = interator.next();
					player.sendMessage(BL + "# " + WI + ench.getName()
							+ GN + " LVL " + WI + item.getEnchantmentLevel(ench));
				}
			}
			player.sendMessage(BL + "#");
		}
		else {
			player.sendMessage(RE + "You are not allowed to use the iteminfo command!");
		}
	}

	/** Command: chat */
	private void cmdChat(Player player, String[] args) {
		if (player.hasPermission("buttoncommands.chat")) {
			if (args.length > 0) {
				String chatMsg = "";
				for (int x = 0; x < args.length; x++) {
					chatMsg = chatMsg + args[x] + (x + 1 < args.length ? " " : "");
				}
				player.chat(Misc.insertAll(chatMsg, player));
			}
			else {
				player.sendMessage(GO + "Syntax: " + WI + "/chat <message>");
			}

		}
		else {
			player.sendMessage(RE + "You are not allowed to use the chat command!");
		}
	}

	/** Command: buttoncommands */
	private void cmdBC(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			if (!sender.hasPermission("buttoncommands.buttoncommands")) {
				sender.sendMessage(RE + "You are not allowed to use the buttoncommands command!");
				return;
			}
		}

		if (args.length == 0) {
			sender.sendMessage("");
			sender.sendMessage(GN + "    >> ButtonCommands <<    ");
			sender.sendMessage(GN + " ========================================");
			sender.sendMessage(GN + " * " + GO + "Author: HON95");
			sender.sendMessage(GN + " * " + GO + "Webpage: " + UL + BL + "tinyurl.com/buttoncommands");
			sender.sendMessage(GN + " * " + GO + "Report a bug: " + UL + BL + "tinyurl.com/buttoncommands-ticket");
			sender.sendMessage(GN + " * ");
			sender.sendMessage(GN + " * " + GO + "To reload this plugin, use /bc reload");
			sender.sendMessage("");
			return;
		}
		else if (args.length > 0) {
			if (args[0].equalsIgnoreCase("reload")) {
				if (sender.hasPermission("buttoncommands.buttoncommands")) {
					PLUGIN.configClass.load();
					Misc.checkVersion(PLUGIN, BCMain.UC_URL);
					sender.sendMessage(GN + "ButtonCommands successfully reloaded!");
				}
				else {
					sender.sendMessage(RE + "You are not allowed to reload ButtonCommands!");
				}
				return;
			}
		}
	}
}
