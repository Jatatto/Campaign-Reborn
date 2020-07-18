package me.jwhz.campaignreborn;

import com.live.bemmamin.gps.api.GPSAPI;
import io.lumine.xikage.mythicmobs.MythicMobs;
import me.jwhz.campaignreborn.campaign.CampaignManager;
import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.ActiveCampaignManager;
import me.jwhz.campaignreborn.campaign.active.action.ActionManager;
import me.jwhz.campaignreborn.command.CommandManager;
import me.jwhz.campaignreborn.database.databases.FlatFileDataBase;
import me.jwhz.campaignreborn.effect.EffectManager;
import me.jwhz.campaignreborn.kits.KitManager;
import me.jwhz.campaignreborn.party.InventoryHolder;
import me.jwhz.campaignreborn.party.PartyManager;
import me.jwhz.campaignreborn.stats.Stats;
import me.jwhz.campaignreborn.utils.PacketHider;
import me.jwhz.campaignreborn.utils.blockchange.BlockChanger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;

public class CampaignReborn extends JavaPlugin {

    private static CampaignReborn instance;

    public CampaignPlaceholderExpansion campaignPlaceholderExpansion;
    public MythicMobs mythicMobs;
    public boolean placeholderAPI;
    public GPSAPI gps;

    public PacketHider packetHider;

    public ActiveCampaignManager activeCampaignManager;
    public CampaignManager campaignManager;
    public InventoryHolder inventoryHolder;
    public CommandManager commandManager;
    public GlobalSettings globalSettings;
    public ActionManager actionManager;
    public EffectManager effectManager;
    public BlockChanger blockChanger;
    public PartyManager partyManager;
    public KitManager kitManager;
    public Messages messages;
    public Stats stats;

    @Override
    public void onEnable() {

        instance = this;

        if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs"))
            this.mythicMobs = (MythicMobs) Bukkit.getPluginManager().getPlugin("MythicMobs");
        else {

            Bukkit.getPluginManager().disablePlugin(this);
            System.out.println("This plugin requires MythicMobs to be installed.");
            return;

        }

        if (Bukkit.getPluginManager().isPluginEnabled("GPS"))
            this.gps = new GPSAPI(this);


        this.placeholderAPI = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");

        saveDefaultConfig();

        this.packetHider = new PacketHider(this, PacketHider.Policy.BLACKLIST);

        this.stats = new Stats(new FlatFileDataBase("stats"));
        this.messages = new Messages();
        this.kitManager = new KitManager();
        kitManager.loadKits();

        this.partyManager = new PartyManager();
        this.blockChanger = new BlockChanger();
        this.effectManager = new EffectManager();
        this.actionManager = new ActionManager();
        this.globalSettings = new GlobalSettings(getConfig().getConfigurationSection("global settings"));
        this.commandManager = new CommandManager();
        this.inventoryHolder = new InventoryHolder();
        this.campaignManager = new CampaignManager();
        this.activeCampaignManager = new ActiveCampaignManager();

        if (placeholderAPI)
            campaignPlaceholderExpansion = new CampaignPlaceholderExpansion(this);

        for (Player player : Bukkit.getOnlinePlayers())
            packetHider.addListener(player);

    }

    @Override
    public void onDisable() {

        Iterator<ActiveCampaign> campaignIterator = activeCampaignManager.getList().iterator();

        while (campaignIterator.hasNext())
            campaignIterator.next().onEnd(false);

        partyManager.updateVisibility();

        packetHider.close();

    }


    public void reload() {

        commandManager.reload();
        messages = new Messages();
        globalSettings = new GlobalSettings(getConfig().getConfigurationSection("global settings"));

    }

    public static CampaignReborn getInstance() {

        return instance;

    }

}
