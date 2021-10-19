import java.util.Scanner;
/**
 * chessEngineMain is hopefully an epic chess program.
 * Grandmasters, I'm coming for you ;)
 * @author Albert Sandru
 * @version started: 11/12/2020
 */
public class chessEngineMain
{
    public static void main(String[]args)
    {
        /** first thing first, stating the obvious here, to play the game of chess, 
         * you need a board (8*8 two dimensional array), pieces (entries in the two dimensional array), 
         * and two intelligent players (human and computer).
         */
        String chessBoard[][] = 
        {
          {"bR","bN","bB","bQ","bK","bB","bN","bR"},
          {"bP","bP","bP","bP","bP","bP","bP","bP"},
          {"**","**","**","**","**","**","**","**"},
          {"**","**","**","**","**","**","**","**"},
          {"**","**","**","**","**","**","**","**"},
          {"**","**","**","**","**","**","**","**"},
          {"wP","wP","wP","wP","wP","wP","wP","wP"},
          {"wR","wN","wB","wQ","wK","wB","wN","wR"}
        };
        boardOutputter board = new boardOutputter();
        isLegal l = new isLegal();
        blackInCheck b = new blackInCheck();
        whiteInCheck w = new whiteInCheck();
        boolean gameOver=false;
        while(gameOver==false)
        {
            board.boardOutputter(chessBoard);
            /** 
             *white (human) makes a move.
             */
            Scanner keyboard = new Scanner(System.in);
            boolean isLegal=false;
            int oldRow=0;
            int oldColumn=0;
            int newRow=0;
            int newColumn=0;
            while(isLegal==false)
            {
                System.out.println("Please enter a legal move by entering the old row and old column of the piece you wish to move followed by the new row and the new column.");
                System.out.println("The rows are numbered from 0-7 up to down, and the columns are numbered from 0-7 left to right");
                oldRow=keyboard.nextInt();
                oldColumn=keyboard.nextInt();
                newRow=keyboard.nextInt();
                newColumn=keyboard.nextInt();
                while(oldRow<0 || oldRow>7 || oldColumn<0 || oldColumn>7 || newRow<0 || newRow>7 || newColumn<0 || newColumn>7)
                {
                    System.out.println("This moves a piece off of the board, please choose a different move to make.");
                    oldRow=keyboard.nextInt();
                    oldColumn=keyboard.nextInt();
                    newRow=keyboard.nextInt();
                    newColumn=keyboard.nextInt();
                }
                isLegal=l.isLegal(chessBoard, oldRow, oldColumn, newRow, newColumn);
                if(chessBoard[oldRow][oldColumn].substring(0,1).equals("b"))
                {
                    isLegal=false;
                }
            }
            //The move from the human might be illegal. We need to check if after the move, the white king is in check.
            int[]arr1=new int[4];
            arr1=w.whiteInCheck(chessBoard, oldRow, oldColumn, newRow, newColumn);
            if(arr1[0]==100 && arr1[1]==100 && arr1[2]==100 && arr1[3]==100)
            {
                System.out.println("Albert's Robot has won the game. You lost");
                break;
            }
            chessBoard[arr1[2]][arr1[3]]=chessBoard[arr1[0]][arr1[1]];
            chessBoard[arr1[0]][arr1[1]]="**";
            //Queen promotion
            if(chessBoard[arr1[2]][arr1[3]].substring(1,2).equals("P") && arr1[2]==0)
            {
                chessBoard[arr1[2]][arr1[3]]="wQ";
            }
            //CLEAR SCREEN
            System.out.print('\u000C');
            /**
             *black (computer) makes a legal and materialistic move.
             */
            boolean isLegalComputer=false;
            int oldRowComputer=0;
            int oldColumnComputer=0;
            int newRowComputer=0;
            int newColumnComputer=0;
            int [] bestMove=new int[1000000];
            for(int i=0; i<1000000; i=i+5)
            {
                while(isLegalComputer==false)
                {
                    oldRowComputer=(int) (8*Math.random());
                    oldColumnComputer=(int) (8*Math.random());
                    newRowComputer=(int) (8*Math.random());
                    newColumnComputer=(int) (8*Math.random());
                    isLegalComputer=l.isLegal(chessBoard, oldRowComputer, oldColumnComputer, newRowComputer, newColumnComputer);
                    if(chessBoard[oldRowComputer][oldColumnComputer].substring(0,1).equals("w"))
                    {
                        isLegalComputer=false;
                    }
                }
                isLegalComputer=false;
                bestMove[i]=oldRowComputer;
                bestMove[i+1]=oldColumnComputer;
                bestMove[i+2]=newRowComputer;
                bestMove[i+3]=newColumnComputer;
                //some pieces are more valuable than others
                if(chessBoard[newRowComputer][newColumnComputer].substring(1,2).equals("*"))
                {
                    bestMove[i+4]=0;
                }
                if(chessBoard[newRowComputer][newColumnComputer].substring(1,2).equals("P"))
                {
                    bestMove[i+4]=1;
                }
                if(chessBoard[newRowComputer][newColumnComputer].substring(1,2).equals("R"))
                {
                    bestMove[i+4]=5;
                }
                if(chessBoard[newRowComputer][newColumnComputer].substring(1,2).equals("N"))
                {
                    bestMove[i+4]=3;
                }
                if(chessBoard[newRowComputer][newColumnComputer].substring(1,2).equals("B"))
                {
                    bestMove[i+4]=3;
                }
                if(chessBoard[newRowComputer][newColumnComputer].substring(1,2).equals("Q"))
                {
                    bestMove[i+4]=9;
                }
            }
            int maximumValueSomething=0;
            int bestIndexPossible=4;
            for(int j=0; j<1000000; j=j+5)
            {
                if(bestMove[j+4]>=maximumValueSomething)
                {
                    maximumValueSomething=bestMove[j+4];
                    bestIndexPossible=j+4;
                }
            }
            chessBoard[bestMove[bestIndexPossible-2]][bestMove[bestIndexPossible-1]]=chessBoard[bestMove[bestIndexPossible-4]][bestMove[bestIndexPossible-3]];
            chessBoard[bestMove[bestIndexPossible-4]][bestMove[bestIndexPossible-3]]="**";
            //Queen promotion
            if(chessBoard[bestMove[bestIndexPossible-2]][bestMove[bestIndexPossible-1]].substring(1,2).equals("P") && bestMove[bestIndexPossible-2]==7)
            {
                chessBoard[bestMove[bestIndexPossible-2]][bestMove[bestIndexPossible-1]]="bQ";
            }
        }
        System.out.println("Game Over.");
        System.out.println("This was the final position on the chess board: ");
        board.boardOutputter(chessBoard);
    }
}

/**
 * Checks if moves are legal (conform to the official rules of chess).
 * @author Albert Sandru
 * @version 12/3/2020
 */
class isLegal
{
   public boolean isLegal(String a[][], int oldRow, int oldColumn, int newRow, int newColumn)
   {
       //it is illegal to start from an empty square in chess
       if(a[oldRow][oldColumn]=="**")
       {
           return false;
       }
       //it is illegal to skip your turn in chess
       if(oldRow==newRow && oldColumn==newColumn)
       {
           return false;
       }
       /**
        *Rooks have special rules
        */
       if(a[oldRow][oldColumn].substring(1).equals("R"))
       {
           //kings cannot be captured
           if(a[newRow][newColumn].substring(1,2).equals("K"))
           {
               return false;
           }
           if(oldRow!=newRow && oldColumn!=newColumn)
           {
               return false;
           }
           if(a[oldRow][oldColumn].substring(0,1).equals(a[newRow][newColumn].substring(0,1)))
           {
               return false;
           }
           if(oldRow==newRow)
           {
               if(oldColumn<newColumn)
               {
                   for(int i=oldColumn+1; i<newColumn; i++)
                   {
                       if(a[oldRow][i]!="**")
                       {
                           return false;
                       }
                   }
               }
               if(oldColumn>newColumn)
               {
                   for(int i=newColumn+1; i<oldColumn; i++)
                   {
                       if(a[oldRow][i]!="**")
                       {
                           return false;
                       }
                   }
               }
           }
           if(oldColumn==newColumn)
           {
               if(oldRow<newRow)
               {
                   for(int i=oldRow+1; i<newRow; i++)
                   {
                       if(a[i][oldColumn]!="**")
                       {
                           return false;
                       }
                   }
               }
               if(oldRow>newRow)
               {
                   for(int i=newRow+1; i<oldRow; i++)
                   {
                       if(a[i][oldColumn]!="**")
                       {
                           return false;
                       }
                   }
               }
           }
           return true;
       }
       /**
        *Knights have special rules
        */
       if(a[oldRow][oldColumn].substring(1).equals("N"))
       {
           //kings cannot be captured
           if(a[newRow][newColumn].substring(1,2).equals("K"))
           {
               return false;
           }
           //knigts cannot capture their own color pieces
           if(a[oldRow][oldColumn].substring(0,1).equals(a[newRow][newColumn].substring(0,1)))
           {
               return false;
           }
           //in the best case scenario, a knight has 8 possible moves, worst case scenario is no moves at all
           //knights jump, in an L shape. Possible coordinate changes: -2,+1;-1,+2;+1,+2;+2,+1;+2,-1;+1,-2;-1,-2;-2,-1.
           if(newRow-oldRow==-2 && newColumn-oldColumn==1)
           {
               return true;
           }
           if(newRow-oldRow==-1 && newColumn-oldColumn==2)
           {
               return true;
           }
           if(newRow-oldRow==1 && newColumn-oldColumn==2)
           {
               return true;
           }
           if(newRow-oldRow==2 && newColumn-oldColumn==1)
           {
               return true;
           }
           if(newRow-oldRow==2 && newColumn-oldColumn==-1)
           {
               return true;
           }
           if(newRow-oldRow==1 && newColumn-oldColumn==-2)
           {
               return true;
           }
           if(newRow-oldRow==-1 && newColumn-oldColumn==-2)
           {
               return true;
           }
           if(newRow-oldRow==-2 && newColumn-oldColumn==-1)
           {
               return true;
           }
           return false;
       }
       /**
        *Bishops have special rules
        */
       if(a[oldRow][oldColumn].substring(1).equals("B"))
       {
           //kings cannot be captured
           if(a[newRow][newColumn].substring(1,2).equals("K"))
           {
               return false;
           }
           //bishops can never capture their own color piece
           if(a[oldRow][oldColumn].substring(0,1).equals(a[newRow][newColumn].substring(0,1)))
           {
               return false;
           }
           //Bishops move diagonally, therefore they have 4 choices of direction to head in. Bishops are much like rooks.
           if((double) (newRow-oldRow)/(newColumn-oldColumn)!=1.0 && (double) (newRow-oldRow)/(newColumn-oldColumn)!=-1.0)
           {
               return false;
           }
           //now we have to make sure the bishop doesn't jump over any pieces
           if(newRow>oldRow && newColumn>oldColumn)
           {
               int column1=oldColumn+1;
               for(int row1=oldRow+1; row1<newRow; row1++)
               {
                   if(a[row1][column1] != "**")
                   {
                       return false;
                   }
                   column1++;
               }
           }
           if(newRow<oldRow && newColumn>oldColumn)
           {
               int column2=oldColumn+1;
               for(int row2=oldRow-1; row2>newRow; row2--)
               {
                   if(a[row2][column2] != "**")
                   {
                       return false;
                   }
                   column2++;
               }
           }
           if(newRow<oldRow && newColumn<oldColumn)
           {
               int column3=oldColumn-1;
               for(int row3=oldRow-1; row3>newRow; row3--)
               {
                   if(a[row3][column3] != "**")
                   {
                       return false;
                   }
                   column3--;
               }
           }
           if(newRow>oldRow && newColumn<oldColumn)
           {
               int column4=oldColumn-1;
               for(int row4=oldRow+1; row4<newRow; row4++)
               {
                   if(a[row4][column4] != "**")
                   {
                       return false;
                   }
                   column4--;
               }
           }
           return true;
       }
       /**
        *Queens have special rules
        */
       if(a[oldRow][oldColumn].substring(1).equals("Q"))
       {
           //kings cannot be captured
           if(a[newRow][newColumn].substring(1,2).equals("K"))
           {
               return false;
           }
           //Queens can never capture their own color piece
           if(a[oldRow][oldColumn].substring(0,1).equals(a[newRow][newColumn].substring(0,1)))
           {
               return false;
           }
           //Queens can move in any of 8 directions, provided they don't jump over any pieces.
           if(oldRow!=newRow && oldColumn!=newColumn && (double) (newRow-oldRow)/(newColumn-oldColumn)!=1.0 && (double) (newRow-oldRow)/(newColumn-oldColumn)!=-1.0)
           {
               return false;
           }
           //Queens are basically rooks and bishops combined
           //Here is the code I wrote for the rooks:
           if(oldRow==newRow)
           {
               if(oldColumn<newColumn)
               {
                   for(int i=oldColumn+1; i<newColumn; i++)
                   {
                       if(a[oldRow][i]!="**")
                       {
                           return false;
                       }
                   }
               }
               if(oldColumn>newColumn)
               {
                   for(int i=newColumn+1; i<oldColumn; i++)
                   {
                       if(a[oldRow][i]!="**")
                       {
                           return false;
                       }
                   }
               }
           }
           if(oldColumn==newColumn)
           {
               if(oldRow<newRow)
               {
                   for(int i=oldRow+1; i<newRow; i++)
                   {
                       if(a[i][oldColumn]!="**")
                       {
                           return false;
                       }
                   }
               }
               if(oldRow>newRow)
               {
                   for(int i=newRow+1; i<oldRow; i++)
                   {
                       if(a[i][oldColumn]!="**")
                       {
                           return false;
                       }
                   }
               }
           }
           //Here is the code I wrote for the bishops:
           if(newRow>oldRow && newColumn>oldColumn)
           {
               int column1=oldColumn+1;
               for(int row1=oldRow+1; row1<newRow; row1++)
               {
                   if(a[row1][column1] != "**")
                   {
                       return false;
                   }
                   column1++;
               }
           }
           if(newRow<oldRow && newColumn>oldColumn)
           {
               int column2=oldColumn+1;
               for(int row2=oldRow-1; row2>newRow; row2--)
               {
                   if(a[row2][column2] != "**")
                   {
                       return false;
                   }
                   column2++;
               }
           }
           if(newRow<oldRow && newColumn<oldColumn)
           {
               int column3=oldColumn-1;
               for(int row3=oldRow-1; row3>newRow; row3--)
               {
                   if(a[row3][column3] != "**")
                   {
                       return false;
                   }
                   column3--;
               }
           }
           if(newRow>oldRow && newColumn<oldColumn)
           {
               int column4=oldColumn-1;
               for(int row4=oldRow+1; row4<newRow; row4++)
               {
                   if(a[row4][column4] != "**")
                   {
                       return false;
                   }
                   column4--;
               }
           }
           return true;
       }
       /**
        *Pawns have special rules. They cannot move backwards, they capture diagonally, they can move one or two spaces forward on their first move,
        *however only one space forward on any other move. Once they reach the opposite side of the board, they become queens.
        */
       if(a[oldRow][oldColumn].substring(1).equals("P")) 
       {
           //kings cannot be captured
           if(a[newRow][newColumn].substring(1,2).equals("K"))
           {
               return false;
           }
           //white pawn has 4 ways to move
           if(a[oldRow][oldColumn].substring(0,1).equals("w"))
           {
               if(oldRow==6 && oldColumn==newColumn && newRow==4 && a[5][oldColumn]=="**" && a[4][oldColumn]=="**")
               {
                   return true;
               }
               if(oldColumn==newColumn && newRow==oldRow-1 && a[newRow][newColumn]=="**")
               {
                   return true;
               }
               if(newRow==oldRow-1 && newColumn==oldColumn+1 && a[newRow][newColumn].substring(0,1).equals("b"))
               {
                   return true;
               }
               if(newRow==oldRow-1 && newColumn==oldColumn-1 && a[newRow][newColumn].substring(0,1).equals("b"))
               {
                   return true;
               }
               return false;
           }
           //black pawn has 4 ways to move
           if(a[oldRow][oldColumn].substring(0,1).equals("b"))
           {
               if(oldRow==1 && oldColumn==newColumn&& newRow==3 && a[2][oldColumn]=="**" && a[3][oldColumn]=="**")
               {
                   return true;
               }
               if(oldColumn==newColumn&& newRow==oldRow+1 && a[newRow][newColumn]=="**")
               {
                   return true;
               }
               if(newRow==oldRow+1 && newColumn==oldColumn+1 && a[newRow][newColumn].substring(0,1).equals("w"))
               {
                   return true;
               }
               if(newRow==oldRow+1 && newColumn==oldColumn-1 && a[newRow][newColumn].substring(0,1).equals("w"))
               {
                   return true;
               }
               return false;
           }
       }
       /**
        *Kings have special rules. They are the only pieces that cannot be captured, only checked, or mated.
        */
       if(a[oldRow][oldColumn].substring(1).equals("K")) 
       {
           //kings cannot be captured
           if(a[newRow][newColumn].substring(1,2).equals("K"))
           {
               return false;
           }
           //kings can never capture their own color piece
           if(a[oldRow][oldColumn].substring(0,1).equals(a[newRow][newColumn].substring(0,1)))
           {
               return false;
           }
           if(Math.abs(newRow-oldRow)>1 || Math.abs(newColumn-oldColumn)>1)
           {
               return false;
           }
           return true;
       } 
       return true;
   }
}

/**
 * Checks if the black king is in check and returns a move that halts the check.
 * @author Albert Sandru
 * @version 12/18/2020
 */
class blackInCheck
{
    public int[] blackInCheck(String a[][], int oldRow, int oldColumn, int newRow, int newColumn)
    {
        int []arr=new int[4];
        System.out.println("The black king is in check.");
        return arr;
    }
}

/**
 * Checks if the white king is in check and returns a move that halts the check.
 * @author Albert Sandru
 * @version 12/18/2020
 */
class whiteInCheck
{
    public int[] whiteInCheck(String a[][], int oldRow, int oldColumn, int newRow, int newColumn)
    {
        int[]arr=new int[4];
        /**
         * We have a move from the human, but it may be illegal. If after the move, the white king can be captured by a black piece, we need a different move for white.
         * If no other moves can be found, this is checkmate, and white loses.
         * If white is lost, we will make all entries in the arr array equal to 100, in order to signal back to the main program that white indeed lost.
          */
        isLegal legal = new isLegal();
        int kingLocationRow=0;
        int kingLocationColumn=0;
        if(a[oldRow][oldColumn].substring(1,2).equals("K"))
        {
            kingLocationRow=newRow;
            kingLocationColumn=newColumn; 
            //Now, we will check 10,000 black moves, if any of them results in the capture of the white king, we know the user entered an illegal move. 
            boolean isLegalComputer=false;
            int oldRowComputer=0;
            int oldColumnComputer=0;
            int newRowComputer=0;
            int newColumnComputer=0;
            for(int k=0; k<10000; k++)
            {
                while(isLegalComputer==false)
                {
                    oldRowComputer=(int) (8*Math.random());
                    oldColumnComputer=(int) (8*Math.random());
                    newRowComputer=(int) (8*Math.random());
                    newColumnComputer=(int) (8*Math.random());
                    isLegalComputer=legal.isLegal(a, oldRowComputer, oldColumnComputer, newRowComputer, newColumnComputer);
                    if(a[oldRowComputer][oldColumnComputer].substring(0,1).equals("w"))
                    {
                        isLegalComputer=false;
                    }
                }
                if(newRowComputer==kingLocationRow && newColumnComputer==kingLocationColumn)
                {
                    System.out.println("You moved into check, thus I will have to end this program. Please be more careful next time");
                    System.exit(0);
                }
            }
        }   
        arr[0]=oldRow;
        arr[1]=oldColumn;
        arr[2]=newRow;
        arr[3]=newColumn;
        return arr;
    }
}