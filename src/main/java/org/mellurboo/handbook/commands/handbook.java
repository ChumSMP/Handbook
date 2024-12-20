package org.mellurboo.handbook.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class handbook implements CommandExecutor {

    private ItemStack book;
    private FileConfiguration config;

    private final JavaPlugin plugin;

    /// Initialise this class
    public handbook(org.mellurboo.handbook.Handbook plugin) {
        this.plugin = plugin;
        writeBook();
    }

    /// run then "handbook" command logic when called
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("handbook")) {
            if (sender instanceof Player || args[0].equalsIgnoreCase("reload")) {
                if (args.length > 0) {
                    switch (args[0]){
                        case "give":
                            if (args.length == 1) {giveBook((Player) sender, sender); break;}
                            Player recipient = Bukkit.getPlayer(args[1]);
                            if (recipient != null) {
                                giveBook(recipient, sender);
                            }else {
                                sender.sendMessage(ChatColor.RED + plugin.getConfig().getString("PLAYER_NOT_ONLINE"));
                            }
                            break;
                        case "reload":
                            if (sender.hasPermission("handbook.admin")) plugin.reloadConfig(); else
                                sender.sendMessage(ChatColor.RED + plugin.getConfig().getString("NO_PERMISSION"));
                            break;
                        default:
                            break;
                    }
                    return true;
                }

                ((Player) sender).openBook(book);
                return true;
            }else {
                sender.sendMessage(ChatColor.RED + plugin.getConfig().getString("NOT_A_PLAYER"));
                return false;
            }
        }
        return false;
    }

    /// Handle giving the book to the player and the recipient. the thing is we need to make sure we're
    /// only allowing admins to give other players the book so we restrict it by permissions. but we managed
    /// that earlier so it's fine.
    private void giveBook(Player recipient, CommandSender sender) {
        // Check if the player already has the handbook
        if (!sender.hasPermission("handbook.admin")) {
            sender.sendMessage(ChatColor.RED + plugin.getConfig().getString("BAD_PERMISSIONS"));
            return;
        }

        // Give the book to the player
        ItemStack handbookCopy = book.clone();
        recipient.getInventory().addItem(handbookCopy);

        // Tell the sender and recipient if they're not the same.
        if (recipient != sender) {
            sender.sendMessage(ChatColor.GREEN + plugin.getConfig().getString("HANDBOOK_GRANTED") + recipient.getName() + ".");
        }
        recipient.sendMessage(ChatColor.GREEN + plugin.getConfig().getString("HANDBOOK_RECIEVED"));
    }

    /// write the book settings and metadata to the ItemStack data
    /// to make it reusable, this means we don't have to write it everytime
    /// as this is expensive
    private void writeBook() {
        FileConfiguration config = plugin.getConfig();
        List<String> pages = config.getStringList("handbook.pages");

        if (pages.isEmpty()) {
            plugin.getLogger().severe("pages are not defined in config.yml!");
            return;
        }

        book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();

        if (bookMeta != null) {
            bookMeta.setTitle(config.getString("handbook.title", "Server Handbook"));
            bookMeta.setAuthor(config.getString("handbook.author", "Server Admin"));
            bookMeta.setPages(pages);
            book.setItemMeta(bookMeta);
        }
    }
}
