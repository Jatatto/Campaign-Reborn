package me.jwhz.campaignreborn.campaign.active.action;

import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.actions.ActionHandler;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ActionCompleteEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private Action action;
    private ActionHandler handler;
    private ActionType actionType;

    public ActionCompleteEvent(Action action, ActionType actionType) {

        this.action = action;
        this.handler = action.getHandler();
        this.actionType = actionType;

    }

    public Action getAction() {

        return action;

    }

    public ActiveCampaign getActiveCampaign() {

        return handler.getCampaign();

    }

    public ActionHandler getActionHandler() {

        return handler;

    }

    public ActionType getActionType() {

        return actionType;

    }

    @Override
    @NotNull
    public HandlerList getHandlers() {

        return HANDLERS;

    }

    public static HandlerList getHandlerList() {

        return HANDLERS;

    }

}
