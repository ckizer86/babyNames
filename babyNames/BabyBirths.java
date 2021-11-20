/**
 * Print out the names for which 100 or fewer babies were born in a chosen CSV file of baby name data.
 * 
 * @author Duke Software Team 
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.File;

public class BabyBirths {
    
    public void printNames () {
        FileResource fr = new FileResource();
        int totalBirths = 0;
        StorageResource girlNames = new StorageResource();
        StorageResource boyNames = new StorageResource();
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            totalBirths += numBorn;
            if(rec.get(1).equals("F")){
                girlNames.add(rec.get(0));
            }
            else{
                boyNames.add(rec.get(0));
            }
        }
        
        System.out.println("Total Births = " + totalBirths);
        System.out.println("Girl Names:");
        for(String name : girlNames.data()){
            System.out.println(name);
        }
        
        System.out.println("Boy Names:");
        for(String name : boyNames.data()){
            System.out.println(name);
        }
    }
    
    public int getRank(int year, String name, String gender){
        //the person selects the year
        FileResource checkyear = new FileResource("names/yob" + year + "short.csv");
        
        //create counter initialized to 0
        int counter = 0;
        //look through each record of that file
        for(CSVRecord rec : checkyear.getCSVParser(false)){
            //if the record has the selected gender
            if(rec.get(1).equals(gender)){
                //increment counter
                counter += 1;
                //check for the given name
                if(rec.get(0).equals(name)){
                    //if it matches, return counter
                    return counter;
                }
            }
        }
        return -1;
    }
    
    public void testgetRank(){
        int year = 2012;
        String name = "Mason";
        String gender = "F";
        int test = getRank(year, name, gender);
        if(test == -1){
            System.out.println("That name does not appear for the selected gender");
        }
        
        else{
            System.out.println(name + " ranks at number " + test + " in the list of " + gender + " for year " + year);
        }
    }
    
    public String getName(int year, int rank, String gender){
        //opens file with corresponding year
        FileResource fr = new FileResource("names/yob" + year + "short.csv");
        //create counter and initialize to 0
        int counter = 0;
        //check through each record of the file
        for(CSVRecord rec : fr.getCSVParser(false)){
            //check to see if the gender is the gender selected
            if(rec.get(1).equals(gender)){
                //increment counter
                counter += 1;
                if(counter == rank){
                    //if the counter is equal to the rank return the rank
                    return rec.get(0);
                }
            }
            else{
                counter += 0;
            }
        }
        //if the rank doesn't exist in the file return no name
        return "NO NAME";
    }
    
    public void testgetName(){
        int year = 2012;
        int rank = 8;
        String gender = "F";
        String test = getName(year, rank, gender);
        System.out.println(test + " was ranked number " + rank + " in the year " + year);
    }
    
    public String whatIsNameInYear(String name, int year, int newYear, String gender){
        //get file for year they were born
        int currentRank = getRank(year, name, gender);
        
        if(currentRank == -1){
            System.out.println("Your name does not show up on the list of names in the year you were born for your selected gender");
        }
        //get rank of their name for the year they were born for their gender
        String newName = getName(newYear, currentRank, gender);
        
        return newName;
        //get file for the year they would like to check for new name at same rank
        //return the name
    }
    
    public void testwhatIsNameInYear(){
        String name = "Scarlett";
        int year = 2013;
        int newYear = 1986;
        String gender = "F";
        String newName = whatIsNameInYear(name, year, newYear, gender);
        if(newName == "NO NAME"){
            System.out.println("No name returned for your previous rank");
        }
        System.out.println(name + " born in " + year + " would be " + newName + " if she was born in " + newYear);
        
    }
    
    public int yearOfHighestRank(String name, String gender){
        //select multiple files
        DirectoryResource sr = new DirectoryResource();
        int currenthighest = -1;
        int yearhighest = -1;
        //it will loop through each file
        for(File f : sr.selectedFiles()){
            //in each file it will check the rank for the given name
            String year = f.getName();
            year = year.substring(3, 7);
            int thisyear = Integer.parseInt(year);
            int currentRank = getRank(thisyear, name, gender);
            if(currenthighest == -1){
                currenthighest = currentRank;
            }
            else{
                if (currentRank < currenthighest){
                    currenthighest = currentRank;
                    yearhighest = thisyear;
                }
                else{
                    currenthighest = currenthighest;
                }
            }
        }
        //if the name doesn't appear in any of the selected files it should return a -1
        //otherwise it should return the highest rank of the files
        return yearhighest;
    }
    
    public void testyohighestrank(){
        int test = yearOfHighestRank("Olivia", "F");
        if(test == -1){
            System.out.println("Your name didn't appear in any of the selected files");
        }
        else{
            System.out.println("The year you ranked the highest was " + test);
        }
    }
    
    public double getAverageRank(String name, String gender){
        //select list of files
        DirectoryResource dr = new DirectoryResource();
        //create a counter to keep track of each file that it goes through
        int counter = 0;
        //keep a running sum
        double sum = 0;
        //go through each file
        for(File f : dr.selectedFiles()){
            //get the rank of the name in each file
            String year = f.getName();
            year = year.substring(3, 7);
            int thisyear = Integer.parseInt(year);
            int currentRank = getRank(thisyear, name, gender);
            //if the rank is -1 just increment the counter
            if(currentRank == -1){
                counter += 1;
            }
            //otherwise increment counter and add rank to running sum
            else{
                sum += currentRank;
                counter += 1;
            }
        }
        //if sum is equal to 0 return -1
        if(sum == 0){
            return -1;
        }
        //otherwise divide sum by counter to get the average
        else{
            double avg = sum/counter;
            //return average
            return avg;
        
        }
    }
    
    public void testavgRank(){
        double test = getAverageRank("Mason", "M");
        if(test == -1){
            System.out.println("Your name didn't appear in any of the selected files");
        }
        else{
            System.out.println("Your name's average rank for the selected files was " + test);
            
        }
    }
    
    public int getTotalBirthsRankedHigher(int year, String name, String gender){
        //a file will be opened corresponding the the selected year
        FileResource fr = new FileResource("names/yob" + year + "short.csv");
        
        //this file will need to be set to a parser to parse through
        //there needs to be a running total initialized at 0
        int total = 0;
        //it needs to look through each record
        for(CSVRecord currentRow : fr.getCSVParser()){
            //if the gender is equal to the selected gender
            if(currentRow.get(1).equals(gender)){
                //if the name is equal to the selected name
                if(currentRow.get(0).equals(name)){
                    //return total
                    return total;
                }
                
                else{
                    //otherwise string births that gets the records births needs to be initialized
                    String births = currentRow.get(2);
                    //births needs to be parsed into an integer
                    int totalBirths = Integer.parseInt(births);
                    total += totalBirths;
                    //births needs to be added to total
                }
            }
            else{
                total += 0;
            }
        }
        
        return -1;
    }
    
    public void testgetTotalRankedHigher(){
        int year = 2012;
        String name = "Ethan";
        String gender = "M";
        int test = getTotalBirthsRankedHigher(year, name, gender);
        if(test == -1){
            System.out.println("Your name didn't appear in the selected year");
        }
        else{
            System.out.println("There were " + test + " total births of the names ranked higher than your selected name.");
            
        }
    }
}
