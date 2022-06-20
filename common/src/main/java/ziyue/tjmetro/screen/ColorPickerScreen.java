package ziyue.tjmetro.screen;

import mtr.mappings.ScreenMapper;
import mtr.screen.WidgetColorSelector;

/**
 * @author ZiYueCommentary
 * @see mtr.screen.WidgetColorSelector
 * @since 1.0b
 */

public class ColorPickerScreen extends WidgetColorSelector
{
    public ColorPickerScreen(ScreenMapper screen, Runnable callback) {
        super(screen, callback);
    }
}
