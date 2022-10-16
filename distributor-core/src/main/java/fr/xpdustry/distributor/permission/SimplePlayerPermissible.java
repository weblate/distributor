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
package fr.xpdustry.distributor.permission;

import java.util.*;
import mindustry.*;
import org.jetbrains.annotations.*;

final class SimplePlayerPermissible extends AbstractPermissionHolder implements PermissionPlayer {

  private final String uuid;

  SimplePlayerPermissible(final String uuid) {
    this.uuid = uuid;
  }

  @Override
  public @NotNull String getName() {
    if (Vars.netServer != null) {
      final var info = Vars.netServer.admins.getInfoOptional(uuid);
      return info == null ? "unknown" : info.lastName;
    }
    return "unknown";
  }

  @Override
  public @NotNull String getUuid() {
    return uuid;
  }

  // TODO Implement Equals + Hashcode
}
