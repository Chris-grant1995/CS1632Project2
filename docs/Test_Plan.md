## CS1632 Project 2: Requirement and Test case

### Requirement
Basic requirement must be followed:
1. Functional

  * FUNC-1 Two Players game: This is a game between 2 players, one player cannot play alone, no third player can involve.

  * FUNC-2 Board size : Board size is 10x10 ( horizontal is A-J, vertical is 1-10)

  * FUNC-3 Ship placement

    * FUNC-3.1 Ship placement input format: Must be valid characters (A-J, 1-10), in correct format: [A-J]:[1-10]; both upper and lowecase are allowed. For example A:3, d:7.

    * FUNC-3.2 Ship number: Each player has 5 ships, 1 in each type: carrier, battleship, cruiser, submarine, destroyer.
	
	* FUNC-3.3 Ships size: Carrier: 5 spaces, battleship: 4 spaces, cruiser: 3 spaces, submarine: 3 spaces, destroyer: 2 spaces.

    * FUNC-3.4 Ship placed in horizontal or vertical only: no diagnal ship placement is allowed

    * FUNC-3.5 Ships overlap: No parts/ spaces in any ships can overlap.

    * FUNC-3.6 Ships out of bound: Ships must be placed inside board.
  
  * FUNC-4 Player's input during game
  
	* FUNC-4.1 Player's fire must be valid: characters (A-J, 1-10), in correct format: [A-J]:[1-10]; both upper and lowecase are allowed. For example A:3, d:7.

    * FUNC-4.2 Player's turn: Player can only fire in his turn. Any input in oponent's turn must be ignored.

    * FUNC-4.3 Valid fire coordination: Player's fire coordination must be inside the board.	

    * FUNC-4.4 Duplicate fire: Player can fire a position 1 time only. Duplicate fire not allowed.
	
	* FUNC-4.5 Player quit/surrender in the middle: Player can surrender/ quit in the middle of the game. Type 'q' to quit; 's' to surrender.

  * FUNC-5 Game play	
  
    * FUNC-5.1 Ships placement must be finished before firing phase.  
    
	* FUNC-5.2 Ship sunk announcement: If all spaces in one ship are firedown, 2 players must be announced that ship has been destroyed. For example: Player 1 carrier has been destroyed.
    
    * FUNC-5.3 Ship hit/miss announcement: If a fire hit a ship, both players must be announced that fire was a hit, otherwise they should be announced it was a miss.
    
    * FUNC-5.4 Winner decision: If a player's all 5 ships has been destroyed, the game should announce a winner and finish.

    * FUNC-5.5 Time limit: Player has limited time to make decision. Place the ship: 2 min maximum. Each turn: 30 second. For any reasons (afk, game client crash, internet lost) a player cannot finish his move, he's considered to lose the match.
	
	* FUNC-5.6 Oponent ship disclosure: Player cannot see oponent's ship placement.
	
	* FUNC-5.7 Player must switch turn after every fire.

2. Non Functional

  * NONFUNC-1 Game client must run on all 3 operating system Linux/Windows/Mac.

  * NONFUNC-2 Game server must run on Ja  * language:

  * NONFUNC-2 Players can connect to server and play through network.

  
3. Optional Requirement (to make game more enjoyable; you can have more of them)
  * Random start: who start firing first will be random
  * Multiple game simultaneously: server can handles multiple games at the same time
  * No ship movement: should be a basic requirement, but it's not really necessary
  * Type 'help' to show game's rules
  * etc...
  
### Test Case
Most of the test cases are from @Team6. Thanks for your contribution. 
For simple traceability, test cases are named with same requirement function number. For example test case TC-1a and TC-1b belongs to FUNC-1; TC-Non1a belongs to NONFUNC-1
To find more bugs, for each requirement you can have  your own new test cases. Belows I list simple examples for each requirement.

------------------------------------------------------------------------------
### TC-1 Two Players game

* TC-1a: TEST-PLAYER_1_CONNECT
  * Test Case: This is a test to see what happens when the first player attempts to join
a game server.
  * Preconditions: There are no players currently in the specified game server.
  * Input Values: Player 1 will connect to the game server.
  * Execution Steps: Player 1 should launch the client program and attempt to join a
game server that is currently empty.
  * Post Conditions: Player 1 should be allowed to join the game server and should be
placed into a game. The game server should have * one player after this test. The
game should not start and should instead wait for a second player.

TC-1b: TEST-PLAYER_2_CONNECT
  * Test Case: This is a test to see what happens when a second player attempts to
join a game server that is already occupied by one other player.
  * Preconditions: There is one player currently in the specified game server.
  * Input Values: Player 2 will connect to the game server.
  * Execution Steps: Player 2 should launch the client program and attempt to join a
game server that is already occupied by one other player.
  * Post Conditions: Player 2 should be allowed to join the game server and should be
placed into a game. The game server should have * two players after this test.

------------------------------------------------------------------------------
### TC-2 Board size

TC-2a: Check board size 10x10
  * Preconditions: None
  * Input values: 2 players connected to server
  * Execution step: Open 2 game clients, wait until server accepted and game started
  * Output values: Expected a board with size 10x10 (A-J horizontal and 1-10 vertical) should be displayed in both game clients.
  * Post Condition: None

------------------------------------------------------------------------------
### TC-3.1 Ship placement valid input check

TC-3.1: Invalid input in ship placement phase	
  * Preconditions: 2 players are in ship placement phase, player 1 is placing carrier ship.
  * Input values: Player 1 input invalid coordination
  * Execution step:
    * Invalid characters: #:1, N:4
    * Invalid coordination: A:12
    * Invalid format: 12:A
  * Output values: System must display error and request Player 1 to re input coordination
  * Post Condition:
------------------------------------------------------------------------------
### TC-3.2

TC-3.2a: The server will prompt the user for each of the ships placements separately and sequentially.
  * Preconditions: The server must have * randomly matched two players.
  * Input Values: All f  * ships’ placements.
  * Execution Steps: The game will prompt the user for the first ship, then will prompt the user for the second ship after the user has entered the placement for the first and it does not violate any rules, then the third, then fourth, then the fifth.
  * Output Values: Expected output would be all ships are successfully placed without any placement violations.

------------------------------------------------------------------------------
### TC-3.3 Ships size

TC-3.3a: Correct Ship Input Test X Direction
  * Test Case: All ships must accept the given input values and be placed properly on the board
  * Preconditions:
    * It is the beginning of a player’s ship placement period
  * Input Values:
    * A1-A5 for Carrier
    * B1-B4 for Battleship
    * C1-C3 for Destroyer
    * D1-D3 for Submarine
    * E1-E2 for Patrol Boat
  * Execution Steps:
    * Input the given values for the Carrier. Check that the ship was placed correctly
    * If the ship is rejected, stop the test and mark that the failure came from the Carrier entry. End the test and test again later.
    * Input the given values for the Battleship. Check that the ship was placed correctly
    * If the ship is rejected, stop the test and mark that the failure came from the Battleship entry. End the test and test again later.
    * Input the given values for the Destroyer. Check that the ship was placed correctly
    * If the ship is rejected, stop the test and mark that the failure came from the Destroyer entry. End the test and test again later.
  * Input the given values for the Submarine. Check that the ship was placed correctly
    * If the ship is rejected, stop the test and mark that the failure came from the Submarine entry. End the test and test again later.
  * Input the given values for the Patrol ship. Check that the ship was placed correctly
    * If the ship is rejected, stop the test and mark that the failure came from the Patrol ship entry. End the test and test again later.
  * Output Values:
    * All ships should be placed correctly given the input values
    * On invalid fire, an error message stating which coordinates are
invalid.
  * Postconditions: Other Player should be given their turn to place their
ships

TC-3.3b: Correct Ship Input Test Y Direction
  * Test Case: All ships must accept the given input values and be placed
properly on the board
  * Preconditions:
    * It is the beginning of a player’s ship placement period
  * Input Values:
    * A1-E1 for Carrier
    * A2-D2 for Battleship
    * A3-C3 for Destroyer
    * A4-C4 for Submarine
    * A5-B5 for Patrol Boat
  * Execution Steps:
    * Input the given values for the Carrier. Check that the ship was
placed correctly
      * If the ship is rejected, stop the test and mark that the failure came from the Carrier entry. End the test and test again later.
    * Input the given values for the Battleship. Check that the ship was placed correctly
      * If the ship is rejected, stop the test and mark that the failure came from the Battleship entry. End the test and test again later.
    * Input the given values for the Destroyer. Check that the ship was placed correctly
      * If the ship is rejected, stop the test and mark that the failure came from the Destroyer entry. End the test and test again later.
    * Input the given values for the Submarine. Check that the ship was placed correctly
      * If the ship is rejected, stop the test and mark that the failure came from the Submarine entry. End the test and test again later.
    * Input the given values for the Patrol ship. Check that the ship was placed correctly
      * If the ship is rejected, stop the test and mark that the failure came from the Patrol ship entry. End the test and test again later.
  * Output Values:
    * All ships should be placed correctly given the input values
    * On invalid fire, an error message stating which coordinates are
  * Postconditions: Other player should be given their turn to place ships/fire on ships

------------------------------------------------------------------------------
### TC-3.4 Ship placed in horizontal or vertical only

TC-2: Ship Placement In-Bounds Diagonal Test
  * Test Case: This test will attempt to place a ship in an in-bounds diagonal direction, which is not allowed
  * Preconditions:
    * Ships are currently able to be placed
    * No ships have  been placed yet
  * Input Values:
    * F1:B5 for Carrier (J10:F10 for correct coordinate assuming first coordinates fail)
    * E1:B4 for Battleship (J9:G9 for correct coordinate assuming first coordinates fail)
    * D1:B3 for Destroyer (J8: H8 for correct coordinate assuming first coordinates fail)
    * C1:A3 for Submarine (J7:H7 for correct coordinate assuming first coordinates fail)
    * B1:A2 for Patrol Boat (J6:I6 for correct coordinate assuming first coordinates fail)
  * Execution Steps:
    * Enter First Coordinate Pair
      * If the diagonal coordinates are rejected, this ship passes the test. Input the correct coordinates
      * If the diagonal coordinates are accepted, mark this ship as a failure and continue
    * Repeat Step 1 for all diagonal coordinates provided above
  * Output Values: Game will alert player that this is an invalid ship placement, citing diagonal placement as the reason for this error
  * Postconditions: Ships will not have  been placed, and the player will be given another try to place their ship in each case (correct coordinates should be entered in this case in order to proceed with entry and testing)

------------------------------------------------------------------------------
###TC-3.5 Ships overlap

TC-3.5a: 2 Ships In Same Cell Test (Middle Edge)
  * Test Case: Tests whether one player can place 2 ships in the same cell,
when placing the edge of the ship being placed on the middle of the ship
already placed.
  * Preconditions:
    * The game is in the ship placement phase.
    * Player 0 has carrier placed A1-A5.
  * Input Values:
    * A3-D3
  * Execution Steps:
  * Output Values:
    * An error message stating that player 0’s battleship may not be placed there.
  * Postconditions:
    * Player 0 is able to keep placing ships.
    * Player 0 only has their carrier placed A1-A5.

TC-3.5b: 2 Ships In Same Cell Test (Edge Edge)
  * Test Case: Tests whether one player can place 2 ships in the same cell, when placing the edge of the ship being placed on the edge of the ship already placed.
  * Preconditions:
    * The game is in the ship placement phase.
    * Player 0 has battleship placed A1-A4.
  * Input Values:
    * A1-C1
  * Execution Steps:
    * Place player 0’s submarine A1-C1.
  * Output Values:
  * An error message stating that player 0’s submarine may not be
placed there.
  * Postconditions:
    * Player 0 is able to keep placing ships.
    * Player 0 only has their battleship placed A1-A4.

TC-3.5c: 2 Ships In Same Cell Test (Middle Middle)
  * Test Case: Tests whether one player can place 2 ships in the same cell, when placing the middle of the ship being placed on the middle of the ship already placed.
  * Preconditions:
    * The game is in the ship placement phase.
    * Player 0 has submarine placed B1-B3.
  * Input Values:
    * A2-D2
  * Execution Steps:
    * Place player 0’s battleship A2-D2
  * Output Values:
    * An error message stating that player 0’s battleship may not be placed there.
  * Postconditions:
    * Player 0 is able to keep placing ships.
    * Player 0 only has their submarine placed B1-B3.

TC-3.5d: 2 Ships In Same Cell Test (on top)
  * Test Case: Tests whether one player can place 2 ships in the same cell, when placing a ship entirely on top of another ship.
  * Preconditions:
    * The game is in the ship placement phase.
    * Player 0 has carrier placed A1-A5.
  * Input Values:
    * A1-B2
  * Execution Steps:
    * Place player 0’s patrol boat A1-A2
  * Output Values:
    * An error message stating that player 0’s battleship may not be placed there.
  * Postconditions:
    * Player 0 is able to keep placing ships.
    * Player 0 only has their carrier placed A1-A5

------------------------------------------------------------------------------
### TC-3.6 Ships out of bound

TC-3a: Ship Placement Out of Bounds Diagonal Left Side Test
  * Test Case: This test will attempt to place a ship in an out-of-bounds diagonal direction
  * Preconditions:
    * Ships are currently able to be placed
  * Input Values:
    * D3 : H(-1) for Carrier diagonal placement (J10:F10 for correct coordinate assuming first coordinates fail)
    * D2 : G(-1) for Battleship diagonal placement (J9:G9 for correct coordinate assuming first coordinates fail)
    * D1 : F(-1) for Destroyer diagonal placement (J8: H8 for correct coordinate assuming first coordinates fail)
    * C1 : E(-1) for Submarine diagonal placement (J7:H7 for correct coordinate assuming first coordinates fail)
    * B1 : C0 for Patrol Boat diagonal placement (J6:I6 for correct coordinate assuming first coordinates fail)
  * Execution Steps
    * Enter First Coordinate provided for the first ship
    * Ensure that the game notifies the player of the error, citing “Out of Bounds” as the reason for the error
      * Make note of ship type if the diagonal coordinates do not provide an error, as this is a problem
    * Enter the valid coordinate for that ship to move on to the next ship type
    * Repeat Step 1 through 4 with the next listed ship type
  * Output Values: Game will alert player that this is an invalid ship placement, citing out of bounds as the reason for this error
  * Postconditions: Ships will only have  been placed by entering the “correct coordinates” provided above. The game will then move to the next player’s ship placement phase (or if this is the second player, to the first player’s firing phase)
    * TC-3b: Ship Placement Out of Bounds Diagonal Bottom Side Test
    * Test Case: This test will attempt to place a ship in an out-of-bounds diagonal direction
  * Preconditions:
    * Ships are currently able to be placed
  * Input Values:
    * J5 : N1 for Carrier diagonal placement (A1:E1 for correct coordinate assuming first coordinates fail)
    * J4 : M1 for Battleship diagonal placement (A2:D2 for correct coordinate assuming first coordinates fail)
    * J3 : L1 for Destroyer diagonal placement (A3 : C3 for correct coordinate assuming first coordinates fail)
    * J2 : L0 for Submarine diagonal placement (A4:C4 for correct coordinate assuming first coordinates fail)
    * J1 : K0 for Patrol Boat diagonal placement (A5:B5 for correct coordinate assuming first coordinates fail)
  * Execution Steps
    * Enter First Coordinate provided for the first ship
    * Ensure that the game notifies the player of the error, citing “Out of Bounds” as the reason for the error
      * Make note of ship type if the diagonal coordinates do not provide an error, as this is a problem
    * Enter the valid coordinate for that ship to move on to the next ship type
    * Repeat Step 1 through 4 with the next listed ship type
  * Output Values: Game will alert player that this is an invalid ship placement, citing out of bounds as the reason for this error
  * Postconditions: Ships will only have * been placed by entering the “correct coordinates” provided above. The game will then move to the next player’s ship placement phase (or if this is the second player, to the first player’s firing phase)

------------------------------------------------------------------------------
### TC-4.1 Player's fire must be valid
	
TC-4.1: Invalid input in ship placement phase	
  * Preconditions: 2 players are in ship placement phase, player 1 is placing carrier ship.
  * Input values: Player 1 input invalid coordination
  * Execution step:
    * Invalid characters: #:1, N:4
    * Invalid coordination: A:12
    * Invalid format: 12:A
  * Output values: System must display error and request Player 1 to re input fire coordination
  * Post Condition:

------------------------------------------------------------------------------
### TC-4.2 Player's turn

TC-4.2: Player can only fire in his turn
  * Preconditions: Player 1's turn to fire
  * Input values: Player 2 input valid fire coordination 
  * Execution step: Player 2 input A:2
  * Output values: Player 2 game client should display error: "Not your turn, please wait". Nothing change in the board / server. No miss/ hit announcement display
  * Post Condition: None
------------------------------------------------------------------------------
### TC-4.3 Valid shot coordination

TC-1: Missile Boundary Testing
  * Test Case: This test will attempt to fire a fire at a coordinate that is not on the board
  * Preconditions:
    * All ships has been placed and the game is currently in the firing phase
  * Input Values: [(A to J):-5, (A to J):-1, (A to J):11, (A to J):15, K:1, L:1…, Z:1]
  * Execution Steps:
    * Enter first Coordinate (A-5)
    * Look for error message
    * Ensure the player’s turn to fire hasn’t ended
    * Repeat with the rest of the list of coordinates starting with B-5 and ending at Z1
  * Output Values: An error message stating that the coordinate is not within the firing range should be displayed
  * Postconditions: Current player’s turn should not end and they should be prompted to enter another value

------------------------------------------------------------------------------
### TC-4.4 Duplicate shot

TC-4.4: Game should not allow users to fire multiple times at the same coordinate
  * Test Case: The test will attempt to fire upon 25 separate coordinates two
times to test the program’s allowance of multiple hits or misses on the
same square
  * Preconditions:
    * Both players’ ships have  been placed, preferably with at least one
ship square outside of the A1-E5 square to avoid an early win
ending the test before its completion.
    * Both players are in the firing phase and ready to fire, begins with
player 1
    * Input Values: [A1, A2, A3, A4, A5, B1, B2, B3, B4, B5, C1, C2, C3, C4,
C5 D1, D2, D3, D4, D5, E1, E2, E3, E4, E5] x2
  * Execution Steps
    * Each player will begin firing at A1 on the board, they will proceed to A2, A3, A4, A5, B1, B2...C1, C2...D1, D2...E1, E2… until the square on the board from A1-E5 is completely filled with hits or misses
    * Each player will take turns firing
    * Once the initial sweep is completed, they will once again begin at
A1 and proceed with the same exact firing pattern as the first
sweep
    * At the end of the second sweep, as long as everything worked as it should have , the first player’s turn shouldn’t have  ended and they should then fire upon A6, which will g  * them an output and switch to the second player’s turn
    * Player 2 will pick up from step 2 and continue from there
  * Output Values
    * Initial Sweep: If a ship is hit within this sweep, it should display a message with the ship that is hit. If the fire is a miss then it will declare that as a miss.
    * Second Sweep: All fires except the final (A6) will return an error message stating that the particular coordinate has already been fired upon
  * Postconditions
    * Initial Sweep: the turn will change to the other player’s turn
    * Second Sweep: in any of the duplicate fires, the player’s turn should not change and they will be given another opportunity to fire.
------------------------------------------------------------------------------
### TC-4.5 Player quit/surrender in the middle

TC-4.5a: Player exits game in progress - setup phase
  * Test Case: Tests what happens when a player quits their client during the setup phase.
  * Preconditions:
    * The game is in the setup phase.
  * Execution Steps:
    * Use ctrl-c to exit player 0’s client
    * Start a stopwatch
  * Output Values:
    * Player 0’s client should exit, or type 'quit'
    * Player 1’s client should display a message stating that the connection to player 0 was lost or that player 0 quit.
  * Postconditions:
    * Player 0’s client is not running
    * Player 1’s client is in the matchmaking stage.

TC-4.5b: Player exits game in progress - firing phase
  * Test Case: Tests what happens when a player quits their client during the firing phase.
  * Preconditions:
    * Both players have  their ships placed as follows:
    * carrier A1 - A5
    * battleship B1 - B4
    * destroyer C1 - C3
    * submarine D1 - D3
    * patrol boat F1 - F2
    * The game is in the firing phase.
  * Execution Steps:
    * Use ctrl-c to exit player 0’s client, or type 'quit'
    * Start a stopwatch
  * Output Values:
    * Player 0’s client should exit.
    * Player 1’s client should display a message stating that the connection to player 0 was lost or that player 0 quit.
  * Postconditions:
    * Player 0’s client is not running
    * Player 1’s client is in the matchmaking stage.


------------------------------------------------------------------------------
### TC-5.1 Ships placement must be finished before shooting phase

TC-5.1: Ships Must Be Placed Before Firing Phase Begins
  * Test Case: This test will make sure that both players has placed all of their ships before any player begins firing
  * Preconditions:
    * Player A and Player B have n’t finished placing all ships.
  * Input Values:
    * A1:A5 for Carrier
    * B1:B4 for Battleship
    * C1:C3 for Destroyer
    * D1:D3 for Submarine
    * E1:E2 for Patrol Boat
  * Execution Steps:
    * Both players will input coordinates for their ships in the placement phase
  * Output Values:
    * Firing round start message
  * Postconditions:
    * Both players should have  all 5 ships placed on the board
    * The game is moved into the firing phase

------------------------------------------------------------------------------
### TC-5.2 Ship sunk announcement

TC-5.2: Game should announce when a ship is sunk and what type of ship it is
  * Test Case: Players will fire upon ships that are at known coordinates, whenever a ship takes its maximum amount of hits, a message should appear stating that the ship has been sunk
  * Preconditions:
    * Both players, for the sake of the test, will have * their ships set up as follows: Carrier A1-A5, Battleship B1-B4, Destroyer C1-C3, Submarine D1-D3, Patrol Boat E1-E2
    * It is currently in the “firing phase”
  * Input Values: Each player will input all of these sets of coordinates
    * Coordinate set 1: A1, A2, A3, A4, A5, B1, B2, B3, B4, C1, C2, C3,
D1, D2, D3, E1, E2
    * Coordinate set 2: A1, A2, A3, A5, A4, B1, B2, B4, B3, C1, C3, C2, D1, D3, D2, E2, E1
    * Coordinate set 3: A1, A2, A5, A4, A3, B1, B4, B3, B2, C3, C2, C1, D3, D2, D1, E1, E2
    * Coordinate set 4: A1, A5, A3, A4, A2, B4, B2, B3, B1, C1, C2, C3, D1, D2, D3, E1, E2
    * Coordinate set 5: A4, A2, A3, A4, A1, B1, B2, B3, B4, C1, C2, C3, D1, D2, D3, E1, E2
  * Execution Steps
    * On the player’s first turn they will enter the first input in the current
coordinate list
    * Wait for a hit or sink message, verify it is the correct ship being hit or sunk
    * Turn will switch over to second player, they will repeat from Step 1
    * When the turn changes back over to the first player, they will continue from Step 1, replacing the input with the next in the list of input values
    * When all ships are sunk by player 1, start a new game with the same abo  * preconditions. Player 2 will be the first to fire, ensure all ships can be sunk by player 2
    * Start a new game and re-establish the same preconditions listed above
    * Repeat steps 1-5 with the next coordinate set until all 5 coordinate sets has been tested

------------------------------------------------------------------------------
### TC-5.3 Ship hit/miss announcement

TC-35: Game should clear differentiate between a hit or miss on board
  * Test Case: This test will make sure that a hit and a miss on board will
show different image.
  * Preconditions:
    * Both players have  their ships placed as follows:
      * carrier A1 - A5
      * battleship B1 - B4
      * destroyer C1 - C3
      * submarine D1 - D3
      * patrol boat F1 - F2
    * Players are in the firing phase of the game
  * Input Values:
    * Misses: E1-E10, G1-G6
    * Hits: A1-A5,B1-B4,C1-C3,D1-D3,F1-F2
  * Execution Steps:
    * Enter next coordinate from hit list
    * Ensure that the board is marked with an “X” on the spot that was hit
    * The other player will then take their turn
    * Enter same coordinate as in (4)
    * Ensure that the board is marked with an “X” on the spot that was hit
    * The other player will then take their turn
    * Enter next coordinate from miss list.
    * Ensure that the board is marked with an “O” on the spot that was hit
    * The other player will then take their turn
    * Enter coordinate from (10).
    * Ensure that the board is marked with an “O” on the spot that was hit
    * Play through the game alternating between intentional hits and misses and ensure that the board is marked correctly as described previously
  * Output Values: The hit was marked with an “X”, and the miss was marked with an “O”
  * Postconditions: The game should end after alternating between hits and misses
------------------------------------------------------------------------------
### TC-5.4 Winner decision

TC-5.4a: Win Test for player 1
  * Test Case: Tests whether the game ends when one player wins.
  * Preconditions:
    * Both players have * their ships placed as follows:
      * carrier A1 - A5
      * battleship B1 - B4
      * destroyer C1 - C3
      * submarine D1 - D3
      * patrol boat F1 - F2
    * Both players have * fired on A1-A5, B1-B4, C1-C3, D1-D3, F1
    * It is player 1’s turn to fire
  * Input Values:
    * F2
  * Execution Steps:
    * Player 1 fires on F2
  * Output Values:
    * Message on both clients stating that Player 1 won.
  * Postconditions:
    * Both clients return to matchmaking process.

TC-5.4b: Win Test for player 2
  * Test Case: Tests whether the game ends when one player wins.
  * Preconditions:
    * Both players have * their ships placed as follows:
      * carrier A1 - A5
      * battleship B1 - B4
      * destroyer C1 - C3
      * submarine D1 - D3
      * patrol boat F1 - F2
    * Both players have * fired on A1-A5, B1-B4, C1-C3, D1-D3, F1
    * It is player 2’s turn to fire
  * Input Values:
    * F2
  * Execution Steps:
    * Player 2 fires on F2
  * Output Values:
    * Message on both clients stating that Player 2 won.
  * Postconditions:
    * Both clients return to matchmaking process

------------------------------------------------------------------------------
### TC-5.5: Timeout

TC-5.5a: 
  * Preconditions: 2 players are in ship placement phase
  * Input values: Nothing
  * Execution step: Player 1 do not input anything in 120 seconds, Player 2 finished ship placement before 120 seconds.
  * Output values: System must announce Player 1 lost the game because of timeout rule.
  * Post Condition:

TC-5.5b 
  * Preconditions: 2 players are in firing phase, player 1's turn
  * Input values: Nothing
  * Execution step: Player 1 do not input anything in 30 seconds.
  * Output values: System must announce Player 1 lost the game because of timeout rule.
  * Post Condition:

------------------------------------------------------------------------------
### TC-5.6 Oponent ship disclosure

TC-5.6: Opponent Ship Disclosure Test
  * Test Case: Tests whether a player may see the positions of their opponent’s ships.
  * Preconditions:
    * Both players have * their ships placed as follows:
      * carrier A1 - A5
      * battleship B1 - B4
      * destroyer C1 - C3
      * submarine D1 - D3
      * patrol boat F1 - F2
    * The game is in the firing phase.
    * No fires has been fired yet.
  * Input Values: None.
  * Execution Steps:
    * Attempt to view the positions of the other players ships.
  * Output Values:
    * None.
  * Postconditions:
    * The other players ships should not be visible.

------------------------------------------------------------------------------
### TC-5.7 Oponent ship disclosure

TC-5.7: System should clearly indicate whose turn it is
  * Test Case: This test will make sure that after one player finish fire, it will stop this player action and allow other player to take action.
  * Preconditions:
    * A 10x10 game board exists for every player
    * Two players have * placed 5 ships on their board.
  * Input Values:
    * Input a series of fire coordinate for Player A, like D:1….
    * Input a series of fire coordinate for Player B, like C:3, C:4...
  * Execution Steps:
    * two players will fire a series of coordinates
    * we need to obser  * whether after one player fire, system will notify another player to fire
  * Output Values: After one player fire, the program should indicate another player it is you time to take action.
  * Postconditions:

 --------------------------------------------------------------------------
 
TC-Non1a TEST-WINDOWS-CLIENT
  * Test Case: Test to see that when the game is launched and played on a Microsoft Windows machine, it will run with no problems.
  * Preconditions: The tester is running a machine with Microsoft Windows.
  * Input Values: A full game is played on this machine.
  * Execution Steps: The tester should use a machine with Microsoft Windows. They should launch the client program, join a game server, and play a full game of battleship to completion.
  * Post Conditions: The game should exit and the system should function as it was before the game was run.

TC-Non1b TEST-LINUX-CLIENT
  * Test Case: Test to see that when the game is launched and played on a Ubuntu Linux machine, it will run with no problems.
  * Preconditions: The tester is running a machine with Ubuntu Linux.
  * Input Values: A full game is played on this machine.
  * Execution Steps: The tester should use a machine with Ubuntu Linux. They should launch the client program, join a game server, and play a full game of battleship to completion.
  * Post Conditions: The game should exit and the system should function as it was before the game was run.

TC-Non1c TEST-OSX-CLIENT
  * Test Case: Test to see that when the game is launched and played on an Apple OSX machine, it will run with no problems.
  * Preconditions: The tester is running a machine with Apple OSX.
  * Input Values: A full game is played on this machine.
  * Execution Steps: The tester should use a machine with Apple OSX. They should launch the client program, join a game server, and play a full game of battleship to completion.
  * Post Conditions: The game should exit and the system should function as it was before the game was run.

TC-NON2 TEST-JAVA-SERVER
  * Test Case: This is a test to see what happens when the server program is compiled
with -javac. This is to verify that the server is written in Java.
  * Preconditions: The system must have  Javac and the Java JDK installed.
  * Input Values: Running the -javac and -java commands.
  * Execution Steps: Compile the server program using -javac and run the server program using -java.
  * Post Conditions: The server program will compile without error after the -javac command and should run after -javac command

TC-Non3a The client can reach the server.
  * Preconditions: The client must have  access to internet.
  * Input Values: The client has to launch the program and choose to connect to the server.
  * Execution Steps: Launch program and run as client and connect to server.
  * Output Values: Expected output would be a successful launch message, can also receive error message if unsuccessful or an error occurred while trying to reach the server.
  * Post Conditions: If successful, the client will be connected to the server, if not then the client is not connected to the server.

TC-Non3b: 2 clients can reach the server at once.
  * Preconditions: Both clients must have  access to internet.
  * Input Values: The clients must launch the battleship program and choose to connect to the server.
  * Execution Steps: Launch battleship program and run as client and connect to server.
  * Output Values: Expected output would be a successful launch message for both clients, can also receive error message if unsuccessful or an error occurred while trying to reach the server.
  * Post Conditions: If successful, both clients will be connected to the server, if not then the clients are not connected to the server.
