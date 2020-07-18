package me.jwhz.campaignreborn.campaign.active;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import me.jwhz.campaignreborn.manager.Manager;
import me.jwhz.campaignreborn.party.Party;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ActiveCampaignManager extends Manager<ActiveCampaign> implements Listener {

    public ActiveCampaignLogic activeCampaignLogic;

    public ActiveCampaignManager() {

        super("config");

        Bukkit.getPluginManager().registerEvents(this, core);

        this.activeCampaignLogic = new ActiveCampaignLogic(core);

    }

    public ActiveCampaign getActiveCampaign(Player player) {

        return getList().stream().filter(campaign -> campaign.getParty().getPlayers().contains(player)).findFirst().orElse(null);

    }

    public boolean isInActiveCampaign(Player player) {

        return getList().stream().anyMatch(campaign -> campaign.getParty().getPlayers().contains(player));

    }

    public ActiveCampaign getCampaign(Party party) {

        return getList().stream().filter(campaign -> campaign.getParty().equals(party)).findFirst().orElse(null);

    }


    public boolean hasCampaign(Party party) {

        return getList().stream().anyMatch(campaign -> campaign.getParty().equals(party));

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        if (isInActiveCampaign(e.getPlayer())) {

            ActiveCampaign activeCampaign = getActiveCampaign(e.getPlayer());

            e.getRecipients().removeIf(player -> !activeCampaign.getParty().getPlayers().contains(player));

        } else
            e.getRecipients().removeIf(this::isInActiveCampaign);

    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

        if (isInActiveCampaign(e.getEntity())) {

            e.setKeepInventory(true);
            e.setKeepLevel(true);
            e.setDeathMessage(null);

            getActiveCampaign(e.getEntity()).getParty().sendMessage(core.messages.deathMessage.replace("%player%", e.getEntity().getName()));

        }

    }

    @EventHandler
    public void onMythicMobSpawn(MythicMobSpawnEvent e) {

        for (ActiveCampaign activeCampaign : getList())
            if (activeCampaign.mobs.contains(e.getMob())) {

                Bukkit.getOnlinePlayers().forEach(player -> {

                    if (!activeCampaign.getParty().getPlayers().contains(player)) {

                        core.packetHider.hideEntity(player, e.getEntity());

                        e.getMob().getChildren().forEach(entity -> core.packetHider.hideEntity(player, entity.getBukkitEntity()));

                    }

                });

                break;

            }

    }

}
