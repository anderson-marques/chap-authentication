package me.marques.anderson;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface EntitiesRepository {

    public Entity entity(final String username);

}
