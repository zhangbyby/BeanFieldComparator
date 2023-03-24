package com.zhangbyby.bfc.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.zhangbyby.bfc.common.Constants;
import com.zhangbyby.bfc.component.dialog.BFCDialogWrapper;
import org.jetbrains.annotations.NotNull;

/**
 * tool menu action
 *
 * @author zhangbyby
 */
public class BFCToolMenuAction extends AnAction {

    public BFCToolMenuAction() {
        super(Constants.PLUGIN_TITLE);
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
