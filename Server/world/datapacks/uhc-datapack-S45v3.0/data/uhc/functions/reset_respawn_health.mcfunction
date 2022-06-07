execute if entity @p[scores={MinHealth=1..2}] run attribute @p[tag=Respawn] generic.max_health base set 1
execute if entity @p[scores={MinHealth=3..4}] run attribute @p[tag=Respawn] generic.max_health base set 2
execute if entity @p[scores={MinHealth=5..6}] run attribute @p[tag=Respawn] generic.max_health base set 3
execute if entity @p[scores={MinHealth=7..8}] run attribute @p[tag=Respawn] generic.max_health base set 4
execute if entity @p[scores={MinHealth=9..10}] run attribute @p[tag=Respawn] generic.max_health base set 5
execute if entity @p[scores={MinHealth=11..12}] run attribute @p[tag=Respawn] generic.max_health base set 6
execute if entity @p[scores={MinHealth=13..14}] run attribute @p[tag=Respawn] generic.max_health base set 7
execute if entity @p[scores={MinHealth=15..16}] run attribute @p[tag=Respawn] generic.max_health base set 8
execute if entity @p[scores={MinHealth=17..18}] run attribute @p[tag=Respawn] generic.max_health base set 9
execute if entity @p[scores={MinHealth=19..20}] run attribute @p[tag=Respawn] generic.max_health base set 10
effect give @p[tag=Respawn] health_boost 1 0
effect clear @p[tag=Respawn] health_boost
attribute @p[tag=Respawn] generic.max_health base set 20
tag @p[tag=Respawn] remove Respawn
#This file was made using the "UHC-Datapack-Creator" current version: 3.0.
#If you want to make any changes to this file please contact the UHC-Committee member: Perfidy.
#He will know how to change it. (Without messing things up...)