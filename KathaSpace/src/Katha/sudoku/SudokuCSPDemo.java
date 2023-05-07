package Katha.sudoku;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.CSP;
import aima.core.search.csp.Variable;
import aima.core.search.csp.examples.NQueensCSP;
import aima.core.search.csp.solver.CspListener;
import aima.core.search.csp.solver.CspSolver;
import aima.core.search.csp.solver.FlexibleBacktrackingSolver;

import java.util.HashMap;
import java.util.Optional;

public class SudokuCSPDemo {
    public final static String puzzle1 = "" + //
            "53..7...." + //
            "6..195..." + //
            ".98....6." + //
            "8...6...3" + //
            "4..8.3..1" + //
            "7...2...6" + //
            ".6....28." + //
            "...419..5" + //
            "....8..79";

    /** Example taken from https://de.wikipedia.org/wiki/Sudoku. */
    public final static String puzzle2 = "" + //
            ".3......." + //
            "...195..." + //
            "..8....6." + //
            "8...6...." + //
            "4..8....1" + //
            "....2...." + //
            ".6....28." + //
            "...419..5" + //
            ".......7.";

    /** Example taken from http://sudoku.tagesspiegel.de/sudoku-sehr-schwer. */
    public final static String puzzle3 = "" + //
            ".....9.7." + //
            "....82.5." + //
            "327....4." + //
            ".16.4...." + //
            ".5....3.." + //
            "....9.7.." + //
            "...6....5" + //
            "8.2......" + //
            "..42....8";

    public static void main(String[] args) {
        HashMap<Integer, Integer> sudokuMap = new HashMap<>();
        for(int i = 0; i < 81; i++){
            char charat = puzzle1.charAt(i);
            if(charat != '.'){
                sudokuMap.put(i, Character.getNumericValue(charat)); //char in integer
            }
        }
        CSP<Variable, Integer> csp = new SudokuCSP(sudokuMap);
        CspListener.StepCounter<Variable, Integer> stepCounter = new CspListener.StepCounter<>();
        CspSolver<Variable, Integer> solver;
        Optional<Assignment<Variable, Integer>> solution;
        solver = new FlexibleBacktrackingSolver<Variable, Integer>().setAll();
        solver.addCspListener(stepCounter);
        stepCounter.reset();
        solution = solver.solve(csp);
        if(solution.isPresent()){
            Assignment<Variable, Integer> a = solution.get(); //gibt var von solution
            StringBuilder result = new StringBuilder();
            for(int i = 0; i < a.getVariables().size(); i++){
                int value = a.getValue(new Variable("F"+(i+1)));
                result.append(" | ").append(value);
                if((i+1)%3 == 0)
                    result.append(" | \t");
                if((i+1)%9 == 0){
                    result.append("\n");
                    if((i+1)%27 == 0)
                        result.append("\n");
                }
            }
            System.out.println(result);
        }else{
            System.out.println("No solution :(");
        }
    }
}
