package me.jwhz.campaignreborn.effect.effects;

import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.effect.CampaignEffect;
import me.jwhz.campaignreborn.utils.Utils;
import org.bukkit.configuration.ConfigurationSection;

public class BasicEffect extends CampaignEffect {

    public BasicEffect(ActiveCampaign activeCampaign, ConfigurationSection section) {

        super(activeCampaign, section);

    }

    @Override
    public void loadParticles() {

        getLocations().add(Utils.getLocation(section.getConfigurationSection("location")));

    }

}
