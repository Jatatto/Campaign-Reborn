package me.jwhz.campaignreborn.campaign.active.action.actions.handlers.goals;

import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.Action;
import me.jwhz.campaignreborn.campaign.active.action.actions.ActionHandler;
import me.jwhz.campaignreborn.campaign.active.action.actions.TickActionHandler;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class RandomActionHandler extends TickActionHandler {

    /**
     * RandomAction:
     * type: RANDOM
     * goals:
     * ReachPoint:
     * type: REACH_LOCATION
     * location:
     * x: -6
     * y: 81
     * z: -36
     * world: World
     * radius: 3
     * CollectItem:
     * type: COLLECT_ITEMS
     * item: 'item:STONE amount:8'
     * complete message:
     * - '&aCollected 8 stone goal complete.'
     */

    private Action action;

    public RandomActionHandler(Action action, ConfigurationSection section, ActiveCampaign campaign) {

        super(action, section, campaign);

    }

    @Override
    public boolean onTick() {

        if (action.isComplete() && !isComplete()){

            setComplete();

            return true;

        }

        return false;

    }

    @Override
    public void loadAction() {

        List<String> actions = new ArrayList<>();

        actions.addAll(section.getConfigurationSection("goals").getKeys(false));

        core.actionManager.queuedActions.add((action = new Action(section.getConfigurationSection("goals." + actions.get((int) (Math.random() * actions.size()))), campaign)));

    }

}
