package me.jwhz.campaignreborn.campaign;

import me.jwhz.campaignreborn.utils.Utils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class Settings {

    private ConfigurationSection section;

    public Settings(ConfigurationSection section) {

        this.section = section;

    }

    public boolean isSeparateInventory() {

        return section.getBoolean("separate inventory");

    }

    public boolean keepMissionInventory() {

        return section.getBoolean("keep mission inventory");

    }

    public String getStartingPhase() {

        return section.getString("starting phase");

    }

    public boolean isReplayable() {

        return section.getBoolean("replayable", true);

    }

    public int getMinimumPlayers() {

        return section.getInt("lobby.minimum players");

    }

    public int getMaximumPlayers() {

        return section.getInt("lobby.maximum players");

    }

    public Location getLobby() {

        return Utils.getLocation(section.getConfigurationSection("lobby.location"));

    }

}
