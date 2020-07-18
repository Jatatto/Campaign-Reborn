package me.jwhz.campaignreborn.database;

public interface IDatabase {

    void store(String value, Object key);

    Object retrieve( String value);

    boolean isSet(String value);

}
