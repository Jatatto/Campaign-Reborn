package me.jwhz.campaignreborn;

import me.jwhz.campaignreborn.config.ConfigFile;
import me.jwhz.campaignreborn.config.ConfigHandler;
import me.jwhz.campaignreborn.config.ConfigValue;

public class Messages extends ConfigFile {

    @ConfigValue(path = "messages.party.death message")
    public String deathMessage = "&a%player% has died.";

    @ConfigValue(path = "messages.party.not enough players to start")
    public String notEnoughToStart = "&cThere are not enough players in your party to start.";

    @ConfigValue(path = "messages.party.starting soon")
    public String startingSoon = "&aThe campaign will be starting in 30 seconds.";

    @ConfigValue(path = "messages.party.player joined")
    public String playerJoin = "&a%player% has joined the party.";

    @ConfigValue(path = "messages.party.player leave")
    public String playerLeave = "&c%player% has left the party.";

    @ConfigValue(path = "messages.storage.some items were stored")
    public String someItemsWereStored = "&cSome of your item's couldn't be retrieved because your inventory is full. Please do /campaign storage to claim them.";

    @ConfigValue(path = "messages.commands.this command isn't allowed during campaign")
    public String commandNotAllowed = "&cThis command isn't allowed during this campaign.";

    @ConfigValue(path = "messages.campaign.cant place block there")
    public String cantPlaceThere = "&cYou can't place the block there.";

    @ConfigValue(path = "messages.campaign.cant break this block")
    public String cantBreak = "&cYou have already changed this block.";

    public Messages() {

        super("messages");

        ConfigHandler.setPresets(this);
        ConfigHandler.reload(this);

    }

}
