package me.jwhz.campaignreborn.campaign.active.action.actions.handlers.goals;

import me.jwhz.campaignreborn.campaign.active.action.Action;
import me.jwhz.campaignreborn.campaign.active.action.actions.TickActionHandler;
import me.jwhz.campaignreborn.utils.Utils;
import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.actions.ActionHandler;
import org.bukkit.configuration.ConfigurationSection;

public class CollectItemsHandler extends TickActionHandler {

    public CollectItemsHandler(Action action, ConfigurationSection section, ActiveCampaign campaign) {

        super(action, section, campaign);

    }

    public boolean onTick() {

        if (campaign.getParty().hasItems(Utils.readString(section.getString("item"))) && !isComplete()) {

            sendMessages("complete message");
            setComplete();

            return true;

        }

        return false;

    }

    @Override
    public void loadAction() {

        sendMessages("start message");

    }

}
