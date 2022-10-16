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
package fr.xpdustry.distributor.command;

import cloud.commandframework.arguments.parser.*;
import fr.xpdustry.distributor.command.argument.TeamArgument.*;
import io.leangen.geantyref.*;

public final class ArcParserParameters {

  public static final ParserParameter<TeamMode> TEAM_MODE = new ParserParameter<>("team_mode", TypeToken.get(TeamMode.class));

  private ArcParserParameters() {
  }
}
