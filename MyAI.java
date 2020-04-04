import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

// ======================================================================
// FILE:        MyAI.java
//
// AUTHOR:      Abdullah Younis
//
// DESCRIPTION: This file contains your agent class, which you will
//              implement. You are responsible for implementing the
//              'getAction' function and any helper methods you feel you
//              need.
//
// NOTES:       - If you are having trouble understanding how the shell
//                works, look at the other parts of the code, as well as
//                the documentation.
//
//              - You are only allowed to make changes to this portion of
//                the code. Any changes to other portions of the code will
//                be lost when the tournament runs your code.
// ======================================================================

public class MyAI extends Agent
{
	private int[][] Availability;
	private int[][] Path;
	private int[][] CantGoTo;
	private int AvailCount;
	private boolean GoBack;
	private int[] CurrSpot;
	private int facing;
	private boolean TurnFor;
	private boolean TurnLeft;
	private boolean TurnRight;
	private boolean TurnBack;
	private boolean DoTurn;
	private int GoToTurn;
	private boolean RunningPath;
	private Stack<int[]> CurrPath;
	private Stack<int[]> BackPath;
	private Stack<int[]> UndoPath;
	private Stack<int[]> ShortPath;
	private int[] LastSpot;
	private int[] TwoLastSpot;
	private int[] LastBackStep;
	private int counter;
	private Action TestToReturn;
	private boolean firstRun;
	private int InARow;
	private boolean undo;
	private boolean crash;
	public MyAI ( )
	{
		// ======================================================================
		// YOUR CODE BEGINS
		// ======================================================================
		//UP IS 0
		//RIGHT IS 1
		//LEFT IS 2
		//DOWN IS 3
		crash = false;
		undo = false;
		InARow = 0;
		firstRun = true;
		TestToReturn = Action.GRAB;
		counter = 0;
		Availability = new int[10][10];
		Path = new int[10][10];
		CantGoTo = new int[10][10];
		AvailCount = 0;
		GoBack = false;
		CurrSpot = new int[2];
		LastSpot = new int[2];
		TwoLastSpot = new int[2];
		LastBackStep = new int[2];
		LastBackStep[0] = -1;
		LastBackStep[1] = -1;
		facing = 0;
		TurnFor = false;
		TurnLeft = false;
		TurnRight = false;
		TurnBack = false;
		DoTurn = false;
		GoToTurn = -1;
		CurrPath = new Stack<>();
		BackPath = new Stack<>();
		UndoPath = new Stack<>();
		ShortPath = new Stack<>();
		RunningPath = false;
		// ======================================================================
		// YOUR CODE ENDS
		// ======================================================================
	}
	
	public Action getAction
	(
		boolean stench,
		boolean breeze,
		boolean glitter,
		boolean bump,
		boolean scream
	)
	{
		// ======================================================================
		// YOUR CODE BEGINS
		// ======================================================================
		//System.out.println(CurrSpot[0]);
		if(crash)
			return Action.GRAB;
		//try {
		if(firstRun)
		{
			if(breeze || stench)
				return Action.CLIMB;
			firstRun = false;
			return Action.TURN_LEFT;
		}
		if(bump)
		{
			Availability[CurrSpot[0]][CurrSpot[1]] = 0;
			AvailCount--;
			if(facing == 0)
			{
				for(int a = 0; a < 9; a ++)
				{
					CantGoTo[CurrSpot[0]][a] = 3;
				}
				CurrSpot[0]--;
			}
			if(facing == 1)
			{
				for(int a = 0; a < 10; a ++)
				{
					CantGoTo[a][CurrSpot[1]] = 3;
				}
				CurrSpot[1]--;
			}
			if(facing == 2)
			{
				for(int a = 0; a < 10; a ++)
				{
					CantGoTo[a][CurrSpot[1]] = 3;
				}
				CurrSpot[1]++;
			}
			if(facing == 3)
				CurrSpot[0]++;
			
		}

		if(!GoBack)
		{
			if(glitter)
			{
				GoBack = true;
				ShortPath();
				CurrPath = ShortPath;
				return Action.GRAB;
			}
			if(RunningPath)
			{
				Action a = RunPath(CurrPath);
				if(a != Action.GRAB)
				{
					return a;
				}
			}
			if(Availability[CurrSpot[0]][CurrSpot[1]] == 1)
				AvailCount--;
			Availability[CurrSpot[0]][CurrSpot[1]] = 0;
			Path[CurrSpot[0]][CurrSpot[1]] = 1;
			int[] PushOn1 = new int[2];
			PushOn1[0] = CurrSpot[0];
			PushOn1[1] = CurrSpot[1];
			BackPath.push(PushOn1);
		
			if(!undo && !bump)
			{
				UndoPath.push(PushOn1);
			}
			undo = false;
			if(breeze || stench)
			{ //BREEZE OR STENCH IN CURRENT SPOT
				if(CurrSpot[0] - 1 >= 0)
				{
					if(Availability[CurrSpot[0]-1][CurrSpot[1]] != 1 && Path[CurrSpot[0]-1][CurrSpot[1]] != 1)
						CantGoTo[CurrSpot[0]-1][CurrSpot[1]] = 1;
				}
				if(Availability[CurrSpot[0]+1][CurrSpot[1]] != 1 && Path[CurrSpot[0]+1][CurrSpot[1]] != 1)
					CantGoTo[CurrSpot[0]+1][CurrSpot[1]] = 1;
				if(CurrSpot[1] - 1 >= 0)
				{
					if(Availability[CurrSpot[0]][CurrSpot[1]-1] != 1 && Path[CurrSpot[0]][CurrSpot[1]-1] != 1)
						CantGoTo[CurrSpot[0]][CurrSpot[1]-1] = 1;
				}
					if(Availability[CurrSpot[0]][CurrSpot[1]+1] != 1 && Path[CurrSpot[0]][CurrSpot[1]+1] != 1)
						CantGoTo[CurrSpot[0]][CurrSpot[1]+1] = 1;
			}else
			{ //THERE IS NO BREEZE OR STENCH
				if(CurrSpot[0] - 1 >= 0)
				{
					if(CantGoTo[CurrSpot[0]-1][CurrSpot[1]] != 3 && Path[CurrSpot[0]-1][CurrSpot[1]] != 1 && Availability[CurrSpot[0]-1][CurrSpot[1]] != 1)
					{
						Availability[CurrSpot[0]-1][CurrSpot[1]] = 1;
						AvailCount++;
					}
				}
				if(CantGoTo[CurrSpot[0]+1][CurrSpot[1]] != 3 && Path[CurrSpot[0]+1][CurrSpot[1]] != 1 && Availability[CurrSpot[0]+1][CurrSpot[1]] != 1)
				{
					Availability[CurrSpot[0]+1][CurrSpot[1]] = 1;
					AvailCount++;
				}
				if(CurrSpot[1] - 1 >= 0)
				{
					if(CantGoTo[CurrSpot[0]][CurrSpot[1]-1] != 3 && Path[CurrSpot[0]][CurrSpot[1]-1] != 1 && Availability[CurrSpot[0]][CurrSpot[1]-1] != 1)
					{
						Availability[CurrSpot[0]][CurrSpot[1]-1] = 1;
						AvailCount++;
					}
				}
					if(CantGoTo[CurrSpot[0]][CurrSpot[1]+1] != 3 && Path[CurrSpot[0]][CurrSpot[1]+1] != 1 && Availability[CurrSpot[0]][CurrSpot[1]+1] != 1)
					{
						Availability[CurrSpot[0]][CurrSpot[1]+1] = 1;
						AvailCount++;
					}
			}
			//System.out.println("AVAIL COUNTTTT " +AvailCount);
			if(AvailCount == 0 && !GoBack)
			{
			//	System.out.println("HERE");
				GoBack = true;
				ShortPath();
				CurrPath = ShortPath;
				Action a = RunPath(CurrPath);
				if(a != Action.GRAB)
				{
					return a;
				}
				//System.out.println("uuuhhh");
				return Action.GRAB;
			}
			
			if(1 == Availability[CurrSpot[0]+1][CurrSpot[1]])
			{
				int[] PushOn = new int[2];
				PushOn[0] = CurrSpot[0]+1;
				PushOn[1] = CurrSpot[1];
				CurrPath.push(PushOn);
				RunningPath = true;
				return RunPath(CurrPath);
			}
			if(CurrSpot[0]-1 >= 0 && 1 == Availability[CurrSpot[0]-1][CurrSpot[1]])
			{
				int[] PushOn = new int[2];
				PushOn[0] = CurrSpot[0]-1;
				PushOn[1] = CurrSpot[1];
				CurrPath.push(PushOn);
				RunningPath = true;
				return RunPath(CurrPath);
			}
			if(1 == Availability[CurrSpot[0]][CurrSpot[1]+1])
			{
				int[] PushOn = new int[2];
				PushOn[0] = CurrSpot[0];
				PushOn[1] = CurrSpot[1]+1;
				CurrPath.push(PushOn);
				RunningPath = true;
				return RunPath(CurrPath);
			}
			if(CurrSpot[1]-1 >= 0 && 1 == Availability[CurrSpot[0]][CurrSpot[1]-1])
			{
				int[] PushOn = new int[2];
				PushOn[0] = CurrSpot[0];
				PushOn[1] = CurrSpot[1]-1;
				CurrPath.push(PushOn);
				RunningPath = true;
				return RunPath(CurrPath);
			}
			//go back to previous step. run runpath ( last spot )
			
			//System.out.println();
			int[] PushOn = new int[2];
			UndoPath.pop();
			PushOn[0] = UndoPath.peek()[0];
			PushOn[1] = UndoPath.peek()[1];
			UndoPath.pop();
			CurrPath.push(PushOn);
			RunningPath = true;
			return RunPath(CurrPath);
			/*if(CantGoTo[LastSpot[0]][LastSpot[1]] == 1)//if(LastSpot[0] == LastBackStep[0] && LastSpot[1] == LastBackStep[1])
			{
				GoBack = true;
				return Action.GRAB;
			}
			if(CurrSpot[0] != LastSpot[0] || CurrSpot[1] != LastSpot[1])
			{
				CantGoTo[CurrSpot[0]][CurrSpot[1]] = 1;
				int[] PushOn = new int[2];
				PushOn[0] = LastSpot[0];
				PushOn[1] = LastSpot[1];
				LastBackStep = PushOn;
				CurrPath.push(PushOn);
				RunningPath = true;
				return RunPath(CurrPath);
			}
			CantGoTo[CurrSpot[0]][CurrSpot[1]] = 1;
			int[] PushOn = new int[2];
			PushOn[0] = TwoLastSpot[0];
			PushOn[1] = TwoLastSpot[1];
			CurrPath.push(PushOn);
			RunningPath = true;
			return RunPath(CurrPath);	*/
		}else //GO BACK
		{
			if(CurrSpot[0] != 0 || CurrSpot[1] != 0)
			{
				//Stack<int[]> newStack = new Stack<int[]>();
				//newStack.addAll(CurrPath);
				//System.out.print("size "+newStack.size());
				//while(!newStack.isEmpty()) {
					//System.out.print("[ "+ newStack.peek()[0] + " , "+ newStack.pop()[1]+"] ");}
				//System.out.println();
				Action a = RunPath(CurrPath);
				if(a != Action.GRAB)
				{
					return a;
				}
				//System.out.println("uuuhhh");
				return Action.GRAB;
			}
			return Action.CLIMB;
		}								
		
		// ======================================================================
		// YOUR CODE ENDS
		// ======================================================================
	//}catch(Exception e){
	//	crash = true;
//		return Action.GRAB;}
	}
	
	// ======================================================================
	// YOUR CODE BEGINS
	// ======================================================================
	private void CalcPath(int[] Start,int[] Fin)
	{
		int[] Curr = new int[2];
		Stack<int[]> FinalPath = new Stack<>();
		Curr[0] = Start[0];
		Curr[1] = Start[1];
		List<int[]> DontGo = new ArrayList<>();
		int[] Hold = new int[2];
		boolean done = false;
		while(Curr[0] != Fin[0] || Curr[1] != Fin[1])
		{
			done = false;
			if(Curr[0]-1 >= 0)
			{
				Hold[0] = Curr[0]-1;
				Hold[1] = Curr[1];
				if(!DontGo.contains(Hold) && Path[Hold[0]][Curr[1]] == 1 || Availability[Hold[0]][Curr[1]] == 1)
				{
					int[] toPush = new int[2];
					toPush[0] = Hold[0];
					toPush[1] = Hold[1];
					FinalPath.push(toPush);
					Curr[0] = Hold[0];
					Curr[1] = Hold[1];
					done = true;
				}
				Hold[0] = Curr[0]+1;
				Hold[1] = Curr[1];
			}
			if(!done && (!DontGo.contains(Hold) && Path[Curr[0]+1][Curr[1]] == 1 || Availability[Curr[0]+1][Curr[1]] == 1))
			{
				int[] toPush = new int[2];
				toPush[0] = Hold[0];
				toPush[1] = Hold[1];
				FinalPath.push(toPush);
				Curr[0] = Hold[0];
				Curr[1] = Hold[1];
				Hold[0] = Curr[0];
				Hold[1] = Curr[1]-1;
				done = true;
			}
			if(!done && Hold[1] >= 0)
			{
				if(!DontGo.contains(Hold) && Path[Curr[0]][Hold[1]] == 1 || Availability[Curr[0]][Hold[1]] == 1)
				{
					int[] toPush = new int[2];
					toPush[0] = Hold[0];
					toPush[1] = Hold[1];
					FinalPath.push(toPush);
					Curr[0] = Hold[0];
					Curr[1] = Hold[1];
					done = true;
				}
				Hold[0] = Curr[0];
				Hold[1] = Curr[1]+1;
			}
			if(!done && (Hold[1] >= 0 && !DontGo.contains(Hold) && Path[Curr[0]][Curr[1]+1] == 1 || Availability[Curr[0]][Curr[1]+1] == 1))
			{
				int[] toPush = new int[2];
				toPush[0] = Hold[0];
				toPush[1] = Hold[1];
				FinalPath.push(toPush);
				Curr[0] = Hold[0];
				Curr[1] = Hold[1];
			}else {
				DontGo.add(FinalPath.pop());
				Curr = FinalPath.peek();
			}
		}
		while(!FinalPath.empty())
		{
			CurrPath.push(FinalPath.pop());
		}
		
	}
	private Action RunPath(Stack<int[]> path)
	{
		//try {
		if(DoTurn)
			return Turning(GoToTurn);
		if(path.empty())
		{
			RunningPath = false;
			return Action.GRAB;
		}
		Stack<int[]> newStack = new Stack<int[]>();
		newStack.addAll(path);
		//System.out.print("size "+newStack.size());
		//while(!newStack.isEmpty()) {
			//System.out.print("[ "+ newStack.peek()[0] + " , "+ newStack.pop()[1]+"] ");}
		//System.out.println("  CURRENT SPOT " + CurrSpot[0] +", "+ CurrSpot[1]);
		int[] NextStep = path.peek();
		if(CurrSpot[1] == NextStep[1] && CurrSpot[0] == NextStep[0] && path.size() > 1)
		{
			path.pop();
			NextStep = path.peek();
		}
		if(CurrSpot[0] > NextStep[0])
		{
			if(facing == 3)
			{
				path.pop();
				TwoLastSpot[0] = LastSpot[0];
				TwoLastSpot[0] = LastSpot[1];
				LastSpot[0] = CurrSpot[0];
				LastSpot[1] = CurrSpot[1];
				CurrSpot[0]--;
				return Action.FORWARD;
			}
			else
			{
				DoTurn = true;
				GoToTurn = 3;
				return Turning(GoToTurn);
			}
		}
		if(CurrSpot[0] < NextStep[0])
		{
			if(facing == 0)
			{
				path.pop();
				TwoLastSpot[0] = LastSpot[0];
				TwoLastSpot[0] = LastSpot[1];
				LastSpot[0] = CurrSpot[0];
				LastSpot[1] = CurrSpot[1];
				CurrSpot[0]++;
				return Action.FORWARD;
			}
			else
			{
				DoTurn = true;
				GoToTurn = 0;
				return Turning(GoToTurn);
			}
		}
		if(CurrSpot[1] > NextStep[1])
		{
			if(facing == 2)
			{
				path.pop();
				TwoLastSpot[0] = LastSpot[0];
				TwoLastSpot[0] = LastSpot[1];
				LastSpot[0] = CurrSpot[0];
				LastSpot[1] = CurrSpot[1];
				CurrSpot[1]--;
				return Action.FORWARD;
			}
			else
			{
				DoTurn = true;
				GoToTurn = 2;
				return Turning(GoToTurn);
			}
		}
		if(CurrSpot[1] < NextStep[1])
		{
			if(facing == 1)
			{
				path.pop();
				TwoLastSpot[0] = LastSpot[0];
				TwoLastSpot[0] = LastSpot[1];
				LastSpot[0] = CurrSpot[0];
				LastSpot[1] = CurrSpot[1];
				CurrSpot[1]++;
				return Action.FORWARD;
			}
			else
			{
				DoTurn = true;
				GoToTurn = 1;
				return Turning(GoToTurn);
			}
		}
		if(CurrSpot[1] == NextStep[1] && CurrSpot[0] == NextStep[0])
			BackPath.pop();
		return Action.GRAB;
		//}catch(Exception e)
		//{
		//	crash = true;
	//		return Action.GRAB;
	//	}
	}
	private Action Turning(int direction)
	{

		if(GoToTurn == facing)
		{
			DoTurn = false;
			return RunPath(CurrPath);
		}
		if(facing == 0)
		{
			if(GoToTurn == 1)
			{
				facing = 1;
				return Action.TURN_RIGHT;
			}
			if(GoToTurn == 2)
			{
				facing = 2;
				return Action.TURN_LEFT;
			}
			if(GoToTurn == 3)
			{
				facing = 1;
				return Action.TURN_RIGHT;
			}
		}
		if(facing == 1 && GoToTurn != 3)
		{
			facing = 0;
			return Action.TURN_LEFT;
		}
		if(facing == 2 && GoToTurn != 3)
		{
			facing = 0;
			return Action.TURN_RIGHT;
		}
		if(facing == 3 && GoToTurn != 3)
		{
			facing = 1;
			return Action.TURN_LEFT;
		}
		if(facing == 2 && GoToTurn == 3)
		{
			facing = 3;
			return Action.TURN_LEFT;
		}
		facing = 3;
		return Action.TURN_RIGHT;
	}
	private void ShortPath()
	{
		Stack<int[]> newStack = new Stack<int[]>();
		Stack<int[]> hold = new Stack<int[]>();
		newStack.addAll(BackPath);
		int[][] intr = new int[newStack.size()][];
		int count = 0;
		while(!newStack.isEmpty())
		{
			intr[count] = newStack.pop();
			count++;
		}
		hold.push(CurrSpot);
		for(int a = 0; a <= intr.length-2; a++)
		{
			//System.out.println(a+" HERE");
			//System.out.println(intr.length+"LENGTH HERE");
			for(int b = intr.length-1; b >= 0; b--)
			{
				if((hold.peek()[0]+1 == intr[b][0] && hold.peek()[1] == intr[b][1]) || (hold.peek()[0]-1 == intr[b][0] && hold.peek()[1] == intr[b][1]))
				{
					if(b != a)
					{
						hold.push(intr[b]);
						a = b-1;
						b = 0;
					}
					else
					{
						b = 0;
						hold.push(intr[b]);
					}
				}
				else if((hold.peek()[0] == intr[b][0] && hold.peek()[1]+1 == intr[b][1]) || (hold.peek()[0] == intr[b][0] && hold.peek()[1]-1 == intr[b][1]))
				{
					if(b != a)
					{
						hold.push(intr[b]);
						a = b-1;
						b = 0;
					}
					else
					{
						b = 0;
						hold.push(intr[b]);
					}
				}
			}
		}
		int[] push1 = new int[2];
		push1[0] = 0;
		push1[1] = 0;
		hold.push(push1);
		while(!hold.empty())
		{
			ShortPath.push(hold.pop());
		}
		
		
		
	}
	// ======================================================================
	// YOUR CODE ENDS
	// ======================================================================
}