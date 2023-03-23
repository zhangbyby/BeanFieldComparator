package com.zhangbyby.bfc.ui;

import com.google.common.base.Strings;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.components.JBList;
import com.zhangbyby.bfc.component.button.ClassChooserButtonListener;
import com.zhangbyby.bfc.component.dialog.BFCDialogWrapper;
import com.zhangbyby.bfc.component.list.listener.JListItemClickListener;
import com.zhangbyby.bfc.component.list.listener.JListItemSelectionListener;
import com.zhangbyby.bfc.component.list.render.JListPsiFieldCellRenderer;
import com.zhangbyby.bfc.util.PsiClassUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.util.Arrays;
import java.util.List;

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
    private JPanel filterMenuPanel;
    private JCheckBox hideStatic;
    private JCheckBox hideFinal;
    /**
     * source: don't hide
     * traget: hide final
     */
    private JCheckBox autoHide;

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

        Border sourceBorder = BorderFactory.createEtchedBorder();
        Border sourceTitledBorder = BorderFactory.createTitledBorder(sourceBorder, "<Source>");
        sourceClassFieldsPanel.setBorder(sourceTitledBorder);

        Border targetBorder = BorderFactory.createEtchedBorder();
        Border targetTitledBorder = BorderFactory.createTitledBorder(targetBorder, "<Target>");
        targetClassFieldsPanel.setBorder(targetTitledBorder);

        hideStatic.addActionListener(e -> {
            autoHide.setSelected(false);
            reloadFields();
        });
        hideFinal.addActionListener(e -> {
            autoHide.setSelected(false);
            reloadFields();
        });
        autoHide.addActionListener(e -> {
            hideStatic.setSelected(false);
            hideFinal.setSelected(false);
            reloadFields();
        });

        if (BFCDialogWrapper.sourceClassName != null) {
            sourceClassQualifiedName.setText(BFCDialogWrapper.sourceClassName);
        }
        if (BFCDialogWrapper.targetClassName != null) {
            targetClassQualifiedName.setText(BFCDialogWrapper.targetClassName);
        }
        reloadFields();
    }

    private void reloadFields() {
        reloadFields(sourceClassQualifiedName, sourceFields, false);
        reloadFields(targetClassQualifiedName, targetFields, true);
    }

    private void reloadFields(JTextField classNameText, JList<PsiField> fieldJList, boolean isTarget) {
        if (Strings.isNullOrEmpty(classNameText.getText())) {
            return;
        }

        TitledBorder border = (TitledBorder) (isTarget ? targetClassFieldsPanel.getBorder() : sourceClassFieldsPanel.getBorder());
        List<String> classNamePath = Arrays.asList(classNameText.getText().split("\\."));
        border.setTitle(classNamePath.get(classNamePath.size() - 1));

        PsiClass psiClass = JavaPsiFacade.getInstance(project).findClass(classNameText.getText(), GlobalSearchScope.allScope(project));
        if (psiClass == null) {
            classNameText.setText("");
            fieldJList.setListData(new PsiField[]{});
            return;
        }
        fieldJList.setListData(PsiClassUtils.filterFields(psiClass.getFields(), hideStatic, hideFinal, autoHide, isTarget));
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
                new ClassChooserButtonListener(this, "SourceClass", sourceClassQualifiedName, sourceFields, targetFields, false));
        targetClassChooseButton.addActionListener(
                new ClassChooserButtonListener(this, "TargetClass", targetClassQualifiedName, targetFields, sourceFields, true));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public Project getProject() {
        return project;
    }

    public JCheckBox getHideStatic() {
        return hideStatic;
    }

    public JCheckBox getHideFinal() {
        return hideFinal;
    }

    public JCheckBox getAutoHide() {
        return autoHide;
    }
}