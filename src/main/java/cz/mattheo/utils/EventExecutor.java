package cz.mattheo.utils;

import cz.mattheo.Main;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.reflections.Reflections;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EventExecutor {

    private final Map<String, Object> events;
    private final Main plugin;

    public EventExecutor() {
        this.events = new HashMap<>();
        this.plugin = Main.getInstance();
    }

    public void autoEventRegister() {
        Reflections reflections = new Reflections("cz.mattheo");
        Set<Class<?>> eventClasses = reflections.getTypesAnnotatedWith(Event.class);

        for (Class<?> clazz : eventClasses) {
            try {
                Object listenerInstance = clazz.getDeclaredConstructor().newInstance();
                register(listenerInstance);
            } catch (Exception e) {
                plugin.getLogger().warning("ERROR: Chyba při registraci event listeneru: " + clazz.getName());
                e.printStackTrace();
            }
        }
    }

    public void register(Object listenerInstance) {
        if (!(listenerInstance instanceof Listener)) {
            plugin.getLogger().warning("WARN: "+listenerInstance.getClass().getSimpleName() + " není Listener!");
            return;
        }

        Class<?> moduleClass = listenerInstance.getClass();
        String moduleName = moduleClass.getSimpleName();
        events.put(moduleName, listenerInstance);

        Bukkit.getPluginManager().registerEvents((Listener) listenerInstance, plugin);
    }

    @SuppressWarnings("unchecked")
    public <T> T getEvent(Class<T> moduleClass) {
        return (T) events.get(moduleClass.getSimpleName());
    }

    @Deprecated
    public Object getEvent(String moduleName) {
        return events.get(moduleName);
    }
}
