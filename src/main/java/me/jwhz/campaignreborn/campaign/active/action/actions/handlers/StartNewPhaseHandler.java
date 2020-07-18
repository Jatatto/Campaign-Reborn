package me.jwhz.campaignreborn.campaign.active.action.actions.handlers;

import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.Action;
import me.jwhz.campaignreborn.campaign.active.action.actions.ActionHandler;
import me.jwhz.campaignreborn.campaign.active.phase.Phase;
import org.bukkit.configuration.ConfigurationSection;

public class StartNewPhaseHandler extends ActionHandler {

    public StartNewPhaseHandler(Action action, ConfigurationSection section, ActiveCampaign campaign) {

        super(action, section, campaign);

    }

    @Override
    public void loadAction() {

        sendMessages("start message");

        campaign.startNewPhase(new Phase(campaign.getConfig().getYamlConfiguration().getConfigurationSection(
                "phases." + section.getString("phase")
        ), campaign));
        sendMessages("complete message");

        isComplete = true;

    }

}
