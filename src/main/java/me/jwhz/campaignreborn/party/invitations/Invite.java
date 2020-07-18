package me.jwhz.campaignreborn.party.invitations;

import java.util.UUID;

public class Invite {

    private UUID inviter, invitee;
    private long expires;

    public Invite(UUID inviter, UUID invitee) {

        this.inviter = inviter;
        this.invitee = invitee;

        this.expires = System.currentTimeMillis() + (120 * 1000);

    }

    public boolean isExpired(){

        return System.currentTimeMillis() > expires;

    }

    public UUID getInviter() {

        return inviter;

    }

    public UUID getInvitee() {

        return invitee;

    }

}
