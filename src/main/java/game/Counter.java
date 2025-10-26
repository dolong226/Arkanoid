package game;

public class Counter {
    private int value;

    public Counter() {
        this.value = 0;
    }

    public Counter(int value) {
        this.value = value;
    }

    public static Counter ZERO = new Counter(0);
    public void increase(int number) {
        this.value += number;
    }

    public void decrease(int number) {
        this.value -= number;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public void reset() {
        this.value = 0;
    }
}
