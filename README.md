# Scheduling AI

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

