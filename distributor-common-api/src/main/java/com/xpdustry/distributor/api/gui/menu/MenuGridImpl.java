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
package com.xpdustry.distributor.api.gui.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import org.checkerframework.checker.nullness.qual.Nullable;

final class MenuGridImpl implements MenuGrid {

    private final List<List<MenuOption>> options = new ArrayList<>();

    @Override
    public List<List<MenuOption>> getOptions() {
        return options.stream().map(List::copyOf).toList();
    }

    @Override
    public List<MenuOption> getRow(int index) {
        return options.get(index);
    }

    @Override
    public MenuGrid setRow(int index, List<MenuOption> options) {
        this.options.set(index, new ArrayList<>(options));
        return this;
    }

    @Override
    public MenuGrid addRow(List<MenuOption> options) {
        this.options.add(new ArrayList<>(options));
        return this;
    }

    @Override
    public MenuGrid addRow(int index, List<MenuOption> options) {
        this.options.add(index, new ArrayList<>(options));
        return this;
    }

    @Override
    public MenuGrid removeRow(int index) {
        options.remove(index);
        return this;
    }

    @Override
    public @Nullable MenuOption getOption(final int id) {
        var i = 0;
        for (var row : options) {
            i += row.size();
            if (i > id) {
                return row.get(id - i + row.size());
            }
        }
        return null;
    }

    @Override
    public MenuOption getOption(final int x, final int y) {
        return options.get(y).get(x);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(options);
    }

    @Override
    public boolean equals(final Object o) {
        return (this == o) || (o instanceof MenuGridImpl other && Objects.equals(options, other.options));
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MenuGridImpl.class.getSimpleName() + "{", "}")
                .add("options=" + options)
                .toString();
    }
}
