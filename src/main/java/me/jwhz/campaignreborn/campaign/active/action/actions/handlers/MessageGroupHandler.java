package me.jwhz.campaignreborn.campaign.active.action.actions.handlers;

import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.Action;
import me.jwhz.campaignreborn.campaign.active.action.actions.ActionHandler;
import org.bukkit.configuration.ConfigurationSection;

public class MessageGroupHandler extends ActionHandler {

    public MessageGroupHandler(Action action, ConfigurationSection section, ActiveCampaign campaign) {

        super(action, section, campaign);

    }

    @Override
    public void loadAction() {

        sendMessages("prompt");

        isComplete = true;

    }

}
