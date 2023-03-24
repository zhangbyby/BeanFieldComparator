package com.zhangbyby.bfc.component.list.item;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PropertyUtilBase;

/**
 * JList item wrapper for show field or property
 *
 * @author zhangbyby
 */
public class JListItemWrapper {
    private final boolean isProperty;
    private final String name;

    private PsiField psiField;

    private PsiMethod getterMethod;
    private PsiMethod setterMethod;

    private final String simpleItemType;
    private String tipText;
    private String tipGetterText;
    private String tipSetterText;

    private final String ownerClassSimpleName;

    public JListItemWrapper(PsiField psiField) {
        this.psiField = psiField;
        this.isProperty = false;
        this.name = psiField.getName();
        this.simpleItemType = psiField.getType().getPresentableText();

        PsiClass ownerClass = psiField.getContainingClass();
        this.tipText = ownerClass.getQualifiedName() + "#" + this.name;
        this.ownerClassSimpleName = ownerClass.getName();
    }

    public JListItemWrapper(PsiMethod getterOrSetter, String name) {
        this.isProperty = true;
        this.name = name;
        this.simpleItemType = PropertyUtilBase.getPropertyType(getterOrSetter).getPresentableText();

        PsiClass ownerClass = getterOrSetter.getContainingClass();
        this.ownerClassSimpleName = ownerClass.getName();
        String prefix = ownerClass.getQualifiedName() + "#";

        if (PropertyUtilBase.isSimplePropertyGetter(getterOrSetter)) {
            this.getterMethod = getterOrSetter;
            this.tipGetterText = prefix + getterOrSetter.getName();
        } else {
            this.setterMethod = getterOrSetter;
            this.tipSetterText = prefix + getterOrSetter.getName();
        }
    }

    public JListItemWrapper mergeMethod(JListItemWrapper another) {
        if (this.getterMethod == null) {
            this.getterMethod = another.getterMethod;
            this.tipGetterText = this.getterMethod.getContainingClass().getQualifiedName() + "#" + this.getterMethod.getName();
        } else {
            this.setterMethod = another.setterMethod;
            this.tipSetterText = this.setterMethod.getContainingClass().getQualifiedName() + "#" + this.setterMethod.getName();
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

    public String getSimpleItemType() {
        return simpleItemType;
    }

    public String getTipText() {
        return tipText;
    }

    public String getTipGetterText() {
        return tipGetterText;
    }

    public String getTipSetterText() {
        return tipSetterText;
    }

    public String getOwnerClassSimpleName() {
        return ownerClassSimpleName;
    }
}
