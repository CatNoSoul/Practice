package rip.crystal.practice.player.party.menu.manage.buttons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.player.party.menu.manage.impl.PartyKickMenu;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.shop.impl.killeffects.KillEffectsShopMenu;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.menu.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/24/2023
 */
public class PartyKickButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        Profile profile = Profile.get(player.getUniqueId());

        List<String> lore = new ArrayList<>();
        lore.add("&8&m--------------------------------------");
        lore.add("&7Kick someone from your party");
        lore.add("");
        lore.add("&7&oClick here to select user!");
        lore.add("&8&m--------------------------------------");


        return new ItemBuilder(Material.REDSTONE)
                .name("&4&lKick")
                .lore(lore)
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        playNeutral(player);
        new PartyKickMenu().openMenu(player);
    }
}