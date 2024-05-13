package src.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class IOUtil {
    private final static String filePath="/resources/equations.txt";

    /**
     * get an equation
     * @return an equation
     */
    public static String getAnEquation(){
        List<String> equations = getAllEquation();
        return equations.get(new Random().nextInt(equations.size()));
    }

    /**
     * get all equation
     * @return all equation
     */
    private static List<String> getAllEquation(){
        try {
            List<String> list=new ArrayList<>();
            Scanner scanner=new Scanner(new File(System.getProperty("user.dir")+filePath));
            while(scanner.hasNext()){
                list.add(scanner.next());
            }
            return list;
        } catch (FileNotFoundException e) {
            System.out.println(filePath+" doesn't exist!");
            return new ArrayList<>();
        }
    }
}
