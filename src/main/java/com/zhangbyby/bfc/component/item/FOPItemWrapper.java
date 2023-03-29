package com.zhangbyby.bfc.component.item;

import com.intellij.idea.LoggerFactory;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PropertyUtilBase;
import com.intellij.psi.util.PsiTypesUtil;
import com.zhangbyby.bfc.common.Constants;

import java.util.Map;


/**
 * JList item wrapper for show field or property
 *
 * @author zhangbyby
 */
public class FOPItemWrapper {
    private static final Logger logger = new LoggerFactory().getLoggerInstance(FOPItemWrapper.class.getName());

    private final boolean isProperty;

    /**
     * field or property name
     */
    private final String fopName;

    /**
     * if {@link FOPItemWrapper#isProperty} is <b>false</b>, the psiField
     */
    private PsiField psiField;

    /**
     * if {@link FOPItemWrapper#isProperty} is <b>true</b>, the property's getter method<br/>
     * can be null ,if the property is only writable
     */
    private PsiMethod propertyGetterMethod;
    /**
     * if {@link FOPItemWrapper#isProperty} is <b>true</b>, the property's setter method<br/>
     * can be null ,if the property is only readable
     */
    private PsiMethod propertySetterMethod;

    /**
     * the field or property type
     */
    private final PsiType fopType;
    /**
     * the field or property declared class
     */
    private final PsiClass fopDeclareClass;

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
    private PsiClass classChooserSelectedClass;

    private final boolean isTarget;
    private String castTargetTypeSimpleName;

    private final Project project;

    private final String actualTypeName;

    public FOPItemWrapper(Project project, PsiClass psiClass, PsiField psiField, boolean isTarget) {
        this.project = project;
        this.psiField = psiField;
        this.isProperty = false;
        this.fopName = psiField.getName();
        this.fopType = psiField.getType();

        this.fopDeclareClass = psiField.getContainingClass();
        this.fieldToolTipText = fopDeclareClass.getQualifiedName() + "#" + this.fopName;
        this.classChooserSelectedClass = psiClass;
        this.isTarget = isTarget;

        this.actualTypeName = actualTypeName();
    }

    public FOPItemWrapper(Project project, PsiClass psiClass, PsiMethod getterOrSetter, boolean isTarget) {
        this.project = project;
        this.isProperty = true;
        this.fopName = PropertyUtilBase.getPropertyName(getterOrSetter);
        this.fopType = PropertyUtilBase.getPropertyType(getterOrSetter);

        this.fopDeclareClass = getterOrSetter.getContainingClass();
        String prefix = fopDeclareClass.getQualifiedName() + "#";

        if (PropertyUtilBase.isSimplePropertyGetter(getterOrSetter)) {
            this.propertyGetterMethod = getterOrSetter;
            this.propertyGetterMethodToolTipText = prefix + getterOrSetter.getName();
        } else {
            this.propertySetterMethod = getterOrSetter;
            this.propertySetterMethodToolTipText = prefix + getterOrSetter.getName();
        }
        this.isTarget = isTarget;

        this.classChooserSelectedClass = psiClass;

        this.actualTypeName = actualTypeName();
    }

    public FOPItemWrapper mergeMethod(FOPItemWrapper another) {
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
        return !this.fopDeclareClass.getQualifiedName().equals(classChooserSelectedClass.getQualifiedName());
    }

    public boolean fullSame(FOPItemWrapper anotherItem) {
        if (!this.fopName.equals(anotherItem.getFopName())) {
            return false;
        }

        boolean canCast = false;

        if (actualTypeClass() != null && anotherItem.actualTypeClass() != null) {
            if (isTarget) {
                canCast = actualTypeClass().isAssignableFrom(anotherItem.actualTypeClass());
            } else {
                canCast = anotherItem.actualTypeClass().isAssignableFrom(actualTypeClass());
            }
            if (canCast && isTarget) {
                this.castTargetTypeSimpleName = anotherItem.getActualTypeName();
            } else if (canCast) {
                anotherItem.castTargetTypeSimpleName = getActualTypeName();
            }
        }

        return this.actualTypeName.equals(anotherItem.actualTypeName)
                || canCast;
    }

    public boolean nameSame(FOPItemWrapper anotherItem) {
        return this.fopName.equals(anotherItem.getFopName());
    }

    private PsiType actualTypeClass() {
        PsiClass fopActualClass = ((PsiClassReferenceType) fopType).resolve();
        String name = fopType.getPresentableText();
        if (fopActualClass instanceof PsiTypeParameter) {
            String fullClassName = isTarget ?
                    Constants.TARGET_GENERICS.get(fopDeclareClass.getQualifiedName()).get(name).split(",")[0]
                    : Constants.SOURCE_GENERICS.get(fopDeclareClass.getQualifiedName()).get(name).split(",")[0];
            return
                    PsiTypesUtil.getClassType(JavaPsiFacade
                            .getInstance(project)
                            .findClass(fullClassName, GlobalSearchScope.allScope(project))
                    );
        }
        return fopType;
    }

    public String actualTypeName() {
        PsiClass resolve = ((PsiClassReferenceType) fopType).resolve();
        String name = fopType.getPresentableText();
        if (resolve instanceof PsiTypeParameter) {
            return isTarget ?
                    Constants.TARGET_GENERICS.get(fopDeclareClass.getQualifiedName()).get(name).split(",")[1]
                    : Constants.SOURCE_GENERICS.get(fopDeclareClass.getQualifiedName()).get(name).split(",")[1];
        }
        if (((PsiClassReferenceType) fopType).hasParameters()) {
            Map<String, String> generics = isTarget ?
                    Constants.TARGET_GENERICS.get(fopDeclareClass.getQualifiedName())
                    : Constants.SOURCE_GENERICS.get(fopDeclareClass.getQualifiedName());
            for (Map.Entry<String, String> entry : generics.entrySet()) {
                String genericName = entry.getKey();
                String simpleTypeParameterClassName = entry.getValue().split(",")[1];
                name = name.replace("<" + genericName + ",", "<" + simpleTypeParameterClassName + ",")
                        .replace(" " + genericName + ",", " " + simpleTypeParameterClassName + ",")
                        .replace(" " + genericName + ">", " " + simpleTypeParameterClassName + ">")
                        .replace("<" + genericName + ">", "<" + simpleTypeParameterClassName + ">");
            }
        }
        return name;
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

    public PsiType getFopType() {
        return fopType;
    }

    public PsiClass getFopDeclareClass() {
        return fopDeclareClass;
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

    public PsiClass getClassChooserSelectedClass() {
        return classChooserSelectedClass;
    }

    public void setClassChooserSelectedClass(PsiClass classChooserSelectedClass) {
        this.classChooserSelectedClass = classChooserSelectedClass;
    }

    public boolean isTarget() {
        return isTarget;
    }

    public String getCastTargetTypeSimpleName() {
        return castTargetTypeSimpleName;
    }

    public void setCastTargetTypeSimpleName(String castTargetTypeSimpleName) {
        this.castTargetTypeSimpleName = castTargetTypeSimpleName;
    }

    public Project getProject() {
        return project;
    }

    public String getActualTypeName() {
        return actualTypeName;
    }

    @Override
    public String toString() {
        return "FOPItemWrapper{" +
                "isProperty=" + isProperty +
                ", fopName='" + fopName + '\'' +
                ", psiField=" + psiField +
                ", propertyGetterMethod=" + propertyGetterMethod +
                ", propertySetterMethod=" + propertySetterMethod +
                ", fopType=" + fopType +
                ", fopDeclareClass=" + fopDeclareClass +
                ", fieldToolTipText='" + fieldToolTipText + '\'' +
                ", propertyGetterMethodToolTipText='" + propertyGetterMethodToolTipText + '\'' +
                ", propertySetterMethodToolTipText='" + propertySetterMethodToolTipText + '\'' +
                ", classChooserSelectedClass=" + classChooserSelectedClass +
                ", isTarget=" + isTarget +
                ", castTargetTypeSimpleName='" + castTargetTypeSimpleName + '\'' +
                ", project=" + project +
                ", actualTypeName='" + actualTypeName + '\'' +
                '}';
    }
}
