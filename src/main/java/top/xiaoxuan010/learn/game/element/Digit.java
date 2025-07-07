package top.xiaoxuan010.learn.game.element;

import lombok.Getter;
import lombok.Setter;
import top.xiaoxuan010.learn.game.element.utils.DigitValueProvider;
import top.xiaoxuan010.learn.game.manager.GameLoader;

@Getter
@Setter
public class Digit extends GameElement {
    private int digitValue = 0;

    private DigitValueProvider digitValueProvider;

    public Digit(int x, int y, DigitValueProvider digitValueProvider) {
        super(x, y, GameLoader.imgMap.get("digits.small.0"));
        this.digitValueProvider = digitValueProvider;
    }

    @Override
    public void update() {
        if (digitValueProvider != null) {
            setDigitValue(digitValueProvider.getValue());
        }
        this.setIcon(GameLoader.imgMap.get("digits.small." + digitValue));
    }
}
