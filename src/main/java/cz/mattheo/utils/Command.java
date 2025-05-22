package cz.mattheo.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public abstract class Command extends BukkitCommand {

    public Command(String command, String[] aliases, String description) {
        super(command);

        this.setAliases(Arrays.asList(aliases));
        this.setDescription(description);

        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) field.get(Bukkit.getServer());
            commandMap.register(command, this);
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.WARNING, e.getMessage());
        }

    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, String @NotNull [] args) {
        try {
            execute(commandSender, args);
        } catch (ArrayIndexOutOfBoundsException e) {
            Bukkit.getLogger().log(Level.WARNING, e.getMessage());
        }
        return false;
    }

    public abstract void execute(CommandSender sender, String[] args);

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String @NotNull [] args) throws IllegalArgumentException {
        return onTabComplete(sender, args);
    }

    public abstract List<String> onTabComplete(CommandSender sender, String[] args);


}

