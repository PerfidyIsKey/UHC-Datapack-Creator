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

- **horse_frost_walker**
  - Applies Frost Walker effect to horses.
  - Execution Method: Continuous
  - Priority: Medium

- **init**
  - Example.
  - Execution Method: Single-use

- **initialize**
  - Creates necessary scoreboard objectives, teams and start structures. 
  - Execution Method: Single-use

- **initialize_controlpoint**
  - Activates command blocks for Control Point functions. Sends activation message.
  - Execution Method: Single-use

- **initiate_deathmatch**
  - Schedules deathmatch related functions.
  - Execution Method: Single-use

- **into_calls**
  - Teleport all players for the start of the game.
  - Execution Method: Single-use

- **join_team**
  - Make custom team during the game.
  - Execution Method: Single-use

- **locate_teammate**
  - Create particle effect to find closest team mate while holding the team mate tracker.
  - Execution Method: Continuous
  - Priority: Medium

- **main**
  - Example.
  - Execution Method: Continuous
  - Priority: High

- **minute_1** and **minute_2**
  - Announces that there are 1, 2 minutes remaining, respectively. 
  - Execution Method: Single-use

- **predictions**
  - Teleport all players into the void for season predictions. 
  - Execution Method: Single-use

- **random_teams1** to **random_teams8**
  - Random team assignment logic for teams of 1-8.
  - Execution Method: Single-use

- **remove_banned_items**
  - Strips items that are not allowed in gameplay.
  - Execution Method: Continuous
  - Priority: Medium

- **respawn_player**
  - Respawns player, remove death items, give team mate tools, reset health.
  - Execution Method: Single-use

- **second_controlpoint**
  - Enables command block execution for CP2. Announces activation.
  - Execution Method: Single-use

- **spawn_controlpoints**
  - Spawns all control points into the game world and modifies the terrain to enable the beacon beam.
  - Execution Method: Single-use

- **spread_players**
  - Spreads players/teams randomly across the map.
  - Execution Method: Single-use

- **start_game**
  - Begins the match and all core timers.
  - Execution Method: Single-use

- **start_potions**
  - Give player potions to activate the start of game functions.
  - Execution Method: Single-use

- **survival_mode**
  - Set all gamerules to play mode.
  - Execution Method: Single-use

- **team_score**
  - Updates and tracks team CP score. Calls CP perks function. 
  - Execution Method: Continuous
  - Priority: Medium

- **teams_alive_check**
  - Checks if only a single team/player is alive.
  - Execution Method: Continuous
  - Priority: Low

- **teams_highscore_alive_check**
  - Check if a team/player/traitor has captured the Control Point.
  - Execution Method: Continuous
  - Priority: Low

- **timer**
  - Main game timer logic.
  - Execution Method: Continuous
  - Priority: High

- **title_default_timing**
  - Sets how long title messages display to default values.
  - Execution Method: Single-use

- **traitor_actionbar**
  - Displays Traitor Faction to its members. Calls Traitor victory check. 
  - Execution Method: Continuous
  - Priority: Low

- **traitor_check**
  - Checks whether traitors and non-traitors are alive. 
  - Execution Method: Continuous
  - Priority: Medium

- **traitor_handout**
  - Assigns random eligible players to the Traitor Faction. 
  - Execution Method: Single-use

- **update_min_health**
  - Updates the current minimum health of all players.
  - Execution Method: Continuous
  - Priority: Medium

- **update_mine_count**
  - Tracks how many stone-like blocks have been mined.
  - Execution Method: Continuous
  - Priority: Low

- **update_player_distance**
  - Updates how far players are from a player trying to form a team. Calls function if all conditions are met, else send fitting refusal message. 
  - Execution Method: Continuous
  - Priority: Low

- **update_public_cp_score**
  - Displays CP scores publicly.
  - Execution Method: Continuous
  - Priority: Low

- **update_sidebar**
  - Updates sidebar scoreboard elements.
  - Execution Method: Continuous
  - Priority: Low

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
  - Displays win message if Traitor Faction wins.
  - Execution Method: Single-use

- **wolf_collar_execute**
  - Change wolf collar color to match team color of its owner. 
  - Execution Method: Continuous
  - Priority: Low

- **world_pre_load**
  - Pre-loads overworld and nether terrain.
  - Execution Method: Continuous
  - Priority: Low

- **world_pre_load_activation**
  - Activate command block for world pre-loading
  - Execution Method: Single-use
