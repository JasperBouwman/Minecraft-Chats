package chat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class TabComplete implements TabCompleter {

	private Main p;

	TabComplete(Main instance) {
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

						list.addAll(p.getConfig().getConfigurationSection("chats").getKeys(false));
						return list;
					} else if (args[0].equalsIgnoreCase("remove")) {
						if (player.isOp()) {
							List<String> list = new ArrayList<>();

							list.addAll(p.getConfig().getConfigurationSection("chats").getKeys(false));
							return list;
						} else {
							return new ArrayList<>();
						}
					} else if (args[0].equalsIgnoreCase("list")) {
						List<String> list = new ArrayList<>();

						list.addAll(p.getConfig().getConfigurationSection("chats").getKeys(false));
						return list;
					} else {
						return new ArrayList<>();
					}
				} else if (args.length == 3) {

					if (args[0].equalsIgnoreCase("blacklist") && args[1].equalsIgnoreCase("remove")) {

						for (String s : p.getConfig().getConfigurationSection("chats").getKeys(false)) {
							if (p.getConfig().getString("chats." + s + ".owner").equalsIgnoreCase(player.getName())) {
								return p.getConfig().getStringList("chats." + s + ".blacklist");
							}
						}
						return new ArrayList<>();
					} else if (args[0].equalsIgnoreCase("blacklist") && args[1].equalsIgnoreCase("add")) {
						return null;
					} else {
						return new ArrayList<>();
					}
				} else {
					return new ArrayList<>();
				}
			}
		}

		return null;
	}

}
