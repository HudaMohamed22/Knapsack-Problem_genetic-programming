/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genetic_ass1;

import java.util.ArrayList;

/**
 *
 * @author Hoda
 */
public class Chromosome {
    ArrayList<Boolean> genes  = new ArrayList<>();
    int c_fitness;

    public Chromosome(){

    }
    public Chromosome(ArrayList<Boolean> genes, int c_fitness) {
        this.genes = genes;
        this.c_fitness = c_fitness;
    }
    
}
