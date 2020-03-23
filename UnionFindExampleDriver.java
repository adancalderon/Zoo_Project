

import java.util.*;
//THIS WAS FOR A MY COMP 2230 MIDTERM ON MARCH 21

/**
 *
 * @author John
 */
public class UnionFindExampleDriver {

    public static UnionFind<String> uf = new UnionFind<String>();
    public static ArrayList<Integer> regions = new ArrayList<>();     //POTENTIAL FOR CHANGE
    public static HashSet<Integer> setIds = new HashSet<>();     // setIds for existing set (UNIQUE)
    public static Set<Integer> currentAnimals = new HashSet<>();     // this set will hold the setID for the on going animals
    public static ArrayList<String> currentSet = new ArrayList<>();   // list that provides the (row+","+col) of each cell from that set
    public static Scanner scan = new Scanner(System.in);
    public static Scanner sc = new Scanner(System.in);
    public static Scanner scar = new Scanner(System.in);
    public static HashMap<Integer, String> animalMap = new HashMap<>();  // map that links every single (unique)setID(key)  to a value (animal name)

    public static HashMap<String, String> cellid = new HashMap<>();  // map that link every unique (cell) with an initial value(initials)

    public static HashMap<Integer, Integer> animalCount = new HashMap<>(); // map that link a regionID with number of animals in that ID
    public static HashMap<Integer,Integer> regioncap = new HashMap<>(); // this map will link each regionID to a capacity

    public static HashMap<Integer, ArrayList<String>> setLocations = new HashMap<>();    // this MAP holds a list with each location (row+","+col) for a given region id


    public static String initials;                   // String that takes animalName and makes it to Initials
    public static String animalName;           // user input name of the animal
    public static int number;              // number of animals taken
    public static int regionId;               // id of the region specified (user input) (from)
    public static int regionNum;            // id of the region specified from the user (to)

    public static int input;
    public static Scanner reader = new Scanner(System.in);

    public static Maze grid = new Maze(25, 25, 0.50);      //25 x 25 MAZE     with 50% percent chance of hitting a wall   THIS IS CREATED IN THE CLASS ONCE THE PROGRAM RUNS

    public static void main(String[] args) {
        start();
        mainMenu();


//        //  showRegions();              it shows regions ids from a set
//        displayCapacity();
//        //  uf.display();
//
//
//        addAnimal();
//        //showMap();
//        //showMap();
//        showMap();
//        //addAnimal();
//      //  showMap();
//        //showMap();
//        showMap();
//        displayCapacity();
//        moveanimal();
//        showMap();
//        showMap();
    }
    public static void start(){ //required info to get before doing anything
        findRegions();
        showCurrentCells();
        shuffleLocations();
    }


    public static void mainMenu() {

        while (input != 6) {
            System.out.println("***************************************************************************\n" +
                    "*                           Z O O K E E P E R                             "+ "*"+
                    "\n" +
                    "***************************************************************************");
            System.out.println("Choose...\n" +
                    "1. Show map of Zoo\n" +
                    "2. Place a ZOO animal in the map.\n" +
                    "3. Show region capacity.\n" +
                    "4. Move an animal to another enclosure\n" +
                    "5. Display the whole Zoo list (ZOO LEGEND)\n" +
                    "6. Exit \n" +
                    "Choose>> ");
            try {
                input = reader.nextInt();

                if (input == 1) {
                    showMap();
                } else if (input == 2) {
                    addAnimal();
                } else if (input == 3) {
                    displayCapacity();
                } else if (input == 4) {
                    moveanimal();
                } else if (input == 5) {
                    displayZoo();
                }
            }catch (InputMismatchException e){
                System.out.println("**************************");
                System.out.println("INTEGERS ONLY FOR MENU OPTIONS PLEASE");
                System.out.println("TRY AGAIN");
                System.out.println("**************************");
                reader.nextLine();
            }
        }
    }

    public static void showMap() {           //displays the user with the colored MAP (animals move every time this method is called)

   //     findRegions();       // find regions since the map is created
        System.out.println("\n\t\t\t\t\t\t\tBC WILDLIFE PARK");
        shuffleLocations(); //shuffle locations BEFORE displaying the colored map
        grid.drawMazeSetsInColor(uf);        //SHOWS the colored grid

    }

    public static void findRegions() {        // finds regions connected where there are no boundaries (false) to the left, up, right and down
        for (int row = 0; row < grid.maze.length; row++) {  //row loop
            for (int col = 0; col < grid.maze[row].length; col++) { //column loop
                if (!grid.maze[row][col]) { //if the current does not have a boundary (false) then add each cell to the their own set

                    int cell_id = uf.add(row + "," + col);   // since the current cell  does not have a boundary we add it to our UnionFind

                    Integer cell_up_id = uf.find((row - 1) + "," + col);  // find method will return the set # where the object is found the previous row same column (up_
                    Integer cell_left_id = uf.find(row + "," + (col - 1));    // method that will return set# if there is something found on the left

                    if (cell_up_id != null) {
                        uf.union(cell_id, cell_up_id.intValue());
                    }

                    cell_id = uf.find(row + "," + col);       //find the id for the current cell

                    if (cell_left_id != null) {
                        uf.union(cell_id, cell_left_id.intValue());
                    }

                }
            }
        }

    }


    public static void showCurrentCells() {      //needed method to update EVERY REGION CREATED FROM THE SHOWMAP --> USE IT ONCE! BEFORE SHOWING MAP gets Ids
        for (int row = 0; row < grid.maze.length; row++) {  //row loop
            for (int col = 0; col < grid.maze[row].length; col++) { //column loop
                if (!grid.maze[row][col]) {             // AT THIS POINT EACH CELL HAS THEIR OWN CELL ID
                    regions.add(uf.find(row + "," + col)); // ADD EACH CELL IDS TO A LIST
                }
            }
        }

        //Collections.sort(regions);  //useless debug

        for (int i = 0; i < regions.size(); i++) {
            //  System.out.println(regions.get(i));  // use this TO POTENTIALY CHANGE GRID CONTENT
            setIds.add(regions.get(i));  // we take advantage of set properties (no duplicates) unique elements to PUT all set Ids
            animalMap.put(regions.get(i), null);    // we might as well add every key(setID) to our map
        }

        for (Integer id: setIds){
            regioncap.put(id,uf.count(id));

        }

    }


    public static void displayZoo() {       //displays the entire zoo as a legend to follow (good for keeping track of all the information)
        System.out.println("REGION #: \t" + " CAPACITY: \t" + " ANIMAL:\t"+" ANIMAL QUANTITY: ");
        Iterator<Integer> it = setIds.iterator();

        while (it.hasNext()) {
            Integer id = it.next();
            int capacity = uf.count(id);
            System.out.print("#" + id + "\t\t\t\t" + capacity + "\t\t");
            if (animalMap.get(id) == null) {
                System.out.print("-"+ "\t\t");
            } else {
                System.out.print(animalMap.get(id)+ "\t\t");
            }
            if (animalCount.get(id) == null) {
                System.out.println("-");
            } else {
                System.out.println(animalCount.get(id));
            }
        }
    }

    public static void displayCapacity(){ // prompts the user for spots where they could potentially add according to the number entered
        int spots;
        Scanner read = new Scanner(System.in);
        System.out.println("How many spots do you need?");
        try {
            spots = read.nextInt();

            System.out.println("REGION #: \t" + " CAPACITY: \t" + " ANIMAL:\t" + " ANIMAL QUANTITY: ");
            Iterator<Integer> it = setIds.iterator();
            while (it.hasNext()) {
                Integer id = it.next();
                int capacity = uf.count(id);
                if (capacity >= spots) {            // only display if the capacity of the set/region id has more the same or more spots
                    System.out.print("#" + id + "\t\t\t\t" + capacity + "\t\t");

                    if (animalMap.get(id) == null) {
                        System.out.print("-" + "\t\t");
                    } else {
                        System.out.print(animalMap.get(id) + "\t\t");
                    }
                    if (animalCount.get(id) == null) {
                        System.out.println("-");
                    } else {
                        System.out.println(animalCount.get(id));
                    }
                }
            }
        }catch (InputMismatchException e){
        System.out.println("**************************");
        System.out.println("INTEGERS ONLY FOR SPOTS PLEASE");
        System.out.println("TRY AGAIN");
        System.out.println("**************************");
        scar.nextLine();
    }

    }

    public static void addAnimal() { // adds an animal(initials) to the grid
        System.out.println("What's the name of the animal being placed in the zoo?");
        animalName = scan.nextLine();
        initials = animalName.substring(0, 3).toUpperCase();  //first three letters
        //System.out.println(initials);
        System.out.println("How many " + animalName + "(s) are you housing?");
        number = sc.nextInt();

        System.out.println("These are the possible regions you can place your: " + number + " " + animalName + "(s)");
        System.out.println("Choose a REGION ID#: ");
        System.out.println("REGION #: \t" + " CAPACITY: \t" + " ANIMAL:\t" + "ANIMAL QUANTITY:");
        Iterator<Integer> it = setIds.iterator();
        while (it.hasNext()) {
            Integer id = it.next();
            int capacity = uf.count(id);
            if (capacity >= number && animalMap.get(id) == null) {     // only display regions where there is enough capacity for the number specified AND there are no animals
                System.out.print("#" + id + "\t\t\t\t" + capacity + "\t\t");
                if (animalMap.get(id) == null) {
                    System.out.print("-" + "\t\t");
                } else {
                    System.out.print(animalMap.get(id) + "\t\t");
                }
                if (animalCount.get(id) == null) {
                    System.out.println("-");
                } else {
                    System.out.println(animalCount.get(id));
                }
            }
        }
        try {
            regionId = scar.nextInt();

    while (!(setIds.contains(regionId) && number <= regioncap.get(regionId) && animalMap.get(regionId) == null)){       // we loop until an appropriate region is entered
        System.out.println("WRONG REGION ENTERED,PLEASE ENTER ONE OF THE FOLLOWING IDs");
        System.out.println("Choose a REGION ID#: ");
        System.out.println("REGION #: \t" + " CAPACITY: \t" + " ANIMAL:\t" + "ANIMAL QUANTITY:");
        Iterator<Integer> it2 = setIds.iterator();
        while (it2.hasNext()) {
            Integer id = it2.next();
            int capacity = uf.count(id);
            if (capacity >= number && animalMap.get(id) == null) {          //asking AGAIN IN CASE WRONG INPUT
                System.out.print("#" + id + "\t\t\t\t" + capacity + "\t\t");
                if (animalMap.get(id) == null) {
                    System.out.print("-" + "\t\t");
                } else {
                    System.out.print(animalMap.get(id) + "\t\t");
                }
                if (animalCount.get(id) == null) {
                    System.out.println("-");
                } else {
                    System.out.println(animalCount.get(id));
                }
            }
        }


        regionId = scar.nextInt();
    }
            if (setIds.contains(regionId) && number <= regioncap.get(regionId) && animalMap.get(regionId) == null) {  //checks that the input(region) EXISTS, the number(input) is less than or equall to the region capacity and that animal does not exist in that region
                for (Object location : uf.get(regionId)) {        //getting ENTIRE SET OF the specified region ID
                    currentSet.add((String) location);
                }
                Collections.shuffle(currentSet);
                for (int i = 0; i < number; i++) {// putting the set coordinates into an arraylist as many as the user inputed
                    String location = currentSet.remove(0);                                                  // remember to clear arraylist once used
//                    int row = Integer.parseInt(location.substring(0, 1));
//                    int col = Integer.parseInt(location.substring(location.length() - 1));
                    cellid.put(location, initials);
                    // System.out.println(location);
                }
                currentAnimals.add(regionId);
                animalCount.put(regionId, number);
                animalMap.put(regionId, animalName);
                // REMEMBER TO ADD animalName to MAP when
                currentSet.clear();              // clear currentSet in order to take a fresh new batch of animals // no need to
                System.out.println(number+" "+animalName+"(s)"+"were ADDED to region # "+regionId);
            }
            else {
                System.out.println("DEBUGGG");       //debug not going to reach here (probably)
            }


        }catch (InputMismatchException e){
            System.out.println("**************************");
            System.out.println("INTEGERS ONLY FOR REGION ID PLEASE");
            System.out.println("TRY AGAIN");
            System.out.println("**************************");
            scar.nextLine();
        }
    }

    public static void moveanimal() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Which region # do you want to move from?");     //assume the user knows which region to move from

        regionNum = reader.nextInt();            // id/region to move animals from


        while (!(animalMap.get(regionNum)!=null)){       // we loop until an appropriate region is entered
            System.out.println("WRONG REGION ENTERED,PLEASE ENTER ONE OF THE FOLLOWING IDs that have animals to take from");
            System.out.println("Choose a REGION ID#: ");
            System.out.println("REGION #: \t" + " CAPACITY: \t" + " ANIMAL:\t" + "ANIMAL QUANTITY:");
            Iterator<Integer> it2 = setIds.iterator();
            while (it2.hasNext()) {
                Integer id = it2.next();
                int capacity = uf.count(id);
                if (animalMap.get(id) != null) {                  //will displays regions WHERE THERE IS SOMETHING to move FROM in case the input is not correct
                    System.out.print("#" + id + "\t\t\t\t" + capacity + "\t\t");
                    if (animalMap.get(id) == null) {
                        System.out.print("-" + "\t\t");
                    } else {
                        System.out.print(animalMap.get(id) + "\t\t");
                    }
                    if (animalCount.get(id) == null) {
                        System.out.println("-");
                    } else {
                        System.out.println(animalCount.get(id));
                    }
                }
            }


            regionNum = scar.nextInt();
        }



        if (animalMap.containsKey(regionNum)) {
            System.out.println("Region #" + regionNum + " is currently housing " + animalMap.get(regionNum));
            System.out.println("You need an area capable of holding " + animalCount.get(regionNum) + " " + animalMap.get(regionNum) + " and an area not currently in use");

            System.out.println("These are the possible regions you can place your: " + animalCount.get(regionNum) + " " + animalMap.get(regionNum) + "(s)");

            System.out.println("Choose a REGION ID#: ");
            System.out.println("REGION #: \t" + " CAPACITY: \t" + " ANIMAL:\t" + "ANIMAL QUANTITY:");

            Iterator<Integer> it = setIds.iterator();
            while (it.hasNext()) {
                Integer id = it.next();
                int capacity = uf.count(id);
                if (capacity >= animalCount.get(regionNum) && animalMap.get(id) == null) {       // Only display regions where the capacity can hold the number of(from animal id) and where there is no number
                    System.out.print("#" + id + "\t\t\t\t" + capacity + "\t\t\t\t");
                    if (animalMap.get(id) == null) {
                        System.out.print("-" + "\t\t\t\t");
                    } else {
                        System.out.print(animalMap.get(id) + "\t\t\t\t");
                    }
                    if (animalCount.get(id) == null) {
                        System.out.println("-");
                    } else {
                        System.out.println(animalCount.get(id));
                    }
                }
            }
            regionId = scar.nextInt();

            while (!(setIds.contains(regionId) && number <= regioncap.get(regionId) && animalMap.get(regionId) == null)){       // we loop until an appropriate region is entered
                System.out.println("WRONG REGION ENTERED,PLEASE ENTER ONE OF THE FOLLOWING IDs");
                System.out.println("Choose a REGION ID#: ");
                System.out.println("REGION #: \t" + " CAPACITY: \t" + " ANIMAL:\t" + "ANIMAL QUANTITY:");
                Iterator<Integer> it2 = setIds.iterator();
                while (it2.hasNext()) {
                    Integer id = it2.next();
                    int capacity = uf.count(id);
                    if (capacity >= animalCount.get(regionNum) && animalMap.get(id) == null) {
                        System.out.print("#" + id + "\t\t\t\t" + capacity + "\t\t\t\t");
                        if (animalMap.get(id) == null) {
                            System.out.print("-" + "\t\t\t\t");
                        } else {
                            System.out.print(animalMap.get(id) + "\t\t\t\t");
                        }
                        if (animalCount.get(id) == null) {
                            System.out.println("-");
                        } else {
                            System.out.println(animalCount.get(id));
                        }
                    }
                }


                regionId = scar.nextInt();
            }

            if (setIds.contains(regionId) && number <= regioncap.get(regionId) && animalMap.get(regionId) == null) {
                for (Object location : uf.get(regionId)) {        //getting ENTIRE SET OF the specified region ID
                    currentSet.add((String) location);
                }

                Collections.shuffle(currentSet);


                //given an id from the user i can know what animal is supposed to be stored there
                String valueToBeRemoved = animalMap.get(regionNum).substring(0, 3).toUpperCase(); //given an animal i can  get the initials from that animal

                //given some initials i can iterate over the map that holds the cells and the initials and itirate through it as many times as those initials are present
                Iterator<Map.Entry<String, String>>
                        iterator = cellid.entrySet().iterator();

                // Iterate over the Map
                while (iterator.hasNext()) {              //erease every entry of the cell id map for that current animal (INITIAL)
                    // Get the entry at this iteration
                    Map.Entry<String, String> entry = iterator.next();
                    // Check if this value matches those initials
                    if (valueToBeRemoved.equals(entry.getValue())) {
                        // Remove this entry from map
                        iterator.remove();
                    }
                }

                String previousName = animalMap.get(regionNum); //from this region
                animalMap.remove(regionNum);
                animalMap.put(regionId, previousName); // to this region

                int previouscapicity = animalCount.get(regionNum);
                animalCount.remove(regionNum);
                animalCount.put(regionId, previouscapicity);

                currentAnimals.remove(regionNum);//remove EXISTING region id from the current animal set since it is no longer there (active)
                currentAnimals.add(regionId); // add new region id since we are moving animal to this new region

//                int previousRegionCapacity = regioncap.get(regionId);
//                regioncap.remove(regionNum);             // removing previous capacity from the from id
//                regioncap.put(regionId,previousRegionCapacity); // add the previous capacity to new

                for (int i = 0; i < number; i++) {// putting the set coordinates into an arraylist as many as the user inputed
                    String location = currentSet.remove(0);                                                  // remember to clear arraylist once used
                    cellid.put(location, initials);
                }

            }
            currentSet.clear();              // clear currentSet in order to take a fresh new batch of animals // no need to
            System.out.println(animalCount.get(regionId)+" "+animalMap.get(regionId)+"(s)"+"were MOVED from region#"+regionNum+" to region # "+regionId);
        }
    }


    public static void shuffleLocations() {  //works now
        cellid.clear();

        for (Integer ids : setIds) {// for every regionID
            setLocations.put(ids, null);     // put in THAT particual ID into a "global set" with no value mapped to it
            ArrayList<String> list = new ArrayList<>();  // create a new list each time
            for (Object location : uf.get(ids)) {        //getting ENTIRE SET OF the specified region ID
                list.add((String) location);            // add each individual cell location to the list
            }
            Collections.shuffle(list);             // we shuffle the list to provide new available (if possible) shuffles each time the method is called
            setLocations.put(ids, list);          // set that regionID to that list
        }
                    cellid.clear();           // note: might erase later

//        for (Integer ids : currentAnimals) {    // for each existing animal in the set AND
//            for (int i = 0; i < animalCount.get(ids); i++) {// for each animal in that SET
//                Collections.shuffle(setLocations.get(ids));      //shuffle their location one more time
//                String location = setLocations.get(ids).remove(0);  // removing first element of the SHUFFLED List              BEFORE.get(ids).remove(i);
//                int row = Integer.parseInt(location.substring(0, 1));           // this MIGHT HAVE BEEN THE PROBLEM WHERE TAKING SPECIFIC SUBSTRING (only 1 digit)
//                int col = Integer.parseInt(location.substring(location.length() - 1));    //WAS JUST TAKING A DIGIT
//                String currentinitials = animalMap.get(ids).substring(0, 3).toUpperCase();
//                cellid.put(row + "," + col, currentinitials); // cellid theoretically gets new cells cells each time    (for each active cell id with an animal)
//            }
//
//        }


        for (Integer id:currentAnimals) {     // for each id in this set currentAnimals(set that has the id of the location of all the animals that exist)
            for(int i = 0; i<animalCount.get(id);i++){  // for each animal found in that region we provide a
                Collections.shuffle(setLocations.get(id)); // random location (row+","+col) from the list of the region/set CAPACITY
                String location = setLocations.get(id).remove(0);
                String ini  = animalMap.get(id).substring(0, 3).toUpperCase();
                cellid.put(location,ini);
            }

        }


    }
}