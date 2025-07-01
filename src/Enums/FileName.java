package Enums;

public enum FileName {
    // Start up world
    initialize,

    // Start up game
    developer_mode,
    random_teams,
    predictions,
    predictions_loop,
    into_calls,
    spread_players,
    survival_mode,
    start_game,
    start_potions,
    developer_potion_control,
    game_starter,

    // Timer functions
    timer_main_1,
    timer_main_5,
    timer_main_20,
    timer_control_point_5,
    timer_control_point_20,
    timer_traitor_5,
    timer_traitor_20,
    timer_developer_20,

    // Timer
    horse_frost_walker,
    wolf_updates,
    update_sidebar,
    display_quotes,
    update_mine_count,
    update_min_health,
    remove_banned_items,

    // Messages
    messages_schedule_single,
    messages_pvp,
    messages_eternal_day,

    // Death
    handle_player_death,
    drop_player_heads,
    respawn_player,
    disable_respawn,

    // Control Point
    spawn_control_points,
    initialize_control_point,
    control_point_,
    control_point_tag_,
    control_point_score_,
    team_score,
    second_control_point,
    bbvalue,
    control_point_perks,
    control_point_messages_,
    update_public_cp_score,

    // Care Packages
    drop_carepackages,

    // Traitor Faction
    traitor_handout,
    traitor_actionbar,

    // Victory
    traitor_check,
    teams_alive_check,
    control_point_captured,
    teams_highscore_alive_check,
    victory,
    victory_message_,
    victory_message_solo,
    victory_message_traitor,
    minute_,
    initiate_deathmatch,
    death_match,

    // Misc (gameplay critical)
    clear_enderchest,
    display_rank,
    clear_schedule,
    locate_teammate,
    teams,
    join_team,
    announce_iron_man,
    check_iron_man,
    update_player_distance,

    // Misc (for fun)
    equip_gear,
    god_mode,
    battle_royale,

    // Developer
    debug_give,
    debug_remove,

    // Testing
    current_test_function
}
