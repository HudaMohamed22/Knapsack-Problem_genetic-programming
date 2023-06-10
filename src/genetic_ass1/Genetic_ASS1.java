/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genetic_ass1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javafx.util.Pair;

/**
 *
 * @author Hoda
 */
public class Genetic_ASS1 {
    
    static final Double pc = 0.6;             //probability of crossover 
    static final Double pm = 0.1;            //probability of Mutation 
    static final int populationSize = 1000;  // number of individuals ex:[0,1,0,1,1,0]
    static final int generationSize = 3000;  //number of iterations
    static Random rand = new Random(); //instance of random class
    
    static ArrayList<Chromosome> population = new ArrayList<>(); 
    static ArrayList<Chromosome> selectedIndividuals = new ArrayList<>();     //individuals to make crossover & mutation on it
    static ArrayList<Integer> index_selectedIndividuals = new ArrayList<>();  //indices individuals to make crossover & mutation on it
    
    
    // to generate polulation with size =numOfGenes 
     public static void initializePopulation(int numOfGenes) {
        population = new ArrayList<>();  //you have to initialize static at the top of Fun
        for(int i=0; i<populationSize;i++)
        {
            Chromosome chromosome = new Chromosome();
            for(int j=0; j<numOfGenes;j++)
            {
                    chromosome.genes.add(rand.nextBoolean());
            }
            population.add(chromosome);
        }
    }
     
     // calculate fitness to every individual in the population  
    public static void fitnessEvaluation(ArrayList<Item> items , int knapsackSize) {
        for(int i=0; i<population.size();i++)
        {
            int total_c_fitness = 0 , total_C_weight = 0; //calculate total weight & fitness for each individuals
            for(int j=0;j<items.size();j++){
                if(population.get(i).genes.get(j)) {
                    total_C_weight += items.get(j).weight;
                    total_c_fitness += items.get(j).value;
                }
            }
          if(total_C_weight > knapsackSize || total_c_fitness==0){ //check if total chormosom weight break the constraints 
                 population.remove(i);   //remove individual that break the constraints  
                    i--;                 // 0 1 2 we remove 1 so 2 become in 1 place 
                    
                 }
           else{
            population.get(i).c_fitness = total_c_fitness;
           }
        }
    } 
    // way of selections strategy to select 2 individuals
    private static int rouletteWheelSelection() {
        
        if(isValidPopulation()){
            int totalFitness=0;
            for (int i=0;i<population.size() ;i++){
                       totalFitness +=population.get(i).c_fitness; 
                    
            }
 //********************************************************   
            int randomFitness = rand.nextInt(totalFitness); // random var between 0 and totalFitness
            int cumulativeFitness=0;
            int preCumulative=0;
            for(int j=0 ;j<population.size() ;j++) {
                cumulativeFitness +=population.get(j).c_fitness; // get the range of randomFitness
                if( cumulativeFitness > randomFitness)
                    return j;
            }
           
        }    
        return -20;
    }    
    public static void select2Individuals() {
        //**********************************************************************************************
            int counter =0;
            selectedIndividuals = new ArrayList<>();
            index_selectedIndividuals = new ArrayList<>();
            int index1=rouletteWheelSelection();
            int index2=0;
            index_selectedIndividuals.add(index1);
            selectedIndividuals.add(population.get(index1));
            
            index2=rouletteWheelSelection();
            while(index_selectedIndividuals.get(0)==index2 &&counter !=20){ // to check that we choose 2 different individuals 
                 index2=rouletteWheelSelection();
                 counter++;
 
            }
            index_selectedIndividuals.add(index2);
            selectedIndividuals.add(population.get(index2));
            
        }
                

            
    
    
    
    // check if we perform crossOver & perform one point crossover
    public static void crossOver(int numOfGenes){
        Double r2 = rand.nextDouble();
        if(r2 > pc){
            return;
        }
        int r1 = rand.nextInt(numOfGenes-1) ;
        Chromosome c1 = selectedIndividuals.get(0);
        Chromosome c2 = selectedIndividuals.get(1);
        for(int i=r1;i<numOfGenes;i++){
            Boolean tmp = c1.genes.get(i);
            c1.genes.set(i,c2.genes.get(i));
            c2.genes.set(i,tmp);
        }
    }
    public static void mutation(Chromosome chromosome) {
        for(int i=0;i<chromosome.genes.size(); i++){
            Double r3 = rand.nextDouble();
             
            if(r3 <=pm){
                chromosome.genes.set(i,!chromosome.genes.get(i));
                
            }
        }
    }
    
        public static void mutation_2Individuals() {
            mutation(selectedIndividuals.get(0));
            mutation(selectedIndividuals.get(1));
        
    }
    
        public static void Replacement() {
            for(int i=0;i<population.size() ;i++){
                if(i==index_selectedIndividuals.get(0)){
                     population.set(i,selectedIndividuals.get(0));
                }
                else if (i==index_selectedIndividuals.get(1)){
                     population.set(i,selectedIndividuals.get(1));
                }
            }
    }
       public static Boolean isValidPopulation(){
           if(population.size()>=2)
               return true;
           return false;
    }  
       
    public static void getFinalSolution(int numOfTestCase,ArrayList<Item> items){

        int maxFitness = 0, sol_index = -1 , numOfSelectedItems=0;
        for(int i=0;i<population.size();++i){
            if(population.get(i).c_fitness > maxFitness){  //get of max individual fitness in the last generation
                maxFitness = population.get(i).c_fitness;
                sol_index = i;
            }
        }
        if (sol_index != -1){
            System.out.println("Case " + numOfTestCase ); 
            for(int j=0; j<items.size();j++)            //iterate on chormsome bits(item size) to cal number of selected items
            {
                if(population.get(sol_index).genes.get(j)){
                    numOfSelectedItems++;
                }
            }
            System.out.println("NUM Of Selected Items " + numOfSelectedItems );
            System.out.println("Total value " +maxFitness );
            System.out.println("Items_Num  " + "Weight" +" Value");
            
            int x=1;
            for(int j=0; j<items.size();j++)      //iterate on chormsome bits (size) to print weight &value selected items
            {
                if(population.get(sol_index).genes.get(j)){
                 System.out.println("   "+x+"         "+items.get(j).weight +"     "+items.get(j).value);
                 x++;
                }
            }
        }
    }   
   
        
        
    
    public static void main(String[] args) {
        
        //printTest();
        
        int numOfTestCase=0,sizeOfKnapsack=0,numberOfItems=0;
        File myFile = new File("knapsack_input.txt");
        Scanner scanner;
        ArrayList<Item> items = new ArrayList<>();
        try {
            scanner = new Scanner(myFile);
            numOfTestCase= scanner.nextInt();
           
           for(int i=0;i<numOfTestCase;i++)
            {
               sizeOfKnapsack = scanner.nextInt();
                numberOfItems = scanner.nextInt() ;
                
               items = new ArrayList<>(numberOfItems);
                for(int j=0;j<numberOfItems;j++)
                {   
                    items.add( new Item(scanner.nextInt() , scanner.nextInt()) );
                }
                System.out.println("  ------------- ---------------------");
                
                initializePopulation(numberOfItems);
               
               
                for(int j = 0; isValidPopulation() && j< generationSize; j++ ){
                    fitnessEvaluation(items,sizeOfKnapsack);
                    //*********************************************************************************
                    if( isValidPopulation()){
                        select2Individuals();
                        crossOver(numberOfItems);
                         mutation_2Individuals();
                         Replacement();
                    } else
                        break;
                   
                    
       
                }
                //printPoplutation(numberOfGenes);
                getFinalSolution(i+1 ,items);
            }
           
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

    
    }
}