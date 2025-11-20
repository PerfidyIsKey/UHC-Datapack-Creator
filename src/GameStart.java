import Enums.FileName;

import Enums.TagTemp;

import FileGeneration.FileData;
import commands.Tag;
import shared.EntityTag;
import shared.TagAction;

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
        fileCommands.add(Schedule.callFunction(FileName.display_quotes, 7 * Constant.secPerMinute));

        // Single execute
        fileCommands.add(Schedule.callFunction(FileName.messages_schedule_single));

        // Disable developer timers
        fileCommands.add(Schedule.clearFunction(FileName.timer_developer_20));

        // Disable automatic player respawn after 20 minutes
        fileCommands.add(Schedule.callFunction(FileName.disable_respawn, 20 * Constant.secPerMinute));

        fileCommands.add(Tag.action(Constant.admin, TagAction.ADD)
                        .name(EntityTag.GAME_STARTED)
                                .build());

        return new FileData(FileName.game_starter, fileCommands);
    }
}