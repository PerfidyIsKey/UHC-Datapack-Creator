time set 0
xp set @a 0 levels
effect give @a minecraft:regeneration 1 255
effect give @a minecraft:saturation 1 255
clear @a
title @a title {"text":"Game Starting Now!", "bold":true, "italic":true, "color":"gold"}
gamemode survival @a
gamerule sendCommandFeedback false
fill 0 -62 15 0 -62 2 minecraft:redstone_block replace
fill 2 -62 0 6 -62 0 minecraft:redstone_block replace
fill 15 -62 15 9 -62 15 minecraft:bedrock
setblock 10 -62 0 minecraft:redstone_block destroy
advancement revoke @a everything
xp set @a 0 points
scoreboard players set @p[scores={Admin=1}] Victory 1
#This file was made using the "UHC-Datapack-Creator" current version: 3.0.
#If you want to make any changes to this file please contact the UHC-Committee member: Perfidy.
#He will know how to change it. (Without messing things up...)