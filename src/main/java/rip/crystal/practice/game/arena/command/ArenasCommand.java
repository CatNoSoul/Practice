package rip.crystal.practice.game.arena.command;

import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.game.arena.ArenaType;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.chat.ChatComponentBuilder;
import rip.crystal.practice.utilities.chat.ChatHelper;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class ArenasCommand extends BaseCommand {

	@Command(name = "arenas", permission = "cpractice.arena.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		player.sendMessage(CC.RED + "Arenas:");

		if (Arena.getArenas().isEmpty()) {
			player.sendMessage(CC.GRAY + "There are no arenas.");
			return;
		}

		for (Arena arena : Arena.getArenas()) {
			if (arena.getType() != ArenaType.DUPLICATE) {
				ChatComponentBuilder builder = new ChatComponentBuilder("").parse("&7- " + (arena.isSetup() ? "&8[&a&lEnabled&8] &4&l" : "&8[&4&lDisabled&8] &4&l") + arena.getName() + " &7(" + arena.getType().name() + ")");

				ChatComponentBuilder status = new ChatComponentBuilder("").parse("&7[&cSTATUS&7]");
				status.attachToEachPart(ChatHelper.hover("&7Click to view this arena's status."));
				status.attachToEachPart(ChatHelper.click("/arena status " + arena.getName()));

				builder.append(" ");

				for (BaseComponent component : status.create()) {
					builder.append((TextComponent) component);
				}

				player.spigot().sendMessage(builder.create());
			}
		}
	}

}
