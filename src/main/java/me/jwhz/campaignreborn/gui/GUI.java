package me.jwhz.campaignreborn.gui;

import me.jwhz.campaignreborn.CampaignReborn;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;

public abstract class GUI {

    public Inventory inventory;
    public Listener listener;
    protected CampaignReborn core = CampaignReborn.getInstance();

    public void addDefaultListening(final Player player) {

        listener = new Listener() {

            @EventHandler
            public void onInventoryClick(InventoryClickEvent e) {

                if (e.getInventory().equals(inventory) && e.getWhoClicked().equals(player)) {

                    e.setCancelled(true);
                    onClick(e);

                }
            }

            @EventHandler
            public void onInventoryInteract(InventoryInteractEvent e) {

                if (e.getInventory().equals(inventory) && e.getWhoClicked().equals(player))
                    e.setCancelled(true);

            }

            @EventHandler
            public void onInventoryClose(InventoryCloseEvent e) {

                if (e.getInventory().equals(inventory) && e.getPlayer().equals(player)) {

                    onClose(e);
                    HandlerList.unregisterAll(this);

                }

            }

        };

        core.getServer().getPluginManager().registerEvents(listener, core);

    }

    public void onClick(InventoryClickEvent e) {
    }

    public void onClose(InventoryCloseEvent e) {
    }

    public abstract void setupGUI(Player player);


}
