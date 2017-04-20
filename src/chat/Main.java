package chat;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public void onEnable() {
		getCommand("chat").setExecutor(new Chat(this));
		getCommand("c").setExecutor(new C(this));

		getCommand("chat").setTabCompleter(new TabComplete(this));
	}
}
