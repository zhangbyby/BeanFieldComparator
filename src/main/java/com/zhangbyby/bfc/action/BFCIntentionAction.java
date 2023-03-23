package com.zhangbyby.bfc.action;

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.idea.LoggerFactory;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.util.IncorrectOperationException;
import com.zhangbyby.bfc.component.dialog.BFCDialogWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 上下文操作入口
 *
 * @author zhangbyby
 */
public class BFCIntentionAction extends PsiElementBaseIntentionAction {

    private static final Logger logger = new LoggerFactory().getLoggerInstance(BFCIntentionAction.class.getSimpleName());

    public BFCIntentionAction() {
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
        PsiElement parent = element.getParent();
        PsiElement context = parent.getContext();
        PsiMethod psiMethod = ((PsiMethodCallExpression) context).resolveMethod();
        PsiClass methodClass = (PsiClass) psiMethod.getContext();
        // apache: target, source
        // other: source, target
        boolean isApache = methodClass.getQualifiedName().startsWith("org.apache.commons.beanutils");

        List<String> classNames = readArgumentsQualifiedClassName((PsiMethodCallExpression) context);
        String sourceClassName = isApache ? classNames.get(1) : classNames.get(0);
        String targetClassName = isApache ? classNames.get(0) : classNames.get(1);

        logger.info("sourceClassName: "+ sourceClassName);
        logger.info("targetClassName: "+ targetClassName);

        BFCDialogWrapper.sourceClassName = sourceClassName;
        BFCDialogWrapper.targetClassName = targetClassName;
        ApplicationManager.getApplication().invokeLater(() -> {
            BFCDialogWrapper dialogWrapper = new BFCDialogWrapper(element.getProject());
            dialogWrapper.showAndGet();
            BFCDialogWrapper.sourceClassName = null;
            BFCDialogWrapper.targetClassName = null;
        });
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        PsiElement parent = element.getParent();
        if (!(parent instanceof PsiReferenceExpression)) {
            logger.info("parent element is not instanceof PsiReferenceExpression: " + parent.getClass());
            return false;
        }
        PsiElement context = parent.getContext();
        if (!(context instanceof PsiMethodCallExpression)) {
            logger.info("parent element context is not instanceof PsiMethodCallExpression: " + Optional.ofNullable(context).map(Object::getClass).map(Class::getCanonicalName).orElse(null));
            return false;
        }
        if (!context.getText().contains("copyProperties")) {
            logger.info("context is not contains 'copyProperties': " + context.getText());
            return false;
        }
        PsiMethod psiMethod = ((PsiMethodCallExpression) context).resolveMethod();
        if (psiMethod == null) {
            logger.info("context resolve method is null");
            return false;
        }
        logger.info("method name: " + psiMethod.getName());

        PsiClass methodClass = (PsiClass) psiMethod.getContext();
        if (methodClass == null || methodClass.getQualifiedName() == null) {
            logger.info("method PsiClass is null or PsiClass qualifiedName is null");
            return false;
        }
        logger.info("class qualifiedName: " + methodClass.getQualifiedName());

        return readArgumentsQualifiedClassName((PsiMethodCallExpression) context) != null;
    }

    private List<String> readArgumentsQualifiedClassName(PsiMethodCallExpression context) {
        PsiExpression[] expressions = context.getArgumentList().getExpressions();
        List<String> classNames = new ArrayList<>();
        for (PsiExpression expression : expressions) {
            if (!((expression.getType()) instanceof PsiClassReferenceType)) {
                return null;
            }
            PsiClassReferenceType type = (PsiClassReferenceType) expression.getType();
            PsiClass psiClass = type.resolve();
            if (psiClass == null) {
                return null;
            }
            if (psiClass.getQualifiedName() == null) {
                return null;
            }
            classNames.add(psiClass.getQualifiedName());
        }
        logger.info("readArgumentsQualifiedClassName result: " + classNames);
        return classNames;
    }

    @Override
    public @NotNull @IntentionFamilyName String getFamilyName() {
        return "BFC";
    }

    @Override
    public @IntentionName @NotNull String getText() {
        return "BFC";
    }
}