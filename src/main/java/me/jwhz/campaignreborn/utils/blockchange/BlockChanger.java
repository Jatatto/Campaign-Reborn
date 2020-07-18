package me.jwhz.campaignreborn.utils.blockchange;

import me.jwhz.campaignreborn.CampaignReborn;
import me.jwhz.campaignreborn.party.Party;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.PacketPlayOutBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class BlockChanger {

    private CampaignReborn core = CampaignReborn.getInstance();
    private Map<Party, List<BlockChange>> blockChanges = new HashMap<>();
    private Map<Party, List<BlockChange>> blockChangesQueue = new HashMap<>();
    private List<Party> remove = new ArrayList<>();

    public BlockChanger() {

        Bukkit.getScheduler().scheduleSyncRepeatingTask(core, () -> {

            Iterator<Party> iterator = blockChanges.keySet().iterator();

            while (iterator.hasNext()) {

                Party party = iterator.next();

                if (remove.contains(party) || !core.partyManager.getParties().contains(party))
                    iterator.remove();
                else {

                    List<BlockChange> changes = blockChanges.get(party);

                    for (Player player : party.getPlayers())
                        for (BlockChange blockChange : changes)
                            if (blockChange.getChange().getMaterial() == blockChange.getLocation().getBlock().getType() && !blockChange.sent) {

                                PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(
                                        (int) (Math.random() * 10000),
                                        new BlockPosition(blockChange.getLocation().getX(), blockChange.getLocation().getY(), blockChange.getLocation().getZ()),
                                        8
                                );

                                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

                                blockChange.sent = true;

                            } else
                                player.sendBlockChange(blockChange.getLocation(), blockChange.getChange());

                }

            }

            for (Map.Entry<Party, List<BlockChange>> entry : blockChangesQueue.entrySet())
                if (blockChanges.containsKey(entry.getKey()))
                    blockChanges.get(entry.getKey()).addAll(entry.getValue());
                else
                    blockChanges.put(entry.getKey(), entry.getValue());

            blockChangesQueue.clear();

        }, 0, 1);

    }

    public boolean isChanged(Party party, Location location) {

        for (BlockChange change : getBlockChanges(party))
            if (change.getLocation().equals(location))
                return true;

        return false;

    }

    public void addBlockChange(Party party, BlockChange blockChange) {

        if (blockChangesQueue.containsKey(party))
            blockChangesQueue.get(party).add(blockChange);
        else {

            List<BlockChange> blocks = new ArrayList<>();
            blocks.add(blockChange);

            blockChangesQueue.put(party, blocks);

        }

    }

    public List<BlockChange> removeParty(Party party) {

        List<BlockChange> changes = getBlockChanges(party);
        remove.add(party);

        return changes;

    }

    public List<BlockChange> getBlockChanges(Party party) {

        List<BlockChange> changes = new ArrayList<>();

        if (blockChanges.containsKey(party))
            changes.addAll(blockChanges.get(party));

        if (blockChangesQueue.containsKey(party))
            changes.addAll(blockChangesQueue.get(party));

        return changes;

    }

    public Map<Party, List<BlockChange>> getMap() {

        return blockChanges;

    }

}
