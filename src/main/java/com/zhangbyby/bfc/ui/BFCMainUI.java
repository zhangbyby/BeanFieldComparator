package com.zhangbyby.bfc.ui;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiField;
import com.intellij.ui.components.JBList;
import com.zhangbyby.bfc.component.button.ClassChooserButtonListener;
import com.zhangbyby.bfc.component.list.listener.JListItemClickListener;
import com.zhangbyby.bfc.component.list.listener.JListItemSelectionListener;
import com.zhangbyby.bfc.component.list.render.JListPsiFieldCellRenderer;

import javax.swing.*;

/**
 * 界面设计
 *
 * @author zhangbyby
 */
public class BFCMainUI {
    private final Project project;

    /**
     * generated
     */
    private JPanel mainPanel;
    private JPanel sourceClass;
    private JPanel targetClass;
    private JPanel comparePanel;
    private JScrollPane sourceClassFieldsPanel;
    private JScrollPane targetClassFieldsPanel;

    /**
     * customer
     */
    private JTextField sourceClassQualifiedName;
    private JButton sourceClassChooseButton;
    private JTextField targetClassQualifiedName;
    private JButton targetClassChooseButton;
    private JList<PsiField> sourceFields;
    private JList<PsiField> targetFields;

    public BFCMainUI(Project project) {
        this.project = project;
    }

    private void createUIComponents() {
        sourceClassChooseButton = new JButton();
        targetClassChooseButton = new JButton();

        sourceClassQualifiedName = new JTextField();
        sourceClassQualifiedName.setToolTipText("SourceClassQualifiedName");
        targetClassQualifiedName = new JTextField();
        targetClassQualifiedName.setToolTipText("TargetClassQualifiedName");

        sourceFields = new JBList<>();
        targetFields = new JBList<>();

        sourceFields.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        sourceFields.setCellRenderer(new JListPsiFieldCellRenderer(targetFields));
        sourceFields.addMouseListener(new JListItemClickListener(sourceFields));
        sourceFields.addListSelectionListener(new JListItemSelectionListener(sourceFields, targetFields));

        targetFields.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        targetFields.setCellRenderer(new JListPsiFieldCellRenderer(sourceFields));
        targetFields.addMouseListener(new JListItemClickListener(targetFields));
        targetFields.addListSelectionListener(new JListItemSelectionListener(targetFields, sourceFields));

        sourceClassChooseButton.addActionListener(
                new ClassChooserButtonListener(project, "SourceClass", sourceClassQualifiedName, sourceFields, targetFields));
        targetClassChooseButton.addActionListener(
                new ClassChooserButtonListener(project, "TargetClass", targetClassQualifiedName, targetFields, sourceFields));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}