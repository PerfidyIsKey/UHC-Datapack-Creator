import Enums.*;
import FileGeneration.FileData;
import HelperClasses.Entity;
import HelperClasses.Execute;
import  HelperClasses.Scoreboard;


import java.util.ArrayList;

public class Update {

    private Singleton singleton;
    public Update() {
        singleton = Singleton.getInstance();
    }

    public FileData TimerMain1() {
        // Timer for functions that should be executed each tick
        ArrayList<String> fileCommands = new ArrayList<>();

        // Timer scoreboard
        fileCommands.add(Main.scoreboard.Add(Constant.admin, Objective.Time2, 1));

        // Scheduled events
        fileCommands.add(Execute.If("@e[scores={Time2=" + (20 * Main.secPerMinute * Constant.tickFrequencyShort) + "..}]", false) +
                Execute.UnlessNext("@e[tag=" + Tag.CarePackagesDropped + "]", true) +
                Schedule.callFunction(FileName.drop_carepackages));
        fileCommands.add(Execute.If("@e[scores={Time2=" + (30 * Main.secPerMinute * Constant.tickFrequencyShort) + "..}]", false) +
                Execute.UnlessNext("@e[tag=" + Tag.ControlPoint1Enabled + "]", true) +
                Schedule.callFunction(FileName.initialize_control_point));
        fileCommands.add(Execute.If("@e[scores={Time2=" + (40 * Main.secPerMinute * Constant.tickFrequencyShort) + "..}]", false) +
                Execute.UnlessNext("@e[tag=" + Tag.TraitorsAssigned + "]", true) +
                Schedule.callFunction(FileName.traitor_handout));

        // Schedule functions
        fileCommands.add(Schedule.callFunction(FileName.update_min_health));

        // Self-schedule timer
        fileCommands.add(Schedule.callFunction(FileName.timer_main_1, 1, Duration.ticks));

        return new FileData(FileName.timer_main_1, fileCommands);
    }

    public FileData TimerMain5() {
        // Timer for functions that should be executed every 5 ticks
        ArrayList<String> fileCommands = new ArrayList<>();

        // Schedule functions
        fileCommands.add(Schedule.callFunction(FileName.horse_frost_walker));
        fileCommands.add(Schedule.callFunction(FileName.remove_banned_items));
        fileCommands.add(Execute.If("@p[scores={TimesCalled=1..}]") +
                Schedule.callFunction(FileName.update_player_distance));    // Check if custom team can be made

        // Self-schedule timer
        fileCommands.add(Schedule.callFunction(FileName.timer_main_5, 5, Duration.ticks));

        return new FileData(FileName.timer_main_5, fileCommands);
    }

    public FileData TimerMain20() {
        // Timer for functions that should be executed every 20 ticks
        ArrayList<String> fileCommands = new ArrayList<>();

        // Schedule functions
        fileCommands.add(Execute.If("@p[scores={Deaths=1}]") +
                Schedule.callFunction(FileName.handle_player_death));
        fileCommands.add(Schedule.callFunction(FileName.locate_teammate));
        fileCommands.add(Execute.Unless("@p[tag=IronMan]") +
                Schedule.callFunction(FileName.check_iron_man));    // Update Iron Man candidates
        fileCommands.add(Execute.As("@a") +
                Schedule.callFunction(FileName.update_mine_count)); // Update strip mine count
        fileCommands.add(Schedule.callFunction(FileName.update_sidebar));
        fileCommands.add(Schedule.callFunction(FileName.wolf_updates));

        // Timer scoreboard
        fileCommands.add(Main.scoreboard.Add(Constant.admin, Objective.TimeDum, 1));
        fileCommands.add(Execute.Store(ExecuteStore.result, "CurrentTime", Objective.Time) +
                Main.scoreboard.Get(Constant.adminSingle,Objective.TimeDum));

        // Self-schedule timer
        fileCommands.add(Schedule.callFunction(FileName.timer_main_20, 20, Duration.ticks));

        return new FileData(FileName.timer_main_20, fileCommands);
    }

    public FileData TimerControlPoint5() {
        // Timer for Control Point continuous functions with interval of 5 ticks
        ArrayList<String> fileCommands = new ArrayList<>();

        // Self-schedule timer
        fileCommands.add(Schedule.callFunction(FileName.timer_control_point_5, 5, Duration.ticks));

        return new FileData(FileName.timer_control_point_5, fileCommands);
    }

    public FileData TimerControlPoint20() {
        // Timer for Control Point continuous functions with interval of 20 ticks
        ArrayList<String> fileCommands = new ArrayList<>();

        //Schedule Controlpoint functionality for CP1
        fileCommands.add(Schedule.callFunction("" + FileName.control_point_ + 1));
        fileCommands.add(Schedule.callFunction("" + FileName.control_point_messages_ + 1));

        //Schedule Controlpoint functionality for CP2, when enabled
        fileCommands.add(Execute.If("@e[tag=" + Tag.ControlPoint2Enabled + "]", true) +
                Schedule.callFunction("" + FileName.control_point_ + 2));
        fileCommands.add(Execute.If("@e[tag=" + Tag.ControlPoint2Enabled + "]", true) +
                Schedule.callFunction("" + FileName.control_point_messages_ + 2));

        //Functionality based on CP score.
        //Check if Controlpoint is captured.
        for (int i = 1; i < 3; i++) {
            fileCommands.add(Execute.If("@p[scores={ControlPoint" + i + "=" + 20 * singleton.getMinToCPScore() + "..}]", false) +
                    Execute.UnlessNext("@e[tag=" + Tag.ControlPointCaptured + "]", true) +
                    Schedule.callFunction(FileName.control_point_captured));
        }

        fileCommands.add(Schedule.callFunction(FileName.control_point_perks));
        fileCommands.add(Schedule.callFunction(FileName.update_public_cp_score));

        //enable second controlpoint when necessary.
        fileCommands.add(Execute.If("@p[scores={ControlPoint1=" + 6 * singleton.getMinToCPScore() + "..}]", false) +
                Execute.UnlessNext("@e[tag=" + Tag.ControlPoint2Enabled + "]", true) +
                Schedule.callFunction(FileName.second_control_point));

        // manage boss bar
        fileCommands.add(Schedule.callFunction(FileName.bbvalue));
        fileCommands.add(Schedule.callFunction(FileName.team_score));


        // Self-schedule timer
        fileCommands.add(Schedule.callFunction(FileName.timer_control_point_20, 20, Duration.ticks));

        return new FileData(FileName.timer_control_point_20, fileCommands);
    }

    public FileData TimerTraitor5() {
        // Timer for Traitor Faction continuous functions with interval of 5 ticks
        ArrayList<String> fileCommands = new ArrayList<>();

        // Schedule continuous functions
        fileCommands.add(Execute.If(new Entity("@e[scores={Victory=1}]")) +
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
        fileCommands.add(Execute.Unless("@e[tag=" + Tag.GameStarted +"]") +
                Schedule.callFunction(FileName.timer_developer_20, 20, Duration.ticks));

        return new FileData(FileName.timer_developer_20, fileCommands);
    }
}

