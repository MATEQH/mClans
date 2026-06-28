<h1 align="center">⚔️ mClans</h1>

<p align="center">
  <b>A powerful, modern clan & teams plugin for your Minecraft server.</b>
</p>

<p align="center">
  <img alt="Version" src="https://img.shields.io/badge/version-0.1.2.4-blue.svg">
  <img alt="Java" src="https://img.shields.io/badge/Java-8-orange.svg">
  <img alt="Spigot" src="https://img.shields.io/badge/Spigot-1.8.8-yellow.svg">
  <img alt="License" src="https://img.shields.io/badge/license-MIT-green.svg">
</p>

---

## ✨ Overview

**mClans** is a feature-rich clan/teams plugin built for Spigot servers. Create clans,
invite your friends, manage roles, climb the leaderboards, and customize everything
through a clean configuration. With pluggable storage backends and rich integrations,
mClans fits right into any PvP or survival server.

---

## 🚀 Features

- 🏰 **Clan Management** — Create, rename, disband, and manage your own clan.
- 👥 **Roles & Permissions** — Built-in `Leader`, `Captain`, and `Member` roles with scoped command access.
- ✉️ **Invitations** — Invite, uninvite, join, and leave clans with configurable expiry times.
- 💰 **Clan Bank** — Deposit and withdraw funds via **Vault** integration.
- 🏆 **Leaderboards** — Track clan **kills** and **points** and view the server top clans.
- 🔥 **Team Fire** — Optional friendly-fire control between teammates.
- 💬 **Clan Chat** — Dedicated, fully customizable clan chat formatting.
- 🏷️ **Nametags & Tags** — **LunarClient API** integration for in-game clan tags.
- 📊 **PlaceholderAPI** — Expose clan data anywhere with placeholders.
- ⚔️ **Duels** — Integration with the Duels plugin.
- 🛠️ **Staff Tools** — Force-disband clans and manage points as a staff member.

---

## 🔌 Integrations

mClans hooks into the following plugins (all soft dependencies):

| Plugin | Purpose |
| ------ | ------- |
| [Vault](https://www.spigotmc.org/resources/vault.34315/) | Clan bank / economy |
| [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) | Placeholders & expansions |
| [LunarClient API](https://github.com/LunarClient/BukkitAPI) | In-game nametags & clan tags |
| Duels | Duel integration |

---

## 💾 Storage Backends

mClans supports multiple storage options out of the box — just set `DATABASE.TYPE` in `config.yml`:

| Type | Description |
| ---- | ----------- |
| `FLAT` | Local flat-file storage (default) |
| `MONGO` | MongoDB database |
| `SQL` | SQL database |

---

## 📥 Installation

1. Download the latest `mClans.jar`.
2. Drop it into your server's `/plugins` folder.
3. (Optional) Install **Vault**, **PlaceholderAPI**, **LunarClient-API**, and/or **Duels** for full functionality.
4. Restart your server.
5. Edit `config.yml` and `messages.yml` to your liking, then run `/clan` (or `/team`) in-game.

---

## 🎮 Commands

The main command is `/clan` with the alias `/team`.

### Player

| Command | Description |
| ------- | ----------- |
| `/clan help` | Show the help menu |
| `/clan create <name>` | Create a new clan |
| `/clan info [clan]` | View clan information |
| `/clan list` | Browse all clans |
| `/clan top` | View the clan leaderboard |
| `/clan join <clan>` | Join a clan you were invited to |
| `/clan leave` | Leave your current clan |
| `/clan invites` | View your pending invites |
| `/clan deposit <amount>` | Deposit money into the clan bank |

### Captain

| Command | Description |
| ------- | ----------- |
| `/clan invite <player>` | Invite a player |
| `/clan uninvite <player>` | Cancel an invitation |
| `/clan kick <player>` | Kick a member |
| `/clan withdraw <amount>` | Withdraw from the clan bank |

### Leader

| Command | Description |
| ------- | ----------- |
| `/clan rename <name>` | Rename the clan |
| `/clan disband` | Disband the clan |
| `/clan captains` | Manage clan captains |

### Staff

| Command | Description |
| ------- | ----------- |
| `/clan points <clan> <amount>` | Modify a clan's points |
| `/clan forcedisband <clan>` | Force-disband a clan |

---

## 🔑 Permissions

| Permission | Description | Default |
| ---------- | ----------- | ------- |
| `mclans.use` | Access to core clan commands | `true` |
| `mclans.staff` | Access to staff commands | `op` |

---

## ⚙️ Configuration

mClans ships with two configuration files:

- **`config.yml`** — clan limits, name rules, chat formats, cooldowns, storage settings, and integration toggles.
- **`messages.yml`** — fully customizable, color-coded plugin messages.

A few highlights you can tweak:

```yaml
CLAN:
  MAX_SIZE: 8
  NAME_MIN_LENGTH: 3
  NAME_MAX_LENGTH: 8
  BLOCKED_NAMES:
    - staffs
RENAME_COOLDOWN: 180000
DATABASE:
  TYPE: FLAT   # FLAT | MONGO | SQL
```
