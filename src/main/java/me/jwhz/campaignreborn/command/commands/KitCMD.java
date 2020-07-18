package me.jwhz.campaignreborn.command.commands;

import me.jwhz.campaignreborn.command.CommandBase;
import me.jwhz.campaignreborn.kits.Kit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandBase.Info(
        command = "ckits",
        permission = "CampaignReborn.admin"
)
public class KitCMD extends CommandBase {

    @Override
    public void onCommand(CommandSender sender, String[] args) {

        if (args.length < 1) {

            sender.sendMessage(c("&a&lCampaign Kits Help: "));
            sender.sendMessage(c("&7- &a/ckits create <name>"));
            sender.sendMessage(c("&7- &a/ckits delete <name>"));
            sender.sendMessage(c("&7- &a/ckits list"));
            return;

        }

        Player player = (Player) sender;

        if (args[0].equalsIgnoreCase("create")) {

            if (args.length < 2) {

                sender.sendMessage(c("&cInvalid usage: /ckits create <name>"));
                return;

            }

            if (core.kitManager.isKit(args[1])) {

                sender.sendMessage(c("&cThis kit already exists."));
                return;

            }

            sender.sendMessage(c("&aYou have created the kit using your current items/armor."));

            Kit kit = new Kit(args[1]);
            kit.setItems(player.getInventory());

            core.kitManager.add(kit);

            return;

        }

        if (args[0].equalsIgnoreCase("delete")) {

            if (args.length < 2) {

                sender.sendMessage(c("&cInvalid usage: /ckits delete <name>"));
                return;

            }

            if (!core.kitManager.isKit(args[1])) {

                sender.sendMessage(c("&cThis kit doesn't exist."));
                return;

            }

            sender.sendMessage(c("&aYou have deleted the kit."));

            Kit kit = core.kitManager.get(args[1]);
            kit.delete();
            core.kitManager.remove(kit);

            return;

        }

        if (args[0].equalsIgnoreCase("list")) {

            sender.sendMessage(c("&aKits:"));

            core.kitManager.getList().forEach(kit -> sender.sendMessage(c("&7- &a" + kit.getName())));

            if(core.kitManager.getList().size() < 1)
                sender.sendMessage(c("&7- &cNone."));

        }

    }

}
