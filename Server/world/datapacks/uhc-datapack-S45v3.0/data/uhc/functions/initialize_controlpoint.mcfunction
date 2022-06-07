title @a subtitle {"text":"is now enabled!", "bold":true, "italic":true, "color":"light_purple"}
setblock 7 -62 0 minecraft:redstone_block replace
title @a title {"text":"Control Point 1", "bold":true, "italic":true, "color":"gold"}
bossbar set minecraft:cp1 visible true
bossbar set minecraft:cp2 visible true
setblock 6 -62 0 minecraft:bedrock replace
forceload add -30 -57 -30 -57
setblock -30 89 -57 minecraft:air replace
forceload remove -30 -57 -30 -57
setblock 15 -62 7 minecraft:redstone_block replace
setblock 15 -62 6 minecraft:redstone_block replace
setblock 15 -62 10 minecraft:redstone_block replace
gamerule doDaylightCycle false
#This file was made using the "UHC-Datapack-Creator" current version: 3.0.
#If you want to make any changes to this file please contact the UHC-Committee member: Perfidy.
#He will know how to change it. (Without messing things up...)