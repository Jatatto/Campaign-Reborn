package me.jwhz.campaignreborn.manager;

import me.jwhz.campaignreborn.CampaignReborn;

public abstract class ManagerObject<I> {

    protected CampaignReborn core = CampaignReborn.getInstance();

    public abstract I getIdentifier();

}