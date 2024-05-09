/*
 * Distributor, a feature-rich framework for Mindustry plugins.
 *
 * Copyright (C) 2024 Xpdustry
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
package com.xpdustry.distributor.api.window;

import java.util.List;

public interface Action {

    static Action of(final Action... actions) {
        final var list = List.of(actions);
        return (context) -> {
            for (final var action : list) {
                action.act(context);
            }
        };
    }

    static Action none() {
        return context -> {};
    }

    static <T> Action with(final State.Key<T> key, final T value) {
        return context -> context.getState().put(key, value);
    }

    static Action without(final State.Key<?> key) {
        return context -> context.getState().remove(key);
    }

    static Action back() {
        return back(1);
    }

    static Action back(final int depth) {
        return context -> {
            var current = context;
            var i = depth;
            while (current != null && i-- > 0) {
                current.close();
                current = current.getParent().orElse(null);
            }
            if (current != null) {
                current.open();
            }
        };
    }

    static Action closeAll() {
        return context -> {
            var current = context;
            while (current != null) {
                current.close();
                current = current.getParent().orElse(null);
            }
        };
    }

    void act(final Window.Context context);
}
