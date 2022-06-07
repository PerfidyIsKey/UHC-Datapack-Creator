execute if entity @p[scores={CP1Team0=7200..7250}] run execute if entity @p[team=Team0,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"YELLOW","color":"yellow"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP1Team0=7200..7250}] run effect give @a[team=Team0,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP1Team0=7200..7250}] run execute if entity @p[team=Team0,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team0=7200..7250}] run tag @a[team=Team0] add ReceivedPerk1
execute if entity @p[scores={CP1Team0=14400..14450}] run execute if entity @p[team=Team0,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"YELLOW","color":"yellow"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP1Team0=14400..14450}] run effect give @a[team=Team0,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP1Team0=14400..14450}] run execute if entity @p[team=Team0,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team0=14400..14450}] run tag @a[team=Team0] add ReceivedPerk2
execute if entity @p[scores={CP1Team0=28800..28850}] run execute if entity @p[team=Team0,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"YELLOW","color":"yellow"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP1Team0=28800..28850}] run effect give @a[team=Team0,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP1Team0=28800..28850}] run execute if entity @p[team=Team0,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team0=28800..28850}] run tag @a[team=Team0] add ReceivedPerk3
execute if entity @p[scores={CP1Team0=36000..36050}] run execute if entity @p[team=Team0,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"YELLOW","color":"yellow"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP1Team0=36000..36050}] run give @a[team=Team0,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP1Team0=36000..36050}] run execute if entity @p[team=Team0,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team0=36000..36050}] run tag @a[team=Team0] add ReceivedPerk4
execute if entity @p[scores={CP1Team1=7200..7250}] run execute if entity @p[team=Team1,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"BLUE","color":"blue"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP1Team1=7200..7250}] run effect give @a[team=Team1,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP1Team1=7200..7250}] run execute if entity @p[team=Team1,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team1=7200..7250}] run tag @a[team=Team1] add ReceivedPerk1
execute if entity @p[scores={CP1Team1=14400..14450}] run execute if entity @p[team=Team1,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"BLUE","color":"blue"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP1Team1=14400..14450}] run effect give @a[team=Team1,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP1Team1=14400..14450}] run execute if entity @p[team=Team1,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team1=14400..14450}] run tag @a[team=Team1] add ReceivedPerk2
execute if entity @p[scores={CP1Team1=28800..28850}] run execute if entity @p[team=Team1,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"BLUE","color":"blue"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP1Team1=28800..28850}] run effect give @a[team=Team1,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP1Team1=28800..28850}] run execute if entity @p[team=Team1,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team1=28800..28850}] run tag @a[team=Team1] add ReceivedPerk3
execute if entity @p[scores={CP1Team1=36000..36050}] run execute if entity @p[team=Team1,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"BLUE","color":"blue"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP1Team1=36000..36050}] run give @a[team=Team1,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP1Team1=36000..36050}] run execute if entity @p[team=Team1,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team1=36000..36050}] run tag @a[team=Team1] add ReceivedPerk4
execute if entity @p[scores={CP1Team2=7200..7250}] run execute if entity @p[team=Team2,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"RED","color":"red"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP1Team2=7200..7250}] run effect give @a[team=Team2,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP1Team2=7200..7250}] run execute if entity @p[team=Team2,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team2=7200..7250}] run tag @a[team=Team2] add ReceivedPerk1
execute if entity @p[scores={CP1Team2=14400..14450}] run execute if entity @p[team=Team2,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"RED","color":"red"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP1Team2=14400..14450}] run effect give @a[team=Team2,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP1Team2=14400..14450}] run execute if entity @p[team=Team2,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team2=14400..14450}] run tag @a[team=Team2] add ReceivedPerk2
execute if entity @p[scores={CP1Team2=28800..28850}] run execute if entity @p[team=Team2,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"RED","color":"red"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP1Team2=28800..28850}] run effect give @a[team=Team2,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP1Team2=28800..28850}] run execute if entity @p[team=Team2,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team2=28800..28850}] run tag @a[team=Team2] add ReceivedPerk3
execute if entity @p[scores={CP1Team2=36000..36050}] run execute if entity @p[team=Team2,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"RED","color":"red"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP1Team2=36000..36050}] run give @a[team=Team2,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP1Team2=36000..36050}] run execute if entity @p[team=Team2,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team2=36000..36050}] run tag @a[team=Team2] add ReceivedPerk4
execute if entity @p[scores={CP1Team3=7200..7250}] run execute if entity @p[team=Team3,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"PURPLE","color":"dark_purple"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP1Team3=7200..7250}] run effect give @a[team=Team3,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP1Team3=7200..7250}] run execute if entity @p[team=Team3,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team3=7200..7250}] run tag @a[team=Team3] add ReceivedPerk1
execute if entity @p[scores={CP1Team3=14400..14450}] run execute if entity @p[team=Team3,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"PURPLE","color":"dark_purple"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP1Team3=14400..14450}] run effect give @a[team=Team3,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP1Team3=14400..14450}] run execute if entity @p[team=Team3,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team3=14400..14450}] run tag @a[team=Team3] add ReceivedPerk2
execute if entity @p[scores={CP1Team3=28800..28850}] run execute if entity @p[team=Team3,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"PURPLE","color":"dark_purple"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP1Team3=28800..28850}] run effect give @a[team=Team3,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP1Team3=28800..28850}] run execute if entity @p[team=Team3,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team3=28800..28850}] run tag @a[team=Team3] add ReceivedPerk3
execute if entity @p[scores={CP1Team3=36000..36050}] run execute if entity @p[team=Team3,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"PURPLE","color":"dark_purple"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP1Team3=36000..36050}] run give @a[team=Team3,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP1Team3=36000..36050}] run execute if entity @p[team=Team3,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team3=36000..36050}] run tag @a[team=Team3] add ReceivedPerk4
execute if entity @p[scores={CP1Team4=7200..7250}] run execute if entity @p[team=Team4,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"GREEN","color":"dark_green"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP1Team4=7200..7250}] run effect give @a[team=Team4,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP1Team4=7200..7250}] run execute if entity @p[team=Team4,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team4=7200..7250}] run tag @a[team=Team4] add ReceivedPerk1
execute if entity @p[scores={CP1Team4=14400..14450}] run execute if entity @p[team=Team4,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"GREEN","color":"dark_green"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP1Team4=14400..14450}] run effect give @a[team=Team4,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP1Team4=14400..14450}] run execute if entity @p[team=Team4,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team4=14400..14450}] run tag @a[team=Team4] add ReceivedPerk2
execute if entity @p[scores={CP1Team4=28800..28850}] run execute if entity @p[team=Team4,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"GREEN","color":"dark_green"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP1Team4=28800..28850}] run effect give @a[team=Team4,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP1Team4=28800..28850}] run execute if entity @p[team=Team4,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team4=28800..28850}] run tag @a[team=Team4] add ReceivedPerk3
execute if entity @p[scores={CP1Team4=36000..36050}] run execute if entity @p[team=Team4,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"GREEN","color":"dark_green"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP1Team4=36000..36050}] run give @a[team=Team4,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP1Team4=36000..36050}] run execute if entity @p[team=Team4,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team4=36000..36050}] run tag @a[team=Team4] add ReceivedPerk4
execute if entity @p[scores={CP1Team5=7200..7250}] run execute if entity @p[team=Team5,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"PINK","color":"light_purple"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP1Team5=7200..7250}] run effect give @a[team=Team5,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP1Team5=7200..7250}] run execute if entity @p[team=Team5,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team5=7200..7250}] run tag @a[team=Team5] add ReceivedPerk1
execute if entity @p[scores={CP1Team5=14400..14450}] run execute if entity @p[team=Team5,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"PINK","color":"light_purple"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP1Team5=14400..14450}] run effect give @a[team=Team5,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP1Team5=14400..14450}] run execute if entity @p[team=Team5,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team5=14400..14450}] run tag @a[team=Team5] add ReceivedPerk2
execute if entity @p[scores={CP1Team5=28800..28850}] run execute if entity @p[team=Team5,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"PINK","color":"light_purple"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP1Team5=28800..28850}] run effect give @a[team=Team5,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP1Team5=28800..28850}] run execute if entity @p[team=Team5,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team5=28800..28850}] run tag @a[team=Team5] add ReceivedPerk3
execute if entity @p[scores={CP1Team5=36000..36050}] run execute if entity @p[team=Team5,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"PINK","color":"light_purple"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP1Team5=36000..36050}] run give @a[team=Team5,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP1Team5=36000..36050}] run execute if entity @p[team=Team5,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team5=36000..36050}] run tag @a[team=Team5] add ReceivedPerk4
execute if entity @p[scores={CP1Team6=7200..7250}] run execute if entity @p[team=Team6,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"BLACK","color":"black"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP1Team6=7200..7250}] run effect give @a[team=Team6,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP1Team6=7200..7250}] run execute if entity @p[team=Team6,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team6=7200..7250}] run tag @a[team=Team6] add ReceivedPerk1
execute if entity @p[scores={CP1Team6=14400..14450}] run execute if entity @p[team=Team6,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"BLACK","color":"black"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP1Team6=14400..14450}] run effect give @a[team=Team6,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP1Team6=14400..14450}] run execute if entity @p[team=Team6,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team6=14400..14450}] run tag @a[team=Team6] add ReceivedPerk2
execute if entity @p[scores={CP1Team6=28800..28850}] run execute if entity @p[team=Team6,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"BLACK","color":"black"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP1Team6=28800..28850}] run effect give @a[team=Team6,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP1Team6=28800..28850}] run execute if entity @p[team=Team6,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team6=28800..28850}] run tag @a[team=Team6] add ReceivedPerk3
execute if entity @p[scores={CP1Team6=36000..36050}] run execute if entity @p[team=Team6,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"BLACK","color":"black"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP1Team6=36000..36050}] run give @a[team=Team6,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP1Team6=36000..36050}] run execute if entity @p[team=Team6,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team6=36000..36050}] run tag @a[team=Team6] add ReceivedPerk4
execute if entity @p[scores={CP1Team7=7200..7250}] run execute if entity @p[team=Team7,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"ORANGE","color":"gold"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP1Team7=7200..7250}] run effect give @a[team=Team7,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP1Team7=7200..7250}] run execute if entity @p[team=Team7,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team7=7200..7250}] run tag @a[team=Team7] add ReceivedPerk1
execute if entity @p[scores={CP1Team7=14400..14450}] run execute if entity @p[team=Team7,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"ORANGE","color":"gold"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP1Team7=14400..14450}] run effect give @a[team=Team7,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP1Team7=14400..14450}] run execute if entity @p[team=Team7,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team7=14400..14450}] run tag @a[team=Team7] add ReceivedPerk2
execute if entity @p[scores={CP1Team7=28800..28850}] run execute if entity @p[team=Team7,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"ORANGE","color":"gold"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP1Team7=28800..28850}] run effect give @a[team=Team7,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP1Team7=28800..28850}] run execute if entity @p[team=Team7,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team7=28800..28850}] run tag @a[team=Team7] add ReceivedPerk3
execute if entity @p[scores={CP1Team7=36000..36050}] run execute if entity @p[team=Team7,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"ORANGE","color":"gold"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP1Team7=36000..36050}] run give @a[team=Team7,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP1Team7=36000..36050}] run execute if entity @p[team=Team7,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team7=36000..36050}] run tag @a[team=Team7] add ReceivedPerk4
execute if entity @p[scores={CP1Team8=7200..7250}] run execute if entity @p[team=Team8,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"GRAY","color":"gray"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP1Team8=7200..7250}] run effect give @a[team=Team8,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP1Team8=7200..7250}] run execute if entity @p[team=Team8,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team8=7200..7250}] run tag @a[team=Team8] add ReceivedPerk1
execute if entity @p[scores={CP1Team8=14400..14450}] run execute if entity @p[team=Team8,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"GRAY","color":"gray"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP1Team8=14400..14450}] run effect give @a[team=Team8,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP1Team8=14400..14450}] run execute if entity @p[team=Team8,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team8=14400..14450}] run tag @a[team=Team8] add ReceivedPerk2
execute if entity @p[scores={CP1Team8=28800..28850}] run execute if entity @p[team=Team8,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"GRAY","color":"gray"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP1Team8=28800..28850}] run effect give @a[team=Team8,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP1Team8=28800..28850}] run execute if entity @p[team=Team8,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team8=28800..28850}] run tag @a[team=Team8] add ReceivedPerk3
execute if entity @p[scores={CP1Team8=36000..36050}] run execute if entity @p[team=Team8,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"GRAY","color":"gray"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP1Team8=36000..36050}] run give @a[team=Team8,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP1Team8=36000..36050}] run execute if entity @p[team=Team8,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team8=36000..36050}] run tag @a[team=Team8] add ReceivedPerk4
execute if entity @p[scores={CP1Team9=7200..7250}] run execute if entity @p[team=Team9,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"AQUA","color":"aqua"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP1Team9=7200..7250}] run effect give @a[team=Team9,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP1Team9=7200..7250}] run execute if entity @p[team=Team9,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team9=7200..7250}] run tag @a[team=Team9] add ReceivedPerk1
execute if entity @p[scores={CP1Team9=14400..14450}] run execute if entity @p[team=Team9,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"AQUA","color":"aqua"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP1Team9=14400..14450}] run effect give @a[team=Team9,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP1Team9=14400..14450}] run execute if entity @p[team=Team9,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team9=14400..14450}] run tag @a[team=Team9] add ReceivedPerk2
execute if entity @p[scores={CP1Team9=28800..28850}] run execute if entity @p[team=Team9,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"AQUA","color":"aqua"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP1Team9=28800..28850}] run effect give @a[team=Team9,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP1Team9=28800..28850}] run execute if entity @p[team=Team9,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team9=28800..28850}] run tag @a[team=Team9] add ReceivedPerk3
execute if entity @p[scores={CP1Team9=36000..36050}] run execute if entity @p[team=Team9,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"AQUA","color":"aqua"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP1Team9=36000..36050}] run give @a[team=Team9,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP1Team9=36000..36050}] run execute if entity @p[team=Team9,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team9=36000..36050}] run tag @a[team=Team9] add ReceivedPerk4
execute if entity @p[scores={CP1Team10=7200..7250}] run execute if entity @p[team=Team10,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"DARK RED","color":"dark_red"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP1Team10=7200..7250}] run effect give @a[team=Team10,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP1Team10=7200..7250}] run execute if entity @p[team=Team10,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team10=7200..7250}] run tag @a[team=Team10] add ReceivedPerk1
execute if entity @p[scores={CP1Team10=14400..14450}] run execute if entity @p[team=Team10,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"DARK RED","color":"dark_red"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP1Team10=14400..14450}] run effect give @a[team=Team10,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP1Team10=14400..14450}] run execute if entity @p[team=Team10,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team10=14400..14450}] run tag @a[team=Team10] add ReceivedPerk2
execute if entity @p[scores={CP1Team10=28800..28850}] run execute if entity @p[team=Team10,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"DARK RED","color":"dark_red"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP1Team10=28800..28850}] run effect give @a[team=Team10,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP1Team10=28800..28850}] run execute if entity @p[team=Team10,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team10=28800..28850}] run tag @a[team=Team10] add ReceivedPerk3
execute if entity @p[scores={CP1Team10=36000..36050}] run execute if entity @p[team=Team10,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"DARK RED","color":"dark_red"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP1Team10=36000..36050}] run give @a[team=Team10,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP1Team10=36000..36050}] run execute if entity @p[team=Team10,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team10=36000..36050}] run tag @a[team=Team10] add ReceivedPerk4
execute if entity @p[scores={CP1Team11=7200..7250}] run execute if entity @p[team=Team11,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"DARK BLUE","color":"dark_blue"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP1Team11=7200..7250}] run effect give @a[team=Team11,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP1Team11=7200..7250}] run execute if entity @p[team=Team11,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team11=7200..7250}] run tag @a[team=Team11] add ReceivedPerk1
execute if entity @p[scores={CP1Team11=14400..14450}] run execute if entity @p[team=Team11,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"DARK BLUE","color":"dark_blue"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP1Team11=14400..14450}] run effect give @a[team=Team11,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP1Team11=14400..14450}] run execute if entity @p[team=Team11,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team11=14400..14450}] run tag @a[team=Team11] add ReceivedPerk2
execute if entity @p[scores={CP1Team11=28800..28850}] run execute if entity @p[team=Team11,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"DARK BLUE","color":"dark_blue"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP1Team11=28800..28850}] run effect give @a[team=Team11,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP1Team11=28800..28850}] run execute if entity @p[team=Team11,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team11=28800..28850}] run tag @a[team=Team11] add ReceivedPerk3
execute if entity @p[scores={CP1Team11=36000..36050}] run execute if entity @p[team=Team11,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"DARK BLUE","color":"dark_blue"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP1Team11=36000..36050}] run give @a[team=Team11,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP1Team11=36000..36050}] run execute if entity @p[team=Team11,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team11=36000..36050}] run tag @a[team=Team11] add ReceivedPerk4
execute if entity @p[scores={CP1Team12=7200..7250}] run execute if entity @p[team=Team12,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"DARK AQUA","color":"dark_aqua"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP1Team12=7200..7250}] run effect give @a[team=Team12,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP1Team12=7200..7250}] run execute if entity @p[team=Team12,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team12=7200..7250}] run tag @a[team=Team12] add ReceivedPerk1
execute if entity @p[scores={CP1Team12=14400..14450}] run execute if entity @p[team=Team12,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"DARK AQUA","color":"dark_aqua"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP1Team12=14400..14450}] run effect give @a[team=Team12,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP1Team12=14400..14450}] run execute if entity @p[team=Team12,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team12=14400..14450}] run tag @a[team=Team12] add ReceivedPerk2
execute if entity @p[scores={CP1Team12=28800..28850}] run execute if entity @p[team=Team12,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"DARK AQUA","color":"dark_aqua"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP1Team12=28800..28850}] run effect give @a[team=Team12,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP1Team12=28800..28850}] run execute if entity @p[team=Team12,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team12=28800..28850}] run tag @a[team=Team12] add ReceivedPerk3
execute if entity @p[scores={CP1Team12=36000..36050}] run execute if entity @p[team=Team12,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"DARK AQUA","color":"dark_aqua"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP1Team12=36000..36050}] run give @a[team=Team12,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP1Team12=36000..36050}] run execute if entity @p[team=Team12,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP1Team12=36000..36050}] run tag @a[team=Team12] add ReceivedPerk4
execute if entity @p[scores={CP2Team0=7200..7250}] run execute if entity @p[team=Team0,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"YELLOW","color":"yellow"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP2Team0=7200..7250}] run effect give @a[team=Team0,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP2Team0=7200..7250}] run execute if entity @p[team=Team0,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team0=7200..7250}] run tag @a[team=Team0] add ReceivedPerk1
execute if entity @p[scores={CP2Team0=14400..14450}] run execute if entity @p[team=Team0,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"YELLOW","color":"yellow"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP2Team0=14400..14450}] run effect give @a[team=Team0,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP2Team0=14400..14450}] run execute if entity @p[team=Team0,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team0=14400..14450}] run tag @a[team=Team0] add ReceivedPerk2
execute if entity @p[scores={CP2Team0=28800..28850}] run execute if entity @p[team=Team0,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"YELLOW","color":"yellow"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP2Team0=28800..28850}] run effect give @a[team=Team0,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP2Team0=28800..28850}] run execute if entity @p[team=Team0,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team0=28800..28850}] run tag @a[team=Team0] add ReceivedPerk3
execute if entity @p[scores={CP2Team0=36000..36050}] run execute if entity @p[team=Team0,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"YELLOW","color":"yellow"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP2Team0=36000..36050}] run give @a[team=Team0,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP2Team0=36000..36050}] run execute if entity @p[team=Team0,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team0=36000..36050}] run tag @a[team=Team0] add ReceivedPerk4
execute if entity @p[scores={CP2Team1=7200..7250}] run execute if entity @p[team=Team1,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"BLUE","color":"blue"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP2Team1=7200..7250}] run effect give @a[team=Team1,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP2Team1=7200..7250}] run execute if entity @p[team=Team1,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team1=7200..7250}] run tag @a[team=Team1] add ReceivedPerk1
execute if entity @p[scores={CP2Team1=14400..14450}] run execute if entity @p[team=Team1,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"BLUE","color":"blue"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP2Team1=14400..14450}] run effect give @a[team=Team1,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP2Team1=14400..14450}] run execute if entity @p[team=Team1,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team1=14400..14450}] run tag @a[team=Team1] add ReceivedPerk2
execute if entity @p[scores={CP2Team1=28800..28850}] run execute if entity @p[team=Team1,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"BLUE","color":"blue"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP2Team1=28800..28850}] run effect give @a[team=Team1,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP2Team1=28800..28850}] run execute if entity @p[team=Team1,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team1=28800..28850}] run tag @a[team=Team1] add ReceivedPerk3
execute if entity @p[scores={CP2Team1=36000..36050}] run execute if entity @p[team=Team1,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"BLUE","color":"blue"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP2Team1=36000..36050}] run give @a[team=Team1,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP2Team1=36000..36050}] run execute if entity @p[team=Team1,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team1=36000..36050}] run tag @a[team=Team1] add ReceivedPerk4
execute if entity @p[scores={CP2Team2=7200..7250}] run execute if entity @p[team=Team2,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"RED","color":"red"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP2Team2=7200..7250}] run effect give @a[team=Team2,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP2Team2=7200..7250}] run execute if entity @p[team=Team2,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team2=7200..7250}] run tag @a[team=Team2] add ReceivedPerk1
execute if entity @p[scores={CP2Team2=14400..14450}] run execute if entity @p[team=Team2,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"RED","color":"red"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP2Team2=14400..14450}] run effect give @a[team=Team2,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP2Team2=14400..14450}] run execute if entity @p[team=Team2,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team2=14400..14450}] run tag @a[team=Team2] add ReceivedPerk2
execute if entity @p[scores={CP2Team2=28800..28850}] run execute if entity @p[team=Team2,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"RED","color":"red"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP2Team2=28800..28850}] run effect give @a[team=Team2,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP2Team2=28800..28850}] run execute if entity @p[team=Team2,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team2=28800..28850}] run tag @a[team=Team2] add ReceivedPerk3
execute if entity @p[scores={CP2Team2=36000..36050}] run execute if entity @p[team=Team2,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"RED","color":"red"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP2Team2=36000..36050}] run give @a[team=Team2,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP2Team2=36000..36050}] run execute if entity @p[team=Team2,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team2=36000..36050}] run tag @a[team=Team2] add ReceivedPerk4
execute if entity @p[scores={CP2Team3=7200..7250}] run execute if entity @p[team=Team3,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"PURPLE","color":"dark_purple"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP2Team3=7200..7250}] run effect give @a[team=Team3,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP2Team3=7200..7250}] run execute if entity @p[team=Team3,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team3=7200..7250}] run tag @a[team=Team3] add ReceivedPerk1
execute if entity @p[scores={CP2Team3=14400..14450}] run execute if entity @p[team=Team3,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"PURPLE","color":"dark_purple"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP2Team3=14400..14450}] run effect give @a[team=Team3,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP2Team3=14400..14450}] run execute if entity @p[team=Team3,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team3=14400..14450}] run tag @a[team=Team3] add ReceivedPerk2
execute if entity @p[scores={CP2Team3=28800..28850}] run execute if entity @p[team=Team3,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"PURPLE","color":"dark_purple"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP2Team3=28800..28850}] run effect give @a[team=Team3,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP2Team3=28800..28850}] run execute if entity @p[team=Team3,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team3=28800..28850}] run tag @a[team=Team3] add ReceivedPerk3
execute if entity @p[scores={CP2Team3=36000..36050}] run execute if entity @p[team=Team3,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"PURPLE","color":"dark_purple"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP2Team3=36000..36050}] run give @a[team=Team3,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP2Team3=36000..36050}] run execute if entity @p[team=Team3,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team3=36000..36050}] run tag @a[team=Team3] add ReceivedPerk4
execute if entity @p[scores={CP2Team4=7200..7250}] run execute if entity @p[team=Team4,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"GREEN","color":"dark_green"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP2Team4=7200..7250}] run effect give @a[team=Team4,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP2Team4=7200..7250}] run execute if entity @p[team=Team4,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team4=7200..7250}] run tag @a[team=Team4] add ReceivedPerk1
execute if entity @p[scores={CP2Team4=14400..14450}] run execute if entity @p[team=Team4,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"GREEN","color":"dark_green"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP2Team4=14400..14450}] run effect give @a[team=Team4,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP2Team4=14400..14450}] run execute if entity @p[team=Team4,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team4=14400..14450}] run tag @a[team=Team4] add ReceivedPerk2
execute if entity @p[scores={CP2Team4=28800..28850}] run execute if entity @p[team=Team4,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"GREEN","color":"dark_green"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP2Team4=28800..28850}] run effect give @a[team=Team4,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP2Team4=28800..28850}] run execute if entity @p[team=Team4,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team4=28800..28850}] run tag @a[team=Team4] add ReceivedPerk3
execute if entity @p[scores={CP2Team4=36000..36050}] run execute if entity @p[team=Team4,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"GREEN","color":"dark_green"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP2Team4=36000..36050}] run give @a[team=Team4,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP2Team4=36000..36050}] run execute if entity @p[team=Team4,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team4=36000..36050}] run tag @a[team=Team4] add ReceivedPerk4
execute if entity @p[scores={CP2Team5=7200..7250}] run execute if entity @p[team=Team5,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"PINK","color":"light_purple"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP2Team5=7200..7250}] run effect give @a[team=Team5,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP2Team5=7200..7250}] run execute if entity @p[team=Team5,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team5=7200..7250}] run tag @a[team=Team5] add ReceivedPerk1
execute if entity @p[scores={CP2Team5=14400..14450}] run execute if entity @p[team=Team5,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"PINK","color":"light_purple"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP2Team5=14400..14450}] run effect give @a[team=Team5,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP2Team5=14400..14450}] run execute if entity @p[team=Team5,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team5=14400..14450}] run tag @a[team=Team5] add ReceivedPerk2
execute if entity @p[scores={CP2Team5=28800..28850}] run execute if entity @p[team=Team5,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"PINK","color":"light_purple"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP2Team5=28800..28850}] run effect give @a[team=Team5,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP2Team5=28800..28850}] run execute if entity @p[team=Team5,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team5=28800..28850}] run tag @a[team=Team5] add ReceivedPerk3
execute if entity @p[scores={CP2Team5=36000..36050}] run execute if entity @p[team=Team5,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"PINK","color":"light_purple"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP2Team5=36000..36050}] run give @a[team=Team5,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP2Team5=36000..36050}] run execute if entity @p[team=Team5,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team5=36000..36050}] run tag @a[team=Team5] add ReceivedPerk4
execute if entity @p[scores={CP2Team6=7200..7250}] run execute if entity @p[team=Team6,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"BLACK","color":"black"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP2Team6=7200..7250}] run effect give @a[team=Team6,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP2Team6=7200..7250}] run execute if entity @p[team=Team6,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team6=7200..7250}] run tag @a[team=Team6] add ReceivedPerk1
execute if entity @p[scores={CP2Team6=14400..14450}] run execute if entity @p[team=Team6,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"BLACK","color":"black"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP2Team6=14400..14450}] run effect give @a[team=Team6,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP2Team6=14400..14450}] run execute if entity @p[team=Team6,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team6=14400..14450}] run tag @a[team=Team6] add ReceivedPerk2
execute if entity @p[scores={CP2Team6=28800..28850}] run execute if entity @p[team=Team6,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"BLACK","color":"black"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP2Team6=28800..28850}] run effect give @a[team=Team6,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP2Team6=28800..28850}] run execute if entity @p[team=Team6,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team6=28800..28850}] run tag @a[team=Team6] add ReceivedPerk3
execute if entity @p[scores={CP2Team6=36000..36050}] run execute if entity @p[team=Team6,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"BLACK","color":"black"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP2Team6=36000..36050}] run give @a[team=Team6,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP2Team6=36000..36050}] run execute if entity @p[team=Team6,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team6=36000..36050}] run tag @a[team=Team6] add ReceivedPerk4
execute if entity @p[scores={CP2Team7=7200..7250}] run execute if entity @p[team=Team7,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"ORANGE","color":"gold"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP2Team7=7200..7250}] run effect give @a[team=Team7,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP2Team7=7200..7250}] run execute if entity @p[team=Team7,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team7=7200..7250}] run tag @a[team=Team7] add ReceivedPerk1
execute if entity @p[scores={CP2Team7=14400..14450}] run execute if entity @p[team=Team7,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"ORANGE","color":"gold"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP2Team7=14400..14450}] run effect give @a[team=Team7,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP2Team7=14400..14450}] run execute if entity @p[team=Team7,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team7=14400..14450}] run tag @a[team=Team7] add ReceivedPerk2
execute if entity @p[scores={CP2Team7=28800..28850}] run execute if entity @p[team=Team7,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"ORANGE","color":"gold"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP2Team7=28800..28850}] run effect give @a[team=Team7,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP2Team7=28800..28850}] run execute if entity @p[team=Team7,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team7=28800..28850}] run tag @a[team=Team7] add ReceivedPerk3
execute if entity @p[scores={CP2Team7=36000..36050}] run execute if entity @p[team=Team7,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"ORANGE","color":"gold"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP2Team7=36000..36050}] run give @a[team=Team7,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP2Team7=36000..36050}] run execute if entity @p[team=Team7,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team7=36000..36050}] run tag @a[team=Team7] add ReceivedPerk4
execute if entity @p[scores={CP2Team8=7200..7250}] run execute if entity @p[team=Team8,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"GRAY","color":"gray"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP2Team8=7200..7250}] run effect give @a[team=Team8,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP2Team8=7200..7250}] run execute if entity @p[team=Team8,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team8=7200..7250}] run tag @a[team=Team8] add ReceivedPerk1
execute if entity @p[scores={CP2Team8=14400..14450}] run execute if entity @p[team=Team8,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"GRAY","color":"gray"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP2Team8=14400..14450}] run effect give @a[team=Team8,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP2Team8=14400..14450}] run execute if entity @p[team=Team8,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team8=14400..14450}] run tag @a[team=Team8] add ReceivedPerk2
execute if entity @p[scores={CP2Team8=28800..28850}] run execute if entity @p[team=Team8,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"GRAY","color":"gray"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP2Team8=28800..28850}] run effect give @a[team=Team8,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP2Team8=28800..28850}] run execute if entity @p[team=Team8,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team8=28800..28850}] run tag @a[team=Team8] add ReceivedPerk3
execute if entity @p[scores={CP2Team8=36000..36050}] run execute if entity @p[team=Team8,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"GRAY","color":"gray"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP2Team8=36000..36050}] run give @a[team=Team8,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP2Team8=36000..36050}] run execute if entity @p[team=Team8,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team8=36000..36050}] run tag @a[team=Team8] add ReceivedPerk4
execute if entity @p[scores={CP2Team9=7200..7250}] run execute if entity @p[team=Team9,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"AQUA","color":"aqua"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP2Team9=7200..7250}] run effect give @a[team=Team9,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP2Team9=7200..7250}] run execute if entity @p[team=Team9,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team9=7200..7250}] run tag @a[team=Team9] add ReceivedPerk1
execute if entity @p[scores={CP2Team9=14400..14450}] run execute if entity @p[team=Team9,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"AQUA","color":"aqua"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP2Team9=14400..14450}] run effect give @a[team=Team9,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP2Team9=14400..14450}] run execute if entity @p[team=Team9,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team9=14400..14450}] run tag @a[team=Team9] add ReceivedPerk2
execute if entity @p[scores={CP2Team9=28800..28850}] run execute if entity @p[team=Team9,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"AQUA","color":"aqua"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP2Team9=28800..28850}] run effect give @a[team=Team9,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP2Team9=28800..28850}] run execute if entity @p[team=Team9,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team9=28800..28850}] run tag @a[team=Team9] add ReceivedPerk3
execute if entity @p[scores={CP2Team9=36000..36050}] run execute if entity @p[team=Team9,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"AQUA","color":"aqua"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP2Team9=36000..36050}] run give @a[team=Team9,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP2Team9=36000..36050}] run execute if entity @p[team=Team9,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team9=36000..36050}] run tag @a[team=Team9] add ReceivedPerk4
execute if entity @p[scores={CP2Team10=7200..7250}] run execute if entity @p[team=Team10,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"DARK RED","color":"dark_red"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP2Team10=7200..7250}] run effect give @a[team=Team10,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP2Team10=7200..7250}] run execute if entity @p[team=Team10,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team10=7200..7250}] run tag @a[team=Team10] add ReceivedPerk1
execute if entity @p[scores={CP2Team10=14400..14450}] run execute if entity @p[team=Team10,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"DARK RED","color":"dark_red"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP2Team10=14400..14450}] run effect give @a[team=Team10,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP2Team10=14400..14450}] run execute if entity @p[team=Team10,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team10=14400..14450}] run tag @a[team=Team10] add ReceivedPerk2
execute if entity @p[scores={CP2Team10=28800..28850}] run execute if entity @p[team=Team10,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"DARK RED","color":"dark_red"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP2Team10=28800..28850}] run effect give @a[team=Team10,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP2Team10=28800..28850}] run execute if entity @p[team=Team10,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team10=28800..28850}] run tag @a[team=Team10] add ReceivedPerk3
execute if entity @p[scores={CP2Team10=36000..36050}] run execute if entity @p[team=Team10,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"DARK RED","color":"dark_red"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP2Team10=36000..36050}] run give @a[team=Team10,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP2Team10=36000..36050}] run execute if entity @p[team=Team10,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team10=36000..36050}] run tag @a[team=Team10] add ReceivedPerk4
execute if entity @p[scores={CP2Team11=7200..7250}] run execute if entity @p[team=Team11,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"DARK BLUE","color":"dark_blue"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP2Team11=7200..7250}] run effect give @a[team=Team11,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP2Team11=7200..7250}] run execute if entity @p[team=Team11,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team11=7200..7250}] run tag @a[team=Team11] add ReceivedPerk1
execute if entity @p[scores={CP2Team11=14400..14450}] run execute if entity @p[team=Team11,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"DARK BLUE","color":"dark_blue"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP2Team11=14400..14450}] run effect give @a[team=Team11,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP2Team11=14400..14450}] run execute if entity @p[team=Team11,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team11=14400..14450}] run tag @a[team=Team11] add ReceivedPerk2
execute if entity @p[scores={CP2Team11=28800..28850}] run execute if entity @p[team=Team11,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"DARK BLUE","color":"dark_blue"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP2Team11=28800..28850}] run effect give @a[team=Team11,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP2Team11=28800..28850}] run execute if entity @p[team=Team11,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team11=28800..28850}] run tag @a[team=Team11] add ReceivedPerk3
execute if entity @p[scores={CP2Team11=36000..36050}] run execute if entity @p[team=Team11,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"DARK BLUE","color":"dark_blue"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP2Team11=36000..36050}] run give @a[team=Team11,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP2Team11=36000..36050}] run execute if entity @p[team=Team11,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team11=36000..36050}] run tag @a[team=Team11] add ReceivedPerk4
execute if entity @p[scores={CP2Team12=7200..7250}] run execute if entity @p[team=Team12,tag=!ReceivedPerk1] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"DARK AQUA","color":"dark_aqua"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 1!","color":"gold"}]
execute if entity @p[scores={CP2Team12=7200..7250}] run effect give @a[team=Team12,tag=!ReceivedPerk1] minecraft:speed 999999 0 false
execute if entity @p[scores={CP2Team12=7200..7250}] run execute if entity @p[team=Team12,tag=!ReceivedPerk1] run playsound minecraft:ambient.basalt_deltas.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team12=7200..7250}] run tag @a[team=Team12] add ReceivedPerk1
execute if entity @p[scores={CP2Team12=14400..14450}] run execute if entity @p[team=Team12,tag=!ReceivedPerk2] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"DARK AQUA","color":"dark_aqua"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 2!","color":"gold"}]
execute if entity @p[scores={CP2Team12=14400..14450}] run effect give @a[team=Team12,tag=!ReceivedPerk2] minecraft:resistance 600 0 false
execute if entity @p[scores={CP2Team12=14400..14450}] run execute if entity @p[team=Team12,tag=!ReceivedPerk2] run playsound minecraft:ambient.crimson_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team12=14400..14450}] run tag @a[team=Team12] add ReceivedPerk2
execute if entity @p[scores={CP2Team12=28800..28850}] run execute if entity @p[team=Team12,tag=!ReceivedPerk3] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"DARK AQUA","color":"dark_aqua"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 3!","color":"gold"}]
execute if entity @p[scores={CP2Team12=28800..28850}] run effect give @a[team=Team12,tag=!ReceivedPerk3] minecraft:haste 999999 2 false
execute if entity @p[scores={CP2Team12=28800..28850}] run execute if entity @p[team=Team12,tag=!ReceivedPerk3] run playsound minecraft:ambient.warped_forest.mood master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team12=28800..28850}] run tag @a[team=Team12] add ReceivedPerk3
execute if entity @p[scores={CP2Team12=36000..36050}] run execute if entity @p[team=Team12,tag=!ReceivedPerk4] run tellraw @a ["",{"text":"TEAM ","color":"light_purple"},{"text":"DARK AQUA","color":"dark_aqua"},{"text":" HAS REACHED","color":"light_purple"},{"text":" PERK 4!","color":"gold"}]
execute if entity @p[scores={CP2Team12=36000..36050}] run give @a[team=Team12,tag=!ReceivedPerk4] minecraft:golden_apple
execute if entity @p[scores={CP2Team12=36000..36050}] run execute if entity @p[team=Team12,tag=!ReceivedPerk4] run playsound minecraft:entity.wither.spawn master @a ~ ~50 ~ 100 1 0
execute if entity @p[scores={CP2Team12=36000..36050}] run tag @a[team=Team12] add ReceivedPerk4
#This file was made using the "UHC-Datapack-Creator" current version: 3.0.
#If you want to make any changes to this file please contact the UHC-Committee member: Perfidy.
#He will know how to change it. (Without messing things up...)