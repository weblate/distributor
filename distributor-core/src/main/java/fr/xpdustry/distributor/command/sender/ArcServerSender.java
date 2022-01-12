package fr.xpdustry.distributor.command.sender;

import arc.struct.*;
import arc.util.*;

import mindustry.gen.*;

import fr.xpdustry.distributor.string.*;

import cloud.commandframework.captions.*;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.*;

import java.util.*;
import java.util.regex.*;


public class ArcServerSender extends ArcCommandSender{
    public ArcServerSender(@NonNull CaptionRegistry<ArcCommandSender> captions, @NonNull MessageFormatter formatter){
        super(captions, formatter);
    }

    public ArcServerSender(@NonNull CaptionRegistry<ArcCommandSender> captions){
        this(captions, new ServerMessageFormatter());
    }

    @Override public void send(@NonNull MessageIntent intent, @NonNull String message){
        switch(intent){
            case DEBUG -> Log.debug(message);
            case ERROR -> Log.err(message);
            default -> Log.info(message);
        }
    }

    @Override public boolean isPlayer(){
        return false;
    }

    @Override public @NonNull Playerc asPlayer(){
        throw new UnsupportedOperationException("Cannot convert console to player");
    }

    @Override public @NonNull Locale getLocale(){
        return Locale.getDefault();
    }

    @Override public boolean hasPermission(@NonNull String permission){
        return true;
    }

    public static class ServerMessageFormatter implements MessageFormatter{
        private static final Pattern CAPTION_VARIABLE_PATTERN = Pattern.compile("(\\{[\\w\\-]+})");

        @Override public @NonNull String format(@NonNull MessageIntent intent, @NonNull String message){
            return message;
        }

        @Override public @NonNull String format(@NonNull MessageIntent intent, @NonNull String message, @Nullable Object... args){
            return Strings.format(message.replace("@", "&fb&lb@&fr"), args);
        }

        @Override public @NonNull String format(@NonNull MessageIntent intent, @NonNull String message, @NonNull CaptionVariable... vars){
            final var map = Seq.with(vars).asMap(e -> "{" + e.getKey() + "}", CaptionVariable::getValue);
            final var builder = new StringBuilder();
            final var matcher = CAPTION_VARIABLE_PATTERN.matcher(message);
            while(matcher.find()) matcher.appendReplacement(builder, "&fb&lb" + map.get(matcher.group(), "???") + "&fr");
            matcher.appendTail(builder);
            return format(intent, builder.toString());
        }
    }
}
