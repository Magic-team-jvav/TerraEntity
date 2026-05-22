> **AI-generated** — 2026-05-22

# Tools (工具)

### House Detector (`house_detector`)

Utility item for the NPC system. Right-click to detect house boundaries, or manage NPC housing assignments.

- **Stack Size:** 1
- **Modes** (shift+right-click to cycle):
  - **CHECK** — Right-click a block to detect house boundaries (shows debug particles)
  - **ADD** — Right-click an NPC to assign the detected house to them
  - **DELETE** — Right-click an NPC to unassign their house

---

### Pets

Pets follow the owner and provide access to storage.

#### Chester Staff (`chester_staff`)

Summons Chester, a ground-based portable chest.

- **Entity:** Chester (27-slot inventory)
- **Usage:** Right-click to open chest GUI
- **Shift+right-click:** Cycle chest mode / bind block chest
- **Max:** 1 per player

#### Wallet (`wallet`)

Summons a flying Piggy Bank.

- **Entity:** Piggy Bank (flying, same storage as Chester)
- **Usage:** Right-click to open storage
- **Flying follow:** Hovers behind owner
- **Max:** 1 per player
- **Traded by:** Zoologist (10 emeralds, level 5)

---

### Rideable (坐骑)

Mounts that the player can ride.

#### Slimy Saddle (`slimy_saddle`)

Summons a rideable slime.

- **Entity:** RideableSlime
- **Speed:** 0.5
- **Jump Strength:** 2.0
- **Gravity:** 0.12
- **Special:** Damages entities when jumping on them (5 damage), floats in water
- **Curios slot:** Mount

#### Honeyed Goggles (`honeyed_goggles`)

Summons a rideable bee (flying mount).

- **Entity:** RideableBee
- **Speed:** 0.225 (base), can fly
- **Gravity:** 0.03
- **Fly Time:** 200 ticks max, recharges on ground
- **Special:** Flying mount, dismounts if underwater
- **Curios slot:** Mount
