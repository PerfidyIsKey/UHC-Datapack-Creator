import Enums.Duration;
import Enums.FileName;
import FileGeneration.FileData;

import java.util.ArrayList;

public class Update {
    public FileData TimerTick1() {
        // Timer for functions that should be executed each tick
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(Schedule.callFunction(FileName.timer_tick_1, 1, Duration.ticks));
        return new FileData(FileName.timer_tick_1, fileCommands);
    }

    public FileData TimerTick5() {
        // Timer for functions that should be executed every 5 ticks
        ArrayList<String> fileCommands = new ArrayList<>();

        // Schedule functions
        for (int i = 1; i < 3; i++)
            fileCommands.add(Schedule.callFunction("" + FileName.controlpoint_ + i, 5, Duration.ticks));

        fileCommands.add(Schedule.callFunction(FileName.team_score));
        fileCommands.add(Schedule.callFunction(FileName.traitor_check));
        fileCommands.add(Schedule.callFunction(FileName.handle_player_death));
        fileCommands.add(Schedule.callFunction(FileName.horse_frost_walker));
        fileCommands.add(Schedule.callFunction(FileName.locate_teammate));
        fileCommands.add(Schedule.callFunction(FileName.remove_banned_items));
        fileCommands.add(Schedule.callFunction(FileName.update_min_health));

        fileCommands.add(Schedule.callFunction(FileName.timer_tick_5));
        return new FileData(FileName.timer_tick_5, fileCommands);
    }

    public FileData TimerTick20() {
        // Timer for functions that should be executed every 20 ticks
        ArrayList<String> fileCommands = new ArrayList<>();

        // Schedule functions
        fileCommands.add(Schedule.callFunction(FileName.carepackage_distributor));

        for (int i = 1; i < 3; i++) {
            fileCommands.add(Schedule.callFunction("" + FileName.controlpoint_messages_ + i));
        }
        fileCommands.add(Schedule.callFunction(FileName.controlpoint_perks));
        fileCommands.add(Schedule.callFunction(FileName.teams_alive_check));
        fileCommands.add(Schedule.callFunction(FileName.teams_highscore_alive_check));
        fileCommands.add(Schedule.callFunction(FileName.traitor_actionbar));
        fileCommands.add(Schedule.callFunction(FileName.update_public_cp_score));
        fileCommands.add(Schedule.callFunction(FileName.eliminate_baby_wolf));
        fileCommands.add(Schedule.callFunction(FileName.check_iron_man));
        fileCommands.add(Schedule.callFunction(FileName.update_mine_count));
        fileCommands.add(Schedule.callFunction(FileName.update_sidebar));
        fileCommands.add(Schedule.callFunction(FileName.wolf_collar_execute));
        fileCommands.add(Schedule.callFunction(FileName.check_iron_man));


        fileCommands.add(Schedule.callFunction(FileName.timer_tick_20, 20, Duration.ticks));
        return new FileData(FileName.timer_tick_20, fileCommands);


    }
}

