package me.jwhz.campaignreborn.campaign.active.action.actions;

import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.Action;
import org.bukkit.configuration.ConfigurationSection;

public abstract class TickActionHandler extends ActionHandler {

    public TickActionHandler(Action action, ConfigurationSection section, ActiveCampaign campaign) {

        super(action, section, campaign);

    }

    public abstract boolean onTick();

}
