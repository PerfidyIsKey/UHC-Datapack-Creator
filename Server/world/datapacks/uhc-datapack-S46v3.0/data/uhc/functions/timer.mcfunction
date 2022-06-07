scoreboard players add @p[scores={Admin=1}] Time2 1
scoreboard players add @p[scores={Admin=1}] TimDum 1
execute if entity @p[scores={TimDum=20}] run scoreboard players add @p[scores={Admin=1}] TimeDum 1
execute store result score CurrentTime Time run scoreboard players get @p[scores={Admin=1}] TimeDum
execute if entity @p[scores={TimDum=20..}] run scoreboard players reset @p[scores={Admin=1}] TimDum
execute if entity @p[scores={Time2=6000}] run tellraw @a ["",{"text":"PVP IS NOT ALLOWED UNTIL DAY 2!","color":"gray"}]
execute if entity @p[scores={Time2=24000}] run tellraw @a ["",{"text":" ｜ ","color":"gray"},{"text":"THE DIORITE EXPERTS UHC","color":"gold"},{"text":" ｜ ","color":"gray"},{"text":"DAY TIME HAS ARRIVED & ETERNAL DAY ENABLED!","color":"light_purple"},{"text":" ｜ ","color":"gray"}]
function uhc:display_quotes
#This file was made using the "UHC-Datapack-Creator" current version: 3.0.
#If you want to make any changes to this file please contact the UHC-Committee member: Perfidy.
#He will know how to change it. (Without messing things up...)