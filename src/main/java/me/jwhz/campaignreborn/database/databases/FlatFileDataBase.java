package me.jwhz.campaignreborn.database.databases;

import me.jwhz.campaignreborn.config.ConfigFile;
import me.jwhz.campaignreborn.database.IDatabase;

public class FlatFileDataBase extends ConfigFile implements IDatabase {

    public FlatFileDataBase(String fileName) {

        super(fileName);

    }

    @Override
    public void store(String value, Object key) {

        getYamlConfiguration().set(value, key);
        save();

    }

    @Override
    public Object retrieve(String value) {

        return getYamlConfiguration().get(value);

    }

    @Override
    public boolean isSet(String value) {

        return getYamlConfiguration().isSet(value);

    }

}
