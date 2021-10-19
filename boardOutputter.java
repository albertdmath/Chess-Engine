
/**
 * class boardOutputter outputs the current state of the board.
 * @author Albert Sandru
 * @version 11/12/2020
 */
public class boardOutputter
{
    public void boardOutputter(String[][]a)
    {
        for(int i=0; i<8; i++)
        {
            for(int j=0; j<8; j++)
            {
                String str = a[i][j];
                System.out.print(str + " ");
            }
            System.out.println();
        }
    }
}
