package me.jwhz.campaignreborn.utils;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Objects;

public class Utils {

    public static Entity getEntity(int id) {

        return Bukkit.getWorlds().stream().flatMap(world -> world.getEntities().stream()).filter(entity -> entity.getEntityId() == id).findFirst().orElse(null);

    }

    public static Location getLocation(ConfigurationSection section) {

        return section.isSet("yaw") ?
                new Location(
                        Bukkit.getWorld(Objects.requireNonNull(section.getString("world"))),
                        section.getDouble("x"),
                        section.getDouble("y"),
                        section.getDouble("z"),
                        (float) section.getDouble("yaw"),
                        (float) section.getDouble("pitch")
                ) :
                new Location(
                        Bukkit.getWorld(Objects.requireNonNull(section.getString("world"))),
                        section.getDouble("x"),
                        section.getDouble("y"),
                        section.getDouble("z")
                );

    }

    /*
     * Item Management
     */

    public static ItemStack getMaterial(String string) {

        String[] args = string.split(":");

        Material material = Material.AIR;
        byte data = 0x0;

        if (args.length < 2)
            try {
                int id = Integer.parseInt(args[0]);

                for (Material value : Material.values())
                    if (value.isLegacy() && value.getId() == id) {
                        material = value;
                        break;
                    }

            } catch (Exception e) {
                material = Material.valueOf(args[0]);
            }

        if (args.length >= 2 && args[1] != null)
            data = (byte) Integer.parseInt(args[1]);

        return new ItemStack(material, 1, data);


    }

    public static ItemStack readString(String string) {

        ItemStack item;

        Material material = null;
        int data = 0;

        if (string != null) {
            if (string.contains(" ")) {
                for (String s : string.split(" ")) {
                    if (s.contains("item:")) {
                        try {

                            int id = Integer.parseInt(s.split(":")[1]);

                            for (Material value : Material.values())
                                if (value.isLegacy() && value.getId() == id) {
                                    material = value;
                                    break;
                                }

                        } catch (Exception e) {
                            material = Material.valueOf(s.split(":")[1]);
                        }
                    }
                    if (s.contains("data:"))
                        data = Integer.parseInt(s.split(":")[1]);
                }

                item = new ItemStack(material, 1, (short) data);

                ItemMeta meta = item.getItemMeta();

                ArrayList<String> lore = new ArrayList<String>();

                for (String s : string.split(" ")) {

                    if (s.contains("display:")) {
                        meta.setDisplayName(color(s.replace("display:", "").replace("__", " ")));
                        continue;
                    }

                    if (s.contains("amount:")) {
                        item.setAmount(Integer.parseInt(s.split(":")[1]));
                        continue;

                    }

                    if (s.contains("lore:")) {
                        lore.add(color(s.replace("lore:", "").replace("__", " ")));
                        continue;
                    }

                    if (s.contains("enchant:")) {

                        meta.addEnchant(Enchantment.getByName(s.split(":")[1]), Integer.parseInt(s.split(":")[2]), true);

                    }


                }

                meta.setLore(lore);

                item.setItemMeta(meta);

                return item;
            } else if (string.startsWith("item:")) {

                return getMaterial(string.substring(5));

            }
        }

        return null;
    }

    private static String color(String string) {

        return ChatColor.translateAlternateColorCodes('&', string);

    }


}