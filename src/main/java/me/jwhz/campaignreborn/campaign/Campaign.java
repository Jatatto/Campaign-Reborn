package me.jwhz.campaignreborn.campaign;

import me.jwhz.campaignreborn.config.ConfigFile;
import me.jwhz.campaignreborn.manager.ManagerObject;

public class Campaign extends ManagerObject<String> {

    private String name;
    private Settings settings;
    private ConfigFile config;

    public Campaign(String name) {

        this.name = name;
        this.config = new ConfigFile("/campaigns/" + name);

    }

    public Settings getSettings() {

        return settings == null ? (settings = new Settings(getConfig().getYamlConfiguration().getConfigurationSection("settings"))) : settings;

    }

    public ConfigFile getConfig() {

        return config;

    }

    @Override
    public String getIdentifier() {

        return name;

    }

}
