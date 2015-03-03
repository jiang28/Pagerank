
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pagerank;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author jiangc28
 */
public class Pagerank{

    /**
     * @param args the command line arguments
     */

    public static void pagerank(String inputfile,String outputfile, int NumIteration, double DampingFactor)
    {
	int numUrl = 0;                                 //init number of url
	HashMap adjacencyMatrix = new HashMap();        //create new hashmap
        HashMap valueTable = new HashMap();
	build_adjacency(adjacencyMatrix, inputfile);    //build the adjacency matrix 
           
	numUrl = adjacencyMatrix.keySet().size();       //resize #url according to the input file

        build_ValueTable(valueTable, numUrl);           //build the value table with the number of url
	/* System.out.println(numUrl);                    //testing
        
	
	 */
	for (int j = 0; j < NumIteration; j++) {
	    System.out.println("iteration:" + j);
	    join_rvt_am(valueTable, adjacencyMatrix, numUrl, DampingFactor);        //call join function to calculate pr(link)
	  
	}

	try
	    {
		FileWriter FileWriter = new FileWriter(outputfile);                 //call file writer to write into outputfile
		BufferedWriter BufferedWriter = new BufferedWriter(FileWriter);
		Iterator Iterator = valueTable.keySet().iterator();
	    
		while (Iterator.hasNext()) 
		    {                                                //when there is still element left 
		    int q = ((Integer)Iterator.next()).intValue();
		    BufferedWriter.write(q + " " + valueTable.get(Integer.valueOf(q)) + "\n");
		    }
		BufferedWriter.close();               //close buffer
    
	    } 
	catch (IOException localIOException) 
	    {
	    localIOException.printStackTrace();
	    }

	
    }

    public static void join_rvt_am(HashMap<Integer, Double> HashmapValueTable, HashMap<Integer, ArrayList<Integer>>HashmapAdjacency, int NumUrls, double dampingFactor)
    {
	Iterator iterator = HashmapAdjacency.keySet().iterator();
	HashMap intermediate_rvt = new HashMap(); //create the intermediate hashtable to store data
	for(int i=0; i<NumUrls; i++)
	    {
		intermediate_rvt.put(Integer.valueOf(i), Double.valueOf(0.0D));  //init the table with all 0s.
	    }

	double danglingValue=0.0D;
        while(iterator.hasNext())
	    {

		int sourceUrl = ((Integer)iterator.next()).intValue();
		ArrayList ArrayListTargetUrl=(ArrayList)HashmapAdjacency.get(Integer.valueOf(sourceUrl));    /*create a arraylist to store the value of sourceUrl*/
		int OutdegreeOfSourceUrl =ArrayListTargetUrl.size();

                for (int n = 0; n < OutdegreeOfSourceUrl; n++) {
		    int targetUrl = ((Integer)ArrayListTargetUrl.get(n)).intValue();
		    double UpdateValue = ((Double)intermediate_rvt.get(Integer.valueOf(targetUrl))).doubleValue() + ((Double)HashmapValueTable.get(Integer.valueOf(sourceUrl))).doubleValue() / OutdegreeOfSourceUrl; //Apply equation
		    intermediate_rvt.put(Integer.valueOf(targetUrl), Double.valueOf(UpdateValue));
		
		}
		
		if(OutdegreeOfSourceUrl==0)     //when there is no outdegree link
		    {
			danglingValue=danglingValue+((Double)HashmapValueTable.get(Integer.valueOf(sourceUrl))).doubleValue();   /*only count the inbound link*/
			
		    }
	    }

	double danglingValuePerPage= danglingValue/(double)NumUrls;        /*The dangling value per page is the dangling value/ the number of url*/
	for(int l=0; l<NumUrls; l++)
	    {
		HashmapValueTable.put(Integer.valueOf(l), Double.valueOf(((Double)intermediate_rvt.get(Integer.valueOf(l))).doubleValue()+danglingValuePerPage));  //update rank value table

	    }
	for(int l1 = 0; l1 < NumUrls; l1++)
	    {
		HashmapValueTable.put(Integer.valueOf(l1), Double.valueOf(dampingFactor * ((Double)HashmapValueTable.get(Integer.valueOf(l1))).doubleValue() + (1.0 - dampingFactor) * 1.0/(double)NumUrls)); //update rank value table with multiplying damping factor

	    }

    }
                
    public static void build_adjacency(HashMap<Integer, ArrayList<Integer>> HashMap1, String paramString)
    {
       try
	   {
	   FileReader FileReader = new FileReader(paramString);
	   BufferedReader BufferedReader = new BufferedReader(FileReader);
	   String s1;
	   while ((s1 = BufferedReader.readLine()) != null)
	       {
	          
		   String[] arrayOfString = s1.split(" ");         //split the string with space
	       int i = Integer.parseInt(arrayOfString[0]);
	       ArrayList Al = new ArrayList();
	       for (int k = 1; k < arrayOfString.length; k++) 
		   {
		   int j = Integer.parseInt(arrayOfString[k]);
		   Al.add(Integer.valueOf(j));
		   }
	       HashMap1.put(Integer.valueOf(i), Al);
            
	     
	       }
	   }
	           catch (IOException localIOException) 
		       {
			   localIOException.printStackTrace();
		       }
   }
 
      




    public static void build_ValueTable(HashMap<Integer, Double> HashmapValueTable, int NumUrl)
    {
	double pr =1.0D / NumUrl;                  /*init the value rank table with initial value of 1/number of url */
        for(int i=0; i<NumUrl; i++)
	    {
		HashmapValueTable.put(Integer.valueOf(i), Double.valueOf(pr));
	    }
    }


    public static void main(String args[])
    {
	String input_file = "pagerank.input";
        String output_file= "pagerank.output";
	int iteration =10;
	double damping_factor=0.85;
	
        pagerank(input_file, output_file, iteration, damping_factor);

    }

}
