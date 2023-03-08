package com.zhangbyby.bfc;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.zhangbyby.bfc.component.dialog.BFCDialogWrapper;
import org.jetbrains.annotations.NotNull;

/**
 * 插件入口
 *
 * @author zhangbyby
 */
public class BFCAction extends AnAction {
    public static final String PLUGIN_TITLE = "BeanFieldComparator";

    public BFCAction() {
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