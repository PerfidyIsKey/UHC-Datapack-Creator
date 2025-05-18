# UHC Function List

A list of all functions in the Java project with descriptions and their execution methods.

- **announce_iron_man**
  - Sends a message in chat that Iron Man is crowned
  - Execution Method: Single-use 

- **battle_royale**
  - Spreads players for battle royale mode.
  - Execution Method: Single-use
  - Note: Not used in regular game

- **bbvalue**
  - Update value of bossbar, and color of bossbar and glass block.
  - Execution Method: Continuous
  - Priority: Medium

- **carepackage_distributor**
  - Spreads Care Packages that have been spawned as falling blocks.
  - Execution Method: Continuous
  - Priority: Low

- **check_iron_man**
  - Regularly checks which players are still at full health.
  - Execution Method: Continuous
  - Priority: Low

- **clear_enderchest**
  - Clears all players' Ender Chests.
  - Execution Method: Single-use

- **clear_schedule**
  - Clears scheduled functions.
  - Execution Method: Single-use

- **control_point_captured**
  - Announces that the Control Point has been captured.
  - Execution Method: Single-use

- **controlpoint_1** and **controlpoint_2**
  - Award players CP score. Keep glass, beacon active. Call Control Point messages
  - Execution Method: Continuous
  - Priority: Medium
  - Note: Computationally heavy

- **controlpoint_messages_1** and **controlpoint_messages_2**
  - Message logic and announcements for Control Point attacks and abandonment
  - Execution Method: Continuous
  - Priority: Low
  - Note: Computationally heavy

- **controlpoint_perks**
  - Grants perks for Control Point progress.
  - Execution Method: Continuous
  - Priority: Low

- **current_test_function**
  - Currently used for development testing.
  - Execution Method: Single-use

- **death_match**
  - Prepares world for deathmatch and spreads living players. 
  - Execution Method: Single-use

- **debug_give**
  - Give player access to debug messages. 
  - Execution Method: Single-use

- **debug_remove**
  - Revoke player access to debug messages. 
  - Execution Method: Single-use

- **developer_mode**
  - Toggles developer-specific settings or debug features.
  - Execution Method: Single-use

- **disable_respawn**
  - Disable automatic respawn mechanic. 
  - Execution Method: Single-use

- **display_quotes**
  - Displays quotes during gameplay.
  - Execution Method: Continuous
  - Priority: Low

- **display_rank**
  - Displays player rankings in sidebar.
  - Execution Method: Single-use 

- **drop_carepackages**
  - Summon Care Packages as falling blocks.
  - Execution Method: Single-use

- **drop_player_heads**
  - Drops player heads upon death.
  - Execution Method: Continuous
  - Priority: Medium

- **eliminate_baby_wolf**
  - Kill baby wolves and summon a dolphin
  - Execution Method: Continuous
  - Priority: Low

- **equip_gear**
  - Equips players with basic iron armor and weapons. 
  - Execution Method: Single-use

- **god_mode**
  - Enables invincibility for caller.
  - Execution Method: Single-use

- **handle_player_death**
  - Handles events and clean-up when a player dies.
  - Execution Method: Continuous
  - Priority: Medium

- [ ] TODO

- **horse_frost_walker**
  - Applies Frost Walker effect to horses.
  - Execution Method: Continuous (Low priority)

- **init**
  - Initializes the plugin or game environment.
  - Execution Method: Single-use

- **initialize**
  - Sets up main configuration or game state.
  - Execution Method: Single-use

- **initialize_controlpoint**
  - Sets up control point locations and properties.
  - Execution Method: Single-use

- **initiate_deathmatch**
  - Prepares players and arena for the deathmatch.
  - Execution Method: Single-use

- **into_calls**
  - Possibly routes command calls to the correct subfunction.
  - Execution Method: Continuous (Medium priority)

- **join_team**
  - Adds players to a team.
  - Execution Method: Single-use

- **locate_teammate**
  - Lets players find teammates via coordinates or tracking.
  - Execution Method: Continuous (Medium priority)

- **main**
  - Main game loop or entry point for plugin.
  - Execution Method: Continuous (Very High priority)

- **minute_1**
  - Triggers first-minute timed events.
  - Execution Method: Single-use

- **minute_2**
  - Triggers second-minute timed events.
  - Execution Method: Single-use

- **predictions**
  - Manages voting or prediction mechanics.
  - Execution Method: Single-use

- **random_teams1** to **random_teams8**
  - Variants of random team assignment logic.
  - Execution Method: Single-use

- **remove_banned_items**
  - Strips items that are not allowed in gameplay.
  - Execution Method: Continuous (High priority)

- **respawn_player**
  - Respawns player under certain conditions (if allowed).
  - Execution Method: Single-use

- **second_controlpoint**
  - Initializes or manages the second phase of control points.
  - Execution Method: Single-use

- **spawn_controlpoints**
  - Spawns all control points into the game world.
  - Execution Method: Single-use

- **spread_players**
  - Spreads players randomly across the map at game start.
  - Execution Method: Single-use

- **start_game**
  - Begins the match and all core timers.
  - Execution Method: Single-use

- **start_potions**
  - Activates potion effects if enabled.
  - Execution Method: Single-use

- **survival_mode**
  - Switches all players to survival mode.
  - Execution Method: Single-use

- **team_score**
  - Updates and tracks team score based on events.
  - Execution Method: Continuous (Medium priority)

- **teams_alive_check**
  - Checks how many teams are still alive.
  - Execution Method: Continuous (High priority)

- **teams_highscore_alive_check**
  - Tracks top-performing teams still alive.
  - Execution Method: Continuous (Medium priority)

- **timer**
  - Main game timer logic.
  - Execution Method: Continuous (Very High priority)

- **title_default_timing**
  - Sets how long title messages display.
  - Execution Method: Single-use

- **traitor_actionbar**
  - Displays role info to traitors in the actionbar.
  - Execution Method: Continuous (Medium priority)

- **traitor_check**
  - Checks which players are traitors.
  - Execution Method: Continuous (High priority)

- **traitor_handout**
  - Assigns roles and abilities to traitors.
  - Execution Method: Single-use

- **update_min_health**
  - Adjusts the minimum health cap for players.
  - Execution Method: Continuous (Medium priority)

- **update_mine_count**
  - Tracks how many ores have been mined.
  - Execution Method: Continuous (Low priority)

- **update_player_distance**
  - Updates how far players are from each other.
  - Execution Method: Continuous (Medium priority)

- **update_public_cp_score**
  - Displays control point scores publicly.
  - Execution Method: Continuous (Medium priority)

- **update_sidebar**
  - Updates sidebar HUD elements.
  - Execution Method: Continuous (Medium priority)

- **victory**
  - Triggers end-game victory logic.
  - Execution Method: Single-use

- **victory_message_0** to **victory_message_12**
  - Variants of different victory message displays.
  - Execution Method: Single-use

- **victory_message_solo**
  - Custom message for solo winner.
  - Execution Method: Single-use

- **victory_message_traitor**
  - Displays win message if traitor wins.
  - Execution Method: Single-use

- **wolf_collar_execute**
  - Changes or reacts to wolf collar settings.
  - Execution Method: Continuous (Low priority)

- **world_pre_load**
  - Pre-loads world elements before players join.
  - Execution Method: Single-use

- **world_pre_load_activation**
  - Activates specific systems after pre-load.
  - Execution Method: Single-use
