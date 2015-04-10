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
import com.googlemail.mcdjuady.itemeffects.effect.PlayerEffects;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Max
 */
@EffectOptions(global = true, dataOptions = {
    @EffectDataOption(key = "Defense", dataClass = Integer.class, value = "5")
})
public class DefenseEffect extends Effect{

    public DefenseEffect(ConfigurationSection effectConfig, String effectInfo, PlayerEffects parentEffects, int slot) throws InvalidConfigurationException {
        super(effectConfig, effectInfo, parentEffects, slot);
    }

    @EffectHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        Player player = getPlayer();
        EffectData data = getEffectData();
        if (e.getEntity().equals(player)) {
            double damage = (e.getDamage() - data.getInt("Defense")) / data.getInt("Defense");
            e.setDamage(damage);
            
        }
    }
    
}
