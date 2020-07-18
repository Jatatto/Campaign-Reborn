package me.jwhz.campaignreborn.stats;

import org.bukkit.entity.Player;

public enum Stat {

    MAP_COMPLETIONS("stats.%player%.%campaign%");

    private String value;

    Stat(String value) {

        this.value = value;

    }

    public String getValueFromCampaign(Player player, String campaign) {

        return value.replace("%player%", player.getName()).replace("%campaign%", campaign);

    }

    public String getValueFromPlayer(Player player) {

        return value.replace("%player%", player.getName());

    }

    public String getValue() {

        return value;

    }

}
