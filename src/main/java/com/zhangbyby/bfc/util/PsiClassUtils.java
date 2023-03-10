package com.zhangbyby.bfc.util;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class PsiClassUtils {
    public static PsiField[] filterFields(PsiField[] psiFields, JCheckBox hideStatic, JCheckBox hideFinal) {
        List<PsiField> collect = Arrays.stream(psiFields).filter(it -> {
            PsiModifierList modifierList = it.getModifierList();
            if (modifierList == null) {
                return true;
            }
            if (hideStatic.isSelected() && modifierList.hasExplicitModifier(PsiModifier.STATIC)) {
                return false;
            }
            if (hideFinal.isSelected() && modifierList.hasExplicitModifier(PsiModifier.FINAL)) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());
        return collect.toArray(PsiField[]::new);
    }
}
