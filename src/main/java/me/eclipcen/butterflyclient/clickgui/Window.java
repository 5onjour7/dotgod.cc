package me.eclipcen.butterflyclient.clickgui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.clickgui.buttons.Button;
import me.eclipcen.butterflyclient.clickgui.buttons.SubBind;
import me.eclipcen.butterflyclient.clickgui.buttons.SubButton;
import me.eclipcen.butterflyclient.clickgui.buttons.SubColor;
import me.eclipcen.butterflyclient.clickgui.buttons.SubMode;
import me.eclipcen.butterflyclient.clickgui.buttons.SubSlider;
import me.eclipcen.butterflyclient.clickgui.utils.ColorUtils;
import me.eclipcen.butterflyclient.clickgui.utils.GuiUtils;
import me.eclipcen.butterflyclient.module.api.Category;
import me.eclipcen.butterflyclient.module.api.Module;
import me.eclipcen.butterflyclient.util.colors.Colors;
import me.eclipcen.butterflyclient.util.font.FontUtils;
import me.eclipcen.butterflyclient.util.settings.Setting;
import me.eclipcen.butterflyclient.util.settings.setting.ColorSetting;

public class Window extends BaseButton {
   public boolean isOpen = true;
   private final String text;
   private final List<BaseButton> buttons = new ArrayList<>();
   private int renderYButton = 0;
   private boolean isDragging = false;
   private int dragX = 0;
   private int dragY = 0;
   private final Category category;

   public Window(int x, int y, String name, Category category) {
      super(x, y, 100, 12);
      this.text = name;
      this.category = category;
   }

   public void processMouseClick(int mouseX, int mouseY, int button) {
      updateIsMouseHovered(mouseX, mouseY);
      if (isMouseHovered()) {
         if (button == 0) {
            isDragging = true;
            dragX = mouseX - getX();
            dragY = mouseY - getY();
         } else if (button == 1) {
            isOpen = !isOpen;
         }
      }

      for (BaseButton baseButton : buttons) {
         if (baseButton.shouldRender()) {
            baseButton.processMouseClick(mouseX, mouseY, button);
         }
      }

   }

   public void processMouseRelease(int mouseX, int mouseY, int button) {
      updateIsMouseHovered(mouseX, mouseY);
      if (isDragging) {
         isDragging = false;
      }

      for (BaseButton baseButton : buttons) {
         if (baseButton.shouldRender()) {
            baseButton.processMouseRelease(mouseX, mouseY, button);
         }
      }

   }

   public void processKeyPress(char character, int key) {
      for (BaseButton button : buttons) {
         if (button.shouldRender()) {
            button.processKeyPress(character, key);
         }
      }
   }

   public void draw(int mouseX, int mouseY) {
      if (isDragging) {
         setX(mouseX - dragX);
         setY(mouseY - dragY);
      }

      updateIsMouseHovered(mouseX, mouseY);
      renderYButton = getY() + 15;
      GuiUtils.drawRect(getX(), getY(), getWidth(), 13, ((Color)colours.global.getValue()).getRGB());
      FontUtils.drawStringWithShadow(text, (float)(getX() + 2), (float)(getY() + 2), Colors.WHITE);
      if (isOpen) {
         GuiUtils.drawRect(getX(), getY() + 13, getWidth(), getHeight() + 4 - 13, getColor());

         for (BaseButton button : buttons) {
             if (button.shouldRender()) {
                 button.draw(mouseX, mouseY);
                 renderYButton += button.getHeight() + 1;
             }
         }
      }

   }

   public int getHeight() {
      int i = height;

      for (BaseButton button : buttons) {
         if (button.shouldRender() && !(button instanceof Window)) {
            i += button.getHeight() + 1;
         }
      }

      return i;
   }

   public void openGui() {
      for (BaseButton button : buttons) {
         button.openGui();
      }
   }

   public boolean shouldRender() {
      return true;
   }

   public boolean isOpen() {
      return isOpen;
   }

   public int getColor() {
      return ColorUtils.getColorForGuiEntry(1, isMouseHovered(), false);
   }

   public void init(Category category) {
      List<Module> modules = Butterfly.getInstance().getModuleManager().getModsByCategory(category);

      for (Module module : modules) {
         Button b = addButton(new Button(this, module));
         addSubBind(new SubBind(b));
         for (Setting<?> setting : module.getSettingList()) {
            Object value = setting.getValue();
            if (value instanceof Enum) {
               addSubMode(new SubMode(b, (Setting<Enum>) setting));
            } else if (value instanceof Number) {
               addSubSlider(new SubSlider(b, (Setting<Number>) setting));
            } else if (value instanceof Boolean) {
               addSubButton(new SubButton(b, (Setting<Boolean>) setting));
            } else if (setting instanceof ColorSetting) {
               addColour(new SubColor(b, (ColorSetting) setting));
            }
         }
      }

   }

   private Button addButton(Button b) {
      buttons.add(b);
      return b;
   }

   private void addSubButton(SubButton b) {
      buttons.add(buttons.indexOf(b.getParent()) + 1, b);
   }

   private void addSubBind(SubBind b) {
      buttons.add(buttons.indexOf(b.getParent()) + 1, b);
   }

   private void addSubMode(SubMode b) {
      buttons.add(buttons.indexOf(b.getParent()) + 1, b);
   }

   private void addSubSlider(SubSlider slider) {
      buttons.add(buttons.indexOf(slider.getParent()) + 1, slider);
   }

   private void addColour(SubColor color) {
      buttons.add(buttons.indexOf(color.getParent()) + 1, color);
   }

   public int getRenderYButton() {
      return renderYButton;
   }

   public Category getCategory() {
      return category;
   }
}
