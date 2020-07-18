package me.jwhz.campaignreborn.campaign.active.action.actions.handlers.goals;

import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.Action;
import me.jwhz.campaignreborn.campaign.active.action.actions.EventActionHandler;
import me.jwhz.campaignreborn.utils.Utils;
import me.jwhz.campaignreborn.utils.blockchange.BlockChange;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class PlaceBlocksHandler extends EventActionHandler {

    /**
     * PlaceBlock:
     * type: PLACE_BLOCKS
     * id: 1
     * amount: 5
     * place message: '&aThe party has place another block (%amount_placed%/5).'
     */

    private String message;
    private Material material;
    private int amountNeeded, amountPlaced;

    public PlaceBlocksHandler(Action action, ConfigurationSection section, ActiveCampaign campaign) {

        super(action, section, campaign);

    }

    @Override
    public void handle(Event event) {

        BlockPlaceEvent placeEvent = (BlockPlaceEvent) event;

        if (!isComplete()) {

            if (core.blockChanger.isChanged(campaign.getParty(), placeEvent.getBlock().getLocation()) || core.blockChanger.isChanged(campaign.getParty(), placeEvent.getBlock().getLocation())) {

                placeEvent.getPlayer().sendMessage(core.messages.cantPlaceThere);
                placeEvent.setCancelled(true);

                return;

            }

            if (placeEvent.getBlock().getType() == material) {

                amountPlaced++;
                placeEvent.setCancelled(true);

                core.blockChanger.addBlockChange(campaign.getParty(), new BlockChange(placeEvent.getBlock().getLocation(), placeEvent.getBlock().getBlockData()));

                campaign.getParty().sendMessage(message.replace("%amount_placed%", amountPlaced + ""));

                ItemStack toRemove = new ItemStack(placeEvent.getBlock().getType(), 1);
                placeEvent.getPlayer().getInventory().removeItem(toRemove);

            }else if(!placeEvent.getPlayer().hasPermission("CampaignReborn.bypass"))
                placeEvent.setCancelled(true);

            if (amountNeeded == amountPlaced) {

                setComplete();

                sendMessages("complete message");

            }

        }

    }

    @Override
    public void loadAction() {

        this.message = section.getString("place message");
        this.material = Utils.getMaterial(section.getString("id")).getType();
        this.amountNeeded = section.getInt("amount");
        this.amountPlaced = 0;

        sendMessages("start message");

    }

}
