package menu;

import animation.Animation;
import game.Sprite;
import geometry.Rectangle;
import input.Mouse;
import input.PlayerInput;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class MenuAnimation<T> implements Menu<T>, Animation {
    private List<Selection<T>> selections = new ArrayList<>();
    private T status;
    private PlayerInput input;
    private Sprite background;

    public MenuAnimation(Sprite background, PlayerInput input) {
        this.selections = new ArrayList<>();
        this.status = null;
        this.background = background;
        this.input = input;
    }

    @Override
    public void addSelection(String message, T returnVal, Rectangle rectangle, Color color) {
        selections.add(new Selection<>(message, returnVal, rectangle, color));
    }

    @Override
    public T getStatus() {
        return status;
    }

    @Override
    public void reset() {
        status = null;
    }

    @Override
    public void update(double dt) {

        for (Selection<T> selection: selections) {
            if (selection.isClicked(input.getMouse())) {
                status = selection.getReturnVal();
                break;
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        background.render(gc);

        for (Selection<T> selection : selections) {
            selection.render(gc);
        }
    }

    @Override
    public boolean isFinished() {
        return status != null;
    }

}
