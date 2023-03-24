package com.zhangbyby.bfc.component.list.item;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PropertyUtilBase;

import java.util.Optional;

public class JListItemWrapper {
    private final boolean isProperty;
    private final String name;

    private PsiField psiField;

    private PsiMethod getterMethod;
    private PsiMethod setterMethod;

    private final String simpleTypeText;
    private final String tipText;

    public JListItemWrapper(PsiField psiField) {
        this.psiField = psiField;
        this.isProperty = false;
        this.name = psiField.getName();
        this.simpleTypeText = psiField.getType().getPresentableText();
        this.tipText = Optional.ofNullable(psiField.getContainingClass())
                .map(PsiClass::getQualifiedName).orElse("") + "#" + this.name;
    }

    public JListItemWrapper(PsiMethod getterOrSetter, String name) {
        this.isProperty = true;
        this.name = name;
        this.simpleTypeText = PropertyUtilBase.getPropertyType(getterOrSetter).getPresentableText();
        this.tipText = Optional.ofNullable(getterOrSetter.getContainingClass())
                .map(PsiClass::getQualifiedName).orElse("") + "#" + this.name;
        if (PropertyUtilBase.isSimplePropertyGetter(getterOrSetter)) {
            this.getterMethod = getterOrSetter;
        } else {
            this.setterMethod = getterOrSetter;
        }
    }

    public JListItemWrapper merge(JListItemWrapper another) {
        if (this.getterMethod == null) {
            this.getterMethod = another.getterMethod;
        } else {
            this.setterMethod = another.setterMethod;
        }
        return this;
    }

    public PsiField getPsiField() {
        return psiField;
    }

    public void setPsiField(PsiField psiField) {
        this.psiField = psiField;
    }

    public PsiMethod getGetterMethod() {
        return getterMethod;
    }

    public void setGetterMethod(PsiMethod getterMethod) {
        this.getterMethod = getterMethod;
    }

    public PsiMethod getSetterMethod() {
        return setterMethod;
    }

    public void setSetterMethod(PsiMethod setterMethod) {
        this.setterMethod = setterMethod;
    }

    public boolean isProperty() {
        return isProperty;
    }

    public String getName() {
        return name;
    }

    public String getSimpleTypeText() {
        return simpleTypeText;
    }

    public String getTipText() {
        return tipText;
    }
}
