package Game.Logic;

import Game.Database.Bubble;
import Game.Database.BubbleDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Seed {

    private BubbleDB bubbleDB = BubbleDB.getInstance();

    public Bubble[] convertToBubbleArray(String seed) {
        String[] seedArray = seed.split("");
        Bubble[] convertedString = new Bubble[seedArray.length];
        BubbleFactory bubbleFactory = new BubbleFactory();

        for (int i = 0; i < seedArray.length; i++) {
            String numberAsString = seedArray[i];
            convertedString[i] = bubbleFactory.createBubble(Integer.parseInt(numberAsString));
        }
        return convertedString;
    }

    public List<Integer> analyseSeed(String seed){
        int amountOfBubbles = bubbleDB.getAmountOfBubbles();
        int[] countedElements = new int[amountOfBubbles + 1];
        List<Integer> bubbleTypes = new ArrayList<Integer>();

        for(int i = 0; i < amountOfBubbles + 1; i++){
            countedElements[i] = seed.length() - seed.replace(Integer.toString(i), "").length();
            seed = seed.replace(Integer.toString(i), "");
        }

        for(int i = 1; i < countedElements.length; i++) {
            if (countedElements[i] != 0) {
                if (0 < i && i < 6) {
                    bubbleTypes.add(i);
                }
            }
        }


        Random rand = new Random();

        int randomNumber = rand.nextInt(6);

        if(!bubbleTypes.contains(randomNumber+6)){ bubbleTypes.add(randomNumber+6); }
        return bubbleTypes;
    }
}
