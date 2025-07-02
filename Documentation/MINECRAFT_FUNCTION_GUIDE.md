# UHC Function List

A list of all functions in the Java project with descriptions and their execution methods.

### **announce_iron_man**
  - Sends a message in chat that Iron Man is crowned
  - Execution Method: Single-use 
  - Parents: check_iron_man, victory
  - Children: -

### **battle_royale**
  - Spreads players for battle royale mode.
  - Execution Method: Single-use
  - Note: Not used in regular game
  - Parents: -
  - Children: -

### **bbvalue**
  - Update value of bossbar, and color of bossbar and glass block.
  - Execution Method: Continuous
  - Interval: 5 ticks
  - Parents: timer_control_point_5
  - Children: -

### **check_iron_man**
  - Regularly checks which players are still at full health.
  - Execution Method: Continuous
  - Interval: 20 ticks
  - Parents: timer_main_20
  - Children: announce_iron_man

### **clear_enderchest**
  - Clears all players' Ender Chests.
  - Execution Method: Single-use

### **clear_schedule**
  - Clears scheduled functions.
  - Execution Method: Single-use
  - Parents: developer_mode
  - Children: -

### control_point_i
  - Award players CP score. Keep glass, beacon active. Call Control Point messages
  - Execution Method: Continuous
  - Interval: 5 ticks
  - Parents: timer_control_point_5
  - Children: control_point_score_i, control_point_tag_i
  - Note: Computationally heavy

### **control_point_captured**
  - Announces that the Control Point has been captured.
  - Execution Method: Continuous
  - Interval: 5 ticks
  - Parents: timer_control_point_5
  - Children: teams_highscore_alive_check

### control_point_messages_i
  - Message logic and announcements for Control Point attacks and abandonment
  - Execution Method: Continuous
  - Interval: 20 ticks
  - Parents: timer_control_point_20
  - Children: -
  - Note: Computationally heavy

### **control_point_perks**
  - Grants perks for Control Point progress.
  - Execution Method: Continuous
  - Interval: 20 ticks
  - Parents: timer_control_point_20
  - Children: -

### **control_point_score_i**
- Add `cp score` to team player if they have the `OnCP i` tag. Unless a player from another team has the `OnCP i` tag.
- Add `cp score` to solo player if they have the `Capping i` tag.
- Parents: control_point_i
- Children: -

### **control_point_tag_i**
- Add `OnCP i` tag to players on respective CP.
- Remove `OnCP i` tag when players leave respective CP.
- Add `Capping i` tag when player without team gets `OnCP i` tag, unless other player has the `Capping i` tag.
- Remove `Capping i` tag when player with `OnCP i` tag exists, that does not have the `Capping i` tag.
- Parents: control_point_i
- Children: -

### **current_test_function**
  - Currently used for development testing.
  - Execution Method: Single-use

### **death_match**
  - Prepares world for deathmatch and spreads living players. 
  - Execution Method: Single-use

### **debug_give**
  - Give player access to debug messages. 
  - Execution Method: Single-use

### **debug_remove**
  - Revoke player access to debug messages. 
  - Execution Method: Single-use

### **developer_mode**
  - Toggles developer-specific settings or debug features.
  - Execution Method: Single-use
  - Parents: developer_potion_control
  - Children: timer_developer_5

### **developer_potion_control**
  - Turn potion effect into function execution.
  - Execution Method: Continuous
  - Interval: 5 ticks
  - Parents: timer_developer_5
  - Children: developer_mode, random_teams, predictions, into_calls, spread_players, survival_mode, start_game

### **disable_respawn**
  - Disable automatic respawn mechanic. 
  - Execution Method: Single-use

### **display_quotes**
  - Displays quotes during gameplay.
  - Execution Method: Continuous (self-scheduling)
  - Interval: 7 minutes
  - Parents: game_starter
  - Children: -

### **display_rank**
  - Displays player rankings in sidebar.
  - Execution Method: Single-use 

### **drop_carepackages**
  - Summon Care Packages as falling blocks.
  - Execution Method: Single-use
  - Parents: timer_main_1
  - Children: -

### **drop_player_heads**
  - Drops player heads upon death.
  - Execution Method: Continuous
  - Priority: Medium

### **equip_gear**
  - Equips players with basic iron armor and weapons. 
  - Execution Method: Single-use

### **game_starter**
  - Calls timer functions at the start of the game.
  - Execution Method: Single-use
  - Parents: start_game
  - Children: timer_main, display_quotes, messages_schedule_single

### **god_mode**
  - Enables invincibility for caller.
  - Execution Method: Single-use

### **handle_player_death**
  - Handles events and clean-up when a player dies.
  - Execution Method: Continuous
  - Interval: 5 ticks
  - Parents: timer_main_5
  - Children: drop_player_heads, respawn_player

### **horse_frost_walker**
  - Applies Frost Walker effect to horses.
  - Execution Method: Continuous
  - Interval: 1 tick
  - Parents: timer_main_1
  - Children: -

### **init**
  - Example.
  - Execution Method: Single-use

### **initialize**
  - Creates necessary scoreboard objectives, teams and start structures. 
  - Execution Method: Single-use

### **initialize_control_point**
  - Activates command blocks for Control Point functions. Sends activation message.
  - Execution Method: Single-use
  - Parents: timer_main_1
  - Children: timer_control_point

### **initiate_deathmatch**
  - Schedules deathmatch related functions.
  - Execution Method: Single-use

### **into_calls**
  - Teleport all players for the start of the game.
  - Execution Method: Single-use
  - Parents: developer_potion_control
  - Children: -

### **join_team**
  - Make custom team during the game.
  - Execution Method: Single-use
  - Parents: update_player_distance
  - Children: -

### **locate_teammate**
  - Create particle effect to find closest team mate while holding the team mate tracker.
  - Execution Method: Continuous
  - Interval: 1 tick
  - Parents: timer_main_1
  - Children: -

### **messages_pvp**
  - PVP disabled message
  - Execution Method: Single-use
  - Parents: messages_schedule_single
  - Children: -

### **messages_eternal_day**
  - Eternal day enabled message
  - Execution Method: Single-use
  - Parents: messages_schedule_single
  - Children: -

### **messages_schedule_single**
  - Combination function for all single send messages
  - Execution Method: Single-use
  - Parents: game_starter
  - Children: messages_pvp, messages_eternal_day

### **minute_1** and **minute_2**
  - Announces that there are 1, 2 minutes remaining, respectively. 
  - Execution Method: Single-use

### **predictions**
  - Teleport all players into the void for season predictions. 
  - Execution Method: Single-use
  - Parents: developer_potion_control
  - Children: predictions_loop

### **predictions_loop**
- Check which team comes out as winner in the predictions.
- Execution Method: Continuous, self-scheduling
- Interval: 1 tick
- Parents: predictions
- Children: -

### random_teamsi
  - Random team assignment logic for teams of 1-8.
  - Execution Method: Single-use
  - Parents: developer_potion_control
  - Children: -

### **remove_banned_items**
  - Strips items that are not allowed in gameplay.
  - Execution Method: Continuous
  - Interval: 5 ticks
  - Parents: timer_main_5
  - Children: -

### **respawn_player**
  - Respawns player, remove death items, give team mate tools, reset health.
  - Execution Method: Single-use

### **second_control_point**
  - Enables command block execution for CP2. Announces activation.
  - Execution Method: Continuous
  - Interval: 20 ticks
  - Parents: timer_control_point_20
  - Children: -

### **spawn_control_points**
  - Spawns all control points into the game world and modifies the terrain to enable the beacon beam.
  - Execution Method: Single-use

### **spread_players**
  - Spreads players/teams randomly across the map.
  - Execution Method: Single-use
  - Parents: developer_potion_control
  - Children: -

### **start_game**
  - Begins the match and all core timers.
  - Execution Method: Single-use
  - Parents: developer_potion_control
  - Children: game_starter

### **start_potions**
  - Give player potions to activate the start of game functions.
  - Execution Method: Single-use

### **survival_mode**
  - Set all gamerules to play mode.
  - Execution Method: Single-use
  - Parents: developer_potion_control
  - Children: -

### **team_score**
  - Updates and tracks team Control Point score.
  - Execution Method: Continuous
  - Interval: 5 ticks
  - Parents: timer_control_point_5
  - Children: -

### **teams_alive_check**
  - Checks if only a single team/player is alive.
  - Execution Method: Continuous
  - Interval: 5 ticks
  - Parents: traitor_check (Traitor Faction enabled), timer_main_20 (Traitor Faction disabled)
  - Children: victory_message_0 to victory_message_12, victory_message_solo

### **teams_highscore_alive_check**
  - Check if a team/player/traitor has captured the Control Point.
  - Execution Method: Continuous
  - Priority: Low
  - Parents: control_point_captured
  - Children: victory_message_0 to victory_message_12, victory_message_solo, victory_message_traitor

### **timer_control_point_5**
  - Timer for Control Point related functions with interval of 5 ticks
  - Execution Method: Continuous (self-scheduling)
  - Interval: 5 ticks
  - Parents: initialize_control_point
  - Children: bbvalue, control_point_1, control_point_2, team_score, control_point_captured

### **timer_control_point_20**
  - Timer for Control Point related functions with interval of 20 ticks
  - Execution Method: Continuous (self-scheduling)
  - Interval: 20 ticks
  - Parents: initialize_control_point
  - Children: control_point_messages_1, control_point_messages_2, control_point_perks, update_public_cp_score, second_control_point

### **timer_developer_20**
  - Timer for developer related functions with interval of 20 ticks
  - Execution Method: Continuous (self-scheduling)
  - Interval: 20 ticks
  - Parents: developer_mode
  - Children: developer_potion_control

### **timer_main_1**
  - Timer for main functions with interval of 1 tick
  - Execution Method: Continuous (self-scheduling)
  - Interval: 1 tick
  - Parents: game_starter
  - Children: drop_carepackages, initialize_control_point, traitor_handout

### **timer_main_5**
  - Timer for main functions with interval of 5 ticks
  - Execution Method: Continuous (self-scheduling)
  - Interval: 5 ticks
  - Parents: game_starter
  - Children: handle_player_death, horse_frost_walker, remove_banned_items, update_min_health

### **timer_main_20**
  - Timer for main functions with interval of 20 ticks
  - Execution Method: Continuous (self-scheduling)
  - Interval: 20 ticks
  - Parents: game_starter
  - Children: locate_teammate, check_iron_man, update_mine_count, update_sidebar, wolf_updates, teams_alive_check (Traitor Faction disabled)

### **timer_traitor_5**
  - Timer for Traitor Faction functions with interval of 5 ticks
  - Execution Method: Continuous (self-scheduling)
  - Interval: 5 ticks
  - Parents: traitor_handout
  - Children: traitor_check

### **timer_traitor_20**
  - Timer for Traitor Faction functions with interval of 20 ticks
  - Execution Method: Continuous (self-scheduling)
  - Interval: 20 ticks
  - Parents: traitor_handout
  - Children: traitor_actionbar

### **title_default_timing**
  - Sets how long title messages display to default values.
  - Execution Method: Single-use

### **traitor_actionbar**
  - Displays Traitor Faction to its members. Calls Traitor victory check. 
  - Execution Method: Continuous
  - Interval: 20 ticks
  - Parents: timer_traitor_20
  - Children: -

### **traitor_check**
  - Checks whether traitors and non-traitors are alive. 
  - Execution Method: Continuous
  - Interval: 5 ticks
  - Parents: timer_traitor_5
  - Children: teams_alive_check, victory_message_traitor

### **traitor_handout**
  - Assigns random eligible players to the Traitor Faction. 
  - Execution Method: Single-use
  - Parents: timer_main_1
  - Children: timer_traitor

### **update_min_health**
  - Updates the current minimum health of all players.
  - Execution Method: Continuous
  - Interval: 5 ticks
  - Parents: timer_main_5
  - Children: -

### **update_mine_count**
  - Tracks how many stone-like blocks have been mined.
  - Execution Method: Continuous
  - Interval: 20 ticks
  - Parents: timer_main_20
  - Children: -

### **update_player_distance**
  - Updates how far players are from a player trying to form a team. 
  - Execution Method: Continuous
  - Interval: 5 ticks
  - Parents: timer_main_5
  - Children: join_team

### **update_public_cp_score**
  - Displays CP scores publicly.
  - Execution Method: Continuous
  - Interval: 20 ticks
  - Parents: timer_control_point_20
  - Children: -

### **update_sidebar**
  - Updates sidebar scoreboard elements.
  - Execution Method: Continuous
  - Interval: 20 ticks
  - Parents: timer_main_20
  - Children: -

### **victory**
  - Triggers end-game victory logic.
  - Execution Method: Single-use

### **victory_message_0** to **victory_message_12**
  - Variants of different victory message displays.
  - Execution Method: Single-use

### **victory_message_solo**
  - Custom message for solo winner.
  - Execution Method: Single-use

### **victory_message_traitor**
  - Displays win message if Traitor Faction wins.
  - Execution Method: Single-use

### **wolf_updates**
  - Update wolf parameters, such as collar color, base health, and baby elimination 
  - Execution Method: Continuous
  - Interval: 20 ticks
  - Parents: timer_main_20
  - Children: -