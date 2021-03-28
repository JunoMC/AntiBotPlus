package dev.junomc.antibotplus;

import dev.junomc.antibotplus.eoyaml.YamlMapping;
import dev.junomc.antibotplus.filters.EngineInterface;
import dev.junomc.antibotplus.filters.NewEngine;
import dev.junomc.antibotplus.filters.OldEngine;
import dev.junomc.antibotplus.listeners.PlayerPreLoginListener;
import dev.junomc.antibotplus.utils.AntiBotUtils;
import dev.junomc.antibotplus.utils.FileDataUtils;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

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

    public static Logger log;
    public boolean is19Server;
    public boolean is13Server;
    public boolean oldEngine;

    private static EngineInterface eng;

    @Override
    public void onEnable() {
        instance = this;

        this.log = this.getLogger();

        this.is19Server = true;
        this.is13Server = false;
        this.oldEngine = false;

        getFileDataUtils().createFile("config.yml", YamlFile.CONFIG.getYml());
        getFileDataUtils().createFile("whitelist.yml");
        getFileDataUtils().createFile("blacklist.yml");
        getFileDataUtils().createFile("languages/" + getLang() + ".yml",
                "bot-kick-message:",
                "  - '&f[&c&lAntiBot&a&l+&f]'",
                "  - '&cWe have recognized you as a robot'",
                "  - '&f&lContact admin if you thing this is error'");

        YamlMapping mapping = getFileDataUtils().read("config.yml");
        YamlMapping settings = mapping.yamlMapping("Settings");

        enable = settings.yamlMapping("mode").string("always-prevent-bot").equals("true");

        messages(ConsoleLevel.SUCCESS, getPrefix(), brand());
        messages(ConsoleLevel.SUCCESS, getPrefix(),
                "Has been enabled!",
                "Always on prevent bot mode: " + enable,
                "Work with Vault: " + isVaultHooked()
        );

        getMcVersion();

        if (this.oldEngine) {
            this.eng = new OldEngine();
        } else {
            this.eng = new NewEngine();
        }

        this.getEngine().hideConsoleMessages();

        registerEvents(new PlayerPreLoginListener());

        if (isVaultHooked()) {
            setupPermissions();
        }
    }

    public EngineInterface getEngine() {
        return this.eng;
    }

    private void getMcVersion() {
        final String[] serverVersion = Bukkit.getBukkitVersion().split("-");
        final String version = serverVersion[0];
        messages(ConsoleLevel.SUCCESS, getPrefix(), "spigot version: " + version);

        if (version.matches("1.7.10") || version.matches("1.7.9") || version.matches("1.7.5") || version.matches("1.7.2") || version.matches("1.8.8") || version.matches("1.8.3") || version.matches("1.8.4") || version.matches("1.8")) {
            this.is19Server = false;
            this.is13Server = false;
            this.oldEngine = true;
        }
        else if (version.matches("1.9") || version.matches("1.9.1") || version.matches("1.9.2") || version.matches("1.9.3") || version.matches("1.9.4") || version.matches("1.10") || version.matches("1.10.1") || version.matches("1.10.2") || version.matches("1.11") || version.matches("1.11.1") || version.matches("1.11.2")) {
            this.oldEngine = true;
            this.is19Server = true;
            this.is13Server = false;
        }
        else if (version.matches("1.13") || version.matches("1.13.1") || version.matches("1.13.2")) {
            this.oldEngine = false;
            this.is19Server = true;
            this.is13Server = true;
        }
        else if (version.matches("1.14") || version.matches("1.14.1") || version.matches("1.14.2") || version.matches("1.14.3") || version.matches("1.14.4") || version.matches("1.15")) {
            this.oldEngine = false;
            this.is19Server = true;
            this.is13Server = true;
        }
        else if (version.matches("1.15") || version.matches("1.15.1") || version.matches("1.15.2")) {
            this.oldEngine = false;
            this.is19Server = true;
            this.is13Server = true;
        }
        else if (version.matches("1.16") || version.matches("1.16.1") || version.matches("1.16.2") || version.matches("1.16.3") || version.matches("1.16.4") || version.matches("1.16.5")) {
            this.oldEngine = false;
            this.is19Server = true;
            this.is13Server = true;
        }
        else {
            this.is13Server = true;
            this.is19Server = true;
            this.oldEngine = false;
        }
    }

    private void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, getInstance());
        }
    }

    public String getLang() {
        YamlMapping mapping = getFileDataUtils().read("config.yml");
        return mapping.string("Language");
    }

    public boolean isVaultHooked() {
        return !(getServer().getPluginManager().getPlugin("Vault") == null);
    }

    private Permission perms = null;

    public Permission getVault() {
        return perms;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    @Override
    public void onDisable() {
        messages(ConsoleLevel.SUCCESS, getPrefix(), brand());
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

        List<String> getYml() {
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
                            "    always-prevent-bot: 'true'",
                            "",
                            "    #if you set to false, we'll use following settings",
                            "    #its mean we will enable prevent-bot mode when have 8 joiners in 3 seconds",
                            "    count-join: 8",
                            "    during: 3",
                            "",
                            "  save:",
                            "    #save ip address of bot, you can save your space",
                            "    whitelist: 'true'",
                            "    blacklist: 'true'",
                            "",
                            "  action:",
                            "    amount-bot-prevent: 'true'",
                            "    format: '&f[&eAntiBot&a+&f] &b{amount} &fhad been prevented from server!'",
                            ""
                    ));
                    break;
            }

            return yml;
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

    private void messages(ConsoleLevel level, String prefix, List<String> msgs) {
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
