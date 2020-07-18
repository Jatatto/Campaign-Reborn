package me.jwhz.campaignreborn.manager;

import me.jwhz.campaignreborn.CampaignReborn;
import me.jwhz.campaignreborn.config.ConfigFile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

public abstract class Manager<T extends ManagerObject> extends ConfigFile {

    protected ArrayList<T> list = new ArrayList<T>();

    public CampaignReborn core = CampaignReborn.getInstance();

    public Manager(String fileName) {
        super(fileName);
    }

    public int getIndex(T t) {

        return IntStream.range(0, list.size()).filter(i -> list.get(i).equals(t)).findFirst().orElse(-1);

    }

    public boolean contains(Object identifier) {

        try {
            for (T t : list)
                if (t.getClass().getMethod("getIndentifier").invoke(t).equals(identifier))
                    return true;
        } catch (Exception ignored) {
        }

        return false;

    }

    public boolean remove(Object identifier) {

        boolean removed = false;

        Iterator<T> iterator = list.iterator();

        while (iterator.hasNext())
            try {

                T next = iterator.next();

                if (next.getClass().getMethod("getIdentifier").invoke(next).equals(identifier)) {

                    iterator.remove();
                    removed = true;
                    break;

                }
            } catch (Exception ignored) {
            }

        return removed;

    }

    public T get(Object identifier) {

        for (T t : list)
            try {
                if (t.getClass().getMethod("getIdentifier").invoke(t).equals(identifier))
                    return t;
            } catch (Exception ignored) {
            }

        return null;

    }

    public List<T> getList() {

        return list;

    }

    public void add(T t) {

        getList().add(t);

    }

}