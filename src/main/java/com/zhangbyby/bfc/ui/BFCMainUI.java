package com.zhangbyby.bfc.ui;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.components.JBList;
import com.zhangbyby.bfc.component.button.ClassChooserButtonListener;
import com.zhangbyby.bfc.component.list.listener.JListItemClickListener;
import com.zhangbyby.bfc.component.list.listener.JListItemSelectionListener;
import com.zhangbyby.bfc.component.list.render.JListPsiFieldCellRenderer;
import com.zhangbyby.bfc.util.PsiClassUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

/**
 * 界面设计
 *
 * @author zhangbyby
 */
public class BFCMainUI {
    private final static Logger log = Logger.getLogger(BFCMainUI.class.getSimpleName());

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
    private JPanel filterMenuPanel;
    private JCheckBox hideStatic;
    private JCheckBox hideFinal;

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
        hideStatic.addActionListener(this::reloadFields);
        hideFinal.addActionListener(this::reloadFields);
    }

    private void reloadFields(ActionEvent e) {
        reloadFields(sourceClassQualifiedName, sourceFields);
        reloadFields(targetClassQualifiedName, targetFields);
    }

    private void reloadFields(JTextField classNameText, JList<PsiField> fieldJList) {
        PsiClass psiClass = JavaPsiFacade.getInstance(project).findClass(classNameText.getText(), GlobalSearchScope.allScope(project));
        if (psiClass == null) {
            classNameText.setText("");
            fieldJList.setListData(new PsiField[]{});
            return;
        }
        fieldJList.setListData(PsiClassUtils.filterFields(psiClass.getFields(), hideStatic, hideFinal));
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
                new ClassChooserButtonListener(this, "SourceClass", sourceClassQualifiedName, sourceFields, targetFields));
        targetClassChooseButton.addActionListener(
                new ClassChooserButtonListener(this, "TargetClass", targetClassQualifiedName, targetFields, sourceFields));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public Project getProject() {
        return project;
    }

    public JPanel getSourceClass() {
        return sourceClass;
    }

    public JPanel getTargetClass() {
        return targetClass;
    }

    public JPanel getComparePanel() {
        return comparePanel;
    }

    public JScrollPane getSourceClassFieldsPanel() {
        return sourceClassFieldsPanel;
    }

    public JScrollPane getTargetClassFieldsPanel() {
        return targetClassFieldsPanel;
    }

    public JPanel getFilterMenuPanel() {
        return filterMenuPanel;
    }

    public JCheckBox getHideStatic() {
        return hideStatic;
    }

    public JCheckBox getHideFinal() {
        return hideFinal;
    }

    public JTextField getSourceClassQualifiedName() {
        return sourceClassQualifiedName;
    }

    public JButton getSourceClassChooseButton() {
        return sourceClassChooseButton;
    }

    public JTextField getTargetClassQualifiedName() {
        return targetClassQualifiedName;
    }

    public JButton getTargetClassChooseButton() {
        return targetClassChooseButton;
    }

    public JList<PsiField> getSourceFields() {
        return sourceFields;
    }

    public JList<PsiField> getTargetFields() {
        return targetFields;
    }
}