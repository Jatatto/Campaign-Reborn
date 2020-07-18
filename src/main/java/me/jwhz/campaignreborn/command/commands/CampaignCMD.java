package me.jwhz.campaignreborn.command.commands;

import me.jwhz.campaignreborn.campaign.Campaign;
import me.jwhz.campaignreborn.campaign.Settings;
import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.command.CommandBase;
import me.jwhz.campaignreborn.config.ConfigValue;
import me.jwhz.campaignreborn.gui.guis.StorageClaimGUI;
import me.jwhz.campaignreborn.party.Party;
import me.jwhz.campaignreborn.party.invitations.Invite;
import me.jwhz.campaignreborn.stats.Stat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@CommandBase.Info(
        command = "campaign",
        onlyPlayers = false
)
public class CampaignCMD extends CommandBase {

    @ConfigValue(path = "messages.commands.help page")
    public List<String> helpPage = Arrays.asList(
            "&a&lCampaign Help",
            "&7- &a/campaign create <campaign name>",
            "&7- &a/campaign invite <player>",
            "&7- &a/campaign accept <player>",
            "&7- &a/campaign kick <player>",
            "&7- &a/campaign disband",
            "&7- &a/campaign storage",
            "&7- &a/campaign leave",
            "&7- &a/campaign start",
            "&7- &a/campaign list"
    );

    @ConfigValue(path = "messages.commands.admin help page")
    public List<String> adminExtras = Arrays.asList(
            "&7- &a/campaign activeList",
            "&7- &a/campaign reload"
    );

    @ConfigValue(path = "messages.commands.create invalid usage")
    public String createInvalidUsage = "&cInvalid usage: /campaign create <campaign>";

    @ConfigValue(path = "messages.commands.invalid campaign")
    public String invalidCampaign = "&cInvalid campaign entered.";

    @ConfigValue(path = "messages.commands.created party")
    public String createdParty = "&aYou have created a campaign party.";

    @ConfigValue(path = "messages.commands.not in party")
    public String notInParty = "&cYou are not currently in a party.";

    @ConfigValue(path = "messages.commands.not owner of party")
    public String notOwnerOfParty = "&cYou must be the owner of the party to do this.";

    @ConfigValue(path = "messages.commands.invite invalid usage")
    public String inviteInvalidUSage = "&cInvalid usage: /campaign invite <player>";

    @ConfigValue(path = "messages.commands.invalid player")
    public String invalidPlayer = "&cThis is not a valid player.";

    @ConfigValue(path = "messages.commands.player already invited")
    public String alreadyInvited = "&cThis player has already been invited.";

    @ConfigValue(path = "messages.commands.player already in party")
    public String alreadyInParty = "&cThis player is already in a party.";

    @ConfigValue(path = "messages.commands.your already in party")
    public String yourAlreadyInParty = "&cYou're already in a party.";

    @ConfigValue(path = "messages.commands.player invited")
    public String playerInvited = "&a%player% has been invited to the party.";

    @ConfigValue(path = "messages.commands.invitation received")
    public String invitationReceived = "&a%player% has invited you to their campaign (%campaign%). Do /campaign accept %player% to join.";

    @ConfigValue(path = "messages.commands.accept invalid usage")
    public String acceptInvalidUsage = "&cInvalid usage: /campaign accept <player>";

    @ConfigValue(path = "messages.commands.player no longer in party")
    public String noLongerInParty = "&cThis player is no longer in a party.";

    @ConfigValue(path = "messages.commands.party full")
    public String partyFull = "&cThis party is full.";

    @ConfigValue(path = "messages.commands.invalid invitation")
    public String invalidInvitation = "&cYou have not been invited to this party or your invitation has expired.";

    @ConfigValue(path = "messages.commands.kick invalid usage")
    public String kickInvalidUsage = "&cInvalid usage: /campaign kick <player>";

    @ConfigValue(path = "messages.commands.not in your party")
    public String notInYouParty = "&cThis player must be in your party to be able to kick them.";

    @ConfigValue(path = "messages.commands.kicked from party")
    public String kickedFromParty = "&cYou have been kicked from the party.";

    @ConfigValue(path = "messages.commands.party disband")
    public String partyDisband = "&cThis party has been disbanded.";

    @ConfigValue(path = "messages.commands.owner cant leave")
    public String ownerCantLeave = "&cPlease use /campaign disband.";

    @ConfigValue(path = "messages.commands.left party")
    public String leftParty = "&cYou have left the campaign party.";

    @ConfigValue(path = "messages.commands.empty your storage")
    public String emptyYourStorage = "&cYour storage isn't empty, please empty it first. (/campaign storage)";

    @ConfigValue(path = "messages.commands.nothing in storage")
    public String nothingInStorage = "&cThere is nothing in your storage.";

    @ConfigValue(path = "messages.commands.only owner can start")
    public String onlyOwnerCanStart = "&cOnly the owner can start the campaign.";

    @ConfigValue(path = "messages.commands.campaign already started")
    public String campaignStarted = "&cThis campaign has already started.";

    @ConfigValue(path = "messages.commands.this campaign isn't replayable")
    public String notReplayable = "&cThis campaign isn't replayable and you have already beaten it.";

    @Override
    public void onCommand(CommandSender sender, String[] args) {

        if (args.length < 1 || args[0].equalsIgnoreCase("help")) {

            for (String message : helpPage)
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

            if (sender.hasPermission("CampaignReborn.admin"))
                for (String message : adminExtras)
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

            return;

        }

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (args[0].equalsIgnoreCase("storage")) {

                if (core.inventoryHolder.getStorage(player).size() == 0) {

                    player.sendMessage(nothingInStorage);
                    return;

                }

                new StorageClaimGUI(player);
                return;

            }

            if (args[0].equalsIgnoreCase("kick")) {

                if (!core.partyManager.isInParty(player)) {

                    player.sendMessage(notInParty);
                    return;

                }

                Party party = core.partyManager.getParty(player);

                if (args.length < 2) {

                    player.sendMessage(kickInvalidUsage);
                    return;

                }

                Player toKick = Bukkit.getPlayer(args[1]);

                if (toKick == null) {

                    player.sendMessage(invalidPlayer);
                    return;

                }

                if (!party.getOwner().equals(player.getUniqueId())) {

                    player.sendMessage(notOwnerOfParty);
                    return;

                }

                if (!party.getPlayers().contains(toKick)) {

                    player.sendMessage(notInYouParty);
                    return;

                }

                party.leaveParty(toKick, true, false);

                toKick.sendMessage(kickedFromParty);

                return;

            }

            if (args[0].equalsIgnoreCase("accept")) {

                if (args.length < 2) {

                    player.sendMessage(acceptInvalidUsage);
                    return;

                }

                Player inviter = Bukkit.getPlayer(args[1]);

                if (inviter == null) {

                    player.sendMessage(invalidPlayer);
                    return;

                }

                Party party = core.partyManager.getParty(inviter);

                if (party == null) {

                    player.sendMessage(noLongerInParty);
                    return;

                }

                if (party.getPlayers().size() >= party.getCampaign().getSettings().getMaximumPlayers()) {

                    player.sendMessage(partyFull);
                    return;

                }

                if (!party.isInvited(player)) {

                    player.sendMessage(invalidInvitation);
                    return;

                }

                if (core.inventoryHolder.getStorage(player).size() > 0) {

                    player.sendMessage(emptyYourStorage);
                    return;

                }

                party.joinParty(player);
                return;

            }

            if (args[0].equalsIgnoreCase("invite")) {

                if (!core.partyManager.isInParty(player)) {

                    player.sendMessage(notInParty);
                    return;

                }

                Party party = core.partyManager.getParty(player);

                if (!party.getOwner().equals(player.getUniqueId())) {

                    player.sendMessage(notOwnerOfParty);
                    return;

                }

                if (args.length < 2) {

                    player.sendMessage(inviteInvalidUSage);
                    return;

                }

                Player invitee = Bukkit.getPlayer(args[1]);

                if (invitee == null) {

                    player.sendMessage(invalidPlayer);
                    return;

                }

                if (party.isInvited(invitee)) {

                    player.sendMessage(alreadyInvited);
                    return;

                }

                if (core.partyManager.isInParty(invitee)) {

                    player.sendMessage(alreadyInParty);
                    return;

                }

                party.addInvite(new Invite(player.getUniqueId(), invitee.getUniqueId()));

                party.sendMessage(
                        playerInvited.replace("%player%", invitee.getName())
                );

                invitee.sendMessage(
                        invitationReceived.replace("%player%", player.getName()).replace("%campaign%", party.getCampaign().getIdentifier())
                );

                return;

            }

            if (args[0].equalsIgnoreCase("leave")) {

                if (!core.partyManager.isInParty(player)) {

                    player.sendMessage(notInParty);
                    return;

                }

                Party party = core.partyManager.getParty(player);

                if (party.getOwner().equals(player.getUniqueId())) {

                    player.sendMessage(ownerCantLeave);
                    return;

                }

                party.leaveParty(player, true, false);
                player.sendMessage(leftParty);

                return;

            }

            if (args[0].equalsIgnoreCase("disband")) {

                if (!core.partyManager.isInParty(player)) {

                    player.sendMessage(notInParty);
                    return;

                }

                Party party = core.partyManager.getParty(player);

                if (!party.getOwner().equals(player.getUniqueId())) {

                    player.sendMessage(notOwnerOfParty);
                    return;

                }

                party.sendMessage(partyDisband);
                core.partyManager.remove(party.getIdentifier());
                party.disband(true, false);

                return;

            }

            if (args[0].equalsIgnoreCase("create")) {

                if (args.length < 2) {

                    player.sendMessage(createInvalidUsage);
                    return;

                }

                if (!core.campaignManager.exists(args[1])) {

                    player.sendMessage(invalidCampaign);
                    return;

                }

                if (core.partyManager.isInParty(player)) {

                    player.sendMessage(yourAlreadyInParty);
                    return;

                }

                if (core.inventoryHolder.getStorage(player).size() > 0) {

                    player.sendMessage(emptyYourStorage);
                    return;

                }

                Campaign campaign = core.campaignManager.getCampaign(args[1]);

                if (!campaign.getSettings().isReplayable() && core.stats.isSet(Stat.MAP_COMPLETIONS.getValueFromCampaign(player, campaign.getIdentifier())) &&
                        (int) core.stats.getStat(Stat.MAP_COMPLETIONS.getValueFromCampaign(player, campaign.getIdentifier()), 0) > 0) {

                    player.sendMessage(notReplayable);
                    return;

                }

                Party party = new Party(campaign);
                party.joinParty(player);

                core.partyManager.add(party);

                player.sendMessage(createdParty);

                return;

            }

            if (args[0].equalsIgnoreCase("start")) {

                if (core.partyManager.isInParty(player)) {

                    Party party = core.partyManager.getParty(player);

                    if (!party.getOwner().equals(player.getUniqueId())) {

                        player.sendMessage(onlyOwnerCanStart);
                        return;

                    }

                    if (party.getCampaign().getSettings().getMinimumPlayers() < party.getCampaign().getSettings().getMinimumPlayers()) {

                        player.sendMessage(core.messages.notEnoughToStart);
                        return;

                    }

                    if (core.activeCampaignManager.hasCampaign(party)) {

                        player.sendMessage(campaignStarted);
                        return;

                    }

                    ActiveCampaign activeCampaign = new ActiveCampaign(party.getCampaign().getIdentifier(), party);

                    core.activeCampaignManager.add(activeCampaign);

                    party.sendMessage(core.messages.startingSoon);

                    Bukkit.getScheduler().runTaskLater(core, activeCampaign::onStart, 20 * core.globalSettings.getTimeToStartCampaign());

                    return;

                } else {

                    player.sendMessage(notInParty);
                    return;

                }

            }

        }

        if (args[0].equalsIgnoreCase("list")) {

            sender.sendMessage(c("&aCampaigns:"));

            if (core.campaignManager.getCampaigns().size() == 0)
                sender.sendMessage(c("&7- &cNone"));

            core.campaignManager.getCampaigns().forEach(campaign -> sender.sendMessage(c("&7- &a" + campaign.getIdentifier())));

            return;

        }

        if (sender.hasPermission("CampaignReborn.admin")) {

            if (args[0].equalsIgnoreCase("reload")) {

                sender.sendMessage(c("&aReloaded campaign plugin."));
                core.reload();
                return;

            }

            if (args[0].equalsIgnoreCase("activelist")) {

                sender.sendMessage(c("&aActive Campaign List:"));

                if (core.partyManager.getList().size() == 0)
                    sender.sendMessage(c("&7- &cNone"));

                core.partyManager.getList().forEach(party -> {

                    sender.sendMessage(c("&7- &aParty: " + party.toString()));
                    sender.sendMessage(c("&7    - &bCampaign: " + party.getCampaign().getIdentifier()));

                });

            }

        }

    }

}
