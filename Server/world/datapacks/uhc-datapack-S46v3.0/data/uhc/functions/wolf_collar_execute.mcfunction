execute as @e[type=minecraft:wolf] store result score @s CollarCheck0 run data get entity @s Owner[0]
execute as @e[type=minecraft:wolf] store result score @s CollarCheck1 run data get entity @s Owner[1]
execute as @a[team=Team0] store result score @s CollarCheck0 run data get entity @s UUID[0]
execute as @a[team=Team0] store result score @s CollarCheck1 run data get entity @s UUID[1]
tag @a[team=Team0] add CollarCheck
execute as @e[type=wolf] if score @s CollarCheck0 = @p[tag=CollarCheck] CollarCheck0 if score @s CollarCheck1 = @p[tag=CollarCheck] CollarCheck1 run data modify entity @s CollarColor set value 4b
tag @a[team=Team0] remove CollarCheck
execute as @a[team=Team1] store result score @s CollarCheck0 run data get entity @s UUID[0]
execute as @a[team=Team1] store result score @s CollarCheck1 run data get entity @s UUID[1]
tag @a[team=Team1] add CollarCheck
execute as @e[type=wolf] if score @s CollarCheck0 = @p[tag=CollarCheck] CollarCheck0 if score @s CollarCheck1 = @p[tag=CollarCheck] CollarCheck1 run data modify entity @s CollarColor set value 3b
tag @a[team=Team1] remove CollarCheck
execute as @a[team=Team2] store result score @s CollarCheck0 run data get entity @s UUID[0]
execute as @a[team=Team2] store result score @s CollarCheck1 run data get entity @s UUID[1]
tag @a[team=Team2] add CollarCheck
execute as @e[type=wolf] if score @s CollarCheck0 = @p[tag=CollarCheck] CollarCheck0 if score @s CollarCheck1 = @p[tag=CollarCheck] CollarCheck1 run data modify entity @s CollarColor set value 14b
tag @a[team=Team2] remove CollarCheck
execute as @a[team=Team3] store result score @s CollarCheck0 run data get entity @s UUID[0]
execute as @a[team=Team3] store result score @s CollarCheck1 run data get entity @s UUID[1]
tag @a[team=Team3] add CollarCheck
execute as @e[type=wolf] if score @s CollarCheck0 = @p[tag=CollarCheck] CollarCheck0 if score @s CollarCheck1 = @p[tag=CollarCheck] CollarCheck1 run data modify entity @s CollarColor set value 10b
tag @a[team=Team3] remove CollarCheck
execute as @a[team=Team4] store result score @s CollarCheck0 run data get entity @s UUID[0]
execute as @a[team=Team4] store result score @s CollarCheck1 run data get entity @s UUID[1]
tag @a[team=Team4] add CollarCheck
execute as @e[type=wolf] if score @s CollarCheck0 = @p[tag=CollarCheck] CollarCheck0 if score @s CollarCheck1 = @p[tag=CollarCheck] CollarCheck1 run data modify entity @s CollarColor set value 13b
tag @a[team=Team4] remove CollarCheck
execute as @a[team=Team5] store result score @s CollarCheck0 run data get entity @s UUID[0]
execute as @a[team=Team5] store result score @s CollarCheck1 run data get entity @s UUID[1]
tag @a[team=Team5] add CollarCheck
execute as @e[type=wolf] if score @s CollarCheck0 = @p[tag=CollarCheck] CollarCheck0 if score @s CollarCheck1 = @p[tag=CollarCheck] CollarCheck1 run data modify entity @s CollarColor set value 6b
tag @a[team=Team5] remove CollarCheck
execute as @a[team=Team6] store result score @s CollarCheck0 run data get entity @s UUID[0]
execute as @a[team=Team6] store result score @s CollarCheck1 run data get entity @s UUID[1]
tag @a[team=Team6] add CollarCheck
execute as @e[type=wolf] if score @s CollarCheck0 = @p[tag=CollarCheck] CollarCheck0 if score @s CollarCheck1 = @p[tag=CollarCheck] CollarCheck1 run data modify entity @s CollarColor set value 15b
tag @a[team=Team6] remove CollarCheck
execute as @a[team=Team7] store result score @s CollarCheck0 run data get entity @s UUID[0]
execute as @a[team=Team7] store result score @s CollarCheck1 run data get entity @s UUID[1]
tag @a[team=Team7] add CollarCheck
execute as @e[type=wolf] if score @s CollarCheck0 = @p[tag=CollarCheck] CollarCheck0 if score @s CollarCheck1 = @p[tag=CollarCheck] CollarCheck1 run data modify entity @s CollarColor set value 1b
tag @a[team=Team7] remove CollarCheck
execute as @a[team=Team8] store result score @s CollarCheck0 run data get entity @s UUID[0]
execute as @a[team=Team8] store result score @s CollarCheck1 run data get entity @s UUID[1]
tag @a[team=Team8] add CollarCheck
execute as @e[type=wolf] if score @s CollarCheck0 = @p[tag=CollarCheck] CollarCheck0 if score @s CollarCheck1 = @p[tag=CollarCheck] CollarCheck1 run data modify entity @s CollarColor set value 7b
tag @a[team=Team8] remove CollarCheck
execute as @a[team=Team9] store result score @s CollarCheck0 run data get entity @s UUID[0]
execute as @a[team=Team9] store result score @s CollarCheck1 run data get entity @s UUID[1]
tag @a[team=Team9] add CollarCheck
execute as @e[type=wolf] if score @s CollarCheck0 = @p[tag=CollarCheck] CollarCheck0 if score @s CollarCheck1 = @p[tag=CollarCheck] CollarCheck1 run data modify entity @s CollarColor set value 9b
tag @a[team=Team9] remove CollarCheck
execute as @a[team=Team10] store result score @s CollarCheck0 run data get entity @s UUID[0]
execute as @a[team=Team10] store result score @s CollarCheck1 run data get entity @s UUID[1]
tag @a[team=Team10] add CollarCheck
execute as @e[type=wolf] if score @s CollarCheck0 = @p[tag=CollarCheck] CollarCheck0 if score @s CollarCheck1 = @p[tag=CollarCheck] CollarCheck1 run data modify entity @s CollarColor set value 2b
tag @a[team=Team10] remove CollarCheck
execute as @a[team=Team11] store result score @s CollarCheck0 run data get entity @s UUID[0]
execute as @a[team=Team11] store result score @s CollarCheck1 run data get entity @s UUID[1]
tag @a[team=Team11] add CollarCheck
execute as @e[type=wolf] if score @s CollarCheck0 = @p[tag=CollarCheck] CollarCheck0 if score @s CollarCheck1 = @p[tag=CollarCheck] CollarCheck1 run data modify entity @s CollarColor set value 11b
tag @a[team=Team11] remove CollarCheck
execute as @a[team=Team12] store result score @s CollarCheck0 run data get entity @s UUID[0]
execute as @a[team=Team12] store result score @s CollarCheck1 run data get entity @s UUID[1]
tag @a[team=Team12] add CollarCheck
execute as @e[type=wolf] if score @s CollarCheck0 = @p[tag=CollarCheck] CollarCheck0 if score @s CollarCheck1 = @p[tag=CollarCheck] CollarCheck1 run data modify entity @s CollarColor set value 9b
tag @a[team=Team12] remove CollarCheck
#This file was made using the "UHC-Datapack-Creator" current version: 3.0.
#If you want to make any changes to this file please contact the UHC-Committee member: Perfidy.
#He will know how to change it. (Without messing things up...)