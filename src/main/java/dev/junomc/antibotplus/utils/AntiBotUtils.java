package dev.junomc.antibotplus.utils;

import dev.junomc.antibotplus.AntiBotPlus;
import net.md_5.bungee.api.ChatColor;

public class AntiBotUtils {
    public AntiBotPlus getInstance() {
        return AntiBotPlus.getInstance();
    }

    public String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public FileDataUtils getFileDataUtils() {
        return getInstance().getFileDataUtils();
    }

    public String getPrefix() {
        return getInstance().getPrefix();
    }

    public boolean isVaultHooked() {
        return getInstance().isVaultHooked();
    }

    public String getLang() {
        return getInstance().getLang();
    }
}
