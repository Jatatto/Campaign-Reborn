package me.jwhz.campaignreborn.campaign.active.action.actions;

import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.Action;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;

public abstract class EventActionHandler extends ActionHandler {

    public EventActionHandler(Action action, ConfigurationSection section, ActiveCampaign campaign) {

        super(action, section, campaign);

    }

    public abstract void handle(Event event);

}
