package me.jwhz.campaignreborn.campaign.active.action.actions.handlers;

import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.Action;
import me.jwhz.campaignreborn.campaign.active.action.actions.ActionHandler;
import me.jwhz.campaignreborn.kits.Kit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class SetKitHandler extends ActionHandler {

    public SetKitHandler(Action action, ConfigurationSection section, ActiveCampaign campaign) {

        super(action, section, campaign);

    }

    @Override
    public void loadAction() {

        if (core.kitManager.isKit(section.getString("kit"))) {

            sendMessages("start message");

            Kit kit = core.kitManager.get(section.getString("kit"));

            ItemStack[][] items = kit.getItems();

            for (Player player : campaign.getParty().getPlayers()) {

                player.getInventory().setContents(items[0]);
                player.getInventory().setArmorContents(items[1]);
                player.getInventory().setExtraContents(items[2]);

            }

            sendMessages("complete message");

            isComplete = true;

        }

    }

}
