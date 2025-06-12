import Enums.Duration;
import Enums.FileName;
import Enums.Tag;
import FileGeneration.FileData;

import java.util.ArrayList;


public class GameStart {
    public static FileData GameStarter() {
        ArrayList<String> fileCommands = new ArrayList<>();
        // Add the rest here

        // Starts the update cycle
        // Main timers
        fileCommands.add(Schedule.callFunction(FileName.timer_main_1));
        fileCommands.add(Schedule.callFunction(FileName.timer_main_5));
        fileCommands.add(Schedule.callFunction(FileName.timer_main_20));

        // Other cycles
        fileCommands.add(Schedule.callFunction(FileName.display_quotes, 7 * Main.secPerMinute));

        // Single execute
        fileCommands.add(Schedule.callFunction(FileName.messages_schedule_single));

        // Disable developer timers
        fileCommands.add(CommandBuilder.addTag(Constant.admin, Tag.GameStarted));

        return new FileData(FileName.game_starter, fileCommands);
    }
}