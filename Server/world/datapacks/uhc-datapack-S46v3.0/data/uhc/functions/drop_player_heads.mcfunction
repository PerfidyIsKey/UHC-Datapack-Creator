execute if entity @p[scores={Deaths=1}] run playsound minecraft:entity.lightning_bolt.thunder master @a ~ ~50 ~ 100 1 0
gamemode spectator @a[scores={Deaths=1},gamemode=!spectator]
scoreboard players set @a[scores={Deaths=1}] ControlPoint1 0
scoreboard players set @a[scores={Deaths=1}] ControlPoint2 0
scoreboard players set @p[scores={Admin=1}] Highscore1 1
scoreboard players set @p[scores={Admin=1}] Highscore2 1
scoreboard players set @p[scores={Admin=1}] MinHealth 20
execute if entity @p[scores={Deaths=1},tag=Traitor] run tellraw @a ["",{"text":" | ","bold":true,"color":"dark_gray"},{"text":"A TRAITOR HAS BEEN ELIMINATED","bold":true,"color":"red"},{"text":" | ","bold":true,"color":"dark_gray"},{"text":"WELL DONE","bold":true,"color":"gold"},{"text":" | ","bold":true,"color":"dark_gray"}]
execute at @p[name=Snodog627,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:Snodog627}}}
execute at @p[name=Mr9Madness,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:Mr9Madness}}}
execute at @p[name=Tiba101,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:Tiba101}}}
execute at @p[name=W0omy,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:W0omy}}}
execute at @p[name=MissTutuPrincess,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:MissTutuPrincess}}}
execute at @p[name=Kalazniq,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:Kalazniq}}}
execute at @p[name=Vladik71,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:Vladik71}}}
execute at @p[name=Smashking242,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:Smashking242}}}
execute at @p[name=Pfalz_,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:Pfalz_}}}
execute at @p[name=ThurianBohan,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:ThurianBohan}}}
execute at @p[name=ThurianBodan,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:ThurianBodan}}}
execute at @p[name=PerfidyIsKey,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:PerfidyIsKey}}}
execute at @p[name=deuce__,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:deuce__}}}
execute at @p[name=jonmo0105,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:jonmo0105}}}
execute at @p[name=TheDinoGame,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:TheDinoGame}}}
execute at @p[name=BAAPABUGGETS,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:BAAPABUGGETS}}}
execute at @p[name=Kakarot057,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:Kakarot057}}}
execute at @p[name=viccietors,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:viccietors}}}
execute at @p[name=Rayqson,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:Rayqson}}}
execute at @p[name=Xx__HexGamer__xX,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:Xx__HexGamer__xX}}}
execute at @p[name=Bobdafish,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:Bobdafish}}}
execute at @p[name=Alanaenae,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:Alanaenae}}}
execute at @p[name=jk20028,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:jk20028}}}
execute at @p[name=N_G0n,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:N_G0n}}}
execute at @p[name=SpookySpiker,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:SpookySpiker}}}
execute at @p[name=Clockweiz,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:Clockweiz}}}
execute at @p[name=Eason950116,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:Eason950116}}}
execute at @p[name=CorruptUncle,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:CorruptUncle}}}
execute at @p[name=Pimmie36,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:Pimmie36}}}
execute at @p[name=Jayroon123,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:Jayroon123}}}
execute at @p[name=PbQuinn,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:PbQuinn}}}
execute at @p[name=Vermeil_Chan,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:Vermeil_Chan}}}
execute at @p[name=Jobbo2002,scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:Jobbo2002}}}
scoreboard players reset @a[scores={Deaths=1}] Deaths
#This file was made using the "UHC-Datapack-Creator" current version: 3.0.
#If you want to make any changes to this file please contact the UHC-Committee member: Perfidy.
#He will know how to change it. (Without messing things up...)