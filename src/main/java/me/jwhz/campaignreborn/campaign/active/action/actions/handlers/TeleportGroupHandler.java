package me.jwhz.campaignreborn.campaign.active.action.actions.handlers;

import me.jwhz.campaignreborn.CampaignReborn;
import me.jwhz.campaignreborn.campaign.active.action.Action;
import me.jwhz.campaignreborn.utils.Utils;
import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.actions.ActionHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeleportGroupHandler extends ActionHandler {

    public TeleportGroupHandler(Action action, ConfigurationSection section, ActiveCampaign campaign) {

        super(action, section, campaign);
        this.campaign = campaign;

    }

    @Override
    public void loadAction() {

        sendMessages("start message");

        if (section.isSet("delay")) {

            List<Player> players = new ArrayList<>(campaign.getParty().getPlayers());

            Bukkit.getScheduler().runTaskLater(CampaignReborn.getInstance(), () -> {

                Location location = Utils.getLocation(section.getConfigurationSection("location"));
                players.forEach(player -> player.teleport(location));

                sendMessages("complete message");

                isComplete = true;

            }, section.getInt("delay"));

        } else {

            Location location = Utils.getLocation(section.getConfigurationSection("location"));
            campaign.getParty().getPlayers().forEach(player -> player.teleport(location));

            sendMessages("complete message");

            isComplete = true;

        }

    }

}
