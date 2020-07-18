package me.jwhz.campaignreborn.kits;

import me.jwhz.campaignreborn.manager.Manager;

import java.util.List;

public class KitManager extends Manager<Kit> {

    public KitManager() {

        super("kits");

    }

    public boolean isKit(String name) {

        return getKits().stream().anyMatch(kit -> kit.getName().equalsIgnoreCase(name));

    }

    public List<Kit> getKits() {

        getList().clear();

        if (getYamlConfiguration().isSet("Kits"))
            for (String name : getYamlConfiguration().getConfigurationSection("Kits").getKeys(false))
                add(new Kit(name));

        return getList();

    }

    public void loadKits() {

        if (getYamlConfiguration().isSet("Kits"))
            for (String name : getYamlConfiguration().getConfigurationSection("Kits").getKeys(false))
                add(new Kit(name));

    }

}
