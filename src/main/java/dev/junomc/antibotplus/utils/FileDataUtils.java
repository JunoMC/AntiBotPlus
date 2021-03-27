package dev.junomc.antibotplus.utils;

import dev.junomc.antibotplus.eoyaml.Yaml;
import dev.junomc.antibotplus.eoyaml.YamlMapping;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class FileDataUtils {
    public void WriteFile(File f, String... strings) {
        Arrays.asList(strings).forEach(str -> {
            FileWriter fr;
            try {
                fr = new FileWriter(f, true);
                BufferedWriter br = new BufferedWriter(fr);

                br.write(str);
                br.newLine();

                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void WriteFile(String path, String... strings) {
        String canonicalPath = null;
        try {
            canonicalPath = new File(".").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File f = new File(canonicalPath, "plugins/AntiBotPlus/" + path);

        WriteFile(f, strings);
    }

    public void createFile(File f, String... data) {
        if (!f.exists()) {
            f.getParentFile().mkdirs();
            try {
                f.createNewFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                WriteFile(f, data);
            }
        }
    }

    public void createFile(String path, String... data) {
        String canonicalPath = null;
        try {
            canonicalPath = new File(".").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File f = new File(canonicalPath, "plugins/AntiBotPlus/" + path);

        createFile(f, data);
    }

    public YamlMapping read(File f) {
        YamlMapping mapping = null;
        try {
            mapping = Yaml.createYamlInput(f).readYamlMapping();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mapping;
    }

    public YamlMapping read(String path) {
        String canonicalPath = null;
        try {
            canonicalPath = new File(".").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File f = new File(canonicalPath, "plugins/AntiBotPlus/" + path);
        return read(f);
    }
}
