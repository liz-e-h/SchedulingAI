# Scheduling AI
The scheduling problem for soccer games and practices in the City of Calgary is a complex task that involves various constraints, both hard and soft. To tackle this problem, the proposed approach combines two search models: a set-based genetic search and an integrated or-tree-based search.

- Set-Based Genetic Search: This part of the approach focuses on optimizing soft constraints. Soft constraints are those that can be violated to some extent, but the goal is to minimize their violation. The set-based genetic search is a technique that leverages genetic algorithms to efficiently explore the solution space. Genetic algorithms are a type of heuristic search algorithm inspired by the process of natural selection and evolution.
  - Population Generation: The genetic algorithm starts by creating a population of potential schedules, which are represented as sets of games and practices.
  - Selection: Schedules are selected for reproduction based on their fitness with respect to soft constraints, such as minimizing travel time, optimizing field allocation, and accommodating team preferences.
  - Crossover and Mutation: Genetic operations like crossover and mutation are applied to generate new schedules. Crossover combines elements from two parent schedules, while mutation introduces random changes.
  - Fitness Evaluation: The fitness of each schedule is evaluated, taking into account soft constraints. The goal is to evolve schedules that better satisfy these constraints over generations.
  - Termination: The genetic search continues for a set number of generations or until a termination condition is met.

- Integrated Or-Tree-Based Search: Hard constraints are the ones that must be satisfied without exception. The or-tree-based search is integrated into the approach to efficiently generate random initial solutions that respect the hard constraints.
  - Or-Tree-Based Search: An or-tree is a search tree that explores different options to satisfy hard constraints, such as assigning specific teams to specific fields on particular days and times.
  - Randomized Initial Solutions: The or-tree-based search generates randomized solutions that comply with hard constraints, creating a starting point for the genetic search.
  - Integration with Genetic Search: These hard-constraint-compliant solutions are then passed on to the set-based genetic search, which further optimizes them with respect to soft constraints.

By integrating the or-tree-based search for generating initial solutions, the approach ensures that hard constraints are efficiently satisfied from the beginning. Then, the genetic search fine-tunes these solutions, optimizing them with respect to the softer constraints.

This approach is well-suited for complex scheduling problems like soccer games and practices in the City of Calgary, as it provides a balance between handling hard constraints and optimizing soft constraints, ultimately aiming to generate schedules that are practical and efficient while satisfying the necessary requirements.

## Getting Started
### Prerequisites
Java Development Kit (JDK): Your system should have Java 18 or above installed.
### Installation
Clone the repo
```sh
git clone https://github.com/liz-e-h/SchedulingAI.git
```
Navigate to the project directory
```sh
cd SchedulingAI
```
### Usage
```sh
java Main filename w_{minfilled} w_{pref} w_{pair} w_{secdiff} pen_{gamemin} pen_{practicemin} pen_{notpaired} pen_{section}
```
For example:
```sh
java Main input.txt 1 1 1 1 1 1 1 1
```
The general scheme of an input text file is as follows:
>Name:<br>
Example-name<br>
Game slots:<br>
Day, Start time, gamemax, gamemin<br>
Practice slots:<br>
Day, Start time, practicemax, practicemin<br>
Games:<br>
Game Identifier<br>
Practices:<br>
Practice Identifier<br>
Not compatible:<br>
Game Identifier, Game Identifier<br>
Game Identifier, Practice Identifier<br>
Practice Identifier, Practice Identifier<br>
Unwanted:<br>
Game Identifier, Slot day, Slot time<br>
Practice Identifier, Slot day, Slot time<br>
Preferences:<br>
Slot day, Slot time, Game Identifier, Preference value<br>
Slot day, Slot time, Practice Identifier, Preference value<br>
Pair:<br>
Game Identifier, Game Identifier<br>
Game Identifier, Practice Identifier<br>
Practice Identifier, Practice Identifier<br>
Partial assignments:<br>
Game Identifier, Slot day, Slot time<br>
Practice Identifier, Slot day, Slot time<br>

## Search Probelm Description
Overseeing soccer fields around the City of Calgary, we have to assign *Games* and *Practices* to weekly field time *Slots*.
Each time slot has a different quantity of fields available.

There are several leagues across the city. Each league has a desire to book time slots for weekly games for each of its divisions. As a result, we have a set $`Games = \{ g_1, ..., g_m\}`$ of weekly games slots to book where one $`g_i`$ is the weekly games slots to book for a division 𝑖𝑖 in a league which may have multiple divisions.

We also have a set $`Practices = \{p_{11}, ..., p_{1k_1}, ..., p_{m1}, ..., p_{mk_m}\}`$ of practices associated with those divisional games.

Practice $`p_{i1}, ... p_{ik_i}`$ are associated with teams involved in the games $`g_i`$ of a particular division $`i`$. Note, if $`k_i = 0`$ this would mean the division does not have any practices slots to be scheduled.

Finally, we have a set $`Slots = \{s_1, ..., s_n\}`$ of time slots into which games and practices must be assigned.

## Problem Constraints
### Hard Constraints
1. Not more than $`gamemax(s)`$ games can be assigned to each $`s ∈ Slots`$.
2. Not more than $`practicemax(s)`$ practices can be assigned to each $`s ∈ Slots`$.
3. $`assign(g_i) ≠ assign(p_{ik_i})`$ for all $`i`$ and $`k_i`$.
4. Some divisions allow players to play in both divisions and therefore want to make their games/practices not happen at the same time. The input for your system will contain a list of $`notcompatible(a,b)`$ statements, with $`a, b ∈ Games + Practices`$. For each of those, $`assign(a) ≠ assign(b)`$.
5. Sometimes there are certain divisions that already have pre-arrangements for certain slots. The input for the system can contain a partial assignment $`partassign: Games + Practices → Slots + \{\$\}.`$ (\$ is a placeholder for no pre-assignment.) The assignment $`assign`$ the system produces has to fulfill the condition:
$`assign(a) = partassign(a)`$ for all $`a \in Games + Practices`$ with  $partassign(a) ≠ \$$.
6. The input for your system can contain a list of $`unwanted(a,s)`$ statements, with $`a ∈ Games + Practices`$ and $`s ∈ Slots`$. For each of those $`assign(a) ≠ s`$ must be true.
7. There will be additional hard constraints specific to the City of Calgary that will be explained later.
### Soft Constraints
1. There are certain times of the day that nobody prefers but we’d like our system to produce a result that attempts to spread out the usage of our resources. To accomplish this, we’ll define a soft constraint which is a minimum level of usage we’d like a slot to achieve. To facilitate this, we have for each slot $`s`$ a $`gamemin(s)`$ and $`practicemin(s)`$ that indicate how many games, resp. practices, should at least be scheduled into the slot $`s`$. The system accepts as input penalty values $`pen_{gamemin}`$ and $`pen_{practicemin}`$ (as command line arguments) and for each games below $`gamemin`$ we will get $`pen_{gamemin}`$ and for each practice $`pen_{practivemin}`$ added to the Eval-value of an assignment.
2. Certain leagues have certain preferences regarding in which time slots their games and practices should be scheduled. Naturally, we see this as something that should be treated as soft constraint. Higher age groups and tiers are awarded a certain number of ranking points and lower ones fewer ranking points. Each league can distribute these points over pairs of (game/practice, time slots). Formally, we assume a function $`preference: (Games + Practice) × Slots → ℕ`$ that reports those preferences. (i.e. we have a function that indicates a numerical natural number score of the preference of a game or practice being assigned to a slot.) For each assignment in $`assign`$, we add up the preference-values for a games/practices that refer to a different slot as the penalty that is added to the Eval-value of $`assign`$.
3. For certain games and/or practices, we might prefer these to be scheduled at the same time. To facilitate this, there will be a list of $`pair(a,b)`$ statements in the input for your system, with $`a, b ∈ Games + Practices`$ and a parameter $`pen_{notpaired}`$ for your system. For every 𝒑𝒑 (𝒂 ,𝒃 ) statement, for which (a) is not equal to (b), you have to add 𝑝𝑝𝑝 𝑛𝑛𝑛 𝑛𝑛 𝑛𝑛 to the Eval-value of $`assign`$.

## Instantiation
### Games/Practice Naming
In Calgary, weekly games to be booked games are identified by an organizing body, an age group (and often a tier within that age group), and division in the age group. For example, the main soccer body in Calgary is CMSA who has several different age groups/tiers it operates, but there are also leagues like CUSA and CSSC that operate in the city. A league will have an indicator that includes the division in that sub-league that looks like:
<br />
**CMSA U12T1 DIV 01**
<br />
For Calgary Minor Soccer Association - Under 12 - Tier 1 - Division 1.
Practices add on the end of this indicator
<br />
CMSA U12T1 DIV 01 **PRC 01**
<br />
is practice 1 for CMSA U12T1 DIV 01. If a practice is used by all divisions of the age/tier level, then we drop the division
<br />
CMSA U12T1 PRC 01
<br />
Sometimes a league doesn’t book practices but instead generally creates open field access for their division. Instead of PRC we will see indicator OPN. You will be able to treat OPN as synonymous with PRC for this project as field bookings operate identically.
<br />
CMSA U12T1 DIV 01 **OPN 01**

### Time Slots
The available time slots depend on the day of the week and whether we look at *games* or *practices*.

Mondays and Wednesdays, the slots available for games and practices are 8:00-9:00, 9:00-10:00, 10:00-11:00, 11:00-12:00, 12:00-13:00, 13:00-14:00, 14:00-15:00, 15:00-16:00, 16:00-17:00, 17:00-18:00, 18:00-19:00, 19:00-20:00 and 20:00-21:00. The same slots are available for *games* also on Fridays.

The available time slots for Tuesdays and Thursdays for games are 8:00-9:30, 9:30-11:00, 11:00-12:30, 12:30-14:00, 14:00-15:30, 15:30-17:00, 17:00-18:30 and 18:30-20:00. For practices, the available time slots are the same on Tuesdays and Thursdays as previously given for Mondays and Wednesdays practices.

The slots available for practices on Fridays are 8:00-10:00, 10:00-12:00, 12:00-14:00, 14:00-16:00, 16:00-18:00, 18:00-20:00.

All slots beginning at 18:00 or later are called evening slots.

Note that the fact that the slots for games and practices on Tuesdays and Thursdays are not following the same time scheme requires you to deal with time and how time
slots may overlap. It also seems that this contradicts the general problem scheme, but it can be reformulated in terms of the general problem.

Our City of Calgary problem has the following hard constraints:
- If a division’s games (Ex. CMSA U12T1 DIV 01) are put into a slot on Mondays, then they must be put into the corresponding time slots on Wednesdays and Fridays. So, these three time slots are treated as one abstract slot, which allows us to see our Calgary problem as an instantiation of the general problem!
- Similarly, if a division’s games are put into a slot on Tuesdays, then they must be put into the corresponding time slots on Thursdays.
- If a practice (ex. CMSA U12T1 DIV 01 PRC 01) is put into a slot on Mondays, it must be put into the corresponding time slots on Wednesdays.
- If a practice is put into a slot on Tuesdays, it must be put into the corresponding time slots on Thursdays.
- Fridays are single practice slots and not linked with other days.
- All divisions with a division number starting DIV 9 are evening divisions and must be scheduled into evening slots.
- All games in all tiers of the U15/U16/U17/U19 level must be scheduled into non-overlapping time slots.
- No games can be scheduled on Tuesdays 11:00-12:30 as a league wide meeting occurs weekly at this time for admin.
- There are two special "game bookings" CMSA U12T1S and CMSA U13T1S that must be scheduled Tuesdays / Thursdays 18:00-19:00. CMSA U12T1S is not allowed to overlap with any practices/games of CMSA U12T1 and CMSA U13T1S is not allowed to overlap with any practices/games of CMSA U13T1. These two "game bookings" are a way of schedule special showcase tryouts series for these divisions’ players for selection to special provincial teams for Alberta Games.

The City of Calgary also has the following soft constraints:
- Different divisional games within a single age/tier group should be scheduled at different times. For each pair of divisions that is scheduled into the same slot, we add a penalty $`pen_{section}`$ to the Eval-value of an assignment *assign*. Ex. U12T1 DIV 01 and U12T1 DIV 02 should not be scheduled at same time slot, if possible, for a better Eval score.


