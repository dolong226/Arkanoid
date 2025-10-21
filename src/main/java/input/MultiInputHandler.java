package input;

import java.util.ArrayList;
import java.util.List;

public class MultiInputHandler {
    private List<PlayerInput> players;

    public MultiInputHandler() {
        this.players = new ArrayList<>();
        Keyboard keyboard = new GameKeyboard();
        Mouse mouse = new GameMouse();
        players.add(new PlayerInput(keyboard, mouse));
    }

    public MultiInputHandler(GameKeyboard gameKeyboard, GameMouse gameMouse) {
        this.players = new ArrayList<>();
        players.add(new PlayerInput(gameKeyboard, gameMouse));
    }

    public PlayerInput getPlayerInput(int id) {
        if (id >= 0 && id < players.size()) {
            return players.get(id);
        }
        System.out.println("ID người chơi không hợp lệ");
        return null;
    }

}
