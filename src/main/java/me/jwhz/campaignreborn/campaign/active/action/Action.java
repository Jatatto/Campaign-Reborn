package me.jwhz.campaignreborn.campaign.active.action;

import me.jwhz.campaignreborn.CampaignReborn;
import me.jwhz.campaignreborn.campaign.active.ActiveCampaign;
import me.jwhz.campaignreborn.campaign.active.action.actions.ActionHandler;
import me.jwhz.campaignreborn.effect.CampaignEffect;
import me.jwhz.campaignreborn.effect.CampaignEffectType;
import me.jwhz.campaignreborn.effect.effects.BasicEffect;
import me.jwhz.campaignreborn.effect.effects.CircleEffect;
import me.jwhz.campaignreborn.manager.ManagerObject;
import org.bukkit.configuration.ConfigurationSection;

public class Action extends ManagerObject<String> {

    private ConfigurationSection section;
    private ActiveCampaign campaign;
    private ActionHandler actionHandler;

    public Action(ConfigurationSection section, ActiveCampaign campaign) {

        this(section, campaign, true);

    }

    public Action(ConfigurationSection section, ActiveCampaign campaign, boolean loadAction) {

        this.section = section;
        this.campaign = campaign;

        try {

            this.actionHandler = (ActionHandler) getActionType().getHandler().getConstructor(Action.class, ConfigurationSection.class, ActiveCampaign.class)
                    .newInstance(this, section, campaign);

            if (section.isSet("effects"))
                for (String key : section.getConfigurationSection("effects").getKeys(false)) {

                    ConfigurationSection effectSection = section.getConfigurationSection("effects." + key);

                    CampaignEffect effect = null;

                    if (effectSection.isSet("type"))
                        switch (effectSection.getString("type")) {
                            case "CIRCLE":
                                effect = new CircleEffect(campaign, effectSection);
                                break;
                            case "BASIC":
                                effect = new BasicEffect(campaign, effectSection);
                        }
                    else
                        effect = new BasicEffect(campaign, effectSection);

                    if (effect != null)
                        core.effectManager.addEffect(effect);

                }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (loadAction)
            getHandler().loadAction();

        CampaignReborn.getInstance().actionManager.queuedActions.add(this);

    }

    public boolean isComplete() {

        return getHandler().isComplete();

    }

    public ActiveCampaign getActiveCampaign() {

        return campaign;

    }

    public ActionHandler getHandler() {

        return actionHandler;

    }

    public String getGoalName() {

        if (section.isSet("goal name"))
            return section.getString("goal name");

        String name = getActionType().name().toLowerCase().replace("_", " ");

        return name.substring(0, 1).toUpperCase() + name.substring(1);

    }

    public ActionType getActionType() {

        return ActionType.valueOf(section.getString("type"));

    }

    @Override
    public String getIdentifier() {

        return null;

    }

}
