package me.jwhz.campaignreborn.gui.guis;

import me.jwhz.campaignreborn.gui.GUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class StorageClaimGUI extends GUI {

    private List<ItemStack> storage;

    public StorageClaimGUI(Player player) {

        this.inventory = Bukkit.createInventory(null, 45, "Storage Claim");

        addDefaultListening(player);

        this.storage = core.inventoryHolder.getStorage(player);

        setupGUI(player);

        player.openInventory(inventory);

    }

    @Override
    public void onClose(InventoryCloseEvent e) {

        core.inventoryHolder.saveStorage((Player) e.getPlayer(), storage);

    }

    @Override
    public void onClick(InventoryClickEvent e) {

        if (e.getClickedInventory() != null && e.getClickedInventory().equals(inventory) && storage.size() > 0) {

            Player player = (Player) e.getWhoClicked();

            if (player.getInventory().firstEmpty() != -1) {

                player.getInventory().addItem(storage.get(e.getSlot()).clone());
                storage.remove(e.getSlot());

                core.inventoryHolder.saveStorage((Player) e.getWhoClicked(), storage);

                setupGUI((Player) e.getWhoClicked());

            }

        }

    }

    @Override
    public void setupGUI(Player player) {

        inventory.clear();

        for (int i = 0; i < storage.size(); i++)
            inventory.setItem(i, storage.get(i));

    }

}
