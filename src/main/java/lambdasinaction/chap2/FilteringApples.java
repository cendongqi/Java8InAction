package lambdasinaction.chap2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class FilteringApples {

    public static void main(String... args) {

        List<Apple> inventory = Arrays.asList(new Apple(80, "green"), new Apple(155, "green"), new Apple(120, "red"));
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // 第一种方案：只能选出绿苹果，每增加一种颜色筛选需求，都要增加一个方法
        // [Apple{color='green', weight=80}, Apple{color='green', weight=155}]
        List<Apple> filterGreenApples = filterGreenApples(inventory);
        System.out.println(filterGreenApples);

        // 第二种方案：把颜色作为参数
        // [Apple{color='green', weight=80}, Apple{color='green', weight=155}]
        List<Apple> greenApples = filterApplesByColor(inventory, "green");
        System.out.println(greenApples);

        // [Apple{color='red', weight=120}]
        List<Apple> redApples = filterApplesByColor(inventory, "red");
        System.out.println(redApples);

        // 新增加重量筛选，只能新增一个方法，但是代码还是复制【filterApplesByColor】的
        // [Apple{color='green', weight=155}]
        List<Apple> applesByWeight = filterApplesByWeight(inventory, 150);
        System.out.println(applesByWeight);

        // 笨拙的尝试：把你想到的筛选条件都列出来，依然不能应对变化的需求
        List<Apple> filterApples = filterApples(inventory, "green", 0, true);
        System.out.println(filterApples);

        // 使用了实现接口的方式，用了很多模板代码封装
        // 新增一种行为，不得不新增一个实现类，然后实例化一次，太啰嗦
        List<Apple> greenApples2 = filter(inventory, new AppleColorPredicate());
        System.out.println(greenApples2);

        List<Apple> heavyApples = filter(inventory, new AppleWeightPredicate());
        System.out.println(heavyApples);

        List<Apple> redAndHeavyApples = filter(inventory, new AppleRedAndHeavyPredicate());
        System.out.println(redAndHeavyApples);

        // 使用匿名类，随用随建，减少类声明
        // 笨重且容易无解，参见MeaningOfThis
        List<Apple> redApples2 = filter(inventory, new ApplePredicate() {
            public boolean test(Apple a) {
                return a.getColor().equals("red");
            }
        });
        System.out.println(redApples2);

        // 使用lambda表达式
        List<Apple> redApples3 = filter(inventory, a -> a.getColor().equals("green"));
        System.out.println(redApples3);

        // 不局限于某个类型，使用Predicate解决更广泛的问题
        List<Apple> redApples4 = filterWithPredicate(inventory, a -> a.getColor().equals("red"));
        System.out.println(redApples4);
        List<Integer> evenNumbers = filterWithPredicate(numbers, i -> i % 2 == 0);
        System.out.println(evenNumbers);
    }

    public static List<Apple> filterGreenApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if ("green".equals(apple.getColor())) {
                result.add(apple);
            }
        }
        return result;
    }

    public static List<Apple> filterApplesByColor(List<Apple> inventory, String color) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (apple.getColor().equals(color)) {
                result.add(apple);
            }
        }
        return result;
    }

    public static List<Apple> filterApplesByWeight(List<Apple> inventory, int weight) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (apple.getWeight() > weight) {
                result.add(apple);
            }
        }
        return result;
    }

    public static List<Apple> filterApples(List<Apple> inventory, String color, int weight, boolean flag) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if ((flag && apple.getColor().equals(color)) ||
                    (!flag && apple.getWeight() > weight)) {
                result.add(apple);
            }
        }
        return result;
    }


    /**
     * 谓词对象封装了测试苹果的条件
     */
    public static List<Apple> filter(List<Apple> inventory, ApplePredicate p) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (p.test(apple)) {
                result.add(apple);
            }
        }
        return result;
    }

    public static <T> List<T> filterWithPredicate(List<T> inventory, Predicate<T> p) {
        List<T> result = new ArrayList<>();
        for (T t : inventory) {
            if (p.test(t)) {
                result.add(t);
            }
        }
        return result;
    }

    public static class Apple {
        private int weight;
        private String color;

        public Apple(int weight, String color) {
            this.weight = weight;
            this.color = color;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String toString() {
            return "Apple{" +
                    "color='" + color + '\'' +
                    ", weight=" + weight +
                    '}';
        }
    }

    /**
     * 抽象：对某种属性判断，返回一个boolean值
     */
    interface ApplePredicate {
        boolean test(Apple a);
    }

    /**
     * 对weight属性的筛选
     */
    static class AppleWeightPredicate implements ApplePredicate {
        public boolean test(Apple apple) {
            return apple.getWeight() > 150;
        }
    }

    /**
     * 对color属性的筛选
     */
    static class AppleColorPredicate implements ApplePredicate {
        public boolean test(Apple apple) {
            return "green".equals(apple.getColor());
        }
    }

    /**
     * 同时对weight和color的筛选
     */
    static class AppleRedAndHeavyPredicate implements ApplePredicate {
        public boolean test(Apple apple) {
            return "red".equals(apple.getColor())
                    && apple.getWeight() > 150;
        }
    }
}