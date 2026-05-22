> **AI-generated** — 2026-05-22

### Eye of Cthulhu

- Attributes
  - Health: 728 / scales with player count
  - Armor: 12 (Phase 1) / 0 (Phase 2)
  - Contact Damage: 4 (Phase 1) / 6 (Phase 2, ×1.5 when dashing)
  - Movement Speed: 0.5 (Phase 2 ×1.5)
  - Follow Range: 300
  - Knockback Resistance: 100%
  - XP: 1000
  - Boss Bar Color: Red

- Spawn
  - Biome: any
  - Summoning Item: Suspicious Looking Eye (suspicious_looking_eye)
    - Dropped by Demon Eye (5% chance)
    - Registered in `TEBossSummonsItems.EYE_OF_CTHULHU_SUMMONS`

- Drops
  - <img src="https://raw.githubusercontent.com/Magic-team-jvav/TerraEntity/neoforge-dev/1.21.1/src/main/resources/assets/terra_entity/textures/item/bosssummons/bloody_spine.png" alt="bloody_spine" width="24"> Brain of Cthulhu Summon (bloody_spine) — 50%
  - <img src="https://raw.githubusercontent.com/Magic-team-jvav/TerraEntity/neoforge-dev/1.21.1/src/main/resources/assets/terra_entity/textures/item/bosssummons/worm_food.png" alt="worm_food" width="24"> Eater of Worlds Summon (worm_food) — 50%
  - <img src="https://raw.githubusercontent.com/Magic-team-jvav/TerraEntity/neoforge-dev/1.21.1/src/main/resources/assets/terra_entity/textures/item/summon/slime_staff.png" alt="slime_staff" width="24"> Slime Staff — 100%
  - <img src="https://raw.githubusercontent.com/Magic-team-jvav/TerraCurio/neoforge-dev/1.21.1/src/main/resources/assets/terra_curio/textures/item/curio/shield_of_cthulhu.png" alt="shield_of_cthulhu" width="24"> Shield of Cthulhu (when `terra_curio` present without `confluence`)

- Skill (AI Behavior)

  Uses FSM skill sequencing (`addSkills`). Default params configured via `BossSkillMapDatas.EYE_OF_CTHULHU_PARAMS`.

  **Phase 1 (≥50% HP)**

  1. **Stare** — Duration: 5s (100 ticks)
     - Hover at Y+3 above target, continuously look at target
     - Spawn Demon Eye minions every 20 ticks (minions have 3 HP, no loot drops)

  2. **Dash** — Duration: 30 ticks (trigger at 20 ticks)
     - 20-tick aim pause: predict target position with slight inaccuracy
     - Dash at 2× speed (MOVE_SPEED × 2) with 1.5× damage multiplier
     - Repeats 3 times, then back to Stare

  **Phase Transition (at 50% HP)**

  3. **Switch to Phase 2** — Duration: 23 ticks
     - Play switching animation, play hurried roaring sound
     - Minion spawn CD reduced from 20 → 7 ticks
     - Armor set to 0

  **Phase 2 (<50% HP)**

  4. **Stare** — Duration: 3s (60 ticks)
     - Hover at 1.5× speed, spawn minions faster
     - **Expert:** extends duration when player within 8 blocks
     - **FTW:** at <15% HP → nearly infinite stare; spawns 2× minions

  5. **Dash** — Duration: 30 ticks (trigger at 10 ticks)
     - 10-tick aim pause, dash at 2–3× speed with 1.5× damage
     - **Enhance Dash** (Expert + <30% HP + dash count ≤ max):
       - speedFactor=3, 6× aim inaccuracy, unstable timing
       - Plays hurried roaring, enables motion blur trail
     - **Expert:** dash count = (base + missing HP %) × 1.5 (more dashes = lower HP)
     - **Normal:** always 3 dashes
     - If target < ~4.5 blocks → dash backward first
     - After all dashes → return to Phase 2 Stare; else repeat dash

- Additional
  - **Daytime Escape:** flies away in circular pattern during daytime if no target
  - **No Gravity:** true; **No Physics:** configurable
  - **Expert Damage Vulnerability:** takes +15 damage at <40% HP, +7 at <12% HP

### King Slime (史王)

- Attributes
  - Health: 728
  - Armor: 10
  - Contact Damage: 16.5
  - Follow Range: 100
  - Knockback Resistance: 100%
  - XP: 800
  - Boss Bar Color: Blue
  - Size: (HP ratio × 10) + 6, max 127

- Spawn
  - Biome: any
  - Summoning Item: Slime Crown (slime_crown)
    - Dropped by all colored slimes (1% chance), Pink Slime (20% chance)
    - Registered in `TEBossSummonsItems.KING_SLIME_SUMMONS`

- Drops
  - Code 1 Yoyo — 100%
  - Slime Staff — 33%
  - Swamp Whip — 33%
  - Slimy Saddle — 20%
  - <img src="https://raw.githubusercontent.com/Magic-team-jvav/TerraCurio/neoforge-dev/1.21.1/src/main/resources/assets/terra_curio/textures/item/curio/royal_gel.png" alt="royal_gel" width="24"> Royal Gel (when `terra_curio` present without `confluence`)

- Skill (AI Behavior)

  State machine with 3 states: Normal → Shrink → Enlarge → Normal loop.

  **Normal State:**
  - Jump toward player, speed scaled by health loss
  - On damage: spawns Blue Slimes and Spiked Slimes based on HP loss (每掉1/TOTAL_SPLITS 分裂一波)
  - Immune to suffocation damage
  - Does not hurt player during Shrink/Enlarge transitions

  **Shrink State:**
  - Shrinks over `shrinkDuration` ticks
  - Teleports near a random player (expert: teleports to circle position; non-expert: teleports behind player's view)
  - Spawns crown model entity at old position

  **Enlarge State:**
  - Expands back to max size
  - Returns to Normal state

  **Expert Mode:** at <50% HP, enters phase 2 with increased jump frequency. `TOTAL_SPLITS` and speeds scale with difficulty (Normal/Expert/Master/FTW).

---

### Eater Of Worlds (世吞)

- Attributes
  - Head: Health 54, Armor 4, Attack 11.5
  - Segment: Health 50, Armor 6, Attack 4
  - Base Move Speed: 0.5
  - Turn Speed: 2
  - Segment Count: 60 (configurable)
  - Segment Interval: 2.8
  - Proj Damage: 5
  - Shoot Interval: 200 ticks
  - XP: 30 per segment (60 body + head = 1830 total)
  - Boss Bar Color: Purple
  - No Physics: true

- Spawn
  - Biome: any
  - Summoning Item: Worm Food (worm_food)
    - Dropped by Eater of Souls, Decayer, Devourer (5% chance)
    - Registered in `TEBossSummonsItems.EATER_OF_WORLDS_SUMMONS`

- Drops
  - Iron Golem Staff — 100%
  - <img src="https://raw.githubusercontent.com/Magic-team-jvav/TerraCurio/neoforge-dev/1.21.1/src/main/resources/assets/terra_curio/textures/item/curio/worm_scarf.png" alt="worm_scarf" width="24"> Worm Scarf (when `terra_curio` present without `confluence`)

- Skill (AI Behavior)

  Multi-segment worm with FSM skill sequencing (`addSkills`). All segments share health — total HP is the sum of all alive segments. When a segment dies, new head regenerates from the next alive segment.

  **Skills:**
  1. **Wonder** — Duration: 120 ticks
     - Alternates between flying upward/downward around the target
     - Selects a random position at distance `wanderPosRadius` from target
     - Moves slowly (speed 0.4) with low turn speed
  2. **Direct** — Duration: up to 300 ticks
     - Locks onto target, accelerates to 1.1x speed (FTW: 1.5x)
     - Ends when aligned (< 22.5°) and close (< ~4.5 blocks)
  3. **Dash** — Duration: 120 ticks
     - Continuous forward movement
     - Head fires projectiles every `shootInterval` ticks (currently disabled)

  **On death:**
  - If all segments die: drops loot from main head
  - Otherwise: each dead segment drops loot individually

---

### Brain Of Cthulhu (克脑)

- Attributes
  - Health: 552
  - Armor: 14
  - Attack: 14
  - Move Speed: 0.3 (Phase 2: 0.5)
  - Knockback Resistance: 50%
  - XP: 2000
  - Boss Bar Color: Yellow
  - Minions: 20 Visual Neurons
  - No Physics: true

- Spawn
  - Biome: Crimson
  - Summoning Item: Bloody Spine (bloody_spine)
    - Dropped by Bloody Spore (20%), Blood Crawler/Crimera/etc (5%), Wandering Eye Fish (20%)
    - Registered in `TEBossSummonsItems.BRAIN_OF_CTHULHU_SUMMONS`

- Drops
  - Iron Golem Staff — 100%
  - <img src="https://raw.githubusercontent.com/Magic-team-jvav/TerraCurio/neoforge-dev/1.21.1/src/main/resources/assets/terra_curio/textures/item/curio/brain_of_confusion.png" alt="brain_of_confusion" width="24"> Brain of Confusion (when `terra_curio` present without `confluence`)

- Skill (AI Behavior)

  FSM skill sequencing (`addSkills`) with two phases.

  **Phase 1 (Invulnerable):**
  - **First Spawn** — Duration: 50 ticks (trigger at 20)
    - Spawns 20 VisualNeuron minions in a sphere around itself
    - Rises upward slowly
  - **Stare** — Duration: 200 ticks
    - Circles target at distance 10, Y+5
    - Minions attack target on interval (`minionsSummonInternal`)
    - If no minions alive, immediately advances to phase 2
  - **Fade In/Out** — Duration: 40 ticks each
    - Teleports to a random sphere position around target
    - Controls alpha fade visibility

  **Phase 2 (<50% HP or no minions):**
  - **Switch** — Duration: 15 ticks
    - Plays opening animation
    - Sets noPhysics, spawns 3 BrainFake decoys
  - **Stare** — Duration: 40 ticks
    - Moves along Bezier curve to target's side (distance 16)
  - **Dash** — Duration: 30 ticks (trigger at 10)
    - Dashes at target along Bezier curve with roar
    - At <30% HP: increases dash count to 3
    - Repeats dash `dashCount` times, then returns to Fade
  - **Fade In/Out** — Duration: 30/100 ticks
    - Teleports and repositions around target

---

### Queen Bee (蜂后)

- Attributes
  - Health: 1237
  - Armor: 8
  - Attack: 14
  - XP: 1500
  - Boss Bar Color: Yellow
  - Summon Bee Interval: 10 ticks
  - Shoot Proj Interval: 10 ticks
  - Max Minions: 10
  - Dash Speed Modifier: 2.0
  - Angry Dash Speed Modifier: 1.5
  - Dash Max Range: 15
  - No Physics: true

- Spawn
  - Biome: Jungle
  - Summoning Item: Abeemination (abeemination)
    - Dropped by Hornet (5% chance)
    - Registered in `TEBossSummonsItems.QUEEN_BEE_SUMMONS`

- Drops
  - Bee Spawn Egg — 100%
  - Honeyed Goggles — 20%
  - <img src="https://raw.githubusercontent.com/Magic-team-jvav/TerraCurio/neoforge-dev/1.21.1/src/main/resources/assets/terra_curio/textures/item/curio/hive_pack.png" alt="hive_pack" width="24"> Hive Pack (when `terra_curio` present without `confluence`)

- Skill (AI Behavior)

  FSM skill sequencing (`addSkills`). Pattern: Idle → Summon Bee → Summon Proj → [Pre-dash × 4 → Dash × 4].

  **Skills:**
  1. **First Spawn** — Duration: 50 ticks
  2. **Idle** — Duration: 50 ticks
     - Hovers toward target, maintains distance
     - If target > `dashMaxRange`, skips to dash phase
  3. **Summon Bee** — Duration: 60 ticks (trigger at 10)
     - Hovers at Y+4 above target
     - Spawns LittleHornet minions every `summonBeeInterval`
  4. **Summon Proj** — Duration: 60 ticks (trigger at 10)
     - Fires Bee Stinger projectiles (poison effect) every `summonProjInterval`
     - <30% HP: early exit
  5. **Pre-dash Idle** — Duration: 20 ticks
     - Hover above target, align position
  6. **Pre-dash** — Duration: 15 ticks
     - Stop movement, predict target direction, aim horizontally
  7. **Dash** — Duration: 50 ticks
     - Dash horizontally at `dashSpeedModifier` × move speed
     - If angry (outside Jungle biome): bonus speed
     - Stop if past `dashMaxRange`

  **Enrage:** When outside Jungle biome, gains increased dash speed.

---

### Deerclops (鹿角怪)

- Attributes
  - Health: 3094
  - Armor: 10
  - Attack: 10.4
  - Melee Damage: 10
  - Range Damage: 10 (thrown ice)
  - Attack Range: 10
  - Thrown Ice Count: 20
  - Black Hand Damage: 10
  - XP: 1500
  - Boss Bar Color: Red
  - No Gravity: false (ground-based)

- Spawn
  - Biome: Snow/Ice (夜晚)
  - Summoning Item: Deer Thing (deer_thing)
    - Registered in `TEBossSummonsItems.DEER_THING`

- Drops
  - (none defined — uses no loot table)

- Skill (AI Behavior)

  Behavior Tree with shared flag controller.

  **Alert Phase:**
  1. **Roar** — 10 ticks: play roar animation
  2. **Roaring** — 10 ticks: play roaring animation loop

  **Combat Phase (target within 20 blocks):**
  3. **Move to Target** — Approach within 7 blocks (max 30 ticks)
  4. **Ice Attack** — 15 ticks:
     - If horizontal distance ≥ attack range (10): **Ranged** — throws `thrownIceCount` ice projectiles at target
     - If target Y > Deerclops Y + 5: **Black Hand** — summons 4 Shadow Hand projectiles around target
     - Otherwise: **Melee** — creates ice pillar eruption in cone toward target

  **Out of Combat:**
  5. **Walk to Chest** — Finds nearest chest, walks to it
  6. **Destroy Chest** — Destroys chest (7 ticks animation), drops contents

  **Invulnerability:** When target is > 20 blocks away, Deerclops is immune to all damage.

  **No Target:** Random stroll.

---

### Skeletron (骷髅王)

- Attributes
  - Health: 2288
  - Armor: 10 (0 when both hands destroyed)
  - Attack: 18.2
  - Proj Damage: 6 (expert skulls)
  - Shoot Cooldown: 20 ticks
  - Acceleration: 0.07 / 0.1 / 0.1 / 0.16 (classic/expert/master/ftw)
  - Max Speed: 0.7 / 1.0 / 1.0 / 2.0
  - XP: 2000
  - Boss Bar Color: White
  - No Gravity: true
  - Hands: 2 (left + right)

- Spawn
  - Biome: any (夜晚)
  - Summoning Item: Clothier Voodoo Doll (clothier_voodoo_doll)
    - Dropped by Bones, Angry Bones, Cursed Skull, etc. (5% chance)
    - Registered in `TEBossSummonsItems.SKELETRON_SUMMONS`

- Drops
  - **Skeletron Hand drops:**
    - Bones (10-20) — 100%
    - Iron Golem Staff — 100%
    - Valor Yoyo — 100%
  - <img src="https://raw.githubusercontent.com/Magic-team-jvav/TerraCurio/neoforge-dev/1.21.1/src/main/resources/assets/terra_curio/textures/item/curio/bone_glove.png" alt="bone_glove" width="24"> Bone Glove (when `terra_curio` present without `confluence`)

- Skill (AI Behavior)

  Vanilla-style Goal system with 3 goals.

  **1. Float Goal** (default, night, phase < 267, not enraged)
  - Maintains Y+5 above target
  - Uses acceleration/max-speed physics with damping
  - 30% chance of "crazy" mode: adds random target pull
  - Randomly switches target between players

  **2. Spin Goal** (enraged/day or phase ≥ 267)
  - Charges directly at target
  - Normal: speed = 0.2 (easy) / distance-based 0.22-0.48 (expert+)
  - Enraged (daytime): speed = 1.0 (fastest)
  - Losing hands increases speed
  - Plays roar on activation

  **3. Shoot Skull Goal** (expert+, <75% HP or hand lost, not spinning)
  - Fires homing SkullProjectile at target every `shootCooldown` ticks
  - No hands → interval halved
  - FTW: interval × 0.8

  **Hands:** 2 SkeletronHand entities spawn at first spawn.
  - While both hands alive: full armor
  - Each hand lost: armor reduced, shoot speed increased

  **Enrage:** Daytime → max speed +1.0 spin mode permanently. Also gives massive attack damage bonus (999 ADD_VALUE from LibAttributes).

---

### Wall Of Flesh (血肉墙)

- Attributes
  - Health: 3096
  - Armor: 6
  - Attack: 39
  - Base Move Speed: 0.125
  - Phase 2 Multiplier: ×1.45
  - Knockback Resistance: 999
  - Boss Bar Color: Red
  - No Physics: true, No Gravity: true
  - Grid: 40 × 30 cells, spacing 15
  - Eye/Mouth Count: procedurally generated (quadtree)

- Spawn
  - Dimension: Nether
  - Summoning Item: Guide Voodoo Doll (Wall) (guide_voodoo_doll_wall, max summon range 100/80)
    - Dropped by Voodoo Demon (20% chance) — for Hill of Flesh
    - Registered in `TEBossSummonsItems.WALL_OF_FLESH_SUMMONS`

- Drops
  - Netherite Sword Staff — 100%
  - Flamarang — 100%
  - On death: obsidian frame structure (9×9×9), or demonite/crimtane brick with `confluence`

- Skill (AI Behavior)

  Continuous horizontal wall that moves in one direction (N/E/S/W).

  **Structure:**
  - Body is a 40×30 grid of cells (spacing 15) containing Eyes, Mouths, and The Hungry
  - Generated via quadtree algorithm with subdivision depth 6
  - Eyes: 45% chance, Mouths: 40% chance, Hungry: 30% chance
  - Extra mouths generated between vertically adjacent eyes
  - Max 2 mouths and 4 eyes assigned to players at a time

  **Movement:**
  - Moves forward at 0.125 speed, aligns to cardinal direction
  - Maintains height at grid center
  - Phase 2 (<50% HP): speed × 1.45
  - Removes blocks in a 9×9×9 area as it passes (replaces with obsidian/demonite frame)

  **Combat:**
  - Applies Horrified effect to players inside outer bounding box
  - Eyes: fire projectiles at assigned player
  - Mouths: deal contact damage to assigned player
  - The Hungry: guardians that orbit and attack players; respawn at 40% chance on death
  - `summonCD` = 1200 ticks

  **Despawn:** Reaches finish line (2000 blocks) → kills all Horrified players → discards

---

### The Destroyer (毁灭者)

- Attributes
  - Head: Health 23333, Armor 2, Attack 35
  - Segment: Health 23333, Armor 2, Attack 66 (head: 35)
  - Probe: Health 100, Armor 10, Attack 12
  - Base Move Speed: 1.0
  - Turn Speed: 9
  - Segment Count: 80
  - Segment Interval: 3.2
  - XP: 2000
  - Boss Bar Color: Red
  - No Physics: true, No Gravity: true

- Spawn
  - Biome: any (夜晚)
  - Summoning Item: Mechanical Worm (not yet implemented)

- Drops
  - (none defined)

- Skill (AI Behavior)

  Three-phase AI based on target Y height.

  **1. Underground Mode** (target Y < 60)
  - Head shell: **closed**
  - No lasers, no probes
  - **Drill Charge:** Track → Rev up (20 ticks, spinning + minecart sound) → Dash (30 ticks, 1.5× speed, poor turning) → Recovery (20 ticks)
  - Body roll (spiral motion) during drill

  **2. Ground Mode** (target Y 60–100)
  - **Lurk** — Burrow 20 blocks underground, approach target
  - **Jump** — Leap up at target with circular wander pattern
  - **Dive** — 45 ticks cool-down, descend

  **3. Sky Mode** (target Y > 100)
  - Head shell: **open**
  - **Hover** — Circle at Y+25 above target (60 ticks)
  - **Swoop** — Attack from side position, laser volley enabled (60 ticks)
  - **Rush** — Direct charge
  - **Barrel Roll** — Random 720° roll (visual)
  - **Laser Volley** — All probe segments fire at target every 80+random(40) ticks

  **Features:**
  - 80 segments with physics chain (snake-like movement)
  - Probe segments fire lasers in sequence
  - Half HP: texture variant change (damage visual)
  - Block collision: particle effects + screen shake
  - Immune to suffocation and fall damage

---

### The Twins — Spazmatism & Retinazer (双子魔眼)

- Attributes (controller)
  - TheTwins: Health 16770, Armor 0, Attack 0 (invisible controller)
- Attributes (Spazmatism — 魔焰眼)
  - Health: 8970, Armor: 10, Attack: 22
  - Move Speed: 1.5 (Phase 1) / 1.0 (Phase 2)
  - Dash Speed: 1.0 (Phase 1) / 2.0 (Phase 2)
  - Shoot: 5 shots, interval 20 (P1) / 33 shots, interval 3 (P2)
  - Dash: 5 dashes, interval 10, duration 10
  - Follow Distance: 7
  - XP: 1500
- Attributes (Retinazer — 激光眼)
  - Health: 7800, Armor: 10, Attack: 19
  - Move Speed: 1.0 (both phases)
  - Shoot: 5 shots, interval 20 (P1) / 6 shots, interval 20 (P2)
  - Dash: 5 dashes, interval 10, duration 5, speed 1.5
  - Follow Distance: 7
  - XP: 1500
- Boss Bar Color: Red
- No summon item yet

- Drops
  - (none defined)

- Skill (AI Behavior)

  Two independent Behavior Tree AI entities sharing one boss bar (TheTwins controller).

  **Spazmatism (魔焰眼 — Cursed Flames):**

  **Phase 1 (≥50% HP):**
  1. **Move** — 50 ticks: follow target at distance 7, speed 1.5
  2. **Shoot** — Fire `shootCount1` cursed flame projectiles at `shootInterval1` while hovering
  3. **Dash** — `dashCount1` dashes at speed `dashSpeed1`, each preceded by `dashInterval1` ticks

  **Phase 2 (<50% HP):**
  4. **Switch** — Play switching animation (30 ticks total)
  5. **Move** — Fly toward target, speed 1.0
  6. **Flame Breath** — Continuous fire breath attack (particle cone + direct damage), `shootCount2` ticks
  7. **Dash** — `dashCount2` dashes at speed `dashSpeed2`

  **Retinazer (激光眼 — Laser):**

  **Phase 1:**
  1. **Move** — Follow at distance 7, speed 1.0, Y+5 offset
  2. **Shoot** — Fire laser projectiles at target (only if within 20 blocks)
  3. **Dash** — `dashCount1` dashes

  **Phase 2:**
  4. **Switch** — Play switching animation
  5. **Hover Shoot** — Fly above target (Y+5), rapid fire
  6. **Parallel Shoot** — Fire while hovering at distance 7
  7. **Rapid Fire** — Double shot count at quarter interval

  **Combined:**
  - TheTwins controller manages the combined boss bar (total HP of both)
  - When one dies, the other continues fighting
  - When both die, TheTwins dies and drops loot
  - No target: eyes drift toward each other

---

### Skeletron Prime (机械骷髅王)

- Attributes
  - Health: 10920
  - Armor: 6
  - Attack: 21
  - Arms: 4 (SkeletronPrimePart)
  - Resource Location: `skeletron_prime`
  - Boss Bar Color: Red
  - No Gravity: true

- Spawn
  - Biome: any (夜晚)
  - Summoning Item: (not yet implemented)

- Drops
  - (none defined)

- Skill (AI Behavior)

  Behavior Tree with shared flag controller.

  **Day Mode (Enraged):**
  - Spin flag activated
  - +999 attack damage (one-hit kill)
  - Continuous charge at target at speed 2.0

  **Night Mode (Normal):**
  - **Phase 1 (Approach):** 200 ticks — hover toward target with LerpTrack physics, slow down on alignment
    - Uses SimpleTrack (π turn radius, 1.1 speed, 0.12 inertia, 2.5d drift, 0.3 lerp)
  - **Phase 2 (Spin):** activates spin flag — charge at speed 0.8 for 50 ticks
  - Continuous loop: approach → align → spin charge

  **Movement Physics:**
  - FlyingPathNavigation
  - LerpTrack: smooth movement with angle-based speed control

---

### Plantera (世纪之花)

- Attributes
  - Health: 10920
  - Armor: 36
  - Attack: 26
  - Move Speed: 0.1 / 0.2 / 0.2 (P1/P2/Enrage)
  - Acceleration: 0.1
  - Hook Range: 48, Hook Speed: 2
  - Spike Ball Interval: 22–14 (P1–Enrage), Speed 0.85, Damage 18–42
  - Seed Interval: 27–13, Speed 2.5, Damage 12–28
  - Spore Interval: 27–13, Speed 2.5, Damage 12–28
  - Tentacles: 8 (body) + 3 per hook × 3 hooks
  - XP: 2000
  - Boss Bar Color: Green
  - No Gravity: true

- Spawn
  - Biome: Underground Jungle
  - Found naturally (Plantera's Bulb — not yet implemented as item)

- Drops
  - (none defined)

- Skill (AI Behavior)

  Four Goal-based AI components.

  **1. Move Goal:**
  - Accelerates toward target with limited speed
  - Hook pull: when hooks are grabbed onto blocks, tension pulls body toward hook
  - Each hook adds spring-like force when stretched beyond hook range

  **2. Hook Goal:**
  - Every 50 ticks: fires up to 3 hooks to find occluding blocks
  - Uses line-of-sight raycast with 18 search angles (45° sweep × rotation)
  - Cannot find block → enrage
  - Every 50 ticks: retracts the furthest grabbed hook if 3 are active

  **3. Projectile Goal:**
  - Phase 1 (100%–50% HP): fires Seeds and Spike Balls, rate increases as HP drops
  - Phase 2 (<50% HP): fires Spores only (homing), rate increases
  - Enrage: fires all three at maximum rate
  - Seed: direct fire toward target
  - Spike Ball: bounces on surfaces
  - Spore: homes toward target

  **4. Tentacle Goal:**
  - Phase 2 trigger: at 50% HP, spawns `tentacleCountSelf` tentacles on body and `tentacleCountHook` on each hook
  - Tentacles respawn after 5s × (alive tentacles + 1)

  **Enrage:** Player goes outside hook range (48 blocks) → 200 ticks enrage with max fire rate.

---

### Prime Ender Dragon (世纪末影龙)

- Attributes
  - Health: 4624
  - Armor: 20
  - Attack: 32
  - Turn Speed Base: 0.7
  - Turn Speed Inertia: 0.2
  - Boss Bar Color: Red
  - No Physics: true
  - Parts: Head, Body, Tail(×3), Wings(×2)

- Spawn
  - Found naturally in The End

- Drops
  - (none defined)

- Skill (AI Behavior)

  Behavior Tree (`BTBossTwoStageRoot`) with DragonMovement physics.

  **Movement:**
  - DragonMovement: smooth flying with turn speed, inertia, and gravity control
  - Position buffer (64 frames) for tail/head interpolation

  **Skills:**
  1. **Dash to Target** — Continuously re-evaluate target position every 20 ticks, fly toward it
  2. **Laser Attack** — When aligned (angle < 30°) and close (distance < 20), fires laser from head dealing 5 magic damage + fire
     - Laser range scales with activation time (EASE_IN_QUAD curve)
  3. **Land Sweep** — 10% chance: find landing position, land for 50 ticks (sweep), take off
  4. **Dragon Fireball** — Fire EntityType.DRAGON_FIREBALL at target
  5. **Random Stroll** — Wander to random air position

  **Damage:**
  - Head: 100% damage
  - Body/Wings/Tail: 25% + min(damage, 1)
  - Always hurts by player or ALWAYS_HURTS_ENDER_DRAGONS damage type

---

### Hill Of Flesh (血肉山)

- Attributes
  - Health: 3824
  - Armor: 6
  - Attack: 1 (base)
  - Inner Radius: 14
  - Outer Radius: 75
  - Height: 100
  - Fire Pillar Damage: 14–25 (scales with difficulty)
  - Magic Damage Inner: 40, Outer: 40, Attach: 10
  - R summon Leeches: 5–8, Flesh Slimes: 5–8, Fire Pillars: 5–8
  - XP: 5000
  - Boss Bar Color: Red

- Spawn
  - Dimension: Nether
  - Summoning Item: Guide Voodoo Doll (Hill) (guide_voodoo_doll_hill, max summon range 40/15)
    - Dropped by Voodoo Demon (20% chance)
    - Registered in `TEBossSummonsItems.HILL_OF_FLESH_SUMMONS`

- Drops
  - Netherite Sword Staff — 100%
  - Flamarang — 100%

- Skill (AI Behavior)

  Stationary circular wall structure with eyes and mouths.

  **Structure:**
  - 5 eyes + 5 mouths arranged on a ring
  - Expands outward over 30 seconds from spawn
  - Inner/outer radius determines damage zones

  **Combat Goals:**
  1. **Summon Leech** — Summons Leech worms (BaseWorm) that chase players, max `summonLeechCount`
  2. **Summon Flesh Slime** — Mouths spawn Flesh Slimes from each mouth position, max `summonFleshSlimeCount`
  3. **Generate Fire Pillar** — Creates Lava Pillar projectiles at player positions, max `summonFirePillarCount`
  4. **Magic Damage** — Deals magic damage to players in inner/outer ring zones

  **Features:**
  - Eyes track and target players
  - Mouths act as summoning points for Flesh Slimes
  - Breakable blocks in path are destroyed
  - Fire immune entity tag (`FLESH_ALLIANCE`) for friendly-fire prevention

---

### Dungeon Guardian (地牢守卫)

- Attributes
  - Health: 9999
  - Armor: 9999
  - Attack: 9999 (passes armor)
  - Speed: 0.8
  - Boss Bar: none (hidden)
  - No Gravity: true

- Spawn
  - Structure: Dungeon
  - Trigger: Enter dungeon before defeating Skeletron

- Drops
  - (none — invincible, not meant to be killed)

- Skill (AI Behavior)

  Extends Skeletron's SpinGoal behavior.

  **Combat:**
  - Spin charge at target at speed 0.8
  - Always faces target
  - Attack passes armor (`PASS_ARMOR` damage type)
  - 50 tick attack delay between hits

  **Persistence:**
  - Despawns if no player within 100 blocks for 50 ticks
  - Does not save (regenerated each time)
  - Does not show boss bar or death message
