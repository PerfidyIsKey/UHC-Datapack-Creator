execute if score @r[limit=1,gamemode=!spectator,team=Team0] ControlPoint1 > @p[scores={Admin=1}] Highscore1 run bossbar set minecraft:cp1 color yellow
execute if score @r[limit=1,gamemode=!spectator,team=Team0] ControlPoint2 > @p[scores={Admin=1}] Highscore2 run bossbar set minecraft:cp2 color yellow
execute if score @r[limit=1,gamemode=!spectator,team=Team1] ControlPoint1 > @p[scores={Admin=1}] Highscore1 run bossbar set minecraft:cp1 color blue
execute if score @r[limit=1,gamemode=!spectator,team=Team1] ControlPoint2 > @p[scores={Admin=1}] Highscore2 run bossbar set minecraft:cp2 color blue
execute if score @r[limit=1,gamemode=!spectator,team=Team2] ControlPoint1 > @p[scores={Admin=1}] Highscore1 run bossbar set minecraft:cp1 color red
execute if score @r[limit=1,gamemode=!spectator,team=Team2] ControlPoint2 > @p[scores={Admin=1}] Highscore2 run bossbar set minecraft:cp2 color red
execute if score @r[limit=1,gamemode=!spectator,team=Team3] ControlPoint1 > @p[scores={Admin=1}] Highscore1 run bossbar set minecraft:cp1 color purple
execute if score @r[limit=1,gamemode=!spectator,team=Team3] ControlPoint2 > @p[scores={Admin=1}] Highscore2 run bossbar set minecraft:cp2 color purple
execute if score @r[limit=1,gamemode=!spectator,team=Team4] ControlPoint1 > @p[scores={Admin=1}] Highscore1 run bossbar set minecraft:cp1 color green
execute if score @r[limit=1,gamemode=!spectator,team=Team4] ControlPoint2 > @p[scores={Admin=1}] Highscore2 run bossbar set minecraft:cp2 color green
execute if score @r[limit=1,gamemode=!spectator,team=Team5] ControlPoint1 > @p[scores={Admin=1}] Highscore1 run bossbar set minecraft:cp1 color pink
execute if score @r[limit=1,gamemode=!spectator,team=Team5] ControlPoint2 > @p[scores={Admin=1}] Highscore2 run bossbar set minecraft:cp2 color pink
execute if score @r[limit=1,gamemode=!spectator,team=Team6] ControlPoint1 > @p[scores={Admin=1}] Highscore1 run bossbar set minecraft:cp1 color white
execute if score @r[limit=1,gamemode=!spectator,team=Team6] ControlPoint2 > @p[scores={Admin=1}] Highscore2 run bossbar set minecraft:cp2 color white
execute if score @r[limit=1,gamemode=!spectator,team=Team7] ControlPoint1 > @p[scores={Admin=1}] Highscore1 run bossbar set minecraft:cp1 color white
execute if score @r[limit=1,gamemode=!spectator,team=Team7] ControlPoint2 > @p[scores={Admin=1}] Highscore2 run bossbar set minecraft:cp2 color white
execute if score @r[limit=1,gamemode=!spectator,team=Team8] ControlPoint1 > @p[scores={Admin=1}] Highscore1 run bossbar set minecraft:cp1 color white
execute if score @r[limit=1,gamemode=!spectator,team=Team8] ControlPoint2 > @p[scores={Admin=1}] Highscore2 run bossbar set minecraft:cp2 color white
execute if score @r[limit=1,gamemode=!spectator,team=Team9] ControlPoint1 > @p[scores={Admin=1}] Highscore1 run bossbar set minecraft:cp1 color white
execute if score @r[limit=1,gamemode=!spectator,team=Team9] ControlPoint2 > @p[scores={Admin=1}] Highscore2 run bossbar set minecraft:cp2 color white
execute if score @r[limit=1,gamemode=!spectator,team=Team10] ControlPoint1 > @p[scores={Admin=1}] Highscore1 run bossbar set minecraft:cp1 color white
execute if score @r[limit=1,gamemode=!spectator,team=Team10] ControlPoint2 > @p[scores={Admin=1}] Highscore2 run bossbar set minecraft:cp2 color white
execute if score @r[limit=1,gamemode=!spectator,team=Team11] ControlPoint1 > @p[scores={Admin=1}] Highscore1 run bossbar set minecraft:cp1 color white
execute if score @r[limit=1,gamemode=!spectator,team=Team11] ControlPoint2 > @p[scores={Admin=1}] Highscore2 run bossbar set minecraft:cp2 color white
execute if score @r[limit=1,gamemode=!spectator,team=Team12] ControlPoint1 > @p[scores={Admin=1}] Highscore1 run bossbar set minecraft:cp1 color white
execute if score @r[limit=1,gamemode=!spectator,team=Team12] ControlPoint2 > @p[scores={Admin=1}] Highscore2 run bossbar set minecraft:cp2 color white
execute as @r[limit=1,gamemode=!spectator] run scoreboard players operation @p[scores={Admin=1}] Highscore1 > @s ControlPoint1
execute store result bossbar minecraft:cp1 value run scoreboard players get @p[scores={Admin=1}] Highscore1
execute as @r[limit=1,gamemode=!spectator] run scoreboard players operation @p[scores={Admin=1}] Highscore2 > @s ControlPoint2
execute store result bossbar minecraft:cp2 value run scoreboard players get @p[scores={Admin=1}] Highscore2
#This file was made using the "UHC-Datapack-Creator" current version: 3.0.
#If you want to make any changes to this file please contact the UHC-Committee member: Perfidy.
#He will know how to change it. (Without messing things up...)