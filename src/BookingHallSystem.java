import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookingHallSystem {
    static void showMenu(){
        System.out.println("# APPLICATION MENU #");
        System.out.println("<A>. BOOKING");
        System.out.println("<B>. HALL");
        System.out.println("<C>. SHOWTIME");
        System.out.println("<D>. REBOOT SYSTEM");
        System.out.println("<E>. HISTORY");
        System.out.println("<F>. EXIT");
        System.out.print("CHOOSE THE OPTION : ");
    }
    static void historyInfo(String[][] history,int countRow){
        if(countRow==-1){
            System.out.println("DON'T HISTORY");
            return;
        }
        String formattedText = """
                               # NO: %s
                               # SEATS: [ %s ]
                               # HALL\t\t\t#STU.ID\t\t#CREATED_DATE
                               %s\t\t%s\t\t%s
                               """;
        for (int row = 0; row < history.length; row++) {
            if (history[row][0] != null && !history[row][0].isEmpty()) {
                System.out.printf(formattedText, history[row][0], history[row][1].toUpperCase(), history[row][2], history[row][3], history[row][4]);
                System.out.println();
            }
        }

    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        // this scanner I want to do press enter to continue
        Scanner press = new Scanner(System.in);
        String reGex = "\\D";
        int rows = 0, seats = 0;
        System.out.println("[=====================================]");
        System.out.println("# WELCOME TO SEU CINEMA #");
        System.out.println("[=====================================]");
        Pattern pattern1 = Pattern.compile(reGex);
        do {
            System.out.print("--> CONFIG TOTAL ROWS IN THE HALL : ");
            String inputRows = in.next();
            /* Check the user input number or letter if the user input letter is match so the loop will
            the loop again user will input until the user input the right*/
            Matcher matcherRows = pattern1.matcher(inputRows);

            if (matcherRows.matches()) {
                System.out.println("INVALID INPUT TRY AGAIN!!! ");
                continue;
            } else {
                rows = Integer.parseInt(inputRows);
            }
            if(rows<1&&rows<9){
                System.out.println("YOU CAN ONLY INPUT [1-9]");
                continue;
            }
            System.out.print("--> CONFIG TOTAL SEATS PER ROW IN THE HALL : ");
            String inputSeat = in.next();
            //Validation the seats if the user input string try again until input number
            Matcher matcherSeat = pattern1.matcher(inputSeat);
            if (matcherSeat.matches()) {
                System.out.println("INVALID INPUT TRY AGAIN!!!");
            } else {
                seats = Integer.parseInt(inputSeat);
                if(seats<1&&seats<9){
                    System.out.println("YOU CAN ONLY INPUT [1-9]");
                    continue;
                }
                break;
            }

        } while (true);
        String[][] hallMorning = new String[rows][seats];
        String[][] hallAfterNoon = new String[rows][seats];
        String[][] hallEvening = new String[rows][seats];
        String student;
        String[][] history = new String[100][5];
        int countRow=-1,countNo=0;
        initializeHalls(hallMorning, hallAfterNoon, hallEvening);
        // if notExit = true the loop or the program will continue until you choose the option number 6
        boolean notExit = true;
        String ask;
        int found=0;
        do {
            showMenu();
            String option = in.next();
            Pattern pattern = Pattern.compile(reGex);
            Matcher matcher = pattern.matcher(option);
            if (matcher.matches()) {
                switch (option) {
                    case "A", "a":
                        showTime();
                        String chooseSeat;
                        String[] chooseSeats;
                        String update;
                        Scanner hallInput = new Scanner(System.in);
                        System.out.print("-> INPUT THE SHOWTIME ( A | B | C ) : ");
                        String inputShowTime=in.next();
                        instruction();
                        switch (inputShowTime) {
                            case "a","A" :
                                found=0;
                                String ch11="null";
                                hallMorningMethod(hallMorning);
                                System.out.print("-> PLEASE CHOOSE THE SEAT THAT AVAILABLE (AV) : ");
                                chooseSeat=hallInput.nextLine();
                                chooseSeats=chooseSeat.split(",");
                                for(int i=0;i<hallMorning.length;i++) {
                                    for (int j = 0; j < hallMorning[i].length; j++) {
                                        if (hallMorning[i][j].substring(0, 3).equals(chooseSeats[0].toUpperCase()) && hallMorning[i][j].contains("AV")) {
                                            ch11="Available";
                                        }
                                        else if( hallMorning[i][j].contains("BO") && hallMorning[i][j].substring(0,3).equals(chooseSeats[0].toUpperCase())){
                                            ch11="Book";
                                        }
                                    }
                                }
                                if(ch11.equals("Book")){
                                    System.out.println(chooseSeats[0]+" ALREADY BOOKED BY SOMEONE");
                                    break;
                                } else if (ch11.equals("null")) {
                                    System.out.println("DON'T FOUND OR INVALID INPUT");
                                    break;
                                }
                                if (chooseSeats.length==1){
                                    System.out.print("# ARE YOU SURE YOU WANT SURE YOU WANT TO BOOKING? [Y|YES / N|NO] : ");
                                    ask=in.next();
                                    if(ask.toLowerCase().equals("yes")||ask.toLowerCase().equals("y")){
                                        System.out.print("-> PLEASE ENTER STUDENT ID : ");
                                        student=in.next();
                                        for(int i=0;i<hallMorning.length;i++){
                                            for(int j =0;j<hallMorning[i].length;j++){
                                                if(hallMorning[i][j].substring(0,3).equals(chooseSeats[0].toUpperCase()) && hallMorning[i][j].contains("AV")) {
                                                    update = hallMorning[i][j].substring(0, 3) + "::BO";
                                                    hallMorning[i][j] = update;
                                                    found=1;
                                                    System.out.println("# BOOKING SUCCESSFULLY #");
                                                    history[++countRow][0]=String.valueOf(++countNo);
                                                    history[countRow][1]=chooseSeats[0];
                                                    history[countRow][2]="HALL MORNING";
                                                    history[countRow][3]=student;
                                                    history[countRow][4]=now.format(formatter);
                                                }
                                            }
                                        }
                                    }else if(ask.toLowerCase().equals("no")||ask.toLowerCase().equals("n")){
                                        break;
                                    }
                                    if (found==0){
                                        System.out.println("!! DON'T FOUND OR INVALID INPUT");

                                    }

                                }else if (chooseSeats.length>3){
                                    System.out.println("YOU CAN BOOK ONLY 3 SEAT IN TIME");
                                }
                                else if (chooseSeats.length>1){
                                    int foundOne=0,foundTwo=0,foundThree=0;
                                    String ch1="",ch2="",ch3="";
                                    outer:
                                    for(int i=0;i<hallMorning.length;i++) {
                                        for (int j = 0; j < hallMorning[i].length; j++) {
                                            if (hallMorning[i][j].substring(0, 3).equals(chooseSeats[0].toUpperCase()) && hallMorning[i][j].contains("AV")) {
                                                ch1="Available";
                                            }
                                            else if( hallMorning[i][j].contains("BO") && hallMorning[i][j].substring(0,3).equals(chooseSeats[0].toUpperCase())){
                                                ch1="Book";
                                            }
                                        }
                                    }
                                    if(ch1.equals("Book")){
                                        System.out.println(chooseSeats[0].toUpperCase()+" IS NOT AVAILABLE PLEASE TRY AGAIN ");
                                        break;
                                    }else if (ch1.equals("Available")){
                                        System.out.println(chooseSeats[0].toUpperCase()+" IS AVAILABLE ");
                                    }else {
                                        System.out.println(chooseSeats[0].toUpperCase()+" DON'T HAVE IN OUR HALL");
                                        break;
                                    }

                                    for(int i=0;i<hallMorning.length;i++) {
                                        for (int j = 0; j < hallMorning[i].length; j++) {
                                            if (hallMorning[i][j].substring(0, 3).equals(chooseSeats[1].toUpperCase()) && hallMorning[i][j].contains("AV")) {
                                                ch2="Available";
                                            }
                                            else if( hallMorning[i][j].contains("BO") && hallMorning[i][j].substring(0,3).equals(chooseSeats[1].toUpperCase())){
                                                ch2="BO";
                                            }
                                        }
                                    }
                                    if(ch2.equals("BO")){
                                        System.out.println(chooseSeats[1].toUpperCase()+" IS NOT AVAILABLE PLEASE TRY AGAIN ");
                                        break;
                                    }else if (ch2.equals("Available")){
                                        System.out.println(chooseSeats[1].toUpperCase()+" IS AVAILABLE ");
                                    }else {
                                        System.out.println(chooseSeats[1].toUpperCase()+" DON'T HAVE IN OUR HALL");
                                        break;
                                    }
                                    if (chooseSeats.length==3){
                                        for(int i=0;i<hallMorning.length;i++) {
                                            for (int j = 0; j < hallMorning[i].length; j++) {
                                                if (hallMorning[i][j].substring(0, 3).equals(chooseSeats[2].toUpperCase()) && hallMorning[i][j].contains("AV")) {
                                                    ch3="Available";
                                                }
                                                else if( hallMorning[i][j].contains("BO") && hallMorning[i][j].substring(0,3).equals(chooseSeats[0].toUpperCase())){
                                                    ch3="Book";
                                                }
                                            }
                                        }
                                        if(ch3.equals("Book")){
                                            System.out.println(chooseSeats[2].toUpperCase()+" IS NOT AVAILABLE PLEASE TRY AGAIN");
                                            break;
                                        }else if (ch3.equals("Available")){
                                            System.out.println(chooseSeats[2].toUpperCase()+" IS AVAILABLE ");
                                        }else {
                                            System.out.println(chooseSeats[2].toUpperCase()+" DON'T HAVE IN OUR HALL");
                                            break;
                                        }
                                    }
                                    System.out.print("# ARE YOU SURE YOU WANT SURE YOU WANT TO BOOKING? [Y|YES / N|NO] : ");
                                    ask=in.next();
                                    if(ask.toLowerCase().equals("yes")||ask.toLowerCase().equals("y")) {
                                        System.out.print("-> PLEASE ENTER STUDENT ID : ");
                                        student= in.next();
                                        int check = 0, foundCheck = 0;
                                        for (int i = 0; i < hallMorning.length; i++) {
                                            for (int j = 0; j < hallMorning[i].length; j++) {
                                                if (hallMorning[i][j].substring(0, 3).equals(chooseSeats[check].toUpperCase()) && hallMorning[i][j].contains("AV")) {
                                                    update = hallMorning[i][j].substring(0, 3) + "::BO";
                                                    hallMorning[i][j] = update;
                                                    foundOne = 1;
                                                    foundCheck = 1;
                                                }
                                            }
                                        }
                                        check++;
                                        if (foundCheck == 1) {
                                            for (int i = 0; i < hallMorning.length; i++) {
                                                for (int j = 0; j < hallMorning[i].length; j++) {
                                                    if (hallMorning[i][j].substring(0, 3).equals(chooseSeats[check].toUpperCase()) && hallMorning[i][j].contains("AV")) {
                                                        update = hallMorning[i][j].substring(0, 3) + "::BO";
                                                        hallMorning[i][j] = update;
                                                        foundTwo = 1;
                                                        foundCheck = 2;
                                                    }
                                                }
                                            }
                                        }
                                        check++;
                                        if (chooseSeats.length == 3) {
                                            if (foundCheck == 2) {
                                                for (int i = 0; i < hallMorning.length; i++) {
                                                    for (int j = 0; j < hallMorning[i].length; j++) {
                                                        if (hallMorning[i][j].substring(0, 3).equals(chooseSeats[check].toUpperCase()) && hallMorning[i][j].contains("AV")) {
                                                            update = hallMorning[i][j].substring(0, 3) + "::BO";
                                                            hallMorning[i][j] = update;
                                                            foundThree = 1;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (chooseSeats.length == 2) {
                                            if (foundOne == 1 && foundTwo == 1) {
                                                System.out.println("# BOOKING SUCCESSFULLY #");
                                                history[++countRow][0]=String.valueOf(++countNo);
                                                history[countRow][1]=chooseSeats[0]+" "+chooseSeats[1];
                                                history[countRow][2]="HALL MORNING";
                                                history[countRow][3]=student;
                                                history[countRow][4]=now.format(formatter);
                                            }
                                        }
                                        if (chooseSeats.length == 3) {
                                            if (foundOne == 1 && foundTwo == 1 && foundThree == 1) {
                                                System.out.println("# BOOKING SUCCESSFULLY #");
                                                history[++countRow][0]=String.valueOf(++countNo);
                                                history[countRow][1]=chooseSeats[0]+" "+chooseSeats[1]+" "+chooseSeats[2];
                                                history[countRow][2]="HALL MORNING";
                                                history[countRow][3]=student;
                                                history[countRow][4]=now.format(formatter);
                                            }
                                        }

                                    }

                                }
                                break;
                                // This IS B Section

                            case "b","B" :
                                found=0;
                                String ch111="null";
                                hallAfternoonMethod(hallAfterNoon);
                                System.out.print("-> PLEASE CHOOSE THE SEAT THAT AVAILABLE (AV) : ");
                                chooseSeat=hallInput.nextLine();
                                chooseSeats=chooseSeat.split(",");
                                for(int i=0;i<hallAfterNoon.length;i++) {
                                    for (int j = 0; j < hallAfterNoon[i].length; j++) {
                                        if (hallAfterNoon[i][j].substring(0, 3).equals(chooseSeats[0].toUpperCase()) && hallAfterNoon[i][j].contains("AV")) {
                                            ch111="Available";
                                        }
                                        else if( hallAfterNoon[i][j].contains("BO") && hallAfterNoon[i][j].substring(0,3).equals(chooseSeats[0].toUpperCase())){
                                            ch111="Book";
                                        }
                                    }
                                }
                                if(ch111.equals("Book")){
                                    System.out.println(chooseSeats[0]+" ALREADY BOOKED BY SOMEONE");
                                    break;
                                } else if (ch111.equals("null")) {
                                    System.out.println("DON'T FOUND OR INVALID INPUT");
                                    break;
                                }
                                if (chooseSeats.length==1){
                                    System.out.print("# ARE YOU SURE YOU WANT SURE YOU WANT TO BOOKING? [Y|YES / N|NO] : ");
                                    ask=in.next();
                                    if(ask.toLowerCase().equals("yes")||ask.toLowerCase().equals("y")){
                                        System.out.print("-> PLEASE ENTER STUDENT ID : ");
                                        student=in.next();
                                        for(int i=0;i<hallAfterNoon.length;i++){
                                            for(int j =0;j<hallAfterNoon[i].length;j++){
                                                if(hallAfterNoon[i][j].substring(0,3).equals(chooseSeats[0].toUpperCase()) && hallAfterNoon[i][j].contains("AV")) {
                                                    update = hallAfterNoon[i][j].substring(0, 3) + "::BO";
                                                    hallAfterNoon[i][j] = update;
                                                    found=1;
                                                    System.out.println("# BOOKING SUCCESSFULLY #");
                                                    history[++countRow][0]=String.valueOf(++countNo);
                                                    history[countRow][1]=chooseSeats[0];
                                                    history[countRow][2]="HALL AFTERNOON";
                                                    history[countRow][3]=student;
                                                    history[countRow][4]=now.format(formatter);

                                                }
                                            }
                                        }
                                    }else if(ask.toLowerCase().equals("no")||ask.toLowerCase().equals("n")){
                                        break;
                                    }
                                    if (found==0){
                                        System.out.println("!! DON'T FOUND OR INVALID INPUT");

                                    }

                                }else if (chooseSeats.length>3){
                                    System.out.println("YOU CAN BOOK ONLY 3 SEAT IN TIME");
                                }
                                else if (chooseSeats.length>1){
                                    int foundOne=0,foundTwo=0,foundThree=0;
                                    String ch1="",ch2="",ch3="";
                                    for(int i=0;i<hallAfterNoon.length;i++) {
                                        for (int j = 0; j < hallAfterNoon[i].length; j++) {
                                            if (hallAfterNoon[i][j].substring(0, 3).equals(chooseSeats[0].toUpperCase()) && hallAfterNoon[i][j].contains("AV")) {
                                                ch1="Available";
                                            }
                                            else if( hallAfterNoon[i][j].contains("BO") && hallAfterNoon[i][j].substring(0,3).equals(chooseSeats[0].toUpperCase())){
                                                ch1="Book";
                                            }
                                        }
                                    }
                                    if(ch1.equals("Book")){
                                        System.out.println(chooseSeats[0].toUpperCase()+" IS NOT AVAILABLE PLEASE TRY AGAIN ");
                                        break;
                                    }else if (ch1.equals("Available")){
                                        System.out.println(chooseSeats[0].toUpperCase()+" IS AVAILABLE ");
                                    }else {
                                        System.out.println(chooseSeats[0].toUpperCase()+" DON'T HAVE IN OUR HALL");
                                        break;
                                    }

                                    for(int i=0;i<hallAfterNoon.length;i++) {
                                        for (int j = 0; j < hallAfterNoon[i].length; j++) {
                                            if (hallAfterNoon[i][j].substring(0, 3).equals(chooseSeats[1].toUpperCase()) && hallAfterNoon[i][j].contains("AV")) {
                                                ch2="Available";
                                            }
                                            else if( hallAfterNoon[i][j].contains("BO") && hallAfterNoon[i][j].substring(0,3).equals(chooseSeats[1].toUpperCase())){
                                                ch2="BO";
                                            }
                                        }
                                    }
                                    if(ch2.equals("BO")){
                                        System.out.println(chooseSeats[1].toUpperCase()+" IS NOT AVAILABLE PLEASE TRY AGAIN ");
                                        break;
                                    }else if (ch2.equals("Available")){
                                        System.out.println(chooseSeats[1].toUpperCase()+" IS AVAILABLE ");
                                    }else {
                                        System.out.println(chooseSeats[1].toUpperCase()+" DON'T HAVE IN OUR HALL");
                                        break;
                                    }
                                    if (chooseSeats.length==3){
                                        for(int i=0;i<hallAfterNoon.length;i++) {
                                            for (int j = 0; j < hallAfterNoon[i].length; j++) {
                                                if (hallAfterNoon[i][j].substring(0, 3).equals(chooseSeats[2].toUpperCase()) && hallAfterNoon[i][j].contains("AV")) {
                                                    ch3="Available";
                                                }
                                                else if( hallAfterNoon[i][j].contains("BO") && hallAfterNoon[i][j].substring(0,3).equals(chooseSeats[0].toUpperCase())){
                                                    ch3="Book";
                                                }
                                            }
                                        }
                                        if(ch3.equals("Book")){
                                            System.out.println(chooseSeats[2].toUpperCase()+" IS NOT AVAILABLE PLEASE TRY AGAIN");
                                            break;
                                        }else if (ch3.equals("Available")){
                                            System.out.println(chooseSeats[2].toUpperCase()+" IS AVAILABLE ");
                                        }else {
                                            System.out.println(chooseSeats[2].toUpperCase()+" DON'T HAVE IN OUR HALL");
                                            break;
                                        }
                                    }
                                    System.out.print("# ARE YOU SURE YOU WANT SURE YOU WANT TO BOOKING? [Y|YES / N|NO] : ");
                                    ask=in.next();
                                    if(ask.toLowerCase().equals("yes")||ask.toLowerCase().equals("y")) {
                                        System.out.print("-> PLEASE ENTER STUDENT ID : ");
                                        student= in.next();
                                        int check = 0, foundCheck = 0;
                                        for (int i = 0; i < hallAfterNoon.length; i++) {
                                            for (int j = 0; j < hallAfterNoon[i].length; j++) {
                                                if (hallAfterNoon[i][j].substring(0, 3).equals(chooseSeats[check].toUpperCase()) && hallAfterNoon[i][j].contains("AV")) {
                                                    update = hallAfterNoon[i][j].substring(0, 3) + "::BO";
                                                    hallAfterNoon[i][j] = update;
                                                    foundOne = 1;
                                                    foundCheck = 1;
                                                }
                                            }
                                        }
                                        check++;
                                        if (foundCheck == 1) {
                                            for (int i = 0; i < hallAfterNoon.length; i++) {
                                                for (int j = 0; j < hallAfterNoon[i].length; j++) {
                                                    if (hallAfterNoon[i][j].substring(0, 3).equals(chooseSeats[check].toUpperCase()) && hallAfterNoon[i][j].contains("AV")) {
                                                        update = hallAfterNoon[i][j].substring(0, 3) + "::BO";
                                                        hallAfterNoon[i][j] = update;
                                                        foundTwo = 1;
                                                        foundCheck = 2;
                                                    }
                                                }
                                            }
                                        }
                                        check++;
                                        if (chooseSeats.length == 3) {
                                            if (foundCheck == 2) {
                                                for (int i = 0; i < hallAfterNoon.length; i++) {
                                                    for (int j = 0; j < hallAfterNoon[i].length; j++) {
                                                        if (hallAfterNoon[i][j].substring(0, 3).equals(chooseSeats[check].toUpperCase()) && hallAfterNoon[i][j].contains("AV")) {
                                                            update = hallAfterNoon[i][j].substring(0, 3) + "::BO";
                                                            hallAfterNoon[i][j] = update;
                                                            foundThree = 1;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (chooseSeats.length == 2) {
                                            if (foundOne == 1 && foundTwo == 1) {
                                                System.out.println("# BOOKING SUCCESSFULLY #");
                                                history[++countRow][0]=String.valueOf(++countNo);
                                                history[countRow][1]=chooseSeats[0]+" "+chooseSeats[1];
                                                history[countRow][2]="HALL AFTERNOON";
                                                history[countRow][3]=student;
                                                history[countRow][4]=now.format(formatter);
                                            }
                                        }
                                        if (chooseSeats.length == 3) {
                                            if (foundOne == 1 && foundTwo == 1 && foundThree == 1) {
                                                System.out.println("# BOOKING SUCCESSFULLY #");
                                                history[++countRow][0]=String.valueOf(++countNo);
                                                history[countRow][1]=chooseSeats[0]+" "+chooseSeats[1]+" "+chooseSeats[2];
                                                history[countRow][2]="HALL AFTERNOON";
                                                history[countRow][3]=student;
                                                history[countRow][4]=now.format(formatter);
                                            }
                                        }

                                    }

                                }
                                break;




                                // this is C


                            case "c","C" :
                                found=0;
                                String ch121="null";
                                hallEveningMethod(hallEvening);
                                System.out.print("-> PLEASE CHOOSE THE SEAT THAT AVAILABLE (AV) : ");
                                chooseSeat=hallInput.nextLine();
                                chooseSeats=chooseSeat.split(",");
                                for(int i=0;i<hallEvening.length;i++) {
                                    for (int j = 0; j < hallEvening[i].length; j++) {
                                        if (hallEvening[i][j].substring(0, 3).equals(chooseSeats[0].toUpperCase()) && hallEvening[i][j].contains("AV")) {
                                            ch121="Available";
                                        }
                                        else if( hallEvening[i][j].contains("BO") && hallEvening[i][j].substring(0,3).equals(chooseSeats[0].toUpperCase())){
                                            ch121="Book";
                                        }
                                    }
                                }
                                if(ch121.equals("Book")){
                                    System.out.println(chooseSeats[0]+" ALREADY BOOKED BY SOMEONE");
                                    break;
                                } else if (ch121.equals("null")) {
                                    System.out.println("DON'T FOUND OR INVALID INPUT");
                                    break;
                                }
                                if (chooseSeats.length==1){
                                    System.out.print("# ARE YOU SURE YOU WANT SURE YOU WANT TO BOOKING? [Y|YES / N|NO] : ");
                                    ask=in.next();


                                    if(ask.toLowerCase().equals("yes")||ask.toLowerCase().equals("y")){
                                        System.out.print("-> PLEASE ENTER STUDENT ID : ");
                                        student=in.next();
                                        for(int i=0;i<hallEvening.length;i++){
                                            for(int j =0;j<hallEvening[i].length;j++){
                                                if(hallEvening[i][j].substring(0,3).equals(chooseSeats[0].toUpperCase()) && hallEvening[i][j].contains("AV")) {
                                                    update = hallEvening[i][j].substring(0, 3) + "::BO";
                                                    hallEvening[i][j] = update;
                                                    found=1;
                                                    System.out.println("# BOOKING SUCCESSFULLY #");
                                                    history[++countRow][0]=String.valueOf(++countNo);
                                                    history[countRow][1]=chooseSeats[0];
                                                    history[countRow][2]="HALL EVENING";
                                                    history[countRow][3]=student;
                                                    history[countRow][4]=now.format(formatter);
                                                }
                                            }
                                        }
                                    }else if(ask.toLowerCase().equals("no")||ask.toLowerCase().equals("n")){
                                        break;
                                    }
                                    if (found==0){
                                        System.out.println("!! DON'T FOUND OR INVALID INPUT");

                                    }

                                }else if (chooseSeats.length>3){
                                    System.out.println("YOU CAN BOOK ONLY 3 SEAT IN TIME");
                                }
                                else if (chooseSeats.length>1){
                                    int foundOne=0,foundTwo=0,foundThree=0;
                                    String ch1="",ch2="",ch3="";
                                    outer:
                                    for(int i=0;i<hallEvening.length;i++) {
                                        for (int j = 0; j < hallEvening[i].length; j++) {
                                            if (hallEvening[i][j].substring(0, 3).equals(chooseSeats[0].toUpperCase()) && hallEvening[i][j].contains("AV")) {
                                                ch1="Available";
                                            }
                                            else if( hallEvening[i][j].contains("BO") && hallEvening[i][j].substring(0,3).equals(chooseSeats[0].toUpperCase())){
                                                ch1="Book";
                                            }
                                        }
                                    }
                                    if(ch1.equals("Book")){
                                        System.out.println(chooseSeats[0].toUpperCase()+" IS NOT AVAILABLE PLEASE TRY AGAIN ");
                                        break;
                                    }else if (ch1.equals("Available")){
                                        System.out.println(chooseSeats[0].toUpperCase()+" IS AVAILABLE ");
                                    }else {
                                        System.out.println(chooseSeats[0].toUpperCase()+" DON'T HAVE IN OUR HALL");
                                        break;
                                    }

                                    for(int i=0;i<hallEvening.length;i++) {
                                        for (int j = 0; j < hallEvening[i].length; j++) {
                                            if (hallEvening[i][j].substring(0, 3).equals(chooseSeats[1].toUpperCase()) && hallEvening[i][j].contains("AV")) {
                                                ch2="Available";
                                            }
                                            else if( hallEvening[i][j].contains("BO") && hallEvening[i][j].substring(0,3).equals(chooseSeats[1].toUpperCase())){
                                                ch2="BO";
                                            }
                                        }
                                    }
                                    if(ch2.equals("BO")){
                                        System.out.println(chooseSeats[1].toUpperCase()+" IS NOT AVAILABLE PLEASE TRY AGAIN ");
                                        break;
                                    }else if (ch2.equals("Available")){
                                        System.out.println(chooseSeats[1].toUpperCase()+" IS AVAILABLE ");
                                    }else {
                                        System.out.println(chooseSeats[1].toUpperCase()+" DON'T HAVE IN OUR HALL");
                                        break;
                                    }
                                    if (chooseSeats.length==3){
                                        for(int i=0;i<hallEvening.length;i++) {
                                            for (int j = 0; j < hallEvening[i].length; j++) {
                                                if (hallEvening[i][j].substring(0, 3).equals(chooseSeats[2].toUpperCase()) && hallEvening[i][j].contains("AV")) {
                                                    ch3="Available";
                                                }
                                                else if( hallEvening[i][j].contains("BO") && hallEvening[i][j].substring(0,3).equals(chooseSeats[0].toUpperCase())){
                                                    ch3="Book";
                                                }
                                            }
                                        }
                                        if(ch3.equals("Book")){
                                            System.out.println(chooseSeats[2].toUpperCase()+" IS NOT AVAILABLE PLEASE TRY AGAIN");
                                            break;
                                        }else if (ch3.equals("Available")){
                                            System.out.println(chooseSeats[2].toUpperCase()+" IS AVAILABLE ");
                                        }else {
                                            System.out.println(chooseSeats[2].toUpperCase()+" DON'T HAVE IN OUR HALL");
                                            break;
                                        }
                                    }
                                    System.out.print("# ARE YOU SURE YOU WANT SURE YOU WANT TO BOOKING? [Y|YES / N|NO] : ");
                                    ask=in.next();
                                    if(ask.toLowerCase().equals("yes")||ask.toLowerCase().equals("y")) {
                                        System.out.print("-> PLEASE ENTER STUDENT ID : ");
                                        student= in.next();
                                        int check = 0, foundCheck = 0;
                                        for (int i = 0; i < hallEvening.length; i++) {
                                            for (int j = 0; j < hallEvening[i].length; j++) {
                                                if (hallEvening[i][j].substring(0, 3).equals(chooseSeats[check].toUpperCase()) && hallEvening[i][j].contains("AV")) {
                                                    update = hallEvening[i][j].substring(0, 3) + "::BO";
                                                    hallEvening[i][j] = update;
                                                    foundOne = 1;
                                                    foundCheck = 1;
                                                }
                                            }
                                        }
                                        check++;
                                        if (foundCheck == 1) {
                                            for (int i = 0; i < hallEvening.length; i++) {
                                                for (int j = 0; j < hallEvening[i].length; j++) {
                                                    if (hallEvening[i][j].substring(0, 3).equals(chooseSeats[check].toUpperCase()) && hallEvening[i][j].contains("AV")) {
                                                        update = hallEvening[i][j].substring(0, 3) + "::BO";
                                                        hallEvening[i][j] = update;
                                                        foundTwo = 1;
                                                        foundCheck = 2;
                                                    }
                                                }
                                            }
                                        }
                                        check++;
                                        if (chooseSeats.length == 3) {
                                            if (foundCheck == 2) {
                                                for (int i = 0; i < hallEvening.length; i++) {
                                                    for (int j = 0; j < hallEvening[i].length; j++) {
                                                        if (hallEvening[i][j].substring(0, 3).equals(chooseSeats[check].toUpperCase()) && hallEvening[i][j].contains("AV")) {
                                                            update = hallEvening[i][j].substring(0, 3) + "::BO";
                                                            hallEvening[i][j] = update;
                                                            foundThree = 1;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (chooseSeats.length == 2) {
                                            if (foundOne == 1 && foundTwo == 1) {
                                                System.out.println("# BOOKING SUCCESSFULLY #");
                                                history[++countRow][0]=String.valueOf(++countNo);
                                                history[countRow][1]=chooseSeats[0]+" "+chooseSeats[1];
                                                history[countRow][2]="HALL EVENING";
                                                history[countRow][3]=student;
                                                history[countRow][4]=now.format(formatter);
                                            }
                                        }
                                        if (chooseSeats.length == 3) {
                                            if (foundOne == 1 && foundTwo == 1 && foundThree == 1) {
                                                System.out.println("# BOOKING SUCCESSFULLY #");
                                                history[++countRow][0]=String.valueOf(++countNo);
                                                history[countRow][1]=chooseSeats[0]+" "+chooseSeats[1]+" "+chooseSeats[2];
                                                history[countRow][2]="HALL EVENING";
                                                history[countRow][3]=student;
                                                history[countRow][4]=now.format(formatter);
                                            }
                                        }

                                    }

                                }
                                break;
                            default:
                                System.out.println("!! INPUT INCORRECT !!");
                        }
                        System.out.print("Press enter key to continue...");
                        press.nextLine();
                        break;
                    case "B", "b":
                        System.out.println("=============================");
                        System.out.println("# HALL INFORMATION");
                        System.out.println("=============================");
                        hallMorningMethod(hallMorning);
                        hallAfternoonMethod(hallAfterNoon);
                        hallEveningMethod(hallEvening);
                        System.out.print("Press enter key to continue...");
                        press.nextLine();
                        break;
                    case "C", "c":
                        showTime();
                        System.out.print("Press enter key to continue...");
                        press.nextLine();
                        break;
                    case "D", "d":
                        System.out.println("ARE YOU SURE YOU WANT TO REBOOT??");
                        String askk=in.next();
                        switch (askk){
                            case "yes" ,"y","YES","Y": initializeHalls(hallMorning,hallAfterNoon,hallEvening); countRow=-1;
                                break;
                            case "n" ,"no","NO","N":
                                break;
                            default:
                                System.out.println("UNKNOWN INPUT");
                        }
                        System.out.print("Press enter key to continue...");
                        press.nextLine();
                        break;
                    case "E", "e": historyInfo(history,countRow);
                        System.out.print("Press enter key to continue...");
                        press.nextLine();
                        break;
                    case "F", "f":
                        notExit = false;
                        break;
                    default:
                        System.out.println("# UNKNOWN OPTION PLEASE CHOOSE AGAIN!!");
                        System.out.println("===================================");
                        System.out.print("Press enter key to continue...");
                        press.nextLine();
                }
            } else {
                System.out.println("===================================");
                System.out.println("ERROR 404 PLEASE ENTER ONLY NUMBER!!!!");
                System.out.println("===================================");
            }
        } while (notExit);
    }


    static void instruction(){
        System.out.println("# INSTRUCTION");
        System.out.println("# SINGLE : B-3");
        System.out.println("# MULTIPLE ( SEPARATE BY COMMA ) [ONLY 3 SEATS] : B-3,B-4");
    }

    static void hallMorningMethod(String[][] hallMorning){
        System.out.println("# HALL MORNING #");
        for (String[] arr:hallMorning){
            for(String arrInner:arr){
                System.out.print(arrInner+" | ");
            }
            System.out.println();
        }
        System.out.println("===============================");
    }
    static void hallAfternoonMethod(String[][] hallAfternoon){
        System.out.println("# HALL AFTERNOON #");
        for (String[] arr:hallAfternoon){
            for(String arrInner:arr){
                System.out.print(arrInner+" | ");
            }
            System.out.println();
        }
        System.out.println("===============================");
    }
    static void hallEveningMethod(String[][] hallEvening){
        System.out.println("# HALL EVENING #");
        for (String[] arr:hallEvening){
            for(String arrInner:arr){
                System.out.print(arrInner+" | ");
            }
            System.out.println();
        }
        System.out.println("===============================");
    }

    static void showTime () {
        System.out.println("============================");
        System.out.println("# SHOWTIME OF SEU HALL");
        System.out.println("============================");
        System.out.println("# MORNING ( 09:30 AM - 12:30 AM )");
        System.out.println("# AFTERNOON ( 2:00 PM - 5:30 PM )");
        System.out.println("# EVENING ( 07:30 PM - 9:30 PM )");
        System.out.println("============================");
    }
    static void initializeHalls(String[][] hallMorning,String[][] hallAfterNoon,String[][] hallEvening)
    {
        char[] letter = new char[26];
        int count=0;
        for (char ch='A'; ch<='Z';ch++) {
            letter[count++]=ch;
        }
        for(int i = 0; i<hallMorning.length;i++){
            for(int j=0;j<hallMorning[i].length;j++){
                hallMorning[i][j]=letter[i]+"-"+(j+1)+"::AV";
                hallAfterNoon[i][j]=letter[i]+"-"+(j+1)+"::AV";
                hallEvening[i][j]=letter[i]+"-"+(j+1)+"::AV";
            }
        }

    }


}
