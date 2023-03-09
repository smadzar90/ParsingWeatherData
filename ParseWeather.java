import org.apache.commons.csv.*;
import edu.duke.*;
import java.io.*;
import java.util.Scanner;

public class ParseWeather
{
    
    public CSVRecord coldestHourInFile(CSVParser parser) {
        
        CSVRecord largestOfAll = null;
        
        for(CSVRecord currRecord : parser) {
            
            largestOfAll = lowestThanTwo(largestOfAll, currRecord);
        }
        
        return largestOfAll;
        
    }
    
    
    public CSVRecord lowestThanTwo(CSVRecord largestRecord, CSVRecord currRecord) {
        
        if(largestRecord == null) {
            largestRecord = currRecord;
        }
        
        else {
            double largestTemp = Double.parseDouble(largestRecord.get("TemperatureF"));
            double currTemp = Double.parseDouble(currRecord.get("TemperatureF"));
            
            if(currTemp < largestTemp && currTemp != -9999) {
                largestRecord = currRecord;
            }
        }
        
        return largestRecord;
    }
    
    public void fileWithColdestTemperature() {
        
        DirectoryResource dr = new DirectoryResource();
        CSVRecord largestOfAll = null;
        String fileName = "";
        String pathName = "";
        StorageResource list = new StorageResource();
        
        
        for(File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            CSVParser parser = fr.getCSVParser();
            
            if(largestOfAll == null) {
            largestOfAll = coldestHourInFile(parser);
            fileName = f.getName();
            pathName = f.getPath();
            
            }
            else { 
                
                CSVRecord currRecord = coldestHourInFile(parser);
                if(Double.parseDouble(currRecord.get("TemperatureF")) <
                   Double.parseDouble(largestOfAll.get("TemperatureF"))) {
                     
                       fileName = f.getName();
                       pathName = f.getPath();
                    }
                
                largestOfAll = lowestThanTwo(largestOfAll, currRecord);
                   
            }
            
        }
        
        System.out.println("Coldest day was in file " + fileName);
        System.out.println("Coldest temperature on that day was " + 
        largestOfAll.get("TemperatureF"));
        System.out.println("All the Temperatures on the coldest day were:");
        
        FileResource fr = new FileResource(pathName);
        CSVParser parser = fr.getCSVParser();
        
        for(CSVRecord record : parser) {
            System.out.println(record.get("DateUTC") + ": " + 
                               record.get("TemperatureF"));
        } 
        
    }
    
    public CSVRecord lowestHumidityInFile(CSVParser parser) {
        
        CSVRecord lowest = null;
        
        for(CSVRecord currRecord : parser) {
            
            lowest = getLowerHumidity(lowest, currRecord);
        }
        
        return lowest;
        
    }
    
    public CSVRecord getLowerHumidity(CSVRecord lowest, CSVRecord currRecord) {
        
        if(lowest == null) {
            lowest = currRecord;
        }
        
        else {
            
            String lowestH = lowest.get("Humidity");
            String currH = currRecord.get("Humidity");
            
            if(!lowestH.equals("N/A")&&(!currH.equals("N/A"))) {
                int lowestD = Integer.parseInt(lowestH);
                int currD = Integer.parseInt(currH);
                
                if(currD < lowestD) {
                    lowest = currRecord;
                }
                
            }
            else if(lowestH.equals("N/A")) {
                lowest = currRecord;
            }
            else {
                return lowest;
            }
           
        }
        
        return lowest;
        
    }
    
    public CSVRecord lowestHumidityInManyFiles() {
        
        DirectoryResource dr = new DirectoryResource();
        CSVRecord lowest = null;
        CSVRecord currRecord;
        
        for(File file : dr.selectedFiles()) {
            FileResource fr = new FileResource(file);
            CSVParser parser = fr.getCSVParser();
            
            if(lowest == null) {
                lowest = lowestHumidityInFile(parser); 
            }
            else {
                currRecord = lowestHumidityInFile(parser); 
                lowest = getLowerHumidity(lowest, currRecord);
            }
            
        }
        
        return lowest;
        
    }
    
    public Double averageTemperatureInFile(CSVParser parser) {
        
        int numOfRecords = 0;
        double tempSum = 0.0;
        
        for(CSVRecord record : parser) {
            
            double recordT = Double.parseDouble(record.get("TemperatureF"));
            
            numOfRecords++;
            tempSum = tempSum + recordT;
            
        }
        
        return tempSum / numOfRecords;
    }
    
    public Double averageTemperatureWithHighHumidityInFile(CSVParser parser, int value) {
        
        int recordSum = 0;
        double tempSum = 0.0;
        
        for(CSVRecord record : parser) {
            
            int humidity = Integer.parseInt(record.get("Humidity"));
            
            if(humidity >= value) {
                double temp = Double.parseDouble(record.get("TemperatureF"));
                recordSum++;
                tempSum += temp; 
            }
            
        }
        
        if(recordSum == 0) {
            return 0.0;
        }
        
        return tempSum / recordSum;
    }
    
    public void testAverageTemperatureWithHighHumidityInFile() {
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        Scanner scan = new Scanner(System.in);
        int value = scan.nextInt();
        
        double avarage = averageTemperatureWithHighHumidityInFile(parser, value);
        
        if(avarage != 0.0) {
            System.out.println("Avarage Temperature when Humidity is " + value 
            + " is " + avarage);
        }
        else {
            System.out.println("No temperatures with that humidity");
        }
    }
    
    public void testAverageTemperatureInFile() {
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        double avarage = averageTemperatureInFile(parser);
        
        System.out.println("Average Temperature in file is " + 
                           avarage);                   
    }
    
    public void testLowestHumidityInManyFiles() {
        
        CSVRecord lowest = lowestHumidityInManyFiles();
        System.out.println("Lowest Humidity was " + lowest.get("Humidity") +
                           " at " + lowest.get("DateUTC"));
    }
    
    public void testLowestHumidityInFile() {
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        CSVRecord lowestH = lowestHumidityInFile(parser);
        
        System.out.println("Lowest Humidity was " + lowestH.get("Humidity") +
                           " at " + lowestH.get("DateUTC"));
        
        
    }
    
    public void testColdestHourInFile() {
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        
        CSVRecord largest = coldestHourInFile(parser);
        System.out.println("Coldest temperature: " + largest.get("TemperatureF"));
        System.out.println("Time of occurrence: " + largest.get("TimeEDT"));
    }
    
    public static void main(String[] args) {
        
    }
    
    
}
