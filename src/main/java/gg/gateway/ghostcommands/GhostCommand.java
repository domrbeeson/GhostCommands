package gg.gateway.ghostcommands;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class GhostCommand extends Command implements IGhostCommand {
	@Nullable
	private final String permission;

	public GhostCommand(Builder builder) {
		super(builder.name, "", "", builder.aliases);
		this.permission = builder.permission;
	}

	@Nullable
	public String getPermission() {
		return permission;
	}

	public static class Builder {
		private String name;
		@Nullable
		private String permission;
		private List<String> aliases = new ArrayList<>();

		public Builder(String name) {
			this.name = name;
		}

		public Builder withPermission(String permission) {
			this.permission = permission;
			return this;
		}

		public Builder addAlias(String alias) {
			aliases.add(alias);
			return this;
		}
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!label.equalsIgnoreCase(getName()) && !getAliases().contains(label)) return false;
		if (!sender.hasPermission(getPermission())) return false;

		run(sender, args);

		return true;
	}
}
