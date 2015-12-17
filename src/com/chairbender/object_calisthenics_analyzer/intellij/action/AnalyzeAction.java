package com.chairbender.object_calisthenics_analyzer.intellij.action;

import com.chairbender.object_calisthenics_analyzer.ObjectCalisthenicsAnalyzer;
import com.chairbender.object_calisthenics_analyzer.intellij.ui.ReportComponent;
import com.chairbender.object_calisthenics_analyzer.violation.ViolationMonitor;
import com.github.javaparser.ParseException;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.io.File;
import java.io.IOException;

/**
 * Action which runs the analysis and creates a report listing all the rule
 * violations.
 */
public class AnalyzeAction extends AnAction {

    public void actionPerformed(AnActionEvent anActionEvent) {
        final Project project = (Project) anActionEvent.getData(PlatformDataKeys.PROJECT);
        //Run a report, spit the text out into a tool window
        ProgressManager.getInstance().run(new Task.Modal(project, "Calisthenics Analyzer", false) {
            @Override
            public void run(ProgressIndicator indicator) {
                indicator.setIndeterminate(true);
                // do whatever you need to do
                ViolationMonitor analysis = null;
                try {
                    analysis = ObjectCalisthenicsAnalyzer.analyze(new File(project.getBasePath()), "UTF-8");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                final ViolationMonitor finalAnalysis = analysis;
                //create that tool window
                ApplicationManager.getApplication().invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
                        ToolWindow reportToolWindow = toolWindowManager.getToolWindow(Constants.REPORT_WINDOW_ID);
                        if (reportToolWindow == null) {
                            reportToolWindow = toolWindowManager.registerToolWindow(Constants.REPORT_WINDOW_ID, true, ToolWindowAnchor.BOTTOM);
                            reportToolWindow.setTitle("Calisthenics");
                        }

                        ContentManager reportWindowContentManager = reportToolWindow.getContentManager();
                        reportWindowContentManager.removeAllContents(true);
                        ReportComponent reportComponent = null;
                        try {
                            reportComponent = new ReportComponent(finalAnalysis, project);
                        } catch (BadLocationException e) {
                            e.printStackTrace();
                        }
                        Content reportContent = reportWindowContentManager.getFactory().createContent(reportComponent, "Report", false);
                        reportWindowContentManager.addContent(reportContent);
                        reportToolWindow.activate(null);
                    }
                });
            }
        });

    }
}
