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

    private final String fieldOrProperty;

    private PsiField psiField;

    private PsiMethod propertyGetterMethod;
    private PsiMethod propertySetterMethod;

    private final String fieldOrPropertySimpleClassName;

    private String fieldToolTipText;
    private String propertyGetterMethodToolTipText;
    private String propertySetterMethodToolTipText;

    private PsiClass selectedClass;
    private final String selectedClassSimpleName;
    private final String fieldOrPropertyOwnerClassSimpleName;


    public JListItemWrapper(PsiClass psiClass, PsiField psiField) {
        this.psiField = psiField;
        this.isProperty = false;
        this.fieldOrProperty = psiField.getName();
        this.fieldOrPropertySimpleClassName = psiField.getType().getPresentableText();

        PsiClass ownerClass = psiField.getContainingClass();
        this.fieldToolTipText = ownerClass.getQualifiedName() + "#" + this.fieldOrProperty;
        this.fieldOrPropertyOwnerClassSimpleName = ownerClass.getName();

        this.selectedClass = psiClass;
        this.selectedClassSimpleName = psiClass.getName();
    }

    public JListItemWrapper(PsiClass psiClass, PsiMethod getterOrSetter, String name) {
        this.isProperty = true;
        this.fieldOrProperty = name;
        this.fieldOrPropertySimpleClassName = PropertyUtilBase.getPropertyType(getterOrSetter).getPresentableText();

        PsiClass ownerClass = getterOrSetter.getContainingClass();
        this.fieldOrPropertyOwnerClassSimpleName = ownerClass.getName();
        String prefix = ownerClass.getQualifiedName() + "#";

        if (PropertyUtilBase.isSimplePropertyGetter(getterOrSetter)) {
            this.propertyGetterMethod = getterOrSetter;
            this.propertyGetterMethodToolTipText = prefix + getterOrSetter.getName();
        } else {
            this.propertySetterMethod = getterOrSetter;
            this.propertySetterMethodToolTipText = prefix + getterOrSetter.getName();
        }

        this.selectedClass = psiClass;
        this.selectedClassSimpleName = psiClass.getName();
    }

    public JListItemWrapper mergeMethod(JListItemWrapper another) {
        if (this.propertyGetterMethod == null) {
            this.propertyGetterMethod = another.propertyGetterMethod;
            this.propertyGetterMethodToolTipText = this.propertyGetterMethod.getContainingClass().getQualifiedName() + "#" + this.propertyGetterMethod.getName();
        } else {
            this.propertySetterMethod = another.propertySetterMethod;
            this.propertySetterMethodToolTipText = this.propertySetterMethod.getContainingClass().getQualifiedName() + "#" + this.propertySetterMethod.getName();
        }
        return this;
    }

    public boolean isFromSuperType() {
        return !fieldOrPropertyOwnerClassSimpleName.equals(selectedClassSimpleName);
    }

    public boolean isProperty() {
        return isProperty;
    }

    public String getFieldOrProperty() {
        return fieldOrProperty;
    }

    public PsiField getPsiField() {
        return psiField;
    }

    public void setPsiField(PsiField psiField) {
        this.psiField = psiField;
    }

    public PsiMethod getPropertyGetterMethod() {
        return propertyGetterMethod;
    }

    public void setPropertyGetterMethod(PsiMethod propertyGetterMethod) {
        this.propertyGetterMethod = propertyGetterMethod;
    }

    public PsiMethod getPropertySetterMethod() {
        return propertySetterMethod;
    }

    public void setPropertySetterMethod(PsiMethod propertySetterMethod) {
        this.propertySetterMethod = propertySetterMethod;
    }

    public String getFieldOrPropertySimpleClassName() {
        return fieldOrPropertySimpleClassName;
    }

    public String getFieldToolTipText() {
        return fieldToolTipText;
    }

    public void setFieldToolTipText(String fieldToolTipText) {
        this.fieldToolTipText = fieldToolTipText;
    }

    public String getPropertyGetterMethodToolTipText() {
        return propertyGetterMethodToolTipText;
    }

    public void setPropertyGetterMethodToolTipText(String propertyGetterMethodToolTipText) {
        this.propertyGetterMethodToolTipText = propertyGetterMethodToolTipText;
    }

    public String getPropertySetterMethodToolTipText() {
        return propertySetterMethodToolTipText;
    }

    public void setPropertySetterMethodToolTipText(String propertySetterMethodToolTipText) {
        this.propertySetterMethodToolTipText = propertySetterMethodToolTipText;
    }

    public PsiClass getSelectedClass() {
        return selectedClass;
    }

    public void setSelectedClass(PsiClass selectedClass) {
        this.selectedClass = selectedClass;
    }

    public String getSelectedClassSimpleName() {
        return selectedClassSimpleName;
    }

    public String getFieldOrPropertyOwnerClassSimpleName() {
        return fieldOrPropertyOwnerClassSimpleName;
    }
}
