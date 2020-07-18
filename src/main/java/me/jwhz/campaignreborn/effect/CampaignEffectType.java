package me.jwhz.campaignreborn.effect;

import me.jwhz.campaignreborn.effect.effects.BasicEffect;
import me.jwhz.campaignreborn.effect.effects.CircleEffect;

public enum CampaignEffectType {

    CIRCLE(CircleEffect.class),
    BASIC(BasicEffect.class);

    private Class clazz;

    CampaignEffectType(Class clazz) {

        this.clazz = clazz;

    }

    public Class getEffectClass() {

        return clazz;

    }

}
