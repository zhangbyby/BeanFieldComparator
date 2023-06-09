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
import com.intellij.util.IncorrectOperationException;
import com.zhangbyby.bfc.component.dialog.BFCDialogWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * intention action
 *
 * @author zhangbyby
 */
public class BFCIntentionAction extends PsiElementBaseIntentionAction {

    private static final Logger logger = new LoggerFactory().getLoggerInstance(BFCIntentionAction.class.getName());

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

        List<PsiType> psiTypes = readArgumentsQualifiedClassName((PsiMethodCallExpression) context);
        PsiType sourcePsiType= isApache ? psiTypes.get(1) : psiTypes.get(0);
        PsiType targetPsiType = isApache ? psiTypes.get(0) : psiTypes.get(1);

        BFCDialogWrapper.sourcePsiType = sourcePsiType;
        BFCDialogWrapper.targetPsiType = targetPsiType;
        ApplicationManager.getApplication().invokeLater(() -> {
            BFCDialogWrapper dialogWrapper = new BFCDialogWrapper(element.getProject());
            dialogWrapper.showAndGet();
            BFCDialogWrapper.sourcePsiType = null;
            BFCDialogWrapper.targetPsiType = null;
        });
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        PsiElement parent = element.getParent();
        if (!(parent instanceof PsiReferenceExpression)) {
            return false;
        }
        PsiElement context = parent.getContext();
        if (!(context instanceof PsiMethodCallExpression)) {
            return false;
        }
        if (!context.getText().contains("copyProperties")) {
            return false;
        }
        PsiMethod psiMethod = ((PsiMethodCallExpression) context).resolveMethod();
        if (psiMethod == null) {
            return false;
        }

        PsiClass methodClass = (PsiClass) psiMethod.getContext();
        if (methodClass == null || methodClass.getQualifiedName() == null) {
            return false;
        }

        return readArgumentsQualifiedClassName((PsiMethodCallExpression) context) != null;
    }

    private List<PsiType> readArgumentsQualifiedClassName(PsiMethodCallExpression context) {
        PsiExpression[] expressions = context.getArgumentList().getExpressions();
        List<PsiType> psiTypes = new ArrayList<>();
        for (PsiExpression expression : expressions) {
            if (!((expression.getType()) instanceof PsiClassType.Stub)) {
                return null;
            }
            PsiClassType.Stub type = (PsiClassType.Stub) expression.getType();
            PsiClass psiClass = type.resolve();
            if (psiClass == null) {
                return null;
            }
            if (psiClass.getQualifiedName() == null) {
                return null;
            }
            psiTypes.add(type);
        }
        return psiTypes.isEmpty() ? null : psiTypes;
    }

    @Override
    public @NotNull
    @IntentionFamilyName String getFamilyName() {
        return "BFC";
    }

    @Override
    public @IntentionName
    @NotNull String getText() {
        return "BFC";
    }
}