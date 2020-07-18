package me.jwhz.campaignreborn.command;

import me.jwhz.campaignreborn.CampaignReborn;
import me.jwhz.campaignreborn.config.ConfigValue;
import me.jwhz.campaignreborn.manager.ManagerObject;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class CommandBase extends ManagerObject<String> implements CommandExecutor {

    @ConfigValue(path = "Messages.no permission")
    public String noPermission = "&cYou do not have permission to use this command!";

    private Info annotationInfo = getClass().getAnnotation(Info.class);
    protected CampaignReborn core = CampaignReborn.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (annotationInfo.command().equalsIgnoreCase(command.getName())) {

            if (annotationInfo.onlyPlayers() && !(sender instanceof Player)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cOnly players can use this command!"));
                return true;
            }

            onCommand(sender, args);

        }

        return true;

    }

    @Override
    public String getIdentifier() {

        return annotationInfo.command();

    }

    public Info getAnnotationInfo() {

        return annotationInfo;

    }

    protected String c(String s) {

        return ChatColor.translateAlternateColorCodes('&', s);

    }

    public abstract void onCommand(CommandSender sender, String[] args);

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info {

        String command();

        String permission() default "CampaignReborn.user";

        boolean onlyPlayers() default true;

    }

}
