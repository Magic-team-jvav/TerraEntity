> **AI-generated** — 2026-05-22

# Whip & Tag System (鞭子与标记系统)

Whips are summoner weapons that apply a **summon tag** to enemies, causing your minions to deal bonus damage to tagged targets.

---

## Tag Mechanics

1. **Hit an enemy** with a whip → enemy is **tagged**
2. All your **minions prioritize** tagged targets
3. Minions deal **bonus mark damage** on tagged targets via the `MARK_DAMAGE` attribute

### Summon Focus Effect
- Applied by whip hits and certain abilities
- +2 flat summon damage against the affected target
- Stacks with mark damage bonus

---

## Whip Stats

Each whip has:
- **Damage** — Base summon damage
- **Mark Damage** — Bonus damage applied through minions on tagged enemies
- **Attack Speed** — Swing speed modifier
- **Cooldown** — Ticks between hits on the same target
- **Range Factor** — Range multiplier (scales with `WHIP_RANGE` attribute)

### Tag Damage Formula

```
minion_damage = baseDamage × (1 + owner.summon_damage) + owner.mark_damage + (tagged ? +2 : 0)
```

Where:
- `mark_damage` comes from the `MARK_DAMAGE` attribute
- `+2` is the Summon Focus effect when target is tagged
- `summon_damage` is the player's `SUMMON_DAMAGE` multiplier

---

## Whip Enchantments

### Whip Sweep
- **Max Level:** I
- **Effect:** Chance to deal wide-area damage around the target
- Good for crowd control

### Multi Boomerang
Not whip-specific, but applies to whips via shared enchantability.

---

## Custom Whip Paths

Whip swing animations are data-driven via `whip_path_config`. Datapacks can define:

- `path` — The whip strike animation (keyframes)
- `sweep_path` — The sweep/hitbox animation (keyframes)

See [Configuration Files](https://github.com/Magic-team-jvav/TerraEntity/wiki/Gameplay-Configuration_Files) for path format details.

---

## Summoner's Pact Enchantment

- **Applies to:** Helmets
- **Effect:** +1 minion capacity per level
- **Synergy:** More minions = more whip tag damage output

---

## Full Whip List

See [Whips](https://github.com/Magic-team-jvav/TerraEntity/wiki/Items-Weapons-Whips) for complete stats of all 13 whips.
