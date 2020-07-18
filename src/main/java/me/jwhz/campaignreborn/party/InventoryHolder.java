package me.jwhz.campaignreborn.party;

import me.jwhz.campaignreborn.CampaignReborn;
import me.jwhz.campaignreborn.campaign.Campaign;
import me.jwhz.campaignreborn.config.ConfigFile;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

public class InventoryHolder extends ConfigFile {

    private Map<UUID, ItemStack[][]> inventoryCache = new HashMap<>();

    public InventoryHolder() {

        super("inventory saves");

    }

    public void store(Player player) {

        inventoryCache.put(player.getUniqueId(),
                new ItemStack[][]{
                        clone(player.getInventory().getContents()),
                        clone(player.getInventory().getArmorContents()),
                        clone(player.getInventory().getExtraContents())
                }
        );

    }

    public void restore(Player player, Campaign campaign) {

        boolean usedStorage = false;

        if (inventoryCache.containsKey(player.getUniqueId())) {

            ItemStack[][] content = inventoryCache.get(player.getUniqueId());

            if (!campaign.getSettings().keepMissionInventory()) {

                player.getInventory().setContents(content[0]);
                player.getInventory().setArmorContents(content[1]);
                player.getInventory().setExtraContents(content[2]);

            } else {

                for (ItemStack item : content[0])
                    if (item != null)
                        if (player.getInventory().firstEmpty() == -1) {

                            addItemToStore(player, item);
                            usedStorage = true;

                        } else
                            player.getInventory().addItem(item);

            }

            inventoryCache.remove(player.getUniqueId());

        }

        if (usedStorage)
            player.sendMessage(CampaignReborn.getInstance().messages.someItemsWereStored);

    }

    public List<ItemStack> getStorage(Player player) {

        return (List<ItemStack>) getYamlConfiguration().getList("storage." + player.getUniqueId(), new ArrayList<>());

    }

    public void saveStorage(Player player, List<ItemStack> storage) {

        getYamlConfiguration().set("storage." + player.getUniqueId(), storage);
        save();

    }

    public void addItemToStore(Player player, ItemStack item) {

        List<ItemStack> storage = getStorage(player);

        storage.add(item);

        saveStorage(player, storage);

    }


    private ItemStack[] clone(ItemStack[] array) {

        ItemStack[] clone = new ItemStack[array.length];

        for (int i = 0; i < array.length; i++)
            if (array[i] != null)
                clone[i] = array[i].clone();

        return clone;

    }

}
