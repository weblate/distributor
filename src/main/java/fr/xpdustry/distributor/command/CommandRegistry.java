package fr.xpdustry.distributor.command;

import arc.util.Nullable;
import arc.util.*;

import mindustry.gen.*;

import fr.xpdustry.distributor.command.LambdaCommand.*;
import fr.xpdustry.xcommand.*;

import org.jetbrains.annotations.*;

import java.util.*;


/**
 * Utility class to keep track of commands for a given plugin.
 */
public class CommandRegistry{
    private @NotNull CommandWrapperFactory wrapperFactory = CommandWrapper::new;
    private final SortedMap<String, Command<Playerc>> commands = new TreeMap<>();

    public Command<Playerc> register(@NotNull Command<Playerc> command){
        commands.put(command.getName(), command);
        return command;
    }

    public @NotNull Command<Playerc> register(@NotNull LambdaCommandBuilder<Playerc> builder){
        return register(builder.build());
    }

    public @NotNull LambdaCommandBuilder<Playerc> builder(@NotNull String name){
        return LambdaCommand.of(name, Commands.PLAYER_TYPE);
    }

    public @Nullable Command<Playerc> getCommand(@NotNull String name){
        return commands.get(name);
    }

    public SortedMap<String, Command<Playerc>> getCommands(){
        return new TreeMap<>(commands);
    }

    public void setWrapperFactory(@NotNull CommandWrapperFactory wrapperFactory){
        this.wrapperFactory = wrapperFactory;
    }

    public void export(@NotNull CommandHandler handler){
        for(var command : commands.values()){
            handler.register(command.getName(), Commands.getParameterText(command),
                command.getDescription(), wrapperFactory.wrapCommand(command));
        }
    }

    public void dispose(@NotNull CommandHandler handler){
        for(var command : commands.values()){
            handler.removeCommand(command.getName());
        }
    }

    public interface CommandWrapperFactory{
        CommandWrapper wrapCommand(@NotNull Command<Playerc> command);
    }
}
