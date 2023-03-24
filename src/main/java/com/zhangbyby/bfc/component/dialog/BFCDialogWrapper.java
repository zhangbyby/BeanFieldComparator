package com.zhangbyby.bfc.component.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.zhangbyby.bfc.common.Constants;
import com.zhangbyby.bfc.ui.BFCMainUI;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * <B>MAIN</B>: dialog wrapper
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
        setTitle(Constants.PLUGIN_TITLE);
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return new BFCMainUI(project).getMainPanel();
    }
}