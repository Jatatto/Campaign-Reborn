package me.jwhz.campaignreborn.campaign.active.action.actions.handlers.goals;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import me.jwhz.campaignreborn.CampaignReborn;
import me.jwhz.campaignreborn.campaign.active.action.Action;
import me.jwhz.campaignreborn.utils.Utils;
import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.actions.EventActionHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class KillBossHandler extends EventActionHandler {

    public KillBossHandler(Action action, ConfigurationSection section, ActiveCampaign campaign) {

        super(action, section, campaign);

    }

    @Override
    public void loadAction() {

        sendMessages("start message");

        ActiveMob mob = CampaignReborn.getInstance().mythicMobs.getMobManager().spawnMob(section.getString("mob name"), Utils.getLocation(section.getConfigurationSection("location")));

        for (Player player : Bukkit.getOnlinePlayers())
            if (!campaign.getParty().getPlayers().contains(player))
                core.packetHider.hideEntity(player, mob.getEntity().getBukkitEntity());

        campaign.mobs.add(mob);

    }

    @Override
    public void handle(Event event) {

        MythicMobDeathEvent mobDeathEvent = (MythicMobDeathEvent) event;

        if (mobDeathEvent.getMob().getType().getInternalName().equalsIgnoreCase(section.getString("mob name")) && !isComplete()) {

            isComplete = true;

            campaign.mobs.remove(mobDeathEvent.getMob());

            dropItems(mobDeathEvent.getDrops(), mobDeathEvent.getEntity().getLocation());

            mobDeathEvent.setDrops(null);

            sendMessages("complete message");

        }

    }



    private void dropItems(List<ItemStack> items, Location location) {

        for (ItemStack itemStack : items) {

            Item item = location.getWorld().dropItemNaturally(location.add(0, 0.5, 0), itemStack);

            core.activeCampaignManager.activeCampaignLogic.addDroppedItem(item, campaign);

        }

    }

}
