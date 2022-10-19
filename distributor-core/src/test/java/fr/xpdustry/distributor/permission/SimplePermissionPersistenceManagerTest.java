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

import fr.xpdustry.distributor.util.*;
import java.io.*;
import java.nio.file.*;
import java.util.function.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.*;
import org.spongepowered.configurate.*;
import org.spongepowered.configurate.yaml.*;

public final class SimplePermissionPersistenceManagerTest {

  private static final String PLAYER1 = "Anuke";

  private static final String GROUP1 = "group1";
  private static final String GROUP2 = "group2";

  private static final String PERMISSION1 = "fr.xpdustry.test.a";
  private static final String PERMISSION2 = "fr.xpdustry.test.b";


  @TempDir
  private Path tempDir;

  private SimplePermissionManager manager;

  @BeforeEach
  void setup() {
    manager = new SimplePermissionManager(tempDir);
    manager.setVerifyAdmin(false);
  }

  @Test
  void test_player_save() throws ConfigurateException {
    final var players = manager.getPlayerPermissionManager();
    final var player = players.findOrCreateById(PLAYER1);

    player.setPermission(PERMISSION1, Tristate.TRUE);
    player.setPermission(PERMISSION2, Tristate.FALSE);

    Assertions.assertEquals(0, players.count());
    players.save(player);
    Assertions.assertEquals(1, players.count());

    final var root = YamlConfigurationLoader.builder().path(tempDir.resolve("players.yaml")).build().load();
    Assertions.assertTrue(root.node(PLAYER1, "permissions", PERMISSION1).getBoolean());
    Assertions.assertFalse(root.node(PLAYER1, "permissions", PERMISSION2).getBoolean());
  }

  @Test
  void test_group_save() throws ConfigurateException {
    final var groups = manager.getGroupPermissionManager();
    final var group = groups.findOrCreateById(GROUP1);

    group.setPermission("fr.xpdustry.test.a", Tristate.TRUE);
    group.setPermission("fr.xpdustry.test.b", Tristate.FALSE);
    group.setWeight(6);

    Assertions.assertEquals(0, groups.count());
    groups.save(group);
    Assertions.assertEquals(1, groups.count());

    final var root = YamlConfigurationLoader.builder().path(tempDir.resolve("groups.yaml")).build().load();
    Assertions.assertTrue(root.node(GROUP1, "permissions", PERMISSION1).getBoolean());
    Assertions.assertFalse(root.node(GROUP1, "permissions", PERMISSION2).getBoolean());
    Assertions.assertEquals(6, root.node(GROUP1, "weight").getInt());
  }

  @Test
  void test_permission_calculation_player() {
    setupPlayer(PLAYER1, player -> player.setPermission(PERMISSION1, Tristate.TRUE));
    Assertions.assertTrue(manager.hasPermission(PLAYER1, PERMISSION1));
  }

  @Test
  void test_permission_calculation_group() {
    setupPlayer(PLAYER1, player -> player.addParent(GROUP1));
    setupGroup(GROUP1, group -> group.setPermission(PERMISSION1, Tristate.FALSE));
    Assertions.assertFalse(manager.hasPermission(PLAYER1, PERMISSION1));
  }

  @Test
  void test_permission_calculation_group_with_weight() {
    setupPlayer(PLAYER1, player -> {
      player.addParent(GROUP1);
      player.addParent(GROUP2);
    });
    setupGroup(GROUP1, group -> {
      group.setPermission(PERMISSION1, Tristate.FALSE);
      group.setWeight(10);
    });
    setupGroup(GROUP2, group -> {
      group.setPermission(PERMISSION1, Tristate.TRUE);
      group.setWeight(20);
    });
    Assertions.assertTrue(manager.hasPermission(PLAYER1, PERMISSION1));
  }

  @Test
  void test_default_group() {
    setupGroup(manager.getPrimaryGroup(), group -> group.setPermission(PERMISSION2, Tristate.TRUE));
    Assertions.assertTrue(manager.hasPermission(PLAYER1, PERMISSION2));
  }

  private void setupPlayer(final String uuid, final Consumer<PlayerPermissible> setup) {
    final var players = manager.getPlayerPermissionManager();
    final var player = players.findOrCreateById(uuid);
    setup.accept(player);
    players.save(player);
  }

  private void setupGroup(final String name, final Consumer<GroupPermissible> setup) {
    final var groups = manager.getGroupPermissionManager();
    final var group = groups.findOrCreateById(name);
    setup.accept(group);
    groups.save(group);
  }
}
