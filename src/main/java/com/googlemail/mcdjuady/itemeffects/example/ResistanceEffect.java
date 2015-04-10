/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlemail.mcdjuady.itemeffects.example;

import com.googlemail.mcdjuady.itemeffects.effect.Effect;
import com.googlemail.mcdjuady.itemeffects.effect.EffectDataOption;
import com.googlemail.mcdjuady.itemeffects.effect.EffectHandler;
import com.googlemail.mcdjuady.itemeffects.effect.EffectOptions;
import com.googlemail.mcdjuady.itemeffects.effect.PlayerEffects;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 *
 * @author Max
 */
@EffectOptions(dataOptions = {
    @EffectDataOption(key = "ResistType", dataClass = String.class, value = "ENTITY_ATTACK", canEnchant = false),
    @EffectDataOption(key = "ResistAmount", value = "5.0"),})
public class ResistanceEffect extends Effect {

    private final DamageCause resistanceType;

    public ResistanceEffect(ConfigurationSection effectConfig, String effectInfo, PlayerEffects parentEffects, int slot) throws InvalidConfigurationException {
        super(effectConfig, effectInfo, parentEffects, slot);
        resistanceType = DamageCause.valueOf(getEffectData().getString("ResistType"));
    }

    @EffectHandler(events = {EntityDamageByBlockEvent.class, EntityDamageByEntityEvent.class, EntityDamageEvent.class})
    public void onDamage(EntityDamageEvent e) {
        if (!e.getEntity().equals(getPlayer())) {
            return;
        }
        double amount = getEffectData().getDouble("ResistAmount");
        if (resistanceType == e.getCause()) {
            e.setDamage(e.getDamage() - amount);
        }
        if (resistanceType == DamageCause.FIRE && (e.getCause() == DamageCause.FIRE_TICK || e.getCause() == DamageCause.LAVA)) {
            e.setDamage(e.getDamage() - amount);
        }
        if (resistanceType == DamageCause.ENTITY_ATTACK && e.getCause() == DamageCause.PROJECTILE) {
            e.setDamage(e.getDamage() - amount);
        }
    }

}
