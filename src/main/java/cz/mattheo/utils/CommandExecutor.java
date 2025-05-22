package cz.mattheo.utils;

import cz.mattheo.Main;
import org.bukkit.Bukkit;
import org.reflections.Reflections;

import java.util.Set;

public class CommandExecutor {

    private final Main plugin;

    public CommandExecutor() {
        this.plugin = Main.getInstance();
    }

    public void autoRegister() {
        Reflections reflections = new Reflections("cz.mattheo");
        Set<Class<?>> commandClasses = reflections.getTypesAnnotatedWith(Executor.class);

        for (Class<?> clazz : commandClasses) {
            if (!Command.class.isAssignableFrom(clazz)) {
                Bukkit.getLogger().warning("ERROR:" + clazz.getName() + " není podtřídou Command!");
                continue;
            }

            try {
                clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                Bukkit.getLogger().severe("ERROR: Chyba při registraci příkazu: " + clazz.getName());
                e.printStackTrace();
            }
        }
    }
}
