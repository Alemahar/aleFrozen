package com.alemahar.alefrozen;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
  extends JavaPlugin
  implements Listener
{
  ArrayList<Player> frozen = new ArrayList();
  String prefix = (ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix")));
  
  public void onEnable()
  {
    this.getLogger().info("aleFreeze enabled successfully.");
    getConfig().options().copyDefaults(true);
    saveConfig();
    Bukkit.getServer().getPluginManager().registerEvents(this, this);
  }
  
  @EventHandler
  public void onPlayerMove(PlayerMoveEvent e)
  {
    Player p = e.getPlayer();
    if (this.frozen.contains(p))
    {
      e.setTo(e.getFrom());
      p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("onPlayerMove")));
      p.closeInventory();
         }
  }
  
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    Player player = (Player)sender;
    if (cmd.getName().equalsIgnoreCase("freeze"))
    {
      if (player.hasPermission("alefrozen.staff"))
      {
        if (args.length == 0)
        {
          player.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("noPlayerFound")));
          return true;
        }
        Player t = Bukkit.getServer().getPlayer(args[0]);
        if (t == null)
        {
          player.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("noPlayerOnline")));
          return true;
        }
        if (this.frozen.contains(t))
        {
          this.frozen.remove(t);
          t.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("onPlayerUnfreeze")));
          player.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("onStaffUnfreeze")));
          t.setBanned(false);
          return true;
        }
        this.frozen.add(t);
        player.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("onStaffFreeze")) + args[0]);
        t.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("onPlayerFreeze")) + player.getName() + ".");
        t.setBanned(true);
        
        return true;
      }
      player.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("noPermissions")));
      return true;
    }
    return false;
  }
}
