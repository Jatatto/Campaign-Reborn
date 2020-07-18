package me.jwhz.campaignreborn.gui.guis;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.gui.GUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

import java.lang.reflect.InvocationTargetException;

public class ContainerShowerGUI extends GUI {

    private InventoryHolder holder;

    public ContainerShowerGUI(ActiveCampaign activeCampaign, Block block) {

        this.holder = (InventoryHolder) block.getState();

        this.inventory = Bukkit.createInventory(null, holder.getInventory().getSize(), "Chest");

        listener = new Listener() {

            @EventHandler
            public void onInventoryOpen(InventoryOpenEvent e) {

                if (e.getInventory().equals(inventory) && e.getViewers().size() <= 1) {

                    PacketContainer animation = new PacketContainer(PacketType.Play.Server.BLOCK_ACTION);

                    animation.getBlocks().write(0, Material.CHEST);
                    animation.getBlockPositionModifier().write(0, new BlockPosition(block.getX(), block.getY(), block.getZ()));
                    animation.getIntegers().write(0, 1);
                    animation.getIntegers().write(1, 1);

                    for (Player player : activeCampaign.getParty().getPlayers()) {
                        try {
                            ProtocolLibrary.getProtocolManager().sendServerPacket(player, animation, true);
                        } catch (InvocationTargetException e1) {
                            e1.printStackTrace();
                        }

                    }

                }

            }

            @EventHandler
            public void onInventoryClose(InventoryCloseEvent e) {

                if (e.getInventory().equals(inventory) && activeCampaign.getParty().getPlayers().contains(e.getPlayer())) {

                    if (inventory.getViewers().size() == 1) {

                        PacketContainer animation = new PacketContainer(PacketType.Play.Server.BLOCK_ACTION);

                        animation.getBlocks().write(0, Material.CHEST);
                        animation.getBlockPositionModifier().write(0, new BlockPosition(block.getX(), block.getY(), block.getZ()));
                        animation.getIntegers().write(0, 1);
                        animation.getIntegers().write(1, 0);

                        for (Player player : activeCampaign.getParty().getPlayers()) {
                            try {
                                ProtocolLibrary.getProtocolManager().sendServerPacket(player, animation, true);
                            } catch (InvocationTargetException e1) {
                                e1.printStackTrace();
                            }
                        }

                    }

                }

            }

        };

        Bukkit.getPluginManager().registerEvents(listener, core);

        setupGUI(null);


    }

    public void unregister() {

        HandlerList.unregisterAll(listener);

    }

    @Override
    public void setupGUI(Player player) {

        for (int i = 0; i < inventory.getSize(); i++)
            if (holder.getInventory().getItem(i) != null)
                inventory.setItem(i, holder.getInventory().getItem(i).clone());

    }

}
