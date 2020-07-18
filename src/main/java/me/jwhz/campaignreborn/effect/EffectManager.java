package me.jwhz.campaignreborn.effect;

import me.jwhz.campaignreborn.manager.Manager;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EffectManager extends Manager<CampaignEffect> {

    private List<CampaignEffect> effectQueue = new ArrayList<>();

    public EffectManager() {

        super("config");

        Bukkit.getScheduler().scheduleSyncRepeatingTask(core, () -> {

            Iterator<CampaignEffect> effects = getList().iterator();

            while (effects.hasNext()) {

                CampaignEffect effect = effects.next();

                if (core.activeCampaignManager.getCampaign(effect.activeCampaign.getParty()) == null)

                    effects.remove();

                else {

                    effect.onTick();

                    if (effect.showsOnce())
                        effects.remove();

                }

            }

            getList().addAll(effectQueue);
            effectQueue.clear();

        }, 0, 12);

    }

    public void addEffect(CampaignEffect effect) {

        effectQueue.add(effect);

    }


}
