package com.zhangbyby.bfc.ui;

import com.google.common.base.Strings;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.impl.source.PsiImmediateClassType;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.ui.components.JBList;
import com.intellij.ui.treeStructure.Tree;
import com.zhangbyby.bfc.common.Constants;
import com.zhangbyby.bfc.common.PsiClassUtils;
import com.zhangbyby.bfc.component.button.ClassChooserButtonListener;
import com.zhangbyby.bfc.component.dialog.BFCDialogWrapper;
import com.zhangbyby.bfc.component.item.FOPItemWrapper;
import com.zhangbyby.bfc.component.list.listener.JListItemClickListener;
import com.zhangbyby.bfc.component.list.listener.JListItemSelectionListener;
import com.zhangbyby.bfc.component.list.render.JListItemCellRenderer;
import com.zhangbyby.bfc.component.tree.listener.JBTreeSelectionListener;
import com.zhangbyby.bfc.component.tree.render.JBTreeItemCellRenderer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.*;

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
    private JList<FOPItemWrapper> sourceElements;
    private JList<FOPItemWrapper> targetElements;
    private JCheckBox disPropertyGroup;
    private Tree sourceTree;
    private Tree targetTree;
    private JScrollPane sourceTreePanel;
    private JScrollPane targetTreePanel;
    private JPanel treeMainPanel;

    public BFCMainUI(Project project) {
        initConstants();

        treeMainPanel.setVisible(false);
        this.project = project;

        Border sourceBorder = BorderFactory.createEtchedBorder();
        Border sourceTitledBorder = BorderFactory.createTitledBorder(sourceBorder, "<Source>");
        sourceClassElementsPanel.setBorder(sourceTitledBorder);

        Border targetBorder = BorderFactory.createEtchedBorder();
        Border targetTitledBorder = BorderFactory.createTitledBorder(targetBorder, "<Target>");
        targetClassElementsPanel.setBorder(targetTitledBorder);

        sourceTreePanel.setBorder(sourceTitledBorder);
        targetTreePanel.setBorder(targetTitledBorder);

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

        if (BFCDialogWrapper.sourcePsiType != null) {
            sourceClassQualifiedName.setText(PsiTypesUtil.getPsiClass(BFCDialogWrapper.sourcePsiType).getQualifiedName());
        }
        if (BFCDialogWrapper.targetPsiType != null) {
            targetClassQualifiedName.setText(PsiTypesUtil.getPsiClass(BFCDialogWrapper.targetPsiType).getQualifiedName());
        }
        reloadAllItem();
    }

    private void initConstants() {
        Constants.SOURCE_GENERICS.clear();
        Constants.TARGET_GENERICS.clear();
    }

    private void reloadAllItem() {
        resolveSourceGenericTypes();
        resolveTargetGenericTypes();

        reloadSingleList(sourceClassQualifiedName, sourceElements, false);
        reloadSingleList(targetClassQualifiedName, targetElements, true);

        reloadSingleTree(sourceClassQualifiedName, sourceTree, false);
        reloadSingleTree(targetClassQualifiedName, targetTree, true);
    }

    private void resolveTargetGenericTypes() {
        if (BFCDialogWrapper.sourcePsiType == null) {
            return;
        }
        resolveGenerics(Constants.SOURCE_GENERICS, BFCDialogWrapper.sourcePsiType);
    }

    private void resolveSourceGenericTypes() {
        if (BFCDialogWrapper.targetPsiType == null) {
            return;
        }
        resolveGenerics(Constants.TARGET_GENERICS, BFCDialogWrapper.targetPsiType);
    }

    private void resolveGenerics(Map<String, Map<String, String>> genericMap, PsiType psiType) {
        if (psiType.getCanonicalText().equals("java.lang.Object")) {
            return;
        }
        PsiClass psiClass = PsiTypesUtil.getPsiClass(psiType);
        if (!genericMap.containsKey(psiClass.getQualifiedName())) {
            getGeneric:
            if (((PsiClassType) psiType).hasParameters()) {
                Map<String, String> generics = new HashMap<>();
                PsiType[] actualGenericTypes;
                PsiTypeParameter[] declareGenericNames;

                if (psiType instanceof PsiClassReferenceType) {
                    actualGenericTypes = ((PsiClassReferenceType) psiType).getParameters();
                    declareGenericNames = ((PsiClassReferenceType) psiType).resolve().getTypeParameters();
                } else if (psiType instanceof PsiImmediateClassType) {
                    actualGenericTypes = ((PsiImmediateClassType) psiType).getParameters();
                    declareGenericNames = ((PsiImmediateClassType) psiType).resolve().getTypeParameters();
                } else {
                    System.err.println(psiType);
                    break getGeneric;
                }

                for (int i = 0; i < declareGenericNames.length; i++) {
                    PsiTypeParameter declareGenericName = declareGenericNames[i];
                    generics.put(declareGenericName.getName(),
                            actualGenericTypes[i].getCanonicalText() + "," + actualGenericTypes[i].getPresentableText());
                }
                genericMap.put(psiClass.getQualifiedName(), generics);
            } else {
                genericMap.put(psiClass.getQualifiedName(), Collections.emptyMap());
            }
        }
        PsiType[] superTypes = psiType.getSuperTypes();
        for (PsiType superType : superTypes) {
            resolveGenerics(genericMap, superType);
        }
    }

    private void reloadSingleTree(JTextField classNameText, Tree itemTree, boolean isTarget) {
        if (Strings.isNullOrEmpty(classNameText.getText())) {
            return;
        }

        TitledBorder border = (TitledBorder) (isTarget ? targetTreePanel.getBorder() : sourceTreePanel.getBorder());
        List<String> classNamePath = Arrays.asList(classNameText.getText().split("\\."));
        border.setTitle(classNamePath.get(classNamePath.size() - 1));

        PsiClass psiClass = JavaPsiFacade.getInstance(project).findClass(classNameText.getText(), GlobalSearchScope.allScope(project));
        if (psiClass == null) {
            classNameText.setText("");
            itemTree.setModel(null);
            return;
        }

        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        FOPItemWrapper[] wrappers = PsiClassUtils.filterElements(psiClass, this, isTarget);
        root.setAllowsChildren(wrappers.length > 0);
        for (FOPItemWrapper wrapper : wrappers) {
            root.add(new DefaultMutableTreeNode(wrapper, false));
        }
        DefaultTreeModel model = new DefaultTreeModel(root, true);
        itemTree.setModel(model);
    }

    private void reloadSingleList(JTextField classNameText, JList<FOPItemWrapper> itemList, boolean isTarget) {
        if (Strings.isNullOrEmpty(classNameText.getText())) {
            return;
        }

        TitledBorder border = (TitledBorder) (isTarget ? targetClassElementsPanel.getBorder() : sourceClassElementsPanel.getBorder());
        List<String> classNamePath = Arrays.asList(classNameText.getText().split("\\."));
        border.setTitle(classNamePath.get(classNamePath.size() - 1));

        PsiClass psiClass = JavaPsiFacade.getInstance(project).findClass(classNameText.getText(), GlobalSearchScope.allScope(project));
        if (psiClass == null) {
            classNameText.setText("");
            itemList.setListData(new FOPItemWrapper[0]);
            return;
        }

        FOPItemWrapper[] wrappers = PsiClassUtils.filterElements(psiClass, this, isTarget);
        itemList.setListData(wrappers);
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

        sourceTree = new Tree();
        targetTree = new Tree();

        sourceTree.setRootVisible(false);
        sourceTree.setCellRenderer(new JBTreeItemCellRenderer(targetTree, false));
        sourceTree.addTreeSelectionListener(new JBTreeSelectionListener(sourceTree, targetTree));

        targetTree.setRootVisible(false);
        targetTree.setCellRenderer(new JBTreeItemCellRenderer(sourceTree, true));
        targetTree.addTreeSelectionListener(new JBTreeSelectionListener(targetTree, sourceTree));

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