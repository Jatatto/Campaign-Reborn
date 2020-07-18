package me.jwhz.campaignreborn.kits;

import me.jwhz.campaignreborn.CampaignReborn;
import me.jwhz.campaignreborn.campaign.Campaign;
import me.jwhz.campaignreborn.manager.ManagerObject;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Kit extends ManagerObject<String> {

    private String name;

    public Kit(String name) {

        this.name = name;

    }

    public String getName() {

        return name;

    }

    public void setItems(PlayerInventory inv) {

        CampaignReborn.getInstance().kitManager.getYamlConfiguration().set("Kits." + name + ".items", inv.getContents());
        CampaignReborn.getInstance().kitManager.getYamlConfiguration().set("Kits." + name + ".armor", inv.getArmorContents());
        CampaignReborn.getInstance().kitManager.getYamlConfiguration().set("Kits." + name + ".extra", inv.getExtraContents());

        CampaignReborn.getInstance().kitManager.save();

    }

    public ItemStack[][] getItems() {

        return new ItemStack[][]{
                fromList(CampaignReborn.getInstance().kitManager.getYamlConfiguration().getList("Kits." + name + ".items")),
                fromList(CampaignReborn.getInstance().kitManager.getYamlConfiguration().getList("Kits." + name + ".armor")),
                fromList(CampaignReborn.getInstance().kitManager.getYamlConfiguration().getList("Kits." + name + ".extra"))

        };

    }

    private ItemStack[] fromList(List list) {

        ItemStack[] array = new ItemStack[list.size()];

        IntStream.range(0, array.length).filter(i -> list.get(i) != null).forEach(i -> array[i] = (ItemStack) list.get(i));

        return array;

    }

    public void delete() {

        CampaignReborn.getInstance().kitManager.getYamlConfiguration().set("Kits." + name, null);
        CampaignReborn.getInstance().kitManager.save();

    }

    @Override
    public String getIdentifier() {

        return name;

    }

}
