package com.zhangbyby.bfc.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.zhangbyby.bfc.component.dialog.BFCDialogWrapper;
import org.jetbrains.annotations.NotNull;

public class BFCToolMenuAction extends AnAction {
    public static final String PLUGIN_TITLE = "BeanFieldComparator";

    public BFCToolMenuAction() {
        super(PLUGIN_TITLE);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        BFCDialogWrapper dialogWrapper = new BFCDialogWrapper(e.getProject());
        dialogWrapper.showAndGet();
    }

    @Override
    public boolean isDumbAware() {
        return super.isDumbAware();
    }
}
