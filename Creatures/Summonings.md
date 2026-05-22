> **AI-generated** — 2026-05-22

# Summonings

Summoned entities that assist players in combat. They cannot be attacked by players (`canBeSeenAsEnemy = false`), can only be removed via `/kill` or when their owner dies. Summoned via Summon Staves, consume summon slots (cost), and benefit from enchantments.

Common properties:
- **Invulnerable** (`isPickable = false`, `hurt` only accepts `GENERIC_KILL`)
- **Fire immune**
- **No fall damage**
- **Follow owner**, teleport when too far
- **Auto-discard** when owner dies
- **PartEntity targeting** (can attack multi-segment boss parts)

---

## Combat Minions (战斗召唤物)

### Finch (雀宝宝)

- Attributes
  - Summon Item: Finch Staff (`finch_staff`)
  - Cost: 1
  - Summon Key: 2
  - Flying: true (FlyingMoveControl)
  - Follow Range: 32
  - Move Speed: 0.7
  - Attack Knocback: 0

- Behavior
  - **Attack Goal:** Hovers near target, aligns facing direction, then dashes in for contact damage
  - Cooldown: 10 ticks between attacks
  - Constant vertical oscillation (`sin(tickCount * 0.5) * 0.03`)
  - Uses FlyingPathNavigation

---

### Slime (史莱姆宝宝)

- Attributes
  - Summon Item: Slime Staff (`slime_staff`)
  - Cost: 1
  - Summon Key: 5
  - Follow Range: 32
  - Move Speed: 0.7
  - Attack Knocback: 0
  - Base Jump: 0.5, Enhance Jump: 1.0

- Behavior
  - **Jump Attack Goal:** Jumps toward target with slime physics, enhanced jump when target is above
  - **Keep Jumping Goal:** When no target, continuously jumps toward owner
  - **Fly to Owner Goal:** If distance > 25 blocks, flies (noPhysics) directly to owner
  - **Float Goal:** Jumps when in water/lava

---

### Hornet (蜜蜂宝宝)

- Attributes
  - Summon Item: Hornet Staff (`hornet_staff`)
  - Cost: 1
  - Summon Key: 8
  - Flyer: true (extends `Hornet` with `FlyMonsterPrefab.BEE_BUILDER`)
  - Follow Range: 84
  - Move Speed: 0.7

- Behavior
  - **Ranged Attack:** Fires Bee Stinger projectiles (poison II for 5s) at target
  - Follows owner when no target
  - Aiming: adjusts for PartEntity targets

---

### SculkWisp (幽匿鬼火)

- Attributes
  - Summon Item: Sculk Wisp Staff (`sculk_wisp_staff`)
  - Cost: 1
  - Summon Key: 7
  - Flyer: true
  - Follow Range: 64
  - Move Speed: 0.7
  - Range: 30 blocks, Attack cooldown: 20 ticks

- Behavior
  - **Sonic Boom Attack:** Fires sonic boom (Warden-style) at target
  - Deals magic damage, applies strong knockback (`2.5 * (1 - KB resistance)`)
  - Particle effect: `SONIC_BOOM` line
  - Uses `FlyRangeAttackSummonMob` base

---

### Imp (小鬼)

- Attributes
  - Summon Item: Imp Staff (`imp_staff`)
  - Cost: 1
  - Summon Key: 14
  - Flyer: true
  - Follow Range: 84
  - Move Speed: 0.7
  - Attack cooldown: 20 ticks, Delay: 15 ticks

- Behavior
  - **Ranged Attack:** Fires `FIRE_IMP_PROJ` fireball projectiles at target
  - Uses `FlyRangeAttackSummonMob` base with fire projectile

---

### Snow Flinx (雪狐)

- Attributes
  - Summon Item: Snow Flinx Staff (`snow_flinx_staff`)
  - Cost: 1
  - Summon Key: 7
  - Ground-based (no gravity enabled)
  - Follow Range: 32
  - Move Speed: 0.7

- Behavior
  - **Melee Attack:** Approaches target, deals contact damage
  - **Jump Over Block:** When close to target (< 5 blocks) and target is above, jumps up
  - **Dash:** On successful hit, dashes toward target (20 tick cooldown)

---

### Iron Golem (铁傀儡)

- Attributes
  - Summon Item: Iron Golem Staff (`iron_golem_staff`)
  - Cost: 1
  - Summon Key: 8
  - Size: 1.5×3.0
  - Follow Range: 32
  - Move Speed: 0.8 (base 0.6 + 0.2 modifier)

- Behavior
  - **Melee Attack:** Charges target, deals heavy knockback (vertical launch)
  - **Move Toward Target:** Follows target at 0.9 speed within 32 blocks
  - No friendly fire with owner

---

### Summon Swords (召唤剑)

剑类召唤物围绕主人飞行，序列号决定排列位置。使用 OBB 碰撞箱进行攻击。

#### Wooden Sword (木剑)

- Summon Item: Wooden Sword Staff (`summon_wooden_sword_staff`)
- Cost: 1, Key: 2
- RGB Color: `0x714C11`
- Special Effect: Poison II for 5s
- Follow Range: 40, Move Speed: 1.5

#### Stone Sword (石剑)

- Summon Item: Stone Sword Staff (`summon_stone_sword_staff`)
- Cost: 1, Key: 3
- RGB Color: `0x8E9797`
- Special Effect: Slowness II for 5s
- Follow Range: 40, Move Speed: 1.5

#### Iron Sword (铁剑)

- Summon Item: Iron Sword Staff (`summon_iron_sword_staff`)
- Cost: 1, Key: 4
- RGB Color: `0xE6F0F3`
- Special Effect: Heal 0.5 HP on hit
- Follow Range: 40, Move Speed: 1.5

#### Golden Sword (金剑)

- Summon Item: Golden Sword Staff (`summon_golden_sword_staff`)
- Cost: 1, Key: 5
- RGB Color: `0xE3D529`
- Special Effect: Set target on fire
- Follow Range: 40, Move Speed: 1.5

#### Diamond Sword (钻石剑)

- Summon Item: Diamond Sword Staff (`summon_diamond_sword_staff`)
- Cost: 1, Key: 6
- RGB Color: `0x17CFC1`
- Special Effect: Frozen (冰冻)
- Follow Range: 40, Move Speed: 1.5

#### Netherite Sword (下界合金剑)

- Summon Item: Netherite Sword Staff (`summon_netherite_sword_staff`)
- Cost: 1, Key: 7
- RGB Color: `0x8136D2`
- Special Effect: Hell Fire (地狱火)
- Follow Range: 40, Move Speed: 1.5

- **Common Behavior (所有召唤剑):**
  - **Follow Owner:** Floats behind owner in a formation (sequence-based positioning), tilts down like a cape
  - **Auto-Attack:** Aligns facing direction with target, accelerates to contact
  - **Slash Skill:** Cooldown 150 ticks — flies above target, slashes down with +30% damage boost
  - **Trail Effect:** Motion trail rendered behind the sword
  - OBB collision with `inflate(10)` for wide hit detection

#### Terraprisma (泰拉棱镜)

- Attributes
  - Summon Item: Terraprisma (`terraprisma`)
  - Cost: 1
  - Summon Key: 18
  - RGB Color: Dynamically cycles between `#1FE6C0` and `#C67C28`
  - Follow Range: 40, Move Speed: 1.5
  - Size scale: variable (up to 2× for X, 3× for Y)

- Behavior
  - All sword common behaviors plus:
  - **Slash Skill** (cooldown 120): On hit, 25-50% chance to spin X-axis (2-5 full rotations) — more spins and scale boost
  - **Rotate Skill** (cooldown 80): Z-axis spin (1080° in 10 ticks, +30% total damage), then 50% chance of Y-axis twirl (720° in 30 ticks) with Z 90° hold, 2× scale
  - **Dynamic Color:** RGB smoothly oscillates between cyan and orange
  - **Light Source:** Integrates with `veil` mod for dynamic lighting (radius 17, intensity 0.01)
  - 2× scale during certain attacks

---

### Stardust Dragon (星尘龙)

- Attributes
  - Summon Item: Stardust Dragon Staff (`stardust_dragon_staff`)
  - Cost: 1 per segment (min 1)
  - Summon Key: 1 (unlimited merge)
  - Follow Range: 40
  - Move Speed: 1.5
  - Turn Speed: 0.7 (combat) / 0.3 (wander)
  - Segment Spacing: 0.55 blocks
  - Parts: Head + segments (multi-part entity)

- Behavior
  - **Movement:** DragonMovement physics (smooth turning, inertia, gravity control)
  - **Combat:** Flies toward target's center-of-mass at speed 0.7
  - **Wander:** Flies around owner at speed 0.3
  - **Segment Physics:** Body follows head via path history buffer
  - **Merging Summon:** Using staff again adds a segment to existing dragon instead of spawning new one

---

## Utility Summons (功能召唤物)

### Chester (切斯特 / 便携箱子)

- Attributes
  - Summon Item: Chester Staff (`chester_staff`)
  - Cost: 1 (max 1)
  - Size: 1×1
  - Ground-based

- Behavior
  - **Portable Chest:** Right-click to open 27-slot inventory
  - **Global Storage:** Opens bound global chest storage
  - **Block Chest Access:** Can open specific block chests (bound blocks)
  - **Animation:** Open/close animation on interaction
  - **Sleeping:** Nighttime idle animation (daytime > 13000)
  - **Walking:** Follows owner on ground
  - Uses `SummonItem` — only one Chester per player

### Piggy Bank (飞猪存钱罐)

- Attributes
  - Summon Item: Wallet (`wallet`)
  - Cost: 1 (max 1)
  - Flying: true (FlyingMoveControl)
  - Size: 1×1

- Behavior
  - **Portable Piggy Bank:** Same as Chester but flies
  - **Flying Follow:** Hovers behind owner in the air
  - **Animation:** Constant fly animation
  - Only one Piggy Bank per player
