/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlemail.mcdjuady.itemeffects.example;

import com.googlemail.mcdjuady.itemeffects.EffectManager;
import com.googlemail.mcdjuady.itemeffects.ItemEffects;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Max
 */
public class ExamplePlugin extends JavaPlugin {

    private EffectManager manager;

    @Override
    public void onEnable() {
        if (!this.getDataFolder().exists()) {
            this.saveDefaultConfig();
        }
        manager = ItemEffects.getInstance().getEffectManager();
        manager.registerEffectClass("AttackEffect", AttackEffect.class);
        manager.registerEffectClass("BurnEffect", BurnEffect.class);
        manager.registerEffectClass("DefenseEffect", DefenseEffect.class);
        manager.registerEffectClass("DodgeEffect", DodgeEffect.class);
        manager.registerEffectClass("HealthEffect", HealthEffect.class);
        manager.registerEffectClass("LevelEffect", LevelEffect.class);
        manager.registerEffectClass("ResistanceEffect", ResistanceEffect.class);
        reloadConfig();
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig(); //To change body of generated methods, choose Tools | Templates.
        updateConfig();
        manager.registerEffects(getConfig().getConfigurationSection("Effects"));
    }

    private void updateConfig() {
        FileConfiguration config = this.getConfig();
        Configuration defaultConfig = config.getDefaults();
        if (config.getKeys(true).equals(defaultConfig.getKeys(true))) {
            return;
        }
        for (String key : defaultConfig.getKeys(true)) {
            if (config.get(key, null) == null) {
                config.set(key, defaultConfig.get(key));
            }
        }
        this.saveConfig();
    }

}
