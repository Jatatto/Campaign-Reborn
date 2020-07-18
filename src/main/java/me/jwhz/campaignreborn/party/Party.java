package me.jwhz.campaignreborn.party;

import me.clip.placeholderapi.PlaceholderAPI;
import me.jwhz.campaignreborn.campaign.Campaign;
import me.jwhz.campaignreborn.manager.ManagerObject;
import me.jwhz.campaignreborn.party.invitations.Invite;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Party extends ManagerObject<List<Player>> {

    private UUID owner;

    private List<Player> players = new ArrayList<Player>();
    private Map<UUID, Invite> invites = new HashMap<>();

    private Campaign campaign;

    public Party(Campaign campaign) {

        this.campaign = campaign;

    }

    /*
     * Invites
     */

    public boolean isInvited(Player player) {

        return invites.containsKey(player.getUniqueId()) && !invites.get(player.getUniqueId()).isExpired();

    }

    public void addInvite(Invite invite) {

        invites.put(invite.getInvitee(), invite);

    }

    /*
     * Player party methods
     */

    public boolean hasItems(ItemStack itemStack) {

        int amount = itemStack.getAmount();

        for (Player player : players)
            for (ItemStack item : player.getInventory().getContents())
                if (item != null && itemStack.isSimilar(item))
                    amount -= item.getAmount();

        return amount <= 0;

    }

    public void sendMessage(String message) {

        getPlayers().forEach(player ->
                player.sendMessage(
                        ChatColor.translateAlternateColorCodes('&',
                                core.placeholderAPI ?
                                        PlaceholderAPI.setPlaceholders(player, message) :
                                        message
                        )
                )
        );

    }

    /*
     * Join/Leave
     */

    public void joinParty(Player player) {

        players.add(player);
        invites.remove(player.getUniqueId());

        player.teleport(campaign.getSettings().getLobby());

        if (players.size() > 1)
            sendMessage(core.messages.playerJoin.replace("%player%", player.getName()));
        else
            owner = player.getUniqueId();

        if (campaign.getSettings().isSeparateInventory()) {

            core.inventoryHolder.store(player);

            player.getInventory().setContents(new ItemStack[]{});
            player.getInventory().setArmorContents(null);
            player.getInventory().setExtraContents(null);

        }

    }

    public void leaveParty(Player player, boolean showMessage, boolean successful) {

        players.remove(player);

        player.teleport(core.globalSettings.getSpawn());

        if (players.size() > 0 && showMessage)
            sendMessage(core.messages.playerLeave.replace("%player%", player.getName()));

        if (players.size() == 0) {

            core.partyManager.getList().remove(this);
            core.activeCampaignManager.getList().removeIf(activeCampaign -> activeCampaign.getParty().equals(this));

        }

        if (!successful && campaign.getSettings().isSeparateInventory()) {

            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.getInventory().setExtraContents(null);

        }

        if (campaign.getSettings().isSeparateInventory())
            core.inventoryHolder.restore(player, campaign);

        if (core.activeCampaignManager.isInActiveCampaign(player))
            core.activeCampaignManager.getActiveCampaign(player).onLeave(player);

    }

    public void disband(boolean teleportToSpawn, boolean successful) {

        Iterator<Player> players = getPlayers().iterator();

        while (players.hasNext()) {

            Player player = players.next();

            if(core.gps != null && core.gps.gpsIsActive(player))
                core.gps.stopGPS(player);

            if (teleportToSpawn)
                player.teleport(core.globalSettings.getSpawn());

            if (!successful && campaign.getSettings().isSeparateInventory()) {

                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
                player.getInventory().setExtraContents(null);

            }

            if (campaign.getSettings().isSeparateInventory())
                core.inventoryHolder.restore(player, campaign);

            if (core.activeCampaignManager.isInActiveCampaign(player))
                core.activeCampaignManager.getActiveCampaign(player).onLeave(player);

            players.remove();

        }

    }

    /*
     * Getters
     */

    public Map<UUID, Invite> getInvites() {

        return invites;

    }

    public UUID getOwner() {

        return owner;

    }

    public Campaign getCampaign() {

        return campaign;

    }

    public List<Player> getPlayers() {

        return players;

    }

    public List<Player> getIdentifier() {

        return players;

    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        getPlayers().forEach(player -> builder.append(player.getName()).append(", "));

        return builder.substring(0, builder.length() - 2);

    }

}
