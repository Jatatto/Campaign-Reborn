package me.jwhz.campaignreborn.campaign.active.action;

import me.jwhz.campaignreborn.campaign.active.action.actions.handlers.*;
import me.jwhz.campaignreborn.campaign.active.action.actions.handlers.goals.*;

public enum ActionType {

    BREAK_BLOCKS(BlockBreakHandler.class),

    COLLECT_ITEMS(CollectItemsHandler.class),

    HEALTH_POOL(HealthPoolHandler.class),

    KILL_BOSS(KillBossHandler.class),

    INTERACT_WITH_BLOCK(InteractWithBlockHandler.class),

    SET_KIT(SetKitHandler.class),

    MESSAGE_GROUP(MessageGroupHandler.class),

    REACH_LOCATION(ReachLocationHandler.class),

    REWARD_GROUP(RewardGroupHandler.class),

    SET_RESPAWN(SetRespawnHandler.class),

    START_NEW_PHASE(StartNewPhaseHandler.class),

    TELEPORT_GROUP(TeleportGroupHandler.class),

    PLACE_BLOCKS(PlaceBlocksHandler.class),

    PLAYER_SURVIVE(PlayerSurviveHandler.class),

    EXECUTE_COMMAND(ExecuteCommandHandler.class),

    PROTECT_PLAYER(ProtectRandomPlayerHandler.class),

    RANDOM(RandomActionHandler.class);

    private Class clazz;

    ActionType(Class clazz) {

        this.clazz = clazz;

    }

    public Class getHandler() {

        return clazz;

    }

}
