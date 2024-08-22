package org.juancatalan.edgepaircoverageplugin.ui.panels;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.CheckboxTree;
import com.intellij.ui.CheckedTreeNode;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.tree.TreeUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MetodoSelectorPanel extends JPanel {

    private final Project project;
    private final List<PsiMethod> selectedMethods = new ArrayList<>();
    private final CheckboxTree methodTree;

    public MetodoSelectorPanel(@Nullable Project project) {
        this.project = project;
        setLayout(new BorderLayout());

        // Crea el árbol de métodos
        CheckedTreeNode rootNode = new CheckedTreeNode("Métodos");
        methodTree = new CheckboxTree(new CheckboxTree.CheckboxTreeCellRenderer() {
            @Override
            public void customizeRenderer(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                if (value instanceof CheckedTreeNode) {
                    CheckedTreeNode node = (CheckedTreeNode) value;
                    Object userObject = node.getUserObject();
                    if (userObject instanceof PsiMethod) {
                        PsiMethod method = (PsiMethod) userObject;
                        String methodName = method.getName();
                        String parameters = Stream.of(method.getParameterList().getParameters())
                                .map(param -> param.getType().getPresentableText() + " " + param.getName())
                                .collect(Collectors.joining(", "));
                        getTextRenderer().append(methodName + "(" + parameters + ")", SimpleTextAttributes.REGULAR_ATTRIBUTES);
                        getTextRenderer().setIcon(AllIcons.Nodes.Method);

                    } else if (userObject instanceof PsiClass) {
                        PsiClass psiClass = (PsiClass) userObject;
                        getTextRenderer().append(psiClass.getQualifiedName(), SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
                        getTextRenderer().setIcon(AllIcons.Nodes.Class);
                    }
                }
            }
        }, rootNode);

        populateMethods(rootNode, project);

        methodTree.setModel(new DefaultTreeModel(rootNode));
        uncheckAllNodes(rootNode);
        TreeUtil.expandAll(methodTree);

        add(new JBScrollPane(methodTree), BorderLayout.CENTER);
    }

    private void uncheckAllNodes(CheckedTreeNode node) {
        node.setChecked(false);
        Enumeration<?> children = node.children();
        while (children.hasMoreElements()) {
            Object child = children.nextElement();
            if (child instanceof CheckedTreeNode) {
                uncheckAllNodes((CheckedTreeNode) child);
            }
        }
    }

    public static void populateMethods(CheckedTreeNode rootNode, Project project) {
        FileType javaFileType = FileTypeManager.getInstance().getFileTypeByExtension("java");
        Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(javaFileType, GlobalSearchScope.projectScope(project));

        for (VirtualFile virtualFile : virtualFiles) {
            // Verificar si el archivo está en un directorio de prueba
            if (isInTestDirectory(virtualFile)) {
                continue; // Si está en un directorio de prueba, omitir este archivo
            }

            PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);

            if (psiFile instanceof PsiJavaFile) {
                psiFile.accept(new PsiRecursiveElementWalkingVisitor() {
                    @Override
                    public void visitElement(PsiElement element) {
                        super.visitElement(element);

                        if (element instanceof PsiClass) {
                            PsiClass psiClass = (PsiClass) element;

                            // Comprobar que no sea una clase anonima
                            if (psiClass.getQualifiedName() != null){
                                CheckedTreeNode classNode = new CheckedTreeNode(psiClass);

                                for (PsiMethod method : psiClass.getMethods()) {
                                    CheckedTreeNode methodNode = new CheckedTreeNode(method);
                                    classNode.add(methodNode);
                                }

                                if (classNode.getChildCount() > 0) {
                                    rootNode.add(classNode);
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    private static boolean isInTestDirectory(VirtualFile file) {
        // Obtén el directorio padre del archivo
        VirtualFile parent = file.getParent();

        // Recorre hacia arriba para encontrar el directorio raíz del proyecto
        while (parent != null && !parent.getName().equals("src")) {
            if (parent.getName().equals("test")) return true;
            parent = parent.getParent();
        }
        return false;
    }

    public List<PsiMethod> getSelectedMethodsList() {
        List<PsiMethod> methods = new ArrayList<>();
        CheckedTreeNode root = (CheckedTreeNode) methodTree.getModel().getRoot();
        collectSelectedMethods(root, methods);
        return methods;
    }

    private void collectSelectedMethods(CheckedTreeNode node, List<PsiMethod> methods) {
        if (node.isChecked() && node.getUserObject() instanceof PsiMethod) {
            methods.add((PsiMethod) node.getUserObject());
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            CheckedTreeNode childNode = (CheckedTreeNode) node.getChildAt(i);
            collectSelectedMethods(childNode, methods);
        }
    }
}


