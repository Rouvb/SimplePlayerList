package net.arkamc.simplePlayerList.command;

import java.util.List;
import java.util.stream.Collectors;
import net.arkamc.simplePlayerList.SimplePlayerList;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class ListCommand implements CommandExecutor {

    private final SimplePlayerList plugin;
    private final List<String> cachedRanks;
    private Chat chat = null;

    public ListCommand(SimplePlayerList plugin) {
        this.plugin = plugin;
        setupChat();
        this.cachedRanks = plugin.getConfig().getStringList("ranks");
    }

    private void setupChat() {
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServicesManager().getRegistration(Chat.class);
        if (rsp != null) {
            chat = rsp.getProvider();
        }
    }

    private String getFormattedRanks() {
        return String.join(ChatColor.WHITE + ", ", cachedRanks);
    }

    private String getFormattedPlayerList() {
        if (chat == null) {
            List<String> playerNames = Bukkit.getOnlinePlayers()
                    .stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());
            return String.join(ChatColor.WHITE + ", ", playerNames);
        }

        List<String> playerListStr = Bukkit.getOnlinePlayers()
                .stream()
                .map(p -> {
                    String prefix = chat.getPlayerPrefix(p);
                    return prefix + p.getName();
                })
                .collect(Collectors.toList());

        return String.join(ChatColor.WHITE + ", ", playerListStr);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("list")) return false;

        String ranks = getFormattedRanks();
        String players = getFormattedPlayerList();
        String onlineCount = String.valueOf(Bukkit.getOnlinePlayers().size());
        String maxPlayers = String.valueOf(Bukkit.getMaxPlayers());

        for (String line : plugin.getConfig().getStringList("list")) {
            line = line.replace("{ranks}", ranks);
            line = line.replace("{online}", onlineCount);
            line = line.replace("{max_online}", maxPlayers);
            line = line.replace("{players}", players);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
        }

        return true;
    }

}
