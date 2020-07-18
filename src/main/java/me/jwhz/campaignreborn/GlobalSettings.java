package me.jwhz.campaignreborn;

import me.jwhz.campaignreborn.utils.Utils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class GlobalSettings {

    private ConfigurationSection section;

    public GlobalSettings(ConfigurationSection section) {

        this.section = section;

    }

    public Location getSpawn(){

        return Utils.getLocation(section.getConfigurationSection("spawn"));

    }

    public int getTimeToStartCampaign() {

        return section.getInt("time to start campaign");

    }

    public List<String> getAllowedCommands() {

        return section.getStringList("allowed commands in campaign");

    }

    public boolean isAllowed(String command) {

        for (String string : getAllowedCommands())
            if (command.startsWith("/" + string))
                return true;

        return false;

    }


}
