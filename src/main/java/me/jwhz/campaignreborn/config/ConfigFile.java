package me.jwhz.campaignreborn.config;

import me.jwhz.campaignreborn.CampaignReborn;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigFile {

    public String fileName;
    public File file;
    public YamlConfiguration yamlConfiguration;

    public ConfigFile(String fileName) {

        this.fileName = fileName;

        if(!CampaignReborn.getInstance().getDataFolder().exists())
            CampaignReborn.getInstance().getDataFolder().mkdir();

        file = new File(CampaignReborn.getInstance().getDataFolder() + "/" + fileName + ".yml");

        if (!file.exists())
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }

        yamlConfiguration = YamlConfiguration.loadConfiguration(file);

    }

    public void save(){

        try {
            getYamlConfiguration().save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public File getFile() {

        return file;

    }

    public YamlConfiguration getYamlConfiguration() {

        return yamlConfiguration != null ? yamlConfiguration : YamlConfiguration.loadConfiguration(file);

    }


}
