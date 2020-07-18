package me.jwhz.campaignreborn.campaign.active;

import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import me.jwhz.campaignreborn.CampaignReborn;
import me.jwhz.campaignreborn.campaign.Campaign;
import me.jwhz.campaignreborn.campaign.active.action.ActionCompleteEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.Map;

public class ActiveCampaignLogic implements Listener {

    private CampaignReborn core;
    private HashMap<Integer, ActiveCampaign> dropItems = new HashMap<>();

    public ActiveCampaignLogic(CampaignReborn core) {

        this.core = core;

        Bukkit.getPluginManager().registerEvents(this, core);

    }

    public Map<Integer, ActiveCampaign> getDrops(ActiveCampaign campaign) {

        Map<Integer, ActiveCampaign> map = new HashMap<>();

        dropItems.forEach((key, value) -> {
            if (value.equals(campaign))
                map.put(key, value);
        });

        return map;

    }

    public void addDroppedItem(Item item, ActiveCampaign campaign) {

        dropItems.put(item.getEntityId(), campaign);

        for (Player player : Bukkit.getOnlinePlayers())
            if (!campaign.getParty().getPlayers().contains(player))
                core.packetHider.hideEntity(player, item);

    }

    private boolean isCampaignMob(int id) {

        return core.activeCampaignManager.getList().stream().flatMap(campaign -> campaign.mobs.stream()).anyMatch(mob -> mob.getEntity().getBukkitEntity().getEntityId() == id);

    }

    private ActiveCampaign getCampaignMob(int id) {

        return core.activeCampaignManager.getList().stream().filter(campaign -> campaign.mobs.stream().anyMatch(mob -> mob.getEntity().getBukkitEntity().getEntityId() == id)).findFirst().orElse(null);

    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {

        if (isCampaignMob(e.getEntity().getEntityId())) {

            ActiveCampaign campaign = getCampaignMob(e.getEntity().getEntityId());

            if (!(e.getTarget() instanceof Player) || !campaign.getParty().getPlayers().contains(e.getTarget())) {

                e.setCancelled(true);

                ((Creature) e.getEntity()).setTarget(getClosest(e.getEntity(), campaign));

            }

        }

    }

    private Player getClosest(Entity entity, ActiveCampaign campaign) {

        Player closest = null;
        double distance = Double.MAX_VALUE;

        for (Player player : campaign.getParty().getPlayers())
            if (player.getLocation().distanceSquared(entity.getLocation()) < distance || closest == null)
                closest = player;

        return closest;

    }

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent e) {

        ActiveCampaign campaign = null;

        if (isCampaignMob(e.getEntity().getEntityId()))
            campaign = getCampaignMob(e.getEntity().getEntityId());
        else if (e.getEntity() instanceof Player && core.activeCampaignManager.isInActiveCampaign((Player) e.getEntity()))
            campaign = core.activeCampaignManager.getActiveCampaign((Player) e.getEntity());

        if (campaign != null) {

            ((Arrow) e.getProjectile()).setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
            ((Arrow) e.getProjectile()).setBounce(false);

            e.getEntity().setMetadata("campaign", new FixedMetadataValue(core, campaign));
            e.getEntity().setMetadata("shooter", new FixedMetadataValue(core, e.getEntity().getName()));

            for (Player player : Bukkit.getOnlinePlayers())
                if (!campaign.getParty().getPlayers().contains(player))
                    core.packetHider.hideEntity(player, e.getProjectile());

        } else
            for (Player player : Bukkit.getOnlinePlayers())
                if (core.activeCampaignManager.isInActiveCampaign(player))
                    core.packetHider.hideEntity(player, e.getProjectile());

    }

    @EventHandler
    public void onCollide(ProjectileCollideEvent e) {

        ActiveCampaign collideCampaign = e.getCollidedWith() instanceof Player ? core.activeCampaignManager.getActiveCampaign((Player) e.getCollidedWith()) :
                getCampaignMob(e.getCollidedWith().getEntityId());

        Entity shooter = (Entity) e.getEntity().getShooter();

        ActiveCampaign entityCampaign = null;

        if (shooter != null)
            entityCampaign = shooter instanceof Player ? core.activeCampaignManager.getActiveCampaign((Player) shooter) :
                    getCampaignMob(shooter.getEntityId());


        if (entityCampaign == null && collideCampaign == null)
            return;

        if (entityCampaign != null && !entityCampaign.equals(collideCampaign) || !collideCampaign.equals(entityCampaign))
            e.setCancelled(true);


    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {

        ActiveCampaign damagerCampaign = e.getDamager() instanceof Player ? core.activeCampaignManager.getActiveCampaign((Player) e.getDamager()) :
                getCampaignMob(e.getDamager().getEntityId());

        ActiveCampaign entityCampaign = e.getEntity() instanceof Player ? core.activeCampaignManager.getActiveCampaign((Player) e.getEntity()) :
                getCampaignMob(e.getEntity().getEntityId());

        if (e.getDamager() instanceof Projectile) {

            Entity shooter = (Entity) ((Projectile) e.getDamager()).getShooter();

            damagerCampaign = shooter instanceof Player ? core.activeCampaignManager.getActiveCampaign((Player) shooter) :
                    getCampaignMob(shooter.getEntityId());

        }

        if (entityCampaign == null && damagerCampaign == null)
            return;

        if (entityCampaign != null && !entityCampaign.equals(damagerCampaign) || !damagerCampaign.equals(entityCampaign))
            e.setCancelled(true);
        else if (entityCampaign.equals(damagerCampaign) && e.getEntity() instanceof Player && (e.getDamager() instanceof Player || (e.getDamager() instanceof Projectile && ((Projectile) e.getDamager()).getShooter() instanceof Player)))
            e.setCancelled(true);

        if (e.getDamager() instanceof Monster && e.getEntity() instanceof Player && damagerCampaign != null && damagerCampaign.equals(entityCampaign))
            e.setCancelled(false);

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Bukkit.getScheduler().scheduleSyncDelayedTask(core, () -> {

            core.partyManager.updateVisibility();

            for (Map.Entry<Integer, ActiveCampaign> entry : dropItems.entrySet())
                core.packetHider.hideEntity(e.getPlayer(), entry.getKey());

            for (ActiveCampaign activeCampaign : core.activeCampaignManager.getList())
                for (ActiveMob mob : activeCampaign.mobs)
                    core.packetHider.hideEntity(e.getPlayer(), mob.getEntity().getBukkitEntity());

        }, 20);

    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {

        if (dropItems.containsKey(e.getItem().getEntityId())) {

            ActiveCampaign activeCampaign = dropItems.get(e.getItem().getEntityId());

            if (!activeCampaign.getParty().getPlayers().contains(e.getPlayer()))
                e.setCancelled(true);

        }

    }

    @EventHandler
    public void onEntityDrop(EntityDropItemEvent e) {

        if (isCampaignMob(e.getEntity().getEntityId()))
            addDroppedItem(e.getItemDrop(), getCampaignMob(e.getEntity().getEntityId()));

    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {

        if (core.activeCampaignManager.isInActiveCampaign(e.getPlayer()))
            addDroppedItem(e.getItemDrop(), core.activeCampaignManager.getActiveCampaign(e.getPlayer()));


    }

    @EventHandler
    public void onActionComplete(ActionCompleteEvent e) {

        if (e.getActiveCampaign().getCurrentPhase().isComplete())
            e.getActiveCampaign().getCurrentPhase().onSuccess();

    }

}
