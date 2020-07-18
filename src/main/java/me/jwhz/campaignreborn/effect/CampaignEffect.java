package me.jwhz.campaignreborn.effect;

import me.jwhz.campaignreborn.CampaignReborn;
import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.manager.ManagerObject;
import me.jwhz.campaignreborn.utils.Utils;
import net.minecraft.server.v1_15_R1.PacketPlayOutWorldParticles;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class CampaignEffect extends ManagerObject<ConfigurationSection> {

    private List<Location> locations = new ArrayList<>();

    protected CampaignReborn core = CampaignReborn.getInstance();
    protected ActiveCampaign activeCampaign;
    protected ConfigurationSection section;

    public CampaignEffect(ActiveCampaign activeCampaign, ConfigurationSection section) {

        this.activeCampaign = activeCampaign;
        this.section = section;

        loadParticles();

    }

    public List<Location> getLocations() {

        return locations;

    }

    public void onTick() {

        getLocations().forEach(location -> activeCampaign.getParty().getPlayers().forEach(player -> sendParticles(player, location)));

    }

    public boolean showsOnce() {

        return section.getBoolean("show-once", false);

    }

    public Color getColor() {

        String[] rgb = section.getString("color").split(",");

        return Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));

    }

    public void sendParticles(Player player, Location location) {

        Particle particle = Particle.valueOf(section.getString("effect"));

        int size = section.isSet("size") ? section.getInt("size") : 1;
        int amount = section.isSet("amount") ? section.getInt("amount") : 1;

        if (section.isSet("color"))
            player.spawnParticle(particle, location, amount, new Particle.DustOptions(getColor(), size));
        else if (section.isSet("item"))
            player.spawnParticle(particle, location, amount, Utils.readString(section.getString("item")));
        else
            player.spawnParticle(particle, location, amount);


    }

    @Override
    public ConfigurationSection getIdentifier() {

        return section;

    }

    public abstract void loadParticles();

}
