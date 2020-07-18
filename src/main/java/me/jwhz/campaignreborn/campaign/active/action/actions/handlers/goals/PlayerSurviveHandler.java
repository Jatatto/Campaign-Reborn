package me.jwhz.campaignreborn.campaign.active.action.actions.handlers.goals;

import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.Action;
import me.jwhz.campaignreborn.campaign.active.action.actions.TickActionHandler;
import org.bukkit.configuration.ConfigurationSection;

public class PlayerSurviveHandler extends TickActionHandler {

    /**
     *
     * PlayerSurvive:
     *      type: PLAYER_SURVIVE
     *      amount: 2
     *
     */

    private int minimumPlayers;

    public PlayerSurviveHandler(Action action, ConfigurationSection section, ActiveCampaign campaign) {

        super(action, section, campaign);

    }


    @Override
    public boolean onTick() {

        if(campaign.getParty().getPlayers().size() < minimumPlayers){

            setComplete();

            sendMessages("complete message");
            campaign.onEnd(false);

            return true;

        }

        return false;

    }

    @Override
    public void loadAction() {

        minimumPlayers = section.getInt("amount");

        sendMessages("start message");

    }

}
