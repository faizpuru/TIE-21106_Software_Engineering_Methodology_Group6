begin: position of the begin of the level
end: position of the end of the level
time: time to succeed the level

Create a platform :
P POSITION HEIGHT WIDTH DIRECTION

Create a hole :
H POSITION HEIGHT WIDTH DIRECTION

Create a stair with platforms :
S NB_PLATFORMS POS_FIRST_PLATFORM POS_LAST_PLATFORM HEI_FIRST_PLATFORM HEI_LAST_PLATFORM DIRECTION_STAIRS DIRECTION

//DIRECTION : + = upwards, - = downwards

Enemies:
N	Nasty			XPOS YPOS HEIGHT WIDTH DIRECTION
BO	Bomb			XPOS YPOS HEIGHT WIDTH DIRECTION
SA	Static Alien	XPOS YPOS HEIGHT WIDTH DIRECTION
MA	Moving Alien	XPOS YPOS HEIGHT WIDTH DIRECTION
