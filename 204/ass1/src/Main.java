import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.io.* ;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Main {
    public static final int testCount = 10;
    public static List<Integer> mainArr = new ArrayList<>();
    public static int[] inputAxis = {512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 251281};
    public static void main(String args[]) throws IOException {
        
        Scanner sc = new Scanner(new File("src\\TrafficFlowDataset.csv"));
        sc.useDelimiter("\n");
        sc.next();
        while (sc.hasNext()) {
            mainArr.add(Integer.parseInt(sc.next().split(",")[7]));
        }
        sc.close();

        List<Integer> sorted = Arrays.asList(new Integer[mainArr.size()]);
        Collections.copy(sorted,mainArr);
        Collections.sort(sorted); // to keep order

        List<Integer> reverseSorted = Arrays.asList(new Integer[mainArr.size()]);
        Collections.copy(reverseSorted,sorted);
        Collections.sort(reverseSorted, Collections.reverseOrder()); // to keep order

        List<Integer>[] totalRandom = new ArrayList[4]; // Array of lists to hold every test cases for every algorithms
        List<Integer>[] totalSorted = new ArrayList[4];
        List<Integer>[] totalReversed = new ArrayList[4];

        for (int i = 0; i < 4; i++) { // initialize lists
            totalRandom[i] = new ArrayList<Integer>();
            totalSorted[i] = new ArrayList<Integer>();
            totalReversed[i] = new ArrayList<Integer>();
        }

      
        
        // Insertion sort 
        sortTester(mainArr,totalRandom[0],"Random",1,"Insertion");
        sortTester(sorted,totalSorted[0],"Sorted",1,"Insertion");
        sortTester(reverseSorted,totalReversed[0],"ReverseSorted",1,"Insertion");
        
        
        // merge Sort
        sortTester(mainArr,totalRandom[1],"Random",2,"Merge");
        sortTester(sorted,totalSorted[1],"Sorted",2,"Merge");
        sortTester(reverseSorted,totalReversed[1],"ReverseSorted",2,"Merge");
        
        
        // Pigeonhole sort
        sortTester(mainArr,totalRandom[2],"Random",3,"Pigeonhole");
        sortTester(sorted,totalSorted[2],"Sorted",3,"Pigeonhole");
        sortTester(reverseSorted,totalReversed[2],"ReverseSorted",3,"Pigeonhole");

        // Counting sort
        sortTester(mainArr,totalRandom[3],"Random",4,"Counting");
        sortTester(sorted,totalSorted[3],"Sorted",4,"Counting");
        sortTester(reverseSorted,totalReversed[3],"ReverseSorted",4,"Counting");
        
        // Create Tables and Charts 
        tableAndChart(totalRandom,"Random Data");
        tableAndChart(totalSorted,"Sorted Data");
        tableAndChart(totalReversed,"Reversely Sorted Data");
        
       
    }


    public static void sortTester(List<Integer> arr , List<Integer> total,String nameOfTest,int numberOfTest,String algorithm){
        for (Integer testInput : inputAxis) {
            int totalTime = 0;
            for (int i = 1; i <= testCount; i++) { //Insertion Sort
                
                List<Integer> subArr = arr.subList(0, testInput);
                List<Integer> copy = Arrays.asList(new Integer[subArr.size()]);
                Collections.copy(copy,subArr);
                int[] array = new int[copy.size()];
                for(int j = 0; j < copy.size(); j++) array[j] = copy.get(j);
                for (int a : array) {
                    
                }
                Instant start = Instant.now();
                AllSorts(array, numberOfTest); // choose sort algorithm
                Instant finish = Instant.now();
                long timeElapsed = Duration.between(start, finish).toMillis();
                totalTime+=timeElapsed;
            }
            System.out.println(String.format("%d test %s", testInput,nameOfTest));
            
            System.out.println(String.format("    last %d ms", totalTime));
            total.add((int)totalTime/testCount);
        }
        System.out.println("total ms " + total + " " + algorithm+ " " + nameOfTest);
        System.out.println("----------------------");
    }

    public static void insertionSort(int[] arr){
        for (int i = 0; i < arr.length; i++) {
            int currentElement = arr[i];
            for (int j = i-1; j >=0; j--) { 
                if (arr[j] > currentElement ) {
                    arr[j+1] = arr[j];
                    arr[j] = currentElement;
                }
                else{break;} 
            }
        }
    }

    public static void mergeSort(int[] arr){
        Integer[] sortedNums = mergeSortRecur(arr,0,arr.length-1);
        for (int i = 0; i < sortedNums.length; i++) {
            arr[i] = sortedNums[i];
        }
    }

    public static Integer[] mergeSortRecur(int[] arr,int low,int upp){ 
        int mid = low + (int)((upp-low)/2); // set mid point
        Integer[] lower; // lower array
        Integer[] upper; // upper array
        if (low <upp ) { // if there is more than one element in array
            lower = mergeSortRecur(arr, low, mid); 
            upper = mergeSortRecur(arr, mid+1, upp);
            return  merge(lower,upper); // combine 2 arrays
        }
        Integer [] ret = new Integer[upp-low+1]; // copy array
        int index = 0;
        for (int i = low; i < upp+1; i++) {
            ret[index++] = arr[i];
        }
        
        return ret;
    }

    public static Integer[] merge(Integer[] lower , Integer[] upper){
        Integer[] newArr = new Integer[lower.length+upper.length];
        
        int low =0; // current index in lower array
        int upp = 0; // current index in upper array
        for (int i = 0; i < newArr.length; i++) {
            if ( low < lower.length && upp < upper.length) {
                if (lower[low] < upper[upp] ) {
                    newArr[i] = lower[low];
                    low++;
                }
                else{
                    newArr[i] = upper[upp];
                    upp++;
                }
            }
            else{
                if ( low < lower.length) {
                    newArr[i] = lower[low];
                    low++;
                }
                else if ( upp < upper.length){
                    newArr[i] = upper[upp];
                    upp++;
                }
            }
        }

        return newArr; // return merged array
    }

    public static void pigeonholeSort(int arr[]){
        int n = arr.length;
        int max =Collections.max(Arrays.stream(arr).boxed().collect(Collectors.toList()),null);
        int min =  Collections.min(Arrays.stream(arr).boxed().collect(Collectors.toList()),null);
    
        int range = max - min + 1;
        LinkedList<Integer>[] anArray =  new LinkedList[range];

        for (int i = 0; i < n; i++){
            if (anArray[arr[i]-min] == null) {
                anArray[arr[i]-min] = new LinkedList<>();
            }
            anArray[arr[i]-min].add(arr[i]);
        }

        int ind = 0;
        for (int i = 0; i < range; i++) {
            if (anArray[i] !=null) {
                for (int j : anArray[i]) {
                    arr[ind++] = j;
                }
            }
        }
    }

    public static int[] countingSort(int[] arr){ 
        int max =Collections.max(Arrays.stream(arr).boxed().collect(Collectors.toList()),null);
        int min =  Collections.min(Arrays.stream(arr).boxed().collect(Collectors.toList()),null);

        int range = max - min + 1;
        int[] count = new int[range];
        int[] out = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            count[arr[i]-min]++;
        }

        for (int i = 1; i < count.length ; i++) { 
            count[i] +=count[i-1];
        }
        for (int i = 0; i < arr.length; i++) { // create output array
            out[count[arr[i]-min]-1] = arr[i];
            count[arr[i]-min]--;

        }

        return out;
    }

    public static void AllSorts(int[] arr,int number){ // choose sort algorithm
        if (number ==1) {
            insertionSort(arr);
        }
        else if(number ==2){
            mergeSort(arr);
        }
        else if(number ==3){
            pigeonholeSort(arr);
        }
        else if(number ==4){
            countingSort(arr);
        }
    }

    public static void showAndSaveChart(String title, int[] xAxis, double[][] yAxis) throws IOException {
        // Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title(title)
                .yAxisTitle("Time in Milliseconds").xAxisTitle("Input Size").build();

        // Convert x axis to double[]
        double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        // Add a plot for a sorting algorithm
        chart.addSeries("Insertion Sort", doubleX, yAxis[0]);
        chart.addSeries("Merge Sort", doubleX, yAxis[1]);
        chart.addSeries("Pigeonhole Sort", doubleX, yAxis[2]);  
        chart.addSeries("Counting Sort", doubleX, yAxis[3]);

        // Save the chart as PNG
        BitmapEncoder.saveBitmap(chart, title + ".png", BitmapEncoder.BitmapFormat.PNG);

        // Show the chart
        new SwingWrapper(chart).displayChart();
    }

    public static void writeToTable(String nameOfTable,double[][] yAxis){
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File(nameOfTable+".csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String s0 = Arrays.toString( inputAxis);
        s0 = s0.substring(1, s0.length() - 1);
        String[] row0 = s0.split(", ");
            
        String s = Arrays.toString( yAxis[0]);
        s = s.substring(1, s.length() - 1);
        String[] row1 = s.split(", ");

        String s1 = Arrays.toString( yAxis[1]);
        s1 = s1.substring(1, s1.length() - 1);
        String[] row2 = s1.split(", ");

        String s2 = Arrays.toString( yAxis[2]);
        s2 = s2.substring(1, s2.length() - 1);
        String[] row3 = s2.split(", ");

        String s3 = Arrays.toString( yAxis[3]);
        s3 = s3.substring(1, s3.length() - 1);
        String[] row4 = s3.split(", ");

        StringBuilder builder = new StringBuilder();
        String columnNamesList = "Algorithm,"+ String.join(",", row0);

        builder.append(columnNamesList +"\n");
        builder.append("Insertion Sort"+","+String.join(",", row1) + "\n");
        builder.append("Merge Sort"+","+String.join(",", row2) + "\n");
        builder.append("Pigeonhole Sort"+","+String.join(",", row3) + "\n");
        builder.append("Counting Sort"+","+String.join(",", row4) + "\n");

        pw.write(builder.toString());
        pw.close();
    }

    public static void tableAndChart(List<Integer>[] lists,String chartName) throws IOException {
        double[][] yAxis = new double[4][10];
        int[] r0 = lists[0].stream().mapToInt(i->i).toArray();
        int[] r1 = lists[1].stream().mapToInt(i->i).toArray();
        int[] r2 = lists[2].stream().mapToInt(i->i).toArray();
        int[] r3 = lists[3].stream().mapToInt(i->i).toArray();
        yAxis[0] =  Arrays.stream(r0).asDoubleStream().toArray();
        yAxis[1] = Arrays.stream(r1).asDoubleStream().toArray();
        yAxis[2] = Arrays.stream(r2).asDoubleStream().toArray();
        yAxis[3] = Arrays.stream(r3).asDoubleStream().toArray();

        // Save the char as .png and show it
        showAndSaveChart(chartName, inputAxis, yAxis);

        writeToTable(chartName, yAxis);
    }

    public static boolean isSorted(int[] a){
        // base case
        if (a == null || a.length <= 1) {
            return true;
        }

        return IntStream.range(0, a.length - 1).noneMatch(i -> a[i] > a[i + 1]);
    }
}