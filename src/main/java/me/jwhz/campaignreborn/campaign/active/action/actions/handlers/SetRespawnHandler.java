package me.jwhz.campaignreborn.campaign.active.action.actions.handlers;

import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.Action;
import me.jwhz.campaignreborn.campaign.active.action.actions.EventActionHandler;
import me.jwhz.campaignreborn.campaign.active.action.actions.handlers.goals.ProtectRandomPlayerHandler;
import me.jwhz.campaignreborn.utils.Utils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerRespawnEvent;

public class SetRespawnHandler extends EventActionHandler {

    private Location respawnLocation;

    public SetRespawnHandler(Action action, ConfigurationSection section, ActiveCampaign campaign) {

        super(action, section, campaign);

    }

    @Override
    public void handle(Event event) {

        PlayerRespawnEvent e = (PlayerRespawnEvent) event;

        e.setRespawnLocation(respawnLocation);

    }

    @Override
    public void loadAction() {

        sendMessages("start message");

        respawnLocation = Utils.getLocation(section.getConfigurationSection("location"));

        sendMessages("complete message");

    }

}