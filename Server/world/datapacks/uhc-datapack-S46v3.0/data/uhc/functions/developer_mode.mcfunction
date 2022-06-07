gamerule commandBlockOutput true
gamerule doDaylightCycle false
gamerule keepInventory true
gamerule doMobSpawning false
gamerule doTileDrops false
gamerule drowningDamage false
gamerule fallDamage false
gamerule fireDamage false
gamerule sendCommandFeedback true
gamerule doImmediateRespawn true
scoreboard players reset @a
fill 0 -62 15 0 -62 2 minecraft:bedrock replace
fill 2 -62 0 8 -62 0 minecraft:bedrock replace
fill 15 -62 3 15 -62 11 minecraft:bedrock replace
scoreboard players set Snodog627 Admin 1
fill 15 -62 15 9 -62 15 minecraft:redstone_block replace
setblock 11 -62 0 minecraft:bedrock destroy
setblock 10 -62 0 minecraft:bedrock destroy
bossbar set minecraft:cp name "CP1: -30, 86, -57; CP2 soon: 124, 63, 214"
forceload add -30 -57 -30 -57
forceload add 124 214 124 214
function uhc:spawn_controlpoints
forceload remove -30 -57 -30 -57
forceload remove 124 214 124 214
bossbar set minecraft:cp1 color white
bossbar set minecraft:cp1 visible false
bossbar set minecraft:cp1 players @a
bossbar set minecraft:cp2 color white
bossbar set minecraft:cp2 visible false
bossbar set minecraft:cp2 players @a
bossbar set minecraft:cp2 name "CP2 soon: 124, 63, 214"
bossbar set minecraft:carepackage visible false
bossbar set minecraft:carepackage players @a
tag @a remove Traitor
tag @a remove DontMakeTraitor
worldborder set 1500 1
team leave @a
function uhc:display_rank
scoreboard players set NightTime Time 600
scoreboard players set CarePackages Time 1200
scoreboard players set ControlPoints Time 1800
scoreboard players set TraitorFaction Time 2400
tag @a remove ReceivedPerk1
tag @a remove ReceivedPerk2
tag @a remove ReceivedPerk3
tag @a remove ReceivedPerk4
forceload add 59 142 59 142
setblock 59 76 142 air replace
forceload remove 59 142 59 142
gamemode creative @s
#This file was made using the "UHC-Datapack-Creator" current version: 3.0.
#If you want to make any changes to this file please contact the UHC-Committee member: Perfidy.
#He will know how to change it. (Without messing things up...)