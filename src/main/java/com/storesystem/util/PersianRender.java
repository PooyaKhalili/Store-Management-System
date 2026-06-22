package com.storesystem.util;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;

public class PersianRender {
    public static String format(String text) {
        if (text == null || text.isEmpty()) return "";
        try {
            // 1. چسباندن حروف (Shaping)
            ArabicShaping shaper = new ArabicShaping(ArabicShaping.LETTERS_SHAPE | ArabicShaping.DIGITS_EN2AN);
            String shaped = shaper.shape(text);

            // 2. اصلاح جهت (Bidi)
            Bidi bidi = new Bidi(shaped, Bidi.DIRECTION_RIGHT_TO_LEFT);
            bidi.setReorderingMode(Bidi.REORDER_DEFAULT);
            return bidi.writeReordered(Bidi.DO_MIRRORING);
        } catch (ArabicShapingException e) {
            return text;
        }
    }
}
