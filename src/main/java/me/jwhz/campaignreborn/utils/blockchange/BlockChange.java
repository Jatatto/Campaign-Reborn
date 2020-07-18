package me.jwhz.campaignreborn.utils.blockchange;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

public class BlockChange {

    private Location location;
    private BlockData data;
    public boolean sent = false;

    public BlockChange(Location location,  BlockData data) {

        this.location = location;
        this.data = data;

    }

    public Location getLocation() {

        return location;

    }

    public BlockData getChange() {

        return data;

    }

}