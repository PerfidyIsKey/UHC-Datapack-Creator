execute as @a[tag=Traitor] run title @s actionbar ["",{"text":">>> ","color":"gold"},{"text":"Traitor Faction: ","color":"light_purple"},{"selector":"@a[tag=Traitor]"},{"text":" <<<","color":"gold"}]
execute if entity @p[scores={Admin=1,Victory=1}] run function uhc:traitor_check
#This file was made using the "UHC-Datapack-Creator" current version: 3.0.
#If you want to make any changes to this file please contact the UHC-Committee member: Perfidy.
#He will know how to change it. (Without messing things up...)