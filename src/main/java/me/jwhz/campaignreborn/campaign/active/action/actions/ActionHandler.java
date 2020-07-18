package me.jwhz.campaignreborn.campaign.active.action.actions;

import me.jwhz.campaignreborn.CampaignReborn;
import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.Action;
import me.jwhz.campaignreborn.campaign.active.action.ActionCompleteEvent;
import me.jwhz.campaignreborn.campaign.active.action.ActionType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public abstract class ActionHandler implements Cloneable {

    protected Action action;
    protected ConfigurationSection section;
    protected ActiveCampaign campaign;
    protected CampaignReborn core;

    protected boolean isComplete;

    public ActionHandler(Action action, ConfigurationSection section, ActiveCampaign campaign) {

        this.action = action;
        this.section = section;
        this.campaign = campaign;
        this.core = CampaignReborn.getInstance();

    }

    public ActiveCampaign getCampaign() {

        return campaign;

    }

    public ConfigurationSection getSection() {

        return section;

    }

    public void setComplete() {

        if (!isComplete) {

            this.isComplete = true;
            if (!getCampaign().getCurrentPhase().isChangingPhases())
                Bukkit.getPluginManager().callEvent(new ActionCompleteEvent(action, ActionType.valueOf(section.getString("type"))));

        }

    }

    public boolean isComplete() {

        return isComplete;

    }

    public void sendMessages(String path) {

        if (section.isSet(path))
            section.getStringList(path).forEach(msg -> campaign.getParty().sendMessage(msg));

    }

    public abstract void loadAction();

    @Override
    public Object clone() {

        try {

            return super.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return null;

    }

}
