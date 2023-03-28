package com.zhangbyby.bfc.common;

import com.intellij.psi.*;
import com.intellij.psi.util.PropertyUtilBase;
import com.zhangbyby.bfc.component.item.FOPItemWrapper;
import com.zhangbyby.bfc.ui.BFCMainUI;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class PsiClassUtils {
    public static FOPItemWrapper[] filterElements(PsiClass psiClass, BFCMainUI mainUI, boolean isTarget) {
        if (mainUI.getDisPropertyGroup().isSelected()) {
            // get all fields
            PsiField[] allFields = psiClass.getAllFields();
            return Arrays.stream(allFields).filter(it -> {
                PsiModifierList modifierList = it.getModifierList();
                if (modifierList == null) {
                    return true;
                }
                if (mainUI.getHideStatic().isSelected() && modifierList.hasExplicitModifier(PsiModifier.STATIC)) {
                    return false;
                }
                if (mainUI.getHideFinal().isSelected() && modifierList.hasExplicitModifier(PsiModifier.FINAL)) {
                    return false;
                }
                if (isTarget && mainUI.getAutoHide().isSelected() && modifierList.hasExplicitModifier(PsiModifier.FINAL)) {
                    return false;
                }
                return true;
            }).map(it -> new FOPItemWrapper(mainUI.getProject(), psiClass, it, isTarget)).toArray(FOPItemWrapper[]::new);
        } else {
            // get all methods
            PsiMethod[] allMethods = psiClass.getAllMethods();

            // getPropertyName
            Map<String, FOPItemWrapper> propertiesMap = Arrays.stream(allMethods)
                    .filter(it -> !"getClass".equals(it.getName()))
                    .filter(it -> PropertyUtilBase.isSimplePropertyGetter(it) || PropertyUtilBase.isSimplePropertySetter(it))
                    .map(it -> new FOPItemWrapper(mainUI.getProject(), psiClass, it, isTarget))
                    .collect(Collectors.toMap(
                            FOPItemWrapper::getFopName,
                            it -> it,
                            FOPItemWrapper::mergeMethod))
                    .entrySet().stream().filter((entry) -> {
                        if (isTarget) {
                            return entry.getValue().getPropertySetterMethod() != null;
                        } else {
                            return entry.getValue().getPropertyGetterMethod() != null;
                        }
                    }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            return propertiesMap.values().toArray(new FOPItemWrapper[0]);
        }
    }
}
