import java.util.Scanner;
import java.io.*;
import java.io.IOException;
public class SRTFtest {

public static int n;
public static int proc[][];
static Scanner input = new Scanner(System.in);

public static void menu()throws IOException {
    System.out.println("Choose one of the following:");
    System.out.println("1. Enter process information.");
    System.out.println("2. Show detailed information about each process and different scheduling criteria.");
    System.out.println("3. Exit the program.");

    System.out.print("-> ");
    int choice = input.nextInt();

    switch (choice) {
      case 1:
        enterProcessInformation();
        break;
      case 2:
        showDetailedInformation();
        break;
        case 3: System.exit(0); 
        break;
      default: System.out.println("you didn't enter a valid value");
        
    }
  }

public static void enterProcessInformation() {
    
      System.out.println("Please enter the number of Processes: ");
       n = input.nextInt();
       proc = new int[n + 1][6];//proc AT [][0] Burst Time [][1] - WT[][2] - TR[][3] - ST[][4] -F[5]

       for(int i = 1; i <= n; i++){
      System.out.println("Please enter the Arrival Time for Process " + i + ": ");
      proc[i][0] = input.nextInt();
      System.out.println("Please enter the Burst Time for Process " + i + ": ");
      proc[i][1] = input.nextInt();
      proc[i][4] = -1;
      proc[i][5] = -1;
     }
       System.out.println();
       }
 public static void showDetailedInformation()throws IOException{ 
 int[] burst = new int[n+1];
 //copy burst time into new array
    for (int i=0; i<burst.length; i++){
    burst[i]=proc[i][1];
    }
    
         File file = new File("Report.txt");
         FileWriter fr = new FileWriter(file, true);
         BufferedWriter br = new BufferedWriter(fr);

       //Calculation of Total Time and Initialization of Time Chart array
     int total_time = 0;
     for(int i = 1; i <= n; i++){
      total_time += proc[i][1];
      
     }

     int time_chart[] = new int[total_time];
     
     for(int i = 0; i < total_time; i++) {
      //Selection of shortest process which has arrived
      int sel_proc = 0;
      int min = 99999;
      for(int j = 1; j <= n; j++){
       if(proc[j][0] <= i){//Condition to check if Process has arrived
        if(proc[j][1] < min && proc[j][1] != 0){
         min = proc[j][1];
         sel_proc = j;
        }
       }
      }
      
      //Assign selected process to current time in the Chart
      time_chart[i] = sel_proc;
    
      if( proc[sel_proc][4] == -1 ){//hasn;t been assigned a response time
      setResponseTime( sel_proc , i );
      } 

      
      //Decrement Remaining Time of selected process by 1 since it has been assigned the CPU for 1 unit of time
      proc[sel_proc][1]--; //decremint from burst time
      
      //assign finish time
      if(proc[sel_proc][1]==0){ //case burst time equals zero -> finished executing
      int fTime = i+1;
      proc[sel_proc][5] = fTime;
      }
      
      //WT and TT Calculation
      for(int j = 1; j <= n; j++){
       if(proc[j][0] <= i){
        if(proc[j][1] != 0){
         proc[j][3]++;//If process has arrived and it has not already completed execution its TT is incremented by 1
            if(j != sel_proc)//If the process has not been currently assigned the CPU and has arrived its WT is incremented by 1
             proc[j][2]++;
        }
        else if(j == sel_proc)//This is a special case in which the process has been assigned CPU and has completed its execution
         proc[j][3]++;
       }
      }
      
      //Printing the Time Chart
      if(i != 0)
      {
       if(sel_proc != time_chart[i - 1])
        //If the CPU has been assigned to a different Process we need to print the current value of time and the name of 
        //the new Process
       {
        br.write("--" + i + "--P" + sel_proc);
       }
      }
      else//If the current time is 0 i.e the printing has just started we need to print the name of the First selected Process
       br.write(i + "--P" + sel_proc);
      if(i == total_time - 1)//All the process names have been printed now we have to print the time at which execution ends
       br.write("--" + (i + 1));
      
     }
     br.write("\n");
     br.write("\n");
     
     //Printing the WT and TT for each Process
     br.write("PID\t "+"BT\t "+"AT\t "+"TT\t "+"WT\t "+"ST\t"+"RT\t   "+"FT\t" );
     br.write("\n");
     for(int i = 1; i <= n; i++)
     {
    int  pRT = ( proc[i][4] - proc[i][0] ); 
      br.write("P"+i+"\t    "+burst[i]+"\t    "+proc[i][0]+"\t   "+proc[i][3]+"\t    "+proc[i][2]+"\t    "+proc[i][4]+"\t    "+pRT+"\t    "+proc[i][5]+"\t    " );
      br.write("\n");
     }
     
     br.write("\n");
     
     //Printing the average WT & TT
     float WT = 0,TT = 0, RT = 0;
     for(int i = 1; i <= n; i++)
     {
      WT += proc[i][2];
      TT += proc[i][3];
      //RT = ST - AT
      RT += (proc[i][4] - proc[i][0]);
     }
     WT /= n;
     TT /= n;
     RT /= n;
     br.write("The Average WT is: " + WT + "ms");
     br.write("\n");
     br.write("The Average TT is: " + TT + "ms");
     br.write("\n");
     br.write("The Average RT is: " + RT + "ms");
     br.write("\n");
       br.close();
       fr.close();
 }
 
 //method to caluclate reponse time
 public static void setResponseTime( int process , int arrTime  ){
 proc[process][4] = arrTime;
 }//end of setResponseTime
 
 
 public static void main(String[] args)throws IOException{
    while(true) {
      menu();
    }
  }
}

    
