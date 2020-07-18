package me.jwhz.campaignreborn.campaign.active;

import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import me.jwhz.campaignreborn.campaign.Campaign;
import me.jwhz.campaignreborn.campaign.active.phase.Phase;
import me.jwhz.campaignreborn.gui.guis.ContainerShowerGUI;
import me.jwhz.campaignreborn.party.Party;
import me.jwhz.campaignreborn.stats.Stat;
import me.jwhz.campaignreborn.utils.Utils;
import me.jwhz.campaignreborn.utils.blockchange.BlockChange;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.FoodMetaData;
import net.minecraft.server.v1_15_R1.PacketPlayOutBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActiveCampaign extends Campaign {

    public List<ActiveMob> mobs = new ArrayList<>();
    public Map<Location, ContainerShowerGUI> containers = new HashMap<>();

    private Party party;
    private Phase currentPhase;

    public ActiveCampaign(String name, Party party) {

        super(name);
        this.party = party;

    }

    public Party getParty() {

        return party;

    }

    public Phase getCurrentPhase() {

        return currentPhase;

    }

    private Phase getStartingPhase() {

        return new Phase(getConfig().getYamlConfiguration().getConfigurationSection("phases." + getSettings().getStartingPhase()), this);

    }

    public void onLeave(Player player) {

        if (!getSettings().keepMissionInventory()) {

            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.getInventory().setExtraContents(null);

        }

    }

    public void startNewPhase(Phase phase) {

        (currentPhase = phase).onStart();

        core.partyManager.updateVisibility();

    }

    public void onStart() {

        for (Player player : getParty().getPlayers()) {

            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.setGameMode(GameMode.SURVIVAL);

        }

        startNewPhase(getStartingPhase());


    }

    public void onEnd(boolean complete) {

        for (ActiveMob mob : mobs) {

            mob.getEntity().remove();
            core.mythicMobs.getMobManager().setEntityDespawned(mob.getUniqueId());

        }

        if (core.activeCampaignManager.activeCampaignLogic.getDrops(this).size() > 0)
            for (Integer id : core.activeCampaignManager.activeCampaignLogic.getDrops(this).keySet()) {

                Item entity = (Item) Utils.getEntity(id);

                if (entity != null)
                    entity.teleport(new Location(entity.getWorld(), 0, 0, 0));

            }

        if (core.blockChanger.getMap().containsKey(party)) {

            List<BlockChange> changes = core.blockChanger.removeParty(party);

            for (Player player : party.getPlayers())
                for (BlockChange change : changes) {
                    if (change.getChange().getMaterial() == change.getLocation().getBlock().getType()) {

                        PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(
                                (int) (Math.random() * 10000),
                                new BlockPosition(change.getLocation().getX(), change.getLocation().getY(), change.getLocation().getZ()),
                                10
                        );

                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

                    } else

                        player.sendBlockChange(change.getLocation(), change.getLocation().getBlock().getBlockData());

                }
        }

        if (containers.size() > 0)
            for (ContainerShowerGUI container : containers.values())
                container.unregister();


        if (complete)
            for (Player player : party.getPlayers())
                core.stats.setStat(Stat.MAP_COMPLETIONS.getValueFromCampaign(player, getIdentifier()),
                        (int) core.stats.getStat(Stat.MAP_COMPLETIONS.getValueFromCampaign(player, getIdentifier()), 0) + 1);

        currentPhase = null;

        core.partyManager.remove(party.getIdentifier());

        party.disband(!complete, complete);

        core.partyManager.updateVisibility();

    }

}
