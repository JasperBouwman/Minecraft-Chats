package chat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class TabComplete implements TabCompleter {

	Main p;

	public TabComplete(Main instance) {
		p = instance;
	}

	@Override
	public java.util.List<String> onTabComplete(CommandSender sender, Command cmd, String CommandLabel, String[] args) {

		if (cmd.getName().equalsIgnoreCase("chat")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (args.length == 1) {
					List<String> list = new ArrayList<>();

					list.add("add");
					list.add("remove");
					list.add("blacklist");
					list.add("list");
					list.add("join");
					list.add("help");
					return list;

				} else if (args.length == 2) {
					if (args[0].equalsIgnoreCase("blacklist")) {

						List<String> list = new ArrayList<>();

						list.add("add");
						list.add("remove");

						return list;
					} else if (args[0].equalsIgnoreCase("join")) {
						List<String> list = new ArrayList<>();

						for (String s : p.getConfig().getConfigurationSection("chats").getKeys(false)) {
							list.add(s);
						}
						return list;
					} else if (args[0].equalsIgnoreCase("remove")) {
						if (player.isOp()) {
							List<String> list = new ArrayList<>();

							for (String s : p.getConfig().getConfigurationSection("chats").getKeys(false)) {
								list.add(s);
							}
							return list;
						} else {
							List<String> list = new ArrayList<>();
							return list;
						}
					} else if (args[0].equalsIgnoreCase("list")) {
						List<String> list = new ArrayList<>();

						for (String s : p.getConfig().getConfigurationSection("chats").getKeys(false)) {
							list.add(s);
						}
						return list;
					} else {
						List<String> list = new ArrayList<>();
						return list;
					}
				} else if (args.length == 3) {

					if (args[0].equalsIgnoreCase("blacklist") && args[1].equalsIgnoreCase("remove")) {

						for (String s : p.getConfig().getConfigurationSection("chats").getKeys(false)) {
							if (p.getConfig().getString("chats." + s + ".owner").equalsIgnoreCase(player.getName())) {
								List<String> list = p.getConfig().getStringList("chats." + s + ".blacklist");
								return list;
							} else {
								return null;
							}
						}

					} else if (args[0].equalsIgnoreCase("blacklist") && args[1].equalsIgnoreCase("add")) {
						return null;
					} else {
						List<String> list = new ArrayList<String>();
						return list;
					}

				} else {
					List<String> list = new ArrayList<String>();
					return list;
				}
			}
		}

		return null;
	}

}
