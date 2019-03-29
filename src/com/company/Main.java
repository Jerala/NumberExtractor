package com.company;

import java.util.*;

public class Main {
    private List numbers;

    public Main() {
        numbers = new ArrayList<Integer>();
    }

    public void addElement() {
        String numberStr;
        Scanner scanner = new Scanner(System.in);
        int number;
        while (true) {
            if (scanner.hasNext()) {
                numberStr = scanner.nextLine();
                number = StringToInt(numberStr);
                if (number == -1) {
                    System.out.println("Input error");
                    continue;
                }
                synchronized (numbers) {
                    numbers.add(number);
                }
            }
        }
    }

    public void removeElement() throws InterruptedException {
        synchronized (numbers) {
            int min;
            while (true) {
                numbers.wait(5000);
                if (!numbers.isEmpty()) {
                    min = (int) Collections.min(numbers);
                    System.out.println(min);
                    numbers.remove(numbers.indexOf(min));
                }
            }
        }
    }

    public int StringToInt(String numStr) {

        if (numStr.equals("zero")) return 0;

        String[] digits = new String[]{"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
                "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen",
                "twenty"};
        String[] numbers = numStr.split(" ");
        String[] decades = new String[]{null, null, "twenty", "thirty",
                "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};

        int result = 0;
        int factor = 1;
        for (int curWord = numbers.length - 1; curWord >= 0; curWord--) {
            if (numbers[curWord].equals("hundred")) {
                factor *= 100;
                if (factor < result)
                    factor = 100000;
            } else if (numbers[curWord].equals("thousand"))
                factor *= 1000;
            else if (numbers[curWord].contains("-")) {
                result += (Arrays.asList(decades).indexOf(numbers[curWord].substring(0,
                        numbers[curWord].indexOf("-"))) * 10
                        + Arrays.asList(digits).indexOf
                        (numbers[curWord].substring(numbers[curWord].indexOf("-") + 1))) * factor;
                factor = 1;
            } else if (numbers[curWord].equals("and"))
                continue;
            else {
                result += Arrays.asList(digits).indexOf(numbers[curWord]) * factor;
                factor = 1;
            }
        }

        return result;
    }

    public static void main(String[] args) {

        Main demo = new Main();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                demo.addElement();
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    demo.removeElement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        t2.start();
    }
}

