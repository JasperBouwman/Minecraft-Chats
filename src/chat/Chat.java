package chat;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Chat implements CommandExecutor {

	Main p;

	public Chat(Main instance) {
		p = instance;
	}

	int a = 1;

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("you must be a player to use this");
			return false;
		}
		Player player = (Player) sender;

		if (args.length == 0) {

			if (!p.getConfig().contains("chats")) {
				player.sendMessage("§4you arn't in a chat, use §c/chat join <chatname>");
				return false;
			}

			boolean bool = true;
			for (String s : p.getConfig().getConfigurationSection("chats").getKeys(false)) {
				ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("chats." + s + ".players");
				if (list.contains(player.getName())) {
					player.sendMessage("§3you are in the chat: " + s);
					bool = false;
				}
			}
			if (bool) {
				player.sendMessage("§4you arn't in a chat, use §c/chat join <chatname>");
			}
			return false;
		}

		if (args[0].equalsIgnoreCase("join")) {

			if (args.length != 2) {
				player.sendMessage("§4wrong use, use §c/chat join <chatname>");
				return false;
			}

			if (!p.getConfig().contains("chats." + args[1])) {
				player.sendMessage("§4this chat doesnt exist, create one using §c/chat add <chatname>");
				return false;
			}
			ArrayList<String> list1 = (ArrayList<String>) p.getConfig().getStringList("chats." + args[1] + ".players");
			if (list1.contains(player.getName())) {
				player.sendMessage("§4you already are in this chat");
				return false;
			}

			// blacklist
			for (String s : p.getConfig().getConfigurationSection("chats").getKeys(false)) {
				ArrayList<String> blacklist = (ArrayList<String>) p.getConfig()
						.getStringList("chats." + s + ".blacklist");

				if (blacklist.contains(player.getName())) {
					player.sendMessage("§4you are blacklisted from this chat");
					return false;
				}
			}
			// remove old chat
			for (String s : p.getConfig().getConfigurationSection("chats").getKeys(false)) {
				ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("chats." + s + ".players");
				if (list.contains(player.getName())) {
					list.remove(player.getName());
					p.getConfig().set("chats." + s + ".players", list);
					p.saveConfig();

					for (String ss : list) {
						if (Bukkit.getPlayerExact(ss) == null) {
							a = a + 1;
						} else {
							Player players = Bukkit.getPlayerExact(ss);
							players.sendMessage("§3chat " + s + ": §9" + player.getName() + " §rleft this chat");
						}
					}
				}
			}
			// add new chat
			ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("chats." + args[1] + ".players");

			for (String ss : list) {
				if (Bukkit.getPlayerExact(ss) == null) {
					a = a + 1;
				} else {
					Player players = Bukkit.getPlayerExact(ss);
					players.sendMessage("§3chat " + args[1] + ": §9" + player.getName() + " §rjoined this chat");
				}
			}

			list.add(player.getName());
			p.getConfig().set("chats." + args[1] + ".players", list);
			p.saveConfig();
			player.sendMessage("§3you joined §9" + args[1]);

		} else if (args[0].equalsIgnoreCase("leave")) {

			for (String s : p.getConfig().getConfigurationSection("chats").getKeys(false)) {
				ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("chats." + s + ".players");
				if (list.contains(player.getName())) {
					list.remove(player.getName());
					p.getConfig().set("chats." + s + ".players", list);
					p.saveConfig();
					player.sendMessage("§3you left §9" + s);
					for (String ss : list) {
						if (Bukkit.getPlayerExact(ss) == null) {
							a = a + 1;
						} else {
							Player players = Bukkit.getPlayerExact(ss);
							players.sendMessage("§3chat " + s + ": §9" + player.getName() + " §rleft this chat");
						}
					}
					return false;
				}
			}

			player.sendMessage("§4you arn't in a chat, use §c/chat join <chatname> §4to join a chat");

		} else if (args[0].equalsIgnoreCase("blacklist")) {
			int a = 0;
			for (String s : p.getConfig().getConfigurationSection("chats").getKeys(false)) {
				a++;
				s.trim();
			}

			if (a == 0) {
				player.sendMessage("§4you don't own a chat, to create one use §c/chat add <chatname>");
				return false;
			}

			if (args.length != 3) {
				player.sendMessage("§4wrong use, use §c/chat blacklist <add/remove> <playername>");
				return false;
			}
			boolean add = false;
			boolean remove = false;
			boolean online = false;
			if (args[1].equalsIgnoreCase("add")) {
				for (String s : p.getConfig().getConfigurationSection("chats").getKeys(false)) {
					if (p.getConfig().getString("chats." + s + ".owner").equalsIgnoreCase(player.getName())) {

						OfflinePlayer[] players = Bukkit.getServer().getOfflinePlayers();

						for (OfflinePlayer ss : players) {
							if (ss.getName().equals(args[2])) {

								if (Bukkit.getServer().getPlayerExact(args[2]) == null) {
									player.sendMessage("§4this player must be online");
									return false;
								}

								Player black = Bukkit.getServer().getPlayerExact(args[2]);
								ArrayList<String> list = (ArrayList<String>) p.getConfig()
										.getStringList("chats." + s + ".blacklist");
								if (black.getName().equals(player.getName())) {
									player.sendMessage("§4you can't blacklist yourself");
									return false;
								}
								if (list.contains(black.getName())) {
									player.sendMessage("§4this player is already in you blacklist");
									return false;
								} else {
									list.add(black.getName());
									p.getConfig().set("chats." + s + ".blacklist", list);
									p.saveConfig();
									black.sendMessage("§3you are blacklisted from the chat §9" + s);
									player.sendMessage("§3succesfully added §9" + black.getName());

									ArrayList<String> list1 = (ArrayList<String>) p.getConfig()
											.getStringList("chats." + s + ".players");
									if (list1.contains(black.getName())) {
										list1.remove(black.getName());
										p.getConfig().set("chats." + s + ".players", list1);
										p.saveConfig();
									}

									return false;
								}
							} else {
								online = true;
							}
						}
					} else {
						add = true;
					}
				}

				if (online) {
					player.sendMessage("§4you must use a online playername");
				}

				if (add) {
					player.sendMessage("§4you don't own a chat, to create one use §c/chat add <chatname>");
				}

			} else if (args[1].equalsIgnoreCase("remove")) {

				for (String s : p.getConfig().getConfigurationSection("chats").getKeys(false)) {
					if (p.getConfig().getString("chats." + s + ".owner").equalsIgnoreCase(player.getName())) {

						OfflinePlayer[] players = Bukkit.getServer().getOfflinePlayers();

						for (OfflinePlayer ss : players) {
							if (ss.getName().equals(args[2])) {

								if (Bukkit.getServer().getPlayerExact(args[2]) == null) {
									player.sendMessage("§4you must use a online playername");
									return false;
								}

								Player black = Bukkit.getServer().getPlayerExact(args[2]);
								ArrayList<String> list = (ArrayList<String>) p.getConfig()
										.getStringList("chats." + s + ".blacklist");

								if (black.getName().equals(player.getName())) {
									player.sendMessage("§4you can't remove yourself");
									return false;
								}
								if (!list.contains(black.getName())) {
									player.sendMessage("§4this player isn't in you blacklist");
									return false;
								} else {
									list.remove(black.getName());
									p.getConfig().set("chats." + s + ".blacklist", list);
									p.saveConfig();
									black.sendMessage("§3you aren't anymore blacklisted from the chat §9" + s);
									player.sendMessage("§3succesfully removed §9" + black.getName());
									return false;
								}
							} else {
								online = true;
							}
						}
					} else {
						remove = true;
					}
				}

				if (online) {
					player.sendMessage("§4you must use a online playername");
				}

				if (remove) {
					player.sendMessage("§4you don't own a chat, to create one use §c/chat add <chatname>");
				}

			}

		}

		else if (args[0].equalsIgnoreCase("add")) {

			if (args.length != 2) {
				player.sendMessage("§4wrong use, use §c/chat add <chatname>");
				return false;
			}

			if (p.getConfig().contains("chats." + args[1])) {
				player.sendMessage("§4this chatname already exist, please use another name");
				return false;
			}
			if (p.getConfig().contains("chats")) {
				for (String s : p.getConfig().getConfigurationSection("chats").getKeys(false)) {
					if (p.getConfig().getString("chats." + s + ".owner").equalsIgnoreCase(player.getName())) {
						player.sendMessage(
								"§4you only can own 1 chat, remove §c" + s + " §4before you create a new one");
						return false;
					}
				}
			}
			if (p.getConfig().contains("chats")) {
				for (String s : p.getConfig().getConfigurationSection("chats").getKeys(false)) {
					ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("chats." + s + ".players");
					if (list.contains(player.getName())) {
						list.remove(player.getName());
						p.getConfig().set("chats." + s + ".players", list);
						p.saveConfig();

						for (String ss : list) {
							if (Bukkit.getPlayerExact(ss) == null) {
								a = a + 1;
							} else {
								Player players = Bukkit.getPlayerExact(ss);
								players.sendMessage("§3chat " + s + ": §9" + player.getName() + " §rleft this chat");
							}
						}
					}
				}
			}
			p.getConfig().set("chats." + args[1] + ".owner", player.getName());
			ArrayList<String> list1 = new ArrayList<String>();
			ArrayList<String> list = new ArrayList<String>();
			list.add(player.getName());
			p.getConfig().set("chats." + args[1] + ".blacklist", list1);
			p.getConfig().set("chats." + args[1] + ".players", list);
			p.saveConfig();
			player.sendMessage("§3succesfully added the chat §9" + args[1]);

		} else if (args[0].equalsIgnoreCase("remove")) {

			if (args.length == 1) {

				for (String s : p.getConfig().getConfigurationSection("chats").getKeys(false)) {
					if (p.getConfig().getString("chats." + s + ".owner").equalsIgnoreCase(player.getName())) {

						ArrayList<String> list = (ArrayList<String>) p.getConfig()
								.getStringList("chats." + s + ".players");
						for (String ss : list) {
							if (Bukkit.getPlayerExact(ss) == null) {
								a = a + 1;
							} else if (Bukkit.getPlayerExact(ss).getName().equals(player.getName())) {
								a = a + 1;
							} else {
								Player players = Bukkit.getPlayerExact(ss);
								players.sendMessage("§3chat " + s + ": §cthis chat is removed by the owner");
							}
						}

						p.getConfig().set("chats." + s, null);
						p.saveConfig();
						player.sendMessage("§3succesfully removed the chat §9" + s);
						return false;
					}
				}

				player.sendMessage("§4you don't own a chat, to create one use §c/chat add <chatname>");

			} else if (args.length == 2) {

				if (player.isOp()) {
					if (p.getConfig().contains("chats." + args[1])) {
						if (!p.getConfig().getString("chats." + args[1] + ".owner").equals(player.getName())) {
							player.sendMessage("§3succesfully removed from an OP position");
							if (Bukkit.getServer()
									.getPlayerExact(p.getConfig().getString("chats." + args[1] + ".owner")) != null) {
								Player owner = Bukkit.getServer()
										.getPlayerExact(p.getConfig().getString("chats." + args[1] + ".owner"));
								owner.sendMessage("§3your chat was removed by an OP");
							}
							ArrayList<String> list = (ArrayList<String>) p.getConfig()
									.getStringList("chats." + args[1] + ".players");

							for (String ss : list) {
								if (Bukkit.getPlayerExact(ss) == null) {
									a = a + 1;
								} else if (Bukkit.getPlayerExact(ss).getName().equals(player.getName())) {
									a = a + 1;
								} else {
									Player players = Bukkit.getPlayerExact(ss);
									players.sendMessage("§3chat " + args[1] + ": §cthis chat is removed by an OP");
								}
							}
							p.getConfig().set("chats." + args[1], null);
							p.saveConfig();
							return false;
						} else {
							player.sendMessage(
									"§4wrong use, can't remove your own chat from an OP position, use §c/chat remove");
						}
					} else {
						player.sendMessage("§4this chat doesn't exist");
					}
				} else {
					player.sendMessage("§4wrong use, use §c/chat remove");
				}
			} else {
				player.sendMessage("§4wrong use, use §c/chat remove");
			}

		} else if (args[0].equalsIgnoreCase("list")) {

			if (args.length == 1) {

				if (!p.getConfig().contains("chats")) {
					player.sendMessage("§4there are not chats yet, use §c/chat add <chatname> §4to create one");
					return false;
				}

				player.sendMessage("§3these are the chats: ");
				int i = 0;
				for (String keys : p.getConfig().getConfigurationSection("chats").getKeys(false)) {
					if (i == 1) {
						player.sendMessage("§3" + keys);
						i = 0;
					} else {
						player.sendMessage("§9" + keys);
						i = 1;
					}

				}
			} else if (args.length == 2) {
				if (p.getConfig().contains("chats." + args[1])) {
					player.sendMessage("§3players in chat §9" + args[1] + ":");
					int i = 0;
					for (String keys : p.getConfig().getStringList("chats." + args[1] + ".players")) {
						if (i == 1) {
							player.sendMessage("§3" + keys);
							i = 0;
						} else {
							player.sendMessage("§9" + keys);
							i = 1;
						}
					}
				} else {
					player.sendMessage("§4this chat doesn't exist");
				}
			}
		} else if (args[0].equalsIgnoreCase("help")) {
			player.sendMessage("§3welcome at the chat help centrum");
			player.sendMessage("");
			player.sendMessage(
					"§3use §9/chat list §3to see all the available chats or §9/chat list <chatname> §3to see all the players in that chat.");
			player.sendMessage(
					"§3use §9/chat add <chatname> §3to create a new chat. to join a chat use §9/chat join <chatname> §3to leave a chat use §9/chat leave <chatname>");
			player.sendMessage(
					"§3use §9/chat blacklist <add/remove> <playername> §3to add/remove a player from your blacklist");
			player.sendMessage("§3use §9/c <message> §3to talk in the chat group");
			return false;
		} else {
			player.sendMessage("§4wrong use, use §c/chat help §4 for more info");
		}

		return false;
	}

}
