gamerule naturalRegeneration false
gamerule doImmediateRespawn true
gamerule doPatrolSpawning false
gamerule doMobSpawning false
gamerule doWeatherCycle false
difficulty hard
defaultgamemode adventure
setworldspawn 0 151 3
scoreboard objectives add Admin dummy 
scoreboard objectives add TimDum dummy 
scoreboard objectives add TimeDum dummy "Elapsed Time"
scoreboard objectives add Time dummy "Elapsed Time"
scoreboard objectives add Time2 dummy "Elapsed Time"
scoreboard objectives add SideDum dummy 
scoreboard objectives add ControlPoint1 dummy 
scoreboard objectives add ControlPoint2 dummy 
scoreboard objectives add MSGDum1CP1 dummy 
scoreboard objectives add MSGDum2CP1 dummy 
scoreboard objectives add MSGDum1CP2 dummy 
scoreboard objectives add MSGDum2CP2 dummy 
scoreboard objectives add Highscore1 dummy 
scoreboard objectives add Highscore2 dummy 
scoreboard objectives add Hearts health 
scoreboard objectives add Apples minecraft.used:minecraft.golden_apple "Golden Apple"
scoreboard objectives add Stone minecraft.mined:minecraft.stone 
scoreboard objectives add Diorite minecraft.mined:minecraft.diorite 
scoreboard objectives add Andesite minecraft.mined:minecraft.andesite 
scoreboard objectives add Granite minecraft.mined:minecraft.granite 
scoreboard objectives add Deepslate minecraft.mined:minecraft.deepslate 
scoreboard objectives add Mining dummy "I like mining-leaderboard"
scoreboard objectives add Deaths deathCount 
scoreboard objectives add Kills playerKillCount 
scoreboard objectives add Crystal dummy 
scoreboard objectives add Quits minecraft.custom:minecraft.leave_game 
scoreboard objectives add Rank dummy 
scoreboard objectives add WorldLoad dummy 
scoreboard objectives add CollarCheck0 dummy 
scoreboard objectives add CollarCheck1 dummy 
scoreboard objectives add MinHealth dummy 
scoreboard objectives add Victory dummy 
scoreboard objectives setdisplay belowName Hearts
scoreboard objectives setdisplay list Hearts
bossbar remove minecraft:cp1
bossbar remove minecraft:cp2
bossbar add minecraft:cp1 "CP1: -30, 86, -57"
bossbar set minecraft:cp1 max 48000
bossbar add minecraft:cp2 "CP2 soon: 124, 63, 214"
bossbar set minecraft:cp2 max 48000
bossbar add minecraft:carepackage "Care Package available at x, y, z"
team add Team0
team modify Team0 color yellow
scoreboard objectives add CP1Team0 dummy
scoreboard objectives add CP2Team0 dummy
team add Team1
team modify Team1 color blue
scoreboard objectives add CP1Team1 dummy
scoreboard objectives add CP2Team1 dummy
team add Team2
team modify Team2 color red
scoreboard objectives add CP1Team2 dummy
scoreboard objectives add CP2Team2 dummy
team add Team3
team modify Team3 color dark_purple
scoreboard objectives add CP1Team3 dummy
scoreboard objectives add CP2Team3 dummy
team add Team4
team modify Team4 color dark_green
scoreboard objectives add CP1Team4 dummy
scoreboard objectives add CP2Team4 dummy
team add Team5
team modify Team5 color light_purple
scoreboard objectives add CP1Team5 dummy
scoreboard objectives add CP2Team5 dummy
team add Team6
team modify Team6 color black
scoreboard objectives add CP1Team6 dummy
scoreboard objectives add CP2Team6 dummy
team add Team7
team modify Team7 color gold
scoreboard objectives add CP1Team7 dummy
scoreboard objectives add CP2Team7 dummy
team add Team8
team modify Team8 color gray
scoreboard objectives add CP1Team8 dummy
scoreboard objectives add CP2Team8 dummy
team add Team9
team modify Team9 color aqua
scoreboard objectives add CP1Team9 dummy
scoreboard objectives add CP2Team9 dummy
team add Team10
team modify Team10 color dark_red
scoreboard objectives add CP1Team10 dummy
scoreboard objectives add CP2Team10 dummy
team add Team11
team modify Team11 color dark_blue
scoreboard objectives add CP1Team11 dummy
scoreboard objectives add CP2Team11 dummy
team add Team12
team modify Team12 color dark_aqua
scoreboard objectives add CP1Team12 dummy
scoreboard objectives add CP2Team12 dummy
fill -6 150 -6 6 156 6 minecraft:barrier
fill -5 151 -5 5 156 5 minecraft:air
setblock 0 152 -5 minecraft:oak_wall_sign[facing=south,waterlogged=false]{Color:"black",GlowingText:0b,Text1:'{"clickEvent":{"action":"run_command","value":"tp @s 5 -59 5"},"text":""}',Text2:'{"text":"Teleport to"}',Text3:'{"text":"Command Center"}',Text4:'{"text":""}'}
setblock -2 -64 -2 minecraft:structure_block[mode=load]{metadata:"",mirror:"NONE",ignoreEntities:0b,powered:0b,seed:0L,author:"?",rotation:"NONE",posX:1,mode:"LOAD",posY:1,sizeX:18,posZ:1,integrity:1.0f,showair:0b,name:"minecraft:commandcenter_s45",sizeY:31,sizeZ:18,showboundingbox:1b}
setblock -2 -63 -2 minecraft:redstone_block
fill 0 -59 1 0 -58 1 minecraft:air
#This file was made using the "UHC-Datapack-Creator" current version: 3.0.
#If you want to make any changes to this file please contact the UHC-Committee member: Perfidy.
#He will know how to change it. (Without messing things up...)