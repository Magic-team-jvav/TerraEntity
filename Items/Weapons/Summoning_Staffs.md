> **AI-generated** — 2026-05-22

# Summoning Staffs (召唤法杖)

Summoning staffs summon minions that aid the player in combat. Each staff requires 1 minion slot per use. The `Summon Key` determines slot priority (lower = higher priority, 1 = highest).

**Sword Staffs** orbit the player and perform auto-attacks + skills. All sword staffs share follow range 40 and move speed 1.5.

**Minion Staffs** summon autonomous creatures with various attack styles.

| Staff | Minion | Summon Key | Special Effect |
|-------|--------|-----------|---------------|
| Finch Staff (`finch_staff`) | Finch | 2 | Contact damage |
| Slime Staff (`slime_staff`) | Slime | 5 | Jump melee |
| Hornet Staff (`hornet_staff`) | Hornet | 8 | Ranged poison stingers |
| Sculk Wisp Staff (`sculk_wisp_staff`) | SculkWisp | 7 | Sonic boom ranged |
| Imp Staff (`imp_staff`) | Imp | 14 | Fireball ranged |
| Snow Flinx Staff (`snow_flinx_staff`) | Snow Flinx | 7 | Melee + dash |
| Iron Golem Staff (`iron_golem_staff`) | Iron Golem | 8 | Tank + knockback |
| Wooden Sword Staff (`summon_wooden_sword_staff`) | Wooden Sword | 2 | Poison II |
| Stone Sword Staff (`summon_stone_sword_staff`) | Stone Sword | 3 | Slowness II |
| Iron Sword Staff (`summon_iron_sword_staff`) | Iron Sword | 4 | Heal 0.5 HP |
| Golden Sword Staff (`summon_golden_sword_staff`) | Golden Sword | 5 | Set fire |
| Diamond Sword Staff (`summon_diamond_sword_staff`) | Diamond Sword | 6 | Freeze |
| Netherite Sword Staff (`summon_netherite_sword_staff`) | Netherite Sword | 7 | Hellfire |
| Terraprisma (`terraprisma`) | Terraprisma | 18 | Dynamic color, rotation skills |
| Stardust Dragon Staff (`stardust_dragon_staff`) | Stardust Dragon | 1 | Segmented worm, merges on reuse |

**Mechanics:**
- Right-click to summon; uses 1 minion slot per cast
- Minions are invulnerable, fire-immune, auto-target enemies
- Sword staffs orbit behind the player in sequence formation
- Sword staffs perform auto-attacks (fly toward target) and skills (slash/rotate)
- Stardust Dragon merges into existing dragon (adds segment) instead of creating new one
- See [Summonings](https://github.com/Magic-team-jvav/TerraEntity/wiki/Summonings) for full minion behavior details
