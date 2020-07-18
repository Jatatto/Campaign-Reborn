package me.jwhz.campaignreborn;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.Action;
import me.jwhz.campaignreborn.campaign.active.action.actions.ActionHandler;
import me.jwhz.campaignreborn.campaign.active.action.actions.EventActionHandler;
import me.jwhz.campaignreborn.campaign.active.action.actions.TickActionHandler;
import me.jwhz.campaignreborn.campaign.active.action.actions.handlers.goals.HealthPoolHandler;
import me.jwhz.campaignreborn.campaign.active.action.actions.handlers.goals.PlayerSurviveHandler;
import me.jwhz.campaignreborn.campaign.active.action.actions.handlers.goals.ProtectRandomPlayerHandler;
import me.jwhz.campaignreborn.campaign.active.action.actions.handlers.goals.RandomActionHandler;
import me.jwhz.campaignreborn.stats.Stat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CampaignPlaceholderExpansion extends PlaceholderExpansion {

    private CampaignReborn core;

    public CampaignPlaceholderExpansion(CampaignReborn core) {

        this.core = core;

        PlaceholderAPI.registerPlaceholderHook(getIdentifier(), this);

    }

    @Override
    public boolean canRegister() {

        return true;

    }

    @Override
    public String getIdentifier() {

        return "campaign";

    }

    @Override
    public String getPlugin() {

        return core.getName();

    }

    @Override
    public String getAuthor() {

        return core.getDescription().getAuthors().get(0);

    }

    @Override
    public String getVersion() {

        return core.getDescription().getVersion();

    }

    @Override
    public String onPlaceholderRequest(Player player, String value) {

        if (core.campaignManager.exists(value))
            return core.stats.getStat(Stat.MAP_COMPLETIONS.getValueFromCampaign(player, value), 0) + "";

        if (value.equalsIgnoreCase("goal")) {

            if (!core.activeCampaignManager.isInActiveCampaign(player))
                return "";

            ActiveCampaign campaign = core.activeCampaignManager.getActiveCampaign(player);

            for (Action action : campaign.getCurrentPhase().getActions()) {

                ActionHandler handler = action.getHandler();

                if (!(handler instanceof RandomActionHandler) && !(handler instanceof HealthPoolHandler) && !(handler instanceof ProtectRandomPlayerHandler)
                        && !(handler instanceof PlayerSurviveHandler) && (handler instanceof TickActionHandler || handler instanceof EventActionHandler))
                    return ChatColor.translateAlternateColorCodes('&', action.getGoalName());

            }

        }

        return null;

    }

}
