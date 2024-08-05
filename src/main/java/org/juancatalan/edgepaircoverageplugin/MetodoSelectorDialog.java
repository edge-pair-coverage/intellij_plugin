package org.juancatalan.edgepaircoverageplugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.CheckboxTree;
import com.intellij.ui.CheckedTreeNode;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.tree.TreeUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.util.ArrayList;
import java.util.List;

public class MetodoSelectorDialog extends DialogWrapper {

    private final Project project;
    private final List<PsiMethod> selectedMethods = new ArrayList<>();
    private final CheckboxTree methodTree;

    public MetodoSelectorDialog(@Nullable Project project) {
        super(project);
        this.project = project;
        setTitle("Seleccionar los métodos para medir su cobertura");

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
                        getTextRenderer().append(method.getName());
                    } else if (userObject instanceof PsiClass) {
                        PsiClass psiClass = (PsiClass) userObject;
                        getTextRenderer().append(psiClass.getQualifiedName());
                    }
                }
            }
        }, rootNode);

        populateMethods(rootNode);

        methodTree.setModel(new DefaultTreeModel(rootNode));
        TreeUtil.expandAll(methodTree);
        init();
    }

    private void populateMethods(CheckedTreeNode rootNode) {
        JavaPsiFacade psiFacade = JavaPsiFacade.getInstance(project);
        // Usando projectScope para mejorar la performance y limitar la búsqueda solo al proyecto
        PsiClass[] classes = psiFacade.findClasses("", GlobalSearchScope.projectScope(project));
        System.out.println("Empiezo a buscar metodos");
        System.out.println("Encontradas " + classes.length);
        for (PsiClass psiClass : classes) {
            // Imprimir el nombre completo de la clase para depuración
            System.out.println(psiClass.getQualifiedName());
            // Crear un nodo para la clase
            CheckedTreeNode classNode = new CheckedTreeNode(psiClass);
            // Añadir métodos de la clase al nodo
            for (PsiMethod method : psiClass.getMethods()) {
                CheckedTreeNode methodNode = new CheckedTreeNode(method);
                classNode.add(methodNode);
            }
            // Solo añadir el nodo de la clase al árbol si tiene métodos
            if (classNode.getChildCount() > 0) {
                rootNode.add(classNode);
            }
        }
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return new JBScrollPane(methodTree);
    }

    @Override
    protected void doOKAction() {
        selectedMethods.clear();
        CheckedTreeNode root = (CheckedTreeNode) methodTree.getModel().getRoot();
        collectSelectedMethods(root);
        super.doOKAction();
    }

    private void collectSelectedMethods(CheckedTreeNode node) {
        if (node.isChecked() && node.getUserObject() instanceof PsiMethod) {
            selectedMethods.add((PsiMethod) node.getUserObject());
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            CheckedTreeNode childNode = (CheckedTreeNode) node.getChildAt(i);
            collectSelectedMethods(childNode);
        }
    }

    public List<String> getSelectedMethods() {
        List<String> methodNames = new ArrayList<>();
        for (PsiMethod method : selectedMethods) {
            // Devuelve el nombre calificado del método para garantizar unicidad
            methodNames.add(method.getContainingClass().getQualifiedName() + "." + method.getName());
        }
        return methodNames;
    }
}
