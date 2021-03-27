package dev.junomc.antibotplus;

import dev.junomc.antibotplus.eoyaml.YamlMapping;
import dev.junomc.antibotplus.utils.AntiBotUtils;
import dev.junomc.antibotplus.utils.FileDataUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class AntiBotPlus extends JavaPlugin {
    private static AntiBotPlus instance;

    public static AntiBotPlus getInstance() {
        return instance;
    }

    public static int botCounter = 0;
    public static int joinCounter = 0;
    public static long joinTime = 0;

    public static boolean enable;

    public static long timeEnable = 0;

    @Override
    public void onEnable() {
        instance = this;

        getFileDataUtils().createFile("config.yml", YamlFile.CONFIG.getYml());
        getFileDataUtils().createFile("whitelist.yml", "IP_List:");
        getFileDataUtils().createFile("blacklist.yml", "IP_List:");
        getFileDataUtils().createFile("languages/" + getLang() + ".yml", YamlFile.CONFIG.getYml());

        YamlMapping mapping = getFileDataUtils().read("config.yml");
        YamlMapping settings = mapping.yamlMapping("Settings");

        enable = Boolean.parseBoolean(settings.yamlMapping("mode").string("always-prevent-bot"));

        messages(ConsoleLevel.SUCCESS, getPrefix(), (String[]) brand().toArray());
        messages(ConsoleLevel.SUCCESS, getPrefix(),
                "Has been enabled!",
                "Always on prevent bot mode: " + enable,
                "Work with Vault: " + isVaultHooked()
        );
    }

    public String getLang() {
        YamlMapping mapping = getFileDataUtils().read("config.yml");
        return mapping.string("Language");
    }

    public boolean isVaultHooked() {
        return (getServer().getPluginManager().getPlugin("Vault") == null);
    }

    @Override
    public void onDisable() {
        messages(ConsoleLevel.SUCCESS, getPrefix(), (String[]) brand().toArray());
        messages(ConsoleLevel.DANGER, getPrefix(), "Had been disabled, see you soon!");
    }

    public String getPrefix() {
        YamlMapping mapping = getFileDataUtils().read("config.yml");
        return mapping.string("Prefix");
    }

    enum YamlFile {
        CONFIG(1), LANGUAGE(2);


        private List<String> yml;

        private int code;

        YamlFile(int code) {
            this.code = code;
        }

        String[] getYml() {
            this.yml = Collections.synchronizedList(new ArrayList<>());
            switch (this.code) {
                case 1:
                    yml.addAll(Arrays.asList(
                            "Prefix: '&f[&c&lAntiBot&a&l+&f] '",
                            "",
                            "IPHunter:",
                            "  #To get APIKey -> https://iphunter.info/",
                            "  APIKey: 'Input your APIKey here'",
                            "",
                            "#get language code here: http://www.lingoes.net/en/translator/langcode.htm/",
                            "Language: 'vi-VN'",
                            "",
                            "Settings:",
                            "  mode:",
                            "    #we will protect your server with up to 100% efficiency",
                            "    always-prevent-bot: true",
                            "",
                            "    #if you set to false, we'll use following settings",
                            "    #its mean we will enable prevent-bot mode when have 8 joiners in 3 seconds",
                            "    count-join: 8",
                            "    during: 3",
                            "",
                            "  save:",
                            "    #save ip address of bot, you can save your space",
                            "    whitelist: true",
                            "    blacklist: true",
                            "",
                            "  announce:",
                            "    bot-join-server: true",
                            "    player-join-server: true",
                            "",
                            "  action:",
                            "    amount-bot-prevent: true",
                            "    format: '&f[&eAntiBot&a+&f] &b{amount} &fhad been prevented from server!'",
                            ""
                    ));
                    break;
            }

            return (String[]) yml.toArray();
        }
    }

    enum ConsoleLevel {
        DEFAULT("&r"), INFO("&b"), WARNING("&e"), SUCCESS("&a"), DANGER("&c");

        private String lvl;

        ConsoleLevel(String lv) {
            this.lvl = lv;
        }

        String getColor() {
            return this.lvl;
        }
    }

    private void messages(ConsoleLevel level, String prefix, String... msgs) {
        AntiBotUtils abutils = new AntiBotUtils();
        for (String msg : msgs) {
            Bukkit.getConsoleSender().sendMessage(abutils.color(prefix + level.getColor() + msg));
        }
    }

    private List<String> brand() {
        return Arrays.asList(
                "",
                "",
                "    _       _   _ ___      _     _   ",
                "   /_\\  _ _| |_(_) _ ) ___| |_ _| |_ ",
                "  / _ \\| ' \\  _| | _ \\/ _ \\  _|_   _|",
                " /_/ \\_\\_||_\\__|_|___/\\___/\\__| |_|  ",
                "   version: " + version() + " - copyright by JunoMC",
                "",
                ""
        );
    }

    public FileDataUtils getFileDataUtils() {
        return new FileDataUtils();
    }

    private String version() {
        return instance.getDescription().getVersion();
    }
}
