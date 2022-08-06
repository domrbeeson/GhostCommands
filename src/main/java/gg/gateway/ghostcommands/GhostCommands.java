package gg.gateway.ghostcommands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GhostCommands extends JavaPlugin implements Listener {

	private static Map<String, IGhostCommand> commands = new HashMap<>();
	private static Map<String, String> aliases = new HashMap<>();

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void tabComplete(TabCompleteEvent event) {
		String buffer = event.getBuffer();
		buffer = buffer.charAt(0) == '/' ? buffer.substring(1) : buffer;
		String[] parts = buffer.split(" ");

		String label = parts[0].toLowerCase();
		IGhostCommand command = commands.get(label);
		if (command == null) {
			String labelFromAlias = aliases.get(label);
			if (labelFromAlias == null) return;

			command = commands.get(labelFromAlias);
		}

		String[] args = new String[parts.length - 1];
		for (int i = 1; i < parts.length; i++) {
			args[i - 1] = parts[i];
		}


		CommandSender sender = event.getSender();
		if (!sender.hasPermission(command.getPermission())) return;

		event.setCompletions(command.tabComplete(sender, args));
	}

	public static void register(IGhostCommand command) {
		commands.put(command.getName().toLowerCase(), command);
		command.getAliases().forEach(alias -> aliases.put(alias.toLowerCase(), command.getName()));

		// Register command with bukkit too so it exists in tab complete
		Method getCommandMap;
		try {
			getCommandMap = Bukkit.getServer().getClass().getMethod("getCommandMap", null);
			Object cmdMap = getCommandMap.invoke(Bukkit.getServer(), null);
			Method register = cmdMap.getClass().getMethod("register", String.class, Command.class);
			register.invoke(cmdMap, command.getName(), command);
		} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
