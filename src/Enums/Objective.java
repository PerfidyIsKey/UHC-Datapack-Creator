package Enums;

import HelperClasses.ScoreboardObjective;

public enum Objective {
        TimeDum("TimeDum"),
        Time("Time"),
        Time2("Time2"),
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
        IsKiller("IsKiller"),
        TempKills("TempKills"),
        RandomQuotes("RandomQuotes");

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
