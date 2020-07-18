package me.jwhz.campaignreborn.effect.effects;

import io.lumine.xikage.mythicmobs.skills.ParticleMaker;
import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.effect.CampaignEffect;
import me.jwhz.campaignreborn.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class CircleEffect extends CampaignEffect {

    /**
     * CircleEffect:
     * type: CIRCLE
     * effect: DUST
     * show-once: false
     * color: '255,0,0'
     * radius: 5
     * location:
     * x: 5
     * y: 5
     * z: 5
     * world: World
     */

    public CircleEffect(ActiveCampaign activeCampaign, ConfigurationSection section) {

        super(activeCampaign, section);

    }

    @Override
    public void loadParticles() {

        double radius = section.getDouble("radius");

        Location center = Utils.getLocation(section.getConfigurationSection("location"));

        for (double angle = 0; angle <= Math.PI * 2; angle += Math.PI / (Math.pow(radius, 2)))
            getLocations().add(new Location(center.getWorld(), center.getX() + (radius * Math.cos(angle)), center.getY(), center.getZ() + (Math.sin(angle) * radius)));

    }

}
