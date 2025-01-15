package Enums;

import HelperClasses.ScoreboardObjective;

public enum Objective {
        TimDum("TimDum"),
        TimeDum("TimeDum"),
        Time("Time"),
        SideDum("SideDum"),
        ControlPoint("ControlPoint"),
        CPScore("CPScore"),
        MSGDum("MSGDum"),
        Highscore("Highscore"),
        Hearts("Hearts"),
        Apples("Apples"),
        Stone("Stone"),
        Diorite("Diorite"),
        Andesite("Andesite"),
        Granite("Granite"),
        Deepslate("Deepslate"),
        Mining("Mining"),
        Deaths("Deaths"),
        Kills("Kills"),
        Rank("Rank"),
        WorldLoad("WorldLoad"),
        CollarCheck("CollarCheck"),
        MinHealth("MinHealth"),
        Victory("Victory"),
        WolfAge("WolfAge"),
        CP("CP"),
        FoundTeam("FoundTeam"),
        TimesCalled("TimesCalled"),
        Distance("Distance"),
        Pos("Pos"),
        Square("Square"),
        DamageTaken("DamageTaken"),
        IsKiller("IsKiller");

        private final String symbol;

        Objective(String symbol) {
                this.symbol = symbol;
        }

        public String extendName(int number) {
                return symbol + number;
        }

        public String extendName(String content) {
                return symbol + content;
        }
}
