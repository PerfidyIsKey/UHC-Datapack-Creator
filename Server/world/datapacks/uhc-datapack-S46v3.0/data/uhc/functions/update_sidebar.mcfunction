scoreboard players add @p[scores={Admin=1}] SideDum 1
execute if entity @p[scores={SideDum=100}] run scoreboard objectives setdisplay sidebar Time
execute if entity @p[scores={SideDum=200}] run scoreboard objectives setdisplay sidebar Hearts
execute if entity @p[scores={SideDum=300}] run scoreboard objectives setdisplay sidebar Apples
execute if entity @p[scores={SideDum=400}] run scoreboard objectives setdisplay sidebar Mining
execute if entity @p[scores={SideDum=500}] run scoreboard objectives setdisplay sidebar Kills
execute if entity @p[scores={SideDum=501}] run scoreboard players reset @p[scores={Admin=1}] SideDum
scoreboard players set @a[scores={Mining=1..}] Mining 0
execute as @a run function uhc:update_mine_count
function uhc:update_min_health
#This file was made using the "UHC-Datapack-Creator" current version: 3.0.
#If you want to make any changes to this file please contact the UHC-Committee member: Perfidy.
#He will know how to change it. (Without messing things up...)