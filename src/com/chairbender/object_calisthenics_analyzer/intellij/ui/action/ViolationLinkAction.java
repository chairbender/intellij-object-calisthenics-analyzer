package com.chairbender.object_calisthenics_analyzer.intellij.ui.action;

import com.chairbender.object_calisthenics_analyzer.violation.Violation;
import com.github.javaparser.ast.Node;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;

/**
 * An action for navigating to a specific location in a project's
 * Java source code
 */
public class ViolationLinkAction {
    private Violation violation;
    private Project project;

    /**
     *
     * @param violation violation to link to
     * @param inProject project the error occurred in
     */
    public ViolationLinkAction(Violation violation, Project inProject) {
        super();
        this.violation = violation;
        this.project = inProject;
    }

    /**
     * navigates to the violation. Doesn't do it if there was a problem locating the class.
     */
    public void goToViolation() {
        //open the file indicated by the node
        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        String className = violation.getFullyQualifiedViolationClass();
        if (className != null) {
            PsiClass violatingClass = javaPsiFacade.findClass(className, GlobalSearchScope.projectScope(project));
            PsiFile violatingFile = violatingClass.getContainingFile();
            Node violatingNode = violation.getViolationLocation();
            OpenFileDescriptor descriptor = new OpenFileDescriptor(project, violatingFile.getVirtualFile(), violatingNode.getBeginLine() - 1, violatingNode.getBeginColumn());
            FileEditorManager.getInstance(project).openTextEditor(descriptor, true);

        }
    }
}
