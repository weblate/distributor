/*
 * Distributor, a feature-rich framework for Mindustry plugins.
 *
 * Copyright (C) 2022 Xpdustry
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package fr.xpdustry.distributor.api.plugin;

import arc.util.CommandHandler;
import java.nio.file.Path;
import java.util.List;
import mindustry.Vars;
import mindustry.mod.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A better plugin base class. With better methods, SLF4J support, plugin listeners, etc.
 */
public interface MindustryPlugin {

    /**
     * Wraps a regular plugin into a {@code MindustryPlugin}.
     *
     * @param plugin the plugin to wrap
     * @return the wrapped plugin
     */
    static MindustryPlugin wrap(final Plugin plugin) {
        return new WrappingMindustryPlugin(plugin);
    }

    /**
     * Called after the plugin instance creation.
     * Initialize your plugin here (initializing the fields, registering the listeners, etc.).
     */
    default void onInit() {}

    /**
     * Called after {@link #onInit()}.
     * Register your client-side commands here.
     *
     * @param handler the client command handler
     */
    default void onClientCommandsRegistration(final CommandHandler handler) {}

    /**
     * Called after {@link #onClientCommandsRegistration(CommandHandler)}.
     * Register your server-side commands here.
     *
     * @param handler the server command handler
     */
    default void onServerCommandsRegistration(final CommandHandler handler) {}

    /**
     * Called every tick while the server is running.
     */
    default void onUpdate() {}

    /**
     * Called when the server is closing.
     * Unload your plugin here (closing the database connection, saving files, etc.).
     */
    default void onExit() {}

    /**
     * Returns the plugin data directory. {@code ./config/mods/[plugin-name]/} by default.
     */
    default Path getDirectory() {
        return Vars.modDirectory.child(this.getDescriptor().getName()).file().toPath();
    }

    /**
     * Returns the logger bound to this plugin.
     */
    default Logger getLogger() {
        return LoggerFactory.getLogger(this.getDescriptor().getName());
    }

    /**
     * Returns the descriptor of this plugin.
     */
    PluginDescriptor getDescriptor();

    /**
     * Returns an unmodifiable list of the listeners registered to this plugin.
     */
    List<PluginListener> getListeners();

    /**
     * Adds a {@link PluginListener} to this plugin.
     *
     * @param listener the listener to add
     */
    void addListener(final PluginListener listener);
}
