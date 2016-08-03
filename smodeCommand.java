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

public class smodeCommand implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player && label.equals("smode")) {
            Player player = (Player) sender;
            if(player.hasPermission("staffmode.smode.inventory")) {

                File f = new File(Main.getPlugin.getDataFolder().getAbsolutePath()+ "/inventories", player.getName() + ".yml");
                FileConfiguration c = YamlConfiguration.loadConfiguration(f);

                // Get Staff Mode from config, check if null.
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
                        playToStaff(player, f, c);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if(isStaffMode) {
                    try {
                        staffToPlay(player, f, c);
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

    private void staffToPlay(Player p, File f, FileConfiguration c) throws IOException {
        // Set Staff Mode to False and Save file
        c.set("StaffMode", false);
        c.save(f);
        saveInventory(p, f, "staff");
        restoreInventory(p, f, "play");

    }

    private void playToStaff(Player p, File f, FileConfiguration c) throws IOException {
        // Set Staff Mode to true
        c.set("StaffMode", true);
        c.save(f); // Must have this save before calling saveInventory....why?
        saveInventory(p, f, "play");
        restoreInventory(p, f, "staff");

    }

    private void saveInventory(Player p, File f, String ID) throws IOException {
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        c.set("inventory.armor."+ID, p.getInventory().getArmorContents());
        c.set("inventory.content."+ID, p.getInventory().getContents());
        c.save(f);
    }

    @SuppressWarnings("unchecked")
    private void restoreInventory(Player p, File f, String ID) throws IOException {
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        ItemStack[] content = ((List<ItemStack>) c.get("inventory.armor."+ID)).toArray(new ItemStack[0]);
        p.getInventory().setArmorContents(content);
        content = ((List<ItemStack>) c.get("inventory.content."+ID)).toArray(new ItemStack[0]);
        p.getInventory().setContents(content);
    }
}
