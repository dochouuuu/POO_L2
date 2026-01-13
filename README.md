# UBGarden – 2D Java Game
UBGarden is a small **2D Java game** developed as part of a **second-year (L2) Object-Oriented Programming (OOP) course in Java**. The player controls a gardener whose goal is to rescue a lost hedgehog while avoiding enemies such as bees and wasps. The game features multiple garden levels where the player must explore the environment, collect items, unlock doors, and survive hazards in order to reach the hedgehog and complete the game.

## Author

- Tran Minh Chau DO

- Léa DRION

### Game Overview
- The world is a grid of cells (grass, dirt, carrots, trees, flower bushes…).
- Bees move randomly and sting the player (life –1). After a sting, the player becomes invincible for a short time.
- The player can collect bonuses:  
  **hearts** (life), **keys** (open doors), **apples** (energy boost), **poisoned apples** (increase disease level).
- Some doors are locked and require a key.
- The game ends when the hedgehog reaches the exit or loses all lives.

### Features Implemented
- Movement and collision with obstacles  
- Energy, lives, disease level, and key management  
- Bonus pickup using double dispatch  
- Random bee movement with configurable speed  
- Loading multi‑level worlds from .properties files  
