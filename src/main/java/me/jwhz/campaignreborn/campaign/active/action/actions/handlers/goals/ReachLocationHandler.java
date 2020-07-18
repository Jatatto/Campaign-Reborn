package me.jwhz.campaignreborn.campaign.active.action.actions.handlers.goals;

import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.Action;
import me.jwhz.campaignreborn.campaign.active.action.actions.EventActionHandler;
import me.jwhz.campaignreborn.utils.Utils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class ReachLocationHandler extends EventActionHandler {

    private Location location;
    private double radius;

    public ReachLocationHandler(Action action, ConfigurationSection section, ActiveCampaign campaign) {

        super(action, section, campaign);

    }

    @Override
    public void handle(Event event) {

        if (!isComplete())
            for (Player player : campaign.getParty().getPlayers())
                if (player.getLocation().distanceSquared(location) <= radius) {

                    setComplete();

                    sendMessages("complete message");
                    return;

                }

    }

    @Override
    public void loadAction() {

        sendMessages("start message");

        this.location = Utils.getLocation(section.getConfigurationSection("location"));

        if (core.gps != null && section.isSet("gps") && section.getBoolean("gps"))
            for (Player player : campaign.getParty().getPlayers())
                core.gps.startCompass(player, location);

        this.radius = section.getDouble("radius");

    }

}
