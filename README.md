# 🎮 Conway's Game of Life — Java GUI

A polished implementation of **Conway's Game of Life**, built using Java and the Swing library. Developed as a final project for CMP 256: GUI Design and Programming at the **American University of Sharjah**, this simulation presents a dynamic cellular automaton with rich interactive features and a responsive graphical interface.

---

## 🧠 Project Summary

This project simulates the classic **Game of Life**, a zero-player game where cells live, die, or reproduce based on a set of mathematical rules. While the rules are simple, the emergent behavior can be complex and visually fascinating.

The grid-based simulation is presented with a fully interactive GUI that includes controls for stepping through generations, changing speed and scale, toggling edit mode, and loading preset shapes. The application supports both manual and automatic execution modes.

We scored **100/100** on this project.

---

## 🗂 Folder Structure

```plaintext
📁 Game-Of-Life/
├── 📁 GOF Project/ # Project Folder for Java
└── README.md # This File
````

---

## 🖥️ Core Features

- **Interactive 2D Grid**  
  Displays a zoomable, pannable grid with square cells. Live and dead cells are visually distinct and easy to edit.

- **Shape Menu**  
  Load predefined patterns such as:
  - Glider
  - Blinker
  - Beacon
  - Beehive  
  (And others…)

- **Simulation Controls**
  - `Next`: Advances one generation manually.
  - `Start/Stop`: Toggles automatic simulation mode.
  - `Speed`: Choose from Slow, Normal, or Fast.
  - `Scale`: Zoom in or out with Small, Medium, or Big.

- **Edit Mode**  
  Toggle grid interactivity — allows the user to click cells to toggle between alive/dead states.

- **Generation Counter**  
  Displays the number of generations elapsed in the current simulation.

- **Window Resizing**  
  Fully resizable GUI; the grid stays centered and cells maintain aspect ratio.

- **Mouse Drag**  
  Pan the grid by dragging with the mouse (compatible with edit mode).

- **Pattern Centering**  
  Any shape loaded is auto-centered regardless of the zoom level.

---

## 💾 Persistence Features

- **Save/Open Grid Configuration**  
  Via right-click pop-up menu using standard FileChooser dialog. Saves grid state, simulation settings, and generation count.

- **Preferences Panel**  
  Allows configuration of:
  - Default shape
  - Speed
  - Zoom
  - Optionally restore grid content on next launch
  - (Other settings also available…)

---

## 🌟 Optional Features (Partially Completed)

Some additional enhancements were explored and partially implemented, including:
- Theming (e.g., light/dark mode toggle)
- Enhanced visualization (e.g., cell status coloring)
- Help/About pop-ups
- Support for future add-ons like copy/paste and infinite zoom

These components were included to improve UX, though some remain experimental.

---

## 📐 Design & Architecture

The application was designed following the **Model-View-Controller (MVC)** architecture to ensure a clean separation of logic, data, and presentation. Proper use of **Java threading** ensures the GUI remains responsive during simulations.

GUI development followed Swing best practices and adheres to standard **Java conventions**, ensuring readable, extensible, and maintainable code.

---

## 👥 Team Contributions

Mohammed Arfan Ameen

---

## 📚 References

This simulation is inspired by the original Game of Life developed by **John Conway**, and informed by:

* [Conway’s Game of Life @ Wikipedia](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life)
* [Golly — Open Source Game of Life](http://golly.sourceforge.net/)
* In-class and textbook examples provided during CMP 256

---
