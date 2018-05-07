package am.aua;

import am.aua.placement.PlacementInputReader;

public class Main {
    public static void main(String[] args) {

//        PlacementObjective objective = TotalWirelengthObjective.getInstance();
//        PartitioningAlgorithm algorithm = PartitioningAlgorithm.FIDUCCIA_MATTHEYSES;
//
//        PlacementSolver solver = new PlacementSolverByPartitioning(objective, algorithm);
//
//        List<Module> modules = new ArrayList<>();
//        modules.add(Module.withId(1));
//        modules.add(Module.withId(2));
//        modules.add(Module.withId(3));
//        modules.add(Module.withId(4));
//
//        List<Net> nets = new ArrayList<>();
//        nets.add(new Net(Module.withId(1), Module.withId(3)));
//        nets.add(new Net(Module.withId(2), Module.withId(4)));
//
//        PlacementResult result = solver.solve(modules, nets, 2, 2);
//        for (Module module : modules) {
//            System.out.println(result.getSlotForModule(module));
//        }
//
//        ObjectMapper mapper = new ObjectMapper();
//
//        try {
//            PlacementInput input = mapper.readValue("{\n" +
//                    "\t\"height\" : 2,\n" +
//                    "\t\"width\" : 2,\n" +
//                    "\t\"moduleCount\" : 4,\n" +
//                    "\t\"nets\" : [\n" +
//                    "\t\t[1, 3],\n" +
//                    "\t\t[2, 4]\n" +
//                    "\t]\n" +
//                    "}", PlacementInput.class);
//
//            System.out.println(input);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }

        PlacementInputReader reader = new PlacementInputReader();
        PlacementInput input = reader.read();


    }
}
