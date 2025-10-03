package org.owleebr.professions.FoxCore.GUIUtils.InventoryGUI.GUIObjects;

public enum ClickType {
    RIGHT_CLICK("[ПКМ]"), LEFT_CLICK("[ЛКМ]"), SHIFT_RIGHT_CLICK("[Shift + ПКМ]"), SHIFT_LEFT_CLICK("[Shift + ЛКМ]"), ANY("Щёлкните, чтобы ");

    private final String textDisplay;

    ClickType(String txt){
        this.textDisplay = txt;
    }

    public String getTextDisplay(){
        return this.textDisplay;
    }
}
