package org.juancatalan.edgepaircoverageplugin.utils;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;

public class PsiMethodToFullMethodName {
    static public String transform(PsiMethod method){
        return getFullMethodName(method);
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
}
