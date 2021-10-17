**Requirements Specification**

**Project Name:**

‘Woke Village’ Minecraft Java Edition plugin.

**Purpose:**

Creation of a functional NPC (Non – Playable Character), which would bring more variety into the existing villager system of the game.

**Summary:**

The NPC would accomplish certain tasks, be hired for resources without having to download additional mods. These NPC's sometimes spawn in vanilla Minecraft villages or by admins executing ‘/npc create ...’ command. The NPC could trade resources based on its role (can only specialize in a single role) for a certain price. This should add some more interesting gameplay for the players while expanding NPC variation.

**Functional Requirements:**

Installation:

- Installation is really simple and quick.
- It requires only simple computer knowledge.
- No additional software needed in order to install the plugin.
- The installation does not require any permissions.

Interaction:

- Players can interact with the NPC by right clicking it.
- A simple to use inventory-like menu.
- Pick a service by pressing on it.
- Receive the goods into the inventory.

Process:

- Very easy to use, does not require any confusing actions.
- The prices of the services are shown when a user hovers on the service.
- After paying all the user needs to do is just wait for it to come back.
- Once the NPC is back, the player receives the resources and the transaction ends.

Uninstallation: 

- As simple as the installation part.
- All the user needs to do it just delete the plugin file.

**Non-Functional Requirements:**

Performance: (How fast does the system return results? How much will this performance change with higher workloads?)

- TBD.
- TBD.

Compatibility:

- Minecraft: Java Edition (version 1.17.1) game client.
- PaperMC (version 1.17.1) server client.
- Java 16 (or OpenJRE 16, or OpenJDK 16).
- Gradle (version 7.1.1 or higher).

Security:

- The plugin does not collect any sensitive user data.
- Only data collected is the NPC data, which is only saved locally.

Architectural Decisions:

- Coding language: Java.
- Build tool: Gradle.
- IDE: IntelliJ IDEA.
- Data storage: SQLite.

**Illustration of Interaction**

*Figure  SEQ Figure \\* ARABIC 1. Interaction Flowchart*


