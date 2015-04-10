/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlemail.mcdjuady.itemeffects.example;

import com.googlemail.mcdjuady.itemeffects.effect.Effect;
import com.googlemail.mcdjuady.itemeffects.effect.EffectData;
import com.googlemail.mcdjuady.itemeffects.effect.EffectDataOption;
import com.googlemail.mcdjuady.itemeffects.effect.EffectHandler;
import com.googlemail.mcdjuady.itemeffects.effect.EffectOptions;
import com.googlemail.mcdjuady.itemeffects.ItemEffects;
import com.googlemail.mcdjuady.itemeffects.effect.EffectDataMaxCombiner;
import com.googlemail.mcdjuady.itemeffects.effect.PlayerEffects;
import com.googlemail.mcdjuady.itemeffects.event.GlobalActivateEvent;
import com.googlemail.mcdjuady.itemeffects.event.GlobalDeactivateEvent;
import com.googlemail.mcdjuady.itemeffects.event.GlobalUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Max
 */
@EffectOptions(global = true, recalculateGlobal = true, dataOptions = {
    @EffectDataOption(key = "Level", dataClass = Integer.class, value = "5", combiner = EffectDataMaxCombiner.class)
})
public class LevelEffect extends Effect {

    private class EffectTask extends BukkitRunnable {

        private final Player player;
        private int desiredLevel;

        public EffectTask(EffectData globalData, Player player) {
            super();
            this.player = player;
            this.desiredLevel = globalData.getInt("Level");
        }

        public void updateData(EffectData newData) {
            this.desiredLevel = newData.getInt("Level");
        }

        @Override
        public void run() {
            if (!player.isOnline() || desiredLevel <= player.getLevel()) {
                Bukkit.getScheduler().cancelTask(this.getTaskId());
                return;
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 39, 50, true, false), true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 39, 50, true, false), true);
        }

    }

    private EffectTask task;

    public LevelEffect(ConfigurationSection effectConfig, String effectInfo, PlayerEffects parentEffects, int slot) throws InvalidConfigurationException {
        super(effectConfig, effectInfo, parentEffects, slot);
    }

    @EffectHandler
    public void onActivate(GlobalActivateEvent e) {
        int requiredLevel = e.getGlobalData().getInt("Level");
        int playerLevel = getPlayer().getLevel();
        if (requiredLevel > playerLevel) {
            task = new EffectTask(e.getGlobalData(), getPlayer());
            task.runTaskTimer(ItemEffects.getInstance(), 0, 10);
        }
    }

    @EffectHandler
    public void onUpdate(GlobalUpdateEvent e) {
        //if we have a task update the data to the recombined global data
        if (task != null) {
            task.updateData(e.getGlobalData());
        }
        //only update if the task is not running
        if (task == null || (!Bukkit.getScheduler().isQueued(task.getTaskId()) && !Bukkit.getScheduler().isCurrentlyRunning(task.getTaskId()))) {
            int requiredLevel = e.getGlobalData().getInt("Level");
            int playerLevel = getPlayer().getLevel();
            if (requiredLevel > playerLevel) {
                task = new EffectTask(e.getGlobalData(), getPlayer());
                task.runTaskTimer(ItemEffects.getInstance(), 0, 10);
            }
        }

    }

    @EffectHandler
    public void onDeactivate(GlobalDeactivateEvent e) {
        if (task != null) {
            Bukkit.getScheduler().cancelTask(task.getTaskId());
        }
    }

}
