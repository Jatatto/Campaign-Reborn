package me.jwhz.campaignreborn.party;

import me.jwhz.campaignreborn.manager.Manager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class PartyManager extends Manager<Party> implements Listener {

    public PartyManager() {

        super("config");

        Bukkit.getPluginManager().registerEvents(this, core);

    }

    public List<Party> getParties() {

        return getList();

    }

    public Party getParty(Player player) {

        return getParties().stream().filter(party -> party.getPlayers().contains(player)).findFirst().get();

    }

    public boolean isInParty(Player player) {

        return getParties().stream().anyMatch(party -> party.getPlayers().contains(player));

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        if (isInParty(event.getPlayer())) {

            Party party = getParty(event.getPlayer());

            if (party.getPlayers().size() == 1 && core.activeCampaignManager.hasCampaign(party)) {

                core.activeCampaignManager.getCampaign(party).onEnd(false);
                updateVisibility();
                return;

            }

            if (party.getOwner().equals(event.getPlayer().getUniqueId()) && !core.activeCampaignManager.isInActiveCampaign(event.getPlayer())) {

                core.partyManager.remove(party.getIdentifier());
                party.disband(true, false);
                updateVisibility();
                return;

            }

            party.leaveParty(event.getPlayer(), true, false);

            updateVisibility();

        }

    }

    public void updateVisibility() {

        Bukkit.getOnlinePlayers().forEach(viewer -> {

            Party party = isInParty(viewer) ? getParty(viewer) : null;

            Bukkit.getOnlinePlayers().forEach(player -> {

                if (viewer != player)
                    if (party != null && !party.getPlayers().contains(player))
                        core.packetHider.hideEntity(viewer, player);
                    else if (party == null && isInParty(player))
                        core.packetHider.hideEntity(viewer, player);
                    else
                        core.packetHider.showEntity(viewer, player);

            });

        });

    }

}
