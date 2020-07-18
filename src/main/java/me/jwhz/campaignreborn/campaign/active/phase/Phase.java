package me.jwhz.campaignreborn.campaign.active.phase;

import me.jwhz.campaignreborn.CampaignReborn;
import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.Action;
import me.jwhz.campaignreborn.campaign.active.action.ActionType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Phase {

    private List<Action> actions = null;
    private List<Action> successActions = null;

    private ConfigurationSection section;
    private ActiveCampaign campaign;

    private boolean changingPhases = false;

    public Phase(ConfigurationSection section, ActiveCampaign campaign) {

        this.section = section;
        this.campaign = campaign;

    }

    public boolean isChangingPhases() {

        return changingPhases;

    }

    public List<Action> getActions() {

        return loadActions();

    }

    private List<Action> loadActions() {

        if (actions != null)
            return actions;

        actions = new ArrayList<>();

        if (section.isSet("goals"))
            for (String action : section.getConfigurationSection("goals").getKeys(false))
                actions.add(new Action(section.getConfigurationSection("goals." + action), campaign));

        return actions;

    }

    public boolean isComplete() {

        return getActions().stream().allMatch(Action::isComplete);

    }

    public void onStart() {

        if (section.isSet("onStart"))
            for (String action : section.getConfigurationSection("onStart").getKeys(false))
                new Action(section.getConfigurationSection("onStart." + action), campaign);

        getActions();

    }

    public void onSuccess() {

        this.changingPhases = true;

        if (!hasAnotherPhase())
            for (Player player : campaign.getParty().getPlayers())
                CampaignReborn.getInstance().inventoryHolder.restore(player, campaign);

        if (section.isSet("onSuccess"))
            for (Action action : onSuccessActions())
                action.getHandler().loadAction();

        if (campaign.getCurrentPhase().equals(this)) {

            campaign.onEnd(true);
            CampaignReborn.getInstance().activeCampaignManager.remove(campaign.getIdentifier());

        }

        this.changingPhases = false;

    }

    public boolean hasAnotherPhase() {

        return onSuccessActions().stream().anyMatch(action -> action.getActionType() == ActionType.START_NEW_PHASE);

    }

    private List<Action> onSuccessActions() {

        if (successActions != null)
            return successActions;

        successActions = new ArrayList<>();

        if (section.isSet("onSuccess"))
            for (String action : section.getConfigurationSection("onSuccess").getKeys(false))
                successActions.add(new Action(section.getConfigurationSection("onSuccess." + action), campaign, false));

        return successActions;

    }

}
