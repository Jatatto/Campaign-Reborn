package me.jwhz.campaignreborn.campaign.active.action.actions.handlers.goals;

import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.Action;
import me.jwhz.campaignreborn.campaign.active.action.actions.TickActionHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.UUID;

public class ProtectRandomPlayerHandler extends TickActionHandler {

    /**
     * ProtectRandom:
     * start message:
     * - '&aProtect %player%'
     * onFail:
     * StartNewPhase:
     * type: START_NEW_PHASE
     * phase: fail
     */

    private ConfigurationSection onFail;

    private UUID player;
    private boolean playerDead = false;

    public ProtectRandomPlayerHandler(Action action, ConfigurationSection section, ActiveCampaign campaign) {

        super(action, section, campaign);

    }

    @Override
    public boolean onTick() {

        if (isComplete)
            return false;

        if (playerDead) {

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
            isComplete = true;

            return false;

        }

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
    public void loadAction() {

        if (section.isSet("onFail"))
            this.onFail = section.getConfigurationSection("onFail");
        this.player = campaign.getParty().getPlayers().get((int) (Math.random() * campaign.getParty().getPlayers().size())).getUniqueId();

        if (section.isSet("start message"))
            for (String line : section.getStringList("start message"))
                campaign.getParty().sendMessage(line.replace("%player%", Bukkit.getPlayer(player).getName()));

    }

    public Player getPlayer() {

        return Bukkit.getPlayer(player);

    }

    public void setDead() {

        this.playerDead = true;

    }

}
