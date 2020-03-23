/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Collections;
import java.util.HashSet;

/**
 *
 * @author John
 */
public class Maze {

    public boolean[][] maze;
    public static HashSet<Integer> ids = new HashSet<>();                     //added to have unique ids

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String BG_ANSI_BLACK = "\u001B[40m";
    public static final String BG_ANSI_RED = "\u001B[41m";
    public static final String BG_ANSI_GREEN = "\u001B[42m";
    public static final String BG_ANSI_YELLOW = "\u001B[43m";
    public static final String BG_ANSI_BLUE = "\u001B[44m";
    public static final String BG_ANSI_PURPLE = "\u001B[45m";
    public static final String BG_ANSI_CYAN = "\u001B[46m";
    public static final String BG_ANSI_WHITE = "\u001B[47m";

    public static final String[] colors = new String[]{
            BG_ANSI_RED,
            BG_ANSI_GREEN,
            BG_ANSI_YELLOW,
            BG_ANSI_BLUE,
            BG_ANSI_PURPLE,
            BG_ANSI_CYAN};


    public Maze(int rows, int cols, double prob) {
        maze = new boolean[rows][cols];
        create(prob);
    }

    private void create(double prob) {
        for (int i = 0; i < maze.length; i++)
            for (int j = 0; j < maze[0].length; j++)
                if (Math.random() < prob) maze[i][j] = true;
    }

    public void drawMazeSetsInColor(UnionFind uf) {

        String[] use_color = new String[maze.length * maze[0].length];
        String color;

        int color_loop = 0;

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {

                Integer in_set = uf.find(i + "," + j);   //find the set ID for the current cell
                if (in_set != null)     // if it has a cell ID then
                {
                    color = use_color[in_set]; // set a color to that cell

                    if (color == null)    // if that specific  color has is nothing
                    {
                        color = colors[color_loop++ % colors.length];
                        use_color[in_set] = color;
                    }

                } else color = "";



                if(UnionFindExampleDriver.cellid.containsKey(i+","+j)){          //if the map contains this specific key (location)   then it has a specific value (initial) to it
                    System.out.printf(color + "%-3s" + ANSI_RESET, in_set == null ? "#" : UnionFindExampleDriver.cellid.get(i+","+j)); // print value maped to that key
                }
                else if (!ids.contains(in_set)) {                // if the current cell number is not in the set then
                    System.out.printf(color + "%-3s" + ANSI_RESET, in_set == null ? "#" : in_set); // print the set number      IT ALSO MAINTAINS THE SET NUMBER IT BELONGS IF THERE IS AT LEAST ONE SPACE
                    ids.add(in_set); // add the set # to the set
                }
                else{
                    System.out.printf(color + "%-3s" + ANSI_RESET, in_set == null ? "#" : "."); //prints point if there is no animal in there and the set # has already been printed
                }

            }
            System.out.println(); //<------------------ after last column(of the current row) we go to the next row
        }
ids.clear();//maybe clear ids so that everytime this method restarts it checks if the id is there or not
    }

    }




    

