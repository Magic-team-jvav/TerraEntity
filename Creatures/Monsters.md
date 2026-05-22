> **AI-generated** — 2026-05-22

# Monsters (怪物)

All monsters in TerraEntity. Most use a prefab system (`AbstractPrefab`/`AttributeBuilder`) for data-driven behavior and attributes.

---

## Slimes

Size 0.6×0.6. Common attributes: jump melee, splits on damage.

| Entity | HP | ATK | ARM | Color | Notes |
|--------|----|-----|-----|-------|-------|
| `blue_slime` | 4 | 4 | 2 | `#73bcf4` | Basic slime |
| `green_slime` | 3 | 3 | 0 | `#48E920` | Basic slime |
| `purple_slime` | 5 | 5 | 6 | `#f334f8` | Basic slime |
| `red_slime` | 5 | 5 | 4 | `#f83434` | Basic slime |
| `yellow_slime` | 6 | 6 | 7 | `#f8e234` | Basic slime |
| `pink_slime` | 2 | 2 | 2 | `#FF87B3` | Drops King Slime summon (20%) |
| `black_slime` | 20 | 2 | 0 | `#7E7E7E` | Custom class |
| `dungeon_slime` | 15.6 | 15.6 | 2 | `#6d697b` | Dungeon spawn |
| `desert_slime` | 6 | 6 | 5 | `#DCC59a` | Desert |
| `jungle_slime` | 12 | 12 | 6 | `#9ae920` | Jungle |
| `ice_slime` | 5 | 5 | 4 | `#B3F0EA` | Ice biome |
| `lava_slime` | 10 | 10 | 10 | `#FFB150` | Fire immune, Nether |
| `corrupt_slime` | 28 | 28 | 20 | `#C91717` | Corruption |
| `crimslime` | 31.2 | 31.2 | 26 | `#8B4949` | Crimson |
| `tropic_slime` | 5 | 5 | 1 | `#73bcf4` | Ocean/tropic |
| `luminous_slime` | 36.4 | 36.4 | 30 | `#FFFFFF` | Glowing |
| `honey_slime` | 16 | 0 | 0 | `#f8e234` | Custom class |
| `golden_slime` | 97 | 5 | 2 | — | Valuable drops |
| `green_dumpling_slime` | 5 | 5 | 0 | `#32CD32` | Basic slime |
| `swamp_slime` | 5 | 5 | 1 | `#556B2F` | Swamp |
| `flesh_slime` | 14 | 14 | 6 | `#FF0000` | Hill of Flesh minion |
| `spiked_slime` | 7 | 7 | 5 | — | King Slime minion |
| `spiked_jungle_slime` | 15 | 15 | 8 | — | Jungle variant |
| `spiked_ice_slime` | 6 | 6 | 8 | — | Ice variant |

---

## Flying Monsters

| Entity | HP | ATK | ARM | Size | Notes |
|--------|----|-----|-----|------|-------|
| `demon_eye` | 20 | 2 | 0 | 1.1×1.1 | Variant system, drops Suspicious Looking Eye (5%) |
| `crimera` | 20 | 11 | 6 | 1.2×1.2 | Crimson, dash |
| `eater_of_souls` | 20 | 11 | 6 | 1.2×1.2 | Corruption, dash, drops Worm Food (5%) |
| `drippler` | 26 | 14 | 7 | 1.6×1.6 | Crimson, dash |
| `servant_of_cthulhu` | 10 | 3 | 1 | 1.1×1.1 | Brain of Cthulhu minion |
| `wandering_eye_fish` | 156 | 15 | 18 | 1.4×1.4 | Fly+water, fast |
| `flying_fish` | 10 | 2 | 1 | 0.9×0.9 | Ocean |
| `harpy` | 41 | 13 | 8 | 1.0×2.0 | Sky, ranged feathers |
| `demon` | 62 | 20 | 8 | 1.0×2.0 | Nether, no friction |
| `voodoo_demon` | 62 | 20 | 8 | 1.0×2.0 | Nether, drops Voodoo Doll (20%) |
| `antlion_swarmer` | 31 | 15 | 8 | 3.0×1.5 | Desert |
| `giant_antlion_swarmer` | 46 | 17 | 12 | 3.5×2.0 | Desert, larger |

---

## Hornets & Bats

| Entity | HP | ATK | ARM | Size | Notes |
|--------|----|-----|-----|------|-------|
| `hornet` | 32 | 13 | 6 | 0.8×1.8 | Ranged stinger, drops Abeemination (5%) |
| `little_hornet` | 3 | 3 | 1 | 0.4×0.4 | Passive, Queen Bee minion |
| `cave_bat` | 8 | 4 | 1 | 1.6×1.6 | Floating flight |
| `jungle_bat` | 17 | 8 | 1 | 1.6×1.6 | Jungle |
| `hell_bat` | 23 | 15 | 2 | 1.6×1.6 | Nether, fire immune, lava particles |
| `ice_bat` | 15 | 7 | 2 | 1.6×1.6 | Snowflake particles |
| `spore_bat` | 15 | 7 | 2 | 1.6×1.6 | Spore/mushroom |

---

## Land Monsters

| Entity | HP | ATK | ARM | Size | Notes |
|--------|----|-----|-----|------|-------|
| `face_monster` | 36 | 13 | 10 | 0.75×1.95 | Crimson, jump attack |
| `blood_zombie` | 39 | 10 | 8 | 0.75×1.95 | Crimson, accelerates |
| `blood_crawler` | 31 | 15 | 8 | 1.8×1.2 | Crimson spider |
| `bloody_spore` | 100 | 0 | 6 | 1.0×1.5 | Crimson, drops Bloody Spine (20%) |
| `blood_tumors` | 5 | 0 | 2 | 0.5×0.5 | Summons mobs then dies |
| `decayeder` | 10 | 6 | 6 | 1.0×1.8 | Corruption ranged, drops Worm Food (5%) |
| `spore_skeleton` | 31 | 11 | 8 | 0.65×1.85 | Spore biome |
| `spore_zombie` | 93 | 20 | 10 | 0.75×1.95 | Spore biome |
| `hat_spore_zombie` | 114 | 19 | 16 | 0.75×1.95 | Spore mushroom hat |
| `snow_flinx` | 36 | 13 | 12 | 1.25×1.25 | Snow, jump melee |
| `giant_shelly` | 26 | 9 | 12 | 1.0×1.0 | Desert/beach |
| `crawdad` | 26 | 15 | 6 | 1.0×1.0 | Desert/beach |
| `nymph` | 156 | 15 | 16 | 0.8×1.95 | Underground, passive |
| `snatcher` | 31 | 13 | 10 | 1.0×1.0 | Jungle plant |
| `man_eater` | 57 | 15 | 10 | 1.0×1.0 | Jungle plant, larger |

---

## Dungeon Skeletons (MeleeSkeleton)

All use `MeleeSkeleton`. Bones drop Skeletron summon (5%).

| Entity | HP | ATK | ARM | Size |
|--------|----|-----|-----|------|
| `base_bones` | 41 | 13 | 2 | 0.65×1.85 |
| `anger_bones` | 41 | 13 | 8 | 0.65×1.85 |
| `short_bones` | 37 | 12 | 7 | 0.55×1.65 |
| `big_bones` | 52 | 17 | 9 | 0.85×2.25 |
| `big_anger_bones` | 36 | 17 | 6 | 0.9×2.4 |
| `big_muscle_anger_bones` | 36 | 14 | 12 | 0.95×2.45 |
| `big_helmet_anger_bones` | 62 | 12 | 14 | 1.0×2.6 |
| `undead_viking` | 36 | 12 | 10 | 1.0×2.6 |

---

## Ranged Casters

| Entity | HP | ATK | ARM | Size | Projectile |
|--------|----|-----|-----|------|-----------|
| `dark_caster` | 26 | 10 | 2 | 0.65×1.85 | `DARK_CASTER_PROJ` |
| `goblin_sorcerer` | 20 | 10 | 2 | 0.65×1.85 | `DARK_CASTER_PROJ` |
| `fire_imp` | 36 | 15 | 16 | 0.65×1.0 | `FIRE_IMP_PROJ` |

---

## Wall-Phasing

| Entity | HP | ATK | ARM | Size |
|--------|----|-----|-----|------|
| `cursed_skull` | 21 | 18 | 6 | 1.0×1.0 |
| `ghost` | 26 | 8 | 4 | 1.0×1.8 |
| `meteor_head` | 13 | 21 | 6 | 1.0×1.0 |

---

## Worms

All multi-segment, no gravity.

| Entity | HP | ATK | ARM | Notes |
|--------|----|-----|-----|-------|
| `devourer` | 52 | 8 | 2 | Corruption surface |
| `tomb_crawler` | 16 | 4 | 2 | Desert |
| `giant_worm` | 31 | 9 | 3 | Underground |
| `leech` | 36 | 10 | 4 | Crimson, Hill of Flesh minion |
| `bone_serpent` | 156 | 18 | 12 | Nether, fire immune |
| `wither_bone_serpent` | 186 | 22 | 15 | Nether, harder variant |

---

## Goblin Army (HumanoidMonster)

All use `HumanoidMonster` with different equipment.

| Entity | HP | ATK | ARM | Weapon |
|--------|----|-----|-----|--------|
| `goblin_peon` | 31 | 6 | 4 | None |
| `goblin_thief` | 41 | 10 | 6 | None |
| `goblin_warrior` | 57 | 13 | 8 | Stone Sword |
| `goblin_archer` | 41 | 11 | 6 | Bow |
| `goblin_scout` | 41 | 10 | 6 | None |
| `anger_goblin` | 220 | 15 | 0 | Golden Sword |
| `goblin_sorcerer` | 20 | 10 | 2 | (ranged) |

---

## Mimics

| Entity | HP | ATK | ARM | Size | Class |
|--------|----|-----|-----|------|-------|
| `wooden_mimic` | 260 | 42 | 30 | 0.8×0.8 | WoodenMimic |
| `golden_mimic` | 260 | 42 | 30 | 0.8×0.8 | WoodenMimic |
| `ice_mimic` | 260 | 42 | 30 | 0.8×0.8 | WoodenMimic |
| `shadow_mimic` | 260 | 42 | 30 | 0.8×0.8 | WoodenMimic |
| `crimson_mimic` | 1820 | 47 | 34 | 1.6×1.6 | CrimsonMimic |
| `corrupt_mimic` | 1820 | 47 | 34 | 1.6×1.6 | CrimsonMimic |
| `hallowed_mimic` | 1820 | 47 | 34 | 1.6×1.6 | CrimsonMimic |
| `jungle_mimic` | 1820 | 47 | 34 | 1.6×1.6 | CrimsonMimic |

---

## Hardmode

| Entity | HP | ATK | ARM | Size | Notes |
|--------|----|-----|-----|------|-------|
| `wyvern` | 2080 | 41 | 10 | 1.0×1.0 | Multi-segment worm, sky |
| `pixie` | 78 | 28 | 20 | 1.0×1.0 | Hallow, flying |
| `possess_armor` | 135 | 28 | 10 | 1.0×2.0 | Humanoid |
| `possess_armor_void_vessel` | 1 | 28 | 0 | 1.0×2.0 | Harder variant |
| `wraith` | 83 | 33 | 0 | 1.0×2.0 | Ghost-like, wall phasing |
| `mummy` | 67 | 26 | 16 | 0.75×1.95 | Desert |
| `dark_mummy` | 93 | 32 | 18 | 0.75×1.95 | Evil desert |
| `blood_mummy` | 93 | 32 | 18 | 0.75×1.95 | Crimson desert |
| `light_mummy` | 104 | 28 | 18 | 0.75×1.95 | Hallowed desert |
| `dark_lamia` | 182 | 27 | 28 | 0.75×1.95 | Desert |
| `light_lamia` | 182 | 27 | 28 | 0.75×1.95 | Desert |
| `ghoul` | 93 | 26 | 26 | 0.75×1.95 | Desert |
| `tainted_ghoul` | 114 | 33 | 32 | 0.75×1.95 | Corrupt desert |
| `vile_ghoul` | 130 | 31 | 30 | 0.75×1.95 | Crimson desert |
| `dreamer_ghoul` | 156 | 28 | 32 | 0.75×1.95 | Hallowed desert |
| `sand_poacher` | 166 | 34 | 24 | 1.8×1.2 | Desert, burrows |
| `derpling` | 156 | 41 | 26 | 2.0×2.0 | Jungle, jump attack |
| `herpling` | 114 | 33 | 26 | 1.0×1.0 | Jungle, fast jump |

---

## Water

| Entity | HP | ATK | ARM | Size |
|--------|----|-----|-----|------|
| `piranha` | 15 | 13 | 2 | 0.5×0.5 |
| `arapaima` | 104 | 39 | 30 | 2.2×0.7 |
| `shark` | 156 | 20 | 2 | 2.5×1.0 |
| `blue_jellyfish` | 17 | 13 | 4 | 0.5×0.5 |
| `pink_jellyfish` | 36 | 15 | 6 | 0.5×0.5 |
| `green_jellyfish` | 62 | 41 | 18 | 0.5×0.5 |

---

## Hungries

| Entity | HP | ATK | ARM | Size | Notes |
|--------|----|-----|-----|------|-------|
| `the_hungry` | 87 | 15 | 16 | 1.0×1.0 | Wall of Flesh guardian |
| `hill_hungry` | 87 | 15 | 16 | 1.0×1.0 | Hill of Flesh guardian |

---

## Boss Minions

| Entity | HP | ATK | ARM | Boss | Notes |
|--------|----|-----|-----|------|-------|
| `visual_neuron` | 44 | 9 | 10 | Brain of Cthulhu | Phase 1 minion |
| `servant_of_cthulhu` | 10 | 3 | 1 | Brain of Cthulhu | Phase 2 minion |
| `little_hornet` | 3 | 3 | 1 | Queen Bee | Bee minion |
| `flesh_slime` | 14 | 14 | 6 | Hill of Flesh | Summoned by mouths |
| `leech` | 36 | 10 | 4 | Hill of Flesh | Leeches summoned by boss |
