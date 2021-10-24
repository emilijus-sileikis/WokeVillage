

# **''Woke Village'' Technical specification**

"Woke Village" introduces a collection of active NPCs. They are ordinary mortal creatures, with
health and hunger stats, further on they can have one of five roles and every role has the same
rotation of random personalities. These villagers rarely spawn in villages and can roam around the
world providing gathering services for the player. They can gather every basic resource in bulk,
but contrary to Minecraft villagers, they actually walk to their desired resource, take time to gather
it and then come back to the place where the deal happened and give those resources to the player.
We strived to create an easily accessible addition to the game, which would not force the player to
download external files. To enable the plugin, only the servers creator would require installing it
before launch and other player could just join and start playing without any issues.

## **System context:**

Our plugins environment mainly consists of three entities.

*Users* – the users are the most significant part of the project. Our plugin is designed to
communicate with the user via the ‘’Minecraft’’ in game chat and the NPC menu. Spawning
in the NPC requires using the spawn command, which is typed into the game chat. In
addition, in order to interact with the NPC, the user will have to use the inventorylike menu,
which appears once you right click on the NPC.

*Data.yml* – this is our main data storage for now, however, later on we are planning to use
SQLite for out data storage. This data file saves all the necessary data, which is required in
order to save the NPC and to reload it back into the server. This data includes the NPCs
location, UUID, skin, signature, etc.

*Paper.jar* – this is the game server, which our plugin depends on. The game server launches
the plugin and has all the necessary dependencies in order for the plugin to function as well.
These dependencies include: *io.papermc:paper-api, org.bukkit, org.spigot and *net.minecraft.server.*

![Illustration of System Context](doc/pictures/SystemContext.png)
*Figure 1 System Context*

## **Deployment:**

This project is built on more dependencies, than a regular one, because we are adding functionality
"on top" of an already existing project. In the graph (Figure 2, Graph section), we can see the
services we use. The Minecraft: Java Edition game server (later Paper) is configured and ran by
the game administrator using Paper.jar file on which this project (WokeVillage.jar) depends on
and is launched by. Paper looks for plugins folder, located in the server files directory, in which
our plugin will be installed and using JavaPlugin interface, Paper will launch it.

## **NPC Interaction (Figure 3, Graph section):**

Start means either spawning in an NPC (*/npc create*) or finding him in the wild. After this, you can
right click him (*PlayerUseUnknownEntityEvent*) to open the menu (Figure 4). Then the player can
either close it or pick a service. If a player decides to pick a service, he can then pay the NPC
(*player.getInventory().remove(Material)*) and wait the given time. After the time passes, the player
can finally receive their goods and end the transaction.

![Illustration of NPC Menu](doc/pictures/NPCMenu.png)
*Figure 4 NPC Menu*

## **Roles and Personalities:**

**Roles:**

Roles are titles, which define what type of resource gathering the NPC will be capable of. There
are five different roles and every NPC will have only one, randomly assigned when it spawns
(Figure 5, Graph section). Spawn rate of every role is the same and the determining formula is: 
*NPC spawn rate while entering a village (100%) / Number of roles (5) = 20%*

**Every role in depth:**

- **Lumberjack** - gathers wood-related resources like, Oak Logs, Birch Logs, Spruce Logs,
	Dark Oak Logs, Acacia Logs, and Jungle Logs, as well as Sticks. If the NPC gets lucky
	(1/10 chance), when gathering Oak Logs it will bring a random amount of Apples (<30)
	and when gathering Jungle Logs it will bring a random amount of Cocoa Beans (<64).
	
- **Miner** - gathers underground resources, like Stone, Coal, Iron ore, Gold ore and Gravel.
	If the NPC gets lucky (1/20 chance) while gathering any resource it will bring a random
	amount of Redstone (<20) / Lapis Lazuli (<20) / (1/40 chance) Diamond (<2) / Emerald
	(<2). Every additional resource from getting lucky is unique and does not stack (ex.: the
	NPC cannot get lucky and bring both Redstone and Lapis Lazuli).

- **Fisher** - catches resources from the water, like Raw Cod, Raw Salmon, Tropical Fish, Puffer
	Fish. If the NPC gets lucky (1/10 chance) while gathering any resource it will bring a
	random Enchanting Book (1) / Name Tag (1) / Saddle (1) / Rotten Flesh (1) / Bone (1) /
	(1/30 chance) Explorer Map (1). Every additional resource from getting lucky is unique
	and does not stack (ex.: the NPC cannot get lucky and bring both Enchanting Book and
	Name Tag).
	
- **Farmer** - gathers crops, like Wheat, Beetroot, Carrot, Potato, Melon, Pumpkin, Sugar
	Cane, Cactus, Mushroom, and Glow Berries. If the NPC gets lucky (1/3 chance), when
	gathering Wheat it will bring a random amount of Wheat Seeds (<10), when gathering
	Melon it will bring a random amount of Melon Seeds (<10), when gathering Pumpkin it
	will bring a random amount of Pumpkin Seeds (<10).

- **Hunter** - gathers wildlife items, like Beef, Pork, Chicken, Rabbit, Rabbit Hide, Leather,
	and White Wool. If the NPC gets lucky (1/15 chance), when hunting Rabbits it will bring
	Rabbit’s Foot (1), when hunting Chicken it will bring a random amount of Feathers (<10)
	or random amount of Eggs (<5).

**Personalities:**

Personalities are permanent modifiers to every NPC, which define certain service buffs
(improvements) or debuffs (deterioration). There are 6 different personality traits and every
NPC will have only one, randomly assigned when it spawns (Figure 5). Spawn rate of every personality trait is the same and the determining formula is: *NPC spawn* *rate while entering a village (100%) / Number of roles (5) / Number of personality traits (6)* *= 3.33%*

**Every personality trait in depth:**

- **Lazy** - takes a random amount of time longer to deliver your order (<240s) (without Lazy or Hardworking trait the base is 500s). Every transaction resets the previous debuff and calculates anew.

- **Greedy** - raises the price for services a random amount (>1x <2x). Every transaction resets the previous debuff and calculates anew.

- **Clumsy** - randomly raises the chance of failure for the NPC (<15%) (without Clumsy or Reliable trait the base is 5%). Every transaction resets the previous debuff and calculates anew.

- **Hardworking** - takes a random amount of time shorter to deliver your order (<240s) (without Lazy or Hardworking trait the base is 500s). Every transaction resets the previous debuff and calculates anew.

- **Generous** - lowers the price for services a random amount (>0.5x <1x). Every transaction resets the previous debuff and calculates anew.

- **Reliable** - randomly lowers the chance of failure for the NPC (<=5%) (without Clumsy or Reliable trait the base is 5%). Every transaction resets the previous debuff and calculates anew.

## **Connection Packets:**

The Minecraft: Java Edition server accepts connections from TCP (transmission control protocol) clients and communicates with them by using packets. A packet is a sequence of bytes sent over the TCP connection. The packets are responsible for nearly everything the player does, interacts, or sees on the server. So in order to spawn the NPC, make it visible to every player on the server, to make him interact able and so on, we had to implement packet sending. In the graph (Figure 6, Graph section) an NPC, spawning via packets is shown. In order to add an NPC packet, we need to send three different packets to the Client. These include the *Player Info* (two times with different parameters), *Add Player* packets. In order to make them visible to all the players online, the server needs to repeat sending these packets for every new player who joins. In order to hide the NPC nametag we need three other packets: *Set Player Team Remove, Set Player* *Team Add and Set Player Team Add Player packets.*

## **Command Execution (Figure 7, Graph section):**

Command execution splits into three different functions - *onEnable()*, *registerCommands()* and *onCommand()*.

- **onEnable()** - is responsible for everything that happens once the server is started. Initiates all the previous tasks as well as executes the *registerCommands()* function.

- **registerCommands()** - creates an instance of the *CommandManager* class which is responsible for all the commands of the plugin by using the *new CommandManager()* call. Injects the new command manager and registers a command class (*NPCCommands*) for it.

- **onCommand()** - is responsible for everything that happens when a command is executed. Combines all the arguments into one class and passes them for the execution. Then, if no exception is caught, executes the command and returns true. Otherwise, it informs the user about the bad command input and returns false, thus not executing the command.

## **Technologies and tools:**

- **Coding language:** Java (version 16).

- **Build tool:** Gradle (Minimum required version is 7.1.1).

- **IDE:** IntelliJ IDEA.

- **Data storage:** SQLite.

## **Graphs:**

![Illustration of Deployment](doc/pictures/Deployment.png)

*Figure 2 Deployment diagram*

![Illustration of Interaction](doc/pictures/Interaction_diagram.png)

*Figure 3 Interaction diagram*


![Illustration of Roles and personalities](doc/pictures/rolepersonalitygraph.png)

*Figure 5 Role and personality graph*


![Illustration of NPC spawning via packets](doc/pictures/npc-spawning-packets.png)

*Figure 6 NPC Spawning via Packets*

![Illustration of cmd-lifecycle](doc/pictures/cmd-lifecycle.png)

*Figure 7 Command Lifecycle*

