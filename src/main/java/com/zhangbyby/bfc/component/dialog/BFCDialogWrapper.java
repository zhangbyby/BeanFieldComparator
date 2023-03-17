package com.zhangbyby.bfc.component.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.zhangbyby.bfc.ui.BFCMainUI;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.zhangbyby.bfc.action.BFCToolMenuAction.PLUGIN_TITLE;

/**
 * 插件弹窗包装
 *
 * @author zhangbyby
 */
public class BFCDialogWrapper extends DialogWrapper {
    private final Project project;
    public static String sourceClassName;
    public static String targetClassName;

    public BFCDialogWrapper(Project project) {
        super(true);
        this.project = project;
        setTitle(PLUGIN_TITLE);
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return new BFCMainUI(project).getMainPanel();
    }
}