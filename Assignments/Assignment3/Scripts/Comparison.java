

/**
 * This program will compare the result of Include script to the result of Understand only.
 */
import java.io.*;
import java.util.*;

public class Comparison
{
	
	public static List<String> includeList;
	private static List<String> understandList;
	private static List<String> overlap;	
	private static String otherFileName = "IncludeDependencies.txt"; // the file generated by the include script.
	private static String understandFileName = "UnderstandDependency.txt"; // the understand file it can be csv as well
	private static String outputName = "Include_Vs_Understand.txt";
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException
	{
		File other = new File(otherFileName);
		File understand = new File(understandFileName);
		includeList = new ArrayList<String>();
		overlap = new ArrayList<String>();
		understandList = new ArrayList<String>();
		
		FileInputStream fisI = null;
		FileInputStream fisI2 = null;
		
		BufferedInputStream bis = null;
		BufferedInputStream bis2 = null;
		
		DataInputStream dis = null;
		DataInputStream dis2 = null;
	 
		try
		{
			fisI = new FileInputStream(other);
	 		bis = new BufferedInputStream(fisI);
			dis = new DataInputStream(bis);
	 
			while (dis.available() != 0) 
			{	
				includeList.add(dis.readLine()); // adding the include results to a list.
				
			}
			System.out.println("Other List  before overlapping old Size:" + includeList.size());
			
		// dispose all the resources after using them.
		fisI.close();
		bis.close();
		dis.close();
		  
		fisI2 = new FileInputStream(understand);
	 
		bis2 = new BufferedInputStream(fisI2);
		dis2 = new DataInputStream(bis2);
		String line;
		while (dis2.available() != 0) // here we will format the input of understand to match the include input so we can comare them 
		{ 
			//Reads each line in the Understand file, and adds to the list
			//Also removes the reference numbers at the end
			line = dis2.readLine();
			line =line.replaceAll(",\\d+,\\d+,\\d+", ""); // removing the last numbers the digits
			line = line.replace(",", " "); // removing the comma and put space instead
			
			String split[] = line.split(" "); // i want to get the parts before the space 
			
			String split2[] = split[1].split("\\\\"); // the goal is to get to the .h or .c file only so we split it
			
			line = split[0] + " "+split2[split2.length-1]; // the .c or .h file will always be the last token or the right one
			understandList.add(line);	
			
			
		}	 
		// dispose all the resources after using them.
		fisI2.close();
		bis2.close();
		dis2.close();		
		System.out.println("Understand List before overlapping old Size:" + understandList.size());
		
		
		System.out.println("Please Wait....");
		
		//Creates a copy of the Understand List for overlap
		List<String> newUnderstand = new ArrayList<String>(understandList); 
		
		understandList.retainAll(includeList); // OVERLAP list
		newUnderstand.removeAll(understandList); // new Understand list
		includeList.removeAll(understandList); // new Include list
		
		System.out.println();
		System.out.println("other List After removing Overlapping new Size: " + includeList.size());
		System.out.println("Overlaping List new Size: " + understandList.size());
		System.out.println("Understand List After removing Overlapping new Size: " + newUnderstand.size());
			
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
              new FileOutputStream(outputName), "utf-8"));
			  
		//Writing the lists into a new file  
		writer.write("-----------------Overlap-------------------");		  
		  
		for (String e: understandList)
		{
			writer.newLine();
			writer.write(e);		  
		}
		writer.newLine();
		writer.write("---------------------Understand -------------------");
		for (String e: newUnderstand) 
		{
			writer.newLine();
			writer.write(e);		  
		}		  
		writer.newLine();
		writer.write("----------------------- IncludeFile -------------------");
		for (String e: includeList)
		{
			writer.newLine();
			writer.write(e);	
			//System.out.println(e);
		}
		  
		writer.flush();
		writer.close();
				
		} catch (FileNotFoundException e) 
		{
		  e.printStackTrace();
		} catch (IOException e) 
		{
		  e.printStackTrace();
		}
	}	
}