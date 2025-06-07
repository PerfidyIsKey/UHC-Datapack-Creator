import Enums.*;
import FileGeneration.FileData;
import HelperClasses.Entity;
import HelperClasses.Execute;
import  HelperClasses.Scoreboard;


import java.util.ArrayList;

public class Update {

    public FileData TimerMain1() {
        // Timer for functions that should be executed each tick
        ArrayList<String> fileCommands = new ArrayList<>();

        // Timer scoreboard
        fileCommands.add(Main.scoreboard.Add(Main.admin, Objective.Time2, 1));

        // Scheduled events
        fileCommands.add(Main.execute.If("@e[scores={Time2=" + (20 * Main.secPerMinute * Main.tickPerSecond) + "}]") +
                Schedule.callFunction(FileName.drop_carepackages));
        fileCommands.add(Main.execute.If("@e[scores={Time2=" + (30 * Main.secPerMinute * Main.tickPerSecond) + "}]") +
                Schedule.callFunction(FileName.initialize_control_point));
        fileCommands.add(Main.execute.If("@e[scores={Time2=" + (40 * Main.secPerMinute * Main.tickPerSecond) + "}]") +
                Schedule.callFunction(FileName.traitor_handout));

        // Self-schedule timer
        fileCommands.add(Schedule.callFunction(FileName.timer_main_1, 1, Duration.ticks));

        return new FileData(FileName.timer_main_1, fileCommands);
    }

    public FileData TimerMain5() {
        // Timer for functions that should be executed every 5 ticks
        ArrayList<String> fileCommands = new ArrayList<>();

        // Schedule functions
        fileCommands.add(Main.execute.If("@p[scores={Deaths=1}]") +
                Schedule.callFunction(FileName.handle_player_death));
        fileCommands.add(Schedule.callFunction(FileName.horse_frost_walker));
        fileCommands.add(Schedule.callFunction(FileName.remove_banned_items));
        fileCommands.add(Schedule.callFunction(FileName.update_min_health));

        // Self-schedule timer
        fileCommands.add(Schedule.callFunction(FileName.timer_main_5, 5, Duration.ticks));

        return new FileData(FileName.timer_main_5, fileCommands);
    }

    public FileData TimerMain20() {
        // Timer for functions that should be executed every 20 ticks
        ArrayList<String> fileCommands = new ArrayList<>();

        // Schedule functions
        fileCommands.add(Schedule.callFunction(FileName.locate_teammate));
        fileCommands.add(Schedule.callFunction(FileName.eliminate_baby_wolf));
        fileCommands.add(Main.execute.Unless("@p[tag=IronMan]") +
                Schedule.callFunction(FileName.check_iron_man));    // Update Iron Man candidates



        fileCommands.add(Schedule.callFunction(FileName.update_mine_count));
        fileCommands.add(Schedule.callFunction(FileName.update_sidebar));
        fileCommands.add(Schedule.callFunction(FileName.wolf_collar_execute));

        // Timer scoreboard
        fileCommands.add(Main.scoreboard.Add(Main.admin, Objective.TimeDum, 1));
        fileCommands.add(Main.execute.Store(ExecuteStore.result, "CurrentTime", Objective.Time) +
                Main.scoreboard.Get(Main.adminSingle,Objective.TimeDum));

        // Self-schedule timer
        fileCommands.add(Schedule.callFunction(FileName.timer_main_20, 20, Duration.ticks));

        return new FileData(FileName.timer_main_20, fileCommands);
    }

    public FileData TimerControlPoint5() {
        // Timer for Control Point continuous functions with interval of 5 ticks
        ArrayList<String> fileCommands = new ArrayList<>();

        // Schedule continuous functions
        fileCommands.add(Schedule.callFunction(FileName.bbvalue));
        for (int i = 1; i < 3; i++) {
            fileCommands.add(Schedule.callFunction("" + FileName.control_point_ + i));
            fileCommands.add(Main.execute.If("@p[scores={ControlPoint" + i + "=" + 48000 + "..}]") +
                    Schedule.callFunction(FileName.control_point_captured));
        }
        fileCommands.add(Schedule.callFunction(FileName.team_score));


        // Self-schedule timer
        fileCommands.add(Schedule.callFunction(FileName.timer_control_point_5, 5, Duration.ticks));

        return new FileData(FileName.timer_control_point_5, fileCommands);
    }

    public FileData TimerControlPoint20() {
        // Timer for Control Point continuous functions with interval of 20 ticks
        ArrayList<String> fileCommands = new ArrayList<>();

        // Schedule continuous functions
        for (int i = 1; i < 3; i++) {
            fileCommands.add(Schedule.callFunction("" + FileName.control_point_messages_ + i));
        }
        fileCommands.add(Schedule.callFunction(FileName.control_point_perks));
        fileCommands.add(Schedule.callFunction(FileName.update_public_cp_score));
        fileCommands.add(Main.execute.If("@p[scores=ControlPoint1={" + 14400 + "..}]") +
                Schedule.callFunction(FileName.second_control_point));


        // Self-schedule timer
        fileCommands.add(Schedule.callFunction(FileName.timer_control_point_20, 20, Duration.ticks));

        return new FileData(FileName.timer_control_point_20, fileCommands);
    }

    public FileData TimerTraitor5() {
        // Timer for Traitor Faction continuous functions with interval of 5 ticks
        ArrayList<String> fileCommands = new ArrayList<>();

        // Schedule continuous functions
        fileCommands.add(Main.execute.If(new Entity("@e[scores={Victory=1}]")) +
                Schedule.callFunction(FileName.traitor_check));  // Check if traitors have won

        // Self-schedule timer
        fileCommands.add(Schedule.callFunction(FileName.timer_traitor_5, 5, Duration.ticks));

        return new FileData(FileName.timer_traitor_5, fileCommands);
    }

    public FileData TimerTraitor20() {
        // Timer for Traitor Faction continuous functions with interval of 20 ticks
        ArrayList<String> fileCommands = new ArrayList<>();

        // Schedule continuous functions
        fileCommands.add(Schedule.callFunction(FileName.traitor_actionbar)); // Display traitor actionbar

        // Self-schedule timer
        fileCommands.add(Schedule.callFunction(FileName.timer_traitor_20, 20, Duration.ticks));

        return new FileData(FileName.timer_traitor_20, fileCommands);
    }

    public FileData TimerDeveloper20() {
        // Timer for Developer mode continuous functions with interval of 20 ticks
        ArrayList<String> fileCommands = new ArrayList<>();

        // Schedule continuous functions
        fileCommands.add(Schedule.callFunction(FileName.developer_potion_control));

        // Self-schedule timer
        fileCommands.add(Schedule.callFunction(FileName.timer_developer_20, 20, Duration.ticks));

        return new FileData(FileName.timer_developer_20, fileCommands);
    }
}

