package org.juancatalan.edgepaircoverageplugin.dialogs;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiUtil;
import com.intellij.ui.CheckboxTree;
import com.intellij.ui.CheckedTreeNode;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.tree.TreeUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        init();
    }

    private void uncheckAllNodes(CheckedTreeNode node) {
        node.setChecked(false);
        Enumeration children = node.children();
        while (children.hasMoreElements()) {
            Object child = children.nextElement();
            if (child instanceof CheckedTreeNode) {
                uncheckAllNodes((CheckedTreeNode) child);
            }
        }
    }

    public static void populateMethods(CheckedTreeNode rootNode, Project project) {
        // Obtener todos los archivos Java en el proyecto
        FileType javaFileType = FileTypeManager.getInstance().getFileTypeByExtension("java");
        Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(javaFileType, GlobalSearchScope.projectScope(project));

        System.out.println("Empiezo a buscar métodos");

        // Iterar sobre cada archivo Java
        for (VirtualFile virtualFile : virtualFiles) {
            PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);

            if (psiFile instanceof PsiJavaFile) {
                // Usar un visitor para recorrer las clases y métodos
                psiFile.accept(new PsiRecursiveElementWalkingVisitor() {
                    @Override
                    public void visitElement(PsiElement element) {
                        super.visitElement(element);

                        if (element instanceof PsiClass) {
                            PsiClass psiClass = (PsiClass) element;
                            // Imprimir el nombre completo de la clase para depuración
                            //System.out.println("Clase encontrada: " + psiClass.getQualifiedName());

                            // Crear un nodo para la clase
                            CheckedTreeNode classNode = new CheckedTreeNode(psiClass);

                            // Añadir métodos de la clase al nodo
                            for (PsiMethod method : psiClass.getMethods()) {
                                CheckedTreeNode methodNode = new CheckedTreeNode(method);
                                classNode.add(methodNode);
                                //System.out.println("Método añadido: " + getFullMethodName(method));
                            }

                            // Solo añadir el nodo de la clase al árbol si tiene métodos
                            if (classNode.getChildCount() > 0) {
                                rootNode.add(classNode);
                            }
                        }
                    }
                });
            }
        }
    }

    private static String getFullMethodName(PsiMethod method) {
        PsiClass containingClass = method.getContainingClass();
        if (containingClass == null) {
            return method.getName(); // Fallback
        }

        // Obtener el nombre completo de la clase
        String className = containingClass.getQualifiedName();
        if (className == null) {
            return method.getName(); // Fallback
        }

        // Obtener los tipos de los parámetros del método
        StringBuilder parameterTypes = new StringBuilder();
        for (PsiParameter parameter : method.getParameterList().getParameters()) {
            parameterTypes.append(getTypeDescriptor(parameter.getType()));
        }

        // Obtener el tipo de retorno
        String returnTypeDescriptor = getTypeDescriptor(method.getReturnType());

        // Construir el nombre completo del método
        return className.replace('.', '/') + "." + method.getName() + ".(" + parameterTypes + ")" + returnTypeDescriptor;
    }

    private static String getTypeDescriptor(PsiType type) {
        if (type == null) {
            return "V"; // Void descriptor
        }

        if (type instanceof PsiArrayType) {
            return "[" + getTypeDescriptor(((PsiArrayType) type).getComponentType());
        }

        if (type instanceof PsiPrimitiveType) {
            // Mapear tipos primitivos a descriptores de bytecode
            switch (type.getCanonicalText()) {
                case "int": return "I";
                case "boolean": return "Z";
                case "byte": return "B";
                case "char": return "C";
                case "short": return "S";
                case "double": return "D";
                case "float": return "F";
                case "long": return "J";
                case "void": return "V";
                default: throw new IllegalArgumentException("Unknown primitive type: " + type.getCanonicalText());
            }
        }

        // Para tipos no primitivos (clases)
        PsiClass resolvedClass = PsiUtil.resolveClassInType(type);
        if (resolvedClass != null) {
            return "L" + resolvedClass.getQualifiedName().replace('.', '/') + ";";
        }

        return "L" + type.getCanonicalText().replace('.', '/') + ";"; // Default for non-primitive types
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
        if (!selectedMethods.isEmpty()) {
            SituacionesImposiblesSelectorDialog situacionesImposiblesSelectorDialog = new SituacionesImposiblesSelectorDialog(selectedMethods);
            if (situacionesImposiblesSelectorDialog.showAndGet()) {
                List<Integer> selectedNumbers = situacionesImposiblesSelectorDialog.getSelectedNumbers();
                // Aquí puedes manejar los números seleccionados por el usuario
                System.out.println("Números seleccionados para cada método:");
                for (int i = 0; i < selectedMethods.size(); i++) {
                    System.out.println(selectedMethods.get(i).getName() + ": " + selectedNumbers.get(i));
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "No hay métodos seleccionados.");
        }

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
            methodNames.add(getFullMethodName(method));
        }
        return methodNames;
    }
}
