package rip.crystal.practice.player.queue.menus;

import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.queue.Queue;
import rip.crystal.practice.player.queue.menus.buttons.FFAButton;
import rip.crystal.practice.player.queue.menus.buttons.RankedButton;
import rip.crystal.practice.player.queue.menus.buttons.UnrankedButton;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.file.type.BasicConfigurationFile;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.utilities.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.visual.leaderboard.menu.button.StatsButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class QueueSelectKitMenu extends Menu {

	private final boolean ranked;

	@Override
	public String getTitle(Player player) {
		if (ranked) return cPractice.get().getMainConfig().getString("QUEUE.RANKED.INVENTORY_TITLE");
		else return cPractice.get().getMainConfig().getString("QUEUE.UNRANKED.INVENTORY_TITLE");
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		HashMap<Integer, Button> buttons = new HashMap<>();

		ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(cPractice.get().getMainConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(cPractice.get().getMainConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();
		this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);

		buttons.put(39, new FFAButton());

		for (Queue queue : Queue.getQueues()) {
			if(queue.isRanked() == ranked) {
				buttons.put(41, new UnrankedButton());
			} else {
				buttons.put(41, new RankedButton());
			}

			if (queue.isRanked() == ranked) buttons.put(queue.getKit().getSlot(), new SelectKitButton(queue));
		}
		return buttons;
	}

	@Override
	public int getSize() {
		return cPractice.get().getMainConfig().getInteger("QUEUES.SIZE") * 9;
	}

	@AllArgsConstructor
	private static class SelectKitButton extends Button {

		private final Queue queue;

		@Override
		public ItemStack getButtonItem(Player player) {
			List<String> lore = new ArrayList<>();

			Profile profile = Profile.get(player.getUniqueId());
			int pos = 0;

			BasicConfigurationFile config = cPractice.get().getMainConfig();
			config.getStringList("QUEUE." + (queue.isRanked() ? "RANKED" : "UNRANKED") + ".LORE").forEach(s ->
					lore.add(s.replace("{bars}", CC.SB_BAR)
							.replace("{in-fight}", String.valueOf(Match.getInFightsCount(queue)))
							.replace("{winstreak}", String.valueOf(profile.getKitData().get(queue.getKit()).getKillstreak()))
							.replace("{elo}", String.valueOf(profile.getKitData().get(queue.getKit()).getElo()))
							.replace("{in-queue}", String.valueOf(queue.getPlayers().size()))));

			ChatColor color = ChatColor.valueOf(config.getString("QUEUE." + (queue.isRanked() ? "RANKED" : "UNRANKED") + ".NAME_COLOR"));
			boolean amount = config.getBoolean("QUEUE.AMOUNT_PER_FIGHTS");

			return new ItemBuilder(queue.getKit().getDisplayIcon())
					.addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
					.addItemFlag(ItemFlag.HIDE_ENCHANTS)
					.addItemFlag(ItemFlag.HIDE_POTION_EFFECTS)
					.name(CC.translate(color + queue.getKit().getName()))
					.amount(amount ? Match.getInFightsCount(queue) + 1 : 1)
					.lore(lore)
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			Profile profile = Profile.get(player.getUniqueId());

			if (profile.isBusy()) {
				player.sendMessage(CC.RED + "You cannot queue right now.");
				return;
			}

			player.closeInventory();
			queue.addPlayer(player, queue.isRanked() ? profile.getKitData().get(queue.getKit()).getElo() : 0);
		}
	}

	@AllArgsConstructor
	private static class Painting extends Button {
		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.STAINED_GLASS)
					.name(CC.translate(""))
					.build();
		}
	}
}
