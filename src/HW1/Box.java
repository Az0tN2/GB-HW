package HW1;

import HW1.fruits.Fruit;

import java.util.ArrayList;

public class Box {
    private final ArrayList<Fruit> arrayList;

    public Box() {
        this.arrayList = new ArrayList();
    }

    public float getWeight() {
        Fruit fruit = arrayList.get(0);
        return fruit.getWeight() * arrayList.size();
    }

    public void addFruit(Fruit fruit) {
        if (arrayList.isEmpty() || fruit.getClass().equals(arrayList.get(0).getClass())) {
            arrayList.add(fruit);
        } else {
            System.out.println("Wrong fruit");
        }
    }

    public void addFruit(Box box) {

        if (arrayList.isEmpty() || box.getAllFruits().get(0).getClass().equals(arrayList.get(0).getClass())) {
            arrayList.addAll(box.getAllFruits());
        } else {
            System.out.println("Wrong fruits");
        }

    }

    public boolean compare(Box box) {
        return Math.abs(this.getWeight() - box.getWeight()) < 0.00001;
    }

    public ArrayList getAllFruits() {
        return arrayList;
    }
}
