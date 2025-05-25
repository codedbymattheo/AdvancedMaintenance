# 🛠️ AdvancedMaintenance

[![License](https://img.shields.io/github/license/codedbymattheo/AdvancedMaintenance)](LICENSE) [![](https://jitpack.io/v/codedbymattheo/advancedmaintenance.svg)](https://jitpack.io/#codedbymattheo/advancedmaintenance)      [![Downloads](https://img.shields.io/github/downloads/codedbymattheo/AdvancedMaintenance/total)](DOWNLOADS)

**AdvancedMaintenance** is Spigot plugin, which allows you to lockdown server within maintenance. <br><br>

---

## 📦 Function
  
- Activation and deactivation of maintenance mode
- 90% of customizable messages
- Including discord webhooks, SQLite integration
- Schedule your Maintenances and let players know on Discord!
- Built-in version checker, so you don't have to worry about checking it

---

## 📥 Installation

1. Download `.jar` file from [Releases](https://github.com/codedbymattheo/AdvancedMaintenance/releases).
2. Put it in your `plugins/` folder.
3. Restart your server.
4. Change `config.yml` and `messages.yml` files
5. And you are good to go!

---

## ✔ Permissions

- maintenance.bypass = With this permission you can access server in maintenance.
- maintenance.admin = With this permission you can execute every command.

---

## ✉ Commands (If you want you are not supposed to provide reason)

- /maintenance on <reason>(Enables maintenance)
- /maintenance off <reason> (Disables maintenance)
- /maintenance status (Tells you if maintenance is active or inactive)
- /maintenance schedule <time> <reason> (Time must be in minutes, so for example just 60 or 20)
- /maintenance cancel (It will cancel the schedule)
- /maintenance reload (It reloads messages.yml and config.yml)

---

## ❓ Comming Soon...

- GUI management 
- More permission separation
- Tab complete
