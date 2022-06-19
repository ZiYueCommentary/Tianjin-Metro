package ziyue.tjmetro.screen;

import mtr.mappings.ScreenMapper;
import mtr.screen.WidgetColorSelector;

/**
 * @author ZiYueCommentary
 * @since 1.0b
 * @see mtr.screen.WidgetColorSelector
 */

public class ColorPickerScreen extends WidgetColorSelector
{
    public ColorPickerScreen(ScreenMapper screen, Runnable callback) {
        super(screen, callback);
    }
}
