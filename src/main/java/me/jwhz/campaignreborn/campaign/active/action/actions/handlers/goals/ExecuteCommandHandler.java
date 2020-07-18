package me.jwhz.campaignreborn.campaign.active.action.actions.handlers.goals;

import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.Action;
import me.jwhz.campaignreborn.campaign.active.action.actions.EventActionHandler;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class ExecuteCommandHandler extends EventActionHandler {

    /**
     * ExecuteCommand:
     * type: EXECUTE_COMMAND
     * command: 'help'
     */

    private String command;

    public ExecuteCommandHandler(Action action, ConfigurationSection section, ActiveCampaign campaign) {

        super(action, section, campaign);

    }


    @Override
    public void handle(Event event) {

        PlayerCommandPreprocessEvent commandEvent = (PlayerCommandPreprocessEvent) event;

        System.out.println(commandEvent.getMessage() + " - " + command);

        if (!isComplete() && commandEvent.getMessage().equalsIgnoreCase(command)) {

            setComplete();

            sendMessages("complete message");

        }

    }

    @Override
    public void loadAction() {

        this.command = section.getString("command");

        sendMessages("start message");

    }

}
