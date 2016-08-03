package com.omnititan.staffmode;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;



/**
 * Created by shaan on 1/08/2016.
 */
private class smodeCommand implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player && label.equals("smode")) {
            Player player = (Player) sender;
            if(player.hasPermission("staffmode.smode.inventory")) {

                File f = new File(Main.getPlugin.getDataFolder().getAbsolutePath()+ "/inventories", player.getName() + ".yml");
                FileConfiguration c = YamlConfiguration.loadConfiguration(f);

                boolean isStaffMode;
                if(c.get("StaffMode") == null){
                    isStaffMode = false;
                    player.sendMessage("Still null");
                } else {
                    isStaffMode = (boolean) c.get("StaffMode");
                    player.sendMessage(String.valueOf(isStaffMode));
                }



                if (!isStaffMode) {
                    try {
                        c.set("StaffMode", true);
                        c.save(f); // Must have this save before calling saveInventory....why?
                        saveInventory(player, f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.getInventory().clear();
                } else if(isStaffMode) {
                    try {
                        player.sendMessage("This is running");
                        restoreInventory(player, f);
                        c.set("StaffMode", false);
                        c.save(f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    player.sendMessage("&4Incorrect Usage of command - /smode");
                }
                return true;
            } else {
               return false;
            }
        } else {
            return false;
        }
    }

    private void saveInventory(Player p, File f) throws IOException {
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        c.set("inventory.armor", p.getInventory().getArmorContents());
        c.set("inventory.content", p.getInventory().getContents());
        c.save(f);
    }

    @SuppressWarnings("unchecked")
    private void restoreInventory(Player p, File f) throws IOException {
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        ItemStack[] content = ((List<ItemStack>) c.get("inventory.armor")).toArray(new ItemStack[0]);
        p.getInventory().setArmorContents(content);
        content = ((List<ItemStack>) c.get("inventory.content")).toArray(new ItemStack[0]);
        p.getInventory().setContents(content);
    }
}
