package neu.edu.cs6650;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GenerateOutput {

//  Map<Integer, Integer> map;
  Map<Integer, Integer> map;
  String fileName;

  public GenerateOutput(Map<Integer, Integer> map, String fileName) {
    this.map = map;
    this.fileName = fileName;
  }
  //Delimiter used in CSV file
  private static final String COMMA_DELIMITER = ",";
  private static final String NEW_LINE_SEPARATOR = "\n";

//  //CSV file header
//  private static final String FILE_HEADER = "id,firstName,lastName,gender,age";

  public void writeCsvFile() {
    FileWriter fileWriter = null;

    try {
      fileWriter = new FileWriter(fileName);

//      //Write the CSV file header
//      fileWriter.append(FILE_HEADER.toString());

      //Add a new line separator after the header
      fileWriter.append(NEW_LINE_SEPARATOR);

      //Write a new student object list to the CSV file
      for (int key : map.keySet()) {
        fileWriter.append(String.valueOf(key));
        fileWriter.append(COMMA_DELIMITER);
        fileWriter.append(String.valueOf(map.get(key)));
        fileWriter.append(NEW_LINE_SEPARATOR);
      }



      System.out.println("CSV file was created successfully !!!");

    } catch (Exception e) {
      System.out.println("Error in CsvFileWriter !!!");
      e.printStackTrace();
    } finally {

      try {
        fileWriter.flush();
        fileWriter.close();
      } catch (IOException e) {
        System.out.println("Error while flushing/closing fileWriter !!!");
        e.printStackTrace();
      }

    }
  }
}
