package me.jwhz.campaignreborn.campaign.active.action.actions.handlers;

import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.Action;
import me.jwhz.campaignreborn.campaign.active.action.actions.ActionHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class RewardGroupHandler extends ActionHandler {

    public RewardGroupHandler(Action action, ConfigurationSection section, ActiveCampaign campaign) {

        super(action, section, campaign);

    }

    @Override
    public void loadAction() {

        sendMessages("start message");

        section.getStringList("rewards").forEach(reward -> campaign.getParty().getPlayers().forEach(player -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), reward.replace("%player%", player.getName()))));

        isComplete = true;

        sendMessages("complete message");

    }

}
