package me.jwhz.campaignreborn.command;

import me.jwhz.campaignreborn.CampaignReborn;
import me.jwhz.campaignreborn.command.commands.CampaignCMD;
import me.jwhz.campaignreborn.command.commands.KitCMD;
import me.jwhz.campaignreborn.config.ConfigHandler;
import me.jwhz.campaignreborn.config.ConfigValue;
import me.jwhz.campaignreborn.manager.Manager;
import org.jetbrains.annotations.NotNull;

public class CommandManager extends Manager<CommandBase> {

    public CommandManager() {

        super("messages");

        ConfigHandler.setPresets(this);
        ConfigHandler.reload(this);

        add(new KitCMD());
        add(new CampaignCMD());

    }

    public void reload(){

        for(CommandBase command : getList()){

            ConfigHandler.setPresets(command, getFile());
            ConfigHandler.reload(command, getFile());

        }

    }

    @Override
    public void add(CommandBase command) {

        CampaignReborn.getInstance().getCommand(command.getAnnotationInfo().command()).setExecutor(command);
        CampaignReborn.getInstance().getCommand(command.getAnnotationInfo().command()).setPermission(command.getAnnotationInfo().permission());
        CampaignReborn.getInstance().getCommand(command.getAnnotationInfo().command()).setPermissionMessage(command.noPermission);

        ConfigHandler.setPresets(command, getFile());
        ConfigHandler.reload(command, getFile());

        list.add(command);

    }
}
