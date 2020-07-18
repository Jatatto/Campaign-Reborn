package me.jwhz.campaignreborn.campaign.active.action;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.actions.EventActionHandler;
import me.jwhz.campaignreborn.campaign.active.action.actions.TickActionHandler;
import me.jwhz.campaignreborn.campaign.active.action.actions.handlers.goals.HealthPoolHandler;
import me.jwhz.campaignreborn.campaign.active.action.actions.handlers.goals.ProtectRandomPlayerHandler;
import me.jwhz.campaignreborn.gui.guis.ContainerShowerGUI;
import me.jwhz.campaignreborn.manager.Manager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ActionManager extends Manager<Action> implements Listener {

    public List<Action> queuedActions = new ArrayList<>();
    public List<Action> removeActions = new ArrayList<>();

    public ActionManager() {

        super("config");

        Bukkit.getPluginManager().registerEvents(this, core);

        Bukkit.getScheduler().runTaskTimer(core, () -> {

            Iterator<Action> actions = getList().iterator();

            while (actions.hasNext()) {

                Action action = actions.next();

                if (removeActions.contains(action))
                    actions.remove();
                else if (action.getHandler() instanceof HealthPoolHandler && ((HealthPoolHandler) action.getHandler()).onTick())
                    actions.remove();
                else if (action.isComplete() && action.getActionType() != ActionType.SET_RESPAWN)
                    actions.remove();
                else if (action.getHandler() instanceof TickActionHandler && ((TickActionHandler) action.getHandler()).onTick())
                    actions.remove();

            }

            Iterator<Action> queue = queuedActions.iterator();

            while (queue.hasNext())
                if (removeActions.contains(queue.next()))
                    queue.remove();

            removeActions.clear();

            getList().addAll(queuedActions);
            queuedActions.clear();

        }, 0, 4);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent e) {

        if (e.getEntity() instanceof Player && !e.isCancelled()) {

            Action toHandle = null;

            for (Action action : getList())
                if (action.getActionType() == ActionType.HEALTH_POOL && action.getActiveCampaign().getParty().getPlayers().contains(e.getEntity())) {
                    toHandle = action;
                    break;
                }

            if (toHandle != null)
                ((EventActionHandler) toHandle.getHandler()).handle(e);

        }

    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){

        Action toHandle = null;

        for (Action action : getList())
            if (action.getActionType() == ActionType.BREAK_BLOCKS && action.getActiveCampaign().getParty().getPlayers().contains(e.getPlayer())) {
                toHandle = action;
                break;
            }

        if (toHandle != null)
            ((EventActionHandler) toHandle.getHandler()).handle(e);


    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {

        Action toHandle = null;

        for (Action action : getList())
            if (action.getActionType() == ActionType.PLACE_BLOCKS && action.getActiveCampaign().getParty().getPlayers().contains(e.getPlayer())) {
                toHandle = action;
                break;
            }

        if (toHandle != null)
            ((EventActionHandler) toHandle.getHandler()).handle(e);

    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {

        Action toHandle = null;

        for (Action action : getList())
            if (action.getActionType() == ActionType.EXECUTE_COMMAND && action.getActiveCampaign().getParty().getPlayers().contains(e.getPlayer())) {
                toHandle = action;
                break;
            }

        if (toHandle != null) {

            ((EventActionHandler) toHandle.getHandler()).handle(e);
            if (toHandle.isComplete())
                return;

        }

        if (core.activeCampaignManager.isInActiveCampaign(e.getPlayer()) && !core.globalSettings.isAllowed(e.getMessage()) && !e.getPlayer().hasPermission("CampaignReborn.bypass")) {

            e.getPlayer().sendMessage(core.messages.commandNotAllowed);
            e.setCancelled(true);

        }

    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {

        Action toHandle = null;

        for (Action action : getList())
            if (action.getActionType() == ActionType.SET_RESPAWN && action.getActiveCampaign().getParty().getPlayers().contains(e.getPlayer()))
                toHandle = action;
            else if (action.getActionType() == ActionType.PROTECT_PLAYER && action.getActiveCampaign().getParty().getPlayers().contains(e.getPlayer()) &&
                    ((ProtectRandomPlayerHandler) action.getHandler()).getPlayer().equals(e.getPlayer()))
                ((ProtectRandomPlayerHandler) action.getHandler()).setDead();

        if (toHandle != null)
            ((EventActionHandler) toHandle.getHandler()).handle(e);

        core.partyManager.updateVisibility();

    }

    @EventHandler
    public void onBossKill(MythicMobDeathEvent e) {

        Action toHandle = null;

        for (Action action : getList())
            if (action.getActionType() == ActionType.KILL_BOSS && action.getActiveCampaign().mobs.contains(e.getMob())) {
                toHandle = action;
                break;
            }

        if (toHandle != null)
            ((EventActionHandler) toHandle.getHandler()).handle(e);

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {

        if (core.activeCampaignManager.isInActiveCampaign(e.getPlayer())) {

            Action toHandle = null;

            for (Action action : getList())
                if (action.getActionType() == ActionType.REACH_LOCATION && action.getActiveCampaign().getParty().getPlayers().contains(e.getPlayer())) {
                    toHandle = action;
                    break;
                }

            if (toHandle != null)
                ((EventActionHandler) toHandle.getHandler()).handle(e);

        }

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {

        Action toHandle = null;

        for (Action action : getList())
            if (action.getActionType() == ActionType.INTERACT_WITH_BLOCK && e.getClickedBlock() != null && action.getActiveCampaign().getParty().getPlayers().contains(e.getPlayer())) {
                toHandle = action;
                break;
            }

        if (toHandle != null)
            ((EventActionHandler) toHandle.getHandler()).handle(e);


        if (e.getClickedBlock() != null && e.getClickedBlock().getState() instanceof org.bukkit.inventory.InventoryHolder) {

            ActiveCampaign activeCampaign = core.activeCampaignManager.getActiveCampaign(e.getPlayer());

            if (activeCampaign == null)
                return;

            if (activeCampaign.containers.containsKey(e.getClickedBlock().getLocation()))
                e.getPlayer().openInventory(activeCampaign.containers.get(e.getClickedBlock().getLocation()).inventory);
            else {

                ContainerShowerGUI gui = new ContainerShowerGUI(activeCampaign, e.getClickedBlock());

                activeCampaign.containers.put(e.getClickedBlock().getLocation(), gui);

                e.getPlayer().openInventory(gui.inventory);

            }

            e.setCancelled(true);

        }

    }

}
