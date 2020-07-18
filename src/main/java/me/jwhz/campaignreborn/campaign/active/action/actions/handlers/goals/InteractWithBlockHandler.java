package me.jwhz.campaignreborn.campaign.active.action.actions.handlers.goals;

import me.jwhz.campaignreborn.campaign.active.action.Action;
import me.jwhz.campaignreborn.utils.Utils;
import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.actions.EventActionHandler;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractWithBlockHandler extends EventActionHandler {

    public InteractWithBlockHandler(Action action, ConfigurationSection section, ActiveCampaign campaign) {

        super(action, section, campaign);

    }

    @Override
    public void loadAction() {

        sendMessages("start message");

    }

    @Override
    public void handle(Event event) {

        PlayerInteractEvent interactEvent = (PlayerInteractEvent) event;

        Location b = interactEvent.getClickedBlock().getLocation(),
                goal = Utils.getLocation(section.getConfigurationSection("location"));

        if (!isComplete() && b.getBlockX() == goal.getBlockX() && b.getBlockY() == goal.getBlockY() && b.getBlockZ() == goal.getBlockZ() && goal.getWorld().getName().equals(b.getWorld().getName())) {

            sendMessages("complete message");

            setComplete();

        }

    }
}
