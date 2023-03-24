package com.zhangbyby.bfc.ui;

import com.google.common.base.Strings;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.components.JBList;
import com.zhangbyby.bfc.common.PsiClassUtils;
import com.zhangbyby.bfc.component.button.ClassChooserButtonListener;
import com.zhangbyby.bfc.component.dialog.BFCDialogWrapper;
import com.zhangbyby.bfc.component.list.item.JListItemWrapper;
import com.zhangbyby.bfc.component.list.listener.JListItemClickListener;
import com.zhangbyby.bfc.component.list.listener.JListItemSelectionListener;
import com.zhangbyby.bfc.component.list.render.JListItemCellRenderer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.util.Arrays;
import java.util.List;

/**
 * main ui
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
    private JScrollPane sourceClassElementsPanel;
    private JScrollPane targetClassElementsPanel;
    private JPanel filterMenuPanel;
    private JCheckBox hideStatic;
    private JCheckBox hideFinal;
    /**
     * source: don't hide
     * target: hide final
     */
    private JCheckBox autoHide;

    /**
     * customer
     */
    private JTextField sourceClassQualifiedName;
    private JButton sourceClassChooseButton;
    private JTextField targetClassQualifiedName;
    private JButton targetClassChooseButton;
    private JList<JListItemWrapper> sourceElements;
    private JList<JListItemWrapper> targetElements;
    private JCheckBox disPropertyGroup;

    public BFCMainUI(Project project) {
        this.project = project;

        Border sourceBorder = BorderFactory.createEtchedBorder();
        Border sourceTitledBorder = BorderFactory.createTitledBorder(sourceBorder, "<Source>");
        sourceClassElementsPanel.setBorder(sourceTitledBorder);

        Border targetBorder = BorderFactory.createEtchedBorder();
        Border targetTitledBorder = BorderFactory.createTitledBorder(targetBorder, "<Target>");
        targetClassElementsPanel.setBorder(targetTitledBorder);

        disPropertyGroup.addActionListener(e -> {
            if (disPropertyGroup.isSelected()) {
                hideStatic.setEnabled(true);
                hideFinal.setEnabled(true);
                autoHide.setEnabled(true);
            } else {
                hideStatic.setEnabled(false);
                hideFinal.setEnabled(false);
                autoHide.setEnabled(false);
            }
            reloadAllItem();
        });

        hideStatic.setEnabled(false);
        hideFinal.setEnabled(false);
        autoHide.setEnabled(false);

        hideStatic.addActionListener(e -> {
            autoHide.setSelected(false);
            reloadAllItem();
        });
        hideFinal.addActionListener(e -> {
            autoHide.setSelected(false);
            reloadAllItem();
        });
        autoHide.addActionListener(e -> {
            hideStatic.setSelected(false);
            hideFinal.setSelected(false);
            reloadAllItem();
        });

        if (BFCDialogWrapper.sourceClassName != null) {
            sourceClassQualifiedName.setText(BFCDialogWrapper.sourceClassName);
        }
        if (BFCDialogWrapper.targetClassName != null) {
            targetClassQualifiedName.setText(BFCDialogWrapper.targetClassName);
        }
        reloadAllItem();
    }

    private void reloadAllItem() {
        reloadSingleItem(sourceClassQualifiedName, sourceElements, false);
        reloadSingleItem(targetClassQualifiedName, targetElements, true);
    }

    private void reloadSingleItem(JTextField classNameText, JList<JListItemWrapper> itemList, boolean isTarget) {
        if (Strings.isNullOrEmpty(classNameText.getText())) {
            return;
        }

        TitledBorder border = (TitledBorder) (isTarget ? targetClassElementsPanel.getBorder() : sourceClassElementsPanel.getBorder());
        List<String> classNamePath = Arrays.asList(classNameText.getText().split("\\."));
        border.setTitle(classNamePath.get(classNamePath.size() - 1));

        PsiClass psiClass = JavaPsiFacade.getInstance(project).findClass(classNameText.getText(), GlobalSearchScope.allScope(project));
        if (psiClass == null) {
            classNameText.setText("");
            itemList.setListData(new JListItemWrapper[0]);
            return;
        }
        itemList.setListData(PsiClassUtils.filterElements(psiClass, this, isTarget));
    }

    private void createUIComponents() {
        sourceClassChooseButton = new JButton();
        targetClassChooseButton = new JButton();

        sourceClassQualifiedName = new JTextField();
        sourceClassQualifiedName.setToolTipText("SourceClassQualifiedName");
        targetClassQualifiedName = new JTextField();
        targetClassQualifiedName.setToolTipText("TargetClassQualifiedName");

        sourceElements = new JBList<>();
        targetElements = new JBList<>();

        sourceElements.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        sourceElements.setCellRenderer(new JListItemCellRenderer(targetElements, false));
        sourceElements.addMouseListener(new JListItemClickListener(sourceElements, false));
        sourceElements.addListSelectionListener(new JListItemSelectionListener(sourceElements, targetElements));

        targetElements.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        targetElements.setCellRenderer(new JListItemCellRenderer(sourceElements, true));
        targetElements.addMouseListener(new JListItemClickListener(targetElements, true));
        targetElements.addListSelectionListener(new JListItemSelectionListener(targetElements, sourceElements));

        sourceClassChooseButton.addActionListener(
                new ClassChooserButtonListener(this, "SourceClass", sourceClassQualifiedName, sourceElements, targetElements, false));
        targetClassChooseButton.addActionListener(
                new ClassChooserButtonListener(this, "TargetClass", targetClassQualifiedName, targetElements, sourceElements, true));
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

    public JCheckBox getDisPropertyGroup() {
        return disPropertyGroup;
    }
}