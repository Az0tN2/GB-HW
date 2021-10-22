package HW1;

import HW1.fruits.Apple;
import HW1.fruits.Orange;

import java.util.ArrayList;
import java.util.Arrays;

public class MainClass<T> {
    public MainClass() {
    }

    public static void main(String[] args) {
        testingMethod(); // Start of the testing method with hardcoded values
    }

    public static void testingMethod() {
        MainClass mainClass = new MainClass(); //creation of an instance to use non-static methods
        int numberOfOranges = 19; //Testing amount of oranges in the box
        int numberOfApples = 15; //Testing amount of apples in the box
        String[] firstArray = {"a", "b", "c", "d", "e"}; //Testing array for the swapElements Method
        mainClass.swapElements(firstArray, 2, 4);
        System.out.println(Arrays.toString(firstArray));//
        Integer[] secondArray = {1, 2, 3, 4, 5};
        mainClass.arrayToArrayList(secondArray);
        Box boxWithOranges = new Box();
        for (int i = 0; i < numberOfOranges; i++) {
            boxWithOranges.addFruit(new Orange());
        }
        Box boxWithApples = new Box();
        for (int i = 0; i < numberOfApples; i++) {
            boxWithApples.addFruit(new Apple());
        }
        System.out.println("Same weight:"+boxWithApples.compare(boxWithOranges));
        System.out.println("Weight is"+boxWithApples.getWeight());
        Box boxWithApplesTwo = boxWithApples;
        boxWithApples.addFruit(boxWithApplesTwo);
        System.out.println("Weight is"+boxWithApples.getWeight());


    }

    public void swapElements(T[] arr, int firstPlace, int secondPlace) {
        if (firstPlace < 0 || secondPlace < 0) {
            System.out.println("Incorrect Place");
        } else if (firstPlace > arr.length - 1 || secondPlace > arr.length - 1) {
            System.out.println("Place number is too large");
        } else {
            T temp;
            temp = arr[firstPlace];
            arr[firstPlace] = arr[secondPlace];
            arr[secondPlace] = temp;
        }
    }

    public ArrayList<T> arrayToArrayList(T[] arr) {
        ArrayList<T> arrayList = new ArrayList<T>();
        for (T element : arr) {
            arrayList.add(element);
        }
        return arrayList;
    }

}
