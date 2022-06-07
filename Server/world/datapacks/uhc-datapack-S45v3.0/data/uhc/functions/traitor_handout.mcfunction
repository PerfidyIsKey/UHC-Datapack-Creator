tag Kalazniq add DontMakeTraitor
tag ThurianBohan add DontMakeTraitor
tag PerfidyIsKey add DontMakeTraitor
tag Xx__HexGamer__xX add DontMakeTraitor
tag Bobdafish add DontMakeTraitor
tag @r[limit=1,tag=!DontMakeTraitor,scores={Rank=35..},gamemode=!spectator] add Traitor
execute if entity @p[tag=Traitor,team=Team0] run tag @a[team=Team0] add DontMakeTraitor
execute if entity @p[tag=Traitor,team=Team1] run tag @a[team=Team1] add DontMakeTraitor
execute if entity @p[tag=Traitor,team=Team2] run tag @a[team=Team2] add DontMakeTraitor
execute if entity @p[tag=Traitor,team=Team3] run tag @a[team=Team3] add DontMakeTraitor
execute if entity @p[tag=Traitor,team=Team4] run tag @a[team=Team4] add DontMakeTraitor
execute if entity @p[tag=Traitor,team=Team5] run tag @a[team=Team5] add DontMakeTraitor
execute if entity @p[tag=Traitor,team=Team6] run tag @a[team=Team6] add DontMakeTraitor
execute if entity @p[tag=Traitor,team=Team7] run tag @a[team=Team7] add DontMakeTraitor
execute if entity @p[tag=Traitor,team=Team8] run tag @a[team=Team8] add DontMakeTraitor
execute if entity @p[tag=Traitor,team=Team9] run tag @a[team=Team9] add DontMakeTraitor
execute if entity @p[tag=Traitor,team=Team10] run tag @a[team=Team10] add DontMakeTraitor
execute if entity @p[tag=Traitor,team=Team11] run tag @a[team=Team11] add DontMakeTraitor
execute if entity @p[tag=Traitor,team=Team12] run tag @a[team=Team12] add DontMakeTraitor
tag @r[limit=1,tag=!DontMakeTraitor,scores={Rank=35..},gamemode=!spectator] add Traitor
execute as @a[tag=Traitor] run tellraw @s ["",{"text":"You feel like betrayal today. You have become a Traitor. Your faction consists of: ","italic":true,"color":"red"},{"selector":"@a[tag=Traitor]","italic":true,"color":"red"},{"text":".","italic":true,"color":"red"}]
title @a title ["",{"text":"A Traitor Faction","bold":true,"color":"red"}]
title @a subtitle ["",{"text":"has been founded!","bold":true,"color":"dark_red"}]
setblock 11 -62 0 minecraft:redstone_block destroy
function uhc:traitor_check
#This file was made using the "UHC-Datapack-Creator" current version: 3.0.
#If you want to make any changes to this file please contact the UHC-Committee member: Perfidy.
#He will know how to change it. (Without messing things up...)