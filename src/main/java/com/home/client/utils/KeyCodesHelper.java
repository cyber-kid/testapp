package com.home.client.utils;

import static com.google.gwt.event.dom.client.KeyCodes.*;

public class KeyCodesHelper {
    public static boolean isLetterKey (int code) {
        switch (code) {
            case KEY_A:
            case KEY_B:
            case KEY_C:
            case KEY_D:
            case KEY_E:
            case KEY_F:
            case KEY_G:
            case KEY_H:
            case KEY_I:
            case KEY_J:
            case KEY_K:
            case KEY_L:
            case KEY_M:
            case KEY_N:
            case KEY_O:
            case KEY_P:
            case KEY_Q:
            case KEY_R:
            case KEY_S:
            case KEY_T:
            case KEY_U:
            case KEY_V:
            case KEY_W:
            case KEY_X:
            case KEY_Y:
            case KEY_Z:
                return true;
            default:
                return false;
        }
    }

    public static boolean isDigitKey (int code) {
        switch (code) {
            case KEY_ZERO:
            case KEY_ONE:
            case KEY_TWO:
            case KEY_THREE:
            case KEY_FOUR:
            case KEY_FIVE:
            case KEY_SIX:
            case KEY_SEVEN:
            case KEY_EIGHT:
            case KEY_NINE:
                return true;
            default:
                return false;
        }
    }
}
