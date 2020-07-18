package me.jwhz.campaignreborn.campaign.active.action.actions.handlers.goals;

import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.Action;
import me.jwhz.campaignreborn.campaign.active.action.actions.EventActionHandler;
import me.jwhz.campaignreborn.utils.Utils;
import me.jwhz.campaignreborn.utils.blockchange.BlockChange;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class BlockBreakHandler extends EventActionHandler {

    private String message;
    private Material material;
    private int amountNeeded, amountBroke;

    public BlockBreakHandler(Action action, ConfigurationSection section, ActiveCampaign campaign) {

        super(action, section, campaign);

    }

    @Override
    public void handle(Event event) {

        if (isComplete)
            return;

        BlockBreakEvent breakEvent = (BlockBreakEvent) event;

        if (breakEvent.getBlock().getType() == material) {

            if(core.blockChanger.isChanged(campaign.getParty(), breakEvent.getBlock().getLocation())){

                breakEvent.getPlayer().sendMessage(core.messages.cantBreak);
                breakEvent.setCancelled(true);

                return;

            }

            amountBroke++;
            breakEvent.setCancelled(true);

            core.blockChanger.addBlockChange(campaign.getParty(), new BlockChange(breakEvent.getBlock().getLocation(), breakEvent.getBlock().getType().createBlockData()));

            campaign.getParty().sendMessage(message.replace("%amount_broke%", amountBroke + ""));

            for (ItemStack drop : breakEvent.getBlock().getDrops(breakEvent.getPlayer().getItemInHand())) {

                Item item = breakEvent.getPlayer().getWorld().dropItemNaturally(getClearBlock(breakEvent.getPlayer(), breakEvent.getBlock()), drop);

                core.activeCampaignManager.activeCampaignLogic.addDroppedItem(item, campaign);

            }

        } else if (!breakEvent.getPlayer().hasPermission("CampaignReborn.bypass"))
            breakEvent.setCancelled(true);

        if (amountBroke == amountNeeded) {

            setComplete();

            sendMessages("complete message");

        }

    }

    private Location getClearBlock(Player player, Block block) {

        List<Location> blocks = Arrays.asList(getSurroundingBlocks(block));

        blocks.sort(Comparator.comparingDouble(o -> player.getLocation().distanceSquared(o)));

        return blocks.stream().filter(loc -> loc.getBlock().getType() == Material.AIR).findFirst().orElse(null);

    }

    private Location[] getSurroundingBlocks(Block block) {

        return new Location[]{
                block.getLocation().clone().add(1, 0, 0),
                block.getLocation().clone().add(-1, 0, 0),
                block.getLocation().clone().add(0, 0, -1),
                block.getLocation().clone().add(0, 0, 1),
                block.getLocation().clone().add(0, 1, 0)
        };

    }

    @Override
    public void loadAction() {

        this.message = section.getString("break message");
        this.material = Utils.getMaterial(section.getString("id")).getType();
        this.amountNeeded = section.getInt("amount");
        this.amountBroke = 0;

        sendMessages("start message");

    }

}
