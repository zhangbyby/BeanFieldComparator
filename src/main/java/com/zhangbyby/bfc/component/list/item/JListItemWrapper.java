package com.zhangbyby.bfc.component.list.item;

import com.intellij.idea.LoggerFactory;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PropertyUtilBase;

/**
 * JList item wrapper for show field or property
 *
 * @author zhangbyby
 */
public class JListItemWrapper {
    private static final Logger logger = new LoggerFactory().getLoggerInstance(JListItemWrapper.class.getName());

    private final boolean isProperty;

    /**
     * field or property name
     */
    private final String fopName;

    /**
     * if {@link JListItemWrapper#isProperty} is <b>false</b>, the psiField
     */
    private PsiField psiField;

    /**
     * if {@link JListItemWrapper#isProperty} is <b>true</b>, the property's getter method<br/>
     * can be null ,if the property is only writable
     */
    private PsiMethod propertyGetterMethod;
    /**
     * if {@link JListItemWrapper#isProperty} is <b>true</b>, the property's setter method<br/>
     * can be null ,if the property is only readable
     */
    private PsiMethod propertySetterMethod;

    /**
     * the field or property type
     */
    private final PsiType fopReturnType;
    /**
     * the field or property type if this field/property is extends from super class
     */
    private final PsiClass fopTypeOwnerType;

    /**
     * if show as field, the label tool tip text<br/>
     * the field's full ref path
     */
    private String fieldToolTipText;
    /**
     * if show as property, the source label tool tip text <br/>
     * the property's getter method full ref path
     */
    private String propertyGetterMethodToolTipText;
    /**
     * if show as property, the target label tool tip text<br/>
     * the property's setter method full ref path
     */
    private String propertySetterMethodToolTipText;

    /**
     * selected type from class chooser
     */
    private PsiClass classChooserSelectedType;


    public JListItemWrapper(PsiClass psiClass, PsiField psiField) {
        this.psiField = psiField;
        this.isProperty = false;
        this.fopName = psiField.getName();
        this.fopReturnType = psiField.getType();

        PsiClass ownerClass = psiField.getContainingClass();
        this.fopTypeOwnerType = ownerClass;

        this.fieldToolTipText = ownerClass.getQualifiedName() + "#" + this.fopName;

        this.classChooserSelectedType = psiClass;
    }

    public JListItemWrapper(PsiClass psiClass, PsiMethod getterOrSetter) {
        this.isProperty = true;
        this.fopName = PropertyUtilBase.getPropertyName(getterOrSetter);
        this.fopReturnType = PropertyUtilBase.getPropertyType(getterOrSetter);

        PsiClass ownerClass = getterOrSetter.getContainingClass();
        this.fopTypeOwnerType = ownerClass;
        String prefix = ownerClass.getQualifiedName() + "#";

        if (PropertyUtilBase.isSimplePropertyGetter(getterOrSetter)) {
            this.propertyGetterMethod = getterOrSetter;
            this.propertyGetterMethodToolTipText = prefix + getterOrSetter.getName();
        } else {
            this.propertySetterMethod = getterOrSetter;
            this.propertySetterMethodToolTipText = prefix + getterOrSetter.getName();
        }

        this.classChooserSelectedType = psiClass;
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
        return !this.fopTypeOwnerType.getQualifiedName().equals(classChooserSelectedType.getQualifiedName());
    }

    public boolean fullSame(JListItemWrapper anotherItem) {
        return this.fopName.equals(anotherItem.getFopName()) && this.fopReturnType.getCanonicalText().equals(anotherItem.getFopReturnType().getCanonicalText());
    }

    public boolean nameSame(JListItemWrapper anotherItem) {
        return this.fopName.equals(anotherItem.getFopName());
    }


    public boolean isProperty() {
        return isProperty;
    }

    public String getFopName() {
        return fopName;
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

    public PsiType getFopReturnType() {
        return fopReturnType;
    }

    public PsiClass getFopTypeOwnerType() {
        return fopTypeOwnerType;
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

    public PsiClass getClassChooserSelectedType() {
        return classChooserSelectedType;
    }

    public void setClassChooserSelectedType(PsiClass classChooserSelectedType) {
        this.classChooserSelectedType = classChooserSelectedType;
    }
}
