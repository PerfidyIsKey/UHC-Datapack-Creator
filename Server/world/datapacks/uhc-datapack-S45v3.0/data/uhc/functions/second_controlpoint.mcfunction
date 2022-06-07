setblock 7 -62 0 minecraft:bedrock replace
setblock 8 -62 0 minecraft:redstone_block replace
tellraw @a ["",{"text":" ⎜ ","color":"gray"},{"text":"THE DIORITE EXPERTS UHC","color":"gold"},{"text":" ⎜ ","color":"gray"},{"text":"CONTROL POINT 2 IS NOW AVAILABLE!","color":"light_purple"},{"text":" ⎜ ","color":"gray"}]
setblock 15 -62 11 minecraft:redstone_block replace
forceload add 124 214 124 214
setblock 124 66 214 minecraft:air replace
forceload remove 124 214 124 214
bossbar set minecraft:cp2 name "CP2: 124, 63, 214 (faster capping!)"
function uhc:carepackage_anti_cp
#This file was made using the "UHC-Datapack-Creator" current version: 3.0.
#If you want to make any changes to this file please contact the UHC-Committee member: Perfidy.
#He will know how to change it. (Without messing things up...)