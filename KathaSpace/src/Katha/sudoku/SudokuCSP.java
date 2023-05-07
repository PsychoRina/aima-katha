package Katha.sudoku;

import aima.core.search.csp.CSP;
import aima.core.search.csp.Domain;
import aima.core.search.csp.Variable;
import aima.core.search.csp.examples.DiffNotEqualConstraint;
import aima.core.search.csp.examples.NotEqualConstraint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SudokuCSP extends CSP<Variable, Integer> {

    public SudokuCSP(Map<Integer, Integer> setFields){
        for (int i = 0; i <= 80; i++) { //9x9
            addVariable(new Variable("F" + (i + 1)));
        }
        List<Integer> values = new ArrayList<>();
        for (int val = 1; val <= 9; val++) {
            values.add(val);
        }
        Domain<Integer> positions = new Domain<>(values);
        for (int i = 0; i < 81; i++) { //jeder Var wieder die Domain zugewiesen
            if(setFields.containsKey(i)){ //wenn Wert im Sudoku an index i, ist das TRUE
                setDomain(getVariables().get(i), new Domain<>(setFields.get(i))); //neue Domain mit nur dem Wert der an dieser Position im Sudoku ist
            }else{
                setDomain(getVariables().get(i), positions);
            }
        }
        //Constraint fue Nachbarvergleich in Zeile und Spalte
        for (int i = 0; i < 9; i++) { //Spalte
            for (int j = 0; j < 9; j++) { //Zeile
                Variable spalteVar = getVariables().get(i+j*9);
                Variable zeileVar = getVariables().get(j+i*9);
                for(int k = j+1; k < 9; k++){ //Wert der Var eins nach rechts, eins drunter
                    Variable spalteVarPlus = getVariables().get(i+k*9);
                    Variable zeileVarPlus = getVariables().get(k+i*9);
                    addConstraint(new NotEqualConstraint<>(spalteVar, spalteVarPlus));
                    addConstraint(new NotEqualConstraint<>(zeileVar, zeileVarPlus));
                }
            }
        }
        //Constraint fuer 3x3 Block
        int[] ersteNummerBox = {0,3,6,27,30,33,54,57,60};
        List<Variable> boxVar = new ArrayList<>(); //pro Durchgang Eintraege der 3x3 Felder speichern
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 3; j++){
                for(int k = 0; k < 3; k++){
                    boxVar.add(getVariables().get(ersteNummerBox[i]+k+j*9));
                }
            }
            for(int m = 0; m < 9; m++){ //innerhalb 3x3 Block Bedingung vergeben
                Variable var1 = boxVar.get(m);
                for(int n = m+1; n < 9; n++){ //mit der naechsten Zahl vergleichen
                    Variable var2 = boxVar.get(n);
                    addConstraint(new NotEqualConstraint<>(var1, var2));
                }
            }
            boxVar.clear(); //loeschen fuer die naechste 3x3 Box
        }


    }
}
