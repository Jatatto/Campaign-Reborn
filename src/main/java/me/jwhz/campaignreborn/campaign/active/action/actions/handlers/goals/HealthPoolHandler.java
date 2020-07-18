package me.jwhz.campaignreborn.campaign.active.action.actions.handlers.goals;

import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.Action;
import me.jwhz.campaignreborn.campaign.active.action.actions.EventActionHandler;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

import java.text.DecimalFormat;

public class HealthPoolHandler extends EventActionHandler {

    private DecimalFormat df = new DecimalFormat("#,###.##");

    private ConfigurationSection onFail;
    private String damageMessage;
    private double healthPool;

    public HealthPoolHandler(Action action, ConfigurationSection section, ActiveCampaign campaign) {

        super(action, section, campaign);

    }

    public boolean onTick() {

        int uncomplete = 0;

        for (Action action : campaign.getCurrentPhase().getActions())
            if (!action.isComplete())
                uncomplete++;

        if (uncomplete == 1) {

            setComplete();

            sendMessages("complete message");

            return true;

        }

        return false;

    }

    @Override
    public void handle(Event event) {

        if (isComplete)
            return;

        EntityDamageEvent damageEvent = (EntityDamageEvent) event;

        healthPool -= damageEvent.getFinalDamage();

        if (healthPool <= 0) {

            if (onFail != null) {

                for (ActiveMob mob : campaign.mobs) {

                    mob.getEntity().remove();
                    core.mythicMobs.getMobManager().setEntityDespawned(mob.getUniqueId());

                }

                for (Action action : core.actionManager.getList())
                    if (action.getActiveCampaign().equals(campaign))
                        core.actionManager.removeActions.add(action);

                for (Action action : core.actionManager.queuedActions)
                    if (action.getActiveCampaign().equals(campaign))
                        core.actionManager.removeActions.add(action);

                for (String action : onFail.getKeys(false))
                    new Action(onFail.getConfigurationSection(action), campaign);

            }

            setComplete();

            sendMessages("complete message");

        } else if (damageMessage != null)
            campaign.getParty().sendMessage(
                    damageMessage
                            .replace("%player%", damageEvent.getEntity().getName())
                            .replace("%damage%", df.format(damageEvent.getFinalDamage()))
                            .replace("%remaining%", df.format(healthPool))
            );

        onTick();

    }

    @Override
    public void loadAction() {

        if (section.isSet("onFail"))
            this.onFail = section.getConfigurationSection("onFail");

        this.healthPool = campaign.getParty().getPlayers().size() * section.getInt("health scale");

        if (section.isSet("damage message"))
            this.damageMessage = section.getString("damage message");

        sendMessages("start message");

    }

}
