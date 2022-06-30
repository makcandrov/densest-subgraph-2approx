import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Main {
    private static ArrayList<String> filesTooLarge = new ArrayList<>();
    

    public static void main(String[] args){
        filesTooLarge.add("com-friendster.ungraph");

        runAllFilesInRandomOrder();
    }

    /**
     * Runs all the files in the data/inputs folder, except those in filesTooLarge to avoid the 
     * "OutOfMemory" error.
     */
    public static void runAllFilesInRandomOrder() {
        File f = new File("data/inputs/");
        ArrayList<String> files = new ArrayList<>(Arrays.asList(f.list()));
        Collections.shuffle(files);
        for (int i = 0; i < files.size(); i++) {
            String fileName = files.get(i).substring(0, files.get(i).length() - 6);
            if (!filesTooLarge.contains(fileName)) {
                System.out.println(String.format("Running %s...", fileName));
            run(fileName);
            }
        }
    }

    /**
     * Runs a single file, i.e. computes the densest subgraph from a file in data/inputs, writes the result in the 
     * file with the same name in data/outputs, and write the running time in a file with the same name in data/times
     * with the .time extension.
     * @param fileName Name of the file you want tu run (as it appears in inputs), without the extension
     */
    public static void run(String fileName) {
        Graph g = new Graph(String.format("data/inputs/%s.edges", fileName));
        long start = System.nanoTime();
        Graph h = g.approxDensestSubgraph();
        long end = System.nanoTime();
        h.export(String.format("data/outputs/%s.edges", fileName));
        writeDuration(fileName, end - start);
    }

    /**
     * Adds the duration given as parameter in a file in data/times with the name given as argument and a .time
     * extension. It doesn't erase the previous values in the file.
     * @param fileName Name of the file (without extension)
     * @param timeElapsed Duration to add in the file
     */
    private static void writeDuration(String fileName, long timeElapsed) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(String.format("data/times/%s.time", fileName), true));
            writer.append(String.valueOf(timeElapsed) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {writer.close();} catch (IOException e) {e.printStackTrace();}
        }
    }
}

