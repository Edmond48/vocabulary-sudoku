# Iteration 2

## 1. Different Devices

**User story**: As a vocabulary learner practicing at home, I want to use my tablet for Sudoku vocabulary practice, so that the words will be conveniently displayed in larger, easier to read fonts. 

**TDD example**: The sudoku game will operate on multiple devices and have larger and easier to read fonts for larger device screen sizes. (i.e. if the device screen size is larger, the sudoku board tiles are larger alongside the words and buttons.)

**Status**: Implemented

![tablet_main_menu](img/tablet_main_menu.png)

![tablet_word_list](img/tablet_word_list.png)

![tablet_word_select](img/tablet_word_select.png)

![tablet_9x9](img/tablet_9x9.png)

## 2. Orientation of device

**User story**: As a vocabulary learner taking the bus, I want to use my phone in landscape mode for Sudoku vocabulary practice, so that longer words are displayed in a larger font than standard mode.

**TDD example**: The sudoku game will be able to switch between portrait mode and landscape mode by tilting your phone 45 degrees from either position. In landscape mode, longer words are displayed with a larger font than the standard portrait mode and the text boxes/board tiles will be larger as well. The game state will not be saved when changing between orientations.

**Status**: Implemented

![landscape_main_menu](img/landscape_main_menu.png)

![landscape_word_list](img/landscape_word_list.png)

![landscape_word_select](img/landscape_word_select.png)

![land_scape_9x9_board](img/land_scape_9x9_board.png)

## 3. Different Size Sudoku Grids

**User story**: As a teacher of elementary and junior high school children, I want scaled versions of Sudoku that use 4x4 and 6x6 grids. In the 6x6 grid version, the overall grid should be divided into rectangles of six cells each (2x3).

**User story**: As a vocabulary learner who wants an extra challenging mode, I want a 12x12 version of Sudoku to play on my tablet. The overall grid should be divided into rectangles of 12 cells each (3x4).

**TDD example**: After selecting a difficulty, the user is prompted with choosing between a 4x4, 6x6, 9x9 and 12x12 version of Sudoku. (The (4x4) version of sudoku is divided into squares of four cells each (2x2), the (6x6) version of sudoku is divided into rectangles of six cells each (2x3), the (9x9) version of sudoku is divided into squares of 9 cells each (3x3), and the (12x12) version of sudoku is divided into rectangles of 12 cells each (4x3)). More words will be presented below for larger boards of sudoku (i.e. 4x4 will have 4 words while 12x12 will have 12 words).

**Status**: Implemented

![board_size_menu](img/board_size_menu.png)

![four_by_four_board](img/four_by_four_board.png)

![six_by_six_board](img/six_by_six_board.png)

![tablet_landscape_12x12](img/tablet_landscape_12x12.png)