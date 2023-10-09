import java.util.*;

import static java.lang.Integer.parseInt;

// Main, read the input file and initiate the search

public class Main{
    public static void main(String[] args){
        String fileName = null;
        int wMinfilled;
        int wPref;
        int wPair;
        int wSecdiff;
        int pGamemin;
        int pPracmin;
        int pNotpaired;
        int pSection;

        // Check if executed with proper number of command line arguments
        if (args.length != 9){
            printUsage();
        }

        // Parsing command line input
        HashMap<String, Integer> penaltyValues = new HashMap<>();
        HashMap<String, Integer> weightValues = new HashMap<>();

        try {
            fileName = args[0];
            wMinfilled = parseInt(args[1]);
            wPref = parseInt(args[2]);
            wPair = parseInt(args[3]);
            wSecdiff = parseInt(args[4]);
            pGamemin = parseInt(args[5]);
            pPracmin = parseInt(args[6]);
            pNotpaired = parseInt(args[7]);
            pSection = parseInt(args[8]);

            penaltyValues.put("penGameMin",pGamemin);
            penaltyValues.put("penPracMin",pPracmin);
            penaltyValues.put("penNotPaired",pNotpaired);
            penaltyValues.put("penSection",pSection);

            weightValues.put("weightMinFilled", wMinfilled);
            weightValues.put("weightPref", wPref);
            weightValues.put("weightPair", wPair);
            weightValues.put("weightSecDiff", wSecdiff);

        } catch (Exception e){
            printUsage();
        }

        // Parse the input file to get initial structure
        try {
            Parser parser = new Parser(fileName);
            parser.s.printStructure();
            ParsedStructure struct = parser.s;
            HashSet<Integer> partAssigned = new HashSet<>();
            List<Map.Entry<Integer,Integer>> parts = struct.getPartAssign();
            for(Map.Entry<Integer, Integer> p : parts){
                partAssigned.add(p.getKey());
            }

            Constraints consts = new Constraints(struct, 0,0);

            State state = new State(struct, penaltyValues, weightValues, partAssigned, consts);


            Instance instance = new Instance(state);

            CheckGoal checkGoal = new CheckGoal(struct);

            checkGoal.CheckGoal(instance,state,struct);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Print the proper command line format example
     */
    private static void printUsage(){
        System.err.println("Command line execution: \n" +
                "java Main filename wminfilled wpref wpair wsecdiff pengamemin penpracticemin pennotpaired pensection\n" +
                "Ex. java Main input.txt 1 1 1 1 1 1 1 1");
        System.exit(1);
    }
}
