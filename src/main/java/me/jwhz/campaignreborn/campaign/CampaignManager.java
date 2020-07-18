package me.jwhz.campaignreborn.campaign;

import me.jwhz.campaignreborn.CampaignReborn;
import me.jwhz.campaignreborn.manager.Manager;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CampaignManager extends Manager<Campaign> {

    private File directory;

    public CampaignManager() {

        super("config");

        directory = new File(CampaignReborn.getInstance().getDataFolder() + "/campaigns");

        if (!directory.exists())
            directory.mkdir();

    }

    public List<Campaign> getCampaigns() {

        return Arrays.stream(Objects.requireNonNull(directory.listFiles())).map(file -> new Campaign(file.getName().replace(".yml", ""))).collect(Collectors.toList());

    }

    public boolean exists(String name){

        return getCampaigns().stream().anyMatch(campaign -> campaign.getIdentifier().equalsIgnoreCase(name));

    }

    public Campaign getCampaign(String name){

        return getCampaigns().stream().filter(campaign -> campaign.getIdentifier().equalsIgnoreCase(name)).findFirst().get();

    }

}
