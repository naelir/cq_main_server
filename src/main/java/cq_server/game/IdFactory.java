package cq_server.game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class IdFactory {
	private static final int PLAYER_DEFAULT_STARTID = 1;

	private static final int COMMON_CLASSES_DEFAULT_STARTID = 0;

	private final Map<Class<?>, AtomicInteger> idCache;

	public IdFactory() {
		this.idCache = new ConcurrentHashMap<>();
	}

	public int createId(final Class<?> clazz) {
		if (this.idCache.containsKey(clazz))
			return this.idCache.get(clazz).incrementAndGet();
		else {
			if (clazz.equals(BasePlayer.class))
				this.idCache.put(clazz, new AtomicInteger(PLAYER_DEFAULT_STARTID));
			else
				this.idCache.put(clazz, new AtomicInteger(COMMON_CLASSES_DEFAULT_STARTID));
			return this.idCache.get(clazz).get();
		}
	}
}
