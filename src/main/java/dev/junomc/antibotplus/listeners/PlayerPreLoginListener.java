package dev.junomc.antibotplus.listeners;

import com.google.gson.JsonObject;
import dev.junomc.antibotplus.AntiBotPlus;
import dev.junomc.antibotplus.eoyaml.YamlMapping;
import dev.junomc.antibotplus.eoyaml.YamlSequence;
import dev.junomc.antibotplus.utils.AntiBotUtils;
import dev.junomc.antibotplus.utils.FileDataUtils;
import dev.junomc.antibotplus.utils.RequestUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import java.util.concurrent.TimeUnit;

public class PlayerPreLoginListener implements Listener {
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
        String address = e.getAddress().getHostAddress();

        AntiBotUtils antiBotUtils = new AntiBotUtils();
        FileDataUtils dataUtils = antiBotUtils.getFileDataUtils();

        YamlMapping mapping = dataUtils.read("config.yml");
        YamlMapping settings = mapping.yamlMapping("Settings");

        YamlSequence whiteList = dataUtils.read("whitelist.yml").yamlSequence("IP_List");
        YamlSequence blackList = dataUtils.read("blacklist.yml").yamlSequence("IP_List");

        if (whiteList.size() > 0) {
            if (whiteList.values().contains(address)) {
                return;
            }
        }

        YamlSequence kickSequence = dataUtils.read("languages/" + antiBotUtils.getLang() + ".yml").yamlSequence("bot-kick-message");
        String kickMsg = "";

        for (int i = 0; i < kickSequence.size(); i++) {
            if (i != 0) {
                kickMsg += '\n';
            }
            kickMsg += kickSequence.string(i);
        }

        if (blackList.size() > 0) {
            if (blackList.values().contains(address)) {
                e.setLoginResult(Result.KICK_FULL);
                e.setKickMessage(antiBotUtils.color(kickMsg));
                AntiBotPlus.botCounter++;
                return;
            }
        }

        if (!settings.yamlMapping("mode").string("always-prevent-bot").equals("true")) {
            AntiBotPlus.joinCounter++;

            if (AntiBotPlus.joinTime == 0) {
                AntiBotPlus.joinTime = System.currentTimeMillis();
            }

            if (time(System.currentTimeMillis(), AntiBotPlus.timeEnable) >= 600 && AntiBotPlus.enable) {
                if (AntiBotPlus.joinCounter >= 200) {
                    AntiBotPlus.timeEnable = System.currentTimeMillis();
                } else {
                    AntiBotPlus.enable = false;
                }

                AntiBotPlus.joinCounter = 0;
                AntiBotPlus.joinTime = 0;
            }


            if (AntiBotPlus.joinCounter >= settings.yamlMapping("mode").integer("count-join") && !AntiBotPlus.enable) {
                if (time(System.currentTimeMillis(), AntiBotPlus.joinTime) <= settings.yamlMapping("mode").integer("during")) {
                    AntiBotPlus.enable = true;

                    if (time(System.currentTimeMillis(), AntiBotPlus.timeEnable) == 0)
                        AntiBotPlus.timeEnable = System.currentTimeMillis();
                }

                AntiBotPlus.joinCounter = 0;
                AntiBotPlus.joinTime = 0;
            }
        }

        if (AntiBotPlus.enable) {
            String APIKey = mapping.yamlMapping("IPHunter").string("APIKey");

            JsonObject object = new RequestUtils(APIKey, address).response();

            if (object.get("status").getAsString().equals("success")) {
                if (object.has("data")) {
                    JsonObject userData = object.get("data").getAsJsonObject();

                    if (userData.get("block").getAsInt() == 0) {
                        if (settings.yamlMapping("save").string("whitelist").equals("true")) {
                            dataUtils.WriteFile("whitelist.yml", "  - '" + address + "'");
                        }
                    } else {
                        AntiBotPlus.botCounter++;

                        if (settings.yamlMapping("save").string("blacklist").equals("true")) {
                            dataUtils.WriteFile("blacklist.yml", "  - '" + address + "'");
                        }

                        if (settings.yamlMapping("action").string("amount-bot-prevent").equals("true")) {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                boolean administrator = false;

                                if (antiBotUtils.isVaultHooked()) {
                                    if (antiBotUtils.getVault().has(player, "abp.announce") || antiBotUtils.getVault().has(player, "*")) {
                                        administrator = true;
                                    }
                                } else {
                                    if (player.hasPermission("abp.announce") || player.hasPermission("*")) {
                                        administrator = true;
                                    }
                                }

                                if (player.isOp()) {
                                    administrator = true;
                                }

                                if (administrator) {
                                    String message = settings.yamlMapping("action").string("format")
                                            .replace("&", "§")
                                            .replace("{amount}", String.valueOf(AntiBotPlus.botCounter));
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                                }
                            }
                        }

                        e.setLoginResult(Result.KICK_FULL);
                        e.setKickMessage(antiBotUtils.color(kickMsg));
                    }
                }
            }
        }
    }

    private long time(long i, long b) {
        return TimeUnit.MILLISECONDS.toSeconds(i - b);
    }
}
