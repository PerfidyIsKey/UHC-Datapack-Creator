setblock 8 -62 0 minecraft:bedrock replace
tellraw @a ["",{"text":" ⎜ ","color":"gray"},{"text":"THE DIORITE EXPERTS UHC","color":"gold"},{"text":" ⎜ ","color":"gray"},{"text":"The Controlpoint has been captured!","color":"light_purple"},{"text":" ⎜ ","color":"gray"}]
fill 15 -62 3 15 -62 4 minecraft:bedrock
title @a subtitle {"text":"has been captured!", "bold":true, "italic":true, "color":"light_purple"}
title @a title {"text":"The Controlpoint", "bold":true, "italic":true, "color":"gold"}
function uhc:teams_highscore_alive_check
#This file was made using the "UHC-Datapack-Creator" current version: 3.0.
#If you want to make any changes to this file please contact the UHC-Committee member: Perfidy.
#He will know how to change it. (Without messing things up...)