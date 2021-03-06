package chat;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class C implements CommandExecutor {
	private Main p;

	C(Main instance) {
		p = instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		boolean bool = true;

		if (!(sender instanceof Player)) {
			sender.sendMessage("you must be a player to use this");
			return false;
		}
		Player player = (Player) sender;

		for (String s : p.getConfig().getConfigurationSection("chats").getKeys(false)) {
			ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("chats." + s + ".players");
			if (list.contains(player.getName())) {
				StringBuilder string = new StringBuilder();
				for (int i = 0; i <= args.length - 1; i++) {
					string.append(args[i]).append(" ");
				}
				int a = 1;
				for (String ss : list) {
					if (Bukkit.getPlayerExact(ss) == null) {
						a = a + 1;
					} else {
						Player players = Bukkit.getPlayerExact(ss);
						players.sendMessage("§3chat " + s + ": §r<" + player.getName() + "> " + string.toString());
					}
				}
				bool = false;
			}
		}
		if (bool) {
			player.sendMessage("§4you aren't in a chat, use §c/chat join <chatname>");
		}

		return false;
	}
}