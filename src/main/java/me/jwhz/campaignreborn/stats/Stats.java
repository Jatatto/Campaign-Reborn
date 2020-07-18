package me.jwhz.campaignreborn.stats;

import me.jwhz.campaignreborn.database.IDatabase;

public class Stats {

    private IDatabase iDatabase;

    public Stats(IDatabase iDatabase) {

        this.iDatabase = iDatabase;

    }

    public void setStat(String value, Object key) {

        iDatabase.store(value, key);

    }

    public Object getStat(String value) {

        return isSet(value) ?
                iDatabase.retrieve(value) :
                null;

    }

    public Object getStat(String value, Object defaultValue) {

        return isSet(value) ?
                iDatabase.retrieve(value) :
                defaultValue;

    }

    public boolean isSet(String value) {

        return iDatabase.isSet(value);

    }

}
