// 
// Decompiled by Procyon v0.5.36
// 

package de.lburgard.replacer.config;

import java.io.InputStream;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.CopyOption;
import de.lburgard.replacer.CustomItemReplacer;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;

public class ConfigManager
{
    private final File configFile;
    private FileConfiguration config;
    
    public ConfigManager() {
        this.configFile = new File(CustomItemReplacer.getInstance().getDataFolder(), "config.yml");
        this.setupConfig();
    }
    
    public void setupConfig() {
        if (!CustomItemReplacer.getInstance().getDataFolder().exists()) {
            CustomItemReplacer.getInstance().getDataFolder().mkdir();
        }
        if (!this.configFile.exists()) {
            try {
                final InputStream resource = CustomItemReplacer.getInstance().getResource("config.yml");
                try {
                    assert resource != null;
                    Files.copy(resource, this.configFile.toPath(), new CopyOption[0]);
                    if (resource != null) {
                        resource.close();
                    }
                }
                catch (Throwable t) {
                    if (resource != null) {
                        try {
                            resource.close();
                        }
                        catch (Throwable exception) {
                            t.addSuppressed(exception);
                        }
                    }
                    throw t;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.configFile);
    }
    
    public String getString(final String path) {
        return this.config.getString(path);
    }
}
