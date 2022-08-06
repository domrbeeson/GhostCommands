package gg.gateway.ghostcommands;

import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;

public interface IGhostCommand {
	public String getName();

	public List<String> getAliases();
	
	@Nullable
	public String getPermission();
	
	default void run(CommandSender sender, String...args) {

	}

	default List<String> tabComplete(CommandSender sender, String...args) {
		return null;
	}
}
